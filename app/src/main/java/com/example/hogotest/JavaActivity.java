package com.example.hogotest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;

public class JavaActivity extends AppCompatActivity {
    Long start = 0L;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        Log.d("handler","handle 0 at"+(System.currentTimeMillis()- start));
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        Log.d("handler","handle 1 at"+(System.currentTimeMillis()- start));
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.homescreen);
        if (!bitmap.isMutable()){
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        }
        Canvas canvas = new Canvas(bitmap);


    }
}
