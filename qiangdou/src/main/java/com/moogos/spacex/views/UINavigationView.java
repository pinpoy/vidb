package com.moogos.spacex.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.constants.User;
import com.moogos.spacex.fragment.FragmentInterface;

@SuppressWarnings("ResourceType")
public class UINavigationView extends LinearLayout {

    public static UINavigationView instance;
    private FragmentInterface fragmentInterface;
    private Button btn_left;
    private Button btn_right;
    private TextView tv_title;

    public UINavigationView(Context context) {
        super(context);
        instance = this;
    }

    public UINavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        final int attrIds[] = new int[]{R.attr.tv_title, R.attr.left_drawable, R.attr.right_drawable};
        TypedArray array = context.obtainStyledAttributes(attrs, attrIds);

        CharSequence title = array.getText(0);
        int left_drawable = array.getResourceId(1, 0);
        int right_drawable = array.getResourceId(2, 0);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.navigation, this);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText(title);
        btn_left.setBackgroundResource(left_drawable);
        btn_right.setBackgroundResource(right_drawable);

        btn_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fragmentInterface != null) {
                    fragmentInterface.navigateBack();
                }
            }
        });
        if (User.isLogin) {
            if (User.isVip()) {
                btn_right.setBackgroundResource(R.drawable.login_in_vip_state);
            } else {
                btn_right.setBackgroundResource(R.drawable.login_in_state);
            }

        } else {
            btn_right.setBackgroundResource(R.drawable.log_out_state);
        }
        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v1) {


                if (User.isLogin) {
                    MyAlertDialog dialog = new MyAlertDialog(v1.getContext());
                    dialog.builder().setTitle("确认退出吗？").setPositiveButton("退出",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
//                                    User.logout(v1.getContext(), new CallBack() {
//                                        @Override
//                                        public void run(String content) {
//                                            getHandler().post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    btn_right.setBackgroundResource(R.drawable.log_out_state);
//                                                    Intent intent = new Intent(v.getContext(), QiangHongBaoActivity.class);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    v1.getContext().startActivity(intent);
//                                                }
//                                            });
//
//                                        }
//                                    });
                                }
                            }).setNegativeButton("取消", null).show();

                } else {
//                    User.login(v1.getContext(), new CallBack() {
//                        @Override
//                        public void run(String content) {
//                            getHandler().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (User.isVip()) {
//                                        btn_right.setBackgroundResource(R.drawable.login_in_vip_state);
//                                    } else {
//                                        btn_right.setBackgroundResource(R.drawable.login_in_state);
//                                    }
//                                }
//                            });
//                            Intent intent = new Intent(v1.getContext(), QiangHongBaoActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            v1.getContext().startActivity(intent);
//                        }
//                    }, null);
                }
            }
        });
    }

    public void setFragmentInterface(FragmentInterface i) {
        this.fragmentInterface = i;
    }

//    public static void setLogIn() {
//        if (instance != null) {
//            instance.btn_right.setBackgroundResource(R.drawable.login_in_state);
//            instance.btn_right.setClickable(false);
//        }
//    }

}