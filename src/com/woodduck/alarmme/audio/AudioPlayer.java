
package com.woodduck.alarmme.audio;

import java.io.IOException;
import android.media.MediaPlayer;

public class AudioPlayer {
    MediaPlayer mPlayer;

    public AudioPlayer() {

    }

    public void play(String recordingPath) {
        if (recordingPath == null || recordingPath.isEmpty()) {
            return;
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(recordingPath);
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

    public void stop() {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

}
