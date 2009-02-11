package org.seasar.ymir.extension.zpt;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.seasar.cms.pluggable.ClassTraverser;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.PropertyTypeHint;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.token.Token;
import org.seasar.ymir.zpt.YmirVariableResolver;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.render.html.InputTag;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerContext extends ZptTemplateContext {
    private static final String MULTIPLE_SUFFIX = "ies";

    private static final String SINGULAR_SUFFIX = "y";

    private static final String MULTIPLE_SUFFIX2 = "s";

    private static final String SINGULAR_SUFFIX2 = "";

    private static final char STR_ARRAY_LPAREN = '[';

    private static final char CHAR_ARRAY_RPAREN = ']';

    private static final String ARRAY_SUFFIX = "[]";

    private static final String PROP_LENGTH = "length";

    private static ClassNamePattern[] freyjaRenderClassNamePairs_;

    private SourceCreator sourceCreator_;

    private HttpMethod method_;

    private Map<String, ClassDesc> classDescMap_;

    private Map<String, ClassDesc> temporaryClassDescMap_ = new LinkedHashMap<String, ClassDesc>();

    private String pageClassName_;

    private FormDesc formDesc_;

    private boolean usingFreyjaRenderClasses_;

    private boolean repeatedPropertyGeneratedAsList_;

    private VariableResolver variableResolver_;

    private Set<String> usedAsVariableSet_ = new HashSet<String>();

    private Set<String> usedAsLocalVariableSet_ = new HashSet<String>();

    private Set<String> ignoreVariableSet_ = new HashSet<String>();

    private String path_;

    private ClassCreationHintBag hintBag_;

    private Stack<Map<String, String>> variableExpressions_ = new Stack<Map<String, String>>();

    private Map<String, String> globalVariableExpression_ = new HashMap<String, String>();

    private int repeatDepth_;

    static {
        final List<ClassNamePattern> list = new ArrayList<ClassNamePattern>();
        ClassTraverser traverser = new ClassTraverser();
        traverser.addReferenceClass(InputTag.class);
        traverser.addClassPattern("net.skirnir.freyja.render", ".+");
        traverser.setClassHandler(new ClassTraversal.ClassHandler() {
            public void processClass(String packageName, String shortClassName) {
                String className = packageName + "." + shortClassName;
                if (className.equals(net.skirnir.freyja.render.Notes.class
                        .getName())) {
                    className = Notes.class.getName();
                } else if (className
                        .equals(net.skirnir.freyja.render.Note.class.getName())) {
                    className = Note.class.getName();
                }
                list.add(new ClassNamePattern(shortClassName, className));
            }
        });
        traverser.traverse();
        Collections.sort(list);
        freyjaRenderClassNamePairs_ = list.toArray(new ClassNamePattern[0]);
    }

    protected static class ClassNamePattern implements
            Comparable<ClassNamePattern> {
        private Pattern pattern_;

        private String shortName_;

        private String className_;

        public ClassNamePattern(String shortName, String className) {
            pattern_ = Pattern.compile(".*" + shortName + "s?");
            shortName_ = shortName;
            className_ = className;
        }

        public String getClassNameIfMatched(String name) {
            if (name == null) {
                return null;
            }
            if (pattern_.matcher(name).matches()) {
                return className_;
            } else {
                return null;
            }
        }

        public String getClassNameEquals(String name) {
            if (shortName_.equals(name)) {
                return className_;
            } else {
                return null;
            }
        }

        public int compareTo(ClassNamePattern o) {
            int cmp = o.shortName_.length() - shortName_.length();
            if (cmp == 0) {
                cmp = shortName_.compareTo(o.shortName_);
            }
            return cmp;
        }

        @Override
        public String toString() {
            return shortName_;
        }
    }

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

    public void setUsingFreyjaRenderClasses(boolean usingFreyjaRenderClasses) {
        usingFreyjaRenderClasses_ = usingFreyjaRenderClasses;
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

    public void defineVariable(int scope, String name, Object value) {
        setUsedAsLocalVariable(name);
        super.defineVariable(scope, name, value);
    }

    public RepeatInfo pushRepeatInfo(String name, Object[] objs) {
        repeatDepth_++;
        setUsedAsLocalVariable(name);
        if (objs != null && objs.length == 1 && objs[0] instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) objs[0];
            ClassDesc valueClassDesc;
            PropertyDesc pd = wrapper.getPropertyDesc();
            if (pd != null) {
                TypeDesc td = pd.getTypeDesc();
                if (!td.isExplicit()) {
                    String className = fromPropertyNameToClassName(wrapper
                            .getParent() != null ? wrapper.getParent()
                            .getValueClassDesc() : null, name);
                    valueClassDesc = getTemporaryClassDesc(className);
                    if (repeatedPropertyGeneratedAsList_) {
                        // List型に補正する。
                        td.setName("java.util.List<" + className + ">");
                    } else {
                        // 配列型に補正する。
                        if (!td.isCollection()) {
                            td.getComponentClassDesc().removePropertyDesc(
                                    PROP_LENGTH);
                        }
                        td.setName(className + "[]");
                    }
                    td.setComponentClassDesc(valueClassDesc);
                } else {
                    valueClassDesc = td.getComponentClassDesc();
                }
            } else {
                valueClassDesc = wrapper.getValueClassDesc();
            }
            objs[0] = new DescWrapper(this, valueClassDesc);
        }

        return super.pushRepeatInfo(name, objs);
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

    String findRenderClassName(String propertyName) {
        String name = capFirst(propertyName);

        // 互換性のため。
        String fullName = name + "Tag";
        for (ClassNamePattern pair : freyjaRenderClassNamePairs_) {
            String className = pair.getClassNameEquals(fullName);
            if (className != null) {
                return className;
            }
        }

        for (ClassNamePattern pair : freyjaRenderClassNamePairs_) {
            String className = pair.getClassNameIfMatched(name);
            if (className != null) {
                return className;
            }
        }

        return null;
    }

    public ClassDesc getTemporaryClassDesc(String className) {
        ClassDesc classDesc = temporaryClassDescMap_.get(className);
        if (classDesc == null) {
            classDesc = sourceCreator_.newClassDesc(className, hintBag_);
            temporaryClassDescMap_.put(className, classDesc);
        }
        return classDesc;
    }

    boolean isAvailable(String className) {
        return (sourceCreator_.getClass(className) != null);
    }

    /**
     * 指定されたClassDescが持つ指定された名前のプロパティの型を表すクラス名を推測して返します。
     * 
     * @param classDesc ClassDesc。
     * @param propertyName プロパティ名。
     * @return プロパティの型を表すクラス名。
     */
    public String fromPropertyNameToClassName(ClassDesc classDesc,
            String propertyName) {
        if (RequestProcessor.ATTR_SELF.equals(propertyName)) {
            return getPageClassName();
            // Kvasir/SoraのpopプラグインのexternalTemplate機能を使って自動生成
            // をしている場合、classNameはnullになり得ることに注意。
        } else if (RequestProcessor.ATTR_NOTES.equals(propertyName)) {
            // NotesはFreyjaにもYmirにもあるが、Ymirのものを優先させたいためこうしている。
            return Notes.class.getName();
        } else if (YmirVariableResolver.NAME_YMIRREQUEST.equals(propertyName)) {
            return Request.class.getName();
        } else if (YmirVariableResolver.NAME_CONTAINER.equals(propertyName)) {
            return S2Container.class.getName();
        } else if (YmirVariableResolver.NAME_MESSAGES.equals(propertyName)) {
            return Messages.class.getName();
        } else if (YmirVariableResolver.NAME_TOKEN.equals(propertyName)) {
            return Token.class.getName();
        } else if (YmirVariableResolver.NAME_VARIABLES.equals(propertyName)) {
            return VariableResolver.class.getName();
        } else if (YmirVariableResolver.NAME_PARAM_SELF.equals(propertyName)) {
            // param-selfに指定されたプロパティ名をPageのプロパティ名とみなさせる方が
            // 都合が良いのでこうしている。
            return getPageClassName();
        } else if (usingFreyjaRenderClasses_) {
            String className = findRenderClassName(propertyName);
            if (className != null) {
                return className;
            }
        } else if (!AnalyzerUtils.isValidVariableName(propertyName)) {
            return Object.class.getName();
        }

        return getDtoClassName(classDesc, propertyName);
    }

    public boolean isSystemVariable(String name) {
        return RequestProcessor.ATTR_SELF.equals(name)
                || RequestProcessor.ATTR_NOTES.equals(name)
                || YmirVariableResolver.NAME_YMIRREQUEST.equals(name)
                || YmirVariableResolver.NAME_CONTAINER.equals(name)
                || YmirVariableResolver.NAME_MESSAGES.equals(name)
                || YmirVariableResolver.NAME_TOKEN.equals(name)
                || YmirVariableResolver.NAME_VARIABLES.equals(name)
                || YmirVariableResolver.NAME_PARAM_SELF.equals(name);
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
            if (isOuter(classDesc) || isEmptyDto(classDesc)) {
                // 自動生成対象外のクラスと中身のないDTOは除外しておく。
                itr.remove();
            }
        }

        for (Iterator<Map.Entry<String, ClassDesc>> itr = temporaryClassDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, ClassDesc> entry = itr.next();
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

        for (Iterator<ClassDesc> itr = temporaryClassDescMap_.values()
                .iterator(); itr.hasNext();) {
            ClassDesc classDesc = itr.next();

            // PageクラスとPageクラスから利用されているDTOと、外部で定義されている変数の型クラス
            // 以外は無視する。
            if (isPage(classDesc)) {
                registerAvailablePagesAndDtos(classDesc);
            } else if (isUsedAsVariable(classDesc.getName())
                    && !isUsedAsLocalVariable(classDesc.getName())) {
                register(classDesc);
            }
        }
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

    void registerAvailablePagesAndDtos(ClassDesc classDesc) {
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
                    registerAvailablePagesAndDtos(cd);
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
                sourceCreator_.getRootPackageName() + ".");
    }

    boolean isOuter(String typeName) {
        return DescUtils.getNonGenericClassName(typeName).startsWith(
                sourceCreator_.getRootPackageName() + ".");
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

    String getDtoClassName(ClassDesc classDesc, String propertyName) {
        StringBuilder dtoClassName = new StringBuilder();
        dtoClassName.append(sourceCreator_.getDtoPackageName());

        if (classDesc != null) {
            String packageName = classDesc.getPackageName();
            String rootPackageName = sourceCreator_.getRootPackageName();
            if (packageName.equals(rootPackageName)) {
                ;
            } else if (packageName.startsWith(rootPackageName + ".")) {
                String subPackageName = packageName.substring(rootPackageName
                        .length() + 1/*= ".".length() */);
                int dot = subPackageName.indexOf('.');
                if (dot >= 0) {
                    // com.example.web.sub ... subPackageName
                    // com.example         ... rootPackageName
                    //                ^    ... この「.」があればサブアプリケーション。

                    return findClassName(sourceCreator_.getDtoPackageName(),
                            subPackageName.substring(dot + 1),
                            getDtoShortClassName(propertyName));
                }
            } else {
                // パッケージがルートパッケージ外の場合はルートパッケージ外としてDTOクラス名を
                // 生成しておく。（最終的にはルートパッケージ外なので無視されるはず）
                return packageName + ".dto."
                        + getDtoShortClassName(propertyName);
            }
        }
        return sourceCreator_.getDtoPackageName() + "."
                + getDtoShortClassName(propertyName);
    }

    String findClassName(String packageName, String relatedSubPackageName,
            String shortClassName) {
        String fullClassName = packageName + "." + relatedSubPackageName + "."
                + shortClassName;
        if (sourceCreator_.getClass(fullClassName) != null) {
            return fullClassName;
        }

        String className;
        int pre = relatedSubPackageName.length();
        int idx;
        while ((idx = relatedSubPackageName.lastIndexOf('.', pre)) >= 0) {
            className = packageName + "."
                    + relatedSubPackageName.substring(0, idx) + "."
                    + shortClassName;
            if (sourceCreator_.getClass(className) != null) {
                return className;
            }
            pre = idx - 1;
        }

        className = packageName + "." + shortClassName;
        if (sourceCreator_.getClass(className) != null) {
            return className;
        }

        return fullClassName;
    }

    String getDtoShortClassName(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        return capFirst(propertyName) + ClassType.DTO.getSuffix();
    }

    String capFirst(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return Character.toUpperCase(string.charAt(0))
                    + string.substring(1);
        }
    }

    public PropertyDesc getPropertyDesc(ClassDesc classDesc, String name,
            int mode) {
        int dot = name.indexOf('.');
        if (dot < 0) {
            return getSinglePropertyDesc(classDesc, name, mode, true);
        } else {
            String baseName = name.substring(0, dot);
            PropertyDesc propertyDesc = getSinglePropertyDesc(classDesc,
                    baseName, PropertyDesc.READ, false);
            ClassDesc typeClassDesc = preparePropertyTypeClassDesc(classDesc,
                    propertyDesc);
            if (typeClassDesc != null) {
                return getPropertyDesc(typeClassDesc, name.substring(dot + 1),
                        mode);
            } else {
                return propertyDesc;
            }
        }
    }

    PropertyDesc getSinglePropertyDesc(ClassDesc classDesc, String name,
            int mode, boolean setAsArrayIfSetterExists) {
        boolean array = false;
        int lparen = name.indexOf(STR_ARRAY_LPAREN);
        int rparen = name.indexOf(CHAR_ARRAY_RPAREN);
        if (lparen >= 0 && rparen > lparen) {
            array = true;
            name = name.substring(0, lparen);
        } else {
            // 今のところ、添え字つきパラメータの型が配列というのはサポートできていない。
            if (setAsArrayIfSetterExists) {
                array = (classDesc.getPropertyDesc(name) != null && classDesc
                        .getPropertyDesc(name).isWritable());
            }
        }
        PropertyDesc propertyDesc = classDesc.addProperty(name, mode);
        if (array) {
            // なるべく元の状態を壊さないようにこうしている。
            // （添字があるならば配列である、は真であるが、裏は真ではない。）
            // つまり、arrayがtrueの時だけsetArray()している、ということ。
            TypeDesc typeDesc = propertyDesc.getTypeDesc();
            if (!typeDesc.isCollection()) {
                typeDesc.getComponentClassDesc()
                        .removePropertyDesc(PROP_LENGTH);
            }
            typeDesc.setCollection(array);

        }
        return adjustPropertyType(classDesc.getName(), propertyDesc);
    }

    public ClassDesc preparePropertyTypeClassDesc(ClassDesc classDesc,
            PropertyDesc propertyDesc) {
        return preparePropertyTypeClassDesc(classDesc, propertyDesc, false);
    }

    public ClassDesc preparePropertyTypeClassDesc(ClassDesc classDesc,
            PropertyDesc propertyDesc, boolean force) {
        ClassDesc cd = propertyDesc.getTypeDesc().getComponentClassDesc();
        ClassDesc returned = null;
        if (cd instanceof ClassDescImpl) {
            returned = cd;
        } else if (cd == TypeDesc.DEFAULT_CLASSDESC || force) {
            String name = propertyDesc.getName();
            if (propertyDesc.getTypeDesc().isCollection()) {
                // 名前を単数形にする。
                name = toSingular(name);
            }
            returned = getTemporaryClassDesc(fromPropertyNameToClassName(
                    classDesc, name));
            propertyDesc.getTypeDesc().setComponentClassDesc(returned);
            propertyDesc.notifyUpdatingType();
        }
        return returned;
    }

    String toSingular(String name) {
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

    public boolean isUsedAsVariable(String name) {
        return usedAsVariableSet_.contains(name);
    }

    public void setUsedAsVariable(String name) {
        usedAsVariableSet_.add(name);
    }

    public boolean isUsedAsLocalVariable(String name) {
        return usedAsLocalVariableSet_.contains(name);
    }

    public void setUsedAsLocalVariable(String name) {
        usedAsLocalVariableSet_.add(name);
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

    public PropertyDesc adjustPropertyType(String className, PropertyDesc pd) {
        PropertyTypeHint hint = getPropertyTypeHint(className, pd.getName());
        String typeName;
        if (hint != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(hint.getTypeName());
            if (hint.isCollection()) {
                sb.append(ARRAY_SUFFIX);
            }
            typeName = sb.toString();
        } else {
            PropertyDescriptor descriptor = getSourceCreator()
                    .getPropertyDescriptor(className, pd.getName());
            if (descriptor == null) {
                // ヒントも既存クラスからの情報もなければ何もしない。
                return pd;
            }

            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                pd.setGetterName(readMethod.getName());
            }

            typeName = DescUtils.getGenericPropertyTypeName(descriptor);
        }

        TypeDescImpl td = new TypeDescImpl(typeName, true);
        String cname = td.getComponentClassDesc().getName();
        Map<String, ClassDesc> map = new HashMap<String, ClassDesc>();
        map.put(cname, getTemporaryClassDesc(cname));
        td.setName(typeName, map);

        pd.setTypeDesc(td);
        pd.notifyUpdatingType();

        return pd;
    }

    public boolean isUsingFreyjaRenderClasses() {
        return usingFreyjaRenderClasses_;
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
}
