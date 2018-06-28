package com.codido.nymeria.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;

/**
 * 下拉对话框
 */
public class DropdownDialog extends Dialog implements View.OnClickListener {
    private String[] items;
    private int[] icons;
    private boolean canceledOnTouchOutside = true;
    private View viewContent;
    private View viewDialog;

    private OnItemClickListener onItemClickListener;

    public DropdownDialog(BaseActivity baseActivity, String[] items, int[] icons, OnItemClickListener onItemClickListener) {
        super(baseActivity, R.style.dialog);
        this.items = items;
        this.icons = icons;
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        public void onItemClick(int index);
    }

    @Override
    public void onClick(View v) {
        if (v == viewDialog) {
            return;
        } else if (v == viewContent) {
            if (!canceledOnTouchOutside) {
                return;
            }
        } else {
            int index = (Integer) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(index);
            }
        }
        dismiss();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_type_select);

        viewContent = findViewById(R.id.viewContent);
        viewContent.setOnClickListener(this);
        viewDialog = findViewById(R.id.viewDialog);
        viewDialog.setOnClickListener(this);

        LinearLayout layoutItems = (LinearLayout) findViewById(R.id.layoutItems);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < items.length; i++) {

            View item = inflater.inflate(R.layout.dialog_dropdown_select_item, null);
            TextView textViewMsg = (TextView) item.findViewById(R.id.textViewMessage);
            item.setTag(i);
            textViewMsg.setText(items[i]);
            item.setOnClickListener(this);

            View viewIcon = item.findViewById(R.id.viewIcon);

            if (icons != null && i < icons.length && icons[i] != 0) {
                viewIcon.setVisibility(View.VISIBLE);
                viewIcon.setBackgroundResource(icons[i]);
            } else {
                viewIcon.setVisibility(View.GONE);
            }

            layoutItems.addView(item, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            if (i == 0) {
                item.findViewById(R.id.viewLine).setVisibility(View.GONE);
            }
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
