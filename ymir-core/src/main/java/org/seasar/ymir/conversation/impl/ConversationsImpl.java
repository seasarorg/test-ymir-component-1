package org.seasar.ymir.conversation.impl;

import static org.seasar.ymir.util.LogUtils.INDENT;
import static org.seasar.ymir.util.LogUtils.LS;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.BeginCondition;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.util.LogUtils;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationsImpl implements Conversations {
    private static final long serialVersionUID = 378981595198068349L;

    private transient HotdeployManager hotdeployManager_;

    private transient ApplicationManager applicationManager_;

    private LinkedList<Conversation> conversationStack_ = new LinkedList<Conversation>();

    private Conversation currentConversation_;

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
        sb.append(INDENT).append("current=").append(
                LogUtils.addIndent(currentConversation_, "  ")).append(LS);
        if (!conversationStack_.isEmpty()) {
            sb.append(INDENT).append("stack=").append(
                    LogUtils.addIndent(conversationStack_, "  ")).append(LS);
        }
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

    public synchronized Conversation getCurrentConversation() {
        return currentConversation_;
    }

    public synchronized String getCurrentConversationName() {
        if (currentConversation_ == null) {
            return null;
        } else {
            return currentConversation_.getName();
        }
    }

    public synchronized Conversation getSuperConversation() {
        if (conversationStack_.size() > 0) {
            return conversationStack_.getFirst();
        } else {
            return null;
        }
    }

    public synchronized String getSuperConversationName() {
        Conversation superConversation = getSuperConversation();
        if (superConversation == null) {
            return null;
        } else {
            return superConversation.getName();
        }
    }

    protected Conversation newConversation(String conversationName) {
        ConversationImpl impl = new ConversationImpl(conversationName);
        impl.setHotdeployManager(hotdeployManager_);
        impl.setApplicationManager(applicationManager_);
        return impl;
    }

    public synchronized Object getAttribute(String name) {
        if (currentConversation_ != null) {
            return currentConversation_.getAttribute(name);
        } else {
            return null;
        }
    }

    public synchronized void setAttribute(String name, Object value) {
        if (currentConversation_ != null) {
            currentConversation_.setAttribute(name, value);
        }
    }

    public synchronized void begin(String conversationName, String phase,
            BeginCondition condition) {

        if (equals(getCurrentConversationName(), conversationName)) {
            if (condition == BeginCondition.EXCEPT_FOR_SAME_CONVERSATION) {
                return;
            }
            if (equals(currentConversation_.getPhase(), phase)) {
                if (condition == BeginCondition.EXCEPT_FOR_SAME_CONVERSATION_AND_SAME_PHASE) {
                    return;
                }
            }
        } else if (currentConversation_ != null) {
            // 現在のカンバセーションが指定されたカンバセーションと一致しない場合。
            // 一致するものがスタックに見つかるまでスタックのエントリを削除する。
            // なお現在のカンバセーションがnullの場合はBeginSubConversation直後のBeginなので、
            // スタック操作は行なわない。
            for (Iterator<Conversation> itr = conversationStack_.iterator(); itr
                    .hasNext();) {
                Conversation conversation = itr.next();
                itr.remove();
                if (conversation.getName().equals(conversationName)) {
                    break;
                }
            }

        }

        currentConversation_ = newConversation(conversationName);
        currentConversation_.setPhase(phase);
    }

    boolean equals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    public synchronized Object end() {
        removeCurrentConversation();
        if (conversationStack_.size() > 0) {
            currentConversation_ = conversationStack_.removeFirst();
            return currentConversation_.getReenterResponse();
        } else {
            return null;
        }
    }

    public synchronized boolean isInSubConversation() {
        return (conversationStack_.size() > 0 && conversationStack_.peek()
                .getReenterResponse() != null);
    }

    public synchronized void join(String conversationName, String phase,
            String[] followAfter, boolean acceptBrowsersBackButton) {
        if (currentConversation_ == null) {
            // カンバセーションに参加していない状態でカンバセーション内に遷移しようとしている場合は不正な遷移。
            illegalTransitionDetected();
            return;
        }

        if (!currentConversation_.getName().equals(conversationName)) {
            // 現在のカンバセーションが同一カンバセーションではない場合。
            // ブラウザバックを許容するのであれば、指定されたカンバセーションがconversationStackにあれば
            // 現在のカンバセーションを破棄してそこに戻る。
            // 許容しないのであれば不正な遷移。
            if (acceptBrowsersBackButton) {
                int idx = 0;
                for (Iterator<Conversation> itr = conversationStack_.iterator(); itr
                        .hasNext(); idx++) {
                    Conversation conversation = itr.next();
                    if (conversation.getName().equals(conversationName)) {
                        break;
                    }
                }
                if (idx < conversationStack_.size()) {
                    // 指定されたカンバセーションが見つかった。
                    for (int i = 0; i < idx; i++) {
                        conversationStack_.removeFirst();
                    }
                    end();
                } else {
                    // 見つからなかったので不正遷移とする。
                    illegalTransitionDetected();
                    return;
                }
            } else {
                illegalTransitionDetected();
                return;
            }
        }

        String currentPhase = currentConversation_.getPhase();
        if (equals(currentPhase, phase)) {
            // フェーズが現在のフェーズと同じ場合は何もしない。
            return;
        }

        if (!acceptBrowsersBackButton) {
            // ブラウザバックを許容する場合は、同一カンバセーションの場合にfollowAfterを見ない。
            boolean matched = false;
            for (int i = 0; i < followAfter.length; i++) {
                if (followAfter[i].equals(currentPhase)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                // 現在のカンバセーションが指定されたフェーズでない。
                illegalTransitionDetected();
                return;
            }
        }

        currentConversation_.setPhase(phase);
    }

    /**
     * 不正な遷移が検出された際に呼び出されるメソッドです。
     * <p>Ymir-0.9.5以前と互換性を保つには、このメソッドをオーバライドして
     * <code>clear()</code>メソッドを呼び出すようにしたConversationsImplのサブクラスを作成し、
     * ConversationManagerImpl#newConversations()メソッドをオーバライドして
     * 上記クラスのインスタンスを生成して返すようにしたConversationManagerImplのサブクラスを作成し、
     * 上記クラスをコンポーネント定義した
     * ymir-component+conversationManager.diconをクラスパスに追加して下さい。
     * </p>
     */
    protected void illegalTransitionDetected() {
        throw new IllegalTransitionRuntimeException();
    }

    void removeCurrentConversation() {
        if (currentConversation_ != null) {
            currentConversation_ = null;
        }
    }

    void clear() {
        currentConversation_ = null;
        conversationStack_.clear();
    }

    public synchronized void beginSubConversation(Object reenterResponse) {
        if (currentConversation_ == null) {
            throw new IllegalClientCodeRuntimeException(
                    "Conversation is not begun");
        }

        currentConversation_.setReenterResponse(reenterResponse);
        conversationStack_.addFirst(currentConversation_);
        currentConversation_ = null;
    }

    LinkedList<Conversation> getConversationStack() {
        return conversationStack_;
    }

    public Iterator<String> getAttributeNames() {
        if (currentConversation_ != null) {
            return currentConversation_.getAttributeNames();
        } else {
            return new ArrayList<String>().iterator();
        }
    }
}
