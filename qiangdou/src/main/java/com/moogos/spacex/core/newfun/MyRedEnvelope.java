package com.moogos.spacex.core.newfun;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.adapter.MyRedAdapter;
import com.moogos.spacex.bean.MyWealth;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的红包
 * Created by xupeng on 2017/12/18.
 */

public class MyRedEnvelope extends BaseActivity {

    @Bind(R.id.mine_coin)
    TextView mineCoin;
    @Bind(R.id.mine_bag_btn)
    Button mineBagBtn;
    @Bind(R.id.mine_rank_btn)
    Button mineRankBtn;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.mine_count)
    TextView mineCount;
    @Bind(R.id.mine_bag_view_list)
    ListView mineBagViewList;
    @Bind(R.id.mine_bag_view)
    RelativeLayout mineBagView;
    @Bind(R.id.webview)
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_envelope);
        ButterKnife.bind(this);

        initPage("http://red.0i-i0.com/stage.html");

    }

    private void initPage(String filePath) {

        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.loadUrl(filePath);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    /**
     * 填充左边栏的信息
     *
     * @param myWealth
     */
    private void initLeftRed(MyWealth myWealth) {
        if (null == myWealth.getResult()) {
            return;
        }


        mineCoin.setText(myWealth.getResult().getSum_money());    //总数

        mineBagViewList.addHeaderView(View.inflate(this, R.layout.item_my_red, null));
        mineBagViewList.setAdapter(new MyRedAdapter(this, myWealth.getResult().getRecords()));
    }

    @OnClick({R.id.mine_bag_btn, R.id.mine_rank_btn, R.id.iv_back})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.mine_bag_btn:
                active_tab(0);
                break;
            case R.id.mine_rank_btn:
                active_tab(1);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        requestForData();
    }

    /**
     * 请求网络获取红包的数据
     */
    private void requestForData() {
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .getMyRedData(ParamsHelper.getRedDataMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyWealth>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.toString();
                    }

                    @Override
                    public void onNext(MyWealth myWealth) {
                        if (null != myWealth && myWealth.getCode() == 200) {
                            initLeftRed(myWealth);
                        }
                    }
                });
    }


    public void active_tab(int type) {
        switch (type) {
            case 0:
                webview.setVisibility(View.INVISIBLE);
                mineBagView.setVisibility(View.VISIBLE);
                mineBagBtn.setTextColor(getResources().getColor(R.color.mine_bag_title_click));
                mineRankBtn.setTextColor(getResources().getColor(R.color.mine_bag_title_noclick));
                break;
            case 1:
                mineBagView.setVisibility(View.INVISIBLE);
                webview.setVisibility(View.VISIBLE);
                mineRankBtn.setTextColor(getResources().getColor(R.color.mine_bag_title_click));
                mineBagBtn.setTextColor(getResources().getColor(R.color.mine_bag_title_noclick));
                break;
        }
    }
}
