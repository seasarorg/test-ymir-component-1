package org.seasar.ymir.servlet;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.impl.FormFileImpl;

/**
 * Commons FileUploadを使ってマルチパートリクエストを扱うようにした
 * HttpServletRequestクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class MultipartServletRequest extends HttpServletRequestWrapper {
    /**
     * {@link FormFile}オブジェクトが格納されたMapをHttpServletRequestの属性として保持するためのキー名です。
     */
    public static final String ATTR_FORMFILEMAP = MultipartServletRequest.class
            .getName()
            + ".formFileMap";

    private HttpServletRequest request_;

    private Map<String, String[]> paramMap_;

    @SuppressWarnings("unchecked")
    public MultipartServletRequest(HttpServletRequest request) {

        super(request);
        request_ = request;

        if (ServletFileUpload.isMultipartContent(new ServletRequestContext(
                request))) {
            String encoding = request.getCharacterEncoding();
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding(encoding);
            try {
                paramMap_ = prepareParameters(upload.parseRequest(request),
                        encoding);
            } catch (FileUploadException ex) {
                paramMap_ = request.getParameterMap();
            }
        } else {
            paramMap_ = request.getParameterMap();
        }
    }

    /*
     * HttpServletRequest
     */

    public String getParameter(String name) {
        String value = (String) getFirst(paramMap_.get(name));
        if (value == null) {
            value = super.getParameter(name);
        }
        return value;
    }

    public Map<String, String[]> getParameterMap() {
        return paramMap_;
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(paramMap_.keySet());
    }

    public String[] getParameterValues(String name) {
        return paramMap_.get(name);
    }

    /*
     * private scope methods
     */

    private Map<String, String[]> prepareParameters(
            List<FileItem> fileItemList, String encoding) {
        @SuppressWarnings("unchecked")
        Map<String, String[]> paramMap = new HashMap<String, String[]>(request_
                .getParameterMap());
        Map<String, FormFile[]> fileMap = new HashMap<String, FormFile[]>();
        for (Iterator<FileItem> itr = fileItemList.iterator(); itr.hasNext();) {
            FileItem fileItem = itr.next();
            String fieldName = fileItem.getFieldName();
            if (fileItem.isFormField()) {
                String value;
                try {
                    value = fileItem.getString(encoding);
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException("Can't happen!");
                }
                paramMap.put(fieldName, add(paramMap.get(fieldName), value,
                        String.class));
            } else {
                FormFile value = new FormFileImpl(fileItem);
                fileMap.put(fieldName, add(fileMap.get(fieldName), value,
                        FormFile.class));
            }
        }
        request_.setAttribute(ATTR_FORMFILEMAP, Collections
                .unmodifiableMap(fileMap));

        return Collections.unmodifiableMap(paramMap);
    }

    @SuppressWarnings("unchecked")
    private <T> T[] add(T[] objs, T value, Class<T> clazz) {
        T[] newObjs;
        if (objs == null) {
            newObjs = (T[]) Array.newInstance(clazz, 1);
            newObjs[0] = value;
        } else {
            newObjs = (T[]) Array.newInstance(objs.getClass()
                    .getComponentType(), objs.length + 1);
            System.arraycopy(objs, 0, newObjs, 0, objs.length);
            newObjs[objs.length] = value;
        }
        return newObjs;
    }

    private Object getFirst(Object obj) {
        Object[] objs = (Object[]) obj;
        if (objs == null || objs.length == 0) {
            return null;
        } else {
            return objs[0];
        }
    }
}
