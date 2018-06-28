package com.codido.nymeria.adapter;

import java.io.Serializable;

import android.view.View;
import android.widget.AbsListView;

import com.codido.nymeria.activity.BaseActivity;

/**
 * 基本Holder
 * 
 * @ClassName:BaseItemHolder
 * @author Junjie.Lai
 * @date 2014年7月27日 上午11:34:34
 * @Version:FPICClient
 */
public abstract class BaseItemHolder<T extends Serializable> {

	/**
	 * 主Activity
	 */
	protected BaseActivity mActivity;

	/**
	 * 主adapter
	 */
	protected BaseItemAdapter<T, BaseItemHolder<T>> mAdapter;

	/**
	 * 调用它的view
	 */
	protected AbsListView absListView;

	/**
	 * 无参构造方法
	 * 
	 * @Title:
	 * @param
	 * @throws
	 */
	public BaseItemHolder() {
	}

	/**
	 * 构造方法
	 * 
	 * @param mActivity
	 * @param mAdapter
	 * @param convertView
	 * @Author:Junjie.Lai
	 * @Since:2014年7月27日
	 * @Version:FPICClient
	 */
	public BaseItemHolder(BaseActivity mActivity, BaseItemAdapter<T, BaseItemHolder<T>> mAdapter, View convertView, AbsListView absListView) {
		this.mActivity = mActivity;
		this.mAdapter = mAdapter;
		this.absListView = absListView;
		initViews(convertView);
	}

	/**
	 * 初始化控件
	 * 
	 * @param convertView
	 * @Author:Junjie.Lai
	 * @Since:2014年7月27日
	 * @Version:FPICClient
	 */
	protected abstract void initViews(View convertView);

	/**
	 * 设置holder的视图的方法
	 * 
	 * @Author:Junjie.Lai
	 * @Since:2014年7月27日
	 * @Version:FPICClient
	 */
	protected abstract void setHolderView(T item);

	public BaseActivity getmActivity() {
		return mActivity;
	}

	public void setmActivity(BaseActivity mActivity) {
		this.mActivity = mActivity;
	}

	public BaseItemAdapter<T, BaseItemHolder<T>> getmAdapter() {
		return mAdapter;
	}

	public void setmAdapter(BaseItemAdapter<T, BaseItemHolder<T>> mAdapter) {
		this.mAdapter = mAdapter;
	}

	public AbsListView getAbsListView() {
		return absListView;
	}

	public void setAbsListView(AbsListView absListView) {
		this.absListView = absListView;
	}
}
