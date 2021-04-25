package com.example.jsbridgetest

import android.text.TextUtils
import com.github.lzyzsd.jsbridge.BridgeHandler
import com.github.lzyzsd.jsbridge.CallBackFunction

class MyHandlerCallBack(private val mReceivedDataListener:OnReceivedDataListener?) : BridgeHandler {
    override fun handler(data: String?, function: CallBackFunction?) {
        Logger.log("接收数据为: $data")
        if(!TextUtils.isEmpty(data) && mReceivedDataListener != null){
            mReceivedDataListener.sendData(data)
        }
        function?.onCallBack("Native已经收到消息")
    }
}

public interface OnReceivedDataListener{
    fun sendData(data:String?)
}
