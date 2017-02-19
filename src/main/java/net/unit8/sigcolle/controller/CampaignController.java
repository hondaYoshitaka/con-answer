package net.unit8.sigcolle.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;

import enkan.component.doma2.DomaProvider;
import enkan.data.Flash;
import enkan.data.HttpResponse;
import enkan.data.Session;
import kotowari.component.TemplateEngine;
import net.unit8.moshas.helper.StringUtil;
import net.unit8.sigcolle.auth.LoginUserPrincipal;
import net.unit8.sigcolle.dao.CampaignDao;
import net.unit8.sigcolle.dao.SignatureDao;
import net.unit8.sigcolle.form.CampaignForm;
import net.unit8.sigcolle.form.CreateCampaignForm;
import net.unit8.sigcolle.form.SignatureForm;
import net.unit8.sigcolle.model.Campaign;
import net.unit8.sigcolle.model.UserCampaign;
import net.unit8.sigcolle.model.Signature;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import static enkan.util.BeanBuilder.builder;
import static enkan.util.HttpResponseUtils.RedirectStatusCode.SEE_OTHER;
import static enkan.util.HttpResponseUtils.redirect;
import static enkan.util.ThreadingUtils.some;

/**
 * @author kawasima
 */
public class CampaignController {
    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private DomaProvider domaProvider;

    /**
     * キャンペーン詳細画面表示.
     * @param form URLパラメータ
     * @param flash flash scope session
     * @return HttpResponse
     */
    public HttpResponse index(CampaignForm form, Flash flash) {
        if (form.hasErrors()) {
            return builder(HttpResponse.of("Invalid"))
                    .set(HttpResponse::setStatus, 400)
                    .build();
        }
        return showCampaign(form.getCampaignIdLong(),
                new SignatureForm(),
                (String) some(flash, Flash::getValue).orElse(null));
    }

    /**
     * 署名の追加処理.
     * @param form 画面入力された署名情報.
     */
    @Transactional
    public HttpResponse sign(SignatureForm form) {
        if (form.hasErrors()) {
            return showCampaign(form.getCampaignIdLong(), form, null);
        }
        Signature signature = builder(new Signature())
                .set(Signature::setCampaignId, form.getCampaignIdLong())
                .set(Signature::setName, form.getName())
                .set(Signature::setSignatureComment, form.getSignatureComment())
                .build();
        SignatureDao signatureDao = domaProvider.getDao(SignatureDao.class);
        signatureDao.insert(signature);

        return builder(redirect("/campaign/" + form.getCampaignId(), SEE_OTHER))
                .set(HttpResponse::setFlash, new Flash("ご賛同ありがとうございました！"))
                .build();
    }

    /**
     * 新規キャンペーン作成画面表示.
     */
    public HttpResponse createForm() {
        return templateEngine.render("signature/new", "form", new CreateCampaignForm());
    }

    /**
     * 新規キャンペーンを作成します.
     *
     * @param form 入力フォーム
     * @param session ユーザsession
     */
    // sessionは 画面htmlで ${session.principal.userId} みたいにinput:hiddenで埋め込むと偽装される心配があるので
    // サーバ側で取り出すほうがbetter. (研修レベルでは 画面htmlからでも問題はないが...)
    public HttpResponse create( CreateCampaignForm form, Session session) {
        if (form.hasErrors()) { // エラーに関しては参考にするcontrollerがあるので 難易度: 中~
            return templateEngine.render("signature/new","form", form);
        }
        // この箇所が ClassCastException になる人には
        // MavenProjects > re-importを行ってください. (そこそこ質問きました)
        final LoginUserPrincipal principal = (LoginUserPrincipal) session.get("principal"); // ここが難易度: 高

        // ここは 気づけたらスゴイレベル.
        final PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);

        final Campaign entity = builder(new Campaign())
                .set(Campaign::setTitle, form.getTitle())
                .set(Campaign::setStatement, processor.markdownToHtml(form.getStatement()))
                .set(Campaign::setGoal, form.getGoal())
                .set(Campaign::setCreateUserId, principal.getUserId()) // ここが難易度: 高
                .build();
        final CampaignDao dao = domaProvider.getDao(CampaignDao.class);
        dao.insert(entity);

        final Flash onetimeMessage = new Flash("キャンペーン「" + shortenTitle(entity.getTitle()) + "」を作成しました。");
        // ここは最悪、キャンペーン一覧 or indexページに遷移していても問題はない.
        return builder(redirect("/campaign/" + entity.getCampaignId(), SEE_OTHER)) // entityから キャンペーンIDを取り出すという発想が難易度: 高↑↑
                .set(HttpResponse::setFlash, onetimeMessage)
                .build();
    }

    /**
     * キャンペーン詳細画面を表示します.
     *
     * @param campaignId 表示するキャンペーンのID
     * @param form 署名画面のform
     * @param message 画面に表示するメッセージ
     */
    private HttpResponse showCampaign(Long campaignId, SignatureForm form, String message) {
        CampaignDao campaignDao = domaProvider.getDao(CampaignDao.class);
        UserCampaign campaign = campaignDao.selectById(campaignId);

        SignatureDao signatureDao = domaProvider.getDao(SignatureDao.class);
        int count = signatureDao.countByCampaignId(campaignId);

        Object[] params = {
                "campaign", campaign,
                "count", count,
                "signature", form,
                "message", message
        };
        return templateEngine.render("campaign", params);
    }

    /**
     * 必要があればタイトルを短縮して表示します.
     *
     * @param title タイトル
     * @return 短縮したタイトル
     */
    private static String shortenTitle (String title) {
        if (title == null || title.length() <= 20) {
            return title;
        }
        return title.substring(0,20) + "...";
    }
}
