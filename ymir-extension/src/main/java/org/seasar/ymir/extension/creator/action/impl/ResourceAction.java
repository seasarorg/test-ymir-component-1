package org.seasar.ymir.extension.creator.action.impl;

import static org.seasar.ymir.extension.creator.SourceCreator.PATH_PREFIX;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.action.UpdateAction;
import org.seasar.ymir.impl.AsIsInputStreamFactory;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.util.ServletUtils;

public class ResourceAction implements UpdateAction {

    private static final String PATH_PREFIX_RESOURCE = PATH_PREFIX
            + "resource/";

    private static final String PACKAGE_PREFIX_RESOURCE = "org/seasar/ymir/extension/resource/";

    private static final String DEFAULT_CONTENTTYPE = "application/octet-stream";

    private static final String PATH_SOURCECREATOR_JS = "js/sourceCreator.js";

    private static final String MARK_CONTEXTPATH = "@CONTEXT_PATH@";

    private SourceCreator sourceCreator_;

    public ResourceAction(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        String resourcePath = getResourcePath(pathMetaData);
        if (resourcePath == null) {
            return VoidResponse.INSTANCE;
        }

        InputStream in = getClass().getClassLoader().getResourceAsStream(
                PACKAGE_PREFIX_RESOURCE + resourcePath);
        if (in == null) {
            return VoidResponse.INSTANCE;
        } else {
            if (resourcePath.equals(PATH_SOURCECREATOR_JS)) {
                try {
                    in = new ByteArrayInputStream(IOUtils.readString(in,
                            "UTF-8", false).replace(MARK_CONTEXTPATH,
                            request.getContextPath()).getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException("Can't happen!", ex);
                }
            }
            String contentType = sourceCreator_.getServletContext()
                    .getMimeType(resourcePath);
            if (contentType == null) {
                contentType = DEFAULT_CONTENTTYPE;
            }
            return new SelfContainedResponse(new AsIsInputStreamFactory(in),
                    contentType);
        }
    }

    String getResourcePath(PathMetaData pathMetaData) {
        String path = ServletUtils.normalizePath(pathMetaData.getPath());
        if (path.startsWith(PATH_PREFIX_RESOURCE)) {
            return path.substring(PATH_PREFIX_RESOURCE.length());
        }
        return null;
    }
}
