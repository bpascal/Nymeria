package com.codido.nymeria.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.activity.LoginActivity;

/**
 * 作者：Junjie.Lai on 2018/1/10 21:15
 * 邮箱：laijj@yzmm365.cn
 */
public class LoginBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        BaseActivity.exit(context);
        //跳登录
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(loginIntent);
    }
}
