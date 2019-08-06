package com.moogos.spacex.core.newfun;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mogo.space.R;
import com.moogos.spacex.util.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 帮助webview的页面
 * Created by xupeng on 2017/12/12.
 */

public class HelpWebviewActivity extends BaseActivity {
    @Bind(R.id.web_webview)
    WebView webWebview;

    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_webview);
        ButterKnife.bind(this);


//        filePath = "file:///android_asset/detail.html";
//        filePath = "http://www.51oneone.com/hongbao/androidDetail.html";
//        filePath = "http://rcv.aiclk.com/click?CAAQIA.mwwLtO01g24FufgFqvufg2PFu2wG0v_ngOz2uh_hgOr75Ho2uhjZgHufuMz53qqfUYz4WiB3Tw5kyo6rqOwYgOPaqO5LwD6L5UpWyhE7IUJ7IFSZAhpaAUjFt4AaqO1dgOwGgwPkwPrrmv4owD4wrwDtQofPG7YoUpWBvxFQ5ZfoVMKeDPwDM4D4ngXwuv4Oz4orwbwW5loDmlPD-QbdAO6XyUu1IZSW-MSEAUsnJUjVIvjFiLSfIUdXyU9VIQusJvj2y2P7gH5dqOoXuU_ZgOnEqMP7qH1FgvbEghgau2Ehgh91uOJEqMrG0fwX-MS10C4T-QbWIOpFxH1duHzh-MuV-MuEAUE2-Mopuho2qOrUg2re5Ug2gHragO915hzYuHgU5h5YgfjEuOgU0MrGgUg70L0T0CJ7AUjFxH9FOo4DP4wlD2AeqHoYgO9kwQ6x5abFgh7X0vjVxHPWJfPGiE9fJ3_WIUbnIOdYyMuUAnpUgC6wi4GfAvuE0vS7IH6GuHDkmvJ2AvbfqfzYi4EEAa_EIhVkuC6uIhpZIabdIU7sIhJE0Q6y53u1JCD7AUjFqf9kPhuX-MS2-CbnxHgYuOgGyHPeqQ6u5aDWIMnT5hn1qfPFuC6L5abFIMp10MYpgL7UgHPWJMn144PwZ4v4w44DP44rDbwDvww4wD6jmwPLO24Ugfo2gHwFqHrSgfgSgE4DvE5rwDrouOAe521Suf5dgfnf0M_fqb4DvEArwErb5hgkuOPkgOPk0OzkqOwk0OA5wHttwz7XihnVIvPXuLeYrmEg-MS7iOV4oMS1Ahps0mw7yfPcrPzagLDmJMnV0mpgHj1dud1srP9YAvGnjUjZLUndy2z2uFe2uZw6LdEzHzYVrvGs-Uz4bUjf-UKsr90nA3usIUeXumeYrPu6AhpW0LK2qLeYyfwTgmDuIU_sIvz4zU9h5C_sy2z2uFe2uf4mo4Keufrdg2PYgf1FqHr2qH_trOAGgv5UgUb1gHoeg2jhufbEgh5egfrGgUoU0v5U5fDEzgwmMy1P5wD6D_rDOw4OP44rwbwvvwo4wtrDmvuY5F720Hwd6w5wV45rPww4wO4wowvkDErtOfPYgFeFuLeFuOgTgH4GPwP";
//        filePath = "http://www.baidu.com";
        filePath = "http://rcv.jesgoo.com/click?CAAQFw.Onn4WyGLTcbDeJQLML0JkyTcTLDxkthKMtbNMJhPMybETpVK1t9LkL9DeJ0TcvlDcwV0CIRismF5zmUF1ynweyHP1yT4niU4TcCazpNYdcGYdKIEhpCPhc9KW0hP1yDseynxenH5nHFWObwiHihTs0F0nDUGOzE2ruOuCpHTskIpWW71jy0HmnQln6y7PUmXlnM4in0iHHkE2m8_loushyU-zcs7TPMNlr3NzpM-d4CMdcb8d6V-jgupztH-eLnx1ynPet32lR8Bj6wxzpNsdgwfhruszrNBdOYITc85zrMse4k8Tcx8TcQJloM8kysPTcMEkyexTJTBecmcMpFIegVPeLVKTthPMJmKTpb3eLDYTcTYkyeBh0sFnuFnW0hP1yDseynxj0i5ycMshLbadcu3dysxzrTxePUuTPkKdgCDkgwCeOYLGRb2GLQ5noUeTPiLTgu7Ggs5etVwj0NRhPiDTJUYeoUFTrMKTgIS1L95ygI-TpCsG6CalgIRTgu5OcbJko9wGrM3hLUxjNbLdc37hc3sktsJeymJe4sx1yN5ygMwdgY8dpb8kyUxeL95HpMshpY-k69AqtHaGLHJzr98kFFinFUiAmFFnuFFOnTmni0iFnnlQm0iH0vBMLTx1yDweJHxeyhxeJeTnuUgOnn4Hyb3egeYeLhYeyDIeLbLkt0TnuUrOnF4HtnK1Lnw1Lnw1Lnw1Lnw1Lnw6nHKp0QMdP88d6xNzJV7eOnUt637Gr0_FHQ7kob-lgm0MEBwzLH_FHCmVHv0mtVPGOiOGg3AkOCMtVFK1Vs_FoGcW4iihoiAk9G3TDa8GOvYeJh7eJT0WHaF9HYezOiAlga3FHG3Tca-W4igkrbJlgC7zJm7eOiylob-dgV-MtV7eOBK1y0JzLDxFHY-Tp3Ak4itTgkNhpD-MtePzLec1nbOyJ0cMLHB1tnJetHwMJHJesU0egQ3ecTPepmxeybEet0cMgbE1gTPTpmxMt0JegHwkLbm5nbT0n90Cw8Un8Fiyn0yH00Fnun66nm0nWFiO6MwTKYJktmwUnTnA0TFHnn0ny0nmn65iNnWyymKzLmBzLhczLFsMxni+CAAQAg.x22uqfFITDolOMLAOD_DTeglOfFDgjFlgDg7vYFIO4TWOfasgez8vfK6vYogj-bkjloKQiPtI_F9CKGFOMg6gYvsOfgu2tGMgjQXSPT51e6PgeAWSUaWgyFdcUzsLUAWSU1scezMveiIvyFJxBO7V4g6vMCWO2H92HFrxVKxHr0xx28L2C2y4TFxuMKF2t22422G2fKx_2rF2rGKOUOigM1sTDHIO4_wOf_7V4oDvMHwTU_jT4o7OjrUgUC8tfHDOMvJ2rFHx2H_24F1xsXG1BClV8-5LUTjSUr0gj6pSUT5cuQMc4WMbDQhx2suHK_6ODFjKKHKCkvr_ML6ODs4TDCfOD2h_YzxTMkhODzxToa4CogAOfRq2oLF2CFFx2g_2tKtF22br_KtHK-7TMg6VfklODH6OfL6ODOg2YKxu826geoDvMLhvfHlOUF6VfgAgUFivM18vfHAVfO6gYtUO0Ftf2KtHKKF2C2H4x_KHqFtxj0WLj15chAGRM2DG2g2";
        initPage(StringUtil.formatString(filePath));

//        dd();
    }


    private void dd(){
        webWebview.loadUrl(filePath);
        WebSettings wSet = webWebview.getSettings();
        wSet.setJavaScriptEnabled(true);
        webWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });


    }

    private void initPage(String filePath) {
        webWebview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webWebview.loadUrl(filePath);
        WebSettings webSettings = webWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
