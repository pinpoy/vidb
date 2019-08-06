package com.moogos.spacex;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jesgoo.sdk.AdSize;
import com.jesgoo.sdk.AdView;
import com.mogo.space.R;

import org.json.JSONObject;


/**
 * 闪屏页
 */

public class SplashActivity extends Activity {

    boolean isFinished = false;
    boolean needFinish = true;
    private boolean mIsSkip = false;

    private ServiceConnection mPlayServiceConnection;

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            _doSkip();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AdView.preLoad(this);//s0c410d3
        AdView initialView = new AdView(this, AdSize.Initial, "s1e48036"); //sec1eb6c s1bf1cbb
        initialView.setListener(new AdView.AdViewListener() {

            @Override
            public void onAdReady(AdView adView) {
            }

            @Override
            public void onAdShow() {
                handler.sendEmptyMessageDelayed(0, 5000);
            }

            @Override
            public void onAdClick() {
            }

            @Override
            public void onAdFailed(String data) {
                _doSkip();
            }

            // 如果开屏点击了，并且已用内部浏览器打开，则在关闭LP时跳转到下一个Activity
            @Override
            public void onEvent(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (1 == jsonObject.optInt("reason", 0) && !isFinished && needFinish) {
                        _doSkip();
                    }
                } catch (Exception e) {
                }
            }

        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        addContentView(initialView, layoutParams);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("doyer", "onResume: ");
        if (mIsSkip) {
            mIsSkip = false;
            _doSkip();
        }
    }

    private void _doSkip() {
        if (!mIsSkip) {
            mIsSkip = true;
            finish();
            isFinished = true;
            startActivity(new Intent(SplashActivity.this, com.moogos.spacex.core.SplashActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsSkip = true;
        Log.d("doyer", "onPause: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsSkip = false;
        _doSkip();

    }


}
