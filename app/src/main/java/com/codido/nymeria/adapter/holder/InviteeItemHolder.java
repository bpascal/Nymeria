package com.codido.nymeria.adapter.holder;

import android.view.View;
import android.widget.TextView;

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

public class InviteeItemHolder extends BaseItemHolder<InviteeVo> {

    @BindView(R.id.textViewUserName)
    TextView textViewUserName;

    @BindView(R.id.textViewUserMobile)
    TextView textViewUserMobile;

    @BindView(R.id.textViewUserLevel)
    TextView textViewUserLevel;

    @BindView(R.id.textViewUserCount)
    TextView textViewUserCount;

    @BindView(R.id.textViewTotalMoney)
    TextView textViewTotalMoney;

    @BindView(R.id.textViewTotalUserCount)
    TextView textViewTotalUserCount;

    public InviteeItemHolder(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    protected void initViews(View convertView) {
//        this.textViewUserName = (TextView) convertView.findViewById(R.id.textViewUserName);
//        this.textViewUserMobile = (TextView) convertView.findViewById(R.id.textViewUserMobile);
//        this.textViewUserLevel = (TextView) convertView.findViewById(R.id.textViewUserLevel);
//        this.textViewUserCount = (TextView) convertView.findViewById(R.id.textViewUserCount);
//        this.textViewTotalMoney = (TextView) convertView.findViewById(R.id.textViewTotalMoney);
    }

    @Override
    protected void setHolderView(InviteeVo item) {
        textViewUserName.setText(YUtils.replaceIfEmpty(item.getUserName(), "未实名"));
        textViewUserMobile.setText(item.getMobile());
        textViewUserLevel.setText(Constants.USER_LEVEL.get(item.getLevel()));
        textViewUserCount.setText(item.getChildrenNum());
        textViewTotalUserCount.setText((Integer.parseInt(item.getOtherNum()) + Integer.parseInt(item.getChildrenNum())) + "/");
        textViewTotalMoney.setText(item.getTotalEarnBalance());
    }
}
