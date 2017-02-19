package net.unit8.sigcolle.auth;

import enkan.security.UserPrincipal;
import lombok.Data;

/**
 * ユーザーのログイン認証情報.
 * enkanの認証機構と連携するために {@link UserPrincipal} を拡張しており、
 * ログイン認証情報はすべてこのクラスで保持する.
 * (see also: http://enkan.github.io/guide/authentication.html )
 * @author blackawa
 */
@Data
public class LoginUserPrincipal implements UserPrincipal {
    private Long userId;
    private String userName;

    public LoginUserPrincipal(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public String getName() {
        return "user";
    }
}
