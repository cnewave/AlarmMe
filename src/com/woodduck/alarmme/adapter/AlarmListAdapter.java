
package com.woodduck.alarmme.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woodduck.alarmme.EventItem;
import com.woodduck.alarmme.R;

public class AlarmListAdapter extends BaseAdapter {
    String TAG = "AlarmListAdapter";
    LayoutInflater mInflater;
    Context mContext;
    List<EventItem> mList;

    public AlarmListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<EventItem> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        if(mList != null){
            EventItem item = mList.get(position);
            if(item != null){
                return item.getId();
            }            
        }        
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.alarmitem, parent,false);
        } else {
            vi = convertView;
        }
        TextView text = (TextView) vi.findViewById(R.id.title);
        // text.setText(data[position]);
        EventItem item = (EventItem) getItem(position);
        Log.d(TAG,"show title:"+item.getName());
        text.setText(item.getName());
        TextView detail = (TextView) vi.findViewById(R.id.detail);
        Log.d(TAG,"show date:"+item.getExecuteTime());
        detail.setText(item.getDetail());
        return vi;

    }

}
