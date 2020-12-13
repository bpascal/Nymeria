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

        BaseReq baseReq = new BaseReq(Global.key_checkUpdate, reqData);


        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @BindView(R.id.textViewVersion)
    TextView textViewVersion;


    @OnClick(R.id.linearLayoutKf)
    void kfMobile() {

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

        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {

            cancelProgressDialog();




        } else if (CALL_QUERY_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        }
    }


}
