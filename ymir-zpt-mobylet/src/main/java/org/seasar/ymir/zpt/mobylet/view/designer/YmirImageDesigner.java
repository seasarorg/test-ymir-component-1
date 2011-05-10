package org.seasar.ymir.zpt.mobylet.view.designer;

import org.mobylet.core.image.ImageCodec;
import org.mobylet.core.image.ImageConfig;
import org.mobylet.core.image.ImageSourceType;
import org.mobylet.core.image.ScaleType;
import org.mobylet.core.util.PathUtils;
import org.mobylet.core.util.SingletonUtils;
import org.mobylet.view.designer.ImageDesigner;

/**
 * サーブレットが有効の場合でもローカル画像のリサイズをサーブレットを使わずに行なうようにするためのImageDesigner実装です。
 * 
 * @author skirnir
 * @since 1.0.7
 */
public class YmirImageDesigner extends ImageDesigner {
    @Override
    public String getSrc(String src, double magniWidth, ScaleType scaleType,
            ImageCodec codec, boolean useFilter) {
        ScaleType pScaleType = scaleType == null ? ScaleType.FITWIDTH
                : scaleType;
        Double pMagniWidth = new Double(magniWidth);
        ImageConfig config = getConfig();
        if (!useFilter
                && config.useScaleServlet()
                && (PathUtils.isNetworkPath(src) || config.getImageSourceType() == ImageSourceType.NETWORK)) {
            return useServlet(src, pMagniWidth, pScaleType);
        } else {
            // useFilterがtrueであるか、
            // scaleServletを使わない設定であるか、
            // ローカル画像を処理する機能が有効でかつローカルパスが指定されている場合はMobyletフィルタで処理をするようにする。
            // Mobyletフィルタではローカル画像を処理する機能が有効かどうかのチェックをしない（scaleServletではチェックされる）ため、
            // ローカル画像を処理する機能が無効である場合はMobyletフィルタには処理させないようにしている。
            return useFilter(src, pMagniWidth, pScaleType, codec);
        }
    }

    protected ImageConfig getConfig() {
        // S2連携時にうまくconfigが取得できないパターンがあるためこうしている。
        if (config == null) {
            config = SingletonUtils.get(ImageConfig.class);
        }
        return config;
    }
}
