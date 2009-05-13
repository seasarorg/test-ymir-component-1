package org.seasar.ymir.extension.zpt;

import static org.seasar.ymir.extension.creator.PropertyDesc.PROBABILITY_DEFAULT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassType;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.FlexibleList;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerContext extends ZptTemplateContext {
    static final char CHAR_ARRAY_LPAREN = '[';

    static final char CHAR_ARRAY_RPAREN = ']';

    private SourceCreator sourceCreator_;

    private HttpMethod method_;

    private String pageClassName_;

    private FormDesc formDesc_;

    private boolean repeatedPropertyGeneratedAsList_;

    private VariableResolver variableResolver_;

    private Set<String> usedClassNameSet_ = new HashSet<String>();

    private Set<String> ignoreVariableSet_ = new HashSet<String>();

    private String path_;

    private Stack<Map<String, String>> variableExpressions_ = new Stack<Map<String, String>>();

    private Map<String, String> globalVariableExpression_ = new HashMap<String, String>();

    private int repeatDepth_;

    private Notes warnings_ = new Notes();

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
            ((DescWrapper) value).setVariableName(name, false, null,
                    SourceCreator.PROBABILITY_NAME);
        }
        super.defineVariable(scope, name, value);
    }

    @Override
    public RepeatInfo pushRepeatInfo(String name, Object[] objs) {
        repeatDepth_++;
        if (objs != null && objs.length == 1 && objs[0] instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) objs[0];
            wrapper.setVariableName(name, true,
                    repeatedPropertyGeneratedAsList_ ? List.class.getName()
                            : null, SourceCreator.PROBABILITY_NAME);
            ClassDesc valueClassDesc;
            PropertyDesc pd = wrapper.getPropertyDesc();
            if (pd != null) {
                valueClassDesc = pd.getTypeDesc().getComponentClassDesc();
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

        sourceCreator_.finishAnalyzing(DescPool.getDefault());

        Set<ClassDesc> usedClassDescSet = new HashSet<ClassDesc>();
        for (ClassDesc classDesc : DescPool.getDefault()
                .getGeneratedClassDescs()) {
            // PageクラスとPageクラスから利用されているDTOと、外部で定義されている変数の型クラス
            // 以外は無視する。
            if (isPage(classDesc) || isUsedClassName(classDesc.getName())) {
                registerDependingClassDescs(classDesc, usedClassDescSet);
            }
        }
        for (Iterator<ClassDesc> itr = DescPool.getDefault().iterator(); itr
                .hasNext();) {
            ClassDesc classDesc = itr.next();
            if (!usedClassDescSet.contains(classDesc)
                    || sourceCreator_.isOuter(classDesc)) {
                itr.remove();
            }
        }
    }

    private void registerDependingClassDescs(ClassDesc classDesc,
            Set<ClassDesc> usedClassDescSet) {
        if (!usedClassDescSet.add(classDesc)) {
            return;
        }
        for (PropertyDesc pd : classDesc.getPropertyDescs()) {
            ClassDesc cd = pd.getTypeDesc().getComponentClassDesc();
            if (sourceCreator_.isDtoClass(cd.getName())) {
                registerDependingClassDescs(cd, usedClassDescSet);
            }
        }
    }

    private boolean isTypeOf(ClassDesc classDesc, ClassType type) {
        return classDesc.isTypeOf(type) && !sourceCreator_.isOuter(classDesc);
    }

    private boolean isPage(ClassDesc classDesc) {
        return isTypeOf(classDesc, ClassType.PAGE);
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
        if (!propertyDesc.isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)
                && sourceCreator_.isOuter(propertyDesc.getTypeDesc()
                        .getComponentClassDesc())
                && !hasProperty(propertyDesc.getTypeDesc()
                        .getComponentClassDesc().getName(), propertyName)) {
            TypeDesc typeDesc = classDesc.getDescPool().newTypeDesc(
                    sourceCreator_.findPropertyClassName(
                            propertyDesc.getName(), sourceCreator_
                                    .getGeneratedClassName(classDesc.getName(),
                                            pageClassName_)));
            propertyDesc.setTypeDesc(typeDesc);
            if (!propertyDesc.isTypeAlreadySet(PROBABILITY_DEFAULT)) {
                propertyDesc.notifyTypeUpdated(PROBABILITY_DEFAULT);
            }
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
        PropertyDesc propertyDesc = sourceCreator_.addPropertyDesc(classDesc,
                requestParameterName, mode, null, collection,
                collectionClassName, SourceCreator.PROBABILITY_COLLECTION,
                pageClassName_);
        if (collection) {
            propertyDesc.getTypeDesc().setCollectionImplementationClassName(
                    collectionImplementationClassName);
        }
        propertyDesc.incrementReferCount();

        return propertyDesc;
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

    public void addWarning(String expression, int columnNumber) {
        warnings_.add(new Note("warning.zpt.syntaxError", getElement()
                .getLineNumber(), getElement().getColumnNumber(), expression,
                columnNumber));
    }

    public Notes getWarnings() {
        return warnings_;
    }

    public void setWarnings(Notes warnings) {
        warnings_ = warnings;
    }
}
