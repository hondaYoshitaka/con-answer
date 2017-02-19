package net.unit8.sigcolle.controller;

import java.util.List;

import javax.inject.Inject;

import enkan.collection.Parameters;
import enkan.component.doma2.DomaProvider;
import enkan.data.HttpResponse;
import kotowari.component.TemplateEngine;
import net.unit8.sigcolle.dao.SignatureDao;
import net.unit8.sigcolle.model.Signature;

/**
 * @author kawasima
 */
public class SignatureController {
    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private DomaProvider domaProvider;

    /**
     * 署名リスト画面表示.
     * @param params URLパラメータ
     * @return HttpResponse
     */
    public HttpResponse list(Parameters params) {
        SignatureDao dao = domaProvider.getDao(SignatureDao.class);
        List<Signature> signatures = dao.selectAllByCampaignId(params.getLong("campaignId"));
        return templateEngine.render("signature/list",
                "signatures", signatures);
    }
}
