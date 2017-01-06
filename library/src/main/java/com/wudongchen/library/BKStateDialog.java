package com.wudongchen.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * BKStateDialog
 *
 * @author dong-chen.wu
 * @version 1.0
 * @date 2016/12/22
 */
public class BKStateDialog extends ProgressDialog implements StateView.Callback {

    private String mMessage;

    private StateView.State mState = StateView.State.SUCCESS;

    private Callback mCallback;

    public BKStateDialog setOnFinish(Callback callback) {
        mCallback = callback;
        return this;
    }

    public void setMessage(@StringRes int message) {
        setMessage(getContext().getResources().getString(message));
    }

    public BKStateDialog setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public BKStateDialog(Context context, StateView.State state) {
        super(context, R.style.StateDialogStyle);
        this.mState = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.argb(120, 0, 0, 0));
        gradientDrawable.setCornerRadius(15);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWidth() / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        layout.setBackground(gradientDrawable);

        StateView wdcView = new StateView(getContext(), mState);
        wdcView.setCallback(this);
        LinearLayout.LayoutParams wdcViewParams = new LinearLayout.LayoutParams(getWidth() / 6, getWidth() / 6);
        wdcViewParams.gravity = Gravity.CENTER;
        wdcView.setLayoutParams(wdcViewParams);
        layout.addView(wdcView);

        if (!TextUtils.isEmpty(mMessage)) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 20);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            textView.setTextColor(Color.rgb(255, 255, 255));
            textView.setGravity(Gravity.CENTER);
            textView.setText(mMessage);
            layout.addView(textView);
        }

        setContentView(layout, params);
    }

    private Handler mHandler = new Handler();

    @Override
    public void onDismiss() {
        mHandler.postDelayed(mRunnable, 500);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isShowing()) {
                dismiss();
                if (mCallback != null)
                    mCallback.onFinish();
            }
        }
    };

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mRunnable);
    }

    public interface Callback {

        void onFinish();
    }

    private int getWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        return point.x;
    }
}

