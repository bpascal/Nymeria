package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.adapter.InviteeListAdapter;
import com.codido.nymeria.adapter.holder.InviteeItemHolder;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.QueryMyInviteeListReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryMyInviteeListResp;
import com.codido.nymeria.bean.vo.InviteeVo;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.view.MoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 作者：Junjie.Lai on 2017/11/9 21:06
 * 邮箱：laijj@yzmm365.cn
 */

public class InviteListActivity extends BaseActivity {

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.moreListView)
    MoreListView moreListView;
    TextView textViewChildNum;
    TextView textViewTotalNum;
    TextView textViewOtherNum;

    @BindView(R.id.viewMore)
    View viewMore;

    /**
     * 顶部view
     */
    private View headView;

    /**
     * 列表的adapter
     */
    private InviteeListAdapter inviteeListAdapter;

    /**
     * 页码
     */
    private int pageNo;

    /**
     * 直推人数
     */
    private String childrenNumStr;

    /**
     * 其他人数
     */
    private String otherNumStr;

    /**
     * 推荐信息列表
     */
    private List<InviteeVo> inviteeVoList;

    /**
     * 用户ID
     */
    private String currentUserId;

    @Override
    protected int getContentView() {
        return R.layout.fragment_invite;
    }

    @Override
    protected void addAction() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重置pageNo
                pageNo = 1;
                inviteeVoList.clear();
                inviteeListAdapter.clear();
                sentQueryMyInviteeListRequest(pageNo, currentUserId);//发请求做查询
                refreshLayout.setRefreshing(true);
            }
        });
        moreListView.setOnRefreshListener(new MoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                sentQueryMyInviteeListRequest(pageNo, currentUserId);//发请求做查询
            }
        });
        //列表点击事件
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入后续页面
                if(position>1){
                    InviteeVo inviteeVo = inviteeVoList.get(position - 2);
                    if (Integer.valueOf(inviteeVo.getChildrenNum() == null ? "0" : inviteeVo.getChildrenNum()) > 0) {
                        //如果没有下级，就点不进去
                        gotoInviteActivity(inviteeVo.getInviteeId());
                    }
                }
            }
        });
    }

    /**
     * 进入邀请列表页面
     */
    private void gotoInviteActivity(String userId) {
        Intent intent = new Intent(this, InviteListActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //获取用户ID
        currentUserId = getIntent().getStringExtra("userId");

        pageNo = 1;//下标从1开始
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));

        //添加头部列表
        headView = LayoutInflater.from(this).inflate(R.layout.head_invite, null);
        textViewChildNum = (TextView) headView.findViewById(R.id.textViewChildNum);
        textViewTotalNum = (TextView) headView.findViewById(R.id.textViewTotalNum);
        textViewOtherNum = (TextView) headView.findViewById(R.id.textViewOtherNum);
        moreListView.addHeaderView(headView);

        //初始化列表
        inviteeListAdapter = new InviteeListAdapter(this, moreListView, InviteeItemHolder.class);


        moreListView.setPullRefreshFlag(false);
        moreListView.setDownRefreshFlag(false);
        moreListView.setAdapter(inviteeListAdapter);
        sentQueryMyInviteeListRequest(pageNo, currentUserId);//发请求做查询

        viewMore.setVisibility(View.GONE );
    }


    /**
     * 查询我的邀请列表请求方法
     */
    private void sentQueryMyInviteeListRequest(int pageNo, String inviterId) {
        QueryMyInviteeListReqData reqData = new QueryMyInviteeListReqData();
        reqData.setInviterId(inviterId);
        reqData.setPageNo(pageNo + "");

        BaseReq baseReq = new BaseReq(Global.key_queryMyInviteeList, reqData);
        ProcessManager.getInstance().addProcess(this, baseReq, this);
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        if (Global.key_queryMyInviteeList.equals(responseBean.getKey())) {
            //获取邀请列表的接口
            if (responseBean.isOk()) {
                QueryMyInviteeListResp resp = (QueryMyInviteeListResp) responseBean;
                if (resp != null) {
                    childrenNumStr = resp.getChildrenNum();
                    otherNumStr = resp.getOtherNum();
                    if (inviteeVoList == null || inviteeVoList.size() == 0) {
                        inviteeVoList = new ArrayList<>();
                    }
                    if (resp.getInviteeList() != null && resp.getInviteeList().size() > 0) {
                        pageNo++;
                        inviteeVoList.addAll(resp.getInviteeList());
                    }
                    runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
                }
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        }
        return false;
    }

    @Override
    public void call(int callID, Object... args) {
        if (CALL_QUERY_SUCCESS == callID) {
            //获取邀请列表获取成功，更新界面
            updateInviteeInfoUi();
        } else if (CALL_QUERY_FAILD == callID) {
            refreshLayout.setRefreshing(false);
            moreListView.onLoadMoreComplete();
        }
    }

    /**
     * 更新界面内容
     */
    private void updateInviteeInfoUi() {
        //if(pageNo <= 1){
            updateTopUserInfoUi();//更新顶部
        //}
        updateInviteeListUi();//更新列表
    }

    /**
     * 更新顶部用户人数信息
     */
    private void updateTopUserInfoUi() {
        textViewChildNum.setText(childrenNumStr);
        textViewOtherNum.setText(otherNumStr);
        textViewTotalNum.setText((Integer.valueOf(childrenNumStr) + Integer.valueOf(otherNumStr)) + "");

    }

    /**
     * 更新列表信息
     */
    private void updateInviteeListUi() {
        //展现列表
        inviteeListAdapter.setItemList(inviteeVoList);
        inviteeListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        moreListView.onLoadMoreComplete();
    }
}
