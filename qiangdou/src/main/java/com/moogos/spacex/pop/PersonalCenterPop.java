package com.moogos.spacex.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.util.DensityUtils;


/**
 * @author: 徐鹏android
 * @Description: 右上角个人中心的展开页面
 * @time: create at 2017/7/13 16:05
 */
public class PersonalCenterPop {
    private Context mContext;
    private static PersonalCenterPop instance;
    private PopupWindow mPopupWindow;
    private TextView tvMyRed;
    private TextView tvTask;
    private TextView tvSeetting;
    private TextView tvShare;
    private TextView tvHelp;
    private TextView userId;
    private TextView vipLeve;


    private PersonalCenterPop(Context context) {
        this.mContext = context;

    }

    public static PersonalCenterPop getInstance(Context context) {
        if (null == instance) {
            instance = new PersonalCenterPop(context);
        }

        return instance;
    }


    private void initPopAtLocation(Context context) {
        View contentView = getContentView(context);
        mPopupWindow = new PopupWindow(contentView, DensityUtils.dip2px(context, 230), ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setAnimationStyle(R.style.pop_anim_style_from_bottom);
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

//        mPopupWindow.setOnDismissListener(() -> {
//            dismissPopupView();
//        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupView();
            }
        });
    }

    private View getContentView(Context context) {
        View contentView = View.inflate(context, R.layout.pop_personal_center, null);
        tvMyRed = (TextView) contentView.findViewById(R.id.tv_my_red);
        tvTask = (TextView) contentView.findViewById(R.id.tv_task);
        tvSeetting = (TextView) contentView.findViewById(R.id.tv_seetting);
        tvShare = (TextView) contentView.findViewById(R.id.tv_share);
        tvHelp = (TextView) contentView.findViewById(R.id.tv_help);

        userId = (TextView) contentView.findViewById(R.id.user_id);
        vipLeve = (TextView) contentView.findViewById(R.id.vip_leve);

        tvMyRed.setOnClickListener(new View.OnClickListener() {     //我的红包
            @Override
            public void onClick(View view) {
                listener.onMyRed();
            }
        });
        tvTask.setOnClickListener(new View.OnClickListener() {     //任务栏
            @Override
            public void onClick(View view) {
                listener.onTask();
            }
        });
        tvSeetting.setOnClickListener(new View.OnClickListener() {   //设置
            @Override
            public void onClick(View view) {
                listener.onSeetting();
            }
        });
        tvShare.setOnClickListener(new View.OnClickListener() {     //分享
            @Override
            public void onClick(View view) {
                listener.onShare();
            }
        });
        tvHelp.setOnClickListener(new View.OnClickListener() {     //帮助
            @Override
            public void onClick(View view) {
                listener.onHelp();
            }
        });
        //登录判断
        if (AppConfig.getConfig(mContext).isLogin()) {
            userId.setVisibility(View.VISIBLE);
            userId.setText("用户ID：" + AppConfig.getConfig(mContext).getUserInfo().getInvitation_code());
        } else {
            userId.setVisibility(View.GONE);
        }
        //vip等级显示
        vipLeve.setText(String.format("VIP等级：%s", AppConfig.vip_type));
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

    private PersonalCenterListener listener;

    public void setPersonalCenterListener(PersonalCenterListener listener) {
        this.listener = listener;
    }


    /**
     * PopupWindow选择Item的监听器
     */
    public interface PersonalCenterListener {

        /**
         * 我的红包
         */
        void onMyRed();

        /**
         * 任务栏
         */
        void onTask();

        /**
         * 设置
         */
        void onSeetting();

        /**
         * 分享
         */
        void onShare();

        /**
         * 帮助
         */
        void onHelp();


    }


}
