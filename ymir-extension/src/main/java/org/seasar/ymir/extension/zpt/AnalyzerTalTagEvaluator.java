package org.seasar.ymir.extension.zpt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Path;
import org.seasar.ymir.annotation.RequestParameter;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.FormDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;
import org.seasar.ymir.util.BeanUtils;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.ExpressionEvaluator;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.TalTagEvaluator;
import net.skirnir.freyja.zpt.ZptTemplateContext;

public class AnalyzerTalTagEvaluator extends TalTagEvaluator {
    private static final String SEGMENT_PARENT = "..";

    private static final String SEGMENT_CURRENT = ".";

    private static final String PREFIX_STRING_EXPRESSION = "string:";

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

    public String evaluate(TemplateContext context, String name,
            Attribute[] attrs, Element[] body) {
        AnnotationResult result = findAnnotation(context, name, attrs);
        String annotation = result.getAnnotation();
        attrs = result.getTheOtherAttributes();
        AnalyzerContext analyzerContext = toAnalyzerContext(context);
        Map<String, Attribute> attrMap = evaluate(analyzerContext, attrs);
        Set<String> runtimeAttributeNameSet = getRuntimeAttributeNameSet(attrs);

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

                if (!propertyDesc.getTypeDesc().isExplicit()) {
                    if ("input".equals(name)) {
                        if ("file".equals(type)) {
                            propertyDesc.getTypeDesc().setClassDesc(
                                    FormFile.class.getName());
                        } else if ("radio".equals(type)) {
                            propertyDesc.getTypeDesc().setArray(false);
                        }
                    }
                }

                String parameterName = getAttributeValue(attrMap, "name", null);
                if (BeanUtils.isSingleSegment(parameterName)
                        && formDesc.getDtoClassDesc() != null) {
                    formDesc.getDtoClassDesc().setPropertyDesc(
                            (PropertyDesc) propertyDesc.clone());
                    propertyDesc
                            .setAnnotationDescForGetter(new MetaAnnotationDescImpl(
                                    org.seasar.ymir.extension.Globals.META_NAME_FORMPROPERTY,
                                    new String[] { formDesc.getName() },
                                    new Class[0]));
                    propertyDesc
                            .setAnnotationDescForSetter(new MetaAnnotationDescImpl(
                                    org.seasar.ymir.extension.Globals.META_NAME_FORMPROPERTY,
                                    new String[] { formDesc.getName() },
                                    new Class[0]));
                }

                if (BeanUtils.isSingleSegment(parameterName)) {
                    propertyDesc
                            .setAnnotationDescForSetter(new AnnotationDescImpl(
                                    RequestParameter.class.getName()));
                } else {
                    formDesc.getClassDesc().getPropertyDesc(
                            BeanUtils.getFirstSimpleSegment(parameterName))
                            .setAnnotationDescForGetter(
                                    new AnnotationDescImpl(
                                            RequestParameter.class.getName()));
                }
            } while (false);
        } else if ("option".equals(name)
                && analyzerContext.isUsingFreyjaRenderClasses()) {
            String returned = super.evaluate(context, name, attrs, body);
            String statement = getAttributeValue(attrMap, "tal:repeat", null);
            if (statement != null) {
                Object evaluated = context.getExpressionEvaluator().evaluate(
                        context, context.getVariableResolver(),
                        statement.substring(statement.indexOf(' ')).trim());
                if (evaluated instanceof DescWrapper[]) {
                    PropertyDesc pd = ((DescWrapper[]) evaluated)[0]
                            .getPropertyDesc();
                    if (pd != null && !pd.getTypeDesc().isExplicit()) {
                        pd
                                .setTypeDesc("net.skirnir.freyja.render.html.OptionTag[]");
                    }
                }
            }
            return returned;
        } else {
            registerTransitionClassDesc(analyzerContext, attrMap,
                    runtimeAttributeNameSet, "href", "GET", false);
            registerTransitionClassDesc(analyzerContext, attrMap,
                    runtimeAttributeNameSet, "src", "GET", false);
        }

        return super.evaluate(context, name, attrs, body);
    }

    Set<String> getRuntimeAttributeNameSet(Attribute[] attrs) {
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
                    if (!isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(expression)) {
                        set.add(name);
                    }
                }
                break;
            }
        }
        return set;
    }

    boolean isStringTypeExpressionAndContainsRuntimeParameterOnlyAsIndex(
            String expression) {
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
                    return false;
                }
                pre = rightParen + 2;
            } else {
                // $...。
                int rightEdge = expression.indexOf(']', idx + 1);
                if (rightEdge < 0
                        || !AnalyzerUtils.isValidVariableName(expression
                                .substring(idx + 1, rightEdge))) {
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
        MatchedPathMapping matched = creator.findMatchedPathMapping(path
                .getTrunk(), method);
        if (matched == null || matched.isDenied()) {
            return null;
        }
        String className = creator.getClassName(matched.getPageComponentName());
        if (className == null) {
            return null;
        }
        ClassDesc classDesc = analyzerContext.getTemporaryClassDesc(className);
        for (Iterator<String> itr = path.getParameterMap().keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (!shouldGeneratePropertyForParameter(name)) {
                continue;
            }
            PropertyDesc propertyDesc;
            if (BeanUtils.isSingleSegment(name)) {
                propertyDesc = classDesc.addProperty(name, PropertyDesc.WRITE
                        | PropertyDesc.READ);
                propertyDesc.setAnnotationDescForSetter(new AnnotationDescImpl(
                        RequestParameter.class.getName()));
            } else {
                propertyDesc = classDesc.addProperty(BeanUtils
                        .getFirstSimpleSegment(name), PropertyDesc.READ);
                propertyDesc.setAnnotationDescForGetter(new AnnotationDescImpl(
                        RequestParameter.class.getName()));
            }
            analyzerContext.adjustPropertyType(classDesc.getName(),
                    propertyDesc);
            propertyDesc.notifyUpdatingType();
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
                    dtoClassDesc = analyzerContext
                            .getTemporaryClassDesc(analyzerContext
                                    .fromPropertyNameToClassName(classDesc,
                                            name));
                    PropertyDesc propertyDesc = classDesc.addProperty(name,
                            PropertyDesc.NONE);
                    propertyDesc
                            .setAnnotationDesc(new MetaAnnotationDescImpl(
                                    org.seasar.ymir.extension.Globals.META_NAME_PROPERTY,
                                    new String[] { name }, new Class[0]));
                    propertyDesc.setTypeDesc(new TypeDescImpl(dtoClassDesc));
                    classDesc.setPropertyDesc(propertyDesc);
                }
            }

            return new FormDescImpl(creator, classDesc, dtoClassDesc, formName,
                    path.getTrunk(), method);
        } else {
            classDesc.setMethodDesc(creator.getExtraPathMapping(
                    path.getTrunk(), method).newActionMethodDesc(
                    new ActionSelectorSeedImpl()));

            return null;
        }
    }

    Path constructPath(String basePath, String pathWithParameters) {
        if (pathWithParameters == null) {
            return null;
        } else {
            return new Path(toAbsolutePath(basePath, pathWithParameters));
        }
    }

    String toAbsolutePath(String basePath, String path) {
        if (path == null) {
            return null;
        } else if (path.length() == 0 || path.startsWith(";")
                || path.startsWith("?") || path.startsWith("#")) {
            return basePath + path;
        }
        String absolutePath;
        if (path.startsWith("/")) {
            absolutePath = path;
        } else {
            int slash = basePath.lastIndexOf('/');
            if (slash >= 0) {
                absolutePath = basePath.substring(0, slash + 1) + path;
            } else {
                absolutePath = basePath + "/" + path;
            }
        }

        int pre = 0;
        int idx;
        LinkedList<String> segmentList = new LinkedList<String>();
        while ((idx = absolutePath.indexOf('/', pre)) >= 0) {
            String segment = absolutePath.substring(pre, idx);
            if (segment.equals(SEGMENT_PARENT)) {
                if (!segmentList.isEmpty()) {
                    segmentList.removeLast();
                }
            } else if (segment.equals(SEGMENT_CURRENT)) {
                ;
            } else {
                segmentList.addLast(segment);
            }
            pre = idx + 1;
        }
        String segment = absolutePath.substring(pre);
        if (segment.equals(SEGMENT_PARENT)) {
            if (!segmentList.isEmpty()) {
                segmentList.removeLast();
            }
        } else if (segment.equals(SEGMENT_CURRENT)) {
            ;
        } else {
            segmentList.addLast(segment);
        }
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (Iterator<String> itr = segmentList.iterator(); itr.hasNext();) {
            sb.append(delim).append(itr.next());
            delim = "/";
        }

        return sb.toString();
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
            return context.getPropertyDesc(formDesc.getClassDesc(), name,
                    PropertyDesc.READ | PropertyDesc.WRITE);
        }

        return null;
    }

    boolean shouldGeneratePropertyForParameter(String name) {
        if (name == null || name.startsWith(Globals.IDPREFIX)) {
            return false;
        }

        int pre = 0;
        int idx;
        while ((idx = name.indexOf('.', pre)) >= 0) {
            if (!shouldGeneratePropertyForParameterSegment(name.substring(pre,
                    idx))) {
                return false;
            }
            pre = idx + 1;
        }
        if (!shouldGeneratePropertyForParameterSegment(name.substring(pre))) {
            return false;
        }
        return true;
    }

    boolean shouldGeneratePropertyForParameterSegment(String name) {
        if (name == null) {
            return false;
        }
        if (name.endsWith("]")) {
            int lbracket = name.indexOf('[');
            if (lbracket < 0) {
                return false;
            }
            name = name.substring(0, lbracket);
        }
        return AnalyzerUtils.isValidVariableName(name);
    }

    @SuppressWarnings("unchecked")
    Map<String, Attribute> evaluate(ZptTemplateContext context,
            Attribute[] attrs) {
        ExpressionEvaluator expEvaluator = context.getExpressionEvaluator();
        VariableResolver varResolver = context.getVariableResolver();

        List<Attribute> attrList = new ArrayList<Attribute>();
        Attribute defineAttr = null;
        Attribute attributesAttr = null;
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().startsWith("tal:")) {
                if ("tal:attributes".equals(attrs[i].getName())) {
                    attributesAttr = attrs[i];
                } else if ("tal:define".equals(attrs[i].getName())) {
                    defineAttr = attrs[i];
                }
            } else {
                attrList.add(attrs[i]);
            }
        }
        if (attributesAttr != null) {
            boolean variableScopePushed = false;
            try {
                if (defineAttr != null) {
                    context.pushVariableScope();
                    variableScopePushed = true;
                    processDefine(context, expEvaluator, varResolver,
                            defineAttr);
                }
                attrs = processAttributes(context, expEvaluator, varResolver,
                        attributesAttr, attrs, true);
            } finally {
                if (variableScopePushed) {
                    context.popVariableScope();
                }
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
            if (pd != null && !pd.isTypeAlreadySet()) {
                pd.setTypeDesc("boolean");
            }
        }

        return expEvaluator.isTrue(evaluated);
    }
}
