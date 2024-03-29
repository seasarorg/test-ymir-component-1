package org.seasar.ymir.extension.zpt;

import static org.seasar.ymir.extension.creator.SourceCreator.PROBABILITY_BOOLEAN_ATTRIBUTE;
import static org.seasar.ymir.extension.creator.SourceCreator.PROBABILITY_TYPE;
import static org.seasar.ymir.extension.zpt.AnalyzerUtils.shouldGeneratePropertyForParameter;
import static org.seasar.ymir.util.BeanUtils.getFirstSimpleSegment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.FormDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.render.html.Option;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.ExpressionEvaluator;
import net.skirnir.freyja.FreyjaRuntimeException;
import net.skirnir.freyja.StringUtils;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.Default;
import net.skirnir.freyja.zpt.TalTagEvaluator;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerTalTagEvaluator extends TalTagEvaluator {
    private static final String PREFIX_STRING_EXPRESSION = "string:";

    private static final String KW_LOCAL = "local";

    private static final String KW_GLOBAL = "global ";

    private static final Set<String> BOOLEAN_ATTRIBUTE_SET = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("compact",
                    "noshade", "multiple", "readonly", "disabled", "nohref",
                    "ismap", "noresize", "checked", "selected", "declare",
                    "defer", "nowrap")));

    private static final String TAL_DEFINE = "tal:define";

    private static final String TAL_REPEAT = "tal:repeat";

    private static final String TAL_ATTRIBUTES = "tal:attributes";

    @Override
    public String[] getSpecialTagPatternStrings() {
        return new String[] { "form", "input", "select", "textarea", "button" };
    }

    @Override
    public String[] getSpecialAttributePatternStrings() {
        return (String[]) ArrayUtil.add(super
                .getSpecialAttributePatternStrings(), new String[] { "href",
            "src" });
    }

    public TemplateContext newContext() {
        return new AnalyzerContext();
    }

    @SuppressWarnings("deprecation")
    public String evaluate(TemplateContext context, String name,
            Attribute[] attrs, Element[] body) {
        AnalyzerContext analyzerContext = toAnalyzerContext(context);

        Attribute defineAttr = null;
        Attribute repeatAttr = null;
        for (int i = 0; i < attrs.length; i++) {
            if (TAL_DEFINE.equals(attrs[i].getName())) {
                defineAttr = attrs[i];
            } else if (TAL_REPEAT.equals(attrs[i].getName())) {
                repeatAttr = attrs[i];
            }
        }
        boolean variableScopePushed = false;
        boolean repeatInfoPushed = false;
        String repeatVarname = null;
        try {
            if (defineAttr != null) {
                analyzerContext.pushVariableScope();
                variableScopePushed = true;
                try {
                    analyzerContext.setProcessingAttributeName(TAL_DEFINE);

                    processDefine(analyzerContext, context
                            .getExpressionEvaluator(), context
                            .getVariableResolver(), defineAttr);
                } finally {
                    analyzerContext.setProcessingAttributeName(null);
                }
            }

            if (repeatAttr != null) {
                ZptTemplateContext.RepeatInfo repeatInfo = null;
                Object[] repeatObjs = null;

                if (!variableScopePushed) {
                    analyzerContext.pushVariableScope();
                    variableScopePushed = true;
                }

                String statement = TagEvaluatorUtils.defilter(repeatAttr
                        .getValue());
                int sp = statement.indexOf(' ');
                if (sp < 0) {
                    throw new EvaluationRuntimeException("Syntax error: "
                            + statement).setLineNumber(
                            repeatAttr.getLineNumber()).setColumnNumber(
                            repeatAttr.getColumnNumber());
                }
                repeatVarname = statement.substring(0, sp).trim();
                String value = statement.substring(sp + 1).trim();

                String oldTargetName = analyzerContext.getTargetName();
                try {
                    analyzerContext.setProcessingAttributeName(TAL_REPEAT);
                    analyzerContext.setTargetName(repeatVarname);

                    repeatObjs = toArray(context.getExpressionEvaluator()
                            .evaluate(context, context.getVariableResolver(),
                                    value));
                } catch (FreyjaRuntimeException ex) {
                    throw ex.setLineNumber(repeatAttr.getLineNumber())
                            .setColumnNumber(repeatAttr.getColumnNumber());
                } catch (RuntimeException ex) {
                    throw new EvaluationRuntimeException(
                            "Can't evaluate tal:repeat expression", ex)
                            .setLineNumber(repeatAttr.getLineNumber())
                            .setColumnNumber(repeatAttr.getColumnNumber());
                } finally {
                    analyzerContext.setTargetName(oldTargetName);
                    analyzerContext.setProcessingAttributeName(null);
                }

                repeatInfo = analyzerContext.pushRepeatInfo(repeatVarname,
                        repeatObjs);
                repeatInfoPushed = true;

                if (repeatObjs.length > 0) {
                    analyzerContext.defineVariable(
                            ZptTemplateContext.SCOPE_LOCAL, repeatVarname,
                            repeatObjs[0]);
                    repeatInfo.update();
                }
            }

            AnnotationResult result = findAnnotation(context, name, attrs);
            String annotation = result.getAnnotation();
            attrs = result.getTheOtherAttributes();
            Map<String, Attribute> attrMap = evaluate(analyzerContext, attrs);
            Set<String> runtimeAttributeNameSet = getRuntimeAttributeNameSet(
                    analyzerContext, attrs);

            if ("form".equals(name)) {
                analyzerContext.setFormDesc(registerTransitionClassDesc(
                        analyzerContext, attrMap, runtimeAttributeNameSet,
                        "action", getAttributeValue(attrMap, "method", "GET")
                                .toUpperCase(), true));
                try {
                    return super.evaluate(context, name, attrs, body);
                } finally {
                    FormDesc formDesc = analyzerContext.getFormDesc();
                    if (formDesc != null) {
                        formDesc.close();
                        analyzerContext.setFormDesc(null);
                    }
                }
            } else if (("input".equals(name) || "select".equals(name)
                    || "textarea".equals(name) || "button".equals(name))
                    && !runtimeAttributeNameSet.contains("name")) {
                // nameの値が実行時に決まる場合は正しくプロパティやメソッドを生成できないので、
                // nameの値が定数である場合のみ処理を行なうようにしている。
                do {
                    FormDesc formDesc = analyzerContext.getFormDesc();
                    if (formDesc == null) {
                        break;
                    }

                    String type = getAttributeValue(attrMap, "type", null);
                    if ("input".equals(name)
                            && ("submit".equals(type) || "button".equals(type) || "image"
                                    .equals(type)) || "button".equals(name)
                            && ("submit".equals(type) || "button".equals(type))) {
                        formDesc.setActionMethodDesc(getAttributeValue(attrMap,
                                "name", null));
                        break;
                    }

                    PropertyDesc propertyDesc = processParameterTag(
                            analyzerContext, attrMap, annotation, formDesc);
                    if (propertyDesc == null) {
                        break;
                    }

                    String parameterName = getAttributeValue(attrMap, "name",
                            null);
                    boolean isRadio = "input".equals(name)
                            && "radio".equals(type);
                    formDesc.addParameter(parameterName, isRadio);

                    TypeDesc typeDesc = propertyDesc.getTypeDesc();
                    if (!propertyDesc
                            .isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)) {
                        if (analyzerContext.isInRepeat()) {
                            // repeatタグの中であれば同一名のinputタグが複数あることになるため、プロパティの型をコレクションにする。
                            // ただし添え字つきパラメータの場合は、同一名のinputタグが複数存在するわけではないため、
                            // コレクションにはしない。
                            // ラジオボタンの時もコレクションにはしない。
                            if (parameterName
                                    .indexOf(AnalyzerContext.CHAR_ARRAY_LPAREN) < 0
                                    && !isRadio) {
                                analyzerContext.getSourceCreator()
                                        .setToCollection(typeDesc, null);
                            }
                        } else {
                            if (formDesc.isMultipleParameter(parameterName)) {
                                analyzerContext.getSourceCreator()
                                        .setToCollection(typeDesc, null);
                            }
                        }
                        if ("input".equals(name)) {
                            if ("file".equals(type)) {
                                typeDesc.setComponentClassDesc(FormFile.class);
                            }
                        }
                    }

                    PropertyDesc firstPropertyDesc = formDesc
                            .getActionPageClassDesc().getPropertyDesc(
                                    getFirstSimpleSegment(parameterName));

                    if (formDesc.getDtoClassDesc() != null) {
                        PropertyDesc cloned = firstPropertyDesc
                                .transcriptTo(firstPropertyDesc.getDescPool()
                                        .newPropertyDesc(
                                                firstPropertyDesc.getName()));
                        // Pageのフォーム要素プロパティとフォームDtoのプロパティは型情報を同期させる必要があるため、
                        // 同じTypeDescを指すようにしておく。
                        cloned.setTypeDesc(firstPropertyDesc.getTypeDesc());
                        formDesc.getDtoClassDesc().setPropertyDesc(cloned);
                        firstPropertyDesc
                                .setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                                        org.seasar.ymir.extension.Globals.META_NAME_FORMPROPERTY,
                                        new String[] { formDesc.getName() },
                                        new Class[0]));
                        firstPropertyDesc
                                .setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                                        org.seasar.ymir.extension.Globals.META_NAME_FORMPROPERTY,
                                        new String[] { formDesc.getName() },
                                        new Class[0]));
                    }

                    if (BeanUtils.isSingleSegment(parameterName)) {
                        propertyDesc
                                .setAnnotationDescOnSetter(new AnnotationDescImpl(
                                        RequestParameter.class.getName()));
                        DescUtils.addParameter(propertyDesc, parameterName);
                    } else {
                        firstPropertyDesc
                                .setAnnotationDescOnGetter(new AnnotationDescImpl(
                                        RequestParameter.class.getName()));
                        DescUtils
                                .addParameter(firstPropertyDesc, parameterName);
                    }
                } while (false);
            } else if ("option".equals(name)) {
                Class<?> optionClass = Option.class;
                if (!analyzerContext.getSourceCreator()
                        .getSourceCreatorSetting().isOnDtoSearchPath(
                                optionClass.getName())) {
                    optionClass = net.skirnir.freyja.render.html.Option.class;
                    if (!analyzerContext.getSourceCreator()
                            .getSourceCreatorSetting().isOnDtoSearchPath(
                                    optionClass.getName())) {
                        optionClass = null;
                    }
                }
                if (optionClass != null) {
                    String returned = super
                            .evaluate(context, name, attrs, body);
                    String statement = getAttributeValue(attrMap, "tal:repeat",
                            null);
                    if (statement != null) {
                        Object evaluated = context.getExpressionEvaluator()
                                .evaluate(
                                        context,
                                        context.getVariableResolver(),
                                        statement.substring(
                                                statement.indexOf(' ')).trim());
                        if (evaluated instanceof DescWrapper[]) {
                            PropertyDesc pd = ((DescWrapper[]) evaluated)[0]
                                    .getPropertyDesc();
                            TypeDesc td = pd.getTypeDesc();
                            if (pd != null
                                    && !pd.isTypeAlreadySet(PROBABILITY_TYPE)) {
                                td.setComponentClassDesc(optionClass);
                                td.setCollection(true);
                                if (analyzerContext
                                        .isRepeatedPropertyGeneratedAsList()) {
                                    td.setCollectionClass(List.class);
                                } else {
                                    td.setCollectionClass(null);
                                }
                                pd.notifyTypeUpdated(PROBABILITY_TYPE);
                            }
                        }
                    }
                    return returned;
                }
            } else {
                registerTransitionClassDesc(analyzerContext, attrMap,
                        runtimeAttributeNameSet, "href", "GET", false);
                registerTransitionClassDesc(analyzerContext, attrMap,
                        runtimeAttributeNameSet, "src", "GET", false);
            }

            return super.evaluate(context, name, attrs, body);
        } finally {
            if (variableScopePushed) {
                analyzerContext.popVariableScope();
            }
            if (repeatInfoPushed) {
                analyzerContext.popRepeatInfo(repeatVarname);
            }
        }
    }

    Set<String> getRuntimeAttributeNameSet(AnalyzerContext analyzerContext,
            Attribute[] attrs) {
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < attrs.length; i++) {
            if ("tal:attributes".equals(attrs[i].getName())) {
                String[] statements = parseStatements(TagEvaluatorUtils
                        .defilter(attrs[i].getValue()));
                for (int j = 0; j < statements.length; j++) {
                    String statement = statements[j];
                    int delim = statement.indexOf(' ');
                    String name = statement.substring(0, delim).trim();
                    String expression = statement.substring(delim).trim();
                    // 添え字の中だけは実行時パラメータを含んでいて良い。
                    if (isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                            analyzerContext, expression)
                            || isDefinedAndStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                                    analyzerContext, expression)) {
                        continue;
                    }
                    set.add(name);
                }
                break;
            }
        }
        return set;
    }

    boolean isDefinedAndStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
            AnalyzerContext analyzerContext, String expression) {
        if (!AnalyzerUtils.isValidVariableName(expression)) {
            return false;
        }
        return isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
                analyzerContext, analyzerContext
                        .getDefinedVariableExpression(expression));
    }

    boolean isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
            AnalyzerContext analyzerContext, String expression) {
        if (expression == null) {
            return false;
        }
        if (!expression.startsWith(PREFIX_STRING_EXPRESSION)) {
            return false;
        }
        expression = expression.substring(PREFIX_STRING_EXPRESSION.length())
                .trim();
        // FIXME 結構適当。
        int pre = 0;
        int idx;
        while ((idx = expression.indexOf('$', pre)) >= 0) {
            if (idx == 0 || expression.charAt(idx - 1) != '[') {
                return false;
            }
            if (idx + 1 < expression.length()
                    && expression.charAt(idx + 1) == '{') {
                // ${...}。
                int rightParen = findEndEdge(expression, idx + 1);
                if (rightParen + 1 >= expression.length()
                        || expression.charAt(rightParen + 1) != ']') {
                    analyzerContext.addWarning(expression, rightParen + 2);
                    return false;
                }
                pre = rightParen + 2;
            } else {
                // $...。
                int rightEdge = expression.indexOf(']', idx + 1);
                if (rightEdge < 0
                        || !AnalyzerUtils.isValidVariableName(expression
                                .substring(idx + 1, rightEdge))) {
                    analyzerContext.addWarning(expression, idx + 2);
                    return false;
                }
                pre = rightEdge + 1;
            }
        }
        return true;
    }

    int findEndEdge(String expression, int idx) {
        int depth = 0;
        for (; idx < expression.length(); idx++) {
            char ch = expression.charAt(idx);
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
            }
            if (depth == 0) {
                break;
            }
        }
        return idx;
    }

    FormDesc registerTransitionClassDesc(AnalyzerContext analyzerContext,
            Map<String, Attribute> attrMap,
            Set<String> runtimeAttributeNameSet, String attrName,
            String methodName, boolean form) {
        SourceCreator creator = analyzerContext.getSourceCreator();
        String url = getAttributeValue(attrMap, attrName, null);
        if ("#".equals(url)) {
            // "#"の時は自動生成の対象外とする。
            return null;
        }

        Path path = constructPath(analyzerContext.getPath(), url);
        if (path == null) {
            return null;
        }
        HttpMethod method = HttpMethod.enumOf(methodName);

        ClassDesc classDesc = creator.buildTransitionClassDesc(DescPool
                .getDefault(), path.getTrunk(), method, path.getParameterMap());
        if (classDesc == null) {
            return null;
        }

        if (form) {
            ClassDesc dtoClassDesc = null;
            String formName = null;
            if (creator.getSourceCreatorSetting()
                    .isFormDtoCreationFeatureEnabled()
                    && !runtimeAttributeNameSet.contains("name")) {
                String name = getAttributeValue(attrMap, "name", null);
                if (AnalyzerUtils.isValidVariableName(name)) {
                    formName = name;
                    PropertyDesc propertyDesc = analyzerContext
                            .getSourceCreator().addPropertyDesc(classDesc,
                                    name, PropertyDesc.NONE,
                                    analyzerContext.getPageClassName());
                    propertyDesc
                            .setAnnotationDesc(new MetaAnnotationDescImpl(
                                    org.seasar.ymir.extension.Globals.META_NAME_PROPERTY,
                                    new String[] { name }, new Class[0]));
                    dtoClassDesc = propertyDesc.getTypeDesc()
                            .getComponentClassDesc();
                    dtoClassDesc.setAttribute(Globals.ATTR_FORMDTO,
                            Boolean.TRUE);
                    dtoClassDesc
                            .setAttribute(Globals.ATTR_OWNERPAGE, classDesc);
                }
            }

            return new FormDescImpl(creator, classDesc, dtoClassDesc, formName,
                    path.getTrunk(), method);
        } else {
            MethodDesc methodDesc = creator.newActionMethodDesc(classDesc
                    .getDescPool(), path.getTrunk(), method,
                    new ActionSelectorSeedImpl());
            classDesc.setMethodDesc(methodDesc);

            // _prerender()を追加する。
            MethodDesc prerenderMethodDesc = creator
                    .newPrerenderActionMethodDesc(classDesc.getDescPool(), path
                            .getTrunk(), method, new ActionSelectorSeedImpl());
            classDesc.setMethodDesc(prerenderMethodDesc);

            return null;
        }
    }

    Path constructPath(String basePath, String pathWithParameters) {
        if (pathWithParameters == null) {
            return null;
        } else {
            return new Path(ServletUtils.toAbsolutePath(basePath,
                    pathWithParameters));
        }
    }

    AnnotationResult findAnnotation(TemplateContext context, String name,
            Attribute[] attrs) {
        String behaviorDuplicateTag = context
                .getProperty("behavior.duplicate-tag");
        String annotation = null;
        List<Attribute> attrList = new ArrayList<Attribute>();
        for (int i = 0; i < attrs.length; i++) {
            if ("tal:annotation".equals(attrs[i].getName())) {
                if (annotation != null) {
                    if (!"ignore".equals(behaviorDuplicateTag)) {
                        throw new EvaluationRuntimeException(
                                "Duplicate tag found: "
                                        + attrs[i].getName()
                                        + ": "
                                        + TagEvaluatorUtils.getBeginTagString(
                                                name, attrs)).setLineNumber(
                                attrs[i].getLineNumber()).setColumnNumber(
                                attrs[i].getColumnNumber());
                    }
                }
                annotation = attrs[i].getValue();
            } else {
                attrList.add(attrs[i]);
            }
        }
        return new AnnotationResult(annotation, attrList
                .toArray(new Attribute[0]));
    }

    PropertyDesc processParameterTag(AnalyzerContext context,
            Map<String, Attribute> attrMap, String annotation, FormDesc formDesc) {
        String name = getAttributeValue(attrMap, "name", null);
        if (name != null && shouldGeneratePropertyForParameter(name)) {
            return context.getRequestParameterPropertyDesc(formDesc
                    .getActionPageClassDesc(), name, PropertyDesc.READ
                    | PropertyDesc.WRITE);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    Map<String, Attribute> evaluate(ZptTemplateContext context,
            Attribute[] attrs) {
        ExpressionEvaluator expEvaluator = context.getExpressionEvaluator();
        VariableResolver varResolver = context.getVariableResolver();

        List<Attribute> attrList = new ArrayList<Attribute>();
        Attribute attributesAttr = null;
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().startsWith("tal:")) {
                if (TAL_ATTRIBUTES.equals(attrs[i].getName())) {
                    attributesAttr = attrs[i];
                }
            } else {
                attrList.add(attrs[i]);
            }
        }
        if (attributesAttr != null) {
            try {
                context.setProcessingAttributeName(TAL_ATTRIBUTES);

                attrs = processAttributes(context, expEvaluator, varResolver,
                        attributesAttr, attrs, true);
            } finally {
                context.setProcessingAttributeName(null);
            }
        }
        return TagEvaluatorUtils.toMap(attrs);
    }

    AnalyzerContext toAnalyzerContext(TemplateContext context) {
        return (AnalyzerContext) context;
    }

    String getAttributeValue(Map<String, Attribute> attrMap, String name,
            String defaultValue) {
        Attribute attr = attrMap.get(name);
        if (attr != null) {
            return TagEvaluatorUtils.defilter(attr.getValue());
        } else {
            return defaultValue;
        }
    }

    static class AnnotationResult {
        private String annotation_;

        private Attribute[] theOtherAttributes_;

        public AnnotationResult(String annotation,
                Attribute[] theOtherAttributes) {
            annotation_ = annotation;
            theOtherAttributes_ = theOtherAttributes;
        }

        public String getAnnotation() {
            return annotation_;
        }

        public Attribute[] getTheOtherAttributes() {
            return theOtherAttributes_;
        }
    }

    @Override
    protected boolean evaluateCondition(TemplateContext context,
            ExpressionEvaluator expEvaluator, VariableResolver varResolver,
            String condition) {
        if (condition.trim().length() == 0) {
            return false;
        }

        return evaluateIfTrue(context, expEvaluator, varResolver, condition);
    }

    @Override
    protected boolean evaluateOmitTag(TemplateContext context,
            ExpressionEvaluator expEvaluator, VariableResolver varResolver,
            String condition) {
        if (condition.trim().length() == 0) {
            return true;
        }

        return evaluateIfTrue(context, expEvaluator, varResolver, condition);
    }

    protected boolean evaluateIfTrue(TemplateContext context,
            ExpressionEvaluator expEvaluator, VariableResolver varResolver,
            String condition) {
        Object evaluated = expEvaluator.evaluate(context, varResolver,
                condition);
        if (evaluated instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) evaluated;
            PropertyDesc pd = wrapper.getPropertyDesc();
            if (pd != null
                    && !pd.isTypeAlreadySet(PropertyDesc.PROBABILITY_MAXIMUM)) {
                pd.decrementReferCount();
                pd.setMayBoolean(true);
            }
        }

        return expEvaluator.isTrue(evaluated);
    }

    @Override
    protected void processDefine(ZptTemplateContext talContext,
            ExpressionEvaluator expEvaluator, VariableResolver varResolver,
            Attribute attr) {
        super.processDefine(talContext, expEvaluator, varResolver, attr);

        AnalyzerContext analyzerContext = toAnalyzerContext(talContext);
        String[] statements = parseStatements(TagEvaluatorUtils.defilter(attr
                .getValue()));
        for (int i = 0; i < statements.length; i++) {
            String statement = statements[i];
            int scope = ZptTemplateContext.SCOPE_LOCAL;

            if (statement.startsWith(KW_LOCAL)) {
                scope = ZptTemplateContext.SCOPE_LOCAL;
                statement = StringUtils.trimLeft(statement.substring(KW_LOCAL
                        .length()));
            } else if (statement.startsWith(KW_GLOBAL)) {
                scope = ZptTemplateContext.SCOPE_GLOBAL;
                statement = StringUtils.trimLeft(statement.substring(KW_GLOBAL
                        .length()));
            }

            int sp = statement.indexOf(' ');
            String varname = statement.substring(0, sp).trim();
            String value = StringUtils.trimLeft(statement.substring(sp + 1));
            analyzerContext.defineVariableExpression(scope, varname, value);
        }
    }

    protected Attribute[] processAttributes(ZptTemplateContext talContext,
            ExpressionEvaluator expEvaluator, VariableResolver varResolver,
            Attribute attr, Attribute[] htmlAttrs, boolean isHTML) {
        Map<String, Object> attrMap = new LinkedHashMap<String, Object>();
        String[] statements = parseStatements(TagEvaluatorUtils.defilter(attr
                .getValue()));
        for (int i = 0; i < statements.length; i++) {
            String statement = statements[i];
            int sp = statement.indexOf(' ');
            if (sp < 0) {
                throw new EvaluationRuntimeException("Syntax error: "
                        + statement).setLineNumber(attr.getLineNumber())
                        .setColumnNumber(attr.getColumnNumber());
            }
            String varname = statement.substring(0, sp).trim();
            String value = StringUtils.trimLeft(statement.substring(sp + 1));
            String oldTargetName = talContext.getTargetName();
            Object evaluated;
            try {
                talContext.setTargetName(varname);

                evaluated = expEvaluator.evaluate(talContext, varResolver,
                        value);
            } catch (FreyjaRuntimeException ex) {
                throw ex.setLineNumber(attr.getLineNumber()).setColumnNumber(
                        attr.getColumnNumber());
            } catch (RuntimeException ex) {
                throw new EvaluationRuntimeException(
                        "Can't evaluate tal:attributes expression", ex)
                        .setLineNumber(attr.getLineNumber()).setColumnNumber(
                                attr.getColumnNumber());
            } finally {
                talContext.setTargetName(oldTargetName);
            }
            if (evaluated != Default.instance) {
                if (isHTML && BOOLEAN_ATTRIBUTE_SET.contains(varname)) {
                    if (evaluated instanceof DescWrapper) {
                        changeTypeToBoolean((DescWrapper) evaluated);
                    }
                    // boolean属性についてはTALES式の評価値を
                    // true/false/defaultのいずれかに変換しておく。
                    evaluated = Boolean.valueOf(expEvaluator.isTrue(evaluated));
                } else if (evaluated != null) {
                    evaluated = renderEvaluatedValue(talContext, evaluated);
                }
            }
            attrMap.put(varname, evaluated);
        }

        List<Attribute> list = new ArrayList<Attribute>(htmlAttrs.length
                + attrMap.size());
        for (int i = 0; i < htmlAttrs.length; i++) {
            String key = htmlAttrs[i].getName();
            if (attrMap.containsKey(key)) {
                Object evaluated = attrMap.get(key);
                if (isHTML && BOOLEAN_ATTRIBUTE_SET.contains(key)) {
                    // boolean属性の場合の処理を行なう。
                    if (evaluated == Default.instance
                            || ((Boolean) evaluated).booleanValue()) {
                        String quote = htmlAttrs[i].getQuote();
                        if (quote.length() == 0) {
                            quote = "\"";
                        }
                        list.add(new Attribute(key, key, quote));
                    }
                } else {
                    // boolean属性以外の属性の処理を行なう。
                    if (evaluated != null) {
                        String evaluatedString;
                        if (Default.instance.equals(evaluated)) {
                            evaluatedString = htmlAttrs[i].getValue();
                        } else {
                            evaluatedString = TagEvaluatorUtils
                                    .filter(evaluated.toString());
                        }
                        list.add(new Attribute(key, evaluatedString,
                                htmlAttrs[i].getQuote()));
                    }
                }
                attrMap.remove(key);
            } else {
                list.add(htmlAttrs[i]);
            }
        }
        for (Iterator<Map.Entry<String, Object>> itr = attrMap.entrySet()
                .iterator(); itr.hasNext();) {
            Map.Entry<String, Object> entry = itr.next();
            String key = entry.getKey();
            Object evaluated = entry.getValue();
            if (isHTML && BOOLEAN_ATTRIBUTE_SET.contains(key)) {
                // boolean属性の場合の処理を行なう。
                if (evaluated != Default.instance
                        && ((Boolean) evaluated).booleanValue()) {
                    list.add(new Attribute(key, key, "\""));
                }
            } else {
                // boolean属性以外の属性の処理を行なう。
                if (evaluated != null && evaluated != Default.instance) {
                    String evaluatedString = TagEvaluatorUtils.filter(evaluated
                            .toString());
                    list.add(new Attribute(key, evaluatedString, "\""));
                }
            }
        }

        return list.toArray(new Attribute[0]);
    }

    void changeTypeToBoolean(DescWrapper wrapper) {
        PropertyDesc propertyDesc = wrapper.getPropertyDesc();
        if (propertyDesc == null) {
            return;
        }
        if (!propertyDesc.isTypeAlreadySet(PROBABILITY_BOOLEAN_ATTRIBUTE)) {
            propertyDesc.setTypeDesc(Boolean.TYPE);
            propertyDesc.notifyTypeUpdated(PROBABILITY_BOOLEAN_ATTRIBUTE);
        }
    }
}
