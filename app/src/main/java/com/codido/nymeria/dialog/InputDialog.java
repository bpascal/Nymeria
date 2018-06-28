package com.codido.nymeria.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codido.nymeria.R;
import com.codido.nymeria.activity.BaseActivity;

public class InputDialog extends Dialog implements View.OnClickListener {
    private String title;
    private String hint;
    private String message;
    private boolean dismissAtButtonClicked = true;
    private OnTextInputListener onTextInputListener;

    public static interface OnTextInputListener {
        public void onTextInput(String text);
    }

    private Button buttonOk;
    private Button buttonCancel;
    private EditText editTextMessage;
    private TextView textViewTitle;

    private String buttonOkLabel;
    private String buttonCancelLabel;

    private boolean isInputNumber = false;

    private boolean canceledOnTouchOutside = true;
    private View viewContent;
    private View viewDialog;

    public InputDialog(BaseActivity baseActivity, String title, String message,
                       OnTextInputListener onTextInputListener, boolean isInputNumber) {
        super(baseActivity, R.style.dialog);
        this.title = title;
        this.message = message;
        this.isInputNumber = isInputNumber;
        this.onTextInputListener = onTextInputListener;
    }

    public InputDialog(BaseActivity baseActivity, String title, String message,
                       OnTextInputListener onTextInputListener, boolean isInputNumber, String hint) {
        super(baseActivity, R.style.dialog);
        this.title = title;
        this.message = message;
        this.isInputNumber = isInputNumber;
        this.onTextInputListener = onTextInputListener;
        this.hint = hint;
    }

    public InputDialog(BaseActivity baseActivity, String title, String message,
                       boolean dismissAtButtonClicked, OnTextInputListener onTextInputListener) {
        super(baseActivity, R.style.dialog);
        this.title = title;
        this.message = message;
        this.dismissAtButtonClicked = dismissAtButtonClicked;
        this.onTextInputListener = onTextInputListener;
    }

    public InputDialog(BaseActivity baseActivity, String title, String message,
                       OnTextInputListener onTextInputListener) {
        super(baseActivity, R.style.dialog);
        this.title = title;
        this.message = message;
        this.onTextInputListener = onTextInputListener;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonOk) {
            if (onTextInputListener != null) {
                onTextInputListener.onTextInput(BaseActivity.getTextFromEditText(editTextMessage));
            }
        } else if (v == buttonCancel) {
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
        setContentView(R.layout.dialog_input);
        editTextMessage = (EditText) findViewById(R.id.textViewMessage);
        if (isInputNumber) {
            editTextMessage.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        }
        if (hint != null) {
            editTextMessage.setHint(hint);
        }
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonOk = (Button) findViewById(R.id.buttonOk);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonOk.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        viewContent = findViewById(R.id.viewContent);
        viewContent.setOnClickListener(this);
        viewDialog = findViewById(R.id.viewDialog);
        viewDialog.setOnClickListener(this);

        BaseActivity.setTextToEditText(editTextMessage, message);
        textViewTitle.setText(this.title == null ? "提示" : title);

        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        getWindow().setWindowAnimations(R.anim.class_select_no_move);
    }

    public void setCanceledOnTouchOutside(boolean bool) {
        this.canceledOnTouchOutside = bool;
        super.setCanceledOnTouchOutside(bool);
        super.setCancelable(bool);
    }

    public OnTextInputListener getOnTextInputListener() {
        return onTextInputListener;
    }

    public void setOnTextInputListener(OnTextInputListener onTextInputListener) {
        this.onTextInputListener = onTextInputListener;
    }

}
