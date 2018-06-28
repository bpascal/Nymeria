package com.codido.nymeria.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.QueryWithdrawCardListReqData;
import com.codido.nymeria.bean.req.WithdrawReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryWithdrawCardListResp;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.BankCardSelectManager;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.YUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class WithdrawActivity extends BaseActivity {

    /**
     * 提现类型
     */
    String withdrawType;

    /**
     * 银行卡管理器
     */
    private BankCardSelectManager bankCardSelectManager;

    @BindView(R.id.layoutBankCards)
    LinearLayout layoutBankCards;
    @BindView(R.id.editTextAmt)
    EditText editTextAmt;
    @BindView(R.id.editTextPasswords)
    EditText editTextPasswords;
    @BindView(R.id.textViewWithdrawTip)
    TextView textViewWithdrawTip;

    @OnClick(R.id.buttonOk)
    void ok() {
        //确认提现
        CardVo cardVo = bankCardSelectManager.getSelectBankCard();

        String amt = getTextFromEditText(editTextAmt);
        String passwords = getTextFromEditText(editTextPasswords);
        if (!YUtils.isEmpty(amt) && YUtils.isNumeric(amt)) {
//            if (!YUtils.isEmpty(passwords)) {
//                sendWithdrawRequest(cardVo, amt, passwords);
//            } else {
//                showToastText("请输入支付密码");
//            }
            sendWithdrawRequest(cardVo, amt, passwords);
        } else {
            showToastText("请输入提现金额");
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void addAction() {
        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        withdrawType = getIntent().getStringExtra("withdrawType");
        if (YUtils.isEmpty(withdrawType)) {
            withdrawType = Global.WITHDRAW_TYPE_CASH;
        }


        bankCardSelectManager = new BankCardSelectManager(this);
        //获取银行卡列表
        sendQueryBankCardListRequest();
    }


    /**
     * 提现银行卡查询
     */
    private void sendQueryBankCardListRequest() {

        QueryWithdrawCardListReqData reqData = new QueryWithdrawCardListReqData();
        reqData.setWithdrawType(withdrawType);
        BaseReq baseReq = new BaseReq(Global.key_queryWithdrawCardList, reqData);


        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    /**
     * 提交充值申请
     */
    private void sendWithdrawRequest(CardVo cardVo, String amt, String payPwd) {
        showProgressDialog("请稍后...");
        WithdrawReqData reqData = new WithdrawReqData();
        reqData.setCardId(cardVo.getCardId());
        reqData.setWithdrawAmount(amt);
        //reqData.setPayPwd(Md5.MD5(payPwd.getBytes()));//加密
        reqData.setWithdrawType(withdrawType);//类型

        BaseReq baseReq = new BaseReq(Global.key_withdraw, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryWithdrawCardList.equals(responseBean.getKey())) {
            cancelProgressDialog();
            if (responseBean.isOk()) {
                //成功
                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        } else if (Global.key_withdraw.equals(responseBean.getKey())) {
            cancelProgressDialog();

            if (responseBean.isOk()) {
                //成功
                runCallFunctionInHandler(CALL_UPLOAD_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_UPLOAD_FAILD, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            cancelProgressDialog();
            //获取银行列表获取成功，更新界面
            QueryWithdrawCardListResp resp = (QueryWithdrawCardListResp) args[0];
            List<CardVo> cardList = resp.getCardList();
            if (cardList != null && cardList.size() > 0) {
                for (CardVo cardVo : cardList) {
                    //循环加载银行卡列表
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutBankCards.addView(bankCardSelectManager.createBankCardView(cardVo), layoutParams);
                }
            } else {
                //如果用户没有绑定储蓄卡，则提示先绑卡
                OkDialog dialog = new OkDialog(this, "提示", "请先绑定一张本人的储蓄卡，然后发起提现", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {// 确认后退出
                        finish();
                    }
                }, null, null);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
            textViewWithdrawTip.setText(resp.getPromptText());
        } else if (CALL_QUERY_FAILD == callID) {
            showToastText("获取提现银行卡失败");
            finish();

        } else if (CALL_UPLOAD_SUCCESS == callID) {
            //充值申请提交成功
            new OkDialog(this, "提示", "提现申请提交成功", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭页面
                    finish();
                }
            }, null, null).show();
        } else if (CALL_UPLOAD_FAILD == callID) {
            BaseResp baseresp = (BaseResp) args[0];
            showToastText(baseresp.getRespMsg());
        }
    }

}
