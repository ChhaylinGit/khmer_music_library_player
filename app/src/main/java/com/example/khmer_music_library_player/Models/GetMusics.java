package com.example.khmer_music_library_player.Models;

public class GetMusics {
    public String musicTitle;
    public String singerID;
    public String singerName;
    public String singerImageUrl;
    public String duration;
    public String mp3Uri;

    public GetMusics(){}

    public GetMusics(String musicTitle, String singerID, String singerName, String singerImageUrl, String duration, String mp3Uri) {
        this.musicTitle = musicTitle;
        this.singerID = singerID;
        this.singerName = singerName;
        this.singerImageUrl = singerImageUrl;
        this.duration = duration;
        this.mp3Uri = mp3Uri;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getSingerID() {
        return singerID;
    }

    public void setSingerID(String singerID) {
        this.singerID = singerID;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerImageUrl() {
        return singerImageUrl;
    }

    public void setSingerImageUrl(String singerImageUrl) {
        this.singerImageUrl = singerImageUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMp3Uri() {
        return mp3Uri;
    }

    public void setMp3Uri(String mp3Uri) {
        this.mp3Uri = mp3Uri;
    }

    @Override
    public String toString() {
        return "GetMusics{" +
                "musicTitle='" + musicTitle + '\'' +
                ", singerID='" + singerID + '\'' +
                ", singerName='" + singerName + '\'' +
                ", singerImageUrl='" + singerImageUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", mp3Uri='" + mp3Uri + '\'' +
                '}';
    }
}
