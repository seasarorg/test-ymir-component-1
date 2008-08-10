package org.seasar.ymir.conversation.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.hotdeploy.HotdeployManager;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationImpl implements Conversation, Serializable {
    private static final long serialVersionUID = -994133844419542105L;

    private transient HotdeployManager hotdeployManager_;

    private transient ApplicationManager applicationManager_;

    private String name_;

    private Map<String, Object> attributeMap_ = new ConcurrentHashMap<String, Object>();

    private String phase_;

    private Object reenterResponse_;

    public ConversationImpl(String name) {
        name_ = name;
    }

    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Override
    public String toString() {
        return getName();
    }

    private Object readResolve() throws ObjectStreamException {
        // transientなオブジェクトを復元する。
        hotdeployManager_ = (HotdeployManager) YmirContext.getYmir()
                .getApplication().getS2Container().getComponent(
                        HotdeployManager.class);
        applicationManager_ = (ApplicationManager) YmirContext.getYmir()
                .getApplication().getS2Container().getComponent(
                        ApplicationManager.class);
        return this;
    }

    public String getName() {
        return name_;
    }

    public Object getAttribute(String name) {
        Object value = attributeMap_.get(name);
        if (applicationManager_.findContextApplication().isUnderDevelopment()) {
            // Scope経由ではなく直接属性を取得された場合のためにこうしている。
            return hotdeployManager_.fit(value);
        } else {
            return value;
        }
    }

    public void setAttribute(String name, Object value) {
        if (value != null) {
            attributeMap_.put(name, value);
        } else {
            attributeMap_.remove(name);
        }
    }

    public String getPhase() {
        return phase_;
    }

    public synchronized void setPhase(String phase) {
        phase_ = phase;
    }

    public Object getReenterResponse() {
        return reenterResponse_;
    }

    public void setReenterResponse(Object reenterResponse) {
        reenterResponse_ = reenterResponse;
    }

    public Iterator<String> getAttributeNames() {
        return attributeMap_.keySet().iterator();
    }
}
