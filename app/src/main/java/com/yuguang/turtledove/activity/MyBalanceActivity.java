package com.codido.nymeria.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryMyBalanceResp;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的资产页面
 * 作者：Junjie.Lai on 2016/10/24 23:19
 * 邮箱：bpascal.cn@gmail.com
 */
public class MyBalanceActivity extends BaseActivity {


    @BindView(R.id.imageViewSwitchSeeAmt)
    ImageView imageViewSwitchSeeAmt;
    @BindView(R.id.textViewMyTotalAmt)
    TextView textViewMyTotalAmt;
    @BindView(R.id.textViewAbleBalance)
    TextView textViewAbleBalance;
    @BindView(R.id.textAccountBalance)
    TextView textAccountBalance;
    @BindView(R.id.textViewShareBalance)
    TextView textViewShareBalance;
    @BindView(R.id.textViewHoldBalance)
    TextView textViewHoldBalance;
    @BindView(R.id.textViewHoldAccountBalance)
    TextView textViewHoldAccountBalance;
    @BindView(R.id.textViewHoldShareBalance)
    TextView textViewHoldShareBalance;
    @BindView(R.id.pieChartMyBalance)
    PieChart pieChartMyBalance;

    /**
     * 显示状态
     */
    private String moneyShowSts;

    /**
     * 我的资产信息
     */
    private QueryMyBalanceResp resp;

    @OnClick(R.id.imageViewSwitchSeeAmt)
    void imageViewSwitchSeeAmtEvent() {
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            //显示改隐藏
            textViewMyTotalAmt.setText("--");//总资产
            textViewAbleBalance.setText("--");//总可用
            textAccountBalance.setText("--");//账户余额
            textViewShareBalance.setText("--");//收益余额
            textViewHoldBalance.setText("--");//总冻结
            textViewHoldAccountBalance.setText("--");//冻结账户
            textViewHoldShareBalance.setText("--");//冻结收益

            //状态切换
            moneyShowSts = Global.SHOW_MONEY_STS_HIDE;
            DataKeeper.saveShowMoneySts(MyBalanceActivity.this, Global.SHOW_MONEY_STS_HIDE);
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
        } else {
            //隐藏改显示
            if (resp != null) {
                textViewMyTotalAmt.setText(resp.getTotalBalance());//总资产
                textViewAbleBalance.setText(resp.getTotalAbleBal());//总可用
                textAccountBalance.setText(resp.getAbleCashBal());//账户余额
                textViewShareBalance.setText(resp.getAbleEarnBal());//收益余额
                textViewHoldBalance.setText(resp.getTotalHoldBal());//总冻结
                textViewHoldAccountBalance.setText(resp.getHoldCashBal());//冻结账户
                textViewHoldShareBalance.setText(resp.getHoldEarnBal());//冻结收益
            } else {
                textViewMyTotalAmt.setText("--");//总资产
                textViewAbleBalance.setText("--");//总可用
                textAccountBalance.setText("--");//账户余额
                textViewShareBalance.setText("--");//收益余额
                textViewHoldBalance.setText("--");//总冻结
                textViewHoldAccountBalance.setText("--");//冻结账户
                textViewHoldShareBalance.setText("--");//冻结收益
            }
            moneyShowSts = Global.SHOW_MONEY_STS_SHOW;
            DataKeeper.saveShowMoneySts(MyBalanceActivity.this, Global.SHOW_MONEY_STS_SHOW);
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_balance;
    }

    @Override
    public void addAction() {
        addBackAction();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        moneyShowSts = DataKeeper.getShowMoneySts(this);
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            //显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_open);
        } else {
            //不显示
            imageViewSwitchSeeAmt.setImageResource(R.mipmap.more_icon_eyes_close);
        }

        sendQueryMyBalanceRequest();
    }

    /**
     * 发送查询我的资产结构的请求
     */
    private void sendQueryMyBalanceRequest() {
        BaseReq baseReq = new BaseReq(Global.key_queryMyBalance, new BaseReqData());
        ProcessManager.getInstance().addProcess(this, baseReq, this);

    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryMyBalance.equals(responseBean.getKey())) {
            //我的资产信息列表接口响应
            if (responseBean.isOk()) {
                resp = (QueryMyBalanceResp) responseBean;
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
            updateMyBalanceUi(resp);
        } else if (CALL_QUERY_FAILD == callID) {
            BaseResp resp = (BaseResp) args[0];
            showToastText("获取资产信息失败:" + resp.getRespMsg());
        }
    }

    /**
     * 更新界面信息
     *
     * @param resp
     */
    private void updateMyBalanceUi(QueryMyBalanceResp resp) {
        if (Global.SHOW_MONEY_STS_SHOW.equals(moneyShowSts)) {
            if (resp != null) {
                textViewMyTotalAmt.setText(resp.getTotalBalance());//总资产
                textViewAbleBalance.setText(resp.getTotalAbleBal());//总可用
                textAccountBalance.setText(resp.getAbleCashBal());//账户余额
                textViewShareBalance.setText(resp.getAbleEarnBal());//收益余额
                textViewHoldBalance.setText(resp.getTotalHoldBal());//总冻结
                textViewHoldAccountBalance.setText(resp.getHoldCashBal());//冻结账户
                textViewHoldShareBalance.setText(resp.getHoldEarnBal());//冻结收益
                //显示饼图
                showChart(pieChartMyBalance, getPieData(2, resp));
            } else {
                textViewMyTotalAmt.setText("--");//总资产
                textViewAbleBalance.setText("--");//总可用
                textAccountBalance.setText("--");//账户余额
                textViewShareBalance.setText("--");//收益余额
                textViewHoldBalance.setText("--");//总冻结
                textViewHoldAccountBalance.setText("--");//冻结账户
                textViewHoldShareBalance.setText("--");//冻结收益
            }
        } else {
            textViewMyTotalAmt.setText("--");//总资产
            textViewAbleBalance.setText("--");//总可用
            textAccountBalance.setText("--");//账户余额
            textViewShareBalance.setText("--");//收益余额
            textViewHoldBalance.setText("--");//总冻结
            textViewHoldAccountBalance.setText("--");//冻结账户
            textViewHoldShareBalance.setText("--");//冻结收益
        }


    }

    /**
     * 显示饼图数据
     *
     * @param pieChart
     * @param pieData
     */
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(100f);  //半径
        pieChart.setTransparentCircleRadius(0f); // 半透明圈
        pieChart.setHoleRadius(0);  //实心圆
        pieChart.setDescription("");
        pieChart.setDrawSliceText(false);//不显示文字
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(false);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        // 初始旋转角度
        pieChart.setRotationAngle(90);

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(false);  //显示成百分比

        pieChart.setCenterText("");  //饼状图中间的文字
        pieChart.setDescriptionTextSize(16f);
        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);  //显示位置
        mLegend.setTextColor(R.color.white);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(750, 750);  //设置动画
    }


    /**
     * 饼图数据生成
     *
     * @param count 份数
     * @return
     */
    private PieData getPieData(int count, QueryMyBalanceResp resp) {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图整理
        for (int i = 0; i < count; i++) {
            String quarterlyName = "";
            float quarterly = 0;
            if (i == 0) {
                quarterlyName = "可用余额";
                quarterly = Float.valueOf(resp.getAbleCashBal() == null ? "0" : resp.getAbleCashBal())+Float.valueOf(resp.getAbleEarnBal() == null ? "0" : resp.getAbleEarnBal());
            } else if (i == 1) {
                quarterlyName = "冻结资金";
                quarterly = Float.valueOf(resp.getHoldCashBal() == null ? "0" : resp.getHoldCashBal())+Float.valueOf(resp.getHoldEarnBal() == null ? "0" : resp.getHoldEarnBal());
            }
            xValues.add(quarterlyName);  //饼块上文案显示

            yValues.add(new Entry(quarterly, 0));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "我的资产"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));

        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);//值不显示

        //DisplayMetrics metrics = getResources().getDisplayMetrics();
        //float px = 5 * (metrics.densityDpi / 160f);
        //pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
}
