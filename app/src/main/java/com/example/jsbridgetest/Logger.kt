package com.example.jsbridgetest

import android.util.Log

class Logger {
    companion object{
        private const val TAG = "myTag"
        fun log(msg:String){
            Log.d(TAG, msg)
        }
    }
}