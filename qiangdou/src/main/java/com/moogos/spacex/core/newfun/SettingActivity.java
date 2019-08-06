package com.moogos.spacex.core.newfun;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mogo.space.R;
import com.moogos.spacex.constants.AppConfig;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.manager.ActivityStack;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.views.AmountView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 * Created by xupeng on 2017/12/8.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.open)
    Button open;
    @Bind(R.id.open_qq)
    Button openQq;
    @Bind(R.id.tv_auto_reply)
    TextView tvAutoReply;
    @Bind(R.id.autoreply)
    Button autoreply;
    @Bind(R.id.autoreply_words_edit)
    EditText autoreplyWordsEdit;
    @Bind(R.id.ll_vip)
    LinearLayout llVip;
    @Bind(R.id.disguard)
    Button disguard;
    @Bind(R.id.amount_view)
    AmountView amountView;
    @Bind(R.id.amount_view2)
    AmountView amountView2;
    @Bind(R.id.settings_version)
    TextView settingsVersion;
    @Bind(R.id.shield_btn)
    Button shieldBtn;
    @Bind(R.id.shield_words_edit)
    EditText shieldWordsEdit;
    private PopupWindow popupWindow;
    private View inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
        initListen();
    }

    private void initListen() {
        //屏蔽关键字
        shieldWordsEdit.setText(ConfigEntry.getShieldContent());
        shieldWordsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = shieldWordsEdit.getText().toString();
                ConfigEntry.setShieldContent(SettingActivity.this, text);
            }
        });

        //延时抢红包
        amountView.setInitCount(ConfigEntry.getDelayCanTimeVip());
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayCanTimeVip(view.getContext(), amount);
                MLog.d(amount + "延时抢红包时间是===");
            }
        });

        //延时回复
        amountView2.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayReplyCanTimeVip(view.getContext(), amount);
            }
        });
        amountView2.setInitCount(ConfigEntry.getDelayReplyCanTimeVip());

    }

    private void initView() {
        //设置默认图标
        open.setBackgroundResource
                (ConfigEntry.getIsWeiXinOpen() ? R.drawable.setting_open : R.drawable.setting_close);
        openQq.setBackgroundResource
                (ConfigEntry.getIsQQOpen() ? R.drawable.setting_open : R.drawable.setting_close);
        autoreply.setBackgroundResource
                (ConfigEntry.getAutoReply() ? R.drawable.setting_open : R.drawable.setting_close);
        autoreplyWordsEdit.setEnabled(ConfigEntry.getAutoReply());      //是否可编辑
        disguard.setBackgroundResource
                (ConfigEntry.getLockedCanVip() ? R.drawable.setting_open : R.drawable.setting_close);
        shieldBtn.setBackgroundResource
                (ConfigEntry.getIsShield() ? R.drawable.setting_open : R.drawable.setting_close);

        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayCanTimeVip(view.getContext(), amount);
                MLog.d(amount + "延时抢红包时间是===");
            }
        });

        amountView.setInitCount(ConfigEntry.getDelayCanTimeVip());


        amountView2.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayReplyCanTimeVip(view.getContext(), amount);
            }
        });
        amountView2.setInitCount(ConfigEntry.getDelayReplyCanTimeVip());


        autoreplyWordsEdit.setText(ConfigEntry.getReplyThink());
        autoreplyWordsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = autoreplyWordsEdit.getText().toString();
//                if (text.trim().equals("")) {
//                    autoreply_words_edit.setText(ConfigEntry.getReplyThink());
//                }
                ConfigEntry.setReplyThink(getApplicationContext(), text);
            }
        });
    }


    @OnClick({R.id.open, R.id.open_qq, R.id.autoreply, R.id.disguard,
            R.id.iv_back, R.id.tv_logout, R.id.shield_btn})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.open:
                boolean isOpen = ConfigEntry.getIsWeiXinOpen();
                open.setBackgroundResource(!isOpen ? R.drawable.setting_open : R.drawable.setting_close);
                ConfigEntry.setIsWeiXinOpen(view.getContext(), !isOpen);
                break;
            case R.id.open_qq:
                boolean isQQOpen = ConfigEntry.getIsQQOpen();
                openQq.setBackgroundResource(!isQQOpen ? R.drawable.setting_open : R.drawable.setting_close);
                ConfigEntry.setIsQQOpen(view.getContext(), !isQQOpen);
                break;

            case R.id.autoreply:
                if (AppConfig.getConfig(this).isLogin()) {      //判断是否登录
                    boolean isAutoReply = ConfigEntry.getAutoReply();
                    autoreply.setBackgroundResource(!isAutoReply ? R.drawable.setting_open : R.drawable.setting_close);
                    ConfigEntry.setAutoReply(view.getContext(), !isAutoReply);
                    if (isAutoReply) {
                        autoreplyWordsEdit.setEnabled(false);
                    } else {
                        autoreplyWordsEdit.setEnabled(true);
                    }
                } else {
                    Toast.makeText(view.getContext(), "使用该功能需要先登陆哦~", Toast.LENGTH_LONG).show();
//                    User.logout(view.getContext(), null);
                }
                break;
            //屏蔽按钮
            case R.id.shield_btn:
                if (AppConfig.getConfig(this).isLogin()) {
                    boolean isShield = ConfigEntry.getIsShield();
                    shieldBtn.setBackgroundResource(!isShield ? R.drawable.setting_open : R.drawable.setting_close);
                    ConfigEntry.setShield(this, !isShield);
                    if (isShield) {
                        shieldWordsEdit.setEnabled(false);
                    } else {
                        shieldWordsEdit.setEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "使用该功能需要先登陆哦~", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.disguard:
                if (AppConfig.getConfig(this).isVip()) {
                    boolean isDisguard = ConfigEntry.getLockedCanVip();
                    disguard.setBackgroundResource(!isDisguard ? R.drawable.setting_open : R.drawable.setting_close);
                    ConfigEntry.setLockedCanVip(view.getContext(), !isDisguard);
                } else {
                    Toast.makeText(view.getContext(), "VIP方可使用,请升级成为VIP！", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_back:
                finish();
                break;
            //退出登录
            case R.id.tv_logout:
                exitDialog();
                break;


        }
    }

    /**
     * @Description:退出对话框
     * @exception:
     */
    private void exitDialog() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        inflate = LayoutInflater.from(this).inflate(R.layout.exit_login, null);
        popupWindow.setContentView(inflate);
        inflate.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //是
                AppConfig.getConfig(getApplication()).clearUserInfo();  //清空数据
                AppConfig.getConfig(getApplication()).clearVipMsg();  //清空vip数据
                ConfigEntry.clearSeeting();//初始化配置
                ActivityStack.getInstanse().removeAll();        //activity移栈
                finish();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });
        inflate.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {       //否
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x50000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(inflate, Gravity.BOTTOM, 0, 0);
    }


}
