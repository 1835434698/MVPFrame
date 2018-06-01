package com.tangzy.mvpframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tangzy.mvpframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public class HomeAdapter extends BaseAdapter{
    private List<String> homes;
    private Context mContext;

    public HomeAdapter(Context context, List<String> lists){
        homes = lists;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return homes.size();
    }

    @Override
    public Object getItem(int position) {
        return homes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);

            holder = new Holder();

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        return convertView;
    }

    private class Holder{
        TextView tv_repay_money;


    }
}
