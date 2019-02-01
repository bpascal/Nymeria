package com.codido.nymeria.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codido.nymeria.R;
import com.codido.nymeria.adapter.BaseItemHolder;
import com.codido.nymeria.util.Constants;
import com.codido.nymeria.util.YUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Junjie.Lai on 2017/11/9 16:26
 * 邮箱：laijj@yzmm365.cn
 */
public class TxnOrderItemHolder extends BaseItemHolder<TxnOrderVo> {

    @BindView(R.id.textViewBankName)
    TextView textViewBankName;
    @BindView(R.id.textViewCardType)
    TextView textViewCardType;
    @BindView(R.id.textViewCardNo)
    TextView textViewCardNo;
    @BindView(R.id.textViewOrderAmt)
    TextView textViewOrderAmt;
    @BindView(R.id.textViewOrderType)
    TextView textViewOrderType;
    @BindView(R.id.imageViewBankIcon)
    ImageView imageViewBankIcon;

    public TxnOrderItemHolder(View convertView) {
        ButterKnife.bind(this, convertView);
    }

    @Override
    protected void initViews(View convertView) {
    }

    @Override
    protected void setHolderView(TxnOrderVo item) {

        textViewBankName.setText(YUtils.replaceIfEmpty(item.getBankName(), ""));//TODO Junjie.Lai 银行名称
        textViewCardType.setText(Constants.CRADS_TYPES.get(item.getCardType()));//TODO Junjie.Lai 卡类型
        textViewCardNo.setText("(" + item.getCardNo() + ")");
        textViewOrderAmt.setText(item.getOrderAmount());
        textViewOrderType.setText(Constants.ORDER_TYPES.get(item.getOrderType()));

        Glide.with(getmActivity()).load(item.getBankIcon()).
                dontAnimate().placeholder(R.mipmap.bankcard_icon_default)
                .error(R.mipmap.bankcard_icon_default).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageViewBankIcon);
    }
}
