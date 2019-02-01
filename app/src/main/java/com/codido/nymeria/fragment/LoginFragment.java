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
 * 作者：Junjie.Lai on 2017/1/17 11:27
 * 邮箱：laijj@yzmm365.cn
 */
public class LoginFragment extends BaseFragment {

    /**
     * 登录成功跳转操作
     */
    private static final int CALL_LOGIN_SUCCESS = BaseActivity.FIRST_VAL++;

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
    void textViewForgetPasswordsEvent(){
        ChangePwdBySmsCodeActivity.changeLoginPassword(getActivity());
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_login;
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void call(int id, Object... args) {
        if (CALL_LOGIN_SUCCESS == id) {
            //登录成功跳转
            gotoMainActivity();
        }
    }

    /**
     * 跳转成功页面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    protected boolean callBack(BaseResp responseBean) {
        if (Global.key_login.equals(responseBean.getKey())) {
            //登录
            if (responseBean.isOk()) {
                //成功
                LoginResp resp = (LoginResp) responseBean;
                // 保存sessionId
                Global.sid = resp.getSid();


                // 保存登录密码
                Global.loginPasswrod = loginPassword;
                // 保存登录用户名
                Global.loginUserName = loginUserName;

                // 保存最近登录信息
                saveLastLoginUserName(getActivity(), Global.loginUserName);

                // 将所有登录成功的数据缓存到本机
                DataKeeper.saveLoginData(getActivity());
                runCallFunctionInHandler(CALL_LOGIN_SUCCESS);
            } else {
                //失败
                showToastText(responseBean.getRespMsg());
            }
        }
        return false;
    }

    @Override
    public void onSelected() {
        editTextPasswords.setText("");
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 发送登录请求
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
        ProcessManager.getInstance().addProcess(getActivity(), baseReq, this);
    }
}
