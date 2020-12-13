package com.codido.nymeria.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 银行卡选择条目管理工具，包括：
 * 1。 创建选择页面的选择项
 * 2。 管理选择关系，只有一个银行卡被选中，并且可以被返回
 * Created by liukaifu on 2017/11/9.
 */

public class BankCardSelectManager implements View.OnClickListener {

    public BankCardSelectManager(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<ViewHolder> viewHolders = new ArrayList<ViewHolder>();


    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag();


        for (ViewHolder h : viewHolders) {
            if (h == viewHolder) {
                h.change(true);
            } else {
                h.change(false);
            }
        }
    }


    /**
     * 辅助类，用来保存界面的UI实体，和相关数据
     */
    class ViewHolder {

        @BindView(R.id.imageViewBankIcon)
        ImageView imageViewBankIcon;
        @BindView(R.id.textViewBankName)
        TextView textViewBankName;
        @BindView(R.id.textViewBankCardType)
        TextView textViewBankCardType;
        @BindView(R.id.textViewBankCardNo)
        TextView textViewBankCardNo;
        @BindView(R.id.imageViewBankSelectFlag)
        ImageView imageViewBankSelectFlag;
        boolean isSelected;

        public ViewHolder(View root) {
            ButterKnife.bind(this, root);
        }


        /**
         * 更新银行卡的选择状态
         */
        void change(boolean isSelected) {
            if (isSelected == this.isSelected) {
                return;
            }

            this.isSelected = isSelected;
            if (this.isSelected) {
                imageViewBankSelectFlag.setImageResource(R.mipmap.bank_select_flag_sel);
            } else {
                imageViewBankSelectFlag.setImageResource(R.mipmap.bank_select_flag_notsel);
            }
        }

    }

}
