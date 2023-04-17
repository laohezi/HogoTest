package com.example.hogotest.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hogotest.R

class TouchEventTestActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.touch_event_test)
    }




}


class FrameLayout1(context: Context,attributeSet: AttributeSet):FrameLayout(context,attributeSet){
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("huhuhu","FrameLayout1 onTouchEvent--$event")
        return super.onTouchEvent(event)
    }
}

class FrameLayout2(context: Context,attributeSet: AttributeSet):FrameLayout(context,attributeSet){
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("huhuhu","FrameLayout2 onTouchEvent--$event")
        return super.onTouchEvent(event)
    }
}

class View1(context: Context,attributeSet: AttributeSet): View(context, attributeSet){
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("huhuhu","View onTouchEvent--$event")
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                return true
            }
            else->{
                return false
            }
        }
    }
}