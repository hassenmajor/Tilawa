package com.major.mahdara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MySearchAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> list;

    public MySearchAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }
    public MySearchAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(R.layout.my_search_item, parent, false);

        TextView itemView = (TextView)view.findViewById(R.id.itemView);
        TextView itemView0 = (TextView)view.findViewById(R.id.itemView0);

        int i = Integer.valueOf(list.get(position).split(";")[0]);
        int j = Integer.valueOf(list.get(position).split(";")[1]);
        itemView.setText("﴿ "+CentreActivity.simpleQuran[i-1][j-1]+" ﴾");
        itemView.setTextSize(20);
        itemView0.setText("("+list.get(position)+")");
        itemView0.setTextSize(12);

        return view;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void addItem(int chapitre, int verset) {
        list.add(chapitre+";"+verset);
        notifyDataSetChanged();
    }

    public void clearItem() {
        list.clear();
        notifyDataSetChanged();
    }

}
