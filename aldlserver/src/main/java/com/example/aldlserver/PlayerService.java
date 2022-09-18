package com.example.aldlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class PlayerService  extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyPlayer  extends   IPlayer.Stub{

        @Override
        public void play() throws RemoteException {
            Log.d("AIDLTEST","player start to play");

        }

        @Override
        public void pause() throws RemoteException {
            Log.d("AIDLTEST","player start to play");

        }


    }
}
