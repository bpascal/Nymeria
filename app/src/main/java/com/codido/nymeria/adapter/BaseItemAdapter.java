package com.codido.nymeria.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.util.Global;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 基础adapter，用于简化adapter代码
 *
 * @param <T>
 * @author Junjie.Lai
 * @ClassName:BaseFPAdapter
 * @date 2014年7月27日 上午11:30:33
 * @Version:FPICClient
 */
public abstract class BaseItemAdapter<T extends Serializable, X extends BaseItemHolder<T>> extends BaseAdapter {

    /**
     * 主activity
     */
    private BaseActivity mActivity;

    /**
     * 调用它的view
     */
    private AbsListView absListView;

    /**
     * 选择器
     */
    private LayoutInflater layoutInflater;

    /**
     * 元素列表
     */
    protected List<T> itemList;

    /**
     * holder的类型
     */
    private Class<X> holderClass;

    /**
     * 构造方法
     *
     * @param @param    mActivity
     * @param mActivity
     * @throws
     * @Title:
     */
    protected BaseItemAdapter(BaseActivity mActivity, AbsListView absListView, Class<X> holderClass) {
        this.mActivity = mActivity;
        this.holderClass = holderClass;
        this.absListView = absListView;
        layoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        if (itemList != null && itemList.size() > 0) {
            return itemList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (itemList != null && itemList.size() > 0) {
            return itemList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (itemList != null && itemList.size() > 0) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        X holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(getItemViewResId(), null);
            try {

                try {
                    Constructor c1 = holderClass.getDeclaredConstructor(new Class[]{View.class});
                    if (c1 != null) {
                        holder = (X) c1.newInstance(convertView);
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if (holder == null) {
                    holder = holderClass.newInstance();
                }

                holder.setmActivity(mActivity);
                holder.setmAdapter((BaseItemAdapter<T, BaseItemHolder<T>>) this);
                holder.setAbsListView(absListView);
                holder.initViews(convertView);
            } catch (InstantiationException e) {
                Global.debug("bpascal BaseItemAdapter new HolderError InstantiationException exception");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Global.debug("bpascal BaseItemAdapter new HolderError IllegalAccessException exception");
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            convertView.setTag(holder);
        } else {
            holder = (X) convertView.getTag();
        }

        holder.setHolderView(itemList.get(position));

        return convertView;
    }

    /**
     * 返回item的列表布局
     *
     * @return
     * @Author:Junjie.Lai
     * @Since:2014年7月27日
     * @Version:FPICClient
     */
    protected abstract int getItemViewResId();

    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }
}
