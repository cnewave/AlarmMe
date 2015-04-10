package com.woodduck.alarmme;

public class EventItem {

    String eventName;
    String eventDetail;
    String audioPath;
    String videoPath;
    String execute_time;
    public EventItem(String eventName, String eventDetail, String audioPath, String videoPath, String execute_time) {
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
        this.execute_time = execute_time;
    }
    public String getName() {
        return eventName;
    }
    public String getDetail() {
        return eventDetail;
    }
    public String getAudioPath() {
        return audioPath;
    }
    public String getVideoPath() {
        return videoPath;
    }
    public String getExecuteTime() {
        return execute_time;
    }
}