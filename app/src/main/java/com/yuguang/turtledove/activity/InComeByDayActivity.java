package com.codido.nymeria.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.codido.nymeria.R;
import com.codido.nymeria.adapter.EarnListAdapter;
import com.codido.nymeria.adapter.holder.EarnVoItemHolder;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.QueryEarnByDayReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryEarnByDayResp;
import com.codido.nymeria.bean.vo.DayEarnVo;
import com.codido.nymeria.bean.vo.EarnVo;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.view.MoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易流水页面
 * 作者：Junjie.Lai on 2017/11/9 22:42
 * 邮箱：laijj@yzmm365.cn
 */
public class InComeByDayActivity extends BaseActivity {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.moreListView)
    MoreListView moreListView;

    /**
     * adapter
     */
    private EarnListAdapter earnListAdapter;


    /**
     *
     */
    private DayEarnVo dayEarnVo;

    /**
     * 页码
     */
    private String maxId;


    /**
     * 交易订单列表
     */
    private List<EarnVo> earnVoList;


    public static void start(Context context, DayEarnVo dayEarnVo) {

        Intent intent = new Intent(context, InComeByDayActivity.class);
        intent.putExtra("dayEarnVo", dayEarnVo);

        context.startActivity(
                intent
        );
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_income_by_day;
    }

    @Override
    protected void addAction() {

        addBackAction();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重置maxId
                maxId = null;
                sentQueryTxnOrderListRequest(null);//发请求做查询
                refreshLayout.setRefreshing(true);
            }
        });
        moreListView.setOnRefreshListener(new MoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                sentQueryTxnOrderListRequest(maxId);//发请求做查询
            }
        });


    }


    @Override
    protected void initData(Bundle savedInstanceState) {

        dayEarnVo = (DayEarnVo) getIntent().getSerializableExtra("dayEarnVo");
        maxId = null;//下标从1开始
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        //初始化列表
        earnVoList = new ArrayList<>();
        earnListAdapter = new EarnListAdapter(this, moreListView, EarnVoItemHolder.class);
        moreListView.setPullRefreshFlag(false);
        moreListView.setAdapter(earnListAdapter);
        sentQueryTxnOrderListRequest(maxId);//发请求做查询

    }

    /**
     * 发送获取流水请求
     *
     * @param maxId
     */
    private void sentQueryTxnOrderListRequest(String maxId) {
        this.maxId = maxId;
        QueryEarnByDayReqData reqData = new QueryEarnByDayReqData();
        reqData.setDay(dayEarnVo.getDay());
        BaseReq baseReq = new BaseReq(Global.key_queryEarnByDay, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryEarnByDay.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {
                QueryEarnByDayResp resp = (QueryEarnByDayResp) responseBean;

                if (maxId == null) {
                    earnVoList = resp.getEarnList();
                } else {
                    earnVoList.addAll(resp.getEarnList());
                }

                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);

            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD, responseBean);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            //获取邀请列表获取成功，更新界面
            earnListAdapter.setItemList(earnVoList);
            earnListAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
            moreListView.onLoadMoreComplete();
        } else if (CALL_QUERY_FAILD == callID) {
            BaseResp resp = (BaseResp) args[0];
            showToastText("获取交易记录失败:" + resp.getRespMsg());
            refreshLayout.setRefreshing(false);
            moreListView.onLoadMoreComplete();
        }
    }
}
