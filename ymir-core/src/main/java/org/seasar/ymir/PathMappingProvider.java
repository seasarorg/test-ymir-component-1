package org.seasar.ymir;

/**
 * PathMappingオブジェクトを束ねるためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface PathMappingProvider {
    /**
     * このオブジェクトが持つ全てのPathMappingを返します。
     * 
     * @return 全てのPathMapping。PathMappingが存在しない場合は空の配列を返します。
     */
    PathMapping[] getPathMappings();
}
