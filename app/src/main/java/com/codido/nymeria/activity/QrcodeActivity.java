package com.codido.nymeria.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.GetShareParamResp;
import com.codido.nymeria.bean.resp.QueryKfInfoResp;
import com.codido.nymeria.util.Global;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 二维码页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class QrcodeActivity extends BaseActivity {

    /**
     * 微信分享api
     */
    private IWXAPI wxApi;

    /**
     * 微信分享参数公共常量
     */
    private GetShareParamResp shareParamResp;

    /**
     * 分享图片
     */
    private Bitmap thumb;

    @BindView(R.id.imageViewQrcode)
    ImageView imageViewQrcode;
    @BindView(R.id.buttonSend)
    Button buttonSend;

    @OnClick(R.id.buttonSend)
    void buttonSendEvent() {
        wechatShare(0, shareParamResp.getShareUrl(), shareParamResp.getShareTitle(), shareParamResp.getShareContent());
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void addAction() {
        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //实例化微信分享API
        wxApi = WXAPIFactory.createWXAPI(this, Global.WX_APP_ID);
        wxApi.registerApp(Global.WX_APP_ID);

        //获取传入参数
        Intent intent = getIntent();
        shareParamResp = (GetShareParamResp) intent.getSerializableExtra("shareParamResp");

        //显示图片
//        Glide.with(this).load(shareParamResp.getShareUrl()).dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(this.imageViewQrcode);

        new Thread(initImageThread).start();
    }

    /**
     * 获取图片线程
     */
    private Runnable initImageThread = new Runnable() {
        @Override
        public void run() {
            thumb = returnBitmap(shareParamResp.getShareImageUrl());
            runCallFunctionInHandler(CALL_QUERY_SUCCESS);
        }
    };


    @Override
    public boolean onDone(BaseResp responseBean) {

        return false;
    }

    QueryKfInfoResp resp;

    @Override
    public void call(int callID, Object... args) {
        if(callID == CALL_QUERY_SUCCESS){
            imageViewQrcode.setImageBitmap(thumb);
        }
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag, String url, String title, String content) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        //这里替换一张自己工程里的图片资源

        //Bitmap thumb = BitmapFactory.decodeResource(getResources(), shareParamResp.getShareImageUrl());
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

}
