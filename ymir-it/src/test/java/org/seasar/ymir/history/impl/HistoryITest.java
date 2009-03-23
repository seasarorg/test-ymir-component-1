package org.seasar.ymir.history.impl;

import org.seasar.ymir.history.History;
import org.seasar.ymir.history.HistoryManager;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.HistoryITest1Page;
import com.example.web.HistoryITest2Page;

public class HistoryITest extends YmirTestCase {
    public void test_historyが正しく記録されること() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        });
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .peekElement().getPath().getTrunk());
        assertEquals(HistoryITest1Page.class, historyManager.getHistory()
                .peekElement().getPageClass());

        process(HistoryITest2Page.class);
        assertEquals("/historyITest2.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
    }

    public void test_PRGの場合にPなリクエストは記録されないこと1() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        }, "redirect");
        process(HistoryITest2Page.class);

        assertEquals("/historyITest2.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
        assertTrue(historyManager.getHistory().isEmpty());
    }

    public void test_PRGの場合にPなリクエストは記録されないこと2() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        }, "proceed");

        assertEquals("/historyITest2.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
        assertTrue(historyManager.getHistory().isEmpty());
    }

    public void test_forwardなリクエストは記録されること() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        }, "forward");

        assertEquals("/historyITest2.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .peekElement().getPath().getTrunk());
    }

    public void test_startRecordingしていない場合はhistoryは記録されないこと() throws Exception {
        HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class);
        assertNull(historyManager.getHistory().popElement());
    }

    public void test_equalsXXXTo() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        });
        process(HistoryITest2Page.class);

        History history = historyManager.getHistory();
        assertTrue(history.equalsPageTo(HistoryITest2Page.class, history
                .peekElement()));
        assertTrue(history.equalsPathTo("/historyITest2.html", history
                .peekElement()));
    }
}
