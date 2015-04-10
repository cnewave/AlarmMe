package com.woodduck.alarmme.audio;

import java.io.IOException;
import android.media.MediaRecorder;
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

    public void startRecording(String recordingPath) {
        Log.d(TAG, "startRecording..");
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setMaxDuration(60 * 1000);// 60 seconds
            recorder.setOutputFile(recordingPath);
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



    public void stopRecording() {
        if (recorder != null) {
            Log.d(TAG, "stopRecording..");
            recorder.stop();
            recorder.reset();
            isRecording = false;
        }
    }
}
