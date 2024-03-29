package br.com.filnat.gamb;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;


public class WidgetActivity extends Service {

    int LAYOUT_FLAG;
    View mFloatingView;
    WindowManager windowManager;
    ImageView imageClose;
    TextView tvWidget;
    float height,width;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }


        mFloatingView = LayoutInflater.from(this).inflate(R.layout.activity_widget, null);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140,
                140,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        imageParams.gravity = Gravity.BOTTOM|Gravity.CENTER;
        imageParams.y = 100;

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        imageClose = new ImageView(this);
        imageClose.setImageResource(R.mipmap.logo);
        imageClose.setVisibility(View.INVISIBLE);
        windowManager.addView(imageClose, imageParams);
        windowManager.addView(mFloatingView, layoutParams);
        mFloatingView.setVisibility(View.VISIBLE);

        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();

        tvWidget = (TextView)mFloatingView.findViewById(R.id.text_widget);

        tvWidget.setOnTouchListener(new View.OnTouchListener() {
            int initialX, initialY;
            float initialTouchX, initialTouchY;
            long startClickTime;

            int MAX_CLICK_DURATION=200;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        imageClose.setVisibility(View.VISIBLE);

                        initialX = layoutParams.x;
                        initialY = layoutParams.y;

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                        return true;
                    case MotionEvent.ACTION_UP:
                        long clickDuration = Calendar.getInstance().getTimeInMillis()-startClickTime;
                        imageClose.setVisibility(View.GONE);

                        layoutParams.x = initialX+(int)(initialTouchX-motionEvent.getRawX());
                        layoutParams.y = initialY+(int)(motionEvent.getRawY()-initialTouchY);

                        if(clickDuration<MAX_CLICK_DURATION)
                        {
                            Toast.makeText(WidgetActivity.this, "Time: "+tvWidget.getText().toString(), Toast.LENGTH_SHORT).show();
                        }else{
                            if(layoutParams.y>(height*0.6))
                            {
                                stopSelf();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        layoutParams.x = initialX+(int)(initialTouchX-motionEvent.getRawX());
                        layoutParams.y = initialY+(int) (motionEvent.getRawY()-initialTouchY);

                        windowManager.updateViewLayout(mFloatingView,layoutParams);

                        if(layoutParams.y>(height*0.6))
                        {
                            imageClose.setImageResource(R.mipmap.logo);
                        }else{
                            imageClose.setImageResource(R.mipmap.logo_round);
                        }
                        return true;
                }

                return false;
            }
        });


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null)
        {
            windowManager.removeView(mFloatingView);
        }

        if(imageClose!=null)
        {
            windowManager.removeView(imageClose);
        }
    }

}
