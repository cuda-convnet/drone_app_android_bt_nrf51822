package com.modulabs.duvallee.droneremotecontroller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * Created by duval on 2017-06-28.
 */

public class SplashConnectScreenView extends View
{
    private MainRemoteControllerActivity mMainActivity = null;

    private int mSpashScreenRetentionCount = 0;
    private int mTryConnectBTTransmitter = 0;

    private ProgressDialog mWaitProgressDialog = null;

    // 2560 x 1440
    private final int TEXT_OUT_Y_2560x1440_COORDINAGE = 1316;
    // 1920 x 1080
    private final int TEXT_OUT_Y_1920x1080_COORDINAGE = 987;

    private int TEXT_OUT_Y_COORDINAGE = 0;

    // ****************************************************************************************** //
    //
    // public SplashConnectScreenView(Context context)
    //
    //
    // ****************************************************************************************** //
    public SplashConnectScreenView(Context context)
    {
        super(context);
        mMainActivity = (MainRemoteControllerActivity) context;
        InitView();

    }

    // ****************************************************************************************** //
    //
    // public SplashConnectScreenView(Context context)
    //
    //
    // ****************************************************************************************** //
    public SplashConnectScreenView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mMainActivity = (MainRemoteControllerActivity) context;
        InitView();
    }

    // ****************************************************************************************** //
    //
    // public SplashConnectScreenView(Context context)
    //
    //
    // ****************************************************************************************** //
    public SplashConnectScreenView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mMainActivity = (MainRemoteControllerActivity) context;
        InitView();
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    public void InitView()
    {
        setBackgroundColor(Color.BLACK);
        ActionBar actionbar = mMainActivity.getSupportActionBar();
        actionbar.hide();

//        String msg = "Searching the BT Transmitter";
//        SpannableString spannablestring = new SpannableString(msg);
//        spannablestring.setSpan(new RelativeSizeSpan(1f), 0, spannablestring.length(), 0);
//        spannablestring.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannablestring.length(), 0);

//        mWaitProgressDialog = new ProgressDialog(mMainActivity, R.style.bt_search_progress_dialog);
        mWaitProgressDialog = new ProgressDialog(mMainActivity);
//        mWaitProgressDialog.setMessage("searching the BT Transmitter");
//        mWaitProgressDialog.setMessage("Searching the BT Transmitter");
        mWaitProgressDialog.setMessage(Html.fromHtml("<font color='#FF7F27'>Searching the BT Transmitter</font>"));
        mWaitProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mWaitProgressDialog.setCancelable(false);
        mWaitProgressDialog.show();

        mWaitProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mWaitProgressDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//        mWaitProgressDialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);


        // width, height
//        mWaitProgressDialog.getWindow().setLayout(1500, 10);


//        mWaitProgressDialog.getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);
//
//        WindowManager.LayoutParams params = mWaitProgressDialog.getWindow().getAttributes();
//        params.y = 1000;
//        mWaitProgressDialog.getWindow().setAttributes(params);

//        mWaitProgressDialog.getWindow().setGravity(Gravity.TOP);

        if (mMainActivity.autoConnect_drone_controller() < 0)
        {
            mWaitProgressDialog.hide();
            mSpashScreenRetentionCount = 1;
            mTryConnectBTTransmitter = 0;
        }
        else
        {
            mSpashScreenRetentionCount = 1;
            mTryConnectBTTransmitter = 1;
        }
        mSplashHandler.sendEmptyMessageDelayed(0, 1000);

        setFocusable(true);
    }

    private int x;
    private int y;

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    private void init()
    {
        Resources resource = getResources();
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    @Override
    protected void onMeasure(int wMeasureSpec, int hHeasureSpec)
    {
        int measureHeight = measureHeight(hHeasureSpec);
        int measureWidth = MeasuredWidth(wMeasureSpec);

        setBackgroundResource(R.mipmap.splash_screen);

        if (measureHeight > 1080)   // QUHD
        {
            TEXT_OUT_Y_COORDINAGE = TEXT_OUT_Y_2560x1440_COORDINAGE;
        }
        else
        {
            // Full-HD
            TEXT_OUT_Y_COORDINAGE = TEXT_OUT_Y_1920x1080_COORDINAGE;
        }

        // must be called setMeasuredDimension
        // if not called, occurred run-time error !!!
        setMeasuredDimension(measureWidth, measureHeight);
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    private int MeasuredWidth(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // calculate width of view ...
        if (specMode == MeasureSpec.AT_MOST)
        {

        } else if (specMode == MeasureSpec.EXACTLY)
        {

        }
        return specSize;
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    private int measureHeight(int measureSpec)
    {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // calculate height of view ...
        if (specMode == MeasureSpec.AT_MOST)
        {

        } else if (specMode == MeasureSpec.EXACTLY) {

        }
        return specSize;
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    // 2,560 x 1,440       (Galaxy Note 4)
    // 2,560 x 1,440 + 160 (Galaxy Note Edge)
    // 2,560 x 1,532
    @Override
    public void onDraw(Canvas canvas)
    {
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        int px = width / 2;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(80f);

        String displayText = "Drone Remote Controller for embedded lab";
        float textWidth = paint.measureText(displayText);

        canvas.drawText(displayText, px - (textWidth / 2), TEXT_OUT_Y_COORDINAGE, paint);
    }


    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        return true;
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent)
    {
        return true;
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    @Override
    public boolean onTrackballEvent(MotionEvent event)
    {
        int actionPerformed = event.getAction();
        return true;
    }

    // ****************************************************************************************** //
    //
    // void InitView()
    //
    //
    // ****************************************************************************************** //
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int actionPerformed = event.getAction();
        mMainActivity.switch_view(mMainActivity.VIEW_MAIN_MENU_SCREEN_INDEX);
        return true;
    }

    // ****************************************************************************************** //
    //
    // Handler mMainMenuUiHandler = new Handler()
    //
    //
    // ****************************************************************************************** //
    private Handler mSplashHandler = new Handler()
    {
        @Override
        // Handler events that received from UART service
        public void handleMessage(Message msg)
        {
            mSpashScreenRetentionCount--;
            if (mSpashScreenRetentionCount <= 0)
            {
                mWaitProgressDialog.dismiss();
                mMainActivity.switch_view(mMainActivity.VIEW_MAIN_MENU_SCREEN_INDEX);
            }
            else
            {
                if (mTryConnectBTTransmitter == 1)
                {

                }
                mSplashHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
}