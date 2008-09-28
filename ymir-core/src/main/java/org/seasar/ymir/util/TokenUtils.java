package org.seasar.ymir.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * このクラスはYmir1.0.xで削除されます。
 * 
 * @author yokota
 */
// TODO [YMIR-1.0][#YMIR-258] 廃止する。
// TODO [YMIR-1.0][#YMIR-259] TokenManagerImplではSessionManagerを使ってセッションにアクセスするように変更する。
public class TokenUtils {
    protected TokenUtils() {
    }

    public static String generateToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            return StringUtils.getScopeKey(session.getId(), true);
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    public static String getToken(HttpServletRequest request, String tokenKey) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                return (String) session.getAttribute(tokenKey);
            }
        }
    }

    public static boolean isTokenValid(HttpServletRequest request,
            String tokenKey) {
        return isTokenValid(request, tokenKey, false);
    }

    public static boolean isTokenValid(HttpServletRequest request,
            String tokenKey, boolean reset) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        synchronized (session.getId().intern()) {
            Object saved = session.getAttribute(tokenKey);
            if (saved == null) {
                return false;
            }
            if (reset) {
                resetToken(request, tokenKey);
            }

            String token = request.getParameter(tokenKey);
            if (token == null) {
                return false;
            }

            return saved.equals(token);
        }
    }

    public static void resetToken(HttpServletRequest request, String tokenKey) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            synchronized (session.getId().intern()) {
                session.removeAttribute(tokenKey);
            }
        }
    }

    public static void saveToken(HttpServletRequest request, String tokenKey) {
        saveToken(request, tokenKey, true);
    }

    public static void saveToken(HttpServletRequest request, String tokenKey,
            boolean force) {
        HttpSession session = request.getSession(false);
        if (!force && session != null && session.getAttribute(tokenKey) != null) {
            return;
        }

        if (session == null) {
            session = request.getSession();
        }

        synchronized (session.getId().intern()) {
            String token = generateToken(request);
            if (token != null) {
                session.setAttribute(tokenKey, token);
            }
        }
    }
}
