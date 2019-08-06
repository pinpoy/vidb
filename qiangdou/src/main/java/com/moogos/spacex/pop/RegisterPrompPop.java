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
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.util.DensityUtils;


/**
 * @author: 徐鹏android
 * @Description: 注册提示pop
 * @time: create at 2017/7/13 16:05
 */
public class RegisterPrompPop {
    private Context mContext;
    private static RegisterPrompPop instance;
    private PopupWindow mPopupWindow;


    private RegisterPrompPop(Context context) {
        this.mContext = context;

    }

    public static RegisterPrompPop getInstance(Context context) {
        if (null == instance) {
            instance = new RegisterPrompPop(context);
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
        View contentView = View.inflate(context, R.layout.pop_register_prom, null);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopupView();
            }
        });
        contentView.findViewById(R.id.linearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRegisterTips();
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

    private RegisterPromListener listener;

    public void setRegisterPromListener(RegisterPromListener listener) {
        this.listener = listener;
    }


    /**
     * PopupWindow选择Item的监听器
     */
    public interface RegisterPromListener {

        /**
         * 注册提示
         */
        void onRegisterTips();


    }


}
