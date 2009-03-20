package org.seasar.ymir.history.impl;

import org.seasar.ymir.HttpMethod;
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

    public void test_GETだけが記録されること() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        });
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .peekElement().getPath().getTrunk());

        process(HistoryITest2Page.class, HttpMethod.POST);
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
    }

    public void test_forwardは記録されないこと() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        }, "forward");
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
    }

    public void test_proceedは記録されること() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        }, "proceed");
        assertEquals("/historyITest2.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
        assertEquals("/historyITest1.html", historyManager.getHistory()
                .popElement().getPath().getTrunk());
    }

    public void test_startRecordingしていない場合はhistoryは記録されないこと() throws Exception {
        HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class);
        assertNull(historyManager.getHistory().popElement());
    }

    public void test_equalsLatestXXXTo() throws Exception {
        final HistoryManager historyManager = getComponent(HistoryManager.class);

        process(HistoryITest1Page.class, new RequestInitializer() {
            public void initialize() {
                historyManager.startRecording();
            }
        });
        process(HistoryITest2Page.class);

        assertTrue(historyManager.getHistory().equalsLatestPageTo(
                HistoryITest2Page.class));
        assertTrue(historyManager.getHistory().equalsLatestPathTo(
                "/historyITest2.html"));
    }
}
