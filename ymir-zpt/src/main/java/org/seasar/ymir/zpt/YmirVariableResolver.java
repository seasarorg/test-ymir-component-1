package org.seasar.ymir.zpt;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.impl.RequestProcessorImpl;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.token.Token;
import org.seasar.ymir.token.TokenManager;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.impl.VariableResolverImpl;

public class YmirVariableResolver extends VariableResolverImpl {
    public static final String NAME_YMIRREQUEST = "ymirRequest";

    public static final String NAME_CONTAINER = "container";

    public static final String NAME_MESSAGES = "messages";

    public static final String NAME_TOKEN = "token";

    private static final String NAME_VARIABLES = "variables";

    private static final String NAME_PARAM_SELF = "param-self";

    private Request ymirRequest_;

    private HttpServletRequest request_;

    private S2Container container_;

    private VariableResolver parent_;

    private Messages messages_;

    private TokenManager tokenManager_;

    private Token token_;

    private boolean selfLoaded_;

    private Object self_;

    private ParamSelf paramSelf_;

    private static final Log log_ = LogFactory
            .getLog(YmirVariableResolver.class);

    public YmirVariableResolver(Request ymirRequest,
            HttpServletRequest request, S2Container container) {
        this(ymirRequest, request, container, null);
    }

    public YmirVariableResolver(Request ymirRequest,
            HttpServletRequest request, S2Container container,
            VariableResolver parent) {
        ymirRequest_ = ymirRequest;
        request_ = request;
        container_ = container;
        messages_ = (Messages) container.getComponent(Globals.NAME_MESSAGES);
        tokenManager_ = (TokenManager) container
                .getComponent(TokenManager.class);
        parent_ = parent;
    }

    public Object getVariable(TemplateContext context, String name) {
        if (NAME_YMIRREQUEST.equals(name)) {
            return ymirRequest_;
        } else if (NAME_CONTAINER.equals(name)) {
            return container_;
        } else if (NAME_MESSAGES.equals(name)) {
            return messages_;
        } else if (RequestProcessor.ATTR_NOTES.equals(name)) {
            // notesがリクエストにない場合はそこでトラバースを打ち切った方が都合が良いため
            // こうしている。リクエストパラメータ等をトラバースされると意図しない動作をする
            // ことがある。例えば /hoe.html?notes=aaa とされると
            // <ul tal:condition="notes"> の判定がtrueになってしまため、
            // 空のulタグがレンダリングされてしまう。
            return getNotes();
        } else if (NAME_TOKEN.equals(name)) {
            return getToken();
        } else if (NAME_VARIABLES.equals(name)) {
            return Variables.INSTANCE;
        } else if (NAME_PARAM_SELF.equals(name)) {
            return getParamSelf();
        } else if (super.containsVariable(name)) {
            return super.getVariable(context, name);
        } else if (parent_ != null) {
            Entry entry = parent_.getVariableEntry(context, name);
            if (entry != null) {
                return entry.getValue();
            }
        }

        // リクエストパラメータを優先させていているのは、バリデーションエラーの後に
        // フォームの値を復元しやすいように。こうなっていると、例えば
        // <input tal:attributes="value hoehoe" name="hoehoe" type="text />
        // のように、TAL式としてパラメータ名だけを書けばPageのgetterで取得できる加工された
        // 値ではなく名前の値がレンダリングに用いられることになり都合が良い。
        Object value = request_.getParameter(name);
        if (value != null) {
            return value;
        }

        Object self = getSelf();
        if (self != null) {
            YmirUtils.preserveTypeConversionHint(context, self, name);
            try {
                return PropertyUtils.getProperty(self, name);
            } catch (Throwable ex) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't get Property: self=" + self + ", name="
                            + name, ex);
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public String[] getVariableNames() {
        Set<String> nameSet = new HashSet<String>();
        nameSet.add(NAME_YMIRREQUEST);
        nameSet.add(NAME_CONTAINER);
        nameSet.add(NAME_MESSAGES);
        nameSet.add(RequestProcessor.ATTR_NOTES);
        nameSet.add(NAME_TOKEN);
        nameSet.add(NAME_VARIABLES);
        nameSet.add(NAME_PARAM_SELF);
        nameSet.addAll(Arrays.asList(super.getVariableNames()));
        if (parent_ != null) {
            nameSet.addAll(Arrays.asList(parent_.getVariableNames()));
        }
        for (Enumeration<String> enm = request_.getParameterNames(); enm
                .hasMoreElements();) {
            nameSet.add(enm.nextElement());
        }
        Object self = getSelf();
        if (self != null) {
            PropertyDescriptor[] pds = PropertyUtils
                    .getPropertyDescriptors(self);
            for (int i = 0; i < pds.length; i++) {
                String name = pds[i].getName();
                if (pds[i].getReadMethod() != null) {
                    nameSet.add(name);
                }
            }
        }
        return nameSet.toArray(new String[0]);
    }

    public boolean containsVariable(String name) {
        if (NAME_YMIRREQUEST.equals(name) || NAME_CONTAINER.equals(name)
                || NAME_MESSAGES.equals(name) || NAME_TOKEN.equals(name)
                || NAME_VARIABLES.equals(name) || NAME_PARAM_SELF.equals(name)) {
            return true;
        } else if (RequestProcessor.ATTR_NOTES.equals(name)) {
            return getNotes() != null;
        } else if (super.containsVariable(name)) {
            return true;
        } else if (parent_ != null && parent_.containsVariable(name)) {
            return true;
        } else if (request_.getParameter(name) != null) {
            return true;
        }

        Object self = getSelf();
        if (self != null) {
            try {
                if (PropertyUtils.getPropertyDescriptor(self, name) != null) {
                    return true;
                }
            } catch (Throwable ex) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't get Property: self=" + self + ", name="
                            + name, ex);
                }
            }
        }

        return false;
    }

    public Entry getVariableEntry(TemplateContext context, String name) {
        if (NAME_YMIRREQUEST.equals(name)) {
            return new EntryImpl(name, Request.class, ymirRequest_);
        } else if (NAME_CONTAINER.equals(name)) {
            return new EntryImpl(name, S2Container.class, container_);
        } else if (NAME_MESSAGES.equals(name)) {
            return new EntryImpl(name, Messages.class, messages_);
        } else if (RequestProcessor.ATTR_NOTES.equals(name)) {
            Notes notes = getNotes();
            if (notes != null) {
                return new EntryImpl(name, Notes.class, notes);
            } else {
                return null;
            }
        } else if (NAME_TOKEN.equals(name)) {
            return new TokenEntry(name);
        } else if (NAME_VARIABLES.equals(name)) {
            return new EntryImpl(name, Variables.class, Variables.INSTANCE);
        } else if (NAME_PARAM_SELF.equals(name)) {
            return new EntryImpl(name, ParamSelf.class, getParamSelf());
        }

        Entry entry = super.getVariableEntry(context, name);
        if (entry != null) {
            return entry;
        } else if (parent_ != null) {
            entry = parent_.getVariableEntry(context, name);
            if (entry != null) {
                return entry;
            }
        }

        Object value = request_.getParameter(name);
        if (value != null) {
            return new EntryImpl(name, String.class, value);
        }

        Object self = getSelf();
        if (self != null) {
            YmirUtils.preserveTypeConversionHint(context, self, name);
            try {
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(
                        self, name);
                if (pd != null) {
                    Method readMethod = pd.getReadMethod();
                    if (readMethod != null) {
                        return new EntryImpl(name, readMethod.getReturnType(),
                                PropertyUtils.getProperty(self, name));
                    }
                }
            } catch (Throwable ex) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't get Property: self=" + self + ", name="
                            + name, ex);
                }
            }
        }

        return null;
    }

    Notes getNotes() {
        return (Notes) request_.getAttribute(RequestProcessor.ATTR_NOTES);
    }

    Token getToken() {
        if (token_ == null) {
            token_ = tokenManager_.newToken();
        }
        return token_;
    }

    class TokenEntry implements Entry {
        private String name_;

        public TokenEntry(String name) {
            name_ = name;
        }

        public String getName() {
            return name_;
        }

        @SuppressWarnings("unchecked")
        public Class getType() {
            return Token.class;
        }

        public Object getValue() {
            return getToken();
        }
    }

    public Object getSelf() {
        if (!selfLoaded_) {
            self_ = request_.getAttribute(RequestProcessorImpl.ATTR_SELF);
            selfLoaded_ = true;
        }
        return self_;
    }

    public ParamSelf getParamSelf() {
        if (paramSelf_ == null) {
            paramSelf_ = new ParamSelf(request_, getSelf());
        }
        return paramSelf_;
    }
}
