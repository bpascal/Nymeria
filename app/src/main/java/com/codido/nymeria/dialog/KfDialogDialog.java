package com.codido.nymeria.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;
import com.codido.nymeria.bean.vo.KfPhoneVo;
import com.codido.nymeria.util.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 确定dialog
 */
public class KfDialogDialog extends Dialog implements
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

    private ArrayList<KfPhoneVo> kfPhoneVoArrayList;

    class KfPhoneItem implements View.OnClickListener {

        View root;
        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.textViewPhone)
        TextView textViewPhone;
        KfPhoneVo kfPhoneVo;


        public KfPhoneItem(View root, final KfPhoneVo kfPhoneVo) {
            this.root = root;
            ButterKnife.bind(this, this.root);
            this.kfPhoneVo = kfPhoneVo;

            textViewName.setText(kfPhoneVo.getName());
            textViewPhone.setText(kfPhoneVo.getPhone());

            this.root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Global.debug("拨打电话:"+kfPhoneVo.toString());
            baseActivity.callPhone(this.kfPhoneVo.getPhone());
            dismiss();
        }
    }

    public KfDialogDialog(BaseActivity baseActivity, ArrayList<KfPhoneVo> kfPhoneVoArrayList) {
        super(baseActivity, R.style.dialog);
        this.baseActivity = baseActivity;
        this.kfPhoneVoArrayList = kfPhoneVoArrayList;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {

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
        setContentView(R.layout.dialog_kf_phone);
        ButterKnife.bind(this);

        buttonOk.setOnClickListener(this);

        viewContent = findViewById(R.id.viewContent);
        viewContent.setOnClickListener(this);
        viewDialog = findViewById(R.id.viewDialog);
        viewDialog.setOnClickListener(this);

        textViewTitle.setText("客服电话");


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < kfPhoneVoArrayList.size(); i++) {
            View view = layoutInflater.inflate(R.layout.item_kf_phone, null);
            new KfPhoneItem(view, kfPhoneVoArrayList.get(i));
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
