package com.codido.nymeria.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.codido.nymeria.R;

/**
 * 进度dialog
 */
public class ProgressDialog extends Dialog
{
	public boolean canCancel = true;
	private TextView textViewMsg;
	private String msg;

	public static ProgressDialog show(Context context, String msg) {
		ProgressDialog p = new ProgressDialog(context, R.style.dialog, msg);
		p.canCancel = false;
		p.show();
		return p;
	}

	public static ProgressDialog show(Context context) {
		String msg = null;
		return show(context, msg);
	}

	public static ProgressDialog show(Context context, OnCancelListener listener) {
		return show(context, listener, null);
	}

	public static ProgressDialog show(Context context, OnCancelListener listener, String msg) {
		ProgressDialog p = new ProgressDialog(context, R.style.dialog, msg);
		p.canCancel = true;

		if (listener != null)
			p.setOnCancelListener(listener);
		p.show();

		return p;
	}

	private Animation animation;
	private View viewLoading;

	public ProgressDialog(Context context, int theme, String msg) {
		super(context, theme);
		this.msg = msg;

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_progress_bar);

		setCanceledOnTouchOutside(false);
		setCancelable(canCancel);

		animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(false);
		animation.setRepeatMode(Animation.RESTART);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(-1);
		animation.setDuration(1000L);

		viewLoading = findViewById(R.id.viewLoading);
		textViewMsg = (TextView) findViewById(R.id.textViewMsg);

		if (msg == null) {
			textViewMsg.setVisibility(View.GONE);
		}
		else {
			textViewMsg.setVisibility(View.VISIBLE);
			textViewMsg.setText(msg);
		}
	}

	public void setMsg(String msg) {
		this.msg = msg;
		if (msg == null) {
			textViewMsg.setVisibility(View.GONE);
		}
		else {
			textViewMsg.setVisibility(View.VISIBLE);
			textViewMsg.setText(msg);
		}
	}

	public void show() {
		super.show();
		viewLoading.startAnimation(animation);
	}

}
