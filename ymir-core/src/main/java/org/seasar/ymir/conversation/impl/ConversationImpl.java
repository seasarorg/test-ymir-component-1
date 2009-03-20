package org.seasar.ymir.conversation.impl;

import static org.seasar.ymir.util.LogUtils.DELIM;
import static org.seasar.ymir.util.LogUtils.INDENT;
import static org.seasar.ymir.util.LogUtils.LS;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.util.LogUtils;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationImpl implements Conversation {
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
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("{").append(LS);
        sb.append(INDENT).append("name=").append(name_);
        sb.append(DELIM).append("phase=").append(phase_);
        sb.append(DELIM).append("reenterResponse=").append(reenterResponse_)
                .append(LS);
        sb.append(INDENT).append("attributes=").append(
                LogUtils.addIndent(LogUtils.toString(attributeMap_)))
                .append(LS);
        sb.append("}");
        return sb.toString();
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
