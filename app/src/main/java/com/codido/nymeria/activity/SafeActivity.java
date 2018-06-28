package com.codido.nymeria.activity;

import android.os.Bundle;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.resp.BaseResp;

import butterknife.OnClick;

/**
 * 安全中心
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class SafeActivity extends BaseActivity {


    @OnClick(R.id.linearLayoutLoginPwd)
    void gotoLoginPwdActivty() {

        ChangePwdActivity.changeLoginPassword(this);

     }


    @OnClick(R.id.linearLayoutPayPwd)
    void gotoPayPwdActivty() {
        ChangePwdActivity.changePayPassword(this);


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_safe;
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

        return false;
    }

    @Override
    public void call(int callID, Object... args) {

    }


}
