package org.seasar.ymir.response.scheme.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import org.apache.commons.beanutils.BeanUtils;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.el.EvaluationException;
import org.seasar.kvasir.util.el.TextTemplateEvaluator;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.kvasir.util.el.impl.SimpleTextTemplateEvaluator;
import org.seasar.ymir.Globals;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.response.TransitionResponse;
import org.seasar.ymir.response.scheme.Strategy;

abstract public class AbstractTransitionStrategy implements Strategy {
    private static final String ENCODING = "UTF-8";

    private static final TextTemplateEvaluator evaluator_ = new SimpleTextTemplateEvaluator();

    public Response constructResponse(String path, Object component) {
        TransitionResponse response = newResponse();
        response.setPath(constructPath(path, component));
        return response;
    }

    abstract public TransitionResponse newResponse();

    protected String constructPath(String path, Object component) {
        if (!isRichPathExpressionAvailable()) {
            return path;
        }

        Path p = null;
        int lparen = path.lastIndexOf('(');
        if (lparen >= 0 && path.endsWith(")")) {
            // /path(a,b)形式。
            p = new Path(path.substring(0, lparen), getCharacterEncoding());
            parseFunctionTypeParameters(path.substring(lparen + 1, path
                    .length() - 1), component, p);
        } else {
            int question = path.indexOf('?');
            if (question >= 0) {
                p = new Path(path.substring(0, question),
                        getCharacterEncoding());
                if (path.indexOf('=', question + 1) >= 0) {
                    // path?a=${a}&b=${b}形式。
                    parseExpressionTypeParameters(path.substring(question + 1),
                            component, p);
                } else {
                    // path?a&b形式。
                    parseSimpleTypeParameters(path.substring(question + 1),
                            component, p);
                }
            }
        }
        if (p != null) {
            return p.asString();
        } else {
            return path;
        }
    }

    protected boolean isRichPathExpressionAvailable() {
        return PropertyUtils
                .valueOf(
                        YmirContext
                                .getYmir()
                                .getApplication()
                                .getProperty(
                                        Globals.APPKEY_CORE_RESPONSE_STRATEGY_RICHPATHEXPRESSIONAVAILABLE),
                        true);
    }

    String getCharacterEncoding() {
        return ((Request) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(Request.class))
                .getCharacterEncoding();
    }

    void parseFunctionTypeParameters(String parameter, Object component,
            Path path) {
        String[] params = parameter.split(",");
        for (int i = 0; i < params.length; i++) {
            String name = decodeURL(params[i].trim());
            path.addParameter(name, getProperty(component, name));
        }
    }

    String getProperty(Object component, String name) {
        if (component != null) {
            try {
                return BeanUtils.getProperty(component, name);
            } catch (IllegalAccessException ex) {
                ;
            } catch (InvocationTargetException ex) {
                ;
            } catch (NoSuchMethodException ex) {
                ;
            }
        }
        return null;
    }

    void parseExpressionTypeParameters(String parameter, Object component,
            Path path) {
        VariableResolver resolver = new ComponentVariableResolver(component);

        String[] params = parameter.split("&");
        for (int i = 0; i < params.length; i++) {
            int equal = params[i].indexOf('=');
            if (equal >= 0) {
                try {
                    path.addParameter(
                            evaluator_.evaluateAsString(decodeURL(params[i]
                                    .substring(0, equal)), resolver),
                            evaluator_.evaluateAsString(decodeURL(params[i]
                                    .substring(equal + 1)), resolver));
                } catch (EvaluationException ex) {
                    throw new IllegalClientCodeRuntimeException(
                            "Expression type parameter's format is wrong: "
                                    + parameter, ex);
                }
            }
        }
    }

    void parseSimpleTypeParameters(String parameter, Object component, Path path) {
        String[] params = parameter.split("&");
        for (int i = 0; i < params.length; i++) {
            String name = decodeURL(params[i]);
            path.addParameter(name, getProperty(component, name));
        }
    }

    String decodeURL(String string) {
        try {
            return URLDecoder.decode(string, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private class ComponentVariableResolver implements VariableResolver {
        private Object component_;

        public ComponentVariableResolver(Object component) {

            component_ = component;
        }

        public Object getValue(Object key) {

            if (key instanceof String) {
                return getProperty(component_, (String) key);
            } else {
                return null;
            }
        }
    }
}
