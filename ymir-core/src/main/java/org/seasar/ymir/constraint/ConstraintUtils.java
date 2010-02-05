package org.seasar.ymir.constraint;

import static org.seasar.ymir.constraint.Globals.PREFIX_MESSAGEKEY;
import static org.seasar.ymir.constraint.Globals.PREFIX_MESSAGEKEY_FULL;

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
}
