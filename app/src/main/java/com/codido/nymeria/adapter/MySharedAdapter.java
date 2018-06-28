package com.codido.nymeria.adapter;

import android.widget.AbsListView;

import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.adapter.holder.MySharedHolder;
import com.codido.nymeria.bean.vo.ShareVo;

/**
 * Created by bpascal on 2017/1/3.
 */
public class MySharedAdapter extends BaseItemAdapter<ShareVo, MySharedHolder> {

    /**
     * 构造方法
     *
     * @param mActivity
     * @param absListView
     */
    protected MySharedAdapter(BaseActivity mActivity, AbsListView absListView) {
        super(mActivity, absListView, MySharedHolder.class);
    }

    @Override
    protected int getItemViewResId() {
        return 0;
    }
}
