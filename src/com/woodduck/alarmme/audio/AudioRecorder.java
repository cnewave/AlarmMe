
package com.woodduck.alarmme.audio;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {
    private String TAG = "AudioRecorder";
    MediaRecorder recorder;
    private boolean isRecording = false;

    public AudioRecorder() {
        recorder = new MediaRecorder();
    }

    public boolean getRecordState() {
        return isRecording;
    }

    public void startRecording() {
        Log.d(TAG, "startRecording..");
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getRecoringPath());
            recorder.prepare();
            recorder.start(); // Recording is now started
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isRecording = true;
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

    public void stopRecording() {
        if (recorder != null) {
            Log.d(TAG, "stopRecording..");
            recorder.stop();
            recorder.reset();
            isRecording = false;
        }
    }
}
