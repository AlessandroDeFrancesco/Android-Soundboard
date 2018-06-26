package com.soundboard.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.soundboard.models.Sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundPlayer {

    private boolean neverInterrupt;
    private MediaPlayer mPlayer;
    private Context mContext;

    private static final String TAG = "SoundPlayer";

    public SoundPlayer(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void playSound(Sound sound) {
        int resource = sound.getAudioID();

        if (neverInterrupt){
            // play multiple sounds contemporary
            playSoundInNewInstance(sound);
        }
        else {
            // play just one sound at a time
            if (mPlayer != null) {
                if (mPlayer.isPlaying())
                    mPlayer.stop();
                mPlayer.reset();

                try {
                    AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(resource);
                    if (afd == null)
                        return;
                    mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mPlayer.prepare();
                } catch (IOException | IllegalArgumentException | SecurityException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else {
                mPlayer = MediaPlayer.create(mContext, resource);
            }

            mPlayer.start();
        }
    }

    private void playSoundInNewInstance(Sound sound) {
        final MediaPlayer player = MediaPlayer.create(mContext, sound.getAudioID());
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });
        player.start();
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    // play the audios in reverse order to take account of the chronological order
    public void playSoundsSubsequently(final List<Sound> sounds, final int startIndex) {
        if(startIndex < sounds.size()) {
            final MediaPlayer player = MediaPlayer.create(mContext, sounds.get(sounds.size() - 1 - startIndex).getAudioID());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.release();
                    playSoundsSubsequently(sounds, startIndex + 1);
                }
            });
            player.start();
        }
    }

    public void playSoundsAllTogether(List<Sound> sounds) {
        ArrayList<MediaPlayer> mediaPlayers = new ArrayList<>();
        for( Sound sound : sounds){
            final MediaPlayer player = MediaPlayer.create(mContext, sound.getAudioID());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.release();
                }
            });
            mediaPlayers.add(player);
        }

        for (MediaPlayer player : mediaPlayers) {
            player.start();
        }
    }

    public boolean isNeverInterrupt() {
        return neverInterrupt;
    }

    public void setNeverInterrupt(boolean neverInterrupt) {
        this.neverInterrupt = neverInterrupt;
    }
}
