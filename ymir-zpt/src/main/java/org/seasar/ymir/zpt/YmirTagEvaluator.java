package org.seasar.ymir.zpt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.ContainerUtils;

import net.skirnir.freyja.zpt.MetalTagEvaluator;

public class YmirTagEvaluator extends MetalTagEvaluator {
    private TagRenderingInterceptor[] tagRenderingInterceptors_;

    private String[] specialTagPatternStrings_;

    private String[] specialAttributePatternStrings_;

    public YmirTagEvaluator() {
        super(new YmirTalTagEvaluator());
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);

        tagRenderingInterceptors_ = gatherTagRenderingInterceptors();

        Set<String> specialTagPatternStringSet = new HashSet<String>();
        Set<String> specialAttributePatternStringSet = new HashSet<String>();
        String[] strings = super.getSpecialTagPatternStrings();
        if (strings != null) {
            specialTagPatternStringSet.addAll(Arrays.asList(strings));
        }
        strings = super.getSpecialAttributePatternStrings();
        if (strings != null) {
            specialAttributePatternStringSet.addAll(Arrays.asList(strings));
        }
        for (int i = 0; i < tagRenderingInterceptors_.length; i++) {
            strings = tagRenderingInterceptors_[i]
                    .getSpecialTagPatternStrings();
            if (strings != null) {
                specialTagPatternStringSet.addAll(Arrays.asList(strings));
            }
            strings = tagRenderingInterceptors_[i]
                    .getSpecialAttributePatternStrings();
            if (strings != null) {
                specialAttributePatternStringSet.addAll(Arrays.asList(strings));
            }
        }

        specialTagPatternStrings_ = specialTagPatternStringSet
                .toArray(new String[0]);
        specialAttributePatternStrings_ = specialAttributePatternStringSet
                .toArray(new String[0]);
    }

    TagRenderingInterceptor[] gatherTagRenderingInterceptors() {
        return (TagRenderingInterceptor[]) ContainerUtils.findAllComponents(
                getS2Container(), TagRenderingInterceptor.class);
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }

    @Override
    public String[] getSpecialTagPatternStrings() {
        return specialTagPatternStrings_;
    }

    @Override
    public String[] getSpecialAttributePatternStrings() {
        return specialAttributePatternStrings_;
    }
}
