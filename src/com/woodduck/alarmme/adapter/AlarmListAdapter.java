
package com.woodduck.alarmme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woodduck.alarmme.EventItem;
import com.woodduck.alarmme.R;

public class AlarmListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    String[] data = {
            "1st", "2nd", "3rd"
    };
    String[] details = {
            "1st.........not not say", "Body ball..", "Height 5....."
    };
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null) {
            vi = mInflater.inflate(R.layout.alarmitem, null);
        } else {
            vi = convertView;
        }
        TextView text = (TextView) vi.findViewById(R.id.title);
        // text.setText(data[position]);
        EventItem item = (EventItem) getItem(position);
        text.setText(item.getName());
        TextView detail = (TextView) vi.findViewById(R.id.detail);
        detail.setText(item.getDetail());
        return vi;

    }

}
