package com.codido.nymeria.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codido.nymeria.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 加载更多listview
 */
public class MoreListView extends ListView implements OnScrollListener {
    private final static int STATE_UP_RELEASE_TO_REFRESH = 0;
    private final static int STATE_UP_PULL_TO_REFRESH = 1;
    private final static int STATE_UP_REFRESHING = 2;
    private final static int STATE_UP_DONE = 3;

    public boolean isLoading() {
        return (STATE_UP_REFRESHING == state_up) || (STATE_DOWN_REFRESHING == state_down);
    }

    private final static int STATE_DOWN_RELEASE_TO_REFRESH = 4;
    private final static int STATE_DOWN_PULL_TO_REFRESH = 5;
    private final static int STATE_DOWN_REFRESHING = 6;
    private final static int STATE_DOWN_DONE = 7;

    private LinearLayout linearLayoutFooter_1;
    private TextView textviewFooter_1;
    private ProgressBar progressBarFooter_1;

    private LinearLayout linearLayoutFooter_2;

    private LayoutInflater inflater;
    private LinearLayout headView;

    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecoredUp;

    private boolean isRecoredDown;

    private int headContentHeight;

    private int footContentHeight2;
    private int footContentHeight1;

    private int startYUp;
    private int startYDown;
    private int firstItemIndex;

    private int state_up = STATE_UP_DONE;
    private boolean isBack_up;

    private int state_down = STATE_DOWN_DONE;
    private boolean isBack_down;

    private boolean pullRefreshFlag = true;

    private boolean downRefreshFlag = true;

    public OnRefreshListener refreshListener;

    public OnPaddingChangeListener onPaddingChangeListener;

    public MoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private ListAdapter adapter;

    // 是否有一次加载更多的行为
    private boolean isLastLoadMore = false;
    private int lastCount = 0;

    public void updateFText() {

        int count = adapter == null ? 0 : adapter.getCount();
        if (count <= lastCount) { // 数据量小于上次的数据量则，设置为暂无更多数据
            textviewFooter_1.setText("暂无更多数据");
        } else {
            textviewFooter_1.setText("加载更多");
        }
    }

    public void setAdapter(ListAdapter a) {
        textviewFooter_1.setText("加载更多");
        super.setAdapter(a);
        this.adapter = a;
        if (getAdapter() != null) {
            getAdapter().registerDataSetObserver(new DataSetObserver() {
                public void onChanged() {
                    // updateFText();
                }
            });
        }

        // 充值isLastLoadMore标志位
        isLastLoadMore = false;
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);

        headView = (LinearLayout) inflater.inflate(R.layout.more_listview_head, null);

        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);

        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();

        setPaddingForHeadView(-1 * headContentHeight);

        addHeaderView(headView);

        super.setOnScrollListener(this);

        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(250);
        reverseAnimation.setFillAfter(true);

        linearLayoutFooter_1 = (LinearLayout) inflater.inflate(R.layout.weibo_list_item_2, null);

        measureView(linearLayoutFooter_1);
        footContentHeight1 = linearLayoutFooter_1.getMeasuredHeight();

        textviewFooter_1 = (TextView) linearLayoutFooter_1.findViewById(R.id.weibo_list_item_2_text);
        progressBarFooter_1 = (ProgressBar) linearLayoutFooter_1.findViewById(R.id.weibo_list_item_2_progress);
        textviewFooter_1.setText("加载更多");
        progressBarFooter_1.setVisibility(View.GONE);
        this.addFooterView(linearLayoutFooter_1);

        linearLayoutFooter_1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (state_down == STATE_DOWN_REFRESHING) {
                    return;
                } else {
                    loadMore();
                }
            }
        });

        linearLayoutFooter_2 = (LinearLayout) inflater.inflate(R.layout.weibo_list_item_3, null);
        measureView(linearLayoutFooter_2);
        footContentHeight2 = linearLayoutFooter_2.getMeasuredHeight();
        linearLayoutFooter_2.setPadding(0, 0, 0, -footContentHeight2);
        linearLayoutFooter_2.invalidate();
        addFooterView(linearLayoutFooter_2);

        try {
            if (Build.VERSION.SDK_INT >= 9) {
                Method method = getClass().getMethod("setOverScrollMode", int.class);
                Field f = View.class.getDeclaredField("OVER_SCROLL_NEVER");
                method.invoke(this, f.getInt(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFooterTextView(String str) {
        textviewFooter_1.setText(str);
    }

    public void onScroll(AbsListView absListView, int firstVisiableItem, int arg2, int arg3) {
        firstItemIndex = firstVisiableItem;
        visibleItemCount = arg2;
        totalItemCount = arg3;
    }

    public boolean isScrollToLastItem() {
        // return (firstItemIndex + visibleItemCount == totalItemCount) &&
        // totalItemCount > 0;
        return getHeight() + computeVerticalScrollOffset() >= computeVerticalScrollRange();
    }

    public int visibleItemCount;
    public int totalItemCount;

    public void refresh() {
        if (state_up == STATE_UP_REFRESHING) {
            return;
        }
        state_up = STATE_UP_REFRESHING;
        changeHeaderViewByState();
        setSelection(0);
        onRefresh();

    }

    public void loadMore() {
        if (state_down == STATE_DOWN_REFRESHING) {
            return;
        }

        state_down = STATE_DOWN_REFRESHING;
        changeFooterViewByState();
        onLoadMore();

    }

    public void processUpAction(MotionEvent event) {

        if (!pullRefreshFlag) {
            return;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (computeVerticalScrollOffset() <= 0 && !isRecoredUp) {
                    startYUp = (int) event.getY();
                    isRecoredUp = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (state_up != STATE_UP_REFRESHING && firstItemIndex == 0) {
                    if (state_up == STATE_UP_DONE) {
                    }
                    if (state_up == STATE_UP_PULL_TO_REFRESH) {
                        state_up = STATE_UP_DONE;
                        changeHeaderViewByState();
                    }
                    if (state_up == STATE_UP_RELEASE_TO_REFRESH) {
                        state_up = STATE_UP_REFRESHING;
                        changeHeaderViewByState();
                        onRefresh();
                    }
                }

                isRecoredUp = false;
                isBack_up = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                if (!isRecoredUp && computeVerticalScrollOffset() <= 0) {
                    isRecoredUp = true;
                    startYUp = tempY;
                }
                if (state_up != STATE_UP_REFRESHING && isRecoredUp) {
                    // 可以松手去刷新了
                    if (state_up == STATE_UP_RELEASE_TO_REFRESH) {
                        // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                        if ((tempY - startYUp < headContentHeight) && (tempY - startYUp) > 0) {
                            state_up = STATE_UP_PULL_TO_REFRESH;
                            changeHeaderViewByState();

                        }
                        // 一下子推到顶了
                        else if (tempY - startYUp <= 0) {
                            state_up = STATE_UP_DONE;
                            changeHeaderViewByState();

                        }
                        // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                        else {
                            // 不用进行特别的操作，只用更新paddingTop的值就行了
                        }
                    }
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                    if (state_up == STATE_UP_PULL_TO_REFRESH) {
                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        if (tempY - startYUp >= headContentHeight) {
                            state_up = STATE_UP_RELEASE_TO_REFRESH;
                            isBack_up = true;
                            changeHeaderViewByState();

                        }
                        // 上推到顶了
                        else if (tempY - startYUp <= 0) {
                            state_up = STATE_UP_DONE;
                            changeHeaderViewByState();
                        }
                    }

                    // done状态下
                    if (state_up == STATE_UP_DONE) {
                        if (tempY - startYUp > 0) {
                            state_up = STATE_UP_PULL_TO_REFRESH;
                            changeHeaderViewByState();
                        }
                    }

                    // 更新headView的size
                    if (state_up == STATE_UP_PULL_TO_REFRESH) {

                        setPaddingForHeadView(-1 * headContentHeight + (tempY - startYUp));

                    }

                    if (state_up == STATE_UP_RELEASE_TO_REFRESH) {

                        setPaddingForHeadView(tempY - startYUp - headContentHeight);
                    }
                }
                break;
        }

    }

    public void setPaddingForHeadView(int paddingTop) {
        headView.setPadding(0, paddingTop, 0, 0);
        headView.invalidate();
        if (onPaddingChangeListener != null) {
            onPaddingChangeListener.onPaddingChange(paddingTop);
        }
//		if ( getChildAt(1) != null)
//			Global.debug(firstItemIndex + "-" + visibleItemCount + "-" + totalItemCount + "-"
//					+  getChildAt(1).getTop());
        // Global.debug((footContentHeight2 + paddingTop) + "-----" + state_up);
    }

    private boolean isHideFooter = false;

    public void hideFooter() {
        isHideFooter = true;

        linearLayoutFooter_2.setPadding(0, 0, 0, -1 * footContentHeight2);
        linearLayoutFooter_2.invalidate();

        linearLayoutFooter_1.setPadding(0, 0, 0, -footContentHeight1);
        linearLayoutFooter_1.invalidate();
    }

    public void processDownAction(MotionEvent event) {

        if (isHideFooter) {
            return;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isScrollToLastItem() && !isRecoredDown) {
                    startYDown = (int) event.getY();
                    isRecoredDown = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (state_down != STATE_DOWN_REFRESHING && isScrollToLastItem()) {
                    if (state_down == STATE_DOWN_DONE) {

                    }
                    if (state_down == STATE_DOWN_PULL_TO_REFRESH) {
                        state_down = STATE_DOWN_DONE;
                        changeFooterViewByState();
                    }
                    if (state_down == STATE_DOWN_RELEASE_TO_REFRESH) {
                        state_down = STATE_DOWN_REFRESHING;
                        changeFooterViewByState();
                        onLoadMore();
                    }
                }

                isRecoredDown = false;
                isBack_down = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                if (!isRecoredDown && isScrollToLastItem()) {
                    isRecoredDown = true;
                    startYDown = tempY;
                }
                if (state_down != STATE_DOWN_REFRESHING && isRecoredDown) {
                    // 可以松手去刷新了
                    if (state_down == STATE_DOWN_RELEASE_TO_REFRESH) {
                        // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                        if ((startYDown - tempY < footContentHeight2) && (startYDown - tempY) > 0) {
                            state_down = STATE_DOWN_PULL_TO_REFRESH;
                            changeFooterViewByState();

                        }
                        // 一下子推到顶了
                        else if (startYDown - tempY <= 0) {
                            state_down = STATE_DOWN_DONE;
                            changeFooterViewByState();

                        }
                        // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                        else {
                            // 不用进行特别的操作，只用更新paddingTop的值就行了
                        }
                    }
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                    if (state_down == STATE_DOWN_PULL_TO_REFRESH) {
                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        if (startYDown - tempY >= footContentHeight2) {
                            state_down = STATE_DOWN_RELEASE_TO_REFRESH;
                            isBack_down = true;
                            changeFooterViewByState();

                        }
                        // 上推到顶了
                        else if (startYDown - tempY <= 0) {
                            state_down = STATE_DOWN_DONE;
                            changeFooterViewByState();
                        }
                    }

                    // done状态下
                    if (state_down == STATE_DOWN_DONE) {
                        if ((startYDown - tempY < footContentHeight2) && startYDown - tempY > 0) {
                            state_down = STATE_DOWN_PULL_TO_REFRESH;
                            changeHeaderViewByState();
                        } else if (startYDown - tempY >= footContentHeight2) {
                            state_down = STATE_DOWN_RELEASE_TO_REFRESH;
                            isBack_down = true;
                            changeFooterViewByState();

                        }
                    }

                    // 更新headView的size
                    if (state_down == STATE_DOWN_PULL_TO_REFRESH
                            || state_down == STATE_DOWN_RELEASE_TO_REFRESH) {
                        linearLayoutFooter_2.setPadding(0, 0, 0, -1 * footContentHeight2
                                + (startYDown - tempY));
                        linearLayoutFooter_2.invalidate();
                    }
                }
                break;
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean isLoading = isLoading();

        if (!isLoading) {
            processUpAction(event);
        }
        if (!isLoading) {
            processDownAction(event);
        }

        return super.onTouchEvent(event);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeFooterViewByState() {
        switch (state_down) {
            case STATE_DOWN_RELEASE_TO_REFRESH:
                linearLayoutFooter_2.setPadding(0, 0, 0, 0);
                linearLayoutFooter_2.invalidate();
                progressBarFooter_1.setVisibility(View.GONE);
                break;
            case STATE_DOWN_PULL_TO_REFRESH:

                linearLayoutFooter_2.setPadding(0, 0, 0, 0);
                linearLayoutFooter_2.invalidate();
                progressBarFooter_1.setVisibility(View.GONE);
                break;

            case STATE_DOWN_REFRESHING:

                linearLayoutFooter_2.setPadding(0, 0, 0, -1 * footContentHeight2);
                linearLayoutFooter_2.invalidate();
                textviewFooter_1.setText("正在加载");
                progressBarFooter_1.setVisibility(View.VISIBLE);
                lastCount = adapter == null ? 0 : adapter.getCount(); // 记录加载时Adapter的数量
                break;
            case STATE_DOWN_DONE:
                linearLayoutFooter_2.setPadding(0, 0, 0, -1 * footContentHeight2);
                linearLayoutFooter_2.invalidate();
                progressBarFooter_1.setVisibility(View.GONE);

                updateFText();

                break;
        }
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state_up) {
            case STATE_UP_RELEASE_TO_REFRESH:

                setPaddingForHeadView(0);

                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.GONE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText("松开刷新");

                break;
            case STATE_UP_PULL_TO_REFRESH:

                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack_up) {
                    setPaddingForHeadView(0);

                    isBack_up = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);

                    tipsTextview.setText("下拉刷新");
                } else {
                    setPaddingForHeadView(-headContentHeight);

                    tipsTextview.setText("下拉刷新");
                }
                break;

            case STATE_UP_REFRESHING:
                setPaddingForHeadView(0);

                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
                lastUpdatedTextView.setVisibility(View.GONE);

                linearLayoutFooter_1.setVisibility(View.GONE);
                break;
            case STATE_UP_DONE:

                setPaddingForHeadView(-1 * headContentHeight);
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.mipmap.black_pull_to_refresh_arrow);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.GONE);
                textviewFooter_1.setText("加载更多");
                linearLayoutFooter_1.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public interface OnPaddingChangeListener {
        public void onPaddingChange(int padding);
    }

    public void onLoadMoreComplete() {
        state_down = STATE_DOWN_DONE;
        changeFooterViewByState();
    }

    private void onLoadMore() {
        isLastLoadMore = true;
        if (refreshListener != null) {
            refreshListener.onLoadMore();
        }
    }

    public void onRefreshComplete() {
        state_up = STATE_UP_DONE;
        lastUpdatedTextView.setText("最近更新: " + format.format(new Date()));
        changeHeaderViewByState();
    }

    static SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setPullRefreshFlag(boolean pullRefreshFlag) {
        this.pullRefreshFlag = pullRefreshFlag;
    }

    public void setDownRefreshFlag(boolean downRefreshFlag) {
        this.downRefreshFlag = downRefreshFlag;
    }

    public OnPaddingChangeListener getOnPaddingChangeListener() {
        return onPaddingChangeListener;
    }

    public void setOnPaddingChangeListener(OnPaddingChangeListener onPaddingChangeListener) {
        this.onPaddingChangeListener = onPaddingChangeListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}
