package com.codido.nymeria.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.codido.nymeria.R;

/**
 * 确定dialog
 */
public class OkDialog extends Dialog implements
        View.OnClickListener {
    private String title;
    private String message;
    private View.OnClickListener okOnClickListener;
    private View.OnClickListener cancelOnClickListener;

    private Button buttonOk;
    private Button buttonCancel;
    private TextView textViewMessage;
    private TextView textViewTitle;

    private String buttonOkLabel;
    private String buttonCancelLabel;

    private boolean canceledOnTouchOutside = true;
    private View viewContent;
    private View viewDialog;

    public OkDialog(Activity activity, String title, String message,
            String buttonOkLabel,
            View.OnClickListener okOnClickListener,
            String buttonCancelLabel,
            View.OnClickListener cancelOnClickListener) {
        super(activity, R.style.dialog);
        this.title = title;
        this.message = message;
        this.okOnClickListener = okOnClickListener;
        this.cancelOnClickListener = cancelOnClickListener;
        this.buttonOkLabel = buttonOkLabel;
        this.buttonCancelLabel = buttonCancelLabel;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {
            if (okOnClickListener != null) {
                okOnClickListener.onClick(v);
            }
        } else if (v == buttonCancel) {
            if (cancelOnClickListener != null) {
                cancelOnClickListener.onClick(v);
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
        setContentView(R.layout.dialog_confirm);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        viewContent = findViewById(R.id.viewContent);
        viewContent.setOnClickListener(this);
        viewDialog = findViewById(R.id.viewDialog);
        viewDialog.setOnClickListener(this);

        textViewMessage.setText(this.message == null ? "提示" : message);
        textViewTitle.setText(this.title == null ? "提示" : title);

        if (buttonOkLabel == null) {
            buttonOk.setVisibility(View.GONE);
            findViewById(R.id.viewCenterOfButtons).setVisibility(View.GONE);
        } else {
            buttonOk.setText(buttonOkLabel);
        }
        if (buttonCancelLabel == null) {
            buttonCancel.setVisibility(View.GONE);
            findViewById(R.id.viewCenterOfButtons).setVisibility(View.GONE);
        } else {
            buttonCancel.setText(buttonCancelLabel);
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
