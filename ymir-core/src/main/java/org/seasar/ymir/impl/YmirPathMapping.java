package org.seasar.ymir.impl;

import static org.seasar.ymir.util.RegexUtils.toRegexPattern;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.el.EvaluationException;
import org.seasar.kvasir.util.el.TextTemplateEvaluator;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.kvasir.util.el.impl.MapVariableResolver;
import org.seasar.kvasir.util.el.impl.SimpleTextTemplateEvaluator;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.util.ClassUtils;

public class YmirPathMapping implements PathMapping {
    public static final String KEY_DENIED = "denied";

    public static final String KEY_IGNORED = "ignored";

    public static final String KEY_PATTERN = "pattern";

    public static final String KEY_TEMPLATE = "template";

    public static final String KEY_PAGECOMPONENTNAME_PATTERN = "pageComponentNamePattern";

    public static final String KEY_PAGECOMPONENTNAME_TEMPLATE = "pageComponentNameTemplate";

    public static final String KEY_ACTIONNAME_TEMPLATE = "actionNameTemplate";

    public static final String KEY_PATHINFO_TEMPLATE = "pathInfoTemplate";

    public static final String KEY_PARAMETER_TEMPLATE = "parameterTemplate";

    private static final Pattern PATTERN_PLACEHOLDER = Pattern
            .compile("\\$\\{[^}]*\\}");

    private static final String PATTRENSTR_PLACEHOLDER_REGEX = ".*";

    public static final String ACTION_PRERENDER = "_prerender";

    public static final String ACTION_DEFAULT = "_default";

    public static final String BUTTONNAME_BASE = "[a-zA-Z_][a-zA-Z_0-9]*";

    public static final String BUTTONNAMEPATTERNSTRINGFORDISPATCHING = "_("
            + BUTTONNAME_BASE + ")$";

    public static final String DEFAULT_ACTIONNAMETEMPLATE = "_${method}";

    private TextTemplateEvaluator evaluator_ = new SimpleTextTemplateEvaluator();

    private Pattern pattern_;

    private String template_;

    private String pageComponentNameTemplate_;

    private String actionNameTemplate_;

    private String pathInfoTemplate_;

    private String parameterTemplate_;

    private String defaultReturnValueTemplate_;

    private Pattern buttonNamePatternForDispatching_ = Pattern
            .compile(BUTTONNAMEPATTERNSTRINGFORDISPATCHING);

    private boolean denied_;

    private boolean ignored_;

    private Pattern pageComponentNameTemplatePattern_;

    private Pattern pageComponentNamePattern_;

    private ActionManager actionManager_;

    private final Log log_ = LogFactory.getLog(YmirPathMapping.class);

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, null, null, null);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, null, null, null);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate, null, null);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate, null, null);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate,
            String parameterTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate,
                parameterTemplate, null);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate,
            String parameterTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate,
                parameterTemplate, null);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, parameterTemplate, null);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, parameterTemplate, null);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate,
            String defaultReturnValueTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, parameterTemplate,
                defaultReturnValueTemplate);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate,
            String defaultReturnValueTemplate) {
        denied_ = denied;
        pattern_ = Pattern.compile(patternString);
        setPageComponentNameTemplate(pageComponentNameTemplate);
        actionNameTemplate_ = actionNameTemplate;
        pathInfoTemplate_ = pathInfoTemplate;
        parameterTemplate_ = parameterTemplate;
        defaultReturnValueTemplate_ = defaultReturnValueTemplate;
    }

    public YmirPathMapping(boolean ignored, String patternString) {
        ignored_ = ignored;
        pattern_ = Pattern.compile(patternString);
    }

    void setPageComponentNameTemplate(String pageComponentNameTemplate) {
        pageComponentNameTemplate_ = pageComponentNameTemplate;
        pageComponentNameTemplatePattern_ = pageComponentNameTemplate_ != null ? Pattern
                .compile(getTemplatePattern(pageComponentNameTemplate_))
                : null;
    }

    String getTemplatePattern(String template) {
        Matcher matcher = PATTERN_PLACEHOLDER.matcher(template);
        StringBuilder sb = new StringBuilder();
        int pre = 0;
        while (matcher.find(pre)) {
            sb.append(toRegexPattern(template.substring(pre, matcher.start())))
                    .append(PATTRENSTR_PLACEHOLDER_REGEX);
            pre = matcher.end();
        }
        sb.append(toRegexPattern(template.substring(pre)));
        return sb.toString();
    }

    public YmirPathMapping(Map<String, Object> map) {
        denied_ = PropertyUtils.valueOf(map.get(KEY_DENIED), false);
        map.remove(KEY_DENIED);
        ignored_ = PropertyUtils.valueOf(map.get(KEY_IGNORED), false);
        map.remove(KEY_IGNORED);
        pattern_ = Pattern.compile(PropertyUtils.valueOf(map.get(KEY_PATTERN),
                (String) null));
        map.remove(KEY_PATTERN);
        setPageComponentNameTemplate(PropertyUtils.valueOf(map
                .get(KEY_PAGECOMPONENTNAME_TEMPLATE), (String) null));
        map.remove(KEY_PAGECOMPONENTNAME_TEMPLATE);
        actionNameTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_ACTIONNAME_TEMPLATE), (String) null);
        map.remove(KEY_ACTIONNAME_TEMPLATE);
        pathInfoTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_PATHINFO_TEMPLATE), (String) null);
        map.remove(KEY_PATHINFO_TEMPLATE);
        parameterTemplate_ = PropertyUtils.valueOf(map
                .get(KEY_PARAMETER_TEMPLATE), (String) null);
        map.remove(KEY_PARAMETER_TEMPLATE);

        setReverseMapping(PropertyUtils.valueOf(map
                .get(KEY_PAGECOMPONENTNAME_PATTERN), (String) null),
                PropertyUtils.valueOf(map.get(KEY_TEMPLATE), (String) null));
        map.remove(KEY_PAGECOMPONENTNAME_PATTERN);
        map.remove(KEY_TEMPLATE);

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

    public void setReverseMapping(String pageComponentNamePatternString,
            String template) {
        pageComponentNamePattern_ = pageComponentNamePatternString != null ? Pattern
                .compile(pageComponentNamePatternString)
                : null;
        template_ = template;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
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

    public String getQueryStringTemplate() {
        return parameterTemplate_;
    }

    public Pattern getPattern() {
        return pattern_;
    }

    public VariableResolver match(String path, HttpMethod method) {
        Matcher matcher = pattern_.matcher(path);
        if (matcher.matches()) {
            Map<String, String> map = createVariableResolverAsMap(path, matcher);
            map.put("METHOD", method.name());
            String lmethod = method.name().toLowerCase();
            map.put("method", lmethod);
            map.put("Method", upper(lmethod));

            return new MapVariableResolver(map);
        } else {
            return null;
        }
    }

    Map<String, String> createVariableResolverAsMap(String pattern,
            Matcher matcher) {
        Map<String, String> map = new HashMap<String, String>();
        int count = matcher.groupCount();
        for (int j = 0; j <= count; j++) {
            String matched = matcher.group(j);
            map.put(String.valueOf(j), matched);
            map.put(j + "u", upper(matched));
            map.put(j + "l", lower(matched));
            map.put(j + "d", decapitalize(matched));
        }

        return map;
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

    String decapitalize(String str) {
        return Introspector.decapitalize(str);
    }

    public String getPageComponentName(VariableResolver resolver) {
        return evaluate(pageComponentNameTemplate_, resolver);
    }

    /**
     * パスに対応するアクションの名前を返します。
     * 
     * @param resolver {@link #match(String, String)}が返す{@link VariableResolver}オブジェクト。
     * @return アクションの名前。
     */
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
        return evaluate(defaultReturnValueTemplate_, resolver);
    }

    public String evaluate(String template, VariableResolver resolver) {
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

    public boolean isIgnored() {
        return ignored_;
    }

    protected String extractButtonName(String name) {
        if (buttonNamePatternForDispatching_ != null) {
            Matcher matcher = buttonNamePatternForDispatching_.matcher(name);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
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
        final String defaultActionName = getDefaultActionName();

        // ボタンに対応するアクションを探索する。
        Action action = pageComponent
                .accept(new PageComponentVisitor<Action>() {
                    @Override
                    public Action process(PageComponent pageComponent,
                            Object... parameters) {
                        return getActionForButton(pageComponent.getPage(),
                                pageComponent.getPageClass(), actionName,
                                request);
                    }
                });
        if (action == null) {
            // ボタンに対応するデフォルトアクションを探索する。
            action = pageComponent.accept(new PageComponentVisitor<Action>() {
                @Override
                public Action process(PageComponent pageComponent,
                        Object... parameters) {
                    return getActionForButton(pageComponent.getPage(),
                            pageComponent.getPageClass(), defaultActionName,
                            request);
                }
            });
        }

        if (action == null) {
            // 通常のアクションを探索する。
            action = pageComponent.accept(new PageComponentVisitor<Action>() {
                @Override
                public Action process(PageComponent pageComponent,
                        Object... parameters) {
                    return getAction(pageComponent.getPage(), pageComponent
                            .getPageClass(), actionName, request);
                }
            });
        }
        if (action == null) {
            // デフォルトアクションを探索する。
            action = pageComponent.accept(new PageComponentVisitor<Action>() {
                @Override
                public Action process(PageComponent pageComponent,
                        Object... parameters) {
                    return getAction(pageComponent.getPage(), pageComponent
                            .getPageClass(), defaultActionName, request);
                }
            });
        }
        return action;
    }

    protected Action getActionForButton(Object page, Class<?> pageClass,
            String actionName, Request request) {
        Method[] methods = ClassUtils.getMethods(pageClass);
        if (log_.isDebugEnabled()) {
            log_.debug("getActionForButton: search " + pageClass + " for "
                    + actionName + " method...");
        }
        for (int i = 0; i < methods.length; i++) {
            Action action = createActionForButton(page, pageClass, methods[i],
                    actionName, request);
            if (action != null) {
                if (log_.isDebugEnabled()) {
                    log_.debug("getActionForButton: Found: " + methods[i]);
                }
                return action;
            }
        }

        return null;
    }

    protected Action createActionForButton(Object page, Class<?> pageClass,
            Method method, String actionName, Request request) {
        String name = method.getName();
        if (!name.startsWith(actionName)) {
            return null;
        }

        String buttonName = extractButtonName(name.substring(actionName
                .length()));
        if (buttonName == null) {
            return null;
        }

        return createActionWithParameters(page, pageClass, method, buttonName,
                request);
    }

    Action createActionWithParameters(Object page, Class<?> pageClass,
            Method method, String buttonName, Request request) {
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            String pname = itr.next();
            if (!pname.startsWith(buttonName)) {
                continue;
            }

            Button button = new Button(pname);
            if (!button.isValid() || !buttonName.equals(button.getName())) {
                continue;
            }

            return actionManager_.newAction(page, pageClass, method,
                    (Object[]) button.getParameters());
        }
        return null;
    }

    protected Action getAction(Object page, Class<?> pageClass,
            String actionName, Request request) {
        Method[] methods = ClassUtils.getMethods(pageClass, actionName);
        if (methods.length == 1) {
            if (log_.isDebugEnabled()) {
                log_.debug("getAction: Found: " + methods[0]);
            }
            return actionManager_.newAction(page, pageClass, methods[0]);
        } else if (methods.length == 0) {
            return null;
        } else {
            throw new IllegalClientCodeRuntimeException(
                    "Action method must be single: class=" + pageClass
                            + ", method=" + Arrays.asList(methods));
        }
    }

    /**
     * 指定されたPageコンポーネント名がこのPathMappingによってパスからマップされ得るかを返します。
     * 
     * @param pageComponentName Pageのコンポーネント名。
     * @return 指定されたPageコンポーネント名がこのPathMappingによってパスからマップされ得るか。
     * @since 0.9.6
     */
    boolean mapsToPageComponentName(String pageComponentName) {
        if (pageComponentName == null) {
            return false;
        }

        return pageComponentNameTemplatePattern_.matcher(pageComponentName)
                .matches();
    }

    public Action getPrerenderAction(PageComponent pageComponent,
            Request request, VariableResolver resolver) {
        Method[] methods = ClassUtils.getMethods(pageComponent.getPageClass(),
                ACTION_PRERENDER);
        if (methods.length == 1) {
            return actionManager_.newAction(pageComponent.getPage(),
                    pageComponent.getPageClass(), methods[0]);
        } else if (methods.length == 0) {
            return null;
        } else {
            throw new IllegalClientCodeRuntimeException(
                    "Prerender method must be single: class="
                            + pageComponent.getPageClass() + ", method="
                            + Arrays.asList(methods));
        }
    }

    protected String getDefaultActionName() {
        return ACTION_DEFAULT;
    }

    public static class Button {
        private static final Pattern PATTERN = Pattern.compile("("
                + BUTTONNAME_BASE + ")((\\[[^]]*\\])*)");

        private static final char PARAM_PREFIX_CHAR = '[';

        private static final char PARAM_SUFFIX_CHAR = ']';

        private static final String PARAM_PREFIX = String
                .valueOf(PARAM_PREFIX_CHAR);

        private static final String PARAM_SUFFIX = String
                .valueOf(PARAM_SUFFIX_CHAR);

        private String rawName_;

        private String name_;

        private String[] parameters_;

        private boolean valid_;

        public Button(String rawName) {
            rawName_ = rawName;

            String name;
            if (rawName.endsWith(".x") || rawName.endsWith(".y")) {
                name = rawName
                        .substring(0, rawName.length() - 2/*=".x".length()*/);
            } else {
                name = rawName;
            }

            Matcher matcher = PATTERN.matcher(name);
            if (!matcher.matches()) {
                return;
            }

            name_ = matcher.group(1);
            parameters_ = parseParameters(matcher.group(2));
            valid_ = true;
        }

        String[] parseParameters(String parametersString) {
            List<String> list = new ArrayList<String>();
            if (parametersString.length() > 0) {
                for (StringTokenizer st = new StringTokenizer(
                        parametersString
                                .substring(1/*=PARAM_PREFIX.length()*/,
                                        parametersString.length() - 1/*=PARAM_SUFFIX.length()*/),
                        PARAM_SUFFIX + PARAM_PREFIX); st.hasMoreTokens();) {
                    list.add(st.nextToken());
                }
            }
            return list.toArray(new String[0]);
        }

        public boolean isValid() {
            return valid_;
        }

        public String getRawName() {
            return rawName_;
        }

        public String getName() {
            return name_;
        }

        public String[] getParameters() {
            return parameters_;
        }
    }

    public VariableResolver matchPageComponentName(String pageComponentName) {
        if (pageComponentNamePattern_ == null || template_ == null) {
            return null;
        }

        Matcher matcher = pageComponentNamePattern_.matcher(pageComponentName);
        if (matcher.matches()) {
            return new MapVariableResolver(createVariableResolverAsMap(
                    pageComponentName, matcher));
        } else {
            return null;
        }
    }

    public String getPath(VariableResolver resolver) {
        if (pageComponentNamePattern_ == null || template_ == null) {
            return null;
        }

        return evaluate(template_, resolver);
    }

    public String getActionKeyFromParameterName(String parameterName) {
        if (parameterName == null) {
            return null;
        }

        Button button = new Button(parameterName);
        if (!button.isValid()) {
            return null;
        }

        return button.getName();
    }
}
