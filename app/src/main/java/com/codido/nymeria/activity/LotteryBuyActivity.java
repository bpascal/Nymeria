package com.codido.nymeria.activity;

import android.content.Intent;
import android.os.Bundle;

import com.codido.nymeria.R;
import com.codido.nymeria.bean.resp.BaseResp;

/**
 * 作者：Junjie.Lai on 2017/2/14 11:39
 * 邮箱：laijj@yzmm365.cn
 */
public class LotteryBuyActivity extends BaseActivity {

    /**
     * 选择的红球列表
     */
    private static final String INTENT_EXTRA_LOTTERYCHOOSELIST_REDBALLS = "INTENT_EXTRA_LOTTERYCHOOSELIST_REDBALLS";

    /**
     * 选择的蓝球列表
     */
    private static final String INTENT_EXTRA_LOTTERYCHOOSELIST_BLUEBALLS = "INTENT_EXTRA_LOTTERYCHOOSELIST_BLUEBALLS";


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
    }

    @Override
    public boolean onDone(BaseResp responseBean) {
        return false;
    }

    @Override
    public void call(int callID, Object... args) {

    }


}
