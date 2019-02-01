package com.codido.nymeria.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class AboutActivity extends BaseActivity {




    @BindView(R.id.textViewKFEmail)
    TextView textViewKFEmail;

    @BindView(R.id.textViewKFWeiXin)
    TextView textViewKFWeiXin;


    @BindView(R.id.textViewKFTime)
    TextView textViewKFTime;

    /**
     * 查询客服信息
     */
    private void queryKfInfo() {

        BaseReqData reqData = new BaseReqData();

        BaseReq baseReq = new BaseReq(Global.key_queryKfInfo, reqData);


        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @BindView(R.id.textViewVersion)
    TextView textViewVersion;


    @OnClick(R.id.linearLayoutKf)
    void kfMobile() {


        if (resp != null && resp.getKfPhoneList() != null && resp.getKfPhoneList().size()>0) {

            KfDialogDialog kfDialogDialog = new KfDialogDialog(this, resp.getKfPhoneList() );
            kfDialogDialog.show();

        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void addAction() {

        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        textViewVersion.setText(Global.version);

        queryKfInfo();
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryKfInfo.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {

                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);

            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        }
        return false;
    }

    QueryKfInfoResp resp;

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {

            cancelProgressDialog();

            resp = (QueryKfInfoResp) args[0];

            textViewKFEmail.setText(resp.getKfEmail());
            textViewKFWeiXin.setText(resp.getKfWeixin());
            textViewKFTime.setText(resp.getServiceTime());



        } else if (CALL_QUERY_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        }
    }


}
