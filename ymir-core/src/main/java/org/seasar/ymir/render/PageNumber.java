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

    private int pageNumber;

    private boolean current;

    public PageNumber() {
        // Ymirの自動生成のために必要。
    }

    public PageNumber(int pageNumber, boolean current) {
        initialize(pageNumber, current);
    }

    public void initialize(int pageNumber, boolean current) {
        this.pageNumber = pageNumber;
        this.current = current;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isCurrent() {
        return current;
    }
}
