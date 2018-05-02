package com.codido.nymeria.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.AddPayoffPlanReqData;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.AddPayOffPlanResp;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryParameterResp;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.dialog.MonthSelectDialog;
import com.codido.nymeria.dialog.OkDialog;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.ValidateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增还款计划
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class AddPayOffPlanActivity extends BaseActivity {

    /**
     * 开始月份
     */
    static final String[] BEGIN_MONTHS =
            new String[]{
                    "2017-11", "2017-12", "2018-01", "2018-02", "2018-03", "2018-04"
            };

    /**
     * 结束月份
     */
    static final String[] END_MONTHS =
            new String[]{
                    "2017-12", "2018-01", "2018-02", "2018-03", "2018-04", "2018-05", "2018-06", "2018-07", "2018-08", "2018-09", "2018-10"
            };

    private String billDay;
    private String payoffDay;
    private String payoffAmount;
    private String startMonth;
    private String endMonth;

    CardVo cardVo;

    @BindView(R.id.textViewBankName)
    TextView textViewBankName;

    @BindView(R.id.textViewBankCardType)
    TextView textViewBankCardType;


    @BindView(R.id.textViewBankCardNo)
    TextView textViewBankCardNo;

    @BindView(R.id.imageViewBankIcon)
    ImageView imageViewBankIcon;


    @BindView(R.id.textViewAddPayOffTip)
    TextView textViewAddPayOffTip;

    @BindView(R.id.editTextBillDay)
    EditText editTextBillDay;

    @BindView(R.id.editTextPayoffFee)
    EditText editTextPayoffFee;

    @BindView(R.id.editTextDeposit)
    EditText editTextDeposit;

    @BindView(R.id.editTextPayoffDay)
    EditText editTextPayoffDay;

    @BindView(R.id.editTextPayoffAmount)
    EditText editTextPayoffAmount;

    @BindView(R.id.textViewBeginMonth)
    TextView textViewBeginMonth;

    @BindView(R.id.lineayLayoutBeginMonth)
    LinearLayout lineayLayoutBeginMonth;

    private ArrayList<Integer> selecteMonthList;
    private ArrayList<Integer> selectedMonthList;


    @OnClick(R.id.lineayLayoutBeginMonth)
    void chooseBeginMonth() {
        hideInputMethod();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        selecteMonthList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {

            selecteMonthList.add(calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1);
            calendar.add(Calendar.MONTH, 1);

        }

        MonthSelectDialog dialog = new MonthSelectDialog(this, selecteMonthList, selectedMonthList);
        dialog.setOnMonthSelectedListener(new MonthSelectDialog.OnMonthSelectedListener() {
            @Override
            public void onMonthSelected(ArrayList<Integer> list) {
                selectedMonthList = list;
                if (list == null || list.size() == 0) {
                    textViewBeginMonth.setText("请选择");
                } else if (list.size() == 1) {
                    textViewBeginMonth.setText(list.get(0) / 100 + "年" + list.get(0) % 100 + "月");
                } else {
                    textViewBeginMonth.setText(list.get(0) / 100 + "年" + list.get(0) % 100 + "月-" +
                            list.get(list.size() - 1) / 100 + "年" + list.get(list.size() - 1) % 100 + "月");
                }


                cal();
            }
        });
        dialog.show();

    }


    @Override
    protected void initData(Bundle savedInstanceState) {

        //初始化对象
        cardVo = (CardVo) getIntent().getSerializableExtra("cardVo");

        //回填控件
        textViewAddPayOffTip.setText("保证金将在还款成功之后退还，手续费按照实际还款金额万分之85的费率收取");
        textViewBankCardNo.setText(cardVo.getCardNo());
        textViewBankCardType.setText(Constants.CRADS_TYPES.get(cardVo.getCardType()));
        textViewBankName.setText(cardVo.getBankName());
        //填图
        Glide.with(this).load(cardVo.getBankIcon()).dontAnimate().
                placeholder(R.mipmap.bankcard_icon_default).error(R.mipmap.bankcard_icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(this.imageViewBankIcon);

        queryParameter();


        editTextPayoffAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cal();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick(R.id.buttonOk)
    void ok() {

        ValidateUtils.Result result;

        result = ValidateUtils.checkCreditCardDate(billDay = getTextFromEditText(editTextBillDay));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkCreditCardDate(payoffDay = getTextFromEditText(editTextPayoffDay));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        result = ValidateUtils.checkMoney(payoffAmount = getTextFromEditText(editTextPayoffAmount));

        if (!result.isOk()) {
            showToastText(result.getResult());
            return;
        }

        if (selectedMonthList == null || selectedMonthList.size() == 0) {
            showToastText("请选择还款月份");
            return;
        }


        startMonth = selectedMonthList.get(0) + "";
        endMonth = selectedMonthList.get(selectedMonthList.size() - 1) + "";

        showProgressDialogCanCancel("请稍后...", 0);

        AddPayoffPlanReqData reqData = new AddPayoffPlanReqData();
        reqData.setBillDay(billDay);
        reqData.setPayoffDay(payoffDay);
        reqData.setPayoffAmount(payoffAmount);
        reqData.setCardId(cardVo.getCardId());
        reqData.setStartMonth(startMonth);
        reqData.setEndMonth(endMonth);

        BaseReq baseReq = new BaseReq(Global.key_addPayoffPlan, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    QueryParameterResp queryParameterResp = null;

    /**
     *
     */
    public void queryParameter() {
        BaseReq baseReq = new BaseReq(Global.key_queryParameter, new BaseReqData());
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    public void cal() {

        ValidateUtils.Result result = ValidateUtils.checkMoney(payoffAmount = getTextFromEditText(editTextPayoffAmount));

        if (!result.isOk()) {

            return;
        }

        if (selectedMonthList == null || selectedMonthList.size() == 0) {
            return;
        }


        if (queryParameterResp == null) {
            return;
        }

        BigDecimal bMoney = new BigDecimal(payoffAmount);
        BigDecimal bDepositPer = new BigDecimal(queryParameterResp.getDepositPer());
        BigDecimal bPayoffFeePer = new BigDecimal(queryParameterResp.getPayoffFeePer());


        //String deposit = bMoney.multiply(bDepositPer).multiply(new BigDecimal(selectedMonthList.size())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String deposit = bMoney.multiply(bDepositPer).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String payOffFee = bMoney.multiply(bPayoffFeePer).multiply(new BigDecimal(selectedMonthList.size())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();


        setTextToEditText(editTextPayoffFee, payOffFee);
        setTextToEditText(editTextDeposit, deposit);
        Global.debug("总金额：" + bMoney.toString() + "，保证金：" + deposit + "，还款手续费： " + payOffFee);

    }

    /**
     * 进入添加还款计划页面
     *
     * @param content
     * @param cardVo
     */
    public static void start(Activity content, CardVo cardVo) {
        Intent intent = new Intent(content, AddPayOffPlanActivity.class);
        intent.putExtra("cardVo", cardVo);

        content.startActivityForResult(intent, REQ_FOR_ADD_CRAD);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_payoff_plan;
    }

    @Override
    public void addAction() {
        addBackAction();
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_addPayoffPlan.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {
                if (responseBean != null) {
                    runCallFunctionInHandler(CALL_UPLOAD_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_UPLOAD_FAILD, responseBean);
            }
        } else if (Global.key_queryParameter.equals(responseBean.getKey())) {
            if (responseBean.isOk()) {


                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);

                queryParameterResp = (QueryParameterResp) responseBean;

            } else {
                queryParameter();
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_UPLOAD_SUCCESS == callID) {
            cancelProgressDialog();

            setResult(RESULT_OK);
            finish();
        } else if (CALL_UPLOAD_FAILD == callID) {
            cancelProgressDialog();

            final BaseResp baseResp = (BaseResp) args[0];

            if (baseResp.getRespCode().equals("2046")) { //需要去充值

                 new OkDialog(this, "提示", baseResp.getRespMsg(), "马上充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddPayOffPlanActivity.this, RechargeActivity.class);

                        AddPayOffPlanResp resp = (AddPayOffPlanResp) baseResp;
                        intent.putExtra("needRechargeAmount", resp.getNeedRechargeAmount());
                        startActivityForResult(intent, REQ_FOR_RECHARGE);
                    }
                }, "我再想想", null).show();


            } else {
                showToastText(((BaseResp) args[0]).getRespMsg());
            }

        } else if (CALL_QUERY_SUCCESS == callID) {

            BigDecimal bPayoffFeePer = new BigDecimal(queryParameterResp.getPayoffFeePer());

            bPayoffFeePer = bPayoffFeePer.multiply(new BigDecimal(10000));

            textViewAddPayOffTip.setText("保证金将在还款成功之后退还，手续费按照实际还款金额万分之" + bPayoffFeePer.intValue() + "的费率收取。");

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(REQ_FOR_RECHARGE == requestCode) {
            if (resultCode == RESULT_OK) {

                ok();

                return  ;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
