package com.codido.nymeria.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.resp.BaseResp;
import com.codido.nymeria.fragment.BaseFragment;
import com.codido.nymeria.fragment.IndexFragment;
import com.codido.nymeria.fragment.InviteFragment;
import com.codido.nymeria.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 主页
 * Created by bpascal on 2016/12/11.
 */
public class MainActivity extends BaseActivity {

    /**
     * fragment标识-首页
     */
    public static final int FRAGMENT_CODE_INDEX = FIRST_VAL++;

    /**
     * fragment标识-开奖
     */
    public static final int FRAGMENT_CODE_LOTTERY = FIRST_VAL++;

    /**
     * fragment标识-更多
     */
    public static final int FRAGMENT_CODE_MORE = FIRST_VAL++;

    /**
     * 两次点击返回按钮的间隔时间，超过这个时间，将退出“达达卡友”
     */
    public static final long DOUBLE_TAP_TIME = 3000L;

    /**
     * 记录上次按下BACK键的时间
     */
    private long lastTapBackTime;

    @BindView(R.id.imageViewIndex)
    ImageView imageViewIndex;
    @BindView(R.id.textViewIndex)
    TextView textViewIndex;
    @BindView(R.id.viewIndex)
    RelativeLayout viewIndex;

    @BindView(R.id.imageViewLottery)
    ImageView imageViewLottery;
    @BindView(R.id.textViewLottery)
    TextView textViewLottery;
    @BindView(R.id.viewLottery)
    RelativeLayout viewLottery;

    @BindView(R.id.imageViewMore)
    ImageView imageViewMore;
    @BindView(R.id.textViewMore)
    TextView textViewMore;
    @BindView(R.id.viewMore)
    RelativeLayout viewMore;
    @BindView(R.id.viewMorePoint)
    View viewMorePoint;

    /**
     * 首页fragment
     */
    private IndexFragment indexFragment;

    /**
     * 开奖fragment
     */
    private InviteFragment inviteFragment;

    /**
     * 更多fragment
     */
    private MoreFragment moreFragment;

    /**
     * 当前选择项
     */
    private int index = -1;
    /**
     * 当前显示的Fragement
     */
    private BaseFragment mSelFragment;


    @OnClick({R.id.viewIndex, R.id.viewLottery, R.id.viewMore})
    void tabClickEvent(View tabView) {
        if (tabView == viewIndex) {
            //首页
            changeTab(FRAGMENT_CODE_INDEX);
        } else if (tabView == viewLottery) {
            //我的分享
            changeTab(FRAGMENT_CODE_LOTTERY);
        }   else if (tabView == viewMore) {
            //我的账户
            changeTab(FRAGMENT_CODE_MORE);
        }
    }

    /**
     * 切换tab的动作
     *
     * @param index
     */
    protected void changeTab(int index) {
        //选择不是同一个
        if (this.index != index) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //将选中的fragment赋值给当前fragment
            mSelFragment = (BaseFragment) fragmentManager.findFragmentByTag(String.valueOf(index));
            //先隐藏所有fragment
            hideFragments(transaction);
            //显示选中的fragment
            transaction.show(mSelFragment);
            //加载选中的时间
            onSelect(mSelFragment);
            //切换tab的样式
            changeTabUi(index);
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            this.index = index;
        }
    }

    /**
     * fragment的选中事件
     *
     * @param fragment
     */
    protected void onSelect(BaseFragment fragment) {
        if (fragment.isSelect()) {
            return;
        } else {
            //把所有fragment设置为未选择
            indexFragment.setIsSelect(false);
            inviteFragment.setIsSelect(false);
             moreFragment.setIsSelect(false);
            //将选中的fragment设置为选中
            fragment.setIsSelect(true);
            //执行选中的fragment的选中事件
            fragment.onSelected();
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (inviteFragment != null) {
            transaction.hide(inviteFragment);
        }

        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
    }

    /**
     * 切换tab的样式
     *
     * @param index
     */
    protected void changeTabUi(int index) {
        int colorSel = getResources().getColor(R.color.main_tab_selected_red);//被选中的颜色
        int colorNor = getResources().getColor(R.color.main_tab_unselect_gray);//未选中的颜色

        if (index == FRAGMENT_CODE_INDEX) {
            //首页,购彩
            imageViewIndex.setImageResource(R.mipmap.icon_tab_idx_sel);
            imageViewLottery.setImageResource(R.mipmap.icon_tab_fri_nor);
             imageViewMore.setImageResource(R.mipmap.icon_tab_more_nor);

            textViewIndex.setTextColor(colorSel);
            textViewLottery.setTextColor(colorNor);
             textViewMore.setTextColor(colorNor);
        } else if (index == FRAGMENT_CODE_LOTTERY) {
            //开奖
            imageViewIndex.setImageResource(R.mipmap.icon_tab_idx_nor);
            imageViewLottery.setImageResource(R.mipmap.icon_tab_fri_sel);
             imageViewMore.setImageResource(R.mipmap.icon_tab_more_nor);

            textViewIndex.setTextColor(colorNor);
            textViewLottery.setTextColor(colorSel);
             textViewMore.setTextColor(colorNor);
        }  else if (index == FRAGMENT_CODE_MORE) {
            //更多
            imageViewIndex.setImageResource(R.mipmap.icon_tab_idx_nor);
            imageViewLottery.setImageResource(R.mipmap.icon_tab_fri_nor);
             imageViewMore.setImageResource(R.mipmap.icon_tab_more_sel);

            textViewIndex.setTextColor(colorNor);
            textViewLottery.setTextColor(colorNor);
             textViewMore.setTextColor(colorSel);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //初始化tab框
        createFragment();
        //默认选中首页
        changeTab(FRAGMENT_CODE_INDEX);
    }

    /**
     * 初始化所有的fragment
     */
    private void createFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        indexFragment = new IndexFragment();
        inviteFragment = new InviteFragment();
         moreFragment = new MoreFragment();

        transaction.add(R.id.mainFrameLayout, indexFragment, String.valueOf(FRAGMENT_CODE_INDEX));
        transaction.add(R.id.mainFrameLayout, inviteFragment, String.valueOf(FRAGMENT_CODE_LOTTERY));
         transaction.add(R.id.mainFrameLayout, moreFragment, String.valueOf(FRAGMENT_CODE_MORE));

        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        return false;
    }

    @Override
    public void call(int callID, Object... args) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {// 截获BACK键的松开操作

                // 获取本次的按键时间
                long time = System.currentTimeMillis();

                // 如果本次按键的时间与上次按键时间的时间差大于指定数值
                if (time - lastTapBackTime > DOUBLE_TAP_TIME) {
                    // 告之用户，“再按一次退出达达卡友”
                    Toast.makeText(this, "再按一次退出" + getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();

                    // 记录本次按键时间
                    lastTapBackTime = time;
                }
                // 如果本次按键的时间与上次按键时间的时间差小于等于指定数值
                else {
                    // 关闭主页面
                    finish();
                    // 退出应用程序
                    BaseActivity.exit(this);
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
