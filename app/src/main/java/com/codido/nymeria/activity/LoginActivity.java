package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.LoginReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.LoginResp;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.Md5;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.YUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class LoginActivity extends BaseActivity {

    /**
     * 登录成功跳转操作
     */
    private static final int CALL_LOGIN_SUCCESS = BaseActivity.FIRST_VAL++;
    /**
     * 登录失败的处理
     */
    private static final int CALL_LOGIN_FAILED = BaseActivity.FIRST_VAL++;

    @BindView(R.id.editTextUserName)
    EditText editTextUserName;
    @BindView(R.id.editTextPasswords)
    EditText editTextPasswords;
    @BindView(R.id.textViewForgetPasswords)
    TextView textViewForgetPasswords;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;

    /**
     * 登录用户名
     */
    private String loginUserName;

    /**
     * 登录密码
     */
    private String loginPassword;

    @OnClick(R.id.buttonLogin)
    void loginButtonEvent() {
        loginUserName = editTextUserName.getText().toString();
        loginPassword = editTextPasswords.getText().toString();
        if (YUtils.isEmpty(loginUserName)) {
            showToastText("请输入用户名!");
            return;
        }
        if (YUtils.isEmpty(loginPassword)) {
            showToastText("请输入密码!");
            return;
        }
        sendLoginRequest(loginUserName, loginPassword);
    }

    @OnClick(R.id.textViewForgetPasswords)
    void textViewForgetPasswordsEvent() {
        ChangePwdBySmsCodeActivity.changeLoginPassword(this);

    }


    /**
     * 跳转成功页面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * 登录请求
     *
     * @param mobileNo
     * @param passwords
     */
    private void sendLoginRequest(String mobileNo, String passwords) {
        LoginReqData reqData = new LoginReqData();
        reqData.setMobile(mobileNo);
        String passwordsCode = Md5.MD5(passwords.getBytes());//做加密
        reqData.setPassword(passwordsCode);

        BaseReq baseReq = new BaseReq(Global.key_login, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void addAction() {

        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_login.equals(responseBean.getKey())) {
            //登录响应
            cancelProgressDialog();
            if (responseBean.isOk()) {
                LoginResp loginResp = (LoginResp) responseBean;
                Global.sid = loginResp.getSid();
                Global.userInfo = loginResp.getUserInfo();
                Global.mobile = loginUserName;

                DataKeeper.saveLoginData(this);
                runCallFunctionInHandler(CALL_LOGIN_SUCCESS);
            } else {
                runCallFunctionInHandler(CALL_LOGIN_FAILED, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (callID == CALL_LOGIN_SUCCESS) {
            //登录成功跳转到主页
            gotoMainActivity();
        } else if (callID == CALL_LOGIN_FAILED) {
            //登录失败
            BaseResp resp = (BaseResp) args[0];
            //showToastText(resp.getRespMsg());
            showToastText("登录失败，请检查用户名及密码");
        }
    }


}
