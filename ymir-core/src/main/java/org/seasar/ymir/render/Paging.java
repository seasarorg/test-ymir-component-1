package org.seasar.ymir.render;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ognl.SetPropertyAccessor;

/**
 * ページング処理を記述するためのDTOです。
 * <p>DBFluteのPagingResultBeanと連携することもできます。
 * </p>
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class Paging implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String CLASSNAME_PAGINGRESULTBEAN = "org.seasar.dbflute.cbean.PagingResultBean";

    private static final String METHODNAME_GETALLRECORDCOUNT = "getAllRecordCount";

    private static final String METHODNAME_GETALLPAGECOUNT = "getAllPageCount";

    private static final String METHODNAME_GETPAGESIZE = "getPageSize";

    private static final String METHODNAME_GETCURRENTPAGENUMBER = "getCurrentPageNumber";

    private static final String METHODNAME_GETPAGERANGESIZE = "getPageRangeSize";

    private int allRecordCount_;

    private int allPageCount_;

    private int pageSize_;

    private int currentPageNumber_;

    private boolean existPrePage_;

    private boolean existNextPage_;

    private int pageRangeSize_;

    private PageNumber[] pageNumbers_;

    public Paging() {
        // Ymirの自動生成のために必要。
    }

    public Paging(int allRecordCount, int allPageCount, int pageSize,
            int currentPageNumber, int pageRangeSize) {
        initialize(allRecordCount, allPageCount, pageSize, currentPageNumber,
                pageRangeSize);
    }

    public void initialize(int allRecordCount, int allPageCount, int pageSize,
            int currentPageNumber, int pageRangeSize) {
        allRecordCount_ = allRecordCount;
        allPageCount_ = allPageCount;
        pageSize_ = pageSize;
        currentPageNumber_ = currentPageNumber;
        existPrePage_ = currentPageNumber > 1;
        existNextPage_ = currentPageNumber < allPageCount;
        pageRangeSize_ = pageRangeSize;
        pageNumbers_ = updatePageNumbers();
    }

    public Paging(Object pagingResultBean) {
        Class<?> beanClass = pagingResultBean.getClass();
        if (!beanClass.getName().equals(CLASSNAME_PAGINGRESULTBEAN)) {
            throw new IllegalArgumentException("Must be an instnaceof "
                    + CLASSNAME_PAGINGRESULTBEAN + " class, but: "
                    + beanClass.getName());
        }

        try {
            initialize(((Integer) beanClass.getMethod(
                    METHODNAME_GETALLRECORDCOUNT).invoke(pagingResultBean))
                    .intValue(), ((Integer) beanClass.getMethod(
                    METHODNAME_GETALLPAGECOUNT).invoke(pagingResultBean))
                    .intValue(), ((Integer) beanClass.getMethod(
                    METHODNAME_GETPAGESIZE).invoke(pagingResultBean))
                    .intValue(), ((Integer) beanClass.getMethod(
                    METHODNAME_GETCURRENTPAGENUMBER).invoke(pagingResultBean))
                    .intValue(), ((Integer) beanClass.getMethod(
                    METHODNAME_GETPAGERANGESIZE).invoke(pagingResultBean))
                    .intValue());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private PageNumber[] updatePageNumbers() {
        List<PageNumber> pageNumbers = new ArrayList<PageNumber>();
        int startPageNumber = currentPageNumber_ - pageRangeSize_;
        if (startPageNumber < 1) {
            startPageNumber = 1;
        }
        int endPageNumber = currentPageNumber_ + pageRangeSize_;
        if (endPageNumber > allPageCount_) {
            endPageNumber = allPageCount_;
        }
        for (int pageNumber = startPageNumber; pageNumber <= endPageNumber; pageNumber++) {
            pageNumbers.add(new PageNumber(pageNumber,
                    pageNumber == currentPageNumber_));
        }
        return pageNumbers.toArray(new PageNumber[0]);
    }

    /**
     * レコードの総数を返します。
     * 
     * @return レコードの総数。
     */
    public int getAllRecordCount() {
        return allRecordCount_;
    }

    /**
     * ページの総数を返します。
     * 
     * @return ページの総数。
     */
    public int getAllPageCount() {
        return allPageCount_;
    }

    /**
     * 1ページあたりのレコード数を返します。
     * 
     * @return 1ページあたりのレコード数。
     */
    public int getPageSize() {
        return pageSize_;
    }

    /**
     * 現在のページ番号を返します。
     * 
     * @return 現在のページ番号。
     */
    public int getCurrentPageNumber() {
        return currentPageNumber_;
    }

    /**
     * 1つ前のページ番号を返します。
     * 
     * @return 1つ前のページ番号。
     */
    public int getPrePageNumber() {
        return currentPageNumber_ - 1;
    }

    /**
     * 1つ後のページ番号を返します。
     * 
     * @return 1つ後のページ番号。
     */
    public int getNextPageNumber() {
        return currentPageNumber_ + 1;
    }

    /**
     * 1つ前のページが存在するかどうかを返します。
     * 
     * @return 1つ前のページが存在するかどうか。
     */
    public boolean isExistPrePage() {
        return existPrePage_;
    }

    /**
     * 1つ後のページが存在するかどうかを返します。
     * 
     * @return 1つ後のページが存在するかどうか。
     */
    public boolean isExistNextPage() {
        return existNextPage_;
    }

    /**
     * 表示するページリンクの幅を表示するかを返します。
     * <p>例えばこのメソッドの返り値が5の場合、
     * {@link #getPageNumbers()}は現在のページの前後5ページ分の
     * {@link PageNumber}オブジェクトを返します。
     * </p>
     * 
     * @return 前後何ページ分のリンクを表示するか。
     */
    public int getPageRangeSize() {
        return pageRangeSize_;
    }

    /**
     * ページ番号のリンクを表示するためのPageNumberオブジェクトの配列を返します。
     * <p>例えば{@link #getPageRangeSize()}の返り値が5の場合、
     * このメソッドは現在のページの前後5ページ分の
     * {@link PageNumber}オブジェクトを返します。
     * </p>
     * 
     * @return PageNumberオブジェクトの配列。
     */
    public PageNumber[] getPageNumbers() {
        return pageNumbers_;
    }
}
