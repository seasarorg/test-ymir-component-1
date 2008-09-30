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
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.util.ClassUtils;

public class YmirPathMapping implements PathMapping {
    public static final String KEY_DENIED = "denied";

    public static final String KEY_PATTERN = "pattern";

    public static final String KEY_PAGECOMPONENTNAME_TEMPLATE = "pageComponentNameTemplate";

    public static final String KEY_ACTIONNAME_TEMPLATE = "actionNameTemplate";

    public static final String KEY_PATHINFO_TEMPLATE = "pathInfoTemplate";

    public static final String KEY_PARAMETER_TEMPLATE = "parameterTemplate";

    private static final Pattern PATTERN_PLACEHOLDER = Pattern
            .compile("\\$\\{[^}]*\\}");

    private static final String PATTRENSTR_PLACEHOLDER_REGEX = ".*";

    public static final String ACTION_PRERENDER = "_prerender";

    public static final String ACTION_DEFAULT = "_default";

    public static final String BUTTONNAMEPATTERNSTRINGFORDISPATCHING = "_([a-zA-Z][a-zA-Z0-9]*)";

    public static final String DEFAULT_ACTIONNAMETEMPLATE = "_${method}";

    private TextTemplateEvaluator evaluator_ = new SimpleTextTemplateEvaluator();

    private Pattern pattern_;

    private String pageComponentNameTemplate_;

    private String actionNameTemplate_;

    private String pathInfoTemplate_;

    private String parameterTemplate_;

    private Pattern buttonNamePatternForDispatching_ = Pattern
            .compile(BUTTONNAMEPATTERNSTRINGFORDISPATCHING);

    private boolean denied_;

    private Pattern pageComponentNameTemplatePattern_;

    private ActionManager actionManager_;

    private final Logger logger_ = Logger.getLogger(getClass());

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, null, null);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, null, null);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate,
            String parameterTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate, parameterTemplate);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String pathInfoTemplate,
            String parameterTemplate) {
        this(denied, patternString, pageComponentNameTemplate,
                DEFAULT_ACTIONNAMETEMPLATE, pathInfoTemplate, parameterTemplate);
    }

    public YmirPathMapping(String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate) {
        this(false, patternString, pageComponentNameTemplate,
                actionNameTemplate, pathInfoTemplate, parameterTemplate);
    }

    public YmirPathMapping(boolean denied, String patternString,
            String pageComponentNameTemplate, String actionNameTemplate,
            String pathInfoTemplate, String parameterTemplate) {
        denied_ = denied;
        pattern_ = Pattern.compile(patternString);
        setPageComponentNameTemplate(pageComponentNameTemplate);
        actionNameTemplate_ = actionNameTemplate;
        pathInfoTemplate_ = pathInfoTemplate;
        parameterTemplate_ = parameterTemplate;
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

    public VariableResolver match(String path, String method) {
        Matcher matcher = pattern_.matcher(path);
        if (matcher.matches()) {
            Map<String, String> prop = new HashMap<String, String>();
            int count = matcher.groupCount();
            for (int j = 0; j <= count; j++) {
                String matched = matcher.group(j);
                prop.put(String.valueOf(j), matched);
                prop.put(j + "u", upper(matched));
                prop.put(j + "l", lower(matched));
                prop.put(j + "d", decapitalize(matched));
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
        // シンプルにするため、YmirPathMappingではデフォルトの返り値は扱わない。
        return null;
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
                    public Action process(PageComponent pageComponent) {
                        return getActionForButton(pageComponent.getPage(),
                                pageComponent.getPageClass(), actionName,
                                request);
                    }
                });
        if (action == null) {
            // ボタンに対応するデフォルトアクションを探索する。
            action = pageComponent.accept(new PageComponentVisitor<Action>() {
                @Override
                public Action process(PageComponent pageComponent) {
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
                public Action process(PageComponent pageComponent) {
                    return getAction(pageComponent.getPage(), pageComponent
                            .getPageClass(), actionName, request);
                }
            });
        }
        if (action == null) {
            // デフォルトアクションを探索する。
            action = pageComponent.accept(new PageComponentVisitor<Action>() {
                @Override
                public Action process(PageComponent pageComponent) {
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
        if (logger_.isDebugEnabled()) {
            logger_.debug("getActionByParameter: search " + pageClass + " for "
                    + actionName + " method...");
        }
        for (int i = 0; i < methods.length; i++) {
            Action action = createActionForButton(page, pageClass, methods[i],
                    actionName, request);
            if (action != null) {
                if (logger_.isDebugEnabled()) {
                    logger_.debug("getActionByParameter: Found: " + methods[i]);
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

            return actionManager_.newAction(page, pageClass, method, button
                    .getParameters());
        }
        return null;
    }

    protected Action getAction(Object page, Class<?> pageClass,
            String actionName, Request request) {
        Method[] methods = ClassUtils.getMethods(pageClass, actionName);
        if (methods.length == 1) {
            if (logger_.isDebugEnabled()) {
                logger_.debug("getAction: Found: " + methods[0]);
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

    public Action getRenderAction(PageComponent pageComponent, Request request,
            VariableResolver resolver) {
        Method[] methods = ClassUtils.getMethods(pageComponent.getPageClass(),
                ACTION_PRERENDER);
        if (methods.length == 1) {
            return actionManager_.newAction(pageComponent.getPage(),
                    pageComponent.getPageClass(), methods[0]);
        } else if (methods.length == 0) {
            return null;
        } else {
            throw new IllegalClientCodeRuntimeException(
                    "Pre-render method must be single: class="
                            + pageComponent.getPageClass() + ", method="
                            + Arrays.asList(methods));
        }
    }

    protected String getDefaultActionName() {
        return ACTION_DEFAULT;
    }

    public static class Button {
        private static final Pattern PATTERN = Pattern
                .compile("([a-zA-Z_][a-zA-Z_0-9]*)((\\[[^]]*\\])*)");

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
}
