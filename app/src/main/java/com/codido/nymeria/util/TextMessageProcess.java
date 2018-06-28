package com.codido.nymeria.util;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.codido.nymeria.bean.parser.MsgParser;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.resp.BaseResp;

/**
 * 接口处理线程
 */
public class TextMessageProcess implements Runnable {
    private Context context;
    private BaseReq req;
    private ProcessListener processListener;
    boolean isCanceled = false;

    public TextMessageProcess(Context context, BaseReq req, ProcessListener processListener) {
        super();
        this.context = context;
        this.req = req;
        this.processListener = processListener;
    }

    NetworkUtils manager;

    public void cancel() {
        if (manager != null) {
            manager.cancel();
        }
        isCanceled = true;
    }

    /**
     * 运行方法
     */
    public void run() {
        manager = new NetworkUtils(context);
        // String reqStr = Utils.getReqStrFromGetMethods(req.getReqData());
        String reqStr = JSON.toJSONString(req.getReqData());

        String url = Global.getAddressByKey(req.getKey());
        String resp = manager.SendAndWaitResponse(reqStr, url, req.getEncoding(), req.isLog());

        if (processListener == null) {
            Global.debugNetLog("processListener can not be null");
            return;
        }

        //处理响应部分
        BaseResp respBean = null;

        if (resp == null) {
            //响应对象为空则认为是网络错误或者服务器内部错误
            respBean = new BaseResp();
            respBean.setKey(req.getKey());
            respBean.setRespCode("NetErr");
            processListener.onDone(respBean);
            return;
        }

        if (isCanceled) {
            return;
        }

        //做对象解析
        try {
            respBean = MsgParser.parser(req.getKey(), resp);
            respBean.setKey(req.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            Global.debug("解析异常");
        }


        if (respBean == null) {
            //对象为空说明解析异常
            respBean = new BaseResp();
            respBean.setKey(req.getKey());
            respBean.setRespCode("PaserErr");
            processListener.onDone(respBean);
            return;
        }

        if (respBean.isSidError()) {
            //如果sid错误，则说明是登录异常，可统一做登录错误处理
            Intent intent = new Intent(Global.ACTION_RECEIVE_LOGIN_OUT);
            intent.putExtra("errMsg", respBean.getRespMsg());
            context.sendBroadcast(intent);
            return;
        }

        if (!isCanceled) {
            //响应正常，给每个传入的processListener做处理
            processListener.onDone(respBean);
        }
    }

}
