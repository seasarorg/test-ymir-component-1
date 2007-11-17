package org.seasar.ymir.impl;

import static org.seasar.ymir.RequestProcessor.ACTION_DEFAULT;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.el.EvaluationException;
import org.seasar.kvasir.util.el.TextTemplateEvaluator;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.kvasir.util.el.impl.MapVariableResolver;
import org.seasar.kvasir.util.el.impl.SimpleTextTemplateEvaluator;
import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.MethodUtils;

public class PathMappingImpl implements PathMapping {
    public static final String KEY_DENIED = "denied";

    public static final String KEY_PATTERN = "pattern";

    public static final String KEY_PAGECOMPONENTNAME_TEMPLATE = "pageComponentNameTemplate";

    public static final String KEY_ACTIONNAME_TEMPLATE = "actionNameTemplate";

    public static final String KEY_PATHINFO_TEMPLATE = "pathInfoTemplate";

    public static final String KEY_PARAMETER_TEMPLATE = "parameterTemplate";

    public static final String KEY_DEFAULTRETURNVALUE = "defaultReturnValue";

    public static final String KEY_BUTTONNAMEPATTERN = "buttonNamePattern";

    private static final char PARAM_PREFIX_CHAR = '[';

    private static final char PARAM_SUFFIX_CHAR = ']';

    private static final String PARAM_PREFIX = String
            .valueOf(PARAM_PREFIX_CHAR);

    private static final String PARAM_SUFFIX = String
            .valueOf(PARAM_SUFFIX_CHAR);

    private TextTemplateEvaluator evaluator_ = new SimpleTextTemplateEvaluator();

    private Pattern pattern_;

    private String pageComponentNameTemplate_;

    private String actionNameTemplate_;

    private String pathInfoTemplate_;

    private String parameterTemplate_;

    private String defaultReturnValueTemplate_;

    private Object defaultReturnValue_;

    private Pattern buttonNamePatternForDispatching_;

    private String buttonNamePatternStringForDispatching_;

    private boolean denied_;

    private TypeConversionManager typeConversionManager_;

    private final Logger logger_ = Logger.getLogger(getClass());

    @Deprecated
    public PathMappingImpl(String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, Object defaultReturnValue,
            String buttonNamePatternStringForDispatching) {

        this(false, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, defaultReturnValue,
                buttonNamePatternStringForDispatching, null);
    }

    public PathMappingImpl(String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, Object defaultReturnValue,
            String buttonNamePatternStringForDispatching,
            String parameterTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, defaultReturnValue,
                buttonNamePatternStringForDispatching, parameterTemplate);
    }

    @Deprecated
    public PathMappingImpl(boolean denied, String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, Object defaultReturnValue,
            String buttonNamePatternStringForDispatching) {
        this(denied, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, defaultReturnValue,
                buttonNamePatternStringForDispatching, null);
    }

    public PathMappingImpl(boolean denied, String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, Object defaultReturnValue,
            String buttonNamePatternStringForDispatching,
            String parameterTemplate) {
        denied_ = denied;
        pattern_ = Pattern.compile(patternString);
        pageComponentNameTemplate_ = pageComponentNameTemplate;
        actionNameTemplate_ = actionNameTemplate;
        pathInfoTemplate_ = pathInfoTemplate;
        if (defaultReturnValue instanceof String) {
            defaultReturnValueTemplate_ = (String) defaultReturnValue;
        } else {
            defaultReturnValue_ = defaultReturnValue;
        }
        if (buttonNamePatternStringForDispatching != null) {
            buttonNamePatternStringForDispatching_ = buttonNamePatternStringForDispatching;
            buttonNamePatternForDispatching_ = Pattern
                    .compile(buttonNamePatternStringForDispatching);
        }
        parameterTemplate_ = parameterTemplate;
    }

    public PathMappingImpl(Map<String, Object> map) {
        denied_ = PropertyUtils.valueOf(map.get(KEY_DENIED), false);
        map.remove(KEY_DENIED);
        pattern_ = Pattern.compile(PropertyUtils.valueOf(map.get(KEY_PATTERN),
                (String) null));
        map.remove(KEY_PATTERN);
        pageComponentNameTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_PAGECOMPONENTNAME_TEMPLATE), (String) null);
        map.remove(KEY_PAGECOMPONENTNAME_TEMPLATE);
        actionNameTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_ACTIONNAME_TEMPLATE), (String) null);
        map.remove(KEY_ACTIONNAME_TEMPLATE);
        pathInfoTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_PATHINFO_TEMPLATE), (String) null);
        map.remove(KEY_PATHINFO_TEMPLATE);
        Object defaultReturnValue = map.get(KEY_DEFAULTRETURNVALUE);
        map.remove(KEY_DEFAULTRETURNVALUE);
        if (defaultReturnValue instanceof String) {
            defaultReturnValueTemplate_ = (String) defaultReturnValue;
        } else {
            defaultReturnValue_ = defaultReturnValue;
        }
        String buttonNamePatternStringForDispatching = PropertyUtils.valueOf(
                map.get(KEY_BUTTONNAMEPATTERN), (String) null);
        map.remove(KEY_BUTTONNAMEPATTERN);
        if (buttonNamePatternStringForDispatching != null) {
            buttonNamePatternStringForDispatching_ = buttonNamePatternStringForDispatching;
            buttonNamePatternForDispatching_ = Pattern
                    .compile(buttonNamePatternStringForDispatching);
        }
        parameterTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_PARAMETER_TEMPLATE), (String) null);
        map.remove(KEY_PARAMETER_TEMPLATE);

        if (!map.isEmpty()) {
            // キーのtypoなどを早めに検知するためにこうしている。
            StringBuilder sb = new StringBuilder();
            String delim = "Unknown key exists (typo?): ";
            for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
                sb.append(delim).append(itr.next());
                delim = ", ";
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public String getActionNameTemplate() {
        return actionNameTemplate_;
    }

    public String getPageComponentNameTemplate() {
        return pageComponentNameTemplate_;
    }

    public String getPathInfoTemplate() {
        return pathInfoTemplate_;
    }

    public String getDefaultReturnValueTemplate() {
        return defaultReturnValueTemplate_;
    }

    public Object getDefaultReturnValue() {
        return defaultReturnValue_;
    }

    public String getQueryStringTemplate() {
        return parameterTemplate_;
    }

    public Pattern getPattern() {
        return pattern_;
    }

    public String getButtonNamePatternStringForDispatching() {
        return buttonNamePatternStringForDispatching_;
    }

    public VariableResolver match(String path, String method) {
        Matcher matcher = pattern_.matcher(path);
        if (matcher.find()) {
            Map<String, String> prop = new HashMap<String, String>();
            int count = matcher.groupCount();
            for (int j = 0; j <= count; j++) {
                String matched = matcher.group(j);
                prop.put(String.valueOf(j), matched);
                prop.put(j + "u", upper(matched));
                prop.put(j + "l", lower(matched));
            }
            prop.put("`", path.substring(0, matcher.start()));
            prop.put("&", path.substring(matcher.start(), matcher.end()));
            prop.put("'", path.substring(matcher.end()));
            prop.put("METHOD", method);
            String lmethod = method.toLowerCase();
            prop.put("method", lmethod);
            prop.put("Method", upper(lmethod));

            return new MapVariableResolver(prop);
        } else {
            return null;
        }
    }

    String upper(String str) {
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    String lower(String str) {
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            return str;
        } else {
            return Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }

    public String getPageComponentName(VariableResolver resolver) {
        return evaluate(pageComponentNameTemplate_, resolver);
    }

    public String getActionName(VariableResolver resolver) {
        return evaluate(actionNameTemplate_, resolver);
    }

    public String getPathInfo(VariableResolver resolver) {
        return evaluate(pathInfoTemplate_, resolver);
    }

    public Map<String, String[]> getParameterMap(VariableResolver resolver) {
        if (parameterTemplate_ == null) {
            return null;
        }

        Map<String, String[]> map = new HashMap<String, String[]>();

        StringTokenizer st = new StringTokenizer(parameterTemplate_, ";");
        while (st.hasMoreTokens()) {
            String tkn = st.nextToken();
            int equal = tkn.indexOf("=");
            String name;
            String value;
            if (equal >= 0) {
                name = tkn.substring(0, equal);
                value = evaluate(tkn.substring(equal + 1), resolver);
            } else {
                name = tkn;
                value = "";
            }
            String[] current = map.get(name);
            if (current == null) {
                map.put(name, new String[] { value });
            } else {
                map.put(name, (String[]) ArrayUtil.add(current, value));
            }
        }

        return map;
    }

    public Object getDefaultReturnValue(VariableResolver resolver) {
        if (defaultReturnValueTemplate_ != null) {
            return evaluate(defaultReturnValueTemplate_, resolver);
        } else {
            return defaultReturnValue_;
        }
    }

    String evaluate(String template, VariableResolver resolver) {
        if (resolver == null || template == null) {
            return null;
        } else {
            try {
                return evaluator_.evaluateAsString(template, resolver);
            } catch (EvaluationException ex) {
                throw new RuntimeException("Can't evaluate template: "
                        + template + ", resolver=" + resolver, ex);
            }
        }
    }

    public boolean isDenied() {
        return denied_;
    }

    protected String extractButtonName(String name) {
        if (buttonNamePatternForDispatching_ != null) {
            Matcher matcher = buttonNamePatternForDispatching_.matcher(name);
            if (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    return matcher.group(1);
                } else {
                    // 「()」が指定されていない。
                    throw new IllegalArgumentException(
                            "parameter pattern must have ( ) specification: "
                                    + buttonNamePatternStringForDispatching_);
                }
            }
        }
        return null;
    }

    public boolean isDispatchingByButton() {
        return (buttonNamePatternForDispatching_ != null);
    }

    /**
     * アクションメソッドを実行するためのActionを構築して返します。
     * <p>実行すべきアクションメソッドが見つからなかった場合はnullを返します。</p>
     * 
     * @param pageClass ページクラス。
     * @param request Requestオブジェクト。
     * @param resolver VariableResolver。
     * @return 構築したAction。
     */
    public Action getAction(PageComponent pageComponent, final Request request,
            VariableResolver resolver) {
        final String actionName = getActionName(resolver);

        Action action = null;
        if (isDispatchingByButton()) {
            // ボタンに対応するアクションを探索する。
            action = (Action) pageComponent.accept(new PageComponentVisitor() {
                @Override
                public Object process(PageComponent pageComponent) {
                    return getActionForButton(pageComponent.getPage(),
                            pageComponent.getPageClass(), actionName, request);
                }
            });
            if (action == null) {
                // ボタンに対応するデフォルトアクションを探索する。
                action = (Action) pageComponent
                        .accept(new PageComponentVisitor() {
                            @Override
                            public Object process(PageComponent pageComponent) {
                                return getActionForButton(pageComponent
                                        .getPage(), pageComponent
                                        .getPageClass(), ACTION_DEFAULT,
                                        request);
                            }
                        });
            }
        }
        if (action == null) {
            // 通常のアクションを探索する。
            action = (Action) pageComponent.accept(new PageComponentVisitor() {
                @Override
                public Object process(PageComponent pageComponent) {
                    return getAction(pageComponent.getPage(), pageComponent
                            .getPageClass(), actionName, request);
                }
            });
        }
        if (action == null) {
            // デフォルトアクションを探索する。
            action = (Action) pageComponent.accept(new PageComponentVisitor() {
                @Override
                public Object process(PageComponent pageComponent) {
                    return getAction(pageComponent.getPage(), pageComponent
                            .getPageClass(), ACTION_DEFAULT, request);
                }
            });
        }
        return action;
    }

    protected Action getActionForButton(Object page, Class<?> pageClass,
            String actionName, Request request) {
        Method[] methods = ClassUtils.getMethods(pageClass);
        if (logger_.isDebugEnabled()) {
            logger_.debug("getActionByParameter: search " + pageClass + " for "
                    + actionName + " method...");
        }
        for (int i = 0; i < methods.length; i++) {
            Action action = createActionForButton(methods[i], actionName,
                    request, page);
            if (action != null) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("getActionByParameter: Found: " + methods[i]);
                }
                return action;
            }
        }

        return null;
    }

    protected Action createActionForButton(Method method, String actionName,
            Request request, Object page) {
        String name = method.getName();
        if (!name.startsWith(actionName)) {
            return null;
        }

        String buttonName = extractButtonName(name.substring(actionName
                .length()));
        if (buttonName == null) {
            return null;
        }

        if (request.getParameter(buttonName) != null
                || request.getParameter(buttonName + ".x") != null) {
            return new ActionImpl(page, new MethodInvokerImpl(method,
                    new Object[0]));
        } else {
            return createActionWithParameters(method, buttonName, request, page);
        }
    }

    Action createActionWithParameters(Method method, String buttonName,
            Request request, Object page) {
        String prefix = buttonName + PARAM_PREFIX;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String pname = itr.next();
            if (pname.startsWith(prefix)) {
                return new ActionImpl(page, new MethodInvokerImpl(method,
                        parseParameters(pname.substring(buttonName.length()),
                                parameterTypes)));
            }
        }
        return null;
    }

    Object[] parseParameters(String string, Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        int prefix = 0;
        boolean broken = false;
        for (int i = 0; i < parameterTypes.length; i++) {
            String param;
            if (broken) {
                param = null;
            } else {
                if (prefix < string.length()
                        && string.charAt(prefix) == PARAM_PREFIX_CHAR) {
                    int suffix = string.indexOf(PARAM_SUFFIX_CHAR, prefix + 1);
                    if (suffix >= 0) {
                        param = string.substring(prefix + 1, suffix);
                        prefix = suffix + 1;
                    } else {
                        param = null;
                        broken = true;
                    }
                } else {
                    param = null;
                    broken = true;
                }
            }
            parameters[i] = typeConversionManager_.convert(param,
                    parameterTypes[i]);
        }
        return parameters;
    }

    protected Action getAction(Object page, Class<?> pageClass,
            String actionName, Request request) {
        Method method = MethodUtils.getMethod(pageClass, actionName);
        if (method != null) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("getAction: Found: " + method);
            }
            return new ActionImpl(page, new MethodInvokerImpl(method,
                    new Object[0]));
        } else {
            return null;
        }
    }
}
