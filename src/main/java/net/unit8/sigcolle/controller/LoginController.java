package net.unit8.sigcolle.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;

import enkan.collection.Multimap;
import enkan.component.doma2.DomaProvider;
import enkan.data.HttpResponse;
import enkan.data.Session;
import kotowari.component.TemplateEngine;
import net.unit8.sigcolle.auth.LoginUserPrincipal;
import net.unit8.sigcolle.dao.UserDao;
import net.unit8.sigcolle.form.LoginForm;
import net.unit8.sigcolle.model.User;
import org.seasar.doma.jdbc.NoResultException;

import static enkan.util.BeanBuilder.builder;
import static enkan.util.HttpResponseUtils.RedirectStatusCode.SEE_OTHER;
import static enkan.util.HttpResponseUtils.redirect;

/**
 * @author takahashi
 */
public class LoginController {
    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private DomaProvider domaProvider;

    private static final String INVALID_USERNAME_OR_PASSWORD = "ユーザー名とパスワードが間違っています。もう一度やり直してください。";

    /**
     * ログイン画面の初期表示.
     * @return HttpResponse
     */
    @Transactional
    public HttpResponse index() {
        return templateEngine.render("login", "login", new LoginForm());
    }

    /**
     * ログイン処理.
     * @param form 画面入力されたform情報
     * @return HttpResponse
     */
    @Transactional
    public HttpResponse login(LoginForm form) {
        if (form.hasErrors()) {
            return templateEngine.render("login", "login", form);
        }

        UserDao userDao = domaProvider.getDao(UserDao.class);
        User user;

        // メールアドレス存在チェック
        try {
            user = userDao.selectByEmail(form.getEmail());
        } catch (NoResultException e) {
            form.setErrors(Multimap.of("error", INVALID_USERNAME_OR_PASSWORD));
            return templateEngine.render("login",
                    "login", form
            );
        }

        // パスワードチェック
        if (!form.getPass().equals(user.getPass())) {
            form.setErrors(Multimap.of("error", INVALID_USERNAME_OR_PASSWORD));
            return templateEngine.render("login",
                    "login", form
            );
        }
        Session session = new Session();
        session.put(
                "principal",
                new LoginUserPrincipal(user.getUserId(), user.getLastName() + " " + user.getFirstName())
        );

        return builder(redirect("/", SEE_OTHER))
                .set(HttpResponse::setSession, session)
                .build();
    }

    /**
     * ログアウト処理.
     * @param session セッション情報
     * @return HttpResponse
     */
    @Transactional
    public HttpResponse logout(Session session) {
        session.clear();
        return builder(redirect("/", SEE_OTHER))
                .set(HttpResponse::setSession, session)
                .build();
    }
}
