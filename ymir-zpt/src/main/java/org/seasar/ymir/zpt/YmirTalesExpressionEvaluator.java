package org.seasar.ymir.zpt;

import net.skirnir.freyja.zpt.tales.NoteLocalizer;
import net.skirnir.freyja.zpt.webapp.ServletTalesExpressionEvaluator;

public class YmirTalesExpressionEvaluator extends
        ServletTalesExpressionEvaluator {
    public static final String TYPE_I18NPAGE = "i18npage";

    public YmirTalesExpressionEvaluator() {
        NoteLocalizer noteLocalizer = new YmirNoteLocalizer();
        addPathResolver(new YmirPathResolver().setNoteLocalizer(noteLocalizer))
                .addPathResolver(
                        new LocalizationPathResolver()
                                .setNoteLocalizer(noteLocalizer));
        addTypePrefix(TYPE_I18NPAGE, new I18NPageTypePrefixHandler());
    }
}
