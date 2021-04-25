package com.example.jsbridgetest

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient

//自定义 WebChromeClient 辅助WebView处理图片上传操作【<input type=file> 文件上传标签,点击会自动调用】
class MyWebChromeClient : WebChromeClient() {
     public fun openFileChooser(uploadMsg:ValueCallback<Uri>){
         Logger.log("openFileChoose(ValueCallback<Uri> uploadMsg)")

     }
}