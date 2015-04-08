package com.woodduck.alarmme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.woodduck.alarmme.R;

public class AlarmListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    String[] data ={"1st","2nd","3rd"};
    String[] details ={"1st.........not not say","Body ball..","Height 5....."};
    public AlarmListAdapter(Context context){
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null){
            vi = mInflater.inflate(R.layout.alarmitem, null);
        }else{
            vi = convertView;
        }
        TextView text = (TextView) vi.findViewById(R.id.title);
        text.setText(data[position]);
        TextView detail = (TextView) vi.findViewById(R.id.detail);
        detail.setText(details[position]);
        return vi;

    }
    
}