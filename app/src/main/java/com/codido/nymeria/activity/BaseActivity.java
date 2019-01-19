package com.codido.nymeria.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codido.nymeria.R;
import com.codido.nymeria.dialog.ProgressDialog;
import com.codido.nymeria.service.DownLoadAppUpdateService;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessListener;
import com.codido.nymeria.util.ServerModelKeeper;
import com.codido.nymeria.util.YUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * activity基类
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity implements ProcessListener {

    /**
     * 常量，用来在给系统中其他的常量赋值，每次赋值后+1，以保证系统中的所有整形常量都绝对唯一
     */
    public static int FIRST_VAL = 18;

    /**
     * 基本常量
     */
    public static final int SHOW_CONFIRM_DIALOG = BaseActivity.FIRST_VAL++;
    public static final int SHOW_ERROR_DIALOG = BaseActivity.FIRST_VAL++;
    public static final int SHOW_MESSAGE_DIALOG = BaseActivity.FIRST_VAL++;
    public static final int SHOW_PROGRESS_DIALOG = BaseActivity.FIRST_VAL++;
    public static final int SHOW_PROGRESS_DIALOG_CAN_CANCEL = BaseActivity.FIRST_VAL++;
    public static final int SHOW_TOAST_TEXT = BaseActivity.FIRST_VAL++;
    public static final int CANCEL_PROGRESS_DIALOG = BaseActivity.FIRST_VAL++;
    public static final int CLOSE_ACTIVITY = BaseActivity.FIRST_VAL++;
    public static final int CALL_FUNCTION = BaseActivity.FIRST_VAL++;
    public static final int WHAT_CLOSE_ACTIVITY = BaseActivity.FIRST_VAL++;
    public static final int CALL_FILE_LOADED = BaseActivity.FIRST_VAL++;
    public static final int CALL_IMG_LOADED = BaseActivity.FIRST_VAL++;
    /**
     * 接口常用常量
     */
    public static final int CALL_QUERY_SUCCESS = BaseActivity.FIRST_VAL++;
    public static final int CALL_QUERY_FAILD = BaseActivity.FIRST_VAL++;
    public static final int CALL_UPLOAD_SUCCESS = BaseActivity.FIRST_VAL++;
    public static final int CALL_UPLOAD_FAILD = BaseActivity.FIRST_VAL++;
    public static final int CALL_EXIT_SUCCESS = BaseActivity.FIRST_VAL++;
    public static final int CALL_EXIT_FAILD = BaseActivity.FIRST_VAL++;

    /**
     * 充值
     */
    public static final int REQ_FOR_RECHARGE = BaseActivity.FIRST_VAL++;
    /**
     * 添加银行卡
     */
    public static final int REQ_FOR_ADD_CRAD = BaseActivity.FIRST_VAL++;

    /**
     * 查看银行卡详情
     */
    public static final int REQ_FOR_CRAD_DETAIL = BaseActivity.FIRST_VAL++;

    /**
     * 添加还款计划
     */
    public static final int REQ_FOR_ADD_PAYOFF_PLAN = BaseActivity.FIRST_VAL++;

    /**
     * 保存头像及用户名
     */
    private static final String PREFERENCES_LAST_LOGIN_INFO = "last_useranme_and_headimg";
    /**
     * 保存所有的Activity界面，用于在退出时，关闭所有的界面
     */
    public static List<BaseActivity> activities;

    /**
     * 屏幕像素密集度相关信息
     */
    public static float density = 0F;

    /**
     * 上次登录的用户名
     */
    protected String lastLoginUserName;

    /**
     * 上次登录的用户头像
     */
    protected String lastLoginHeadImg;

    /**
     * 是否登录
     */
    protected boolean isLogin;

    /**
     * 加载动画
     */
    private Animation animationLoading;

    /**
     * 显示设置相关计算需要用的组件
     */
    private DisplayMetrics metrics = new DisplayMetrics();

    /**
     * 加载的view
     */
    private View progressBarLoading;

    /**
     * 加载dialog
     */
    private ProgressDialog progressDialog;

    /**
     * 界面标题显示
     */
    private TextView textViewTitle;

    /**
     * 读取的文本提示框
     */
    private TextView textViewLoading;

    /**
     * 读取的view
     */
    private View viewLoading;

    /**
     * 回退view
     */
    @Nullable
    @BindView(R.id.viewBack)
    View viewBack;

    /**
     * 加载loadview的判断bool
     */
    private boolean isFindViews = false;

    /**
     * 整体的handler，处理各种非UI进程的调度
     */
    protected Handler handler = new Handler() {
        private boolean runInGroupFlag;

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == SHOW_PROGRESS_DIALOG) {
                String ms = msg.obj == null ? null : (String) msg.obj;
                if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(BaseActivity.this, ms);
                } else {
                    progressDialog.setMsg(ms);
                }
            } else if (msg.what == SHOW_PROGRESS_DIALOG_CAN_CANCEL) {
                String ms = msg.obj == null ? null : (String) msg.obj;
                final int arg = msg.arg1;

                if (progressDialog == null) {
                    Activity activity = runInGroupFlag ? getParent() : BaseActivity.this;
                    progressDialog = ProgressDialog.show(activity, new OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            call(arg);
                        }
                    }, ms);
                } else {
                    progressDialog.setMsg(ms);
                }
            } else if (msg.what == CANCEL_PROGRESS_DIALOG) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } else if (msg.what == SHOW_ERROR_DIALOG) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } else if (msg.what == SHOW_TOAST_TEXT) {
                String info_toast_text = (String) msg.obj;
                Toast.makeText(BaseActivity.this, info_toast_text, Toast.LENGTH_SHORT).show();
            } else if (msg.what == SHOW_MESSAGE_DIALOG) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } else if (msg.what == SHOW_CONFIRM_DIALOG) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } else if (msg.what == CALL_FUNCTION) {
                if (msg.obj == null) {
                    call(msg.arg1);
                } else {
                    call(msg.arg1, (Object[]) msg.obj);
                }
            } else if (msg.what == WHAT_CLOSE_ACTIVITY) {
                BaseActivity.this.finish();
            }
        }
    };

    /**
     * 设置布局
     */
    protected abstract int getContentView();

    /**
     * 添加事件
     */
    protected abstract void addAction();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);


    /**
     * 转换到ui进程处理事件
     *
     * @param callID
     * @param args
     */
    public abstract void call(int callID, Object... args);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isLogin = DataKeeper.recoverLoginData(this);
        // 获取用户在About关于页面中设定的服务器模式，普通用户一般不会设置该参数，用于开发中切换服务器模式
        int serverModel = ServerModelKeeper.getServerModel(this);
        // 如果本机缓存的服务器模式不为空，则启用相关的服务模式
        if (serverModel >= 0) {
            Global.SERVER_MODEL = serverModel;
        }

        // 初始化全局设置
        Global.initGlobal(this);

        if (activities == null) {
            activities = new ArrayList<BaseActivity>();
        }
        activities.add(this);

        //设置布局
        setContentView(getContentView());
        //绑定控件
        ButterKnife.bind(this);
        //初始化数据
        initData(savedInstanceState);
        //添加事件
        addAction();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activities != null) {
            activities.remove(this);
        }
    }

    /**
     * 根据ID隐藏某个View
     *
     * @param res
     */
    public void hideView(int res) {
        View view = findViewById(res);
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }


    /**
     * 打开图片操作
     *
     * @param mUri
     */
    public void crop(Uri mUri) {
        if (null == mUri)
            return;

        Intent intent = new Intent();

        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 200);
    }


    /**
     * 保存上次用户在登录界面是否点击过暂不注册，随便看看
     *
     * @param context
     * @param isNoRegisterEnter
     */
    public static void setNoRegisterEnter(Context context, boolean isNoRegisterEnter) {
        SharedPreferences pref = context.getSharedPreferences(
                PREFERENCES_LAST_LOGIN_INFO, 0);
        Editor editor = pref.edit();
        try {
            editor.putBoolean("NoRegisterEnter", isNoRegisterEnter);
            editor.commit();
        } catch (Exception e) {
        }

    }

    /**
     * 保存上次登录的用户名
     *
     * @param context
     * @param username
     */
    public static void saveLastLoginUserName(Context context, String username) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_LAST_LOGIN_INFO, 0);
        Editor editor = pref.edit();
        try {
            editor.putString("username", username);
            editor.commit();
        } catch (Exception e) {
        }
    }

    /**
     * 保存上次登录的头像地址
     *
     * @param context
     * @param heaImg
     */
    public static void saveLastLoginHeadImg(Context context, String heaImg) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_LAST_LOGIN_INFO, 0);
        Editor editor = pref.edit();
        try {
            editor.putString("headimg", heaImg);
            editor.commit();
        } catch (Exception e) {
        }
    }

    /**
     * 清除上次登录的用户手机号和头像地址
     *
     * @param context
     */
    public void clearLoginInfo(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_LAST_LOGIN_INFO, 0);
        Editor editor = pref.edit();
        try {
            editor.clear();
            editor.commit();
        } catch (Exception e) {
        }
    }

    /**
     * 获取上次登录的用户名和头像信息
     *
     * @param context
     */
    public void getLastLoginInfo(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_LAST_LOGIN_INFO, 0);
        try {
            // 获取登录名和头像信息，直接对成员变量赋值
            lastLoginUserName = pref.getString("username", null);
            lastLoginHeadImg = pref.getString("headimg", null);
        } catch (Exception e) {
        }
    }


    /**
     * 安全的获取EditText中内容的方法
     *
     * @param editText 指定的输入框
     * @return 返回输入框中的文字内容
     */
    public static String getTextFromEditText(TextView editText) {
        // 如果指定的输入框为null，返回空串
        if (editText == null) {
            return "";
        }
        CharSequence editable = editText.getText();
        if (editable == null) {
            return "";
        } else {
            String result = editable.toString();
            if (result != null) {
                result = result.trim();
            }
            return result;
        }
    }

    /**
     * 安全的设置EditText中内容的方法,如果提交的str参数为空，则忽略该操作
     *
     * @param editText
     * @param str
     */
    public static void setTextToEditText(EditText editText, String str) {
        if (editText == null) {
            return;
        }
        if (str == null) {
            editText.setText("");
            return;
        }
        editText.setText(str);
        editText.setSelection(str.length());
    }

    /**
     * 为界面的返回按钮添加返回处理，该处理即关闭本页面，要求页面的layout中有一个id为viewBack的组件，否则调用该函数无效
     */
    public void addBackAction() {
        //viewBack = findViewById(R.id.viewBack);
        if (viewBack != null) {
            viewBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击返回按钮，直接关闭页面
                    finish();
                }
            });
        }
    }

    /**
     * 为界面的返回按钮添加指定的返回处理 ，要求页面的layout中有一个id为viewBack的组件，否则调用该函数无效
     *
     * @param onClickListener 指定的返回按钮点击处理器
     */
    public void addBackAction(OnClickListener onClickListener) {
        //viewBack = findViewById(R.id.viewBack);
        if (viewBack != null) {
            // 点击返回按钮，调用onClickListener的onClick方法
            viewBack.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置界面标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (textViewTitle == null) {
            textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        }
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }


    /**
     * 拨打电话
     *
     * @param phone_no
     */
    public void callPhone(final String phone_no) {
        if (YUtils.isEmpty(phone_no)) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phone_no));
        startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param number
     * @param message
     */
    public void sendSms(String number, String message) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        startActivity(sendIntent);
    }


    @SuppressLint("NewApi")
    public Bitmap changeBy(String cameraPath, Bitmap bitmap) {
        // 根据图片的旋转度数，要确定是否修正图片
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(cameraPath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

            if (bm != null) { // 如果生成的新图片不为空的话,删除之前的图
                if (bitmap != null && bitmap != bm) {
                    bitmap.recycle();
                }
            }
            bitmap = bm;
        }
        return bitmap;

    }

    /**
     * 关闭页面
     */
    public void close() {
        sendMessageToHanler(CLOSE_ACTIVITY);
    }

    /**
     * 复制文本消息到剪贴板
     *
     * @param str
     */
    public void copyText(String str) {
        if (str == null) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("yzmm_copy_data", str);
        cmb.setPrimaryClip(clip);
    }

    /**
     * 从粘贴板获取数据
     */
    public String getTextFromClipboard() {
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        String resultString = "";
        // 检查剪贴板是否有内容
        if (!mClipboard.hasPrimaryClip()) {
            return null;
        } else {
            ClipData clipData = mClipboard.getPrimaryClip();
            int count = clipData.getItemCount();

            if (count <= 0) {
                return null;
            }

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item.coerceToText(BaseActivity.this);

                resultString += (str);
            }

        }
        return resultString;
    }


    /**
     * 获取页面高度px值
     *
     * @return
     */
    public int getHeightPixels() {
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        return metrics.heightPixels;
    }

    /**
     * 取页面宽度的px值
     *
     * @return
     */
    public int getWidthPixels() {
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        return metrics.widthPixels;
    }


    /**
     * 启动外部浏览器打开一个链接
     *
     * @param url 需要打开的URL地址
     */
    public void openUrl(String url) {
        try {
            Uri uri = Uri.parse(url); // url为你要链接的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发起一次非UI进程转入UI进程的请求
     *
     * @param callID
     * @param args
     */
    public void runCallFunctionInHandler(int callID, Object... args) {
        sendMessageToHanler(CALL_FUNCTION, args, callID);
    }

    /**
     * 消息发送方法
     *
     * @param what
     */
    public void sendMessageToHanler(int what) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.sendToTarget();
    }

    /**
     * 带参数的消息发送方法
     *
     * @param what
     * @param arg1
     */
    public void sendMessageToHanler(int what, int arg1) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    /**
     * 发送对象的消息发送方法
     *
     * @param what
     * @param obj
     */
    public void sendMessageToHanler(int what, Object obj) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        message.sendToTarget();
    }

    /**
     * 发送对象和参数的消息发送方法
     *
     * @param what
     * @param obj
     * @param arg1
     */
    public void sendMessageToHanler(int what, Object obj, int arg1) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    /**
     * 显示错误提示框
     *
     * @param message
     */
    protected void showErrorDialog(String message) {
        sendMessageToHanler(SHOW_ERROR_DIALOG, message);
    }

    /**
     * 加载提示框
     *
     * @param message
     */
    protected void showMessageDialog(String message) {
        sendMessageToHanler(SHOW_MESSAGE_DIALOG, message);
    }

    /**
     * 加载进度框
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        sendMessageToHanler(SHOW_PROGRESS_DIALOG, message);
    }

    /**
     * 加载可取消的进度框
     *
     * @param message
     * @param callId
     */
    protected void showProgressDialogCanCancel(String message, int callId) {
        sendMessageToHanler(SHOW_PROGRESS_DIALOG_CAN_CANCEL, message, callId);
    }

    /**
     * 取消加载框
     */
    public void cancelProgressDialog() {
        sendMessageToHanler(CANCEL_PROGRESS_DIALOG);
    }


    /**
     * 显示一句提示toast
     *
     * @param text
     */
    public void showToastText(String text) {
        sendMessageToHanler(SHOW_TOAST_TEXT, text);
    }


    /**
     * 从界面中找到加载相关的组件
     */
    public void findLoadingViews() {
        if (isFindViews) {
            return;
        }
        viewLoading = findViewById(R.id.viewLoading);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        textViewLoading = (TextView) findViewById(R.id.textViewLoading);
        isFindViews = true;
    }


    /**
     * 显示loading界面
     *
     * @param msg
     * @param listener
     */
    public void showLoadingView(String msg, OnClickListener listener) {
        findLoadingViews();

        if (viewLoading == null) {
            return;
        }
        viewLoading.setVisibility(View.VISIBLE);
        animationLoading = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animationLoading.setDuration(1000L);
        animationLoading.setFillAfter(true);
        animationLoading.setInterpolator(new LinearInterpolator());
        animationLoading.setRepeatCount(-1);
        progressBarLoading.startAnimation(animationLoading);

        progressBarLoading.setVisibility(View.VISIBLE);
        textViewLoading.setVisibility(View.VISIBLE);
        if (msg != null) {
            textViewLoading.setText(msg);
        } else {
            textViewLoading.setText("正在加载...");
        }
        if (listener != null) {
            viewLoading.setOnClickListener(listener);
        } else {
            viewLoading.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });

        }
    }

    /**
     * 隐藏正在加载页面，一般在页面加载完毕的时候调用
     */
    public void hideLodingViews() {
        findLoadingViews();
        if (viewLoading == null) {
            return;
        }
        viewLoading.setVisibility(View.GONE);
        progressBarLoading.clearAnimation();
    }

    /**
     * 显示加载失败后的界面，在页面加载失败的时候调用
     */
    public void showLoadFaild(String msg, OnClickListener listener) {
        findLoadingViews();
        if (viewLoading == null) {
            return;
        }
        viewLoading.setVisibility(View.VISIBLE);
        progressBarLoading.setVisibility(View.GONE);
        progressBarLoading.clearAnimation();
        textViewLoading.setVisibility(View.VISIBLE);
        if (msg != null) {
            textViewLoading.setText(msg);
        } else {
            textViewLoading.setText("加载失败，点击重试");
        }
        if (listener != null) {
            viewLoading.setOnClickListener(listener);
        } else {
            viewLoading.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });

        }
    }


    /**
     * 显示系统软键盘
     */
    public void showSoftInput(final View view) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                view.requestFocus();
                imm.toggleSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // imm.showSoftInputFromInputMethod(view.getWindowToken(),
                // InputMethodManager.SHOW_FORCED);
            }
        }, 200L);
    }

    /**
     * 隐藏系统软键盘
     *
     * @return 调用时，软键盘状态
     */
    public boolean hideInputMethod() {
        return hideInputMethod(null);
    }

    /**
     * 隐藏系统软键盘
     *
     * @return 调用时，软键盘状态
     */
    public boolean hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isActive = false;
        if (isActive = imm.isActive()) {
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // (WidgetSearchActivity是当前的Activity)
            } else {
                imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), 0); // (WidgetSearchActivity是当前的Activity)
            }
        }

        Global.debug("键盘状态=" + isActive);
        return isActive;
    }

    /**
     * 启动下载Service，开始下载
     *
     * @param url 指定的下载资源的URL
     */
    public void startDownLoad(String url) {
        // openUrl(url);
        Intent intent = new Intent(this, DownLoadAppUpdateService.class);
        intent.putExtra("Key_App_Name", getString(R.string.app_name));
        intent.putExtra("Key_Down_Url", url);
        startService(intent);
    }

    /**
     * 退出函数，虽然是static函数，但是需要在UI线程中调用
     *
     * @param context 调用退出的上下文
     */
    public static void exit(Context context) {
        Global.CookieStr = null;
        for (int i = activities.size() - 1; i >= 0; i--) {
            BaseActivity activity = activities.get(i);
            activity.handler.sendEmptyMessage(WHAT_CLOSE_ACTIVITY);
        }
    }

    /**
     * 去展示图片页面
     */
//    public void gotoShowImgs(ImageListBean info, int index) {
//        if (info.getImgList() == null || info.getImgList().size() == 0) {
//            return;
//        }
//
//        ArrayList<String> urls = new ArrayList<String>();
//        for (int i = 0; i < info.getImgList().size(); i++) {
//            urls.add(Global.getImgUrl(info.getImgList().get(i)));
//        }
//        ShowImageActivity.start(this, urls, null, index);
    // }
}
