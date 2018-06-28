package com.codido.nymeria.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.dialog.ProgressDialog;
import com.codido.nymeria.util.LollipopUtils;
import com.codido.nymeria.util.ProcessListener;

import butterknife.ButterKnife;

/**
 * fragment的基类
 */
public abstract class BaseFragment extends Fragment implements OnClickListener, ProcessListener {

    //通用常量
    private static final int WHAT_SHOW_PROGRESS_DIALOG = BaseActivity.FIRST_VAL++;
    private static final int WHAT_CANCEL_PROGRESS_DIALOG = BaseActivity.FIRST_VAL++;
    private static final int WHAT_CALL_FUNCTION = BaseActivity.FIRST_VAL++;
    private static final int WHAT_SHOW_TOAST_TEXT = BaseActivity.FIRST_VAL++;

    /**
     * 屏幕像素密集度相关信息
     */
    public static float density = 0F;

    /**
     * 显示设置相关计算需要用的组件
     */
    private DisplayMetrics metrics = new DisplayMetrics();

    /**
     * 保存头像及用户名
     */
    private static final String PREFERENCES_LAST_LOGIN_INFO = "last_useranme_and_headimg";

    /**
     * 是否网络错误的标识
     */
    protected boolean isNetError = false;

    /**
     * 是否被选择
     */
    private boolean isSelect = false;

    /**
     * 根view
     */
    protected View root;

    /**
     * 加载dialog
     */
    private ProgressDialog progressDialog;

    /**
     * fragment所属的activity
     */
    private BaseActivity mActivity;

    /**
     * 处理统一逻辑的handler
     */
    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_CALL_FUNCTION) {
                if (msg.obj == null) {
                    call(msg.arg1);
                } else {
                    call(msg.arg1, (Object[]) msg.obj);
                }
                return true;
            } else if (msg.what == WHAT_SHOW_PROGRESS_DIALOG) {
                //显示进度框
                String ms = msg.obj == null ? null : (String) msg.obj;
                if (progressDialog == null) {
                    progressDialog = ProgressDialog.show(getActivity(), ms);
                } else {
                    progressDialog.setMsg(ms);
                }
                return true;
            } else if (msg.what == WHAT_CANCEL_PROGRESS_DIALOG) {
                //取消进度框
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                return true;
            } else if (msg.what == WHAT_SHOW_TOAST_TEXT) {
                String info_toast_text = (String) msg.obj;
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                Toast toast = new Toast(getActivity());
                View toastView = inflater.inflate(R.layout.toast_common_item, null);
                TextView textView = (TextView) toastView.findViewById(R.id.textViewToast);
                textView.setText(info_toast_text);
                toast.setGravity(Gravity.CENTER, 0, 0);// Toast显示的位置 25.
                toast.setView(toastView);// 定制Toast
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                return true;
            } else {
                return false;
            }
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(getContentView(), null);
        ButterKnife.bind(this, root);
        addAction();
        mActivity = (BaseActivity) getActivity();
        initData(savedInstanceState);
        return root;
    }


    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 编程约束：子类中所有的组件的点击事件监听的设置应该出现在该函数的实现中
     */
    protected abstract void addAction();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);


    /**
     * 编程约束：所有操作UI线程的操作应该出现在该函数的实现中
     */
    protected abstract void call(int id, Object... args);

    /**
     * 服务器回调
     *
     * @param responseBean
     * @return
     */
    protected abstract boolean callBack(BaseResp responseBean);

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (!isAdded()) {
            //这个Fragment处理问题，那么所有的请求中断处理
            return true;
        }
        //获取错误码
        String respCd = responseBean.getRespCode();
        //网络错误检查
        if (BaseResp.RESP_NET_ERROR.equals(respCd)) {
            isNetError = true;
        } else {
            isNetError = false;
        }
        //自下而上的处理机制，如果孩子没有能力处理完这个事件，则父亲来处理这个事件
        if (callBack(responseBean)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 选中了当前的fragment的回调
     */
    public abstract void onSelected();

    /**
     * 显示进度dialog
     *
     * @param message
     */
    public void showProgressDialog(String message) {
        sendMessageToHanler(WHAT_SHOW_PROGRESS_DIALOG, message);
    }

    /**
     * 取消加载框
     */
    public void cancelProgressDialog() {
        sendMessageToHanler(WHAT_CANCEL_PROGRESS_DIALOG);
    }

    /**
     * 显示toast
     *
     * @param text
     */
    public void showToastText(String text) {
        sendMessageToHanler(WHAT_SHOW_TOAST_TEXT, text);
    }


    /**
     * 取页面宽度的px值
     *
     * @return
     */
    public int getWidthPixels() {
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        return metrics.widthPixels;
    }

    /**
     * 在UI线程中运行call函数，一般call函数由子类复写，callID将作为call函数的参数
     *
     * @param callID 在UI线程中运行call函数，一般call函数由子类复写，id将作为call函数的参数传入
     * @param args
     */
    protected void runCallFunctionInHandler(int callID, Object... args) {
        sendMessageToHanler(WHAT_CALL_FUNCTION, args, callID);
    }

    /**
     * 消息发送函数
     */
    public void sendMessageToHanler(int what, int arg1) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    /**
     * 消息发送函数
     */
    public void sendMessageToHanler(int what) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.sendToTarget();
    }

    /**
     * 消息发送函数
     */
    public void sendMessageToHanler(int what, Object obj) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        message.sendToTarget();
    }

    /**
     * 消息发送函数
     */
    public void sendMessageToHanler(int what, Object obj, int arg1) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        message.arg1 = arg1;
        message.sendToTarget();
    }

    /**
     * 延迟消息发生
     *
     * @param what
     * @param obj
     * @param delayMillis 延迟时间
     */
    public void sendMessageToHanlerDelayed(int delayMillis, int what, Object obj, int arg1) {
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        message.arg1 = arg1;
        handler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * 当设备为L以上
     * 状态栏透明时，才能调用该方法
     */
    public void fixStatusTransparent(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = LollipopUtils.getStatusBarHeight(getActivity());
            view.setPadding(0, statusBarHeight, 0, 0);
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
        SharedPreferences.Editor editor = pref.edit();
        try {
            editor.putString("username", username);
            editor.commit();
        } catch (Exception e) {
        }
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public BaseActivity getmActivity() {
        return mActivity;
    }

    public void setmActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
