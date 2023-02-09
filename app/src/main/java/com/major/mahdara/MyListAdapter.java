package com.major.mahdara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    public MyListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return CentreActivity.titres.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.my_list_item, null);

        TextView itemView = (TextView)view.findViewById(R.id.itemView);
        TextView itemView0 = (TextView)view.findViewById(R.id.itemView0);

        itemView.setText(CentreActivity.titres[position]+"\n"+CentreActivity.versets[position]);
        itemView.setTextSize(18);
        itemView0.setText((position+1)+"");
        itemView0.setTextSize(14);

        return view;
    }
}
