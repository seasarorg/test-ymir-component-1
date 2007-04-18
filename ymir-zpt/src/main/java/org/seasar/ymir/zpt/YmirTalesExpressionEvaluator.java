package org.seasar.ymir.zpt;

import net.skirnir.freyja.zpt.tales.NoteLocalizer;
import net.skirnir.freyja.zpt.webapp.ServletTalesExpressionEvaluator;

public class YmirTalesExpressionEvaluator extends
        ServletTalesExpressionEvaluator {
    public YmirTalesExpressionEvaluator() {
        NoteLocalizer noteLocalizer = new YmirNoteLocalizer();
        addPathResolver(new YmirPathResolver().setNoteLocalizer(noteLocalizer))
                .addPathResolver(
                        new LocalizationPathResolver()
                                .setNoteLocalizer(noteLocalizer));
    }
}
