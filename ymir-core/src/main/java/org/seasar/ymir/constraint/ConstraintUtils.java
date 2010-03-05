package org.seasar.ymir.constraint;

import static org.seasar.ymir.constraint.Globals.PREFIX_MESSAGEKEY;
import static org.seasar.ymir.constraint.Globals.PREFIX_MESSAGEKEY_FULL;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;

public class ConstraintUtils {
    private ConstraintUtils() {
    }

    public static String getFullMessageKey(String constraintKey,
            String messageKey) {
        String fullMessageKey;
        if (messageKey == null || messageKey.length() == 0) {
            fullMessageKey = PREFIX_MESSAGEKEY + constraintKey;
        } else if (messageKey.startsWith(PREFIX_MESSAGEKEY_FULL)) {
            fullMessageKey = messageKey.substring(PREFIX_MESSAGEKEY_FULL
                    .length());
        } else {
            fullMessageKey = PREFIX_MESSAGEKEY + constraintKey + "."
                    + messageKey;
        }
        return fullMessageKey;
    }

    public static String getFullMessageKey(String constraintKey) {
        return PREFIX_MESSAGEKEY + constraintKey;
    }

    /**
     * @since 1.0.7
     */
    public static boolean isEmpty(Request request, String name,
            boolean completely, boolean allowWhitespace,
            boolean allowFullWidthWhitespace) {
        String[] values = request.getParameterValues(name);
        if (values != null) {
            if (completely) {
                for (int i = 0; i < values.length; i++) {
                    if (isEmpty(values[i], allowWhitespace,
                            allowFullWidthWhitespace)) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    if (!isEmpty(values[i], allowWhitespace,
                            allowFullWidthWhitespace)) {
                        return false;
                    }
                }
            }
        }
        FormFile[] files = request.getFileParameterValues(name);
        if (files != null) {
            if (completely) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getSize() == 0) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getSize() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @since 1.0.7
     */
    public static boolean isEmpty(String value, boolean allowWhitespace,
            boolean allowFullWidthWhitespace) {
        if (value == null) {
            return true;
        }

        if (!allowWhitespace) {
            value = value.trim();
        }
        if (!allowFullWidthWhitespace) {
            value = value.replace("　", "");
        }
        return value.length() == 0;
    }
}
