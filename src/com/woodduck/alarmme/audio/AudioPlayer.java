package com.woodduck.alarmme.audio;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioPlayer {
	private String TAG = "AudioPlayer";
	MediaPlayer mPlayer;

	public AudioPlayer() {

	}

	// need to refine the path and file name.
	private String getRecoringPath() {
		File sdpath = Environment.getExternalStorageDirectory();

		File recordpath = new File(sdpath.getAbsolutePath() + "/record");
		if (!recordpath.exists())
			recordpath.mkdir();

		Log.d(TAG, "get path:" + recordpath.getAbsolutePath());
		return recordpath.getAbsolutePath() + "/" + "amc";

	}

	public void play() {
		try {
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(getRecoringPath());
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void stop(){
		if(mPlayer != null){
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}

}
