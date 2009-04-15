package org.seasar.ymir.zpt.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

import net.skirnir.freyja.TemplateContext;

public class YmirUtils {
    public static final String ATTR_TYPECONVERSION_HINT = "ymir.typeConversion.hint";

    private static final String SUBSTRING_PAGE = "Page";

    private YmirUtils() {
    }

    public static void preserveTypeConversionHint(TemplateContext context,
            Object obj, String child) {
        Object hint = null;
        // 効率化のためにPageという文字列が含まれるクラスに限定している。
        // 「末尾に」ではないのは、エンハンスされていると末尾にPageがつかないため。
        if (obj != null
                && obj.getClass().getName().indexOf(SUBSTRING_PAGE) >= 0) {
            TypeConversionManager typeConversionManager = getTypeConversionManager();
            PropertyHandler handler = typeConversionManager.getPropertyHandler(
                    obj, child);
            if (handler != null) {
                List<Annotation> hintList = new ArrayList<Annotation>();
                AnnotationHandler annotationHandler = getAnnotationHandler();
                hintList.addAll(Arrays.asList(annotationHandler
                        .getMarkedAnnotations(handler.getReadMethod(),
                                TypeConversionHint.class)));
                hintList.addAll(Arrays.asList(annotationHandler
                        .getMarkedAnnotations(handler.getWriteMethod(),
                                TypeConversionHint.class)));
                hint = hintList.toArray(new Annotation[0]);
            }
        }
        context.setAttribute(ATTR_TYPECONVERSION_HINT, hint);
    }

    public static AnnotationHandler getAnnotationHandler() {
        return (AnnotationHandler) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(AnnotationHandler.class);
    }

    public static TypeConversionManager getTypeConversionManager() {
        return (TypeConversionManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(TypeConversionManager.class);
    }
}
