package org.seasar.ymir.extension.zpt;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.FlexibleList;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerContext extends ZptTemplateContext {
    private static final String MULTIPLE_SUFFIX = "ies";

    private static final String SINGULAR_SUFFIX = "y";

    private static final String MULTIPLE_SUFFIX2 = "s";

    private static final String SINGULAR_SUFFIX2 = "";

    static final char CHAR_ARRAY_LPAREN = '[';

    static final char CHAR_ARRAY_RPAREN = ']';

    private static final String PROP_LENGTH = "length";

    private static final String PROP_SIZE = "size";

    private SourceCreator sourceCreator_;

    private HttpMethod method_;

    private Map<String, ClassDesc> classDescMap_;

    private String pageClassName_;

    private FormDesc formDesc_;

    private boolean repeatedPropertyGeneratedAsList_;

    private VariableResolver variableResolver_;

    private Set<String> usedClassNameSet_ = new HashSet<String>();

    private Set<String> ignoreVariableSet_ = new HashSet<String>();

    private String path_;

    private ClassCreationHintBag hintBag_;

    private Stack<Map<String, String>> variableExpressions_ = new Stack<Map<String, String>>();

    private Map<String, String> globalVariableExpression_ = new HashMap<String, String>();

    private int repeatDepth_;

    @Override
    public VariableResolver getVariableResolver() {
        if (variableResolver_ == null) {
            variableResolver_ = new AnalyzerVariableResolver(super
                    .getVariableResolver());
        }
        return variableResolver_;
    }

    @Override
    public void setVariableResolver(VariableResolver varResolver) {
        super.setVariableResolver(varResolver);
        variableResolver_ = null;
    }

    public void setIgnoreVariables(String[] ignoreVariables) {
        if (ignoreVariables == null) {
            ignoreVariableSet_.clear();
        } else {
            ignoreVariableSet_.addAll(Arrays.asList(ignoreVariables));
        }
    }

    public boolean shouldIgnoreVariable(String name) {
        return ignoreVariableSet_.contains(name);
    }

    @Override
    public void defineVariable(int scope, String name, Object value) {
        if (value instanceof DescWrapper) {
            ((DescWrapper) value).setVariableName(name);
        }
        super.defineVariable(scope, name, value);
    }

    @Override
    public RepeatInfo pushRepeatInfo(String name, Object[] objs) {
        repeatDepth_++;
        if (objs != null && objs.length == 1 && objs[0] instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) objs[0];
            wrapper.setVariableName(name);
            ClassDesc valueClassDesc;
            PropertyDesc pd = wrapper.getPropertyDesc();
            if (pd != null) {
                TypeDesc td = pd.getTypeDesc();
                valueClassDesc = td.getComponentClassDesc();

                if (!td.isExplicit()) {
                    if (repeatedPropertyGeneratedAsList_) {
                        // List型に補正する。
                        changeToCollection(td, List.class.getName());
                    } else {
                        // 配列型に補正する。
                        changeToCollection(td, null);
                    }
                }
            } else {
                valueClassDesc = wrapper.getValueClassDesc();
            }
            objs[0] = new DescWrapper(this, valueClassDesc);
            setUsedClassName(valueClassDesc.getName());
        }

        return super.pushRepeatInfo(name, objs);
    }

    /**
     * 指定されたクラスの指定されたプロパティ名に対応するPropertyDescオブジェクトを返します。
     * <p>プロパティの型情報なども適切に設定されたPropertyDescオブジェクトが返されます。
     * </p>
     * 
     * @param classDesc プロパティが属するクラスの情報を表すClassDescオブジェクト。nullを指定してはいけません。
     * @param propertyName プロパティ名。
     * @param mode プロパティのモード。
     * @param propertyTypeAlias クラス名を生成する際の元になる名前。
     * 通常クラス名はプロパティ名から生成されますが、この値としてnullでない値を指定した場合、
     * プロパティ名よりもこちらが優先されます。
     * @param asCollection trueの場合、プロパティがコレクション型であることを表します。
     * falseの場合、プロパティがコレクション型かどうか判断できていないことを表します。
     * @param collectionClassName コレクション型のクラス名です。nullの場合は配列型であることを表します。
     * この引数はasCollectionがtrueの時のみ有効です。
     * @param force 強制的にプロパティ型を再推論するかどうか。
     * trueの場合、明示的に型が設定されていない限り強制的にプロパティ型を再推論します。
     * @return PropertyDescオブジェクト。nullが返されることはありません。
     */
    public PropertyDesc addProperty(ClassDesc classDesc, String propertyName,
            int mode, String propertyTypeAlias, boolean asCollection,
            String collectionClassName, boolean force) {
        PropertyDesc propertyDesc = classDesc.addProperty(propertyName, mode);
        if (propertyDesc.getTypeDesc().isExplicit()) {
            return propertyDesc;
        }
        if (!force && propertyDesc.isTypeAlreadySet()) {
            return propertyDesc;
        }

        TypeDesc typeDesc;
        boolean shouldNotifyUpdated;

        // プロパティ型のヒント情報を見る。
        // ヒントがなければ、実際の親クラスからプロパティ型を取得する。
        String className = classDesc.getName();
        PropertyTypeHint hint = getPropertyTypeHint(className, propertyName);
        if (hint != null) {
            typeDesc = classDesc.getDescPool().newTypeDesc(hint.getTypeName());
            typeDesc.setExplicit(true);
            // 型が明示的に指定されており確定なので、PropertyDesc#notifyTypeUpdated()を呼ぶようにする。
            shouldNotifyUpdated = true;
        } else {
            PropertyDescriptor descriptor = sourceCreator_
                    .getPropertyDescriptor(className, propertyName);
            if (descriptor != null) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod != null) {
                    propertyDesc.setGetterName(readMethod.getName());
                }

                // typeのexplicitは、プロパティの所属クラスが自動生成対象かによって変わる。
                // 自動生成対象でないクラスについては、プロパティの型は不変なのでtrueにすべき。
                // 自動生成クラスについては、プロパティ型はHTMLテンプレートの内容によって変わり得るのでfalseにすべき。
                boolean explicit = isOuter(className);

                String componentClassName;
                Class<?> componentClass = sourceCreator_.getClass(DescUtils
                        .getComponentPropertyTypeName(descriptor));
                if (componentClass.isInterface()) {
                    // インタフェースの場合は実装クラス型を推論する。
                    componentClassName = findPropertyClassName(
                            propertyTypeAlias != null ? propertyTypeAlias
                                    : Introspector.decapitalize(ClassUtils
                                            .getShorterName(componentClass)),
                            className);

                    // クラスが実在する場合であっても、プロパティ型がインタフェース型の場合は
                    // その実装型が自動生成対象クラスであるならばrepeat変数名からの推論型などに
                    // 変更されうる。
                    explicit = isOuter(componentClassName);

                    // propertyTypeAliasが指定されていない場合は、後でpropertyTypeAliasが
                    // 指定されて型が変更されることがあるため、PropertyDesc#notifyTypeUpdated()を
                    // 呼んではいけない。
                    shouldNotifyUpdated = explicit || propertyTypeAlias != null;

                    setUsedClassName(componentClassName);
                } else {
                    // そうでない場合は実際の型をそのまま使う。
                    componentClassName = componentClass.getName();

                    // 自動生成対象のクラスの場合はtal:conditionやtal:attributesなどでbooleanだとみなされる余地があるので
                    // PropertyDesc#notifyTypeUpdated()を呼ばないようにする。
                    shouldNotifyUpdated = isOuter(componentClassName);
                }

                String typeName = DescUtils
                        .getGenericPropertyTypeName(descriptor);
                if (Collection.class.isAssignableFrom(descriptor
                        .getPropertyType())) {
                    TypeToken typeToken = new TypeToken(typeName);
                    typeToken.getTypes()[0].setBaseName(componentClassName);
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            typeToken.getAsString());
                } else if (descriptor.getPropertyType().isArray()) {
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            componentClassName + "[]");
                } else {
                    typeDesc = classDesc.getDescPool().newTypeDesc(
                            componentClassName);
                }
                typeDesc.setExplicit(explicit);
            } else {
                // 推論できなかった場合は名前から推論を行なう。
                typeDesc = classDesc
                        .getDescPool()
                        .newTypeDesc(
                                inferPropertyClassName(
                                        propertyTypeAlias != null ? propertyTypeAlias
                                                : asCollection ? toSingular(propertyName)
                                                        : propertyName,
                                        className));
                if (!propertyDesc.getTypeDesc().isCollection() && asCollection) {
                    typeDesc.setCollection(true);
                    typeDesc.setCollectionClassName(collectionClassName);
                }

                // propertyTypeAliasが指定されていない場合は、後でpropertyTypeAliasが
                // 指定されて型が変更されることがあるため、PropertyDesc#notifyTypeUpdated()を
                // 呼んではいけない。
                shouldNotifyUpdated = propertyTypeAlias != null;
            }
        }

        if (typeDesc.isCollection()
                && List.class.getName().equals(
                        typeDesc.getCollectionClassName())) {
            typeDesc.setCollectionImplementationClassName(propertyDesc
                    .getTypeDesc().getCollectionImplementationClassName());
        }

        propertyDesc.setTypeDesc(typeDesc);
        if (shouldNotifyUpdated) {
            propertyDesc.notifyTypeUpdated();
        }

        return propertyDesc;
    }

    public PropertyDesc addProperty(ClassDesc classDesc, String propertyName,
            int mode) {
        return addProperty(classDesc, propertyName, mode, null, false, null,
                false);
    }

    /**
     * 指定された名前のプロパティの型を推論して返します。
     * <p>型推論はまず基準となるクラス名を使ってDTO型名を生成し、
     * そのクラスが存在するかを確認します。
     * 存在する場合はそれが推論結果となります。
     * 存在しない場合はDTOサーチパス上のクラスから名前に基づいて推論します。
     * DTOサーチパス上のクラスから対応するクラスが見つかった場合はそれが推論結果となります。
     * 見つからなかった場合は最初に生成したDTO型名が推論結果となります。
     * </p>
     * 
     * @param propertyName プロパティ名。nullを指定してはいけません。
     * @param baseClassName 基準となるクラス名。
     * パッケージがアプリケーションのルートパッケージ配下であるようなクラス名を指定する場合は、
     * クラス名は「ルートパッケージ＋"."+種別パッケージ」配下である必要があります
     * （種別パッケージ：web、dtoなど）。
     * パッケージがアプリケーションのルートパッケージと異なるようなクラス名が指定された場合は
     * 現在のページクラス名が使用されます。
     * また、nullが指定された場合も現在のページクラス名が使用されます。
     * @return 推論結果の型。nullが返されることはありません。
     * またGenerics型が返されることはありません。
     */
    public String inferPropertyClassName(String propertyName,
            String baseClassName) {

        String className = findPropertyClassName(propertyName, baseClassName);
        if (sourceCreator_.getClass(className) != null) {
            return className;
        }

        // DTO型名に対応するクラスが存在しない場合はDTOサーチパス上のクラスから推論する。

        String classNameFromSearchPath = sourceCreator_
                .getSourceCreatorSetting().findDtoClassName(propertyName);
        if (classNameFromSearchPath != null) {
            className = classNameFromSearchPath;
        }

        return className;
    }

    /**
     * 指定された名前のプロパティの型を推論して返します。
     * <p>型推論は基準となるクラス名を使って行なわれます。
     * クラスがサブパッケージ階層に配置されている場合、
     * 同一のサブパッケージ階層に配置されるようなDTO型名が返されますが、
     * 上位階層に同一名のDTO型が存在する場合はそれが返されます。
     * </p>
     * 
     * @param propertyName プロパティ名。nullを指定してはいけません。
     * @param baseClassName 基準となるクラス名。
     * パッケージがアプリケーションのルートパッケージ配下であるようなクラス名を指定する場合は、
     * クラス名は「ルートパッケージ＋"."+種別パッケージ」配下である必要があります
     * （種別パッケージ：web、dtoなど）。
     * パッケージがアプリケーションのルートパッケージと異なるようなクラス名が指定された場合は
     * 現在のページクラス名が使用されます。
     * また、nullが指定された場合も現在のページクラス名が使用されます。
     * @return 推論結果の型。nullが返されることはありません。
     * またGenerics型が返されることはありません。
     */
    public String findPropertyClassName(String propertyName,
            String baseClassName) {
        if (baseClassName == null) {
            baseClassName = pageClassName_;
        }
        String rootPackageName = sourceCreator_.getFirstRootPackageName();
        if (baseClassName.startsWith(rootPackageName + ".")) {
            String subPackageName = baseClassName.substring(rootPackageName
                    .length(), baseClassName.lastIndexOf('.'));
            int dot = subPackageName.indexOf('.', 1);
            if (dot >= 0) {
                subPackageName = subPackageName.substring(dot);
            } else {
                subPackageName = "";
            }
            return findClassName(sourceCreator_.getDtoPackageName(),
                    subPackageName, getDtoShortClassName(propertyName));
        } else {
            return findPropertyClassName(propertyName, pageClassName_);
        }
    }

    private String findClassName(String packageName, String subPackageName,
            String shortClassName) {
        String fullClassName = packageName + subPackageName + "."
                + shortClassName;
        if (sourceCreator_.getClass(fullClassName) != null) {
            return fullClassName;
        }

        String className;
        int pre = subPackageName.length();
        int idx;
        while ((idx = subPackageName.lastIndexOf('.', pre)) >= 0) {
            className = packageName + subPackageName.substring(0, idx) + "."
                    + shortClassName;
            if (sourceCreator_.getClass(className) != null) {
                return className;
            }
            pre = idx - 1;
        }

        return fullClassName;
    }

    String getDtoShortClassName(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        return DescUtils.capFirst(propertyName) + ClassType.DTO.getSuffix();
    }

    @Override
    public void popRepeatInfo(String name) {
        super.popRepeatInfo(name);
        repeatDepth_--;
    }

    public boolean isInRepeat() {
        return repeatDepth_ > 0;
    }

    public HttpMethod getMethod() {
        return method_;
    }

    public void setMethod(HttpMethod method) {
        method_ = method;
    }

    public void setClassDescMap(Map<String, ClassDesc> classDescMap) {
        classDescMap_ = classDescMap;
    }

    boolean isAvailable(String className) {
        return (sourceCreator_.getClass(className) != null);
    }

    public String getPageClassName() {
        return pageClassName_;
    }

    public void setPageClassName(String pageClassName) {
        pageClassName_ = pageClassName;
    }

    public ClassDesc getPageClassDesc() {
        return DescPool.getDefault().getClassDesc(getPageClassName());
    }

    public SourceCreator getSourceCreator() {
        return sourceCreator_;
    }

    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public FormDesc getFormDesc() {
        return formDesc_;
    }

    public void setFormDesc(FormDesc formDesc) {
        formDesc_ = formDesc;
    }

    public void close() {
        // 自動生成対象のHTMLからPageオブジェクトのロジックを全く参照していない場合でも
        // 空のPageを自動生成する方が便利なので、空のPageを自動生成するためにこうしている。
        getPageClassDesc();

        for (Iterator<ClassDesc> itr = DescPool.getDefault().iterator(); itr
                .hasNext();) {
            ClassDesc classDesc = itr.next();

            // 自動生成対象外のクラスと中身のないDTOは除外しておく。
            if (isOuter(classDesc) || isEmptyDto(classDesc)) {
                itr.remove();
                continue;
            }

            // スーパークラスと出自情報を設定する。
            ClassHint hint = getClassHint(classDesc.getName());
            if (hint != null) {
                classDesc.setSuperclassName(hint.getSuperclassName());
            }
            classDesc.setBornOf(path_);
        }

        for (ClassDesc classDesc : DescPool.getDefault()) {
            // プロパティの型に対応するDTOがMapに存在しない場合は、
            // そのDTOは上のフェーズで除外された、すなわちDTOかもしれないと考えて
            // 解析を進めたが結局DTOであることが確定しなかったので、
            // 型をデフォルトクラスに差し替える。
            // [#YMIR-198] ただし明示的に型を指定されている場合は差し替えない。
            for (PropertyDesc pd : classDesc.getPropertyDescs()) {
                if (!pd.getTypeDesc().isExplicit()) {
                    replaceSimpleDtoTypeToDefaultType(pd.getTypeDesc());
                }
            }
            // TODO 消す。
            //            classDesc.merge(classDescMap_.get(classDesc.getName()), false);
        }

        for (ClassDesc classDesc : DescPool.getDefault()) {
            // PageクラスとPageクラスから利用されているDTOと、外部で定義されている変数の型クラスと、
            // renderクラスのプロパティのうちインタフェース型であるものの実装クラスとみなされているクラス
            // 以外は無視する。
            if (isPage(classDesc) || isUsedClassName(classDesc.getName())) {
                registerDependingClassDescs(classDesc);
            }
        }
    }

    public boolean isOnDtoSearchPath(String className) {
        return sourceCreator_.getSourceCreatorSetting().isOnDtoSearchPath(
                className);
    }

    void replaceSimpleDtoTypeToDefaultType(TypeDesc typeDesc) {
        TypeToken typeToken = new TypeToken(typeDesc.getCompleteName());
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(
                    org.seasar.ymir.extension.creator.util.type.Token acceptor) {
                String componentName = DescUtils.getComponentName(acceptor
                        .getBaseName());
                boolean array = DescUtils.isArray(acceptor.getBaseName());
                if (isDto(componentName)
                        && !DescPool.getDefault().contains(componentName)) {
                    acceptor.setBaseName(DescUtils.getClassName(String.class
                            .getName(), array));
                }
                return null;
            }
        });

        typeDesc.setName(typeToken.getAsString());
    }

    void registerDependingClassDescs(ClassDesc classDesc) {
        if (!register(classDesc)) {
            return;
        }
        for (PropertyDesc pd : classDesc.getPropertyDescs()) {
            for (String className : pd.getTypeDesc().getImportClassNames()) {
                ClassDesc cd = DescPool.getDefault().getClassDesc(className);
                if (isDto(cd)) {
                    registerDependingClassDescs(cd);
                }
            }
        }
    }

    boolean register(ClassDesc classDesc) {
        String key = classDesc.getName();
        Object registered = classDescMap_.get(key);
        classDescMap_.put(key, classDesc);
        return registered != classDesc;
    }

    public boolean isOuter(ClassDesc classDesc) {
        return !classDesc.getPackageName().startsWith(
                sourceCreator_.getFirstRootPackageName() + ".");
    }

    public boolean isOuter(String typeName) {
        return !DescUtils.getNonGenericClassName(typeName).startsWith(
                sourceCreator_.getFirstRootPackageName() + ".");
    }

    private boolean isPage(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.PAGE);
    }

    private boolean isDto(String className) {
        return isDto(new ClassDescImpl(null, className));
    }

    private boolean isDto(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.DTO);
    }

    private boolean isTypeOf(ClassDesc classDesc, ClassType type) {
        return classDesc.isTypeOf(type) && !isOuter(classDesc);
    }

    private boolean isEmptyDto(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.DTO) && classDesc.isEmpty();
    }

    public PropertyDesc getRequestParameterPropertyDesc(ClassDesc classDesc,
            String requestParameterName, int mode) {
        int dot = requestParameterName.indexOf('.');
        if (dot < 0) {
            return getSingleRequestParameterPropertyDesc(classDesc,
                    requestParameterName, mode);
        } else {
            String baseName = requestParameterName.substring(0, dot);
            PropertyDesc propertyDesc = getSingleRequestParameterPropertyDesc(
                    classDesc, baseName, PropertyDesc.READ);

            // 後に続くプロパティ名によって型の補正を行なう。
            replaceTypeToGeneratedClassIfNeedToAddProperty(propertyDesc,
                    BeanUtils.getFirstSimpleSegment(requestParameterName
                            .substring(dot + 1)), classDesc);

            return getRequestParameterPropertyDesc(propertyDesc.getTypeDesc()
                    .getComponentClassDesc(), requestParameterName
                    .substring(dot + 1), mode);
        }
    }

    public void replaceTypeToGeneratedClassIfNeedToAddProperty(
            PropertyDesc propertyDesc, String propertyName, ClassDesc classDesc) {
        // プロパティの型が決定されていない状態でかつ自動生成対象外の型である場合は、
        // なんらかのプロパティを追加できるように再度型推論を行なう。
        // cf. ZptAnalyzerTest#testAnalyze36()
        // ただし指定されたプロパティを持つ場合は追加の必要がないため型推論は行なわない。
        if (!propertyDesc.getTypeDesc().isExplicit()
                && isOuter(propertyDesc.getTypeDesc().getComponentClassDesc())
                && !hasProperty(propertyDesc.getTypeDesc()
                        .getComponentClassDesc().getName(), propertyName)) {
            TypeDesc typeDesc = classDesc.getDescPool().newTypeDesc(
                    findPropertyClassName(propertyDesc.getName(), classDesc
                            .getName()));
            propertyDesc.setTypeDesc(typeDesc);
            propertyDesc.notifyTypeUpdated();
        }
    }

    public boolean hasProperty(String className, String propertyName) {
        return sourceCreator_.getPropertyDescriptor(className, propertyName) != null;
    }

    private PropertyDesc getSingleRequestParameterPropertyDesc(
            ClassDesc classDesc, String requestParameterName, int mode) {
        boolean collection = false;
        String collectionClassName = null;
        String collectionImplementationClassName = null;
        int lparen = requestParameterName.indexOf(CHAR_ARRAY_LPAREN);
        int rparen = requestParameterName.indexOf(CHAR_ARRAY_RPAREN);
        if (lparen >= 0 && rparen > lparen) {
            // 添え字つきパラメータを受けるプロパティはList。
            collection = true;
            collectionClassName = List.class.getName();
            collectionImplementationClassName = FlexibleList.class.getName();
            requestParameterName = requestParameterName.substring(0, lparen);
        }
        PropertyDesc propertyDesc = classDesc
                .getPropertyDesc(requestParameterName);
        if (propertyDesc != null && collection) {
            // これから既存プロパティをコレクションにする場合はこの時点で補正しておく。
            // ここでしないと余計なプロパティ（sizeとか）が消えてくれない。
            TypeDesc typeDesc = propertyDesc.getTypeDesc();
            changeToCollection(typeDesc, collectionClassName);
        }
        propertyDesc = addProperty(classDesc, requestParameterName, mode, null,
                collection, collectionClassName, false);
        if (collection) {
            propertyDesc.getTypeDesc().setCollectionImplementationClassName(
                    collectionImplementationClassName);
        }

        return propertyDesc;
    }

    private String toSingular(String name) {
        if (name == null) {
            return null;
        }
        if (name.endsWith(MULTIPLE_SUFFIX)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX.length())
                    + SINGULAR_SUFFIX;
        } else if (name.endsWith(MULTIPLE_SUFFIX2)) {
            return name.substring(0, name.length() - MULTIPLE_SUFFIX2.length())
                    + SINGULAR_SUFFIX2;
        }
        return name;
    }

    public boolean isUsedClassName(String className) {
        return usedClassNameSet_.contains(className);
    }

    public void setUsedClassName(String className) {
        usedClassNameSet_.add(className);
    }

    public String getPath() {
        return path_;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public void setPropertyTypeHintBag(ClassCreationHintBag hintBag) {
        hintBag_ = hintBag;
    }

    public PropertyTypeHint getPropertyTypeHint(String className,
            String propertyName) {
        if (hintBag_ != null) {
            return hintBag_.getPropertyTypeHint(className, propertyName);
        } else {
            return null;
        }
    }

    public ClassHint getClassHint(String className) {
        if (hintBag_ != null) {
            return hintBag_.getClassHint(className);
        } else {
            return null;
        }
    }

    public void pushVariableScope() {
        super.pushVariableScope();

        variableExpressions_.push(new HashMap<String, String>());
    }

    public void popVariableScope() {
        super.popVariableScope();

        variableExpressions_.pop();
    }

    public void defineVariableExpression(int scope, String name,
            String expression) {
        if (scope == SCOPE_LOCAL) {
            variableExpressions_.peek().put(name, expression);
        } else if (scope == SCOPE_GLOBAL) {
            globalVariableExpression_.put(name, expression);
        } else {
            throw new EvaluationRuntimeException("Unknown scope: " + scope);
        }
    }

    public String getDefinedVariableExpression(String name) {
        String expression = null;
        if (!variableExpressions_.isEmpty()) {
            expression = variableExpressions_.peek().get(name);
        }
        if (expression == null) {
            expression = globalVariableExpression_.get(name);
        }
        return expression;
    }

    public boolean isRepeatedPropertyGeneratedAsList() {
        return repeatedPropertyGeneratedAsList_;
    }

    public void setRepeatedPropertyGeneratedAsList(
            boolean repeatedPropertyGeneratedAsList) {
        repeatedPropertyGeneratedAsList_ = repeatedPropertyGeneratedAsList;
    }

    public void changeToCollection(TypeDesc typeDesc, String collectionClassName) {
        if (!typeDesc.isCollection()) {
            if (collectionClassName == null) {
                typeDesc.getComponentClassDesc().removePropertyDesc(
                        AnalyzerContext.PROP_LENGTH);
            } else {
                typeDesc.getComponentClassDesc().removePropertyDesc(
                        AnalyzerContext.PROP_SIZE);
            }
        }
        typeDesc.setCollectionClassName(collectionClassName);
        typeDesc.setCollection(true);
    }
}
