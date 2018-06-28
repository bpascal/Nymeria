package com.codido.nymeria.adapter;

import android.widget.AbsListView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.adapter.holder.EarnVoItemHolder;
import com.codido.nymeria.bean.vo.EarnVo;

import java.util.ArrayList;

/**
 * 作者：Junjie.Lai on 2017/11/9 16:25
 * 邮箱：laijj@yzmm365.cn
 */

public class EarnListAdapter extends BaseItemAdapter<EarnVo, EarnVoItemHolder> {


    /**
     * 构造方法
     *
     * @param mActivity
     * @param absListView
     * @param holderClass @throws
     * @Title:
     */
    public EarnListAdapter(BaseActivity mActivity, AbsListView absListView, Class<EarnVoItemHolder> holderClass) {
        super(mActivity, absListView, holderClass);
        itemList = new ArrayList<EarnVo>();
    }

    @Override
    protected int getItemViewResId() {
        return R.layout.listitem_earn;
    }

    /**
     * 清空列表
     */
    public void clear() {
        itemList.clear();
    }
}
