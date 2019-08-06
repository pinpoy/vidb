package com.moogos.spacex.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mogo.space.R;
import com.moogos.spacex.constants.ConfigEntry;
import com.moogos.spacex.constants.GlobalConfig;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.Util;
import com.moogos.spacex.views.AmountView;
import com.moogos.spacex.views.UINavigationView;

//import com.moogos.spacex.util.Log;

public class SettingsFragment extends Fragment implements FragmentInterface {

    @ViewInject(R.id.open)
    public Button open;

    @ViewInject(R.id.open_qq)
    public Button open_qq;

    //  @ViewInject(R.id.voice)
    //  public  Button voice;

    @ViewInject(R.id.shield_btn)
    public Button open_shield;

    @ViewInject(R.id.autoreply)
    public Button autoreply;

    //    @ViewInject(R.id.close_ad)
    //    public  Button close_ad;
    //    @ViewInject(R.id.autoreply_words)
    //    public  Button autoreply_words;

    @ViewInject(R.id.disguard)
    public Button disguard;

    //    @ViewInject(R.id.goto_restart_accessibility_parent)
    //    public RelativeLayout goto_restart_accessibility_parent;

    @ViewInject(R.id.amount_view)
    public AmountView delayQiangAmountView;

    @ViewInject(R.id.amount_view2)
    public AmountView delayReplyAmountView;

    @ViewInject(R.id.navigation)
    UINavigationView navigation;


    @ViewInject(R.id.settings_version)
    public TextView settings_version;

    @ViewInject(R.id.autoreply_words_edit)
    public EditText autoreply_words_edit;

    @ViewInject(R.id.shield_words_edit)
    public EditText shield_words_edit;

    public SettingsFragment() {

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this);


        navigation.setFragmentInterface(this);

        final Activity activity = getActivity();
        boolean isOpen = ConfigEntry.getIsWeiXinOpen();
        open.setBackgroundResource(isOpen ? R.drawable.btn_on : R.drawable.btn_off);
        boolean isQQOpen = ConfigEntry.getIsQQOpen();
        open_qq.setBackgroundResource(isQQOpen ? R.drawable.btn_on : R.drawable.btn_off);

        boolean isAutoReply = ConfigEntry.getAutoReply();
        autoreply.setBackgroundResource(isAutoReply ? R.drawable.btn_on : R.drawable.btn_off);

        boolean isMyWordsOn = ConfigEntry.getAutoReply();
        autoreply_words_edit.setEnabled(isMyWordsOn);

        boolean isShield = ConfigEntry.getIsShield();
        shield_words_edit.setEnabled(isShield);
        open_shield.setBackgroundResource(isShield ? R.drawable.btn_on : R.drawable.btn_off);

        boolean isDisguard = ConfigEntry.getLockedCanVip();
        disguard.setBackgroundResource(isDisguard ? R.drawable.btn_on : R.drawable.btn_off);

//        delayQiangAmountView= (AmountView) view.findViewById(R.id.amount_view);
//        delayReplyAmountView = (AmountView) view.findViewById(R.id.amount_view2);
        delayQiangAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayCanTimeVip(view.getContext(), amount);
                MLog.d(amount + "延时抢红包时间是===");
            }
        });
        delayQiangAmountView.setInitCount(ConfigEntry.getDelayCanTimeVip());
        delayReplyAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                ConfigEntry.setDelayReplyCanTimeVip(view.getContext(), amount);
            }
        });
        delayReplyAmountView.setInitCount(ConfigEntry.getDelayReplyCanTimeVip());
        autoreply_words_edit.setText(ConfigEntry.getReplyThink());
        autoreply_words_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = autoreply_words_edit.getText().toString();
//                if (text.trim().equals("")) {
//                    autoreply_words_edit.setText(ConfigEntry.getReplyThink());
//                }
                ConfigEntry.setReplyThink(activity, text);
            }
        });

        shield_words_edit.setText(ConfigEntry.getShieldContent());
        shield_words_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = shield_words_edit.getText().toString();
                ConfigEntry.setShieldContent(activity, text);
            }
        });

        settings_version.setText("当前版本：v" + Util.getVersion(activity));
        if (GlobalConfig.isDebug) {
            settings_version.setTextColor(Color.RED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void navigateBack() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Context context = getActivity();
//        String awr = "";
//        try{
//            awr = URLEncoder.encode(ConfigEntry.getReplyThink(),"UTF-8");
//        }catch (Exception e){
//        }
//        Util.httpPing(context, Util.ACTION_URL + Util.getCommonParam(context) +
//                "&clkid=" + Util.ACTION_ID_DONE_SETTINGS + "&open=" + Util.isOpen(context) +
//                "&voice=" + Util.isVoice(context) + "&ar=" + Util.isAutoReply(context) + "&awr=" +
//                awr + "&delay=" + Util.getQiangDelayed(context) + "&disguard=" + Util.isDisguard(context)+"&openqq="+Util.isQQOpen(context));

    }

    long lastClickTime = 0;
    int clkCnt = 0;

    @OnClick({R.id.open, R.id.open_qq, R.id.autoreply, R.id.disguard, R.id.settings_version, R.id.shield_btn})
    public void btnClicked(View v) {
        switch (v.getId()) {
            case R.id.open:
                boolean isOpen = ConfigEntry.getIsWeiXinOpen();
                open.setBackgroundResource(!isOpen ? R.drawable.btn_on : R.drawable.btn_off);
                ConfigEntry.setIsWeiXinOpen(v.getContext(), !isOpen);
                break;
            case R.id.open_qq:
                boolean isQQOpen = ConfigEntry.getIsQQOpen();
                open_qq.setBackgroundResource(!isQQOpen ? R.drawable.btn_on : R.drawable.btn_off);
                ConfigEntry.setIsQQOpen(v.getContext(), !isQQOpen);
                break;
            case R.id.shield_btn:
                if (User.isLogin) {
                    boolean isShield = ConfigEntry.getIsShield();
                    open_shield.setBackgroundResource(!isShield ? R.drawable.btn_on : R.drawable.btn_off);
                    ConfigEntry.setShield(v.getContext(), !isShield);
                    if (isShield) {
                        shield_words_edit.setEnabled(false);
                    } else {
                        shield_words_edit.setEnabled(true);
                    }
                } else {
                    Toast.makeText(v.getContext(), "使用该功能需要先登陆哦~", Toast.LENGTH_LONG).show();
//                    User.logout(v.getContext(), null);
                }
                break;
            case R.id.autoreply:
                if (User.isLogin) {
                    boolean isAutoReply = ConfigEntry.getAutoReply();
                    autoreply.setBackgroundResource(!isAutoReply ? R.drawable.btn_on : R.drawable.btn_off);
                    ConfigEntry.setAutoReply(v.getContext(), !isAutoReply);
                    if (isAutoReply) {
                        autoreply_words_edit.setEnabled(false);
                    } else {
                        autoreply_words_edit.setEnabled(true);
                    }
                } else {
                    Toast.makeText(v.getContext(), "使用该功能需要先登陆哦~", Toast.LENGTH_LONG).show();
//                    User.logout(v.getContext(), null);
                }
                break;
            case R.id.disguard:
                if (User.isVip()) {
                    boolean isDisguard = ConfigEntry.getLockedCanVip();
                    disguard.setBackgroundResource(!isDisguard ? R.drawable.btn_on : R.drawable.btn_off);
                    ConfigEntry.setLockedCanVip(v.getContext(), !isDisguard);
                } else {
                    Toast.makeText(v.getContext(), "VIP方可使用,请升级成为VIP！", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.settings_version:

                if (lastClickTime > 0) {

                    long secondClickTime = SystemClock.uptimeMillis();//距离上次开机时间
                    long dtime = secondClickTime - lastClickTime;
                    //Log.d("settings_version clkCnt:" + clkCnt + " " + dtime);
                    if (dtime < 300) {
                        clkCnt++;
                        if (clkCnt >= 5) {
                            final EditText inputServer = new EditText(v.getContext());
                            inputServer.setText("192.168.2.211");
                            inputServer.setFocusable(true);
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("测试服务器").setView(inputServer).setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    String inputStr = inputServer.getText().toString();
                                    Util.UPDATE_URL_TEST = "http://" + inputStr + ":8099/update/version.json?";
                                    Util.DEBUG = true;
                                    settings_version.setTextColor(Color.RED);

                                }
                            }).setCancelable(false);
                            Toast.makeText(v.getContext(), "已开启测试模式 server = " + inputServer.getText(), Toast.LENGTH_SHORT).show();
                            builder.show();

                        }
                    } else {
                        lastClickTime = 0;
                        clkCnt = 0;
                        GlobalConfig.isDebug = false;
                        settings_version.setTextColor(Color.BLACK);
                    }
                }
                lastClickTime = SystemClock.uptimeMillis();
                break;
        }
    }
}