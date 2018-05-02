package com.codido.nymeria.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.codido.nymeria.R;
import com.codido.nymeria.adapter.TxnOrderListAdapter;
import com.codido.nymeria.adapter.holder.TxnOrderItemHolder;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.QueryTxnOrderListReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryTxnOrderListResp;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.bean.vo.TxnOrderVo;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.view.MoreListView;
import com.codido.nymeria.view.TabHeaderTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易流水页面
 * 作者：Junjie.Lai on 2017/11/9 22:42
 * 邮箱：laijj@yzmm365.cn
 */
public class TxnOrderListActivity extends BaseActivity {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.moreListView)
    MoreListView moreListView;

    @BindView(R.id.tabHeader)
    TabHeaderTextView tabHeader;


    /**
     * adapter
     */
    private TxnOrderListAdapter txnOrderListAdapter;

    /**
     * 卡号
     */
    private CardVo cardVo;

    /**
     * 页码
     */
    private String maxId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 交易订单列表
     */
    private List<TxnOrderVo> txnOrderVoList;

    @Override
    protected int getContentView() {
        return R.layout.activity_txnorderlist;
    }

    @Override
    protected void addAction() {

        addBackAction();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重置maxId
                maxId = null;
                sentQueryTxnOrderListRequest(cardVo == null ? null : cardVo.getCardId(),maxId, orderType);//发请求做查询
                refreshLayout.setRefreshing(true);
            }
        });
        moreListView.setOnRefreshListener(new MoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                sentQueryTxnOrderListRequest(cardVo == null ? null : cardVo.getCardId(),maxId, orderType);//发请求做查询
            }
        });


    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        maxId = null;//下标从1开始
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        //卡对象
        cardVo = (CardVo) getIntent().getSerializableExtra("cardVo");
        //初始化列表
        txnOrderVoList = new ArrayList<>();
        txnOrderListAdapter = new TxnOrderListAdapter(this, moreListView, TxnOrderItemHolder.class);
        moreListView.setPullRefreshFlag(false);
        moreListView.setAdapter(txnOrderListAdapter);
        sentQueryTxnOrderListRequest(cardVo == null ? null : cardVo.getCardId(), maxId, orderType);//发请求做查询


        ArrayList<String> types = new ArrayList<>();
        types.add("全部");
        types.add("充值");
        types.add("提现");
        types.add("还款");
        types.add("扣款");
        types.add("其他");
        tabHeader.setTabs(types);


        tabHeader.setOnTabSelecetedListener(new TabHeaderTextView.OnTabSelecetedListener() {
            @Override
            public void onTabSelected(int index) {

                orderType = Constants.ORDER_TYPES_KEYS[index];
                maxId = null;
                txnOrderListAdapter.clear();
                sentQueryTxnOrderListRequest(cardVo == null ? null : cardVo.getCardId(), maxId, orderType);
            }
        });
    }

    /**
     * 发送获取流水请求
     *
     * @param maxId
     * @param orderType
     */
    private void sentQueryTxnOrderListRequest(String cardId, String maxId, String orderType) {
        QueryTxnOrderListReqData reqData = new QueryTxnOrderListReqData();
        reqData.setCardId(cardId);
        reqData.setOrderType(orderType);
        reqData.setMaxId(maxId);
        BaseReq baseReq = new BaseReq(Global.key_queryTxnOrderList, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryTxnOrderList.equals(responseBean.getKey())) {
            //获取订单列表接口响应
            if (responseBean.isOk()) {
                QueryTxnOrderListResp resp = (QueryTxnOrderListResp) responseBean;
                if (resp != null) {
                    if (maxId == null) {
                        txnOrderVoList = resp.getTxnOrderList();
                    } else {
                        txnOrderVoList.addAll(resp.getTxnOrderList());
                    }
                    maxId = resp.getMaxId();

                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
                }
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
            txnOrderListAdapter.setItemList(txnOrderVoList);
            txnOrderListAdapter.notifyDataSetChanged();
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
