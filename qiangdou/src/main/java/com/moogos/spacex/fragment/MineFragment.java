package com.moogos.spacex.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mogo.space.R;
import com.moogos.spacex.model.RecievedRedBagEntity;
import com.moogos.spacex.util.CallBack;
import com.moogos.spacex.util.MLog;
import com.moogos.spacex.util.NetUtils;
import com.moogos.spacex.util.Util;
import com.moogos.spacex.views.UINavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobo on 15/12/11.
 */

public class MineFragment extends Fragment implements FragmentInterface {
    public MineFragment() {

    }

    @ViewInject(R.id.mine_bag_btn)
    Button bag_btn;
    @ViewInject(R.id.mine_rank_btn)
    Button rank_btn;
    @ViewInject(R.id.mine_bag_view)
    RelativeLayout mine_bag_view;
    @ViewInject(R.id.mine_rank_view)
    RelativeLayout mine_rank_view;
    @ViewInject(R.id.mine_navigation)
    UINavigationView navigation;

    @ViewInject(R.id.mine_count)
    TextView mine_count;

    @ViewInject(R.id.mine_coin)
    TextView mine_coin;


    List<Map<String, String>> mine_bag_data = null;
    List<Map<String, String>> rank_data = null;

    Map<String, String> rank_head = null;
    Map<String, String> mine_bag_head = null;

    int mine_bag_count = 0;
    double mine_bag_coin = 0.0;

    Handler handler = new Handler();

    public void init() {
        rank_head = new HashMap<String, String>();
        rank_head.put("rank_id", "排行");
        rank_head.put("name", "昵称");
        rank_head.put("num", "红包个数");
        rank_head.put("coin", "金额(元)");

        mine_bag_head = new HashMap<String, String>();
        mine_bag_head.put("name", "金主爸爸");
        mine_bag_head.put("from", "来自");
        mine_bag_head.put("time", "时间");
        mine_bag_head.put("coin", "金额(元)");

        rank_data = new ArrayList<Map<String, String>>();
        mine_bag_data = new ArrayList<Map<String, String>>();
        rank_data.add(rank_head);
        getMineBagData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    final JSONArray rank_jsa = NetUtils.getRankFromSP(getActivity());
                    if (rank_jsa.length() == 0) {
                        NetUtils.getRank(getActivity(), new CallBack() {
                            @Override
                            public void run(String content) {
                                try {
                                    JSONArray jsonArray = new JSONArray(content);
                                    parseRankData(jsonArray);
                                    updateRankAdapter();
                                } catch (Exception e) {
                                    MLog.e("Parse Rank Array FAILED content=" + content, e);
                                }
                            }
                        });
                    } else {
                        NetUtils.getRank(getActivity(), null);
                        parseRankData(rank_jsa);
                        updateRankAdapter();
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private void parseRankData(JSONArray jsonArray) throws Exception {
        if (jsonArray.length() > 0) {
            if (rank_data == null) {
                rank_data = new ArrayList<Map<String, String>>();
            }
            rank_data.clear();
            rank_data.add(rank_head);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jso = jsonArray.getJSONObject(i);
                Map<String, String> rank_tmp = new HashMap<String, String>();
                rank_tmp.put("rank_id", (i + 1) + "");
                rank_tmp.put("name", jso.getString("nick_name"));
                rank_tmp.put("num", jso.getString("num"));
                rank_tmp.put("coin", jso.getString("cash"));
                rank_data.add(rank_tmp);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this);
        navigation.setFragmentInterface(this);
        active_tab(0);
        mine_count.setText("您共抢到" + mine_bag_count + "个红包");
        mine_coin.setText(mine_bag_coin + "");

    }

    ListView bag_lv;
    ListView rank_lv;

    class SpecialAdapter extends SimpleAdapter {

        public SpecialAdapter(Context context, List<? extends Map<String, ?>> data,
                              int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

//        @Override
//        public int getCount() {
//            return Util.RANK_DATA.size(); //返回数组的长度
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            int rank_id = 0;
            try {
                rank_id = Integer.parseInt(((TextView) view.findViewById(R.id.mine_item_rank_rankid)).getText().toString());
                if (rank_id >= 1 && rank_id <= 3) {
                    ((TextView) view.findViewById(R.id.mine_item_rank_rankid)).setTextColor(getResources().getColor(R.color.red));
                    ((TextView) view.findViewById(R.id.mine_item_rank_name)).setTextColor(getResources().getColor(R.color.red));
                    ((TextView) view.findViewById(R.id.mine_item_rank_num)).setTextColor(getResources().getColor(R.color.red));
                    ((TextView) view.findViewById(R.id.mine_item_rank_coin)).setTextColor(getResources().getColor(R.color.red));

                }
            } catch (Exception e) {
            }
            return view;
        }

    }

    SpecialAdapter rank_adapter = null;
    SimpleAdapter bag_adapter = null;

    private void updateRankAdapter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (rank_adapter != null) {
                    rank_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void updateBagAdapter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getMineBagData();
                if (bag_adapter != null) {
                    bag_adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            init();
        } catch (Exception e) {
        }
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        bag_lv = (ListView) view.findViewById(R.id.mine_bag_view_list);
        bag_adapter = new SimpleAdapter(getActivity(), mine_bag_data, R.layout.fragment_mine_item_bag,
                new String[]{"name", "time", "coin", "from"},
                new int[]{R.id.mine_item_bag_name, R.id.mine_item_bag_time, R.id.mine_item_bag_coin, R.id.mine_item_bag_from});
        bag_lv.setAdapter(bag_adapter);
        bag_lv.setClickable(false);

        rank_lv = (ListView) view.findViewById(R.id.mine_rank_view_list);
        rank_adapter = new SpecialAdapter(getActivity(), rank_data, R.layout.fragment_mine_item_rank,
                new String[]{"rank_id", "name", "num", "coin"},
                new int[]{R.id.mine_item_rank_rankid, R.id.mine_item_rank_name, R.id.mine_item_rank_num, R.id.mine_item_rank_coin});
        rank_lv.setAdapter(rank_adapter);
        rank_lv.setClickable(false);

        /**--------------------------新增--------------------------------------------*/
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    public void getMineBagData() {
        if (mine_bag_data == null) {
            mine_bag_data = new ArrayList<Map<String, String>>();
        }
        mine_bag_data.clear();
        mine_bag_data.add(mine_bag_head);
        double total = 0.0;
        DbUtils db = DbUtils.create(getActivity());
        ;
        try {
            List<RecievedRedBagEntity> list = db.findAll(Selector.from(RecievedRedBagEntity.class).orderBy("time", true));
            if (list != null) {
                for (RecievedRedBagEntity rrg : list) {
                    Map<String, String> bag_tmp = new HashMap<String, String>();
                    bag_tmp.put("name", rrg.getName());
                    bag_tmp.put("time", Util.formatDate(rrg.getTime()));
                    bag_tmp.put("coin", rrg.getCoin());
                    bag_tmp.put("from", rrg.getFrom());
                    total += Double.parseDouble(rrg.getCoin());
                    mine_bag_data.add(bag_tmp);
                }
                mine_bag_count = mine_bag_data.size() - 1;
                mine_bag_coin = Double.parseDouble(String.format("%.2f", total));
            }
        } catch (Exception e) {
            MLog.e("Mine get Reci redBag failed", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void navigateBack() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    // 0 bag 1 rank
    public void active_tab(int type) {
        switch (type) {
            case 0:
                mine_rank_view.setVisibility(View.INVISIBLE);
                mine_bag_view.setVisibility(View.VISIBLE);
                bag_btn.setTextColor(getResources().getColor(R.color.mine_bag_title_click));
                rank_btn.setTextColor(getResources().getColor(R.color.mine_bag_title_noclick));
                break;
            case 1:
                mine_bag_view.setVisibility(View.INVISIBLE);
                mine_rank_view.setVisibility(View.VISIBLE);
                rank_btn.setTextColor(getResources().getColor(R.color.mine_bag_title_click));
                bag_btn.setTextColor(getResources().getColor(R.color.mine_bag_title_noclick));
                break;
        }
    }

    @OnClick({R.id.mine_bag_btn, R.id.mine_rank_btn})
    public void btnClicked(View v) {
        Activity activity = getActivity();
        switch (v.getId()) {
            case R.id.mine_bag_btn:
                active_tab(0);
                break;
            case R.id.mine_rank_btn:
                active_tab(1);
                break;
        }
    }

}
