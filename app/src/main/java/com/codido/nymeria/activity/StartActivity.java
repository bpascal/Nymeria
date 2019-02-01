package com.codido.nymeria.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.CheckUpdateResp;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.FileManager;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.YUtils;

import butterknife.BindView;

/**
 * 作者：Junjie.Lai on 2016/10/24 16:45
 * 邮箱：laijj@yzmm365.cn
 */
public class StartActivity extends BaseActivity {

    /**
     * 申请文件权限
     */
    protected static final int EXTERNAL_STORAGE_REQ_CODE = BaseActivity.FIRST_VAL++;

    /**
     * 检查更新成功
     */
    public static int CALL_CHECK_UPDATE_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 跳转下一步
     */
    public static int CALL_GOTO_NEXT = BaseActivity.FIRST_VAL++;

    @BindView(R.id.textViewVersion)
    TextView textViewVersion;

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    public void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        requestPermission();//申请权限

        // 初始化文件缓存
        FileManager.initFileCacheFile(this);

        //FIXME Junjie.Lai 做签名判断
        if (!"c1cda151d0a0a0d14908992b5d04ff44".equals(YUtils.getSignInfo(this))) {
            Global.DEBUG = true;
        } else {
            Global.DEBUG = false;
        }
        textViewVersion.setText(Global.getVersion(this));
    }

    /**
     * 跳转线程
     */
    private Runnable skipLoginThread = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runCallFunctionInHandler(CALL_QUERY_SUCCESS, null);
        }
    };


    /**
     * 检查更新函数
     */
    public void sendCheckUpdateRequest() {
        // 构建请求对象，向后台发送检查更新请求
        BaseReqData reqData = new BaseReqData();

        BaseReq baseReq = new BaseReq(Global.key_checkUpdate, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    /**
     * 跳转登录页面
     */
    private void gotoLoginActivity() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转主页
     */
    private void gotoMainActivity() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 发起权限申请
     */
    private void requestPermission() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
            //判断是否有这些权限
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) || (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA))) {
                //如果App的权限申请曾经被用户拒绝过，这里是第二次弹出，就需要在这里跟用户做出解释
                //WRITE_EXTERNAL_STORAGE,READ_PHONE_STATE,ACCESS_FINE_LOCATION,RECORD_AUDIO,SEND_SMS,CAMERA
                showToastText("请在设置->应用->权限列表中赋予我需要的权限，我们承诺所有权限不会用于该APP以外的任何地方");
                //requestPermission();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.SEND_SMS, android.Manifest.permission.CAMERA}, EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {
            //有权限了就跳转
            new Thread(skipLoginThread).start();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_REQ_CODE) {
            // 如果请求被拒绝，那么通常grantResults数组为空
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，进行相应操作
                showToastText("申请权限成功");
                //倒数5秒后跳转登录页面
                new Thread(skipLoginThread).start();
            } else {
                //申请失败，可以继续向用户解释。
                showToastText("申请权限失败，我们需要这些权限，请允许，我们承诺会安全访问您的设备");
                requestPermission();
            }
            return;
        }
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            //成功
            Global.initGlobal(getApplicationContext());

            //确定了版本地址之后就检查版本更新
            sendCheckUpdateRequest();
        } else if (CALL_CHECK_UPDATE_SUCCESS == callID) {
            //检查更新完成
            final CheckUpdateResp resp = (CheckUpdateResp) args[0];
            if (CheckUpdateResp.VER_TYPE_NO_UPDATE.equals(resp.getVerType())) {
                // 如果类型为0，即不需要更新
                gotoNext();
                return;
            }

            //TODO Junjie.Lai 小红点暂时不做
//            VerKeeper.saveNewVer(this, resp.getVerName());
//            Intent intent = new Intent(Global.ACTION_RECEIVE_NEWVER);
//            sendBroadcast(intent);

            String message = "新版本:" + resp.getVerName() + "\n";
            // 非强制版本更新
            if (CheckUpdateResp.VER_TYPE_NOT_MUST_UPDATE.equals(resp.getVerType())) {
                OkDialog dialog = new OkDialog(this, "更新提示", message + resp.getVerDesc(), "确认更新", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {// 确认更新
                        // 开始下载新版本apk
                        startDownLoad(resp.getVerUrl());
                        // 页面仍然跳转到下一步，本版本为非强制更新版本
                        gotoNext();
                    }
                }, "暂不更新", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {// 暂不更新
                        // 页面跳转到下一步
                        gotoNext();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            } else if (CheckUpdateResp.VER_TYPE_MUST_UPDATE.equals(resp.getVerType())) {
                // 强制版本更新
                OkDialog dialog = new OkDialog(this, "更新提示", message, "确认更新",
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) { // 确认更新
                                // 开始下载应用程序apk
                                startDownLoad(resp.getVerUrl());
                                // 退出
                                exit(StartActivity.this);
                            }
                        }, "退出", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {// 退出
                        // 退出应用程序
                        exit(StartActivity.this);
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        } else if (CALL_GOTO_NEXT == callID) {
            //跳转下一步
            gotoNext();
        }
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_checkUpdate.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                // 通知界面返回更新
                runCallFunctionInHandler(CALL_CHECK_UPDATE_SUCCESS, responseBean);
            } else {
                // 检查更新接口调用失败，通知界面跳转到主页面
                runCallFunctionInHandler(CALL_GOTO_NEXT);
            }
        }
        return false;
    }

    /**
     * 跳转下一步
     */
    private void gotoNext() {
        if (isLogin) {
            //登录用户跳转到主页
            gotoMainActivity();
        } else {
            // 非登录用户跳转到非登录用户主页
            gotoLoginActivity();
        }
    }
}
