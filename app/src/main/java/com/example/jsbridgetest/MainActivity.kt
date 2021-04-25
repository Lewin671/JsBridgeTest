package com.example.jsbridgetest

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebSettings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.lzyzsd.jsbridge.BridgeWebView
import com.google.gson.Gson


@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private lateinit var mWebView: BridgeWebView
    private lateinit var mEditText: EditText
    private lateinit var mResetButton: Button
    private lateinit var mSendButton: Button
    private lateinit var mReceivedDataListener: OnReceivedDataListener

    companion object {
        const val WEB_URL = "file:///android_asset/web.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActivity()
    }

    /**
     * 初始化Activity
     */
    private fun initActivity() {
        mWebView = findViewById(R.id.webView)
        mEditText = findViewById(R.id.edit_view)
        mResetButton = findViewById(R.id.btn_reset)
        mSendButton = findViewById(R.id.btn_send)

        mSendButton.setOnClickListener {
            val data = mEditText.text.toString()
            // 直接调用nativeFunction方法向H5发送数据
            mWebView.loadUrl("javascript:nativeFunction('$data')")
        }

        mResetButton.setOnClickListener {
            mWebView.loadUrl(WEB_URL)
        }

        mReceivedDataListener = object : OnReceivedDataListener {
            override fun sendData(data: String?) {
                mEditText.setText("通过webView发送消息接收到数据:\n$data")
            }
        }

        initWebView()
    }

    /**
     * 初始化WebView
     */
    private fun initWebView() {
        // 辅助WebView设置处理页面跳转、页面请求操作
        mWebView.webViewClient = MyWebViewClient(this, mWebView)

        // Handler作为通信桥梁的作用，接受处理来自H5数据以及回传Native数据的处理
        // 当h5调用send发送消息的时候，调用MyHandlerCallBack
        mWebView.setDefaultHandler(MyHandlerCallBack(mReceivedDataListener))

        // WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等比等，不过它还能处理文件上传操作

        // 如果不加这一行，当点击界面链接，跳转到外部时，会出现net::ERR_CACHE_MISS错误
        // 需要在androidManifest.xml文件中声明联网权限
        // <uses-permission android:name="android.permission.INTERNET"/>
        mWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        // 加载网页地址
        mWebView.loadUrl(WEB_URL)

        //有方法名的都需要注册Handler后使用
        mWebView.registerHandler(
            "submitFromWeb"
        ) { data, function ->
            Logger.log("html返回数据为: $data")
            if (!TextUtils.isEmpty(data)) {
                mEditText.setText("通过Native方法接受数据: \n$data")
            }
            function?.onCallBack("Native已经接收到数据: $data, 请确认")
        }

        //应用启动后初始化数据调用，js处理方法connectWebViewJavascriptBridge(function(bridge)
        mWebView.callHandler("functionInJs", Gson().toJson(UserInfo("刘清影", "2017****"))) { data ->
            mEditText.setText("向h5发送初始化数据成功，接受h5返回值为$data")
        }

        //对应js中的bridge.init处理，此处需加CallBackFunction,如果只使用mWebView.send("")；会导致js中只收到通知，接收不到值
        mWebView.send("来自Java的消息") { data ->
            Toast.makeText(this, "bridge.init初始化成功: $data", Toast.LENGTH_LONG).show()
        }

    }


}