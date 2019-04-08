package com.codido.nymeria.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.util.MonthSelectManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 确定dialog
 */
public class MonthSelectDialog extends Dialog implements
        View.OnClickListener {

    @BindView(R.id.buttonOk)
    Button buttonOk;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;


    private boolean canceledOnTouchOutside = true;
    private View viewContent;
    private View viewDialog;

    private BaseActivity baseActivity;

    private ArrayList<Integer> monthArrayList;

    private ArrayList<Integer> initSelectList;

    MonthSelectManager monthSelectManager;

    public OnMonthSelectedListener getOnMonthSelectedListener() {
        return onMonthSelectedListener;
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
        this.onMonthSelectedListener = onMonthSelectedListener;
    }

    OnMonthSelectedListener onMonthSelectedListener;

    public static interface OnMonthSelectedListener {
        public void onMonthSelected(ArrayList<Integer> list);
    }


    public MonthSelectDialog(BaseActivity baseActivity, ArrayList<Integer> monthArrayList, ArrayList<Integer>initSelectList) {
        super(baseActivity, R.style.dialog);
        this.baseActivity = baseActivity;
        this.monthArrayList = monthArrayList;
        this.initSelectList = initSelectList;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {

            if (!monthSelectManager.isSelectRight()) {

                baseActivity.showToastText("请选择连续的还款月份");
                return;
            }

            if (onMonthSelectedListener != null) {
                onMonthSelectedListener.onMonthSelected(monthSelectManager.getSelectMonth());
            }
        } else if (v == viewDialog) {
            return;
        } else if (v == viewContent) {
            if (!canceledOnTouchOutside) {
                return;
            }
        }

        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_month_select);
        ButterKnife.bind(this);

        buttonOk.setOnClickListener(this);

        viewContent = findViewById(R.id.viewContent);
        viewContent.setOnClickListener(this);
        viewDialog = findViewById(R.id.viewDialog);
        viewDialog.setOnClickListener(this);

        textViewTitle.setText("选择还款月份");

        monthSelectManager = new MonthSelectManager(baseActivity, initSelectList);

        for (int i = 0; i < monthArrayList.size(); i++) {

            View view = monthSelectManager.createMonthSelectView(monthArrayList.get(i));

            layoutContent.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        getWindow().setWindowAnimations(R.anim.class_select_no_move);
    }

    public void setCanceledOnTouchOutside(boolean bool) {
        this.canceledOnTouchOutside = bool;
        super.setCanceledOnTouchOutside(bool);
        super.setCancelable(bool);
    }

}
