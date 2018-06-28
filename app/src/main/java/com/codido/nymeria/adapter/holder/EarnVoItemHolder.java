package com.codido.nymeria.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.adapter.BaseItemHolder;
import com.codido.nymeria.bean.vo.EarnVo;
import com.codido.nymeria.util.YDateUtils;
import com.codido.nymeria.util.YUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：Junjie.Lai on 2017/11/9 16:26
 * 邮箱：laijj@yzmm365.cn
 */

public class EarnVoItemHolder extends BaseItemHolder<EarnVo> {

    @BindView(R.id.textViewUserName)
    TextView textViewUserName;

    @BindView(R.id.textViewUserMobile)
    TextView textViewUserMobile;

    @BindView(R.id.textViewAmount)
    TextView textViewAmount;

    @BindView(R.id.textViewShareName)
    TextView textViewShareName;

    @BindView(R.id.textViewShareTime)
    TextView textViewShareTime;


    public EarnVoItemHolder(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    protected void initViews(View convertView) {
    }

    @Override
    protected void setHolderView(EarnVo item) {

        textViewUserName.setText(YUtils.replaceIfEmpty(item.getSourceUserName(), "未实名"));
        textViewUserMobile.setText(item.getSourceMobile());
        textViewAmount.setText(item.getEarnAmount());
        textViewShareName.setText(item.getShareName());
        textViewShareTime.setText(YDateUtils.format(new Date(item.getShareTime()), "yyyy-MM-dd hh:mm"));
    }
}
