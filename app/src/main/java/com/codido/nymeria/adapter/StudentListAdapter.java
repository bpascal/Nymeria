package com.codido.nymeria.adapter;

import android.widget.AbsListView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.adapter.holder.StudentItemHolder;
import com.codido.nymeria.bean.vo.StudentInfo;

import java.util.ArrayList;

/**
 * 学生信息列表adapter
 * 作者：Junjie.Lai on 2017/3/20 15:38
 * 邮箱：laijj@yzmm365.cn
 */
public class StudentListAdapter extends BaseItemAdapter<StudentInfo, StudentItemHolder> {

    /**
     * 构造方法
     *
     * @param mActivity
     * @param absListView
     * @param holderClass @throws
     */
    public StudentListAdapter(BaseActivity mActivity, AbsListView absListView, Class<StudentItemHolder> holderClass) {
        super(mActivity, absListView, holderClass);
        itemList = new ArrayList<StudentInfo>();
    }

    @Override
    protected int getItemViewResId() {
        return R.layout.griditem_student;
    }


    /**
     * 清空列表
     */
    public void clear() {
        itemList.clear();
    }
}
