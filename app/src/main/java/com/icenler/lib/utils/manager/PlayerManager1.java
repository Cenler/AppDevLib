package com.icenler.lib.utils.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import com.icenler.lib.base.BaseApplication;

import java.io.IOException;
/**
 * Created by Administrator on 2015/8/27 0027.
 */
public class PlayerManager1 {
    private static PlayerManager1 playerManager;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private PlayCallback callback;
    private Context context;
    public static PlayerManager1 getManager(Context context){
        if (playerManager == null){
            synchronized (PlayerManager.class){
                playerManager = new PlayerManager1(context);
            }
        }
        return playerManager;
    }
    private PlayerManager1(Context context) {
        this.context = BaseApplication.getInstance();
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
    //    private AudioManager.OnAudioFocusChangeListener changeListener = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
//                mediaPlayer.start();
//            }
//        }
//    };
//
//    private int requestAudioFocus(){
//        return audioManager.requestAudioFocus(changeListener, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//    }
//
//    private int abandonAudioFocus(){
//        return audioManager.abandonAudioFocus(changeListener);
//    }
    public interface PlayCallback{
        void onPrepared();
        void onComplete();
        void onStop();
    }
    private String filePath;
    public void play(String path, final PlayCallback callback){
        this.filePath = path;
        this.callback = callback;
        if (isWiredHeadsetOn()){
            changeToEarphoneForEarphone();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(path));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    if (requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                        mediaPlayer.start();
//                    }
                    callback.onPrepared();
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    abandonAudioFocus();
                    changeToSpeaker();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeToEarphoneForEarphone(){
        audioManager.setSpeakerphoneOn(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//        } else {
//            audioManager.setMode(AudioManager.MODE_IN_CALL);
//        }
    }
    public boolean isWiredHeadsetOn(){
        return audioManager.isWiredHeadsetOn();
    }
    public boolean isInEarphoneMode(){
        return audioManager.isWiredHeadsetOn();
    }
    public void changeToEarphoneForSensor(){
        if (isPlaying()){
            stop();
            audioManager.setSpeakerphoneOn(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
            play(filePath, callback);
        } else {
            audioManager.setSpeakerphoneOn(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }
    }
    public void changeToSpeaker(){
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }
    public void raiseVolume(){
        /*int mode = audioManager.getMode();
        if (mode == AudioManager.MODE_IN_CALL || mode == AudioManager.MODE_IN_COMMUNICATION){
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        } else if (mode == AudioManager.MODE_NORMAL){
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        }*/
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }
    public void lowerVolume(){
        /*int mode = audioManager.getMode();
        if (mode == AudioManager.MODE_IN_CALL || mode == AudioManager.MODE_IN_COMMUNICATION){
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        } else if (mode == AudioManager.MODE_NORMAL){
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        }*/
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
    }
    public void stop(){
        if (isPlaying()){
            try {
                mediaPlayer.stop();
                callback.onStop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isPlaying(){
        if(mediaPlayer == null){
            return false;
        }
        return mediaPlayer.isPlaying();
    }
}
