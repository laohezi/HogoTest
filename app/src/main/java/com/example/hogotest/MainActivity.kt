package com.example.hogotest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.ui.tooling.preview.Preview
import com.blankj.utilcode.util.CloneUtils
import com.blankj.utilcode.util.IntentUtils
import io.flutter.view.FlutterView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    @Preview
    @Composable
    fun getText(){

        return ConstraintLayout(
         constraintSet = ConstraintSet {

         }

        ) {


          Text(text = "lalala",
              modifier = Modifier.testTag("text1")
          )

        }
    }


}





class  CustomView(context: Context,attributeSet: AttributeSet?):LinearLayout(context,attributeSet){
    val TAG = this::class.java.canonicalName
    lateinit var  text1: TextView
    lateinit var  text2: TextView
    lateinit var  text3: TextView
    lateinit var  text4: TextView
    init {
        setBackgroundColor(Color.LTGRAY)
        text1 = TextView(context)
        text1.setBackgroundColor(Color.BLUE)
        text2 = TextView(context)
        text3 = TextView(context)
        text4 = TextView(context)
        val layoutParams1 = LayoutParams(500,500)
        text1.layoutParams = layoutParams1

        //setTextView(text1,"text1")
        GlobalScope.launch {
            val start = System.currentTimeMillis()
            val data = async { getData1fromBg() }
            val data2 = async { getData1fromBg() }
            text1.text = data.await() + data2.await()
            Log.d(TAG, "spend time:${System.currentTimeMillis() - start} ")

        }
        setTextView(text2,"text2")
        setTextView(text3,"text3")
        setTextView(text4,"text4")
        text1.viewTreeObserver.addOnGlobalLayoutListener {
            Log.d(TAG, "onGlobal: measured Width = ${text1.measuredWidth} \n width is ${text1.width}")
        }
        val layoutParams = LinearLayout.LayoutParams(-1,-1)
        layoutParams.width = 300
        layoutParams.height = 300
        addView(text1)
       /* addView(text1,layoutParams)
        addView(text2,layoutParams)
        addView(text3,layoutParams)
        addView(text4,layoutParams)*/
        post {
            Log.d(TAG, "run post: ")
            requestLayout()
        }

        setLayoutParams(LayoutParams(800,800))
    }


    private fun setTextView(textView: TextView,text:String){
        textView.setBackgroundColor(Color.DKGRAY)
        textView.text = text
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.d(TAG, "onMeasure: measuredWidth = ${text1.measuredWidth} \n width is ${text1.width}")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "beforeLayout: measuredWidth = ${text1.measuredWidth} \n" +
                " width is ${text1.width}")
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "afterLayout: measuredWidth = ${text1.measuredWidth} \n" +
                " width is ${text1.width}")
        val span = (width - text1.measuredWidth*childCount)/(childCount+1)

        var lastViewR = 0
        for (i in 0 until  childCount){
            val child = getChildAt(i)
            child.layout(lastViewR+span,0,lastViewR+span+child.measuredWidth,child.measuredHeight)
            lastViewR += span + child.width
        }


    }

}

suspend fun  getDatafromBg(): String {
   return withContext(Dispatchers.IO){
        delay(1000)
        return@withContext " I am from worker"
    }
}

suspend fun  getData1fromBg(): String {
    return withContext(Dispatchers.IO){
        delay(1500)
        return@withContext " I am from worker"
    }
}