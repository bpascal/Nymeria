package com.codido.nymeria.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.ChangePwdReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.MD5Util;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.codido.nymeria.util.Global.PASSWROD_TYPE_KEY;
import static com.codido.nymeria.util.Global.PASSWROD_TYPE_LOGIN;
import static com.codido.nymeria.util.Global.PASSWROD_TYPE_PAY;

/**
 * 修改密码
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class ChangePwdActivity extends BaseActivity {


    @BindView(R.id.textViewTitle)
    TextView textViewTitle;


    String passwordType;


    @BindView(R.id.editOldPassword)
    EditText editOldPassword;

    @BindView(R.id.editNewPassword)
    EditText editNewPassword;

    @BindView(R.id.editNewPassword2)
    EditText editNewPassword2;


    String oldPassword;
    String newPassword;
    String newPassword2;


    @OnClick(R.id.textViewForgetPasswords)
    void changePwdBySms() {

        if (passwordType.equals(PASSWROD_TYPE_LOGIN)) {
            ChangePwdBySmsCodeActivity.changeLoginPassword(this);
        } else if (passwordType.equals(PASSWROD_TYPE_PAY)) {
            ChangePwdBySmsCodeActivity.changePayPassword(this);
        }

    }




    @OnClick(R.id.buttonNext)
    void changePwd() {
        ValidateUtils.Result result;

        result = ValidateUtils.checkPass(oldPassword = getTextFromEditText(editOldPassword));

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

        ChangePwdReqData reqData = new ChangePwdReqData();
        reqData.setOldPwd(MD5Util.md5(oldPassword));
        reqData.setNewPwd(MD5Util.md5(newPassword));
        reqData.setPwdType(passwordType);

        BaseReq baseReq = new BaseReq(Global.key_changePwd, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    public static void changeLoginPassword(Context context) {
        Intent intent = new Intent(context, ChangePwdActivity.class);
        intent.putExtra(PASSWROD_TYPE_KEY, PASSWROD_TYPE_LOGIN);
        context.startActivity(intent);
    }

    public static void changePayPassword(Context context) {
        Intent intent = new Intent(context, ChangePwdActivity.class);
        intent.putExtra(PASSWROD_TYPE_KEY, PASSWROD_TYPE_PAY);
        context.startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_change_pwd;
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
            textViewTitle.setText("修改登录密码");
        } else if (passwordType.equals(PASSWROD_TYPE_PAY)) {
            textViewTitle.setText("修改支付密码");
        }
    }


    @Override
    public boolean onDone(BaseResp responseBean) {

        if (Global.key_changePwd.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {

                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);

            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        }

        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {

            cancelProgressDialog();
            showToastText("密码修改成功");
            finish();


        } else if (CALL_QUERY_FAILD == callID) {

            cancelProgressDialog();

            showToastText(((BaseResp) args[0]).getRespMsg());
        }
    }


}
