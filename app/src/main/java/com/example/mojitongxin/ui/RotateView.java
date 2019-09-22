package com.example.mojitongxin.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.mojitongxin.R;

public class RotateView extends View {
    Context mContext;
    Paint mpaint;
    int RotateView_speed;//转动速度
    int RotateView_outcircle;//外围的圆个数
    Bitmap bitmap;
    Boolean isRotate;//当前图片是否转动
    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initView(context,attrs);
    }
    private void initView(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RotateView);
        for (int i = 0; i < array.length(); i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.RotateView_speed:
                    RotateView_speed = array.getInt(index, 0);
                    break;
                case R.styleable.RotateView_outcircle:
                    RotateView_outcircle=array.getInt(index,0);
                    break;
                default:
                    break;
            }
        }
    }
    private void initPaint(){
        mpaint=new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,0,0,mpaint);
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
