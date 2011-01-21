package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class CollectionFitter extends AbstractFitter<Collection> {
    public Class<Collection> getTargetClass() {
        return Collection.class;
    }

    public void fitContent(Collection value) {
        try {
            // 複数スレッドから同じオブジェクトを参照された場合に処理が衝突しないようにこうしている。
            synchronized (value) {
                List list = new ArrayList();
                for (Iterator itr = value.iterator(); itr.hasNext();) {
                    list.add(getHotdeployManager().fit(itr.next()));
                }
                value.clear();
                for (Iterator itr = list.iterator(); itr.hasNext();) {
                    value.add(itr.next());
                }
            }
        } catch (UnsupportedOperationException ignore) {
            // 変更不能なObjectである可能性がある。
            // その場合は何もしない。
        }
    }
}
