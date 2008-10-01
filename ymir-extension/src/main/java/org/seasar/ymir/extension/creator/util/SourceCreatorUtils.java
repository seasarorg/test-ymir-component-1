package org.seasar.ymir.extension.creator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.DispatchWrapper;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.impl.DispatchImpl;
import org.seasar.ymir.impl.PageComponentImpl;
import org.seasar.ymir.impl.RequestImpl;
import org.seasar.ymir.impl.SingleApplication;

public class SourceCreatorUtils {
    private static final String POM_XML = "pom.xml";

    private SourceCreatorUtils() {
    }

    public static MapProperties readAppPropertiesInOrder(Application application) {
        MapProperties prop = new MapProperties(
                new LinkedHashMap<String, String>());
        String filePath = application.getDefaultPropertiesFilePath();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    prop.load(new FileInputStream(file));
                } catch (IOException ignore) {
                }
            }
        }
        return prop;
    }

    public static void writeHeader(OutputStream out, String header)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        if (header != null) {
            sb.append(header);
        } else {
            sb.append(new Date());
        }
        out.write(sb.append(System.getProperty("line.separator")).toString()
                .getBytes("ISO-8859-1"));
    }

    public static String getOriginalProjectRoot(Application application) {
        return readAppPropertiesInOrder(application).getProperty(
                SingleApplication.KEY_PROJECTROOT);
    }

    public static String findProjectRootDirectory(Application application) {
        String webappRoot = application.getWebappRoot();
        if (webappRoot == null) {
            return null;
        }

        File dir = new File(webappRoot);
        while (dir != null && !new File(dir, POM_XML).exists()) {
            dir = dir.getParentFile();
        }
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }

    public static Request newRequest(final String path,
            final HttpMethod method, Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            parameterMap = new HashMap<String, String[]>();
        }
        final Map<String, FormFile[]> fileParameterMap = new HashMap<String, FormFile[]>();
        Request request = (Request) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(Request.class);
        if (request != null) {
            return new ParameterReplacedRequestWrapper(request, parameterMap,
                    fileParameterMap) {
                @Override
                public HttpMethod getMethod() {
                    return method;
                }

                @Override
                public Dispatch getCurrentDispatch() {
                    return new DispatchWrapper(getCurrentDispatch()) {
                        @Override
                        public String getPath() {
                            return path;
                        }
                    };
                }
            };
        } else {
            RequestImpl created = new RequestImpl(null, method, "UTF-8",
                    parameterMap, fileParameterMap, null, null);
            created.enterDispatch(new DispatchImpl(null, path, null,
                    Dispatcher.REQUEST, null));
            return created;
        }
    }

    public static PageComponent newPageComponent(Class<?> pageClass) {
        try {
            return new PageComponentImpl(pageClass.newInstance(), pageClass);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
}
