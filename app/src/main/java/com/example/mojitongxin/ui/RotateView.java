package com.example.mojitongxin.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mojitongxin.R;

import java.util.logging.LogRecord;

/**
 * 旋转view，作废
 */
public class RotateView extends View{
    private static final String TAG = "RotateView";
    private Context mContext;
    private Paint mpaint;
    private float viewWidth;
    private float viewHeight;
    private int rotateView_speed;//转动速度
    private int rotateView_outcircle;//外围的圆个数
    private Bitmap bitmap;
    private Boolean isRotate;//当前图片是否转动
    private static int curRotate=0;

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        initPaint();
        initView(context,attrs);
    }

    /**
     * 初始化View，读取attr
     * @param context
     * @param attrs
     */
    private void initView(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RotateView);
        for (int i = 0; i < array.length(); i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.RotateView_speed:
                    rotateView_speed = array.getInt(index, 0);
                    break;
                case R.styleable.RotateView_outcircle:
                    rotateView_outcircle=array.getInt(index,0);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 画笔初始化
     */
    private void initPaint(){
        mpaint=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight=MeasureSpec.getSize(heightMeasureSpec);
    }
    Handler handler=new Handler();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCricle(canvas);

        handler.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
                handler.postDelayed(this,500);
            }
        });
    }
//    private static Canvas cv=null;
    public void drawCricle(Canvas canvas)
    {
//        int save = canvas.save();
//        if(cv==null)
//        {
//
//        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius;
        if (width > height) {
            radius = height / 2;
        } else {
            radius = width / 2;
        }
        canvas.rotate(curRotate,viewWidth/2,viewHeight/2);
        canvas.drawCircle(viewWidth/2,viewHeight/2,radius,mpaint);
        mpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,viewWidth/2-radius,viewHeight/2-radius,mpaint);
        mpaint.setXfermode(null);
        canvas.rotate(90,viewWidth/2,viewHeight/2);
        curRotate+=rotateView_speed;
        if(curRotate%360==0)
        {
            curRotate=0;
        }
//        canvas.restoreToCount(save);
    }
    //————————————————对外接口start——————————————————//
    public void setSource(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    public void setPause()
    {
        if(isRotate==true){
            //暂停
        }
    }
    public void setPlay()
    {
        if(isRotate!=true)
        {
            //播放
        }
    }
    //————————————————对外接口end——————————————————//
}
