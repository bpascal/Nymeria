package com.codido.nymeria.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.ChangePwdBySmsCodeReqData;
import com.codido.nymeria.bean.req.GetSmsCodeReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.GetSmsCodeResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.MD5Util;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;
import com.codido.nymeria.util.YUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.codido.nymeria.activity.SmsCodeActivity.CALL_GET_SMS_CODE_FAILD;
import static com.codido.nymeria.activity.SmsCodeActivity.CALL_GET_SMS_CODE_SUCCESS;
import static com.codido.nymeria.util.Global.PASSWROD_TYPE_KEY;
import static com.codido.nymeria.util.Global.PASSWROD_TYPE_LOGIN;
import static com.codido.nymeria.util.Global.PASSWROD_TYPE_PAY;

/**
 * 修改密码
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class ChangePwdBySmsCodeActivity extends BaseActivity {


    @BindView(R.id.textViewTitle)
    TextView textViewTitle;


    String passwordType;


    @BindView(R.id.editTextSmsCode)
    EditText editTextSmsCode;


    @BindView(R.id.editNewMobile)
    EditText editNewMobile;

    @BindView(R.id.editNewPassword)
    EditText editNewPassword;

    @BindView(R.id.editNewPassword2)
    EditText editNewPassword2;

    @BindView(R.id.textViewGetSmsCode)
    TextView textViewGetSmsCode;


    @OnClick(R.id.textViewGetSmsCode)
    void getSmsCode() {
        ValidateUtils.Result result;


        result = ValidateUtils.checkMblNo(mobile = getTextFromEditText(editNewMobile));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkPass(newPassword = getTextFromEditText(editNewPassword));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkBankCard(newPassword2 = getTextFromEditText(editNewPassword2));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        if (!newPassword.equals(newPassword2)) {
            showToastText("两次输入的新密码不一致");
            return;

        }

        showProgressDialogCanCancel("请稍后...", 0);


        GetSmsCodeReqData reqData = new GetSmsCodeReqData();
        reqData.setSmsType(GetSmsCodeReqData.TYPE_RESET_PASSWORD);
        reqData.setMobile(mobile);

        BaseReq baseReq = new BaseReq(Global.key_getSmsCode, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    String mobile;
    String serialCode;
    String newPassword;
    String newPassword2;
    String smsCode;

    @OnClick(R.id.buttonNext)
    void changePwd() {
        ValidateUtils.Result result;

        result = ValidateUtils.checkSerialCode(serialCode);

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkMblCode(smsCode = getTextFromEditText(editTextSmsCode));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkPass(newPassword = getTextFromEditText(editNewPassword));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkBankCard(newPassword2 = getTextFromEditText(editNewPassword2));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        if (!newPassword.equals(newPassword2)) {
            showToastText("两次输入的新密码不一致");
            return;

        }


        result = ValidateUtils.checkMblNo(mobile = getTextFromEditText(editNewMobile));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }


        showProgressDialogCanCancel("请稍后...", 0);

        ChangePwdBySmsCodeReqData reqData = new ChangePwdBySmsCodeReqData();
        reqData.setSerialCode(serialCode);
        reqData.setSmsCode(smsCode);
        reqData.setMobile(mobile);
        reqData.setNewPwd(MD5Util.md5(newPassword));
        reqData.setPwdType(passwordType);

        BaseReq baseReq = new BaseReq(Global.key_changePwdBySmsCode, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    public static void changeLoginPassword(Context context) {
        Intent intent = new Intent(context, ChangePwdBySmsCodeActivity.class);
        intent.putExtra(PASSWROD_TYPE_KEY, PASSWROD_TYPE_LOGIN);
        context.startActivity(intent);
    }

    public static void changePayPassword(Context context) {
        Intent intent = new Intent(context, ChangePwdBySmsCodeActivity.class);
        intent.putExtra(PASSWROD_TYPE_KEY, PASSWROD_TYPE_PAY);

        context.startActivity(intent);
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
    protected int getContentView() {
        return R.layout.activity_change_pwd_by_sms_code;
    }

    @Override
    public void addAction() {

        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        passwordType = getIntent().getStringExtra(PASSWROD_TYPE_KEY);
        if (passwordType == null) {
            passwordType = PASSWROD_TYPE_LOGIN;
        }


        if (passwordType.equals(PASSWROD_TYPE_LOGIN)) {
            textViewTitle.setText("重置登录密码");
        } else if (passwordType.equals(PASSWROD_TYPE_PAY)) {
            textViewTitle.setText("重置支付密码");
        }

        if (!YUtils.isEmpty(Global.mobile)){
            setTextToEditText(editNewMobile, Global.mobile);
        }
    }


    @Override
    public boolean onDone(BaseResp responseBean) {

        if (Global.key_changePwdBySmsCode.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {

                runCallFunctionInHandler(CALL_UPLOAD_SUCCESS, responseBean);

            } else {
                runCallFunctionInHandler(CALL_UPLOAD_FAILD, responseBean);
            }
        }

        if (Global.key_getSmsCode.equals(responseBean.getKey())) {
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
        if (CALL_UPLOAD_SUCCESS == callID) {

            cancelProgressDialog();
            showToastText("密码修改成功");
            finish();

        } else if (CALL_UPLOAD_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        } else if (CALL_GET_SMS_CODE_SUCCESS == callID) {

            cancelProgressDialog();
            serialCode = ((GetSmsCodeResp) args[0]).getSerialCode();

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


}
