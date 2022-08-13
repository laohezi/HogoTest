package com.example.hogotest.font

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.hogotest.databinding.ActivityFontTestBinding

class FontTestActivity :AppCompatActivity(){
    lateinit var  binding :ActivityFontTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFontTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        with(binding.tv){



        }
        with(binding.bt){
            setOnClickListener {
                binding.tv.invalidate()
            }
        }
    }
}


class  MyTextView(context: Context,attributeSet: AttributeSet) : View(context,attributeSet){

    val textPaint = Paint()
    val typeface = Typeface.createFromAsset(context.assets,"Madina-Clean.otf")
    val rectPaint = Paint()
    val string = "t"

    init {
        textPaint.setTypeface(typeface)
        textPaint.textSize = 100f
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 1f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(string,100f,100f,textPaint)
        val bounds = Rect()
        textPaint.getTextBounds(string,0,1,bounds)
        canvas.drawRect(bounds,rectPaint)


        super.onDraw(canvas)
    }
}

