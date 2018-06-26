package com.soundboard.models;

public class Sound {

    private String mName;
    private int audioID;
    private int iconID;

    public Sound(String name, int audioID, int iconID) {
        this.mName = name;
        this.audioID = audioID;
        this.iconID = iconID;
    }

    public int getAudioID() {
        return audioID;
    }

    public void setAudioID(int resourceId) {
        this.audioID = resourceId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }

    public int getIconID() {
        return iconID;
    }
}
