
package com.woodduck.alarmme;

import java.io.Serializable;

public class EventItem implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4273756455770385397L;
    /*
     * Use the event 1. For alarm manager 2. List view Adapter 3. Database insert/delete/update
     */
    String eventName;
    String eventDetail;
    String audioPath;
    String videoPath;
    String execute_time;
    int _id;

    public EventItem() {
    }

    public EventItem(String eventName, String eventDetail, String audioPath, String videoPath, String execute_time) {
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
        this.execute_time = execute_time;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getId() {
        return _id;
    }

    public void setName(String eventName) {
        this.eventName = eventName;
    }

    public String getName() {
        return eventName;
    }

    public void setDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getDetail() {
        return eventDetail;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setExecuteTime(String execute_time) {
        this.execute_time = execute_time;
    }

    public String getExecuteTime() {
        return execute_time;
    }

    public String toString() {
        return eventName + ":" +
                eventDetail + ":" +
                audioPath + ":" +
                videoPath + ":" +
                execute_time + "";
    }
}
