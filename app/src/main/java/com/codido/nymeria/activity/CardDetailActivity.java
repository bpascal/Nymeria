package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.QueryPayoffPlanListReqData;
import com.codido.nymeria.bean.req.RemoveMyCardReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryPayoffPlanListResp;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.bean.vo.PayoffPlanVo;
import com.codido.nymeria.dialog.DropdownDialog;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 卡详情页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class CardDetailActivity extends BaseActivity {

    //控件初始化
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
    @BindView(R.id.viewAddPayOffPlan)
    LinearLayout viewAddPayOffPlan;
    @BindView(R.id.linearLayoutPayoffPlan)
    LinearLayout linearLayoutPayoffPlan;
    @BindView(R.id.textViewMore)
    TextView textViewMore;
    @BindView(R.id.linearLayoutPayTime)
    LinearLayout linearLayoutPayTime;
    @BindView(R.id.linearLayoutListTitle)
    LinearLayout linearLayoutListTitle;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.linearLayoutEmpty)
    LinearLayout linearLayoutEmpty;

    /**
     * 管理菜单列表
     */
    private String[] managerStr;

    /**
     * 卡基本信息
     */
    private CardVo cardVo;


    @OnClick({R.id.viewAddPayOffPlan, R.id.linearLayoutEmpty})
    void addPayOffPlan() {
        Intent intent = new Intent(this, AddPayOffPlanActivity.class);
        intent.putExtra("cardVo", cardVo);
        startActivityForResult(intent, REQ_FOR_ADD_PAYOFF_PLAN);
    }

    @OnClick(R.id.viewMore)
    void showManagerList() {
        showManageListDialog(managerStr);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_card_detail;
    }

    @Override
    public void addAction() {
        addBackAction();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新获取列表
                linearLayoutPayoffPlan.removeAllViews();
                sendQueryPayoffPlanListRequest(cardVo.getCardId());
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        cardVo = (CardVo) getIntent().getSerializableExtra("cardVo");
        //管理菜单
        if ("1".equals(cardVo.getCardType())) {
            //信用卡
            managerStr = new String[]{"查看流水", "解绑银行卡", "增加还款计划"};
        } else {
            managerStr = new String[]{"查看流水", "解绑银行卡"};
        }
        //显示文字
        textViewBankName.setText(cardVo.getBankName());
        textViewCardType.setText(Constants.CRADS_TYPES.get(cardVo.getCardType()));
        textViewCardNo.setText(cardVo.getCardNo());
        String billDay = cardVo.getBillDay() == null ? "账单日:未设置" : ("账单日:" + cardVo.getBillDay());
        textViewBillDate.setText(billDay);
        String payoffDay = cardVo.getPayoffDay() == null ? "还款日:未设置" : ("还款日:" + cardVo.getPayoffDay());
        textViewPayOffDate.setText(payoffDay);

        //显示图片
        Glide.with(this).load(cardVo.getBankIcon()).dontAnimate().
                placeholder(R.mipmap.bankcard_icon_default).error(R.mipmap.bankcard_icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(this.imageViewBankIcon);

        //借记卡不显示还款相关参数
        if ("1".equals(cardVo.getCardType())) {
            //信用卡
            linearLayoutPayTime.setVisibility(View.VISIBLE);
            linearLayoutListTitle.setVisibility(View.VISIBLE);
        } else {
            //借记卡
            linearLayoutPayTime.setVisibility(View.GONE);
            linearLayoutListTitle.setVisibility(View.GONE);

        }

        //发送接口请求
        sendQueryPayoffPlanListRequest(cardVo.getCardId());


    }


    /**
     * 查询还款计划列表
     */
    private void sendQueryPayoffPlanListRequest(String cardId) {
        QueryPayoffPlanListReqData reqData = new QueryPayoffPlanListReqData();
        reqData.setCardId(cardId);
        BaseReq baseReq = new BaseReq(Global.key_queryPayoffPlanList, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    /**
     * 调用解绑银行卡接口
     */
    private void sendRemoveMyCardReqeust(String cardId) {
        RemoveMyCardReqData reqData = new RemoveMyCardReqData();
        reqData.setCardId(cardId);
        BaseReq baseReq = new BaseReq(Global.key_removeMyCard, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryPayoffPlanList.equals(responseBean.getKey())) {
            //查询还款计划
            if (responseBean.isOk()) {
                if (responseBean != null) {
                    QueryPayoffPlanListResp resp = (QueryPayoffPlanListResp) responseBean;
                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, resp);
                }
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        } else if (Global.key_removeMyCard.equals(responseBean.getKey())) {
            //解绑银行卡
            if (responseBean.isOk()) {
                if (responseBean != null) {
                    runCallFunctionInHandler(CALL_UPLOAD_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_UPLOAD_FAILD, responseBean);
            }
        }

        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_FOR_ADD_PAYOFF_PLAN) {
            if(resultCode==RESULT_OK){
                //先移除原有计划
                linearLayoutEmpty.removeAllViews();
                //添加还款计划后重新查询
                sendQueryPayoffPlanListRequest(cardVo.getCardId());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            QueryPayoffPlanListResp resp = (QueryPayoffPlanListResp) args[0];
            cancelProgressDialog();
            showPayoffPlanListUi(resp);
            swipeContainer.setRefreshing(false);
        } else if (CALL_QUERY_FAILD == callID) {
            cancelProgressDialog();
            showToastText(((BaseResp) args[0]).getRespMsg());
            swipeContainer.setRefreshing(false);
        } else if (CALL_UPLOAD_SUCCESS == callID) {
            //解绑银行卡成功
            showToastText("解绑银行卡成功");
            finish();
        } else if (CALL_UPLOAD_FAILD == callID) {
            //解绑银行卡失败
            BaseResp resp = (BaseResp) args[0];
            showToastText(resp.getRespMsg());
        }
    }

    /**
     * 显示还款计划列表
     *
     * @param resp
     */
    private void showPayoffPlanListUi(final QueryPayoffPlanListResp resp) {
        if (resp != null) {
            List<PayoffPlanVo> payoffPlanVoList = resp.getPayoffPlanList();
            if (payoffPlanVoList != null && payoffPlanVoList.size() > 0) {
                linearLayoutEmpty.setVisibility(View.GONE);
                int payoffPlanVoListSize = payoffPlanVoList.size();
                for (int i = 0; i < payoffPlanVoListSize; i++) {
                    final PayoffPlanVo payoffPlanVo = payoffPlanVoList.get(i);
                    View view = LayoutInflater.from(this).inflate(R.layout.listitem_payoff_plan, null);
                    TextView textViewOrderAmt = (TextView) view.findViewById(R.id.textViewOrderAmt);
                    TextView textViewPayoffMonth = (TextView) view.findViewById(R.id.textViewPayoffMonth);
                    TextView textViewPayoffProgress = (TextView) view.findViewById(R.id.textViewPayoffProgress);
                    ImageView imageViewPayOffSts = (ImageView) view.findViewById(R.id.imageViewPayOffSts);

                    textViewOrderAmt.setText(payoffPlanVo.getPayoffAmount());
                    textViewPayoffMonth.setText(
                            payoffPlanVo.getPayoffMonth().substring(0, 4) + "年" + payoffPlanVo.getPayoffMonth().substring(4, 6) + "月");
                    if ("0".equals(payoffPlanVo.getPlanSts())) {
                        imageViewPayOffSts.setImageResource(R.mipmap.payoff_plan_state_0);
                    } else if ("1".equals(payoffPlanVo.getPlanSts())) {
                        imageViewPayOffSts.setImageResource(R.mipmap.payoff_plan_state_1);
                    } else if ("2".equals(payoffPlanVo.getPlanSts())) {
                        imageViewPayOffSts.setImageResource(R.mipmap.payoff_plan_state_2);
                    } else if ("3".equals(payoffPlanVo.getPlanSts())) {
                        imageViewPayOffSts.setImageResource(R.mipmap.payoff_plan_state_3);
                    }

                    String pro = new BigDecimal(payoffPlanVo.getPayedAmount()).divide(new BigDecimal(payoffPlanVo.getPayoffAmount()),2).multiply(new BigDecimal(100)).setScale(2,
                                    BigDecimal.ROUND_HALF_UP).
                                    toPlainString();

                    textViewPayoffProgress.setText(pro + "%");
                    //添加内容
                    linearLayoutPayoffPlan.addView(view);
                    //点击事件
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoPayOffPlanDetailActivity(payoffPlanVo);
                        }
                    });
                }
            } else {
                if ("1".equals(cardVo.getCardType())) {
                    //信用卡
                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    //借记卡
                    linearLayoutEmpty.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 进入
     *
     * @param payoffPlanVo
     */
    private void gotoPayOffPlanDetailActivity(PayoffPlanVo payoffPlanVo) {
        Intent intent = new Intent(this, PayOffPlanDetailActivity.class);
        intent.putExtra("cardVo", cardVo);
        intent.putExtra("payoffPlanVo", payoffPlanVo);
        startActivity(intent);
    }


    /**
     * 弹出下拉列表对话框
     */
    private void showManageListDialog(final String[] items) {
        if (items == null || items.length == 0) {
            return;
        }
        DropdownDialog dropDownDialog = new DropdownDialog(this, items, null,
                new DropdownDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index) {
                        if (index == 0) {
                            //查看流水
                            gotoTxnOrderListActivity();
                        } else if (index == 1) {
                            //解绑银行卡
                            OkDialog okDialog = new OkDialog(CardDetailActivity.this, "提示", "确定解绑银行卡吗？", "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //确定解绑银行卡
                                    sendRemoveMyCardReqeust(cardVo.getCardId());
                                }
                            }, "取消", null);
                            okDialog.show();
                        } else if (index == 2) {
                            //增加还款计划
                            addPayOffPlan();
                        }
                    }
                });
        dropDownDialog.show();
    }

    /**
     * 跳转流水页面
     */
    private void gotoTxnOrderListActivity() {
        Intent intent = new Intent(this, TxnOrderListActivity.class);
        intent.putExtra("cardVo", cardVo);
        startActivity(intent);
    }
}
