package com.moogos.spacex.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mogo.space.R;


/**
 * @author: 徐鹏android
 * @Description: 注册成功的pop
 * @time: create at 2017/7/13 16:05
 */
public class SucsRegisterPop {
    private Context mContext;
    private static SucsRegisterPop instance;
    private PopupWindow mPopupWindow;
    private String userId;


    private SucsRegisterPop(Context context) {
        this.mContext = context;

    }

    public static SucsRegisterPop getInstance(Context context) {
        if (null == instance) {
            instance = new SucsRegisterPop(context);
        }

        return instance;
    }


    private void initPopAtLocation(Context context) {
        View contentView = getContentView(context);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.pop_anim_style_from_center);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // measure the view's width and height
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        }


        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupView();
            }
        });
    }

    private View getContentView(Context context) {
        View contentView = View.inflate(context, R.layout.pop_sucs_register, null);
        //设置会员id
        TextView tvUserId = (TextView) contentView.findViewById(R.id.tv_user_id);
        tvUserId.setText(String.format("尊敬的用户：\n你已成功注册为我们的会员用户，你的ID为：%s，用于邀请好友时使用", userId));


        contentView.findViewById(R.id.relative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSucsRegisterTips();
            }
        });

        return contentView;
    }


    /**
     * 关闭弹出框
     */
    public void dismissPopupView() {
        //resetViewAndData();

        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 指定View为依附类
     *
     * @param parentView down at parentView
     */
    public void showPopupView(View parentView) {
        initPopAtLocation(mContext);

        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(parentView, Gravity.RIGHT, 0, 0);
    }

    private SucsRegisterListener listener;

    public void setSucsRegisterListener(SucsRegisterListener listener) {
        this.listener = listener;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * PopupWindow选择Item的监听器
     */
    public interface SucsRegisterListener {

        /**
         * 注册提示
         */
        void onSucsRegisterTips();


    }


}
