package com.codido.nymeria.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.AddMyCardReqData;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.SendSmsCodeReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.SendSmsCodeResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加银行卡最后获取短信验证码的页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class SmsCodeActivity extends BaseActivity {


    public static final int CALL_GET_SMS_CODE_SUCCESS = FIRST_VAL++;
    public static final int CALL_GET_SMS_CODE_FAILD = FIRST_VAL++;
    /**
     * 真实姓名
     */
    String realName;
    String idCard;
    String bankCard;
    String mobile;
    String serialCode;
    String smsCode;
    String expireDate;
    String cvv2;


    @BindView(R.id.textViewSmsCodeTip)
    TextView textViewSmsCodeTip;

    @BindView(R.id.editTextSmsCode)
    EditText editTextSmsCode;

    @BindView(R.id.textViewGetSmsCode)
    TextView textViewGetSmsCode;

    public static void start(Activity content,
                             String realName, String idCard, String bankCard, String mobile, String serialCode, String expireDate,
                             String cvv2) {

        Intent intent = new Intent(content, SmsCodeActivity.class);
        intent.putExtra("realName", realName);
        intent.putExtra("idCard", idCard);
        intent.putExtra("bankCard", bankCard);
        intent.putExtra("mobile", mobile);
        intent.putExtra("serialCode", serialCode);
        intent.putExtra("expireDate", expireDate);
        intent.putExtra("cvv2", cvv2);

        content.startActivityForResult(intent, REQ_FOR_ADD_CRAD);
    }


    @OnClick(R.id.buttonNext)
    void next() {
        ValidateUtils.Result result;

        result = ValidateUtils.checkSerialCode(smsCode = getTextFromEditText(editTextSmsCode));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }
        showProgressDialogCanCancel("请稍后...", 0);

        AddMyCardReqData reqData = new AddMyCardReqData();
        reqData.setCardNo(bankCard);
        reqData.setUserName(realName);
        reqData.setIdNo(idCard);
        reqData.setMobile(mobile);
        reqData.setSerialCode(serialCode);
        reqData.setSmsCode(smsCode);
        reqData.setExpireDate(expireDate);
        reqData.setCvv2(cvv2);

        BaseReq baseReq = new BaseReq(Global.key_addMyCard, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sms_code;
    }

    @Override
    public void addAction() {

        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        this.realName = getIntent().getStringExtra("realName");
        this.idCard = getIntent().getStringExtra("idCard");
        this.bankCard = getIntent().getStringExtra("bankCard");
        this.mobile = getIntent().getStringExtra("mobile");
        this.serialCode = getIntent().getStringExtra("serialCode");
        this.expireDate = getIntent().getStringExtra("expireDate");
        this.cvv2 = getIntent().getStringExtra("cvv2");

        textViewSmsCodeTip.setText("系统已下发短信到" + mobile + "，请在收到短信验证码后，输入验证码完成绑定");

        new Thread(getRunable()).start();
    }


    @OnClick(R.id.textViewGetSmsCode)
    void sendSmsCode() {

        showProgressDialogCanCancel("请稍后...", 0);

        SendSmsCodeReqData reqData = new SendSmsCodeReqData();
        reqData.setCardNo(bankCard);
        reqData.setMobile(mobile);
        //TODO 接口有修改，如果用户没有实名认证，下发短信验证码的时候就需要增加userName和idNo
        reqData.setUserName("");
        reqData.setIdNo("");

        BaseReq baseReq = new BaseReq(Global.key_sendSmsCode, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    public static final int CALL_TIME_COUNT = FIRST_VAL++;

    private int time;
    private boolean isRun;

    /**
     * 获取计时器线程，在获得验证码成功之后，开始计时操作
     *
     * @return
     */
    public Runnable getRunable() {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // 从180s开始计时
                time = 180;
                // 开始运行标志置为true
                isRun = true;
                // 如果计时时间结束或者页面停止运行，该线程被终止
                while (time >= 0 && isRun) {
                    // 通知页面
                    runCallFunctionInHandler(CALL_TIME_COUNT);
                    // 计时秒数减一
                    time--;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return runnable;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    /**
     * 根据计时的时间，更改页面的显示
     */
    private void timeCount() {
        if (time <= 0) {
            // 如果计时器计时结束，重新将界面状态置为可以重新获取验证码的状态
            textViewGetSmsCode.setText("获取短信验证码");
            textViewGetSmsCode.setEnabled(true);
        } else if (time > 0) {
            // 计时器在计时，但未停止计时，修改界面展示，展示还有多少秒可以重新获取验证码
            textViewGetSmsCode.setText(time + "秒后重新获取");
            textViewGetSmsCode.setEnabled(false);
        }
    }


    @Override
    public boolean onDone(BaseResp responseBean) {

        if (Global.key_addMyCard.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                if (responseBean != null) {

                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        } else if (Global.key_sendSmsCode.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                if (responseBean != null) {

                    runCallFunctionInHandler(CALL_GET_SMS_CODE_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_GET_SMS_CODE_FAILD, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {

            cancelProgressDialog();

            showToastText("添加银行卡成功");

            setResult(RESULT_OK);
            finish();

        } else if (CALL_QUERY_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        } else if (CALL_GET_SMS_CODE_SUCCESS == callID) {

            cancelProgressDialog();

            SendSmsCodeResp resp = (SendSmsCodeResp) args[0];
            serialCode = resp.getSerialCode();


            new Thread(getRunable()).start();

        } else if (CALL_GET_SMS_CODE_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        }

        // 计时器通知
        else if (callID == CALL_TIME_COUNT) {
            timeCount();
        }
    }


}
