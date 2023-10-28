package com.dxxy.note.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.dxxy.note.R;

public class MusicService extends Service {
    private MediaPlayer mp;

    public class MusicPlayBinder extends Binder{        //用作代理的内部类
        public void play(){                   //播放音乐
            if (mp == null) {
                mp = MediaPlayer.create(getApplicationContext(), R.raw.music);
            }
            if (!mp.isPlaying()) {
                mp.start();
                mp.setLooping(true);
            }
        }

        public void stop(){                   //停止播放音乐
            if (mp != null&&mp.isPlaying()){
                mp.pause();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return new MusicPlayBinder();                    //返回服务代理类
    }

    @Override
    public void onDestroy() {                //服务销毁时停止音乐播放
        if (mp != null){
            mp.stop();
            mp.release();
        }
        super.onDestroy();
    }
}
