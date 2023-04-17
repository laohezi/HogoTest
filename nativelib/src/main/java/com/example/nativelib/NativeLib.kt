package com.example.nativelib

import android.util.Log

class NativeLib {




    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun stringFromJNI2(): String
    external fun stringFromJNI3(): String
    external fun getSecondNumberFromJNI(array:Array<String>): String
    external fun errorInJava()


    fun kFunc(string:String){
        Log.d(LOGTAG,"call from JNI with $string")
    }

    companion object {
        val LOGTAG = javaClass.canonicalName
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }

        @JvmStatic
        fun stat(string: String){
            Log.d(LOGTAG,"call static from JNI with $string")
        }

    }
}

class NativeObject{
    var name = "huhuhaha"
    fun kFunc(){
        Log.d(NativeLib.LOGTAG,"call from JNI3 ")
    }
    fun kError(string:String){
        Log.d(NativeLib.LOGTAG,"call from kError with $string")
        throw Exception(string)
    }
}