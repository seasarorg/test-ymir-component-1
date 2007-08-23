package org.seasar.ymir.zpt;

import net.skirnir.freyja.Element;
import net.skirnir.freyja.TagElement;

public interface TemplateParsingInterceptor {
    /**
     * パース時に特別な処理をしたいタグの名前の正規表現の配列を返します。
     * <p>あるタグについてパース処理をインターセプトしたい場合は、
     * そのタグの名前のパターンまたはそのタグが持つ属性名のパターンをこのメソッドか
     * {@link #getSpecialAttributePatternStrings()}が返すようにしておく必要があります。
     * </p>
     * <p>特別な処理をしたいタグの名前に特徴がない場合はnullを返すようにしても構いません。</p>
     * 
     * @return 正規表現の配列。
     */
    String[] getSpecialTagPatternStrings();

    /**
     * パース時に特別な処理をしたいタグの属性名の正規表現の配列を返します。
     * <p>あるタグについてパース処理をインターセプトしたい場合は、
     * そのタグの名前のパターンまたはそのタグが持つ属性名のパターンを
     * {@link #getSpecialTagPatternStrings()}かこのメソッドが返すようにしておく必要があります。
     * </p>
     * <p>特別な処理をしたいタグの属性名に特徴がない場合はnullを返すようにしても構いません。</p>
     * 
     * @return 正規表現の配列。
     */
    String[] getSpecialAttributePatternStrings();

    /**
     * タグが生成されたタイミングで呼び出されるメソッドです。
     * <p>このメソッドは、{@link #getSpecialTagPatternStrings()}または
     * {@link #getSpecialAttributePatternStrings()}で指定した条件にマッチしたタグが
     * 生成された際に呼ばれます。
     * </p>
     * <p>このメソッドの返り値は生成された最終的なタグとみなされますので、
     * 返り値を差し替えることで、生成されたタグを変更したり独自のタグ等を追加したりすることができます。
     * </p>
     * <p>特に何もしない場合は、
     * パーシングインターセプタチェインのtagElementCreatedメソッドを呼んでその返り値をそのまま
     * このメソッドの返り値として下さい。
     * </p>
     * 
     * @param tagElement 生成されたタグオブジェクト。
     * @return 最終的なElementオブジェクトの配列。nullを返してはいけません。
     */
    Element[] tagElementCreated(TagElement tagElement,
            TemplateParsingInterceptorChain chain);
}
