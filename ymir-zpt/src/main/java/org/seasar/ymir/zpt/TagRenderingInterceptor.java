package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

/**
 * タグのレンダリング処理をインターセプトするためのレンダリングインターセプタを表すインタフェースです。
 * <p>タグのレンダリング処理を変更したい場合はこのインタフェースの実装クラスを作成してapp.diconに
 * コンポーネント登録して下さい。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface TagRenderingInterceptor {
    /**
     * レンダリング時に特別な処理をしたいタグの名前の正規表現の配列を返します。
     * <p>あるタグについてレンダリング処理をインターセプトしたい場合は、
     * そのタグの名前のパターンまたはそのタグが持つ属性名のパターンをこのメソッドか
     * {@link #getSpecialAttributePatternStrings()}が返すようにしておく必要があります。
     * </p>
     * <p>特別な処理をしたいタグの名前に特徴がない場合はnullを返すようにしても構いません。</p>
     * 
     * @return 正規表現の配列。
     */
    String[] getSpecialTagPatternStrings();

    /**
     * レンダリング時に特別な処理をしたいタグの属性名の正規表現の配列を返します。
     * <p>あるタグについてレンダリング処理をインターセプトしたい場合は、
     * そのタグの名前のパターンまたはそのタグが持つ属性名のパターンを
     * {@link #getSpecialTagPatternStrings()}かこのメソッドが返すようにしておく必要があります。
     * </p>
     * <p>特別な処理をしたいタグの属性名に特徴がない場合はnullを返すようにしても構いません。</p>
     * 
     * @return 正規表現の配列。
     */
    String[] getSpecialAttributePatternStrings();

    /**
     * タグをレンダリングします。
     * <p>このメソッドは、{@link #getSpecialTagPatternStrings()}または
     * {@link #getSpecialAttributePatternStrings()}で指定した条件にマッチしたタグを
     * レンダリングする際に呼ばれます。
     * </p>
     * <p>特に何もしない場合や一部のレンダリングだけを変更したい場合は、
     * レンダリングインターセプタチェインに渡す引数を変更して
     * レンダリングインターセプタチェインのrenderメソッドを呼び出すようにして下さい。
     * </p>
     * 
     * @param context タグを評価しているコンテキストを表すオブジェクト。
     * @param name タグの名前。
     * @param attributes タグの属性の配列。属性の値はHTMLエスケープされている状態で格納されていますので、
     * レンダリング結果に含める際に改めてHTMLエスケープする必要はありません。
     * @param body タグのボディ。nullであることもあります。
     * @param chain レンダリングインターセプタのチェイン。
     * @return タグのレンダリング結果。nullを返してはいけません。
     */
    String render(TemplateContext context, String name, Attribute[] attributes,
            String body, TagRenderingInterceptorChain chain);
}
