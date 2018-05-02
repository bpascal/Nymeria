package com.codido.nymeria.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.bean.vo.CardVo;

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
        CardVo card;
        boolean isSelected;

        public ViewHolder(View root) {
            ButterKnife.bind(this, root);
        }


        /**
         * 更新银行卡选择条目的显示
         */
        void update() {
            if (card == null) {
                return;
            }

            Glide.with(context).load(card.getBankIcon()).
                    dontAnimate().placeholder(R.mipmap.bankcard_icon_default)
                    .error(R.mipmap.bankcard_icon_default).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageViewBankIcon);

            if (isSelected) {
                imageViewBankSelectFlag.setImageResource(R.mipmap.bank_select_flag_sel);
            } else {
                imageViewBankSelectFlag.setImageResource(R.mipmap.bank_select_flag_notsel);
            }


            String cardNo = card.getCardNo();
            if (cardNo!=null && cardNo.length()>4){
                cardNo = cardNo.substring(cardNo.length()-4,  cardNo.length());
            }

            textViewBankCardNo.setText(cardNo);
            textViewBankCardType.setText(Constants.CRADS_TYPES.get(card.getCardType()));

            textViewBankName.setText(card.getBankName());
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

    /**
     * 获取被选中的银行卡
     *
     * @return
     */
    public CardVo getSelectBankCard() {

        if (viewHolders == null) {
            return null;
        }
        for (ViewHolder h : viewHolders) {
            if (h.isSelected) {
                return h.card;
            }
        }
        return null;
    }

    /**
     * 根据传入的银行卡信息，创建一个银行卡选择条目
     *
     * @param card
     * @return
     */
    public View createBankCardView(CardVo card) {


        View view = layoutInflater.inflate(R.layout.bakcard_select_item, null);
        ButterKnife.bind(this, view);


        ViewHolder viewHolder = new ViewHolder(view);

        viewHolders.add(viewHolder);

        viewHolder.card = card;
        viewHolder.isSelected = false;
//        viewHolder.imageViewBankIcon = (ImageView) view.findViewById(imageViewBankIcon);
//        viewHolder.textViewBankName = (TextView) view.findViewById(R.id.textViewBankName);
//        viewHolder.imageViewBankSelectFlag = (ImageView) view.findViewById(R.id.imageViewBankSelectFlag);


        view.setTag(viewHolder);
        view.setOnClickListener(this);

        viewHolder.update();

        return view;
    }

}
