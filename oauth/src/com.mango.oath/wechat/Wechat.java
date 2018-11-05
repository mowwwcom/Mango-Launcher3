package com.mango.oath.wechat;

import android.content.Context;

import com.android.launcher3.BuildConfig;
import com.google.common.base.Preconditions;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Random;

/**
 * @author tic
 * created on 18/11/4.
 */

public class Wechat {
    static final String APP_ID = "";
    static final String URL_OAUTH = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static void sendAuthRequest(IWXAPI api, String csrfSession) {
        Preconditions.checkNotNull(csrfSession);
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = csrfSession;
        api.sendReq(req);
    }

    public static IWXAPI getApi(Context context) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(APP_ID);
        return api;
    }

    /**
     * protect for CSRF attack
     *
     * @return code
     */
    public static String createCSRF() {
        StringBuilder csrf = new StringBuilder();
        csrf.append(BuildConfig.APPLICATION_ID)
                .append("-");
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            csrf.append(random.nextInt());
        }
        return csrf.toString();
    }

}
