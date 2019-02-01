package com.codido.nymeria.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.AboutActivity;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.activity.LoginActivity;
import com.codido.nymeria.activity.WebActivity;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import butterknife.BindView;
import butterknife.OnClick;

import static com.codido.nymeria.activity.BaseActivity.CALL_EXIT_FAILD;
import static com.codido.nymeria.activity.BaseActivity.CALL_EXIT_SUCCESS;
import static com.codido.nymeria.activity.BaseActivity.CALL_QUERY_FAILD;
import static com.codido.nymeria.activity.BaseActivity.CALL_QUERY_SUCCESS;

/**
 * 我的账户信息页面
 * 作者：Junjie.Lai on 2016/12/18 22:43
 * 邮箱：laijj@yzmm365.cn
 */
public class MoreFragment extends BaseFragment {


    @BindView(R.id.relativeLayoutIncomes)
    View relativeLayoutIncomes;


    @BindView(R.id.relativeLayoutTotalBalance)
    View relativeLayoutTotalBalance;
    @BindView(R.id.linearLayoutTxnOrderList)
    View linearLayoutTxnOrderList;

    @BindView(R.id.imageViewSwitchSeeAmt)
    ImageView imageViewSwitchSeeAmt;
    @BindView(R.id.textViewTotalAmt)
    TextView textViewTotalAmt;
    @BindView(R.id.textViewAbleBalance)
    TextView textViewAbleBalance;
    @BindView(R.id.textViewTotalIncome)
    TextView textViewTotalIncome;
    @BindView(R.id.textViewBalance)
    TextView textViewBalance;
    @BindView(R.id.linearLayoutProblem)
    LinearLayout linearLayoutProblem;
    @BindView(R.id.textViewTotalAssetStr)
    TextView textViewTotalAssetStr;

    /**
     * 显示状态
     */
    private String moneyShowSts;

    /**
     * 用户信息参数
     */
    private UserInfoVo userInfoVo;


    @OnClick(R.id.textViewTotalAmt)
    void gotoBalanceActivity() {
        Intent intent = new Intent(getActivity(), MyBalanceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imageViewSwitchSeeAmt)
    void imageViewSwitchSeeAmtClickEvent() {
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            //显示改隐藏
            textViewTotalAmt.setText("--");
            textViewAbleBalance.setText("--");
            textViewTotalIncome.setText("--");
            textViewBalance.setText("--");
            textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
            moneyShowSts = Global.SHOW_MONEY_STS_HIDE;
            DataKeeper.saveShowMoneySts(getmActivity(), Global.SHOW_MONEY_STS_HIDE);
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
        } else {
            //隐藏改显示
            if (userInfoVo != null) {
                textViewTotalAmt.setText(userInfoVo.getTotalBalance());
                textViewAbleBalance.setText(userInfoVo.getAbleEarnBalance());
                textViewTotalIncome.setText(userInfoVo.getAccuEarnBalance());
                textViewBalance.setText(Float.valueOf(userInfoVo.getAbleCashBal()) + Float.valueOf(userInfoVo.getAccuEarnBalance()) + "");
                textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
            } else {
                textViewTotalAmt.setText("--");
                textViewAbleBalance.setText("--");
                textViewTotalIncome.setText("--");
                textViewBalance.setText("--");
                textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
            }
            moneyShowSts = Global.SHOW_MONEY_STS_SHOW;
            DataKeeper.saveShowMoneySts(getmActivity(), Global.SHOW_MONEY_STS_SHOW);
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
        }
    }

    @OnClick(R.id.linearLayoutExit)
    void exit() {

        new OkDialog(getActivity(), "提示", "是否确认退出？", "退出",
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        MoreFragment.this.showProgressDialog("正在退出");
                        BaseReq req = new BaseReq();
                        req.setKey(Global.key_logout);
                        req.setReqData(new BaseReqData());
                        ProcessManager.getInstance().addProcess(getActivity(), req, MoreFragment.this);
                    }
                }, "取消", null).show();
    }

    @OnClick(R.id.linearLayoutProblem)
    void linearLayoutProblemEvent() {
        WebActivity.start(getActivity(), Global.HTML_HELP_ADDRESS);
    }

    @OnClick(R.id.relativeLayoutIncomes)
    void gotoInComeActivity() {
        Intent intent = new Intent(getActivity(), InComeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonRecharge)
    void gotoRechargeActivity() {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linearLayoutAbout)
    void about() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.buttonWithdraw)
    void gotoWithdrawActivity() {
        Intent intent = new Intent(getActivity(), WithdrawActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linearLayoutTxnOrderList)
    void gotoTxnOrderListActivity() {
        Intent intent = new Intent(getActivity(), TxnOrderListActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.linearLayoutSafeCenter)
    void gotoSafeActivity() {
        Intent intent = new Intent(getActivity(), SafeActivity.class);
        startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_more;
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        moneyShowSts = DataKeeper.getShowMoneySts(getmActivity());
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            //显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
        } else {
            //不显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
        }
        sendQueryMyUserInfoRequest();
    }

    /**
     * 发送获取我的用户信息请求
     */
    private void sendQueryMyUserInfoRequest() {
        BaseReq baseReq = new BaseReq(Global.key_queryMyUserInfo, new BaseReqData());
        ProcessManager.getInstance().addProcess(getActivity(), baseReq, this);
    }

    @Override
    protected void call(int id, Object... args) {
        if (CALL_EXIT_SUCCESS == id) {
            cancelProgressDialog();

            BaseActivity.exit(getActivity());
            DataKeeper.clearLoginData(getActivity());

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (CALL_EXIT_FAILD == id) {

            //BaseResp responseBean = (BaseResp) args[0];
            //showToastText("退出失败");
            BaseActivity.exit(getActivity());
            DataKeeper.clearLoginData(getActivity());

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (CALL_QUERY_SUCCESS == id) {
            //获取邀请列表获取成功，更新界面
            QueryMyUserInfoResp resp = (QueryMyUserInfoResp) args[0];
            if (resp != null) {
                userInfoVo = resp.getUserInfo();
                if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
                    if (userInfoVo != null) {
                        textViewTotalAmt.setText(userInfoVo.getTotalBalance());
                        textViewAbleBalance.setText(userInfoVo.getAbleEarnBalance());
                        textViewTotalIncome.setText(userInfoVo.getAccuEarnBalance());
                        textViewBalance.setText(Float.valueOf(userInfoVo.getAbleCashBal()) + Float.valueOf(userInfoVo.getAccuEarnBalance()) + "");
                        textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
                    } else {
                        textViewTotalAmt.setText("--");
                        textViewAbleBalance.setText("--");
                        textViewTotalIncome.setText("--");
                        textViewBalance.setText("--");
                        textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
                    }
                } else {
                    textViewTotalAmt.setText("--");
                    textViewAbleBalance.setText("--");
                    textViewTotalIncome.setText("--");
                    textViewBalance.setText("--");
                    textViewTotalAssetStr.setText("总资产 ("+userInfoVo.getLevel()+"星)");
                }

            }
        } else if (CALL_QUERY_FAILD == id) {
            //暂无处理
        }

    }

    @Override
    protected boolean callBack(BaseResp responseBean) {
        if (Global.key_logout.equals(responseBean.getKey())) {
            cancelProgressDialog();
            if (responseBean.isOk()) {
                //成功
                runCallFunctionInHandler(CALL_EXIT_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_EXIT_FAILD);
            }
        } else if (Global.key_queryMyUserInfo.equals(responseBean.getKey())) {
            //获取邀请列表的接口
            if (responseBean.isOk()) {
                QueryMyUserInfoResp resp = (QueryMyUserInfoResp) responseBean;

                runCallFunctionInHandler(CALL_QUERY_SUCCESS, resp);
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        }

        return false;

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onClick(View view) {

    }
}
