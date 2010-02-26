package org.seasar.ymir.render;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    private static final String METHODNAME_GETCURRENTPAGENUMBER = "getCurrentPageNumber";

    private static final String METHODNAME_GETPAGERANGESIZE = "getPageRangeSize";

    private int allRecordCount;

    private int allPageCount;

    private int currentPageNumber;

    private boolean existPrePage;

    private boolean existNextPage;

    private int pageRangeSize;

    private PageNumber[] pageNumbers;

    public Paging() {
        // Ymirの自動生成のために必要。
    }

    public Paging(int allRecordCount, int allPageCount, int currentPageNumber,
            int pageRangeSize) {
        initialize(allRecordCount, allPageCount, currentPageNumber,
                pageRangeSize);
    }

    public void initialize(int allRecordCount, int allPageCount,
            int currentPageNumber, int pageRangeSize) {
        this.allRecordCount = allRecordCount;
        this.allPageCount = allPageCount;
        this.currentPageNumber = currentPageNumber;
        existPrePage = currentPageNumber > 1;
        existNextPage = currentPageNumber < allPageCount;
        this.pageRangeSize = pageRangeSize;
        pageNumbers = updatePageNumbers();
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
        int startPageNumber = currentPageNumber - pageRangeSize;
        if (startPageNumber < 1) {
            startPageNumber = 1;
        }
        int endPageNumber = currentPageNumber + pageRangeSize;
        if (endPageNumber > allPageCount) {
            endPageNumber = allPageCount;
        }
        for (int pageNumber = startPageNumber; pageNumber <= endPageNumber; pageNumber++) {
            pageNumbers.add(new PageNumber(pageNumber,
                    pageNumber == currentPageNumber));
        }
        return pageNumbers.toArray(new PageNumber[0]);
    }

    public int getAllRecordCount() {
        return allRecordCount;
    }

    public int getAllPageCount() {
        return allPageCount;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getPrePageNumber() {
        return currentPageNumber - 1;
    }

    public int getNextPageNumber() {
        return currentPageNumber + 1;
    }

    public boolean isExistPrePage() {
        return existPrePage;
    }

    public boolean isExistNextPage() {
        return existNextPage;
    }

    public int getPageRangeSize() {
        return pageRangeSize;
    }

    public PageNumber[] getPageNumbers() {
        return pageNumbers;
    }
}
