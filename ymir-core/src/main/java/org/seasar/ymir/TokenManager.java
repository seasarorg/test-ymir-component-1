package org.seasar.ymir;

public interface TokenManager {
    Token newToken();

    String getTokenKey();

    String generateToken();

    String getToken(String tokenKey);

    boolean isTokenValid(String tokenKey);

    boolean isTokenValid(String tokenKey, boolean reset);

    void resetToken(String tokenKey);

    void saveToken(String tokenKey);

    void saveToken(String tokenKey, boolean force);
}
