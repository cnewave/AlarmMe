
package com.woodduck.alarmme;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VideoFragment extends Fragment {
    private String TAG = "VideoFragment";

    public static VideoFragment newInstance() {
        VideoFragment f = new VideoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        Log.d(TAG, "create view....");
        View view = inflater.inflate(R.layout.audiorecorder, container, false);

        return view;
    }

}
