package org.seasar.ymir.zpt.mobylet.image;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobylet.core.image.ImageConfig;
import org.mobylet.core.util.RequestUtils;

/**
 * mobylet.image.propertiesのimage.scale.servlet.pathを
 * コンテキスト相対で記述できるようにするためのImageConfig実装です。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class YmirImageConfig extends ImageConfig {
    private static final Log log = LogFactory.getLog(YmirImageConfig.class);

    @Override
    public String getScaleServletPath() {
        String original = super.getScaleServletPath();

        HttpServletRequest request = RequestUtils.get();
        if (request == null) {
            return original;
        }

        if (original == null || !original.startsWith("/")) {
            return original;
        }

        String contextPath = request.getContextPath();
        return getScaleServletPath(original, contextPath);
    }

    String getScaleServletPath(String original, String actualContextPath) {
        String originalContextPath = getContextPath(original);
        if (actualContextPath.equals(originalContextPath)) {
            // 指定されたコンテキストパスと実際のコンテキストパスが一致している。
            return original;
        } else if (originalContextPath.length() == 0) {
            // 指定されたコンテキストパスが指定されていない＋実際のコンテキストパスが""でない。
            return actualContextPath + original;
        } else {
            // 指定されたパスに実際と異なるコンテキストパスが付与されている。
            log.warn("ScaleServletPath's context (" + originalContextPath
                    + ") is different from the actual context ("
                    + actualContextPath + ")");
            return original;
        }
    }

    private String getContextPath(String path) {
        int slash = path.indexOf('/', 1);
        if (slash >= 0) {
            return path.substring(0, slash);
        } else {
            return "";
        }
    }
}
