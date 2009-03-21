package org.seasar.ymir.history;

/**
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface HistoryManager {
    void setRecordCount(int recordCount);

    void startRecording();

    boolean isRecording();

    History getHistory();

    void stopRecording();
}
