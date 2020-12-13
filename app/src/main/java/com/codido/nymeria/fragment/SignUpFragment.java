package com.codido.nymeria.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.activity.MainActivity;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.YUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Junjie.Lai on 2017/1/17 12:06
 * 邮箱：laijj@yzmm365.cn
 */
public class SignUpFragment extends BaseFragment {

    /**
     * 时间倒计时处理
     */
    private static int CALL_TIME_COUNT = BaseActivity.FIRST_VAL++;

    /**
     * 登录成功跳转操作
     */
    private static final int CALL_SIGNUP_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 短信验证码序列号
     */
    private String serId;

    @BindView(R.id.editTextUserName)
    EditText editTextUserName;
    @BindView(R.id.editTextPasswords)
    EditText editTextPasswords;
    @BindView(R.id.editTextSmsCode)
    EditText editTextSmsCode;
    @BindView(R.id.textViewGetSmsCode)
    TextView textViewGetSmsCode;
    @BindView(R.id.buttonSignUp)
    Button buttonSignUp;

    /**
     * 获得验证码后，可以重新获取验证码的时间间隔，每秒钟递减一，计时总数为180秒，3分钟。该参数减为0之后，才能重新获取验证码
     */
    private int time = 180;

    /**
     * 页面是否正在运行标志，当isRun=false时，可以结束计时器线程。
     */
    private boolean isRun = false;

    @OnClick(R.id.textViewGetSmsCode)
    void textViewGetSmsCodeEvent() {
        String mobile = editTextUserName.getText().toString();
        if (YUtils.isEmpty(mobile)) {
            showToastText("请输入手机号");
            return;
        }
        showProgressDialog("正在下发短信验证码!");
        sendGetSmsCodeRequest(mobile);
    }

    @OnClick(R.id.buttonSignUp)
    void buttonSignUpEvent() {
        String mobile = editTextUserName.getText().toString();
        if (YUtils.isEmpty(mobile)) {
            showToastText("请输入手机号");
            return;
        }
        String passwords = editTextPasswords.getText().toString();
        if (YUtils.isEmpty(passwords)) {
            showToastText("请输入密码");
            return;
        }
        String smsCode = editTextSmsCode.getText().toString();
        if (YUtils.isEmpty(smsCode)) {
            showToastText("请输入验证码");
            return;
        }
        showProgressDialog("正在注册中，请稍后");
        sendRegisterRequest(mobile, smsCode, passwords, serId);
    }


    /**
     * 获取计时器线程，在获得验证码成功之后，开始计时操作
     *
     * @return
     */
    public Runnable getCountDownRunable() {

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
        return R.layout.fragment_signup;
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void call(int id, Object... args) {
        if (id == CALL_TIME_COUNT) {
            //倒数时间UI更新
            timeCountDown();
        } else if (id == CALL_SIGNUP_SUCCESS) {
            //注册成功
            gotoMainActivity();
        }
    }

    @Override
    protected boolean callBack(BaseResp responseBean) {
        if (Global.key_checkUpdate.equals(responseBean.getKey())) {
            cancelProgressDialog();
            if (responseBean.isOk()) {
                new Thread(getCountDownRunable()).start();
                showToastText("短信验证码正在发送");
            } else {
                showToastText(responseBean.getRespMsg());
            }
        }
        return false;
    }

    @Override
    public void onSelected() {
        editTextPasswords.setText("");
        editTextSmsCode.setText("");
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 根据计时的时间，更改页面的显示
     */
    private void timeCountDown() {
        if (time <= 0) {
            // 如果计时器计时结束，重新将界面状态置为可以重新获取验证码的状态
            textViewGetSmsCode.setText("获取短信验证码");
            textViewGetSmsCode.setEnabled(true);
            editTextUserName.setEnabled(true);
        } else if (time > 0) {
            // 计时器在计时，但未停止计时，修改界面展示，展示还有多少秒可以重新获取验证码
            textViewGetSmsCode.setText(time + "秒后重新获取");
            textViewGetSmsCode.setEnabled(false);
        }
    }

    /**
     * 发送获取验证码的请求
     */
    private void sendGetSmsCodeRequest(String mobile) {

    }

    /**
     * 注册请求
     */
    private void sendRegisterRequest(String mobile, String smsCode, String passwords, String serId) {

    }


    /**
     * 跳转成功页面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
