package com.codido.nymeria.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.activity.AddCardActivity;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.activity.CardDetailActivity;
import com.codido.nymeria.activity.WebActivity;
import com.codido.nymeria.bean.req.BaseReq;
import com.codido.nymeria.bean.req.BaseReqData;
import com.codido.nymeria.bean.req.QueryBannerListReqData;
import com.codido.nymeria.bean.req.QueryNoticeReqData;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.bean.resp.QueryBannerListResp;
import com.codido.nymeria.bean.resp.QueryMyCardListResp;
import com.codido.nymeria.bean.resp.QueryNoticeResp;
import com.codido.nymeria.bean.vo.BannerVo;
import com.codido.nymeria.bean.vo.CardVo;
import com.codido.nymeria.bean.vo.NoticeVo;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.DataKeeper;
import com.codido.nymeria.util.Global;
import com.codido.nymeria.util.ProcessManager;
import com.codido.nymeria.util.YUtils;
import com.codido.nymeria.view.BannerViewPager;
import com.codido.nymeria.view.PointView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.codido.nymeria.activity.BaseActivity.REQ_FOR_ADD_CRAD;
import static com.codido.nymeria.activity.BaseActivity.REQ_FOR_CRAD_DETAIL;

/**
 * 首页
 * 作者：Junjie.Lai on 2016/12/18 22:30
 * 邮箱：laijj@yzmm365.cn
 */
public class IndexFragment extends BaseFragment {

    /**
     * 获取图片成功的常量
     */
    private static final int CALL_LOAD_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 获取图片失败的常量
     */
    private static final int CALL_LOAD_FAILD = BaseActivity.FIRST_VAL++;

    /**
     * 获取模块列表成功的常量
     */
    private static final int CALL_QUERY_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 获取模块列表失败的常量
     */
    private static final int CALL_QUERY_FAILD = BaseActivity.FIRST_VAL++;

    /**
     * 5秒钟延时的处理
     */
    private static final int CALL_TIME_FIVESECONDS = BaseActivity.FIRST_VAL++;


    /**
     * 获取公告成功的常量
     */
    private static final int CALL_QUERY_NOTICE_SUCCESS = BaseActivity.FIRST_VAL++;

    /**
     * 获取公告失败的常量
     */
    private static final int CALL_QUERY_NOTICE_FAILED = BaseActivity.FIRST_VAL++;


    /**
     * 公告列表，目前公告只展示第一条
     */
    private List<NoticeVo> noticeList;


    @BindView(R.id.scrollViewContent)
    ScrollView scrollViewContent;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.layoutViewPager)
    LinearLayout layoutViewPager;
    @BindView(R.id.pointView)
    PointView pointView;
    @BindView(R.id.linearEntries)
    LinearLayout linearEntries;

    @BindView(R.id.viewNotice)
    View viewNotice;

    @BindView(R.id.textViewNotice)
    TextView textViewNotice;

    @BindView(R.id.viewAddBankCard)
    View viewAddBankCard;

    @BindView(R.id.linearLayoutEmpty)
    View linearLayoutEmpty;

    /**
     * 广告位浏览
     */
    private BannerViewPager viewPager;

    /**
     * imageview列表
     */
    private List<ImageView> imageViews;

    /**
     * 小图列表
     */
    private List<View> views;

    /**
     * 广告列表
     */
    private List<BannerVo> bannerVos;

    /**
     * 布局管理器
     */
    private LayoutInflater inflater;

    /**
     * pageview的adapter
     */
    private PagerAdapter adapter;

    /**
     * 是否暂停一次banner的自动滚动，当页面在拖拽时，暂停自动滚动
     */
    private boolean isPauseBannerScroll = false;

    /**
     * 是否可以恢复banner的自动滚动
     */
    private boolean isRecoverScroll = false;

    /**
     * 页面是否停止标志
     */
    private boolean isStop = false;

    /**
     * 轮播图更新线程
     */
    protected Runnable runnable = new Runnable() {

        @Override
        public void run() {
            while (!isStop) {
                // 自动滚动每隔5s滚动
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                }

                // 如果停止标志位为true，则停止滚动，结束线程
                if (isStop) {
                    return;
                }

                // 如果暂停滚动标志位为true，则暂停一次滚动
                if (isPauseBannerScroll) {
                    // 如果可以将banner置为自动滚动(当手动滚动停止时)，则恢复自动滚动
                    if (isRecoverScroll) {
                        isPauseBannerScroll = false;
                    }
                    continue;
                }

                runCallFunctionInHandler(CALL_TIME_FIVESECONDS);
            }
        }
    };


    /**
     * 保存所有格子对象的列表
     */
    private List<BankCardViewHolder> viewHoldersList;

    @Override
    protected int getContentView() {
        return R.layout.fragment_index;
    }

    @Override
    protected void addAction() {
        //滑动刷新的控件
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //接口调用
                queryMyCardList();


                queryBannerList();


                queryNotice();
            }
        });
    }

    @OnClick(R.id.viewNotice)
    void clickNotice() {
        if (noticeList != null && noticeList.size() > 0) {
            NoticeVo notice = noticeList.get(0);
            if(notice.getNoticeUrl()!=null && !"".equals(notice.getNoticeUrl())){
                WebActivity.start(getActivity(), notice.getNoticeUrl());
            }
        } else {
            showToastText("公告为空");
        }
    }

    @OnClick({R.id.viewAddBankCard, R.id.linearLayoutEmpty})
    void addBankCard() {

        Intent intent = new Intent(getActivity(), AddCardActivity.class);
        startActivityForResult(intent, REQ_FOR_ADD_CRAD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_FOR_ADD_CRAD) {
            if (resultCode == RESULT_OK) {
                queryMyCardList();
            }
        } else if (requestCode == REQ_FOR_CRAD_DETAIL) {
            //银行卡详情页面回来，重新拉取一次银行卡
            queryMyCardList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        //重新设定banner部分的高度
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutViewPager.getLayoutParams();
        params.height = (int) (getWidthPixels() / Global.ADV_SCALE);//按照屏幕宽度比一个固定比例
        layoutViewPager.setLayoutParams(params);
        viewPager = new BannerViewPager(getActivity());
        layoutViewPager.addView(viewPager);
        //viewpager的事件在处理过viewPager之后再添加
        //viewpager的touch事件
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeContainer.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeContainer.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        //切换事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (bannerVos == null || bannerVos.size() == 0) {
                    return;
                }
                pointView.setIndex(position % bannerVos.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    // 当页面正在手动滑动时，暂停自动滚动.
                    isPauseBannerScroll = true;
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    // 当页面手动滑动结束时，则可以恢复页面的自动滚动了
                    isRecoverScroll = true;
                }
            }
        });
        //点击事件
        viewPager.setOnItemClickListner(new BannerViewPager.OnItemClickListner() {
            @Override
            public void onItemClick(int index) {
                if (bannerVos == null || index < 0 || bannerVos.size() == 0) {
                    return;
                }

                // 获取当前被点击的广告对象
                BannerVo info = bannerVos.get(index % bannerVos.size());
                if(info.getBannerUrl()!=null && !"".equals(info.getBannerUrl())){
                    WebActivity.start(getActivity(),info.getBannerUrl());
                }
                //onClickAdvInfo(info, SaveAdvsCheckStatusReqData.ADVS_CHECK_POS_INDEXBANNER);
            }
        });

        //启动图片轮播
        new Thread(runnable).start();

        //接口调用
        queryMyCardList();


        queryBannerList();


        queryNotice();

    }


    /**
     * 构建banner的view
     *
     * @return
     */
    private boolean createViews() {
        if (bannerVos == null || bannerVos.size() == 0) {
            return false;
        }

        views = new ArrayList<View>();
        int max = bannerVos.size();
        if (max == 2 || max == 3) {
            max *= 2;
        }
        imageViews = new ArrayList<ImageView>();

        for (int i = 0; i < max; i++) {
            BannerVo info = bannerVos.get(i % bannerVos.size());
            View view = inflater.inflate(R.layout.banner_item_advinfo, null);
            views.add(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageAdvInfo);
            imageView.setTag(R.id.image_tag, info.getBannerPic());
            imageViews.add(imageView);
        }
        pointView.setCount(bannerVos.size());
        return true;
    }


    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();
    }

    /**
     * pageView的adapter
     *
     * @return
     */
    private PagerAdapter createAdapter() {
        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                if (views == null) {
                    return 0;
                }
                if (views.size() == 0 || views.size() == 1) {
                    return views.size();
                }
                return Integer.MAX_VALUE;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                int index = position % views.size();
                container.removeView(views.get(index));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                int index = position % views.size();
                View view = views.get(index);
                ImageView imageView = imageViews.get(index);

                // 图片显示，这里之前用的xutils-->bitmapUtils.display(imageView, (String) imageView.getTag());
                Glide.with(IndexFragment.this).load((String) imageView.getTag(R.id.image_tag)).dontAnimate().placeholder(R.mipmap.bobo_default).error(R.mipmap.bobo_default).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
                container.addView(view);
                return view;
            }
        };
        return adapter;
    }

    @Override
    protected void call(int id, Object... args) {
        if (CALL_LOAD_SUCCESS == id) {
            //广告获取成功
            QueryBannerListResp queryBannerListResp = (QueryBannerListResp) args[0];
            bannerVos = queryBannerListResp.getBannerList();
            if (createViews()) {
                adapter = createAdapter();
                viewPager.setAdapter(adapter);
            }
            swipeContainer.setRefreshing(false);
        } else if (CALL_LOAD_FAILD == id) {
            showToastText("获取banner失败");
            swipeContainer.setRefreshing(false);
        } else if (id == CALL_QUERY_SUCCESS) {
            //展现模块数据
            swipeContainer.setRefreshing(false);
            QueryMyCardListResp resp = (QueryMyCardListResp) args[0];
            createViewByModules(resp.getCardList());
            DataKeeper.saveResp(getActivity(), resp);
            scrollViewContent.scrollBy(0, 1);

            if (resp.getCardList() == null || resp.getCardList().size() == 0) {
                linearLayoutEmpty.setVisibility(View.VISIBLE);
            } else {
                linearLayoutEmpty.setVisibility(View.GONE);

            }


        } else if (id == CALL_QUERY_FAILD) {
            //获取模块列表失败
            showToastText("获取模块列表失败");
            swipeContainer.setRefreshing(false);
        } else if (CALL_TIME_FIVESECONDS == id) {
            //5秒钟获取轮播成功
            if (viewPager == null || views == null || views.size() <= 1) {
                return;
            }
            int index = (viewPager.getCurrentItem() + 1);
            viewPager.setCurrentItem(index, true);

        } else if (id == CALL_QUERY_NOTICE_SUCCESS) {

            QueryNoticeResp resp = (QueryNoticeResp) args[0];

            noticeList = resp.getNoticeList();
            if (noticeList != null && noticeList.size() > 0) {
                textViewNotice.setText(noticeList.get(0).getNoticeTitle() );
             }

        } else if (id == CALL_QUERY_NOTICE_FAILED) {

        }


    }

    /**
     * 构造首页的module
     *
     * @param cards
     */
    private void createViewByModules(List<CardVo> cards) {
        linearEntries.removeAllViews();
        View view;
        viewHoldersList = new ArrayList<BankCardViewHolder>();
        int i = 0;
        for (; i < cards.size(); i++) {

            view = inflater.inflate(R.layout.listitem_bankcard, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BankCardViewHolder holder = (BankCardViewHolder) v.getTag();
                    //holder.module.setNewMsg(0);
                    //holder.update();
                    clickCardItem(holder.card);
                }
            });

            // 确定加入的格子的布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.height = (int) (YUtils.getScreenWidth(getActivity()) / 4 * 0.85);
            // 添加一个格子
            linearEntries.addView(view, layoutParams);

            // 构建ViewHolder
            BankCardViewHolder holder = createViewHolder(view, cards.get(i), i);
            view.setTag(holder);
            viewHoldersList.add(holder);

        }


    }

    /**
     * 模块点击事件
     *
     * @param card
     */
    private void clickCardItem(CardVo card) {
        if (card == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), CardDetailActivity.class);
        intent.putExtra("cardVo", card);
        startActivityForResult(intent, REQ_FOR_CRAD_DETAIL);

    }


    /**
     * 查询我的银行卡
     *
     * @param
     * @param
     */
    private void queryMyCardList() {
        BaseReqData reqData = new BaseReqData();

        BaseReq baseReq = new BaseReq(Global.key_queryMyCardList, reqData);
        ProcessManager.getInstance().addProcess(getActivity(), baseReq, this);
    }


    /**
     * 查询我的银行卡
     *
     * @param
     * @param
     */
    private void queryBannerList() {
        QueryBannerListReqData reqData = new QueryBannerListReqData();
        reqData.setBannerPlace(QueryBannerListReqData.BANNER_PLACE_INDEX);
        BaseReq baseReq = new BaseReq(Global.key_queryBannerList, reqData);
        ProcessManager.getInstance().addProcess(getActivity(), baseReq, this);
    }

    /**
     * 查询公告信息
     *
     * @param
     * @param
     */
    private void queryNotice() {
        QueryNoticeReqData reqData = new QueryNoticeReqData();
        reqData.setNoticePlace(QueryNoticeReqData.BANNER_PLACE_INDEX);
        BaseReq baseReq = new BaseReq(Global.key_queryNotice, reqData);
        ProcessManager.getInstance().addProcess(getActivity(), baseReq, this);
    }


    /**
     * 模块holder
     */
    class BankCardViewHolder {
        View view;
        @BindView(R.id.imageViewBankIcon)
        ImageView imageViewBankIcon;
        @BindView(R.id.textViewBankName)
        TextView textViewBankName;
        @BindView(R.id.textViewCardType)
        TextView textViewCardType;
        @BindView(R.id.textViewCardNo)
        TextView textViewCardNo;
        @BindView(R.id.textViewCardState)
        TextView textViewCardState;
        @BindView(R.id.imageViewCardBg)
        ImageView imageViewCardBg;
        CardVo card;
        int index;


        public BankCardViewHolder(View view) {
            this.view = view;
            ButterKnife.bind(this, view);
        }

        void hide() {
            view.setVisibility(View.GONE);
        }


        void update() {
            if (card != null) {
                textViewBankName.setText(card.getBankName());//设置文字

                textViewCardType.setText(
                        YUtils.repalceEmptySting(Constants.CRADS_TYPES.get(card.getCardType()), ""));
                textViewCardNo.setText(card.getCardNo());


                if ("0".equals(card.getCardType())) {
                    textViewCardState.setVisibility(View.GONE);

                } else {
                    textViewCardState.setVisibility(View.VISIBLE);

                    textViewCardState.setText(
                            YUtils.repalceEmptySting(Constants.CRADS_TYPES.get(card.getCardPayoffSts()), ""));

                }
                textViewCardState.setText(Constants.CRADS_STATES.get(card.getCardType()));

                if (index % 5 == 0) {
                    imageViewCardBg.setBackgroundResource(R.mipmap.bankcard_bg_01);
                } else if (index % 5 == 1) {
                    imageViewCardBg.setBackgroundResource(R.mipmap.bankcard_bg_02);
                } else if (index % 5 == 2) {
                    imageViewCardBg.setBackgroundResource(R.mipmap.bankcard_bg_03);
                } else if (index % 5 == 3) {
                    imageViewCardBg.setBackgroundResource(R.mipmap.bankcard_bg_04);
                } else if (index % 5 == 4) {
                    imageViewCardBg.setBackgroundResource(R.mipmap.bankcard_bg_05);
                }

                Glide.with(IndexFragment.this).load(card.getBankIcon()).dontAnimate()
                        .placeholder(R.mipmap.bobo_default).error(R.mipmap.bobo_default)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageViewBankIcon);//显示图片
            }
        }
    }

    /**
     * 构造一个holder
     *
     * @param view
     * @param card
     * @return
     */
    private BankCardViewHolder createViewHolder(View view, CardVo card, int index) {
        BankCardViewHolder holder = new BankCardViewHolder(view);

        holder.card = card;
        holder.index = index;

        holder.update();

        view.setTag(holder);
        return holder;
    }

    /**
     * 更新viewholder
     *
     * @param cards
     */
    private void updateViewHolders(List<CardVo> cards) {
        if (viewHoldersList == null || cards == null) {
            return;
        }
        for (BankCardViewHolder holder : viewHoldersList) {
            for (CardVo card : cards) {
                if (holder.card.equals(card)) {
                    holder.card = card;
                    holder.update();
                }
            }
        }
    }

    @Override
    protected boolean callBack(BaseResp responseBean) {
        if (Global.key_queryBannerList.equals(responseBean.getKey())) {
            //获取banner的接口
            if (responseBean.isOk()) {
                runCallFunctionInHandler(CALL_LOAD_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_LOAD_FAILD);
            }
        } else if (Global.key_queryMyCardList.equals(responseBean.getKey())) {
            //查询模块列表成功
            if (responseBean.isOk()) {
                runCallFunctionInHandler(CALL_QUERY_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_QUERY_FAILD);
            }
        } else if (Global.key_queryNotice.equals(responseBean.getKey())) {
            //查询模块列表成功
            if (responseBean.isOk()) {
                runCallFunctionInHandler(CALL_QUERY_NOTICE_SUCCESS, responseBean);
            } else {
                runCallFunctionInHandler(CALL_QUERY_NOTICE_FAILED, responseBean);
            }
        }
        return false;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onClick(View view) {

    }
}
