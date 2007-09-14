package org.seasar.ymir;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public interface Ymir {
    void init();

    Request prepareForProcessing(String contextPath, String method,
            String characterEncoding, Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale);

    void enterDispatch(Request request, String path, Dispatcher dispatcher);

    Response processRequest(Request request) throws PageNotFoundException,
            PermissionDeniedException;

    HttpServletResponseFilter processResponse(ServletContext context_,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException;

    void leaveDispatch(Request request);

    void destroy();

    Object backupForInclusion(AttributeContainer attributeContainer);

    void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped);

    Application getApplication();

    String getProjectStatus();

    /**
     * 現在処理中のアプリケーションが開発中であるかどうかを返します。
     * <p>このメソッドは、Ymir自体のステータスが開発中でかつ
     * 現在処理中のアプリケーションが開発中のステータスである場合にのみtrueを返します。
     * </p>
     *
     * @return 現在処理中のアプリケーションが開発中であるかどうか。
     */
    boolean isUnderDevelopment();

    Response processException(Request request, Throwable t);

    YmirProcessInterceptor[] getYmirProcessInterceptors();

    void updateParameterMap(Request request, Map<String, String[]> parameterMap);
}
