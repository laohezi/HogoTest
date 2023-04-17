package com.example.aldlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class PlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return new MyPlayer();
    }

    class MyPlayer extends IPlayer.Stub {

        @Override
        public String play() throws RemoteException {
            return "我是来自remote的play";
        }

        @Override
        public String pause() throws RemoteException {
            return "我是来自remote的pause";
        }
    }
}


