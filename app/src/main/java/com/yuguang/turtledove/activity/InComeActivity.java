package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.adapter.InComeAdapter;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryMyEarnResp;
import com.codido.nymeria.bean.vo.DayEarnVo;
import com.codido.nymeria.bean.vo.MonthEarnVo;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收益页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class InComeActivity extends BaseActivity {

    @BindView(R.id.listViewIncome)
    ExpandableListView listViewIncome;

    TextView textViewTotalEranAmt;//总收益
    TextView textViewAbleEran;//可提收益
    TextView textViewScore;//积分
    TextView textViewWithdrawHold;//提现冻结
    TextView textViewWithdrawedEran;//已提收益
    ImageView imageViewSwitchSeeAmt;//显示图标

    @BindView(R.id.buttonTake)
    Button buttonTake;

    /**
     * 收益列表
     */
    private InComeAdapter inComeAdapter;

    /**
     * 显示状态
     */
    private String moneyShowSts;

    /**
     * 我的收益对象
     */
    private QueryMyEarnResp resp;

    @OnClick(R.id.buttonTake)
    void buttonTakeClickEvent(){
        gotoWithdrawActivity();
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_income;
    }

    @Override
    public void addAction() {

        addBackAction();
        imageViewSwitchSeeAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
                    //显示改隐藏
                    textViewTotalEranAmt.setText("--");
                    textViewAbleEran.setText("--");
                    textViewScore.setText("--");
                    textViewWithdrawHold.setText("--");
                    textViewWithdrawedEran.setText("--");
                    moneyShowSts = Global.SHOW_MONEY_STS_HIDE;
                    DataKeeper.saveShowMoneySts(InComeActivity.this, Global.SHOW_MONEY_STS_HIDE);
                    imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
                } else {
                    //隐藏改显示
                    if (resp != null) {
                        textViewTotalEranAmt.setText(resp.getAccuEarnBalance());
                        textViewAbleEran.setText(resp.getAbleEarnBal());
                        textViewScore.setText(resp.getPointBal());
                        textViewWithdrawHold.setText(resp.getHoldEarnBal());
                        textViewWithdrawedEran.setText(resp.getWithdrawedEarnBal());
                    } else {
                        textViewTotalEranAmt.setText("--");
                        textViewAbleEran.setText("--");
                        textViewScore.setText("--");
                        textViewWithdrawHold.setText("--");
                        textViewWithdrawedEran.setText("--");
                    }
                    moneyShowSts = Global.SHOW_MONEY_STS_SHOW;
                    DataKeeper.saveShowMoneySts(InComeActivity.this, Global.SHOW_MONEY_STS_SHOW);
                    imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
                }
            }
        });
    }



    @Override
    protected void initData(Bundle savedInstanceState) {
        View headView = LayoutInflater.from(this).inflate(R.layout.activity_income_header, null);
        listViewIncome.addHeaderView(headView);

        textViewTotalEranAmt = (TextView) headView.findViewById(R.id.textViewTotalEranAmt);
        textViewAbleEran = (TextView) headView.findViewById(R.id.textViewAbleEran);
        textViewScore = (TextView) headView.findViewById(R.id.textViewScore);
        textViewWithdrawHold = (TextView) headView.findViewById(R.id.textViewWithdrawHold);
        textViewWithdrawedEran = (TextView) headView.findViewById(R.id.textViewWithdrawedEran);
        imageViewSwitchSeeAmt = (ImageView) headView.findViewById(R.id.imageViewSwitchSeeAmt);


        inComeAdapter = new InComeAdapter(this);

        listViewIncome.setGroupIndicator(null);
        listViewIncome.setAdapter(inComeAdapter);

        listViewIncome.expandGroup(0);

        moneyShowSts = DataKeeper.getShowMoneySts(this);
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            //显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
        } else {
            //不显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
        }

        //发送接口请求获取我的收益列表
        sendQueryMyEarnListRequest();
    }


    /**
     * 查询我的收益请求
     */
    private void sendQueryMyEarnListRequest() {
        BaseReq baseReq = new BaseReq(Global.key_queryMyEarn, new BaseReqData());
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }


    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryMyEarn.equals(responseBean.getKey())) {
            //我的收益列表接口响应
            if (responseBean.isOk()) {
                QueryMyEarnResp resp = (QueryMyEarnResp) responseBean;
                if (resp != null) {
                    float maxNum = 0;
                    List<MonthEarnVo> monthEarnVoList = resp.getMonthEarnList();
                    if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
                        int monthEarnVoListSize = monthEarnVoList.size();
                        //第一次循环，取得最大值
                        for (int i = 0; i < monthEarnVoListSize; i++) {
                            MonthEarnVo monthEarnVo = monthEarnVoList.get(i);
                            List<DayEarnVo> dayEarnVoList = monthEarnVo.getDayEarnList();
                            if (dayEarnVoList != null && dayEarnVoList.size() > 0) {
                                for (int j = 0; j < dayEarnVoList.size(); j++) {
                                    DayEarnVo dayEarnVo = dayEarnVoList.get(j);
                                    float currentEarn = Float.valueOf(dayEarnVo.getDayTotalEarn());
                                    if (currentEarn > maxNum) {
                                        maxNum = currentEarn;
                                    }
                                }
                            }
                        }
                        //第二次循环，取每个点的进度
                        for (int i = 0; i < monthEarnVoListSize; i++) {
                            MonthEarnVo monthEarnVo = monthEarnVoList.get(i);
                            List<DayEarnVo> dayEarnVoList = monthEarnVo.getDayEarnList();
                            if (dayEarnVoList != null && dayEarnVoList.size() > 0) {
                                for (int j = 0; j < dayEarnVoList.size(); j++) {
                                    DayEarnVo dayEarnVo = dayEarnVoList.get(j);
                                    float currentEarn = Float.valueOf(dayEarnVo.getDayTotalEarn());
                                    dayEarnVo.setProgressNum(currentEarn * 100 / (maxNum == 0 ? 1 : maxNum));
                                }
                            }
                        }
                    }
                }
                runCallFunctionInHandler(CALL_QUERY_SUCCESS, resp);
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            //获取数据成功成功，更新界面
            resp = (QueryMyEarnResp) args[0];
            updateMyEarnUi(resp);
        } else if (CALL_QUERY_FAILD == callID) {
            BaseResp resp = (BaseResp) args[0];
            showToastText("获取资产信息失败:" + resp.getRespMsg());
        }
    }

    /**
     * 显示我的收益列表
     *
     * @param resp
     */
    private void updateMyEarnUi(QueryMyEarnResp resp) {
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            if (resp != null) {
                textViewTotalEranAmt.setText(resp.getAccuEarnBalance());
                textViewAbleEran.setText(resp.getAbleEarnBal());
                textViewScore.setText(resp.getPointBal());
                textViewWithdrawHold.setText(resp.getHoldEarnBal());
                textViewWithdrawedEran.setText(resp.getWithdrawedEarnBal());
            } else {
                textViewTotalEranAmt.setText("--");
                textViewAbleEran.setText("--");
                textViewScore.setText("--");
                textViewWithdrawHold.setText("--");
                textViewWithdrawedEran.setText("--");
            }
        } else {
            textViewTotalEranAmt.setText("--");
            textViewAbleEran.setText("--");
            textViewScore.setText("--");
            textViewWithdrawHold.setText("--");
            textViewWithdrawedEran.setText("--");
        }

        inComeAdapter.setMonthEarnVoList(resp.getMonthEarnList());
        inComeAdapter.notifyDataSetChanged();

        //默认全部展开
        List<MonthEarnVo> monthEarnVoList = resp.getMonthEarnList();
        if (monthEarnVoList != null && monthEarnVoList.size() > 0) {
            int monthEarnVoListSize = monthEarnVoList.size();
            for (int i = 0; i < monthEarnVoListSize; i++) {
                listViewIncome.expandGroup(i);
            }
        }
    }

    /**
     * 跳转提现页面
     */
    private void gotoWithdrawActivity() {
        Intent intent = new Intent(this, WithdrawActivity.class);
        intent.putExtra("withdrawType",Global.WITHDRAW_TYPE_SHARE);
        startActivity(intent);
    }
}
