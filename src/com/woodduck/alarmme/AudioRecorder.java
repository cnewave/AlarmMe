package com.woodduck.alarmme;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {
    private String TAG = "AudioRecorder";
    MediaRecorder recorder;
    private boolean isRecording = false;
    public AudioRecorder(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //recorder.setOutputFile(PATH_NAME);
        recorder.setOutputFile(getRecoringPath());

    }
    public boolean getRecordState(){
        return isRecording;
    }
    
    public void startRecording(){
        Log.d(TAG,"startRecording..");
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(IOException e){
        	e.printStackTrace();
        }
        recorder.start();   // Recording is now started
        isRecording = true;
    }
    
    // need to refine the path and file name.
    private String getRecoringPath(){
        File sdpath = Environment.getExternalStorageDirectory();

        File recordpath = new File(sdpath.getAbsolutePath()+"/record" );
        if(!recordpath.exists()) recordpath.mkdir();
        
        Log.d(TAG,"get path:"+recordpath.getAbsolutePath());
        return recordpath.getAbsolutePath()+ "/" + "amc";

    }
    
    public void stopRecording(){        
        if(recorder != null){
            Log.d(TAG,"stopRecording..");
            recorder.stop();
            recorder.reset();   // You can reuse the object by going back to setAudioSource() step
            recorder.release(); // Now the object cannot be reused
            isRecording = false;
        }
    }    
}
