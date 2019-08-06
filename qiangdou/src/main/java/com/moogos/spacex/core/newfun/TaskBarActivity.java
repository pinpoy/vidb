package com.moogos.spacex.core.newfun;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.bean.Response;
import com.moogos.spacex.constants.ApiServiceFactory;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.constants.HttpUrls;
import com.moogos.spacex.constants.ParamsHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desc   任务栏页面
 * Created by xupeng on 2017/12/19.
 */

public class TaskBarActivity extends BaseActivity implements TaskRecyclerAdapter.TaskVipListener {
    @Bind(R.id.task_recycler_view)
    RecyclerView taskRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    public static Boolean[] isGetVip = {false, false, false, false};//vip-是否已经领取
    private String[] vipTime = {"5", "7", "10", "20"};  //vip领取的有效时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_bar);
        ButterKnife.bind(this);

        //初始化转换领取类型
        char[] chars = AppConfig.get_record.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '0') {
                isGetVip[i] = false;
            } else {
                isGetVip[i] = true;
            }
        }

        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        taskRecyclerView.setLayoutManager(mLayoutManager);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerAdapter = new TaskRecyclerAdapter(this);
        taskRecyclerAdapter.setTaskVipListener(this);
        taskRecyclerView.setAdapter(taskRecyclerAdapter);
    }


    @OnClick(R.id.iv_back)
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 领取vip
     *
     * @param position
     * @param taskFlag
     */
    @Override
    public void getVip(int position, TextView taskFlag) {
        getVipFromNet(position, taskFlag);
    }

    /**
     * 请求网络领取vip
     *
     * @param position
     * @param taskFlag
     */
    private void getVipFromNet(final int position, final TextView taskFlag) {
        final String vipType = String.valueOf(position + 1);//计算领取的vip等级
        ApiServiceFactory.getSpiderApiService(HttpUrls.HTTP_BASE_URL)
                .getVipFromNet(ParamsHelper.getVipFromNetMap(vipType, vipTime[position]))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getCode() == 200 && null != response) {
                            toast("领取成功");
                            alreadyGetStatus(position, taskFlag);//改变状态
                            AppConfig.vip_type = vipType;
                        }
                    }
                });
    }

    /**
     * 已经领取设置状态并记录
     *
     * @param position
     * @param taskFlag
     */
    private void alreadyGetStatus(int position, TextView taskFlag) {
        toast("已领取VIP" + String.valueOf(position + 1));
        taskFlag.setText("已领取");
        taskFlag.setBackgroundResource(R.drawable.rectangle_task);
        taskFlag.setTextColor(getResources().getColor(R.color.r999999));
        taskFlag.setClickable(false);
        isGetVip[position] = true;
        AppConfig.get_record =
                new StringBuffer(AppConfig.get_record).replace(position, position + 1, "1").toString();
    }
}
