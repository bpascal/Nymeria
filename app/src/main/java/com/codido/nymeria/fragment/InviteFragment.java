package com.codido.nymeria.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.activity.InviteListActivity;
import com.codido.nymeria.activity.QrcodeActivity;
import com.codido.nymeria.adapter.InviteeListAdapter;
import com.codido.nymeria.adapter.holder.InviteeItemHolder;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.GetShareParamReqData;
import com.codido.nymeria.bean.req.QueryMyInviteeListReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.GetShareParamResp;
import com.codido.nymeria.bean.resp.QueryMyInviteeListResp;
import com.codido.nymeria.bean.vo.InviteeVo;
import com.codido.nymeria.dialog.DropdownDialog;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.view.MoreListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的分享
 * 作者：Junjie.Lai on 2016/12/18 22:42
 * 邮箱：laijj@yzmm365.cn
 */
public class InviteFragment extends BaseFragment {

    /**
     * 微信分享api
     */
    private IWXAPI wxApi;

    /**
     * 微信分享参数公共常量
     */
    private GetShareParamResp shareParamResp;

    /**
     * 分享图片
     */
    private Bitmap thumb;

    /**
     * 获取模块列表成功的常量
     */
    private static final int CALL_QUERY_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 获取模块列表失败的常量
     */
    private static final int CALL_QUERY_FAILD = BaseActivity.FIRST_VAL++;

    /**
     * 获取微信参数成功的常量
     */
    private static final int CALL_GETSHARE_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 获取微信参数失败的常量
     */
    private static final int CALL_GETSHARE_FAILD = BaseActivity.FIRST_VAL++;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.moreListView)
    MoreListView moreListView;
    //@BindView(R.id.textViewChildNum)
    TextView textViewChildNum;
    //@BindView(R.id.textViewTotalNum)
    TextView textViewTotalNum;
    //@BindView(R.id.textViewOtherNum)
    TextView textViewOtherNum;


    /**
     * 弹出下拉列表对话框
     */
    @OnClick(R.id.viewMore)
    void showManageListDialog() {
        DropdownDialog dropDownDialog = new DropdownDialog((BaseActivity) getActivity(), new String[]
                {"邀请二维码", "分享给微信好友", "分享到朋友圈"}, null,
                new DropdownDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index) {
                        if (index == 0) {
                            if(shareParamResp != null){
                                gotoQrcodeActivity();
                            }else{
                                showToastText("请稍后");
                            }
                        } else if (index == 1) {
                            if (shareParamResp != null) {
                                wechatShare(0, shareParamResp.getShareUrl(), shareParamResp.getShareTitle(), shareParamResp.getShareContent());
                            } else {
                                showToastText("暂未生成分享地址，请稍后重试");
                            }
                        } else if (index == 2) {
                            if (shareParamResp != null) {
                                wechatShare(1, shareParamResp.getShareUrl(), shareParamResp.getShareTitle(), shareParamResp.getShareContent());
                            } else {
                                showToastText("暂未生成分享地址，请稍后重试");
                            }
                        }
                    }
                });
        dropDownDialog.show();
    }

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
     * 页码
     */
    private int reqPageNo;

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
                reqPageNo = 1;
                sentQueryMyInviteeListRequest(reqPageNo, Global.userId);//发请求做查询
                refreshLayout.setRefreshing(true);
            }
        });
        moreListView.setOnRefreshListener(new MoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                reqPageNo = pageNo + 1;
                sentQueryMyInviteeListRequest(reqPageNo, Global.userId);//发请求做查询
            }
        });
        //列表点击事件
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入后续页面
                if (position > 1) {
                    //顶部不响应
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
        Intent intent = new Intent(getActivity(), InviteListActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }


    /**
     * 进入邀请页面
     */
    private void gotoQrcodeActivity() {
        Intent intent = new Intent(getActivity(), QrcodeActivity.class);
        intent.putExtra("shareParamResp", shareParamResp);
        startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //实例化微信分享API
        wxApi = WXAPIFactory.createWXAPI(getmActivity(), Global.WX_APP_ID);
        wxApi.registerApp(Global.WX_APP_ID);

        //接口参数
        pageNo = 1;//下标从1开始
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));

        //添加头部列表
        headView = LayoutInflater.from(getContext()).inflate(R.layout.head_invite, null);
        textViewChildNum = (TextView) headView.findViewById(R.id.textViewChildNum);
        textViewTotalNum = (TextView) headView.findViewById(R.id.textViewTotalNum);
        textViewOtherNum = (TextView) headView.findViewById(R.id.textViewOtherNum);
        moreListView.addHeaderView(headView);

        //初始化列表
        inviteeListAdapter = new InviteeListAdapter(getmActivity(), moreListView, InviteeItemHolder.class);


        moreListView.setPullRefreshFlag(false);
        moreListView.setDownRefreshFlag(false);
        moreListView.setAdapter(inviteeListAdapter);
        sentQueryMyInviteeListRequest(pageNo, Global.userId);//发请求做查询

        //获取微信分享参数
        sendGetShareParamRequest();
    }


    /**
     * 获取微信参数
     */
    private void sendGetShareParamRequest() {
        BaseReq baseReq = new BaseReq(Global.key_getShareParam, new GetShareParamReqData());
        ProcessManager.getInstance().addProcess(getContext(), baseReq, this);
    }

    /**
     * 查询我的邀请列表请求方法
     */
    private void sentQueryMyInviteeListRequest(int pageNo, String inviterId) {
        QueryMyInviteeListReqData reqData = new QueryMyInviteeListReqData();
        reqData.setInviterId(inviterId);
        reqData.setPageNo(pageNo + "");

        BaseReq baseReq = new BaseReq(Global.key_queryMyInviteeList, reqData);
        ProcessManager.getInstance().addProcess(getContext(), baseReq, this);
    }

    @Override
    protected boolean callBack(BaseResp responseBean) {
        if (Global.key_queryMyInviteeList.equals(responseBean.getKey())) {
            //获取邀请列表的接口
            if (responseBean.isOk()) {
                QueryMyInviteeListResp resp = (QueryMyInviteeListResp) responseBean;

                childrenNumStr = resp.getChildrenNum();
                otherNumStr = resp.getOtherNum();
                if (inviteeVoList == null) {
                    inviteeVoList = new ArrayList<>();
                }

                if (reqPageNo == 1) {
                    if (resp.getInviteeList() != null && resp.getInviteeList().size() > 0) {
                        inviteeVoList = resp.getInviteeList();
                        pageNo = 1;
                    }
                } else {
                    if (resp.getInviteeList() != null && resp.getInviteeList().size() > 0) {

                        inviteeVoList.addAll(resp.getInviteeList());

                        pageNo = reqPageNo + 1;
                    }
                }

                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);

            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        } else if (Global.key_getShareParam.equals(responseBean.getKey())) {
            //获取微信分享参数接口
            if (responseBean.isOk()) {
                shareParamResp = (GetShareParamResp) responseBean;
                thumb = returnBitmap(shareParamResp.getShareImageUrl());
                runCallFunctionInHandler(CALL_GETSHARE_SUCCESS, responseBean);
            } else {
                shareParamResp = null;
                runCallFunctionInHandler(CALL_GETSHARE_FAILD);
            }
        }
        return false;
    }


    @Override
    protected void call(int id, Object... args) {
        if (CALL_QUERY_SUCCESS == id) {
            //获取邀请列表获取成功，更新界面
            updateInviteeInfoUi();
        } else if (CALL_QUERY_FAILD == id) {
            refreshLayout.setRefreshing(false);
            moreListView.onLoadMoreComplete();
        }
    }

    /**
     * 更新界面内容
     */
    private void updateInviteeInfoUi() {
        if(reqPageNo <=1){
            updateTopUserInfoUi();//更新顶部
        }
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

    @Override
    public void onSelected() {

    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
     *
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag, String url, String title, String content) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        //这里替换一张自己工程里的图片资源

        //Bitmap thumb = BitmapFactory.decodeResource(getResources(), shareParamResp.getShareImageUrl());
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }


    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}
