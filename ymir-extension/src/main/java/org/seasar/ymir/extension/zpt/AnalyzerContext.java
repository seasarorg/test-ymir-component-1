package org.seasar.ymir.extension.zpt;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassHint;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
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

    private SourceCreator sourceCreator_;

    private HttpMethod method_;

    private Map<String, ClassDesc> classDescMap_;

    private Map<String, ClassDesc> temporaryClassDescMap_ = new LinkedHashMap<String, ClassDesc>();

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
                        td.setName("java.util.List<" + valueClassDesc.getName()
                                + ">");
                    } else {
                        // 配列型に補正する。
                        if (!td.isCollection()) {
                            td.getComponentClassDesc().removePropertyDesc(
                                    PROP_LENGTH);
                        }
                        td.setName(valueClassDesc.getName() + "[]");
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
     * @param propertyAlias プロパティ名の別名。
     * プロパティの型クラス名を生成する際にこちらが優先されます。
     * 別名がない場合はプロパティ名と同じ名前を指定して下さい。
     * @param mode プロパティのモード。
     * @param force 強制的にプロパティ型を再推論するかどうか。
     * trueの場合、明示的に型が設定されていない限り強制的にプロパティ型を再推論します。
     * @return PropertyDescオブジェクト。nullが返されることはありません。
     */
    public PropertyDesc addProperty(ClassDesc classDesc, String propertyName,
            String propertyAlias, int mode, boolean force) {
        PropertyDesc propertyDesc = classDesc.addProperty(propertyName, mode);
        if (propertyDesc.getTypeDesc().isExplicit()) {
            return propertyDesc;
        }
        if (!force && propertyDesc.isTypeAlreadySet()) {
            return propertyDesc;
        }

        TypeDesc typeDesc;

        // プロパティ型のヒント情報を見る。
        // ヒントがなければ、実際の親クラスからプロパティ型を取得する。
        String className = classDesc.getName();
        PropertyTypeHint hint = getPropertyTypeHint(className, propertyName);
        if (hint != null) {
            typeDesc = new TypeDescImpl(hint.getTypeName(), true);
        } else {
            PropertyDescriptor descriptor = sourceCreator_
                    .getPropertyDescriptor(className, propertyName);
            if (descriptor != null) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod != null) {
                    propertyDesc.setGetterName(readMethod.getName());
                }

                String componentClassName;
                Class<?> componentClass = sourceCreator_.getClass(DescUtils
                        .getComponentPropertyTypeName(descriptor));
                if (componentClass.isInterface()) {
                    // インタフェースの場合は実装クラス型を推論する。
                    componentClassName = findPropertyClassName(Introspector
                            .decapitalize(ClassUtils
                                    .getShorterName(componentClass)), className);
                    setUsedClassName(componentClassName);
                } else {
                    // そうでない場合は実際の型をそのまま使う。
                    componentClassName = componentClass.getName();
                }

                String typeName = DescUtils
                        .getGenericPropertyTypeName(descriptor);
                // typeのexplicitは、プロパティの所属クラスが自動生成対象かによって変わる。
                // 自動生成対象でないクラスについては、プロパティの型は不変なのでtrueにすべき。
                // 自動生成クラスについては、プロパティ型はHTMLテンプレートの内容によって変わり得るのでfalseにすべき。
                if (Collection.class.isAssignableFrom(descriptor
                        .getPropertyType())) {
                    TypeToken typeToken = new TypeToken(typeName);
                    typeToken.getTypes()[0].setBaseName(componentClassName);
                    typeDesc = new TypeDescImpl(typeToken.getAsString(),
                            !isGenerated(classDesc));
                } else if (descriptor.getPropertyType().isArray()) {
                    typeDesc = new TypeDescImpl(componentClassName + "[]",
                            !isGenerated(classDesc));
                } else {
                    typeDesc = new TypeDescImpl(componentClassName,
                            !isGenerated(classDesc));
                }
            } else {
                // 推論できなかった場合は名前から推論を行なう。
                typeDesc = new TypeDescImpl(inferPropertyClassName(
                        propertyAlias, className));
            }
        }

        String cname = typeDesc.getComponentClassDesc().getName();
        Map<String, ClassDesc> map = new HashMap<String, ClassDesc>();
        map.put(cname, getTemporaryClassDesc(cname));
        typeDesc.setName(typeDesc.getName(), map);

        if (typeDesc.isCollection()
                && List.class.getName().equals(
                        typeDesc.getCollectionClassName())) {
            typeDesc.setCollectionImplementationClassName(propertyDesc
                    .getTypeDesc().getCollectionImplementationClassName());
        }

        propertyDesc.setTypeDesc(typeDesc);
        if (!isGenerated(typeDesc.getComponentClassDesc())) {
            // 自動生成対象のクラスの場合はtal:conditionやtal:attributesなどでbooleanだとみなされる余地があるので
            // ここではnotifyTypeUpdated()を呼ばないようにしている。
            propertyDesc.notifyTypeUpdated();
        }

        return propertyDesc;
    }

    public PropertyDesc addProperty(ClassDesc classDesc, String propertyName,
            int mode) {
        return addProperty(classDesc, propertyName, propertyName, mode, false);
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
            return sourceCreator_.getDtoPackageName() + "."
                    + getDtoShortClassName(propertyName);
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
        return capFirst(propertyName) + ClassType.DTO.getSuffix();
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

    public Map<String, ClassDesc> getClassDescMap() {
        return classDescMap_;
    }

    public void setClassDescMap(Map<String, ClassDesc> classDescMap) {
        classDescMap_ = classDescMap;
    }

    public ClassDesc getTemporaryClassDesc(String className) {
        ClassDesc classDesc = temporaryClassDescMap_.get(className);
        if (classDesc == null) {
            classDesc = sourceCreator_.newClassDesc(className, hintBag_);
            classDesc.setBornOf(path_);
            temporaryClassDescMap_.put(className, classDesc);
        }
        return classDesc;
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
        return getTemporaryClassDesc(getPageClassName());
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
        getTemporaryClassDesc(pageClassName_);

        for (Iterator<ClassDesc> itr = temporaryClassDescMap_.values()
                .iterator(); itr.hasNext();) {
            ClassDesc classDesc = itr.next();

            // 自動生成対象外のクラスと中身のないDTOは除外しておく。

            if (isOuter(classDesc) || isEmptyDto(classDesc)) {
                itr.remove();
                continue;
            }
        }

        for (Map.Entry<String, ClassDesc> entry : temporaryClassDescMap_
                .entrySet()) {
            String name = entry.getKey();
            ClassDesc classDesc = entry.getValue();

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
            classDesc.merge(classDescMap_.get(name), false);
        }

        for (ClassDesc classDesc : temporaryClassDescMap_.values()) {
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
                if (isDto(new SimpleClassDesc(componentName))
                        && !temporaryClassDescMap_.containsKey(componentName)) {
                    acceptor.setBaseName(DescUtils.getClassName(
                            TypeDesc.DEFAULT_CLASSDESC.getName(), array));
                }
                return null;
            }
        });

        typeDesc.setName(typeToken.getAsString(), temporaryClassDescMap_);
    }

    void registerDependingClassDescs(ClassDesc classDesc) {
        if (!register(classDesc)) {
            return;
        }
        for (PropertyDesc pd : classDesc.getPropertyDescs()) {
            for (String className : pd.getTypeDesc().getImportClassNames()) {
                ClassDesc cd = temporaryClassDescMap_.get(className);
                if (cd == null) {
                    cd = new SimpleClassDesc(className);
                }
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

    boolean isOuter(ClassDesc classDesc) {
        return !classDesc.getPackageName().startsWith(
                sourceCreator_.getFirstRootPackageName() + ".");
    }

    boolean isOuter(String typeName) {
        return DescUtils.getNonGenericClassName(typeName).startsWith(
                sourceCreator_.getFirstRootPackageName() + ".");
    }

    boolean isPage(ClassDesc classDesc) {
        return classDesc.isTypeOf(ClassType.PAGE) && !isOuter(classDesc);
    }

    boolean isDto(ClassDesc classDesc) {
        return classDesc.isTypeOf(ClassType.DTO) && !isOuter(classDesc);
    }

    boolean isEmptyDto(ClassDesc classDesc) {
        return (isDto(classDesc) && classDesc.isEmpty());
    }

    String capFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toUpperCase(string.charAt(0))
                    + string.substring(1);
        }
    }

    public PropertyDesc getPropertyDesc(ClassDesc classDesc,
            String requestParameterName, int mode) {
        return getPropertyDesc(classDesc, requestParameterName, mode, false);
    }

    private PropertyDesc getPropertyDesc(ClassDesc classDesc,
            String requestParameterName, int mode,
            boolean descendantOfIndexedProperty) {
        int dot = requestParameterName.indexOf('.');
        if (dot < 0) {
            return getSinglePropertyDesc(classDesc, requestParameterName, mode,
                    !descendantOfIndexedProperty);
        } else {
            String baseName = requestParameterName.substring(0, dot);
            PropertyDesc propertyDesc = getSinglePropertyDesc(classDesc,
                    baseName, PropertyDesc.READ, false);

            // 後に続くプロパティ名によって型の補正を行なう。
            String nextName;
            int nextDot = requestParameterName.indexOf('.', dot + 1);
            if (nextDot < 0) {
                nextName = requestParameterName.substring(dot + 1);
            } else {
                nextName = requestParameterName.substring(dot + 1, nextDot);
            }
            replaceTypeToGeneratedClassIfNeedToAddProperty(propertyDesc,
                    nextName, classDesc);

            return getPropertyDesc(propertyDesc.getTypeDesc()
                    .getComponentClassDesc(), requestParameterName
                    .substring(dot + 1), mode,
            // 添え字つきプロパティか添え字つきプロパティの子孫の場合はプロパティをコレクションにしないようにしている。
                    descendantOfIndexedProperty ? true : baseName
                            .indexOf(CHAR_ARRAY_LPAREN) >= 0);
        }
    }

    public void replaceTypeToGeneratedClassIfNeedToAddProperty(
            PropertyDesc propertyDesc_, String propertyName, ClassDesc classDesc) {
        // プロパティの型が決定されていない状態でかつ自動生成対象外の型である場合は、
        // なんらかのプロパティを追加できるように再度型推論を行なう。
        // cf. ZptAnalyzerTest#testAnalyze36()
        // ただし指定されたプロパティを持つ場合は追加の必要がないため型推論は行なわない。
        if (!propertyDesc_.getTypeDesc().isExplicit()
                && !isGenerated(propertyDesc_.getTypeDesc()
                        .getComponentClassDesc())
                && !hasProperty(propertyDesc_.getTypeDesc()
                        .getComponentClassDesc().getName(), propertyName)) {
            TypeDesc typeDesc = new TypeDescImpl(findPropertyClassName(
                    propertyDesc_.getName(), classDesc.getName()), false);
            String cname = typeDesc.getComponentClassDesc().getName();
            Map<String, ClassDesc> map = new HashMap<String, ClassDesc>();
            map.put(cname, getTemporaryClassDesc(cname));
            typeDesc.setName(typeDesc.getName(), map);
            propertyDesc_.setTypeDesc(typeDesc);
            propertyDesc_.notifyTypeUpdated();
        }
    }

    public boolean hasProperty(String className, String propertyName) {
        return sourceCreator_.getPropertyDescriptor(className, propertyName) != null;
    }

    private PropertyDesc getSinglePropertyDesc(ClassDesc classDesc,
            String requestParameterName, int mode,
            boolean setAsCollectionIfSetterExists) {
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
        } else {
            // 今のところ、添え字つきパラメータの型が配列というのはサポートできていない。
            if (setAsCollectionIfSetterExists) {
                // 添え字なしパラメータを複数受けるプロパティは配列。
                collection = classDesc.getPropertyDesc(requestParameterName) != null
                        && classDesc.getPropertyDesc(requestParameterName)
                                .isWritable();
            }
        }
        PropertyDesc propertyDesc = addProperty(classDesc,
                requestParameterName, toSingular(requestParameterName), mode,
                false);
        if (collection) {
            // なるべく元の状態を壊さないようにこうしている。
            // （添字があるならば配列である、は真であるが、裏は真ではない。）
            // つまり、collectionがtrueの時だけsetCollection()している、ということ。
            TypeDesc typeDesc = propertyDesc.getTypeDesc();
            if (!typeDesc.isCollection()) {
                typeDesc.getComponentClassDesc()
                        .removePropertyDesc(PROP_LENGTH);
            }
            typeDesc.setCollection(collection);
            typeDesc.setCollectionClassName(collectionClassName);
            typeDesc
                    .setCollectionImplementationClassName(collectionImplementationClassName);
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

    public boolean isGenerated(ClassDesc classDesc) {
        if (classDesc == null) {
            return false;
        } else {
            return classDesc.getName().startsWith(
                    sourceCreator_.getFirstRootPackageName() + ".");
        }
    }
}
