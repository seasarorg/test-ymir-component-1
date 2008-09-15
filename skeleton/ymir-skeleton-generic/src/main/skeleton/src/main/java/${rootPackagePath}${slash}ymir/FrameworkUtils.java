package ${rootPackageName}.ymir;

import org.seasar.ymir.Messages;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;

public class FrameworkUtils {
    private FrameworkUtils() {
    }

    public static Messages getMessages() {
        return getComponent(Messages.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Object key) {
        return (T) getYmir().getApplication().getS2Container().getComponent(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz) {
        return (T) getYmir().getApplication().getS2Container().getComponent(clazz);
    }

    public static Ymir getYmir() {
        return YmirContext.getYmir();
    }
}
