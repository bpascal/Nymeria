package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.CancelPayoffPlanReqData;
import com.codido.nymeria.bean.req.QueryPayoffPlanDetailReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryPayoffPlanDetailResp;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.bean.vo.PayoffPlanDetailVo;
import com.codido.nymeria.bean.vo.PayoffPlanVo;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 卡详情页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class PayOffPlanDetailActivity extends BaseActivity {


    @BindView(R.id.imageViewBankIcon)
    ImageView imageViewBankIcon;
    @BindView(R.id.textViewBankName)
    TextView textViewBankName;
    @BindView(R.id.textViewCardType)
    TextView textViewCardType;
    @BindView(R.id.textViewCardNo)
    TextView textViewCardNo;
    @BindView(R.id.textViewBillDate)
    TextView textViewBillDate;
    @BindView(R.id.textViewPayOffDate)
    TextView textViewPayOffDate;
    @BindView(R.id.imageViewPaySuccess)
    ImageView imageViewPaySuccess;
    @BindView(R.id.linearLayoutPayoffDay)
    LinearLayout linearLayoutPayoffDay;
    @BindView(R.id.buttonCancel)
    Button buttonCancel;


    @OnClick(R.id.buttonCancel)
    void buttonCancelEvent() {
        //取消计划
        OkDialog okDialog = new OkDialog(this, "提示", "你确定要取消还款计划吗", "确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCancelPayoffPlanRequest(payoffPlanVo.getPlanId());
            }
        }, "取消", null);
        okDialog.show();
    }

    /**
     * 卡详情对象
     */
    private CardVo cardVo;

    /**
     * 还款计划
     */
    private PayoffPlanVo payoffPlanVo;

    @Override
    protected int getContentView() {
        return R.layout.activity_payoff_plan_detail;
    }

    @Override
    public void addAction() {
        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        cardVo = (CardVo) intent.getSerializableExtra("cardVo");
        payoffPlanVo = (PayoffPlanVo) intent.getSerializableExtra("payoffPlanVo");

        //显示文字
        textViewBankName.setText(cardVo.getBankName());
        textViewCardType.setText(Constants.CRADS_TYPES.get(cardVo.getCardType()));
        textViewCardNo.setText(cardVo.getCardNo());
        textViewBillDate.setText("账单日:" + cardVo.getBillDay());
        textViewPayOffDate.setText("还款日:" + cardVo.getPayoffDay());

        //显示图片
        Glide.with(this).load(cardVo.getBankIcon()).dontAnimate().
                placeholder(R.mipmap.bankcard_icon_default).error(R.mipmap.bankcard_icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(this.imageViewBankIcon);


        if ("2.".equals(payoffPlanVo.getPlanSts())) {
            imageViewPaySuccess.setVisibility(View.VISIBLE);
        } else {
            imageViewPaySuccess.setVisibility(View.GONE);

        }

        //取消按钮
        if ("1".equals(payoffPlanVo.getPlanSts()) || "0".equals(payoffPlanVo.getPlanSts())) {
            buttonCancel.setVisibility(View.VISIBLE);
        } else {
            buttonCancel.setVisibility(View.GONE);
        }

        //发送接口请求获取数据
        sendQueryPayoffPlanDetailRequest(payoffPlanVo.getPlanId());

    }


    /**
     * 发送获取还款计划详情的接口请求
     *
     * @param planId
     */
    private void sendQueryPayoffPlanDetailRequest(String planId) {
        QueryPayoffPlanDetailReqData reqData = new QueryPayoffPlanDetailReqData();
        reqData.setPlanId(planId);
        BaseReq baseReq = new BaseReq(Global.key_queryPayoffPlanDetail, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    /**
     * 发送取消还款计划的请求
     *
     * @param planId
     */
    private void sendCancelPayoffPlanRequest(String planId) {
        CancelPayoffPlanReqData reqData = new CancelPayoffPlanReqData();
        reqData.setPlanId(planId);
        BaseReq baseReq = new BaseReq(Global.key_cancelPayoffPlan, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);

    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryPayoffPlanDetail.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                if (responseBean != null) {
                    QueryPayoffPlanDetailResp resp = (QueryPayoffPlanDetailResp) responseBean;
                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, resp);
                }
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        } else if (Global.key_cancelPayoffPlan.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
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
            QueryPayoffPlanDetailResp resp = (QueryPayoffPlanDetailResp) args[0];
            cancelProgressDialog();
            showPayoffPlanDetailUi(resp);
        } else if (CALL_QUERY_FAILD == callID) {
            cancelProgressDialog();
            showToastText(((BaseResp) args[0]).getRespMsg());
        } else if (CALL_UPLOAD_SUCCESS == callID) {
            buttonCancel.setVisibility(View.GONE);
            payoffPlanVo.setPlanSts("3");
            OkDialog okDialog = new OkDialog(this, "提示", "取消还款计划成功", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }, null, null);
            okDialog.show();
        } else if (CALL_UPLOAD_FAILD == callID) {
            cancelProgressDialog();
            showToastText(((BaseResp) args[0]).getRespMsg());
        }
    }

    /**
     * 刷新界面，显示还款计划详情
     *
     * @param resp
     */
    private void showPayoffPlanDetailUi(QueryPayoffPlanDetailResp resp) {
        if (resp != null) {
            PayoffPlanVo payoffPlanVo = resp.getPayoffPlan();
            List<PayoffPlanDetailVo> payoffPlanDetailList = payoffPlanVo.getPayoffPlanDetailList();
            if (payoffPlanDetailList != null && payoffPlanDetailList.size() > 0) {
                int payoffPlanVoListSize = payoffPlanDetailList.size();
                for (int i = 0; i < payoffPlanVoListSize; i++) {
                    final PayoffPlanDetailVo payoffPlanDetailVo = payoffPlanDetailList.get(i);
                    View view = LayoutInflater.from(this).inflate(R.layout.listitem_payoff_item, null);
                    TextView textViewPayOffDate = (TextView) view.findViewById(R.id.textViewPayOffDate);
                    TextView textViewPayoffAmt = (TextView) view.findViewById(R.id.textViewPayoffAmt);
                    TextView textViewPayoffSts = (TextView) view.findViewById(R.id.textViewPayoffSts);

                    textViewPayOffDate.setText(payoffPlanDetailVo.getPayoffDate());
                    textViewPayoffAmt.setText(payoffPlanDetailVo.getPayoffAmount());
                    textViewPayoffSts.setText(transPayoffSts(payoffPlanDetailVo.getStatus()));

                    //添加内容
                    linearLayoutPayoffDay.addView(view);
                }
            }
        }
    }

    /**
     * 还款状态转化
     * @param status
     * @return
     */
    private String transPayoffSts(String status){
        String retStr = "";
        if("0".equals(status)){
            retStr = "未还款";
        }else if("1".equals(status)){
            retStr = "还款中";
        }else if("2".equals(status)){
            retStr = "已还款";
        }
        return retStr;
    }


}
