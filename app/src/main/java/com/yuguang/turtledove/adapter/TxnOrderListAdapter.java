package com.codido.nymeria.adapter;

import android.widget.AbsListView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.adapter.holder.TxnOrderItemHolder;
import com.codido.nymeria.bean.vo.TxnOrderVo;

import java.util.ArrayList;

/**
 * 作者：Junjie.Lai on 2017/11/9 23:38
 * 邮箱：laijj@yzmm365.cn
 */

public class TxnOrderListAdapter extends BaseItemAdapter<TxnOrderVo, TxnOrderItemHolder> {

    /**
     * 构造方法
     *
     * @param mActivity
     * @param absListView
     * @param holderClass @throws
     * @Title:
     */
    public TxnOrderListAdapter(BaseActivity mActivity, AbsListView absListView, Class<TxnOrderItemHolder> holderClass) {
        super(mActivity, absListView, holderClass);
        itemList = new ArrayList<TxnOrderVo>();
    }

    @Override
    protected int getItemViewResId() {
        return R.layout.listitem_txnorder;
    }


    public void clear() {
        itemList = null;
        notifyDataSetChanged();
    }

}
