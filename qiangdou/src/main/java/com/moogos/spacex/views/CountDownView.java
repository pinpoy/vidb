package com.moogos.spacex.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ProjectName: CountDownView
 * Description: 倒计时按钮
 * <p>
 * author: xupeng
 * version: 4.0
 * created at: 2016/8/19 13:01
 */
@SuppressWarnings("ALL")
public class CountDownView extends TextView {
    private static final String TAG = CountDownView.class.getSimpleName();

    private static enum State {
        // 未开始状态
        STATE_DEFAULT,
        // 开始倒计时不可点击
        STATE_START,
        // 倒计时完成
        STATE_FINISH
    }

    private State currentState = State.STATE_DEFAULT;
    private long countTime = 60;
    private long currentTime;
    private boolean isAuto = true;

    private boolean isStarting;     // 是否正在计时。。。

    private String defaultText;
    private String startText;
    private String finishText;

    private int bgResId;            // 背景颜色的Selector
    private int normalTxtColor;     // 文本颜色的正常（有点击效果）
    private int pressedTxtColor;    // 文本颜色的按下（无点击效果）
    private int normalBack;     //正常背景
    private int pressedBack;     //按下的背景

    private ScheduledExecutorService executorService;

    /**
     * @param context
     * @param attrs
     */
    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setOnClickListener(null);
    }

    /**
     * 初始化
     *
     * @param countTime    时长
     * @param defaultText  默认文字
     * @param finishText   完成后文字
     * @param normalResId  正常的背景
     * @param pressedResId 按下的背景
     */
    public void init(long countTime, String defaultText, String startText, String finishText,
                     int bgResId, int normalTxtColor, int pressedTxtColor) {

        this.countTime = countTime;
        this.defaultText = defaultText;
        this.startText = startText;
        this.finishText = finishText;

        this.bgResId = bgResId;
        this.normalTxtColor = normalTxtColor;
        this.pressedTxtColor = pressedTxtColor;

        if (!TextUtils.isEmpty(defaultText)) {
            setText(defaultText);
        }

        setBackgroundResource(bgResId);

        setViewEnable(false);
//        setClickable(false);
    }

    /**
     * @param countTime
     * @param defaultText
     * @param startText
     * @param finishText
     * @param normalBack      正常状态下的背景
     * @param pressedBack     被按下状态下的背景
     * @param normalTxtColor
     * @param pressedTxtColor
     */
    public void init(long countTime, String defaultText, String startText, String finishText,
                     int normalBack, int pressedBack, int normalTxtColor, int pressedTxtColor) {

        this.countTime = countTime;
        this.defaultText = defaultText;
        this.startText = startText;
        this.finishText = finishText;

        this.bgResId = bgResId;
        this.normalTxtColor = normalTxtColor;
        this.pressedTxtColor = pressedTxtColor;
        this.normalBack = normalBack;
        this.pressedBack = pressedBack;
        if (!TextUtils.isEmpty(defaultText)) {
            setText(defaultText);
        }


        //setViewEnable(false);
//        setClickable(false);
    }

    public void setViewEnable(boolean isEnable) {
        setEnabled(isEnable);

        if (isEnable) {
            setTextColor(getResources().getColorStateList(normalTxtColor));
            if (normalBack != 0) {
                setBackgroundResource(normalBack);
            }
        } else {
            setTextColor(getResources().getColor(pressedTxtColor));
            if (pressedBack != 0) {
                setBackgroundResource(pressedBack);
            }
        }
    }


    public boolean isStarting() {
        return isStarting;
    }

    /**
     * 设置是否点击后自动倒计时
     *
     * @param isAuto
     */
    public void setAutoStart(boolean isAuto) {
        this.isAuto = isAuto;
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        currentTime = countTime;

        setViewEnable(false);
        stopCountDown();
        isStarting = true;

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // reset countTime
                if (currentTime <= 0) {

                    post(new Runnable() {

                        @Override
                        public void run() {
                            setViewEnable(true);

                            setText(finishText);
                            currentState = State.STATE_FINISH;
                            isStarting = false;
                        }
                    });

                    stopCountDown();
                    return;
                }

                // start countTime
                currentTime--;
                post(new Runnable() {

                    @Override
                    public void run() {
                        setText(currentTime + startText);
                    }
                });
            }
        }, 1, 1, TimeUnit.SECONDS);

    }

    /**
     * 开始倒计时
     */
    public void start() {
        if (currentState != State.STATE_START) {
            currentState = State.STATE_START;

            startCountDown();
        }
    }

    /**
     * 停止倒计时
     */
    public void stop() {
        reset();
    }

    /**
     * 重置
     */
    public void reset() {
        stopCountDown();

        currentState = State.STATE_DEFAULT;
        setText(defaultText);
    }

    /**
     * 停止倒计时
     */
    public void stopCountDown() {
        if (null != executorService) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {

        super.setOnClickListener(new OnClickListenerWrapper(l));
    }

    private class OnClickListenerWrapper implements OnClickListener {
        private OnClickListener listener;

        public OnClickListenerWrapper(OnClickListener l) {
            listener = l;
        }

        @Override
        public void onClick(View v) {
            if (currentState != State.STATE_START && isAuto) {

                currentState = State.STATE_START;
                startCountDown();
            }

            if (null != listener) {
                listener.onClick(v);
            }
        }
    }
}
