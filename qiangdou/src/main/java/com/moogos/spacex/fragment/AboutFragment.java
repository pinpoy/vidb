package com.moogos.spacex.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mogo.space.R;
//import com.moogos.spacex.util.Log;
import com.moogos.spacex.views.MyWebView;
import com.moogos.spacex.views.UINavigationView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class AboutFragment extends Fragment implements FragmentInterface {

    @ViewInject(R.id.navigation)
    UINavigationView navigation;

    @ViewInject(R.id.mywebview)
    MyWebView myWebView;
    public AboutFragment() {
		
	}
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        ViewUtils.inject(this);
        Bundle bundle0 = getArguments();
        String url = bundle0.getString("url");
        navigation.setFragmentInterface(this);
//        Log.d(url);
        myWebView.loadUrl(url);

    }
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;  
    }
	@Override
	public void navigateBack() {
        Activity activity = getActivity();
        if(activity!=null){
            activity.finish();
        }
	}

    @Override
    public void onPause() {
        super.onPause();
        myWebView.onPause();
    }
}
