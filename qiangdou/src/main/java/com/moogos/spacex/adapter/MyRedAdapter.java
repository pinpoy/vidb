package com.moogos.spacex.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.bean.MyWealth;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiaokewang on 2017/12/18.
 */

public class MyRedAdapter extends BaseAdapter {
    private Context context;
    private List<MyWealth.ResultBean.RecordsBean> records;

    public MyRedAdapter(Context context, List<MyWealth.ResultBean.RecordsBean> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHodler = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_my_red, null);
            viewHodler = new ViewHolder(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHolder) convertView.getTag();
        }
        MyWealth.ResultBean.RecordsBean recordsBean = records.get(i);
        viewHodler.mineItemBagName.setText(recordsBean.getHost_nick_name());
        viewHodler.mineItemBagFrom.setText(recordsBean.getHongbao_channel());
        viewHodler.mineItemBagCoin.setText(String.valueOf(recordsBean.getHongbao_money()));
        viewHodler.mineItemBagTime.setText(recordsBean.getActive_time());

        return convertView;
    }


    static class ViewHolder {

        @Bind(R.id.mine_item_bag_name)
        TextView mineItemBagName;
        @Bind(R.id.mine_item_bag_from)
        TextView mineItemBagFrom;
        @Bind(R.id.mine_item_bag_coin)
        TextView mineItemBagCoin;
        @Bind(R.id.mine_item_bag_time)
        TextView mineItemBagTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
