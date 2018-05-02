package com.codido.nymeria.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.YUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;

/**
 * 内嵌浏览器对象
 *
 * @author liu_kf
 */
public class WebActivity extends BaseActivity implements DownloadListener {


    /**
     * 页面数量
     */
    private int loadedPageCount = 0;


    /**
     * URL地址记录栈
     */
    private Stack<String> urls = new Stack<String>();

    /**
     * URL地址和标题记录
     */
    private Map<String, String> maps = new HashMap<String, String>();

    /**
     * 内嵌浏览器组件
     */
    @BindView(R.id.webView)
    WebView webView;

    /**
     * 分享按钮
     */
    @BindView(R.id.viewShare)
    View viewShare;
    /**
     * 关闭按钮
     */
    @BindView(R.id.viewClose)
    View viewClose;

    /**
     * 页面标题显示组件
     */
    @BindView(R.id.title_bar_text)
    TextView titleBar;

    /**
     * 当前的页面展示的URL
     */
    private String url;

    /**
     * 页面标题
     */
    private String title;

    /**
     * 页面html内容，用于加载本地的页面
     */
    private String body;

    /**
     * 页面是否正在加载标志位
     */
    private boolean isLoading;

    /**
     * 浏览器设置
     */
    WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!YUtils.isEmpty(title)) {
                titleBar.setText(title);

                maps.put(view.getUrl(), title);
            }
        }
    };

    /**
     * 浏览器设置
     */
    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            isLoading = false;

            title = maps.get(url);
            if (!YUtils.isEmpty(title)) {
                setTitle(title);
            }
            super.onPageFinished(view, url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            viewShare.setVisibility(View.GONE);

            if (!YUtils.isEmpty(url)
                    && (url.indexOf("www.") == 0 || url.indexOf("http:") == 0 || url
                    .indexOf("https:") == 0)) {
                loadUrl(url);
                return true;
            } else {
                Global.debug("外部地址，调用外部浏览器打开：" + url);
                openUrl(url);
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebActivity.this.url = url;
            isLoading = true;
            super.onPageStarted(view, url, favicon);
        }
    };

    /**
     * 启动一个web活动页面
     *
     * @param context
     * @param url
     */
    public static void start(Context context, String url, long topicId, int replyNum) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("topicId", topicId);
        intent.putExtra("replyNum", replyNum);
        context.startActivity(intent);
    }

    /**
     * 启动一个web页面
     *
     * @param context
     * @param url
     */
    public static void start(Context context, String url) {
        start(context, null, url);
    }

    /**
     * 启动一个web页面
     *
     * @param context
     * @param url
     */
    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 通知JS，页面关闭，页面收到此通知可以去页面中的音乐，否则会导致音乐不能正常的关闭
        webView.loadUrl("javascript:notifyPageClose()");
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        return false;
    }

    @Override
    public void call(int callID, Object... args) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_web;
    }

    @Override
    public void addAction() {
        addBackAction();
        // 配置下载处理器
        webView.setDownloadListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        isLoading = false;

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(chromeClient);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " TravellerApp");

        addJsMethod();

        url = getIntent().getStringExtra("url");

        if (url != null) {
            // 如果连接不是以http开头的 ，则在连接前面加上http
            if (url.toLowerCase().startsWith("www.")) {
                url = "http://" + url;
            }
            loadUrl(url);
        } else if ((body = getIntent().getStringExtra("body")) != null) {
            String encoding = "UTF-8";
            webView.getSettings().setDefaultTextEncodingName(encoding);
            webView.loadDataWithBaseURL(null, body, "text/html", encoding, null);
            Global.debug("字符集，" + encoding);
            Global.debug("data=" + body);
        } else {
            finish();
            return;
        }
        title = getIntent().getStringExtra("title");
        if (title != null) {
            titleBar.setText(title);
        }
    }

    /**
     * 读取url
     *
     * @param url
     */
    public void loadUrl(String url) {
        if (++loadedPageCount > 1) {
            viewClose.setVisibility(View.VISIBLE);
        }

        Map<String, String> additionalHttpHeaders = new HashMap<String, String>();
        webView.loadUrl(url);
    }

    /**
     * 返回按钮事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (back()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回事件
     *
     * @return
     */
    public boolean back() {
        if (isLoading) {
            isLoading = false;
            webView.stopLoading();
        }
        if (webView.canGoBack()) {
            // 返回之前
            viewShare.setVisibility(View.GONE);
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return false;
    }

    /**
     * 添加js事件
     */
    public void addJsMethod() {
        webView.addJavascriptInterface(new TravellerObject(), "TravellerObject");
    }

    /**
     * js返回处理对象
     */
    private class TravellerObject {

    }



    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        //外部浏览器打开下载
        openUrl(url);
    }

    /**
     * 打开
     *
     * @param context
     * @param title
     * @param url
     * @param i
     */
    public static void startForResult(Activity context, String title, String url, int i) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, i);
    }

    public static void startForResult(Activity context, String url, int i) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivityForResult(intent, i);
    }

}
