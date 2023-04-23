package com.example.hogotest

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment


class TestFragmentActivity :AppCompatActivity(){

    var fragment1 = TestFragment()
    var fragment2 = TestFragment()
   lateinit var  root : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Log.d("ghghgh", "savedInstanceState != null")
        }
        root = FrameLayout(this,null)
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        root.layoutParams = layoutParams
        root.id = R.id.fragment_container
        setContentView(root)
        supportFragmentManager.beginTransaction().add(root.id,fragment1).commit()
        supportFragmentManager.beginTransaction().add(root.id,fragment2).commit()
        supportFragmentManager.beginTransaction().hide(fragment2).commit()

    }
}


class TestFragment :Fragment(){



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext(),null).apply {
            setContent {
                Text(text =Math.random().toString())
            }
        }
    }

}