package com.example.jsbridgetest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import com.github.lzyzsd.jsbridge.BridgeWebView
import com.github.lzyzsd.jsbridge.BridgeWebViewClient
import java.net.URLDecoder
import java.util.*

class MyWebViewClient(private val mContext: Context, webView: BridgeWebView?) :
    BridgeWebViewClient(webView) {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url == null) return false
        Logger.log("url地址为: $url")
        var mUrl: String = url
        try {
            mUrl = URLDecoder.decode(url, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //默认操作url地址yy://__QUEUE_MESSAGE__/
        if (mUrl.trim().startsWith("yy:")) {
            return super.shouldOverrideUrlLoading(view, url)
        }

        //特殊情况tel，调用系统的拨号软件拨号【<a href="tel:110">拨打电话110</a>】
        if (url.trim().startsWith("tel")) {
            val intent = Intent(Intent.ACTION_VIEW);
            intent.data = Uri.parse(mUrl)
            mContext.startActivity(intent);
        } else {
            //特殊情况【调用系统浏览器打开】<a href="https://www.csdn.net">调用系统浏览器</a>
            if (url.toLowerCase(Locale.ROOT).contains("csdn")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            } else {
                //其它非特殊情况全部放行
                view?.loadUrl(url)
            }
        }
        return true
    }
}