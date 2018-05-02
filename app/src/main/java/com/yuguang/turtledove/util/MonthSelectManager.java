package com.codido.nymeria.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codido.nymeria.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liukaifu on 2017/11/9.
 */

public class MonthSelectManager implements View.OnClickListener {

    public MonthSelectManager(Context context, ArrayList<Integer> initSelectList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.initSelectList = initSelectList;
    }

    private Context context;
    private ArrayList<Integer> initSelectList;
    private LayoutInflater layoutInflater;

    private ArrayList<ViewHolder> viewHolders = new ArrayList<ViewHolder>();


    public boolean isSelectRight() {
        if (viewHolders == null || viewHolders.size() == 0) {
            return true;
        }
        boolean hasSelect = false;
        boolean hasNotSelect = false;
        for (int i = 0; i < viewHolders.size(); i++) {
            ViewHolder h = viewHolders.get(i);

            if (!hasSelect) {
                if (h.isSelected) {
                    hasSelect = true;
                }
            } else {
                if (!hasNotSelect) {
                    if (!h.isSelected) {
                        hasNotSelect = true;
                    }
                } else {
                    if (h.isSelected) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();


        for (ViewHolder h : viewHolders) {
            if (h == viewHolder) {
                h.change(!h.isSelected);
            }
        }
    }


    /**
     * 辅助类，用来保存界面的UI实体，和相关数据
     */
    class ViewHolder {

        @BindView(R.id.textViewMonth)
        TextView textViewMonth;
        @BindView(R.id.imageViewSelectFlag)
        ImageView imageViewSelectFlag;
        int month;
        boolean isSelected;

        public ViewHolder(View root) {
            ButterKnife.bind(this, root);
        }


        /**
         * 更新银行卡选择条目的显示
         */
        void update() {
            if (month <= 0) {
                return;
            }


            if (isSelected) {
                imageViewSelectFlag.setImageResource(R.mipmap.checkbox_selected);
            } else {
                imageViewSelectFlag.setImageResource(R.mipmap.checkbox_not_selected);
            }

            textViewMonth.setText(month / 100 + "年" + month % 100 + "月");
        }

        /**
         * 更新银行卡的选择状态
         */
        void change(boolean isSelected) {
            if (isSelected == this.isSelected) {
                return;
            }

            this.isSelected = isSelected;
            if (isSelected) {
                imageViewSelectFlag.setImageResource(R.mipmap.checkbox_selected);
            } else {
                imageViewSelectFlag.setImageResource(R.mipmap.checkbox_not_selected);
            }
        }

    }

    /**
     * 获取被选中的银行卡
     *
     * @return
     */
    public ArrayList<Integer> getSelectMonth() {

        if (viewHolders == null) {
            return null;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (ViewHolder h : viewHolders) {
            if (h.isSelected) {
                arrayList.add(h.month);
            }
        }
        return arrayList;
    }

    /**
     * 根据传入的银行卡信息，创建一个银行卡选择条目
     *
     * @param
     * @return
     */
    public View createMonthSelectView(int month) {


        View view = layoutInflater.inflate(R.layout.item_month_select, null);
        ButterKnife.bind(this, view);


        ViewHolder viewHolder = new ViewHolder(view);

        viewHolders.add(viewHolder);

        viewHolder.month = month;
        viewHolder.isSelected = false;

        if (initSelectList != null) {
            for (Integer in : initSelectList) {
                if (in.intValue() == month) {
                    viewHolder.isSelected = true;
                }
            }
        }
//        viewHolder.imageViewBankIcon = (ImageView) view.findViewById(imageViewBankIcon);
//        viewHolder.textViewBankName = (TextView) view.findViewById(R.id.textViewBankName);
//        viewHolder.imageViewSelectFlag = (ImageView) view.findViewById(R.id.imageViewSelectFlag);


        view.setTag(viewHolder);
        view.setOnClickListener(this);

        viewHolder.update();

        return view;
    }

}
