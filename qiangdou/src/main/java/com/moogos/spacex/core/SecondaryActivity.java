package com.moogos.spacex.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mogo.space.R;
import com.moogos.spacex.core.newfun.BaseActivity;
import com.moogos.spacex.fragment.AboutFragment;
import com.moogos.spacex.fragment.MineFragment;
import com.moogos.spacex.fragment.SettingsFragment;

public class SecondaryActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();

        setContentView(R.layout.secondary_layout);
        try {
            String name = bundle.getString("frag_name");

            if (SettingsFragment.class.getName().equals(name)) {
                getFragmentManager().beginTransaction().add(R.id.content, new SettingsFragment()).commit();
            } else if (AboutFragment.class.getName().equals(name)) {
                String url = bundle.getString("url");
                Bundle bundlePos = new Bundle();
                bundlePos.putString("url", url);
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.setArguments(bundlePos);
                getFragmentManager().beginTransaction().add(R.id.content, aboutFragment).commit();

            } else if (MineFragment.class.getName().equals(name)) {
                getFragmentManager().beginTransaction().add(R.id.content, new MineFragment()).commit();
            }
        }catch (Exception e){

        }

	}

    @Override
    protected void onResume() {
        super.onResume();
//        boolean isAlwaysAwake = Util.isAlwaysAwake(this);
//        if(isAlwaysAwake){
//            Util.doAlwaysAwake(this);
//        }else{
//            Util.cancelAlwaysAwake();
//        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            String name = intent.getExtras().getString("name");
            if (SettingsFragment.class.getName().equals(name)) {
                getFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
            } else if (AboutFragment.class.getName().equals(name)) {
                getFragmentManager().beginTransaction().replace(R.id.content, new AboutFragment()).commit();
            } else if (MineFragment.class.getName().equals(name)) {
                getFragmentManager().beginTransaction().replace(R.id.content, new MineFragment()).commit();
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
