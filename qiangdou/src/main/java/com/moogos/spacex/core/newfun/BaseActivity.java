package com.moogos.spacex.core.newfun;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.moogos.spacex.manager.ActivityStack;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by xupeng on 2017/12/9.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无标题栏
        ActivityStack.getInstanse().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getInstanse().removeActivity(this);
    }


    protected void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //友盟统计
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
