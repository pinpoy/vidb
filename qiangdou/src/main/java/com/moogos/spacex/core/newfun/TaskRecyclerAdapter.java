package com.moogos.spacex.core.newfun;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mogo.space.R;
import com.moogos.spacex.constants.AppConfig;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Desc
 * Created by xupeng on 2018/1/18.
 */

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ItemViewHolder> {
    private Context mContext;
    private String inviteNum = AppConfig.sum_inviter;//邀请人数
    private int[] normalNum = {3, 5, 10, 20};   //额定人数
    private String[] vipLeve = {"可领取VIP1", "可领取VIP2", "可领取VIP3", "可领取VIP4"};   //vip等级

    public TaskRecyclerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public TaskRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // item_home_recycler (抢单使用布局)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, null, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final TaskRecyclerAdapter.ItemViewHolder holder, final int position) {
        //先建立点击事件，后判断只有符合vip的时候可点击
        holder.taskFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getVip(position, holder.taskFlag);
            }
        });

        if (position == 0) {
            holder.taskTitle.setText(String.format("累计邀请人数达到%s/3人", inviteNum));
            initStatus(holder, position);
        } else if (position == 1) {
            holder.taskTitle.setText(String.format("累计邀请人数达到%s/5人", inviteNum));
            initStatus(holder, position);
        } else if (position == 2) {
            holder.taskTitle.setText(String.format("累计邀请人数达到%s/10人", inviteNum));
            initStatus(holder, position);
        } else if (position == 3) {
            holder.taskTitle.setText(String.format("累计邀请人数达到%s/20人", inviteNum));
            initStatus(holder, position);
        }

    }

    /**
     * 填充条目的状态信息
     *
     * @param holder
     * @param position
     */
    private void initStatus(ItemViewHolder holder, int position) {
        //已领取的状态
        if (TaskBarActivity.isGetVip[position]) {
            holder.taskFlag.setText("已领取");
            holder.taskFlag.setClickable(false);
            return;
        }
        //根据vip的领取规则进行状态显示
        if (Integer.valueOf(inviteNum) >= normalNum[position]) {
            holder.taskFlag.setText(vipLeve[position]);
            holder.taskFlag.setBackgroundResource(R.drawable.rectangle_task_red);
            holder.taskFlag.setTextColor(mContext.getResources().getColor(R.color.e82400));
            holder.taskFlag.setClickable(true);
        } else {
            holder.taskFlag.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.task_title)
        TextView taskTitle;
        @Bind(R.id.task_flag)
        TextView taskFlag;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    private TaskVipListener listener;

    public void setTaskVipListener(TaskVipListener listener) {
        this.listener = listener;
    }


    /**
     */
    public interface TaskVipListener {

        /**
         * 获取vip
         *
         * @param position
         * @param taskFlag
         */
        void getVip(int position, TextView taskFlag);


    }


}
