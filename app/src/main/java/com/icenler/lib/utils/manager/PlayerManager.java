package com.icenler.lib.utils.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import com.icenler.lib.feature.base.BaseApplication;

import java.io.IOException;

/**
 * Created by iCenler - 2015/10/8.
 * Description：音乐播放管理类(需要权限android.permission.MODIFY_AUDIO_SETTINGS)
 */
public class PlayerManager {

    private static PlayerManager playerManager;

    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private PlayCallback callback;

    private String filePath;

    public static PlayerManager getManager() {
        if (playerManager == null) {
            synchronized (PlayerManager.class) {
                playerManager = new PlayerManager();
            }
        }

        return playerManager;
    }

    private PlayerManager() {
        this.context = BaseApplication.getInstance();
        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 播放回调接口
     */
    public interface PlayCallback {
        /**
         * 音乐准备完毕
         */
        void onPrepared();

        /**
         * 音乐播放完成
         */
        void onComplete();

        /**
         * 音乐停止播放
         */
        void onStop();
    }

    /**
     * 播放音乐
     *
     * @param path     音乐文件路径
     * @param callback 播放回调函数
     */
    public void play(String path, final PlayCallback callback) {
        this.filePath = path;
        this.callback = callback;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(path));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    callback.onPrepared();
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (isPlaying()) {
            try {
                mediaPlayer.stop();
                callback.onStop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否正在播放
     *
     * @return 正在播放返回true, 否则返回false
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 切换到外放
     */
    public void changeToSpeaker() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到耳机模式
     */
    public void changeToHeadset() {
        audioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到听筒
     */
    public void changeToReceiver() {
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 提高音量
     */
    public void raiseVolume() {
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

    /**
     * 降低音量
     */
    public void lowerVolume() {
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

    /**
     * 耳机插入拔出广播接收器
     */
    class HeadsetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //插入和拔出耳机会触发此广播
                case Intent.ACTION_HEADSET_PLUG:
                    int state = intent.getIntExtra("state", 0);
                    if (state == 1) {
                        playerManager.changeToHeadset();
                    } else if (state == 0) {
                        playerManager.changeToSpeaker();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
