package com.example.hogotest.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.hogotest.R
class RecyclerViewTestActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_recyclerview)
        initRv()
    }


    private fun initRv() {
        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = object :LinearLayoutManager(this){
            override fun calculateExtraLayoutSpace(
                state: RecyclerView.State,
                extraLayoutSpace: IntArray
            ) {
                super.calculateExtraLayoutSpace(state, extraLayoutSpace)
            }
        }

        recyclerView.adapter = outAdapter



    }


    val outAdapter = object : RecyclerView.Adapter<ViewHolder>() {
        val datas = mockdata

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return OutViewHolder(OutAdapterView(parent.context, null))
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val outAdapterView = holder.itemView as OutAdapterView
            outAdapterView.title.setText(datas[position].title)
            val iv = outAdapterView.recyclerView
            iv.layoutManager = LinearLayoutManager(outAdapterView.context)
            val adapter = InnerAdapter()
            adapter.datas = datas[position].list
            iv.adapter = adapter
            iv.requestDisallowInterceptTouchEvent(true)

        }

    }

    class OutViewHolder(view: OutAdapterView) : ViewHolder(view) {}


    class InnerAdapter : RecyclerView.Adapter<ViewHolder>() {
        lateinit var datas: List<String>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return InnerViewHolder(TextView(parent.context, null))
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val textview = holder.itemView as TextView
            textview.text = datas[position]
        }
    }

    class InnerViewHolder(view: TextView) : ViewHolder(view) {}


}




class OutAdapterView(context: Context, attributeSet: AttributeSet?) :
    LinearLayout(context, attributeSet) {

    lateinit var title: TextView
    lateinit var recyclerView: RecyclerView

    init {

        this.apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,200
            )
        }
        title = TextView(context)


        recyclerView = InnerRecyclerView(context).apply {
          layoutParams = LayoutParams(
             ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
        }

        recyclerView.id = R.id.rv

        this.removeAllViews()
        this.addView(title)
        this.addView(recyclerView)

    }

}


data class InnerData(val title: String, val list: List<String>)


val mockdata = arrayListOf<InnerData>(
    InnerData(
        "haha", arrayListOf(
            "huhu"
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu"
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu"
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu", "huhu", "lulu", "dudududud","huhuhuhu"
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu", "huhu", "lulu", "dudududud","huhuhuhu"
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu", "huhu", "lulu", "dudududud","huhuhuhu"
        )
    ),
    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),

    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),
    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    ),
    InnerData(
        "haha", arrayListOf(
            "huhu", "huhu",
        )
    )
)

class ParentRecyclerView(context: Context,attributeSet: AttributeSet?) :RecyclerView(context,attributeSet){

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }
}

class InnerRecyclerView(context: Context,attributeSet: AttributeSet? = null) :RecyclerView(context,attributeSet){
   var scrollDown = true
    init {

    }

    var lastY = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("jijiji",this.toString() +ev.toString())

        if (ev.y<lastY){
            if (canScrollVertically(1)){

                requestDisallowInterceptTouchEvent(true)
            }else{

                requestDisallowInterceptTouchEvent(false)
                Log.d("jijiji","$this requestDisallowInterceptTouchEvent--false")
            }
        }else{
            if (canScrollVertically(-1)){
                requestDisallowInterceptTouchEvent(true)
            }else{
                requestDisallowInterceptTouchEvent(false)
                Log.d("jijiji","$this requestDisallowInterceptTouchEvent--false")
            }
        }

        lastY = ev.y
        ev.getX()
        ev.pointerCount



        return super.dispatchTouchEvent(ev)

    }
}




