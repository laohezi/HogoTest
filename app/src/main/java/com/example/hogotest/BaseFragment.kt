package com.example.hogotest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/*open class BaseFragment :Fragment(){
    val lifecycleObserver = MyLifecycleObserver(this)
    override fun <I : Any?, O : Any?> prepareCall(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        Log.d("lala","ll")
    }

    override fun <I : Any?, O : Any?> prepareCall(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(lifecycleObserver)
        lifecycleObserver.onAttach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleObserver.onCreateView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lifecycleObserver.onActivityCreated()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        lifecycleObserver.onDestroyView()
        super.onDestroyView()
    }

    override fun onDetach() {
        lifecycleObserver.onDetach()
        super.onDetach()
    }








}*/

class  MyLifecycleObserver(val lifecycleOwner: LifecycleOwner) :LifecycleObserver{
    val TAG = "lifecycleOb"

    fun onAttach(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onAttach")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onCreate")
    }

    fun onCreateView(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onCreateView")
    }

    fun onActivityCreated(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onActivityCreated")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onStop")
    }


    fun onDestroyView(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onDestroyView")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onDestroy")

    }

    fun onDetach(){
        Log.d("$TAG--${lifecycleOwner.toString()}","onDetach")
    }







}