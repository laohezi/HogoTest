package com.example.hogotest.font

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.hogotest.databinding.ActivityFontTestBinding

class FontTestActivity :AppCompatActivity(){
    lateinit var  binding : ActivityFontTestBinding
    val string = "t"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFontTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        with(binding.tv){
            setText(string)
            textSize = 100f
            typeface = Typeface.createFromAsset(context.assets,"Madina-Clean.otf")

        }
        with(binding.bt){
            setOnClickListener {
                binding.tv.invalidate()
            }
        }
    }
}


class  MyTextView(context: Context,attributeSet: AttributeSet) : AppCompatTextView(context,attributeSet){

    val textPaint = Paint()
   // val typeface = Typeface.createFromAsset(context.assets,"Madina-Clean.otf")
    val rectPaint = Paint()
    val string = "t"

    init {
        textPaint.setTypeface(typeface)
        textPaint.textSize = 500f
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 1f
        setText(string)
        textSize = 100f

        typeface = Typeface.createFromAsset(context.assets,"Madina-Clean.otf")

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
       /* val bounds = Rect()
        textPaint.getTextBounds(string,0,string.length,bounds)
        val rect = Rect(bounds.left+100,bounds.top+100,bounds.right+100,bounds.bottom+100)
        canvas.drawRect(rect,rectPaint)*/

    }
}

