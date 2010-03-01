package org.seasar.ymir.render;

import java.io.Serializable;

/**
 * ページング処理においてページ情報を保持するためのDTOです。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class PageNumber implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pageNumber_;

    private boolean current_;

    public PageNumber() {
        // Ymirの自動生成のために必要。
    }

    public PageNumber(int pageNumber, boolean current) {
        initialize(pageNumber, current);
    }

    @Override
    public String toString() {
        return String.valueOf(pageNumber_);
    }

    public void initialize(int pageNumber, boolean current) {
        pageNumber_ = pageNumber;
        current_ = current;
    }

    public int getPageNumber() {
        return pageNumber_;
    }

    public boolean isCurrent() {
        return current_;
    }
}
