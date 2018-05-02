package com.codido.nymeria.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.req.RechargeReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryMyCardListResp;
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
 * 充值页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class RechargeActivity extends BaseActivity {

    /**
     * 银行卡管理器
     */
    private BankCardSelectManager bankCardSelectManager;

    @BindView(R.id.layoutBankCards)
    LinearLayout layoutBankCards;
    @BindView(R.id.editTextAmt)
    EditText editTextAmt;

    @OnClick(R.id.buttonOk)
    void ok() {
        //确认充值
        CardVo cardVo = bankCardSelectManager.getSelectBankCard();

        String amt = getTextFromEditText(editTextAmt);
        if (!YUtils.isEmpty(amt) && YUtils.isNumeric(amt)) {
            sendRechargeRequest(cardVo, amt);
        } else {
            showToastText("请输入充值金额");
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void addAction() {
        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        bankCardSelectManager = new BankCardSelectManager(this);

        String amt = getIntent().getStringExtra("needRechargeAmount");

        setTextToEditText(editTextAmt, amt);

        //获取我的银行卡列表
        sendQueryBankCardListRequest();
    }

    /**
     * 查询我的银行卡列表
     */
    private void sendQueryBankCardListRequest() {
        BaseReq baseReq = new BaseReq(Global.key_queryMyCardList, new BaseReqData());
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    /**
     * 提交充值申请
     */
    private void sendRechargeRequest(CardVo cardVo, String amt) {
        RechargeReqData reqData = new RechargeReqData();
        reqData.setCardId(cardVo.getCardId());
        reqData.setRechargeAmount(amt);

        BaseReq baseReq = new BaseReq(Global.key_recharge, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryMyCardList.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                //成功
                QueryMyCardListResp resp = (QueryMyCardListResp) responseBean;
                runCallFunctionInHandler(CALL_QUERY_SUCCESS, resp);
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        } else if (Global.key_recharge.equals(responseBean.getKey())) {
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
            //获取银行列表获取成功，更新界面
            QueryMyCardListResp resp = (QueryMyCardListResp) args[0];
            List<CardVo> cardList = resp.getCardList();
            if (cardList != null && cardList.size() > 0) {
                for (CardVo cardVo : cardList) {
                    //循环加载银行卡列表
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutBankCards.addView(bankCardSelectManager.createBankCardView(cardVo), layoutParams);
                }
            }
        } else if (CALL_QUERY_FAILD == callID) {
            showToastText("获取银行卡列表失败");
        } else if (CALL_UPLOAD_SUCCESS == callID) {
            //充值申请提交成功
            new OkDialog(this, "提示", "充值申请提交成功", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    //关闭页面
                    finish();
                }
            }, null, null).show();
        } else if (CALL_UPLOAD_FAILD == callID) {
            BaseResp baseresp = (BaseResp) args[0];
            showToastText("获取银行卡列表失败:" + baseresp.getRespMsg());
        }
    }

}
