package com.example.common.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.common.R;

import androidx.annotation.Nullable;

//public class DragBubbleView extends View {
//    /**
//     * 气泡默认状态--静止
//     */
//    private final int BUBBLE_STATE_DEFAULT = 0;
//    /**
//     * 气泡相连
//     */
//    private final int BUBBLE_STATE_CONNECT = 1;
//    /**
//     * 气泡分离
//     */
//    private final int BUBBLE_STATE_APART = 2;
//    /**
//     * 气泡消失
//     */
//    private final int BUBBLE_STATE_DISMISS = 3;
//
//    /**
//     * 气泡半径
//     */
//    private float mBubbleRadius;
//    /**
//     * 气泡颜色
//     */
//    private int mBubbleColor;
//
//    public String getmTextStr() {
//        return mTextStr;
//    }
////    private Context context;
////    private AttributeSet attributeSet;
////    private int defStyleAttr;
//    public void setmTextStr(String mTextStr) {
//        this.mTextStr = mTextStr;
//        if(mTextStr.equals("0")){
////            init(context,attributeSet,defStyleAttr);
//            mBubbleState=BUBBLE_STATE_DISMISS;
//            mBubMovableCenter=mBubFixedCenter;
//            return;
//        }else {
//            mBubbleState=BUBBLE_STATE_DEFAULT;
//        }
//        invalidate();
//    }
//
//    /**
//     * 气泡消息文字
//     */
//    private String mTextStr;
//    /**
//     * 气泡消息文字颜色
//     */
//    private int mTextColor;
//    /**
//     * 气泡消息文字大小
//     */
//    private float mTextSize;
//
//    /**
//     * 不动气泡的半径
//     */
//    private float mBubFixedRadius;
//    /**
//     * 可动气泡的半径
//     */
//    private float mBubMovableRadius;
//    /**
//     * 不动气泡的圆心
//     */
//    private PointF mBubFixedCenter;
//    /**
//     * 可动气泡的圆心
//     */
//    private PointF mBubMovableCenter;
//
//    /**
//     * 气泡的画笔
//     */
//    private Paint mBubblePaint;
//
//    /**
//     * 贝塞尔曲线path
//     */
//    private Path mBezierPath;
//
//    private Paint mTextPaint;
//
//    //文本绘制区域
//    private Rect mTextRect;
//
//    private Paint mBurstPaint;
//
//    //爆炸绘制区域
//    private Rect mBurstRect;
//
//    /**
//     * 气泡状态标志
//     */
//    private int mBubbleState = BUBBLE_STATE_DEFAULT;
//    /**
//     * 两气泡圆心距离
//     */
//    private float mDist;
//    /**
//     * 气泡相连状态最大圆心距离
//     */
//    private float mMaxDist;
//    /**
//     * 手指触摸偏移量
//     */
//    private  float MOVE_OFFSET;
//
//    /**
//     * 气泡爆炸的bitmap数组
//     */
//    private Bitmap[] mBurstBitmapsArray;
//    /**
//     * 是否在执行气泡爆炸动画
//     */
//    private boolean mIsBurstAnimStart = false;
//
//    /**
//     * 当前气泡爆炸图片index
//     */
//    private int mCurDrawableIndex;
//
//    /**
//     * 气泡爆炸的图片id数组
//     */
//    private int[] mBurstDrawablesArray = {R.drawable.burst_1, R.drawable.burst_2, R.drawable.burst_3, R.drawable.burst_4, R.drawable.burst_5};
//
//    public DragBubbleView(Context context) {
//        this(context,null);
//    }
//
//    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs,0);
//    }
//
//    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//
//        super(context, attrs, defStyleAttr);
////        this.context=context;
////        this.attributeSet=attrs;
////        this.defStyleAttr=defStyleAttr;
//        init(context,attrs,defStyleAttr);
//    }
//
//    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//        //自定义属性设置
//        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.DragBubbleView,defStyleAttr,0);
//        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius,mBubbleRadius);//半径
//        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);//颜色
//        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text);//文字
//        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize,mTextSize);//文字尺寸
//        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor,Color.WHITE);//文字颜色
//        array.recycle();
//
//
//        //刚开始两个气泡半径大小一致
//        mBubFixedRadius = mBubbleRadius;//固定气泡半径
//        mBubMovableRadius = mBubbleRadius;//可动气泡半径
//        mMaxDist = 8 * mBubbleRadius;//两个气泡可拖拽的最大距离
//
//        MOVE_OFFSET = mMaxDist / 4;
//
//        //初始化气泡画笔
//        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
//        mBubblePaint.setColor(mBubbleColor);
//        mBubblePaint.setStyle(Paint.Style.FILL);
//        mBezierPath = new Path();
//
//        //初始化文本画笔
//        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mTextPaint.setColor(mTextColor);
//        mTextPaint.setTextSize(mTextSize);
//        mTextRect = new Rect();
//
//        //初始化爆炸画笔
//        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBurstPaint.setFilterBitmap(true);
//        mBurstRect = new Rect();
//        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
//        for (int i =0; i < mBurstDrawablesArray.length; i++){
//            //将气泡爆炸的drawable转为bitmap
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),mBurstDrawablesArray[i]);
//            mBurstBitmapsArray[i] = bitmap;
//        }
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        Log.d("TAG", "onSizeChanged: "+w+" "+h+" "+oldw+" "+oldh);
//        //不动气泡圆心
//        if (mBubFixedCenter == null){
//            mBubFixedCenter = new PointF(w/2,h/2);
//        }else {
//            mBubFixedCenter.set(w/2,h/2);
//        }
//        //可动气泡圆心
//        if (mBubMovableCenter == null){
//            mBubMovableCenter = new PointF(w/2,h/2);
//        }else {
//            mBubMovableCenter.set(w/2,h/2);
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Log.d("TAG", "onDraw: "+mBubFixedCenter);
//        if (mBubbleState == BUBBLE_STATE_CONNECT){
//            //绘制不动气泡
//            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, mBubFixedRadius, mBubblePaint);
//            //绘制贝塞尔曲线
//            //控制点坐标
//            int iAnchorX = (int) ((mBubFixedCenter.x + mBubMovableCenter.x) / 2);
//            int iAnchorY = (int) ((mBubFixedCenter.y + mBubMovableCenter.y) / 2);
//
//            float sinTheta = (mBubMovableCenter.y - mBubFixedCenter.y) / mDist;
//            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;
//
//            //B
//            float iBubMovableStartX = mBubMovableCenter.x + sinTheta * mBubMovableRadius;
//            float iBubMovableStartY = mBubMovableCenter.y - cosTheta * mBubMovableRadius;
//
//            //A
//            float iBubFixedEndX = mBubFixedCenter.x + mBubFixedRadius * sinTheta;
//            float iBubFixedEndY = mBubFixedCenter.y - mBubFixedRadius * cosTheta;
//
//            //D
//            float iBubFixedStartX = mBubFixedCenter.x - mBubFixedRadius * sinTheta;
//            float iBubFixedStartY = mBubFixedCenter.y + mBubFixedRadius * cosTheta;
//            //C
//            float iBubMovableEndX = mBubMovableCenter.x - mBubMovableRadius * sinTheta;
//            float iBubMovableEndY = mBubMovableCenter.y + mBubMovableRadius * cosTheta;
//
//            mBezierPath.reset();
//            mBezierPath.moveTo(iBubFixedStartX, iBubFixedStartY);
//            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);
//            //移动到B点
//            mBezierPath.lineTo(iBubMovableStartX, iBubMovableStartY);
//            mBezierPath.quadTo(iAnchorX, iAnchorY,iBubFixedEndX,iBubFixedEndY);
//            mBezierPath.close();
//            canvas.drawPath(mBezierPath, mBubblePaint);
//
//        }
//        if (mBubbleState != BUBBLE_STATE_DISMISS){
//            //默认状态下
//            canvas.drawCircle(mBubMovableCenter.x,mBubMovableCenter.y,mBubbleRadius,mBubblePaint);
//            mTextPaint.getTextBounds(mTextStr,0,mTextStr.length(),mTextRect);
//            canvas.drawText(mTextStr,mBubMovableCenter.x-mTextRect.width()/2,mBubMovableCenter.y+mTextRect.height()/2,mTextPaint);
//        }
//        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length){
//            Log.d("mile","mCurDrawableIndex:"+mCurDrawableIndex);
//
//            mBurstRect.set(
//                    (int)(mBubMovableCenter.x - mBubMovableRadius),
//                    (int)(mBubMovableCenter.y - mBubMovableRadius),
//                    (int)(mBubMovableCenter.x + mBubMovableRadius),
//                    (int)(mBubMovableCenter.y + mBubMovableRadius)
//            );
//            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex],null,mBurstRect,mBubblePaint);
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN://按下动作
//                if (mBubbleState != BUBBLE_STATE_DISMISS){
//                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x,event.getY()-mBubFixedCenter.y);
//                    if (mDist < mBubbleRadius + MOVE_OFFSET){//加上偏移量方便拖拽
//                        mBubbleState = BUBBLE_STATE_CONNECT;
//                    }else{
//                        mBubbleState = BUBBLE_STATE_DEFAULT;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_MOVE://拖拽动作
//                if (mBubbleState != BUBBLE_STATE_DEFAULT){
//                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x,event.getY()-mBubFixedCenter.y);
//                    mBubMovableCenter.x = event.getX();
//                    mBubMovableCenter.y = event.getY();
//                    if (mBubbleState == BUBBLE_STATE_CONNECT){
//                        if (mDist < mMaxDist - MOVE_OFFSET){
//                            mBubFixedRadius = mBubbleRadius - mDist / 8;//动态设置不动圆半径
//                        }else{
//                            mBubbleState = BUBBLE_STATE_APART;
//                        }
//                    }
//                    invalidate();
//                }
//                break;
//            case MotionEvent.ACTION_UP://抬起动作
//                if (mBubbleState == BUBBLE_STATE_CONNECT){
//                    startBubbleRestAnim();
//                }else if (mBubbleState == BUBBLE_STATE_APART){
//                    if (mDist < 2* mBubbleRadius){//如果拖拽距离小于2* mBubbleRadius距离
//                        startBubbleRestAnim();//回到原理位置动画
//                    }else {
//                        startBubbleBurstAnim();//气泡爆炸动画
//                        mClearUnReadCountListeren.clear();
//                    }
//
//                }
//                break;
//        }
//        return true;
//    }
//
//    private void startBubbleBurstAnim() {
//        mBubbleState = BUBBLE_STATE_DISMISS;
//        ValueAnimator anim = ValueAnimator.ofInt(0,mBurstDrawablesArray.length);
//        anim.setDuration(500);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mCurDrawableIndex = (int) animation.getAnimatedValue();
//                invalidate();
//            }
//        });
//        anim.start();
//    }
//    private ClearUnReadCountListeren mClearUnReadCountListeren;
//    public interface ClearUnReadCountListeren{
//        void clear();
//    }
//
//    public void setmClearUnReadCountListeren(ClearUnReadCountListeren mClearUnReadCountListeren) {
//        this.mClearUnReadCountListeren = mClearUnReadCountListeren;
//    }
//
//    private void startBubbleRestAnim() {
//        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),new PointF(mBubMovableCenter.x,mBubMovableCenter.y),new PointF(mBubFixedCenter
//                .x,mBubFixedCenter.y));
//        anim.setDuration(200);
//        anim.setInterpolator(new OvershootInterpolator(5f));
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mBubMovableCenter = (PointF) animation.getAnimatedValue();
//                invalidate();
//            }
//        });
//        anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mBubbleState = BUBBLE_STATE_DEFAULT;
//            }
//        });
//        anim.start();
//    }
//}
/**
 * 仿qq消息气泡
 */
public class DragBubbleView extends View {

    private Paint mBubblePaint;
    private Path mBezierPath;
    private Paint mTextPaint;
    private Rect mTextRect;
    private Paint mExplosionPaint;
    private Rect mExplosionRect;

    /* 黏连小圆的半径 */
    private float mCircleRadius;
    /* 手指拖拽气泡的半径 */
    private float mBubbleRadius;
    /* 气泡的颜色 */
    private int mBubbleColor;
    /* 气泡消息的文本 */
    private String mText;
    /* 气泡消息文本的字体大小 */
    private float mTextSize;
    /* 气泡消息文本的颜色 */
    private int mTextColor;
    /* 黏连小圆的圆心横坐标 */
    private float mCircleCenterX;
    /* 黏连小圆的圆心纵坐标 */
    private float mCircleCenterY;
    /* 手指拖拽气泡的圆心横坐标 */
    private float mBubbleCenterX;
    /* 手指拖拽气泡的圆心纵坐标 */
    private float mBubbleCenterY;
    /* 两圆圆心的间距 */
    private float d;
    /* 两圆圆心间距的最大距离，超出此值黏连小圆消失 */
    private float maxD;

    /* 黏连小圆的贝塞尔曲线起点横坐标 */
    private float mCircleStartX;
    /* 黏连小圆的贝塞尔曲线起点纵坐标 */
    private float mCircleStartY;
    /* 手指拖拽气泡的贝塞尔曲线终点横坐标 */
    private float mBubbleEndX;
    /* 手指拖拽气泡的贝塞尔曲线终点纵坐标 */
    private float mBubbleEndY;
    /* 手指拖拽气泡的贝塞尔曲线起点横坐标 */
    private float mBubbleStartX;
    /* 手指拖拽气泡的贝塞尔曲线起点纵坐标 */
    private float mBubbleStartY;
    /* 黏连小圆的贝塞尔曲线终点横坐标 */
    private float mCircleEndX;
    /* 黏连小圆的贝塞尔曲线终点纵坐标 */
    private float mCircleEndY;
    /* 贝塞尔曲线控制点横坐标 */
    private float mControlX;
    /* 贝塞尔曲线控制点纵坐标 */
    private float mControlY;

    /* 气泡爆炸的图片id数组 */
    private int[] mExplosionDrawables = {R.drawable.burst_1, R.drawable.burst_2, R.drawable.burst_3, R.drawable.burst_4, R.drawable.burst_5};
    /* 气泡爆炸的bitmap数组 */
    private Bitmap[] mExplosionBitmaps;
    /* 气泡爆炸当前进行到第几张 */
    private int mCurExplosionIndex;
    /* 气泡爆炸动画是否开始 */
    private boolean mIsExplosionAnimStart = false;

    /* 气泡的状态 */
    private int mState;
    /* 默认，无法拖拽 */
    private static final int STATE_DEFAULT = 0x00;
    /* 拖拽 */
    private static final int STATE_DRAG = 0x01;
    /* 移动 */
    private static final int STATE_MOVE = 0x02;
    /* 消失 */
    private static final int STATE_DISMISS = 0x03;
    /* 气泡状态的监听 */
    private OnBubbleStateListener mOnBubbleStateListener;
    private int mPosition;

    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.DragBubbleView,defStyleAttr,0);
        mBubbleRadius = ta.getDimension(R.styleable.DragBubbleView_bubble_radius,mBubbleRadius);//半径
        mBubbleColor = ta.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);//颜色
        mText = ta.getString(R.styleable.DragBubbleView_bubble_text);//文字
        mTextSize = ta.getDimension(R.styleable.DragBubbleView_bubble_textSize,mTextSize);//文字尺寸
        mTextColor = ta.getColor(R.styleable.DragBubbleView_bubble_textColor,Color.WHITE);//文字颜色
        mState = STATE_DEFAULT;
        mCircleRadius = mBubbleRadius;
        maxD = 8 * mBubbleRadius;
        ta.recycle();

        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);

        mBezierPath = new Path();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        mExplosionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mExplosionPaint.setFilterBitmap(true);
        mExplosionRect = new Rect();
        mExplosionBitmaps = new Bitmap[mExplosionDrawables.length];
        for (int i = 0; i < mExplosionDrawables.length; i++) {
            //将气泡爆炸的drawable转为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mExplosionDrawables[i]);
            mExplosionBitmaps[i] = bitmap;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    private int measuredDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (2 * mBubbleRadius);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData(w, h);
    }

    private void initData(int w, int h) {
        //设置圆心坐标
        mBubbleCenterX = w / 2;
        mBubbleCenterY = h / 2;
        mCircleCenterX = mBubbleCenterX;
        mCircleCenterY = mBubbleCenterY;
        mState = STATE_DEFAULT;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(mText.equals("0")){
            this.setVisibility(INVISIBLE);
            if(mOnBubbleStateListener!=null)
                mOnBubbleStateListener.onDismiss();
        }else {
            this.setVisibility(VISIBLE);
            if(mOnBubbleStateListener!=null)
                mOnBubbleStateListener.onShow();
        }

        super.onDraw(canvas);
        //画拖拽气泡
        if (mState != STATE_DISMISS) {
            canvas.drawCircle(mBubbleCenterX, mBubbleCenterY, mBubbleRadius, mBubblePaint);
        }

        if (mState == STATE_DRAG && d < maxD - maxD / 4) {
            //画黏连小圆
            canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mBubblePaint);
            //计算控制点坐标，为两圆圆心连线的中点
            mControlX = (mBubbleCenterX + mCircleCenterX) / 2;
            mControlY = (mBubbleCenterY + mCircleCenterY) / 2;
            //计算两条二阶贝塞尔曲线的起点和终点
            float sin = (mBubbleCenterY - mCircleCenterY) / d;
            float cos = (mBubbleCenterX - mCircleCenterX) / d;
            mCircleStartX = mCircleCenterX - mCircleRadius * sin;
            mCircleStartY = mCircleCenterY + mCircleRadius * cos;
            mBubbleEndX = mBubbleCenterX - mBubbleRadius * sin;
            mBubbleEndY = mBubbleCenterY + mBubbleRadius * cos;
            mBubbleStartX = mBubbleCenterX + mBubbleRadius * sin;
            mBubbleStartY = mBubbleCenterY - mBubbleRadius * cos;
            mCircleEndX = mCircleCenterX + mCircleRadius * sin;
            mCircleEndY = mCircleCenterY - mCircleRadius * cos;
            //画二阶贝赛尔曲线
            mBezierPath.reset();
            mBezierPath.moveTo(mCircleStartX, mCircleStartY);
            mBezierPath.quadTo(mControlX, mControlY, mBubbleEndX, mBubbleEndY);
            mBezierPath.lineTo(mBubbleStartX, mBubbleStartY);
            mBezierPath.quadTo(mControlX, mControlY, mCircleEndX, mCircleEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }
        //画消息个数的文本
        if (mState != STATE_DISMISS && !TextUtils.isEmpty(mText)) {
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
            canvas.drawText(mText, mBubbleCenterX - mTextRect.width() / 2, mBubbleCenterY + mTextRect.height() / 2, mTextPaint);
        }

        if (mIsExplosionAnimStart && mCurExplosionIndex < mExplosionDrawables.length) {
            //设置气泡爆炸图片的位置
            mExplosionRect.set((int) (mBubbleCenterX - mBubbleRadius), (int) (mBubbleCenterY - mBubbleRadius)
                    , (int) (mBubbleCenterX + mBubbleRadius), (int) (mBubbleCenterY + mBubbleRadius));
            //根据当前进行到爆炸气泡的位置index来绘制爆炸气泡bitmap
            canvas.drawBitmap(mExplosionBitmaps[mCurExplosionIndex], null, mExplosionRect, mExplosionPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState != STATE_DISMISS) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    d = (float) Math.hypot(event.getX() - mBubbleCenterX, event.getY() - mBubbleCenterY);
                    if (d < mBubbleRadius + maxD / 4) {
                        //当指尖坐标在圆内的时候，才认为是可拖拽的
                        //一般气泡比较小，增加(maxD/4)像素是为了更轻松的拖拽
                        mState = STATE_DRAG;
                    } else {
                        mState = STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mState != STATE_DEFAULT) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mBubbleCenterX = event.getX();
                    mBubbleCenterY = event.getY();
                    //计算气泡圆心与黏连小球圆心的间距
                    d = (float) Math.hypot(mBubbleCenterX - mCircleCenterX, mBubbleCenterY - mCircleCenterY);
//                float d = (float) Math.sqrt(Math.pow(mBubbleCenterX - mCircleCenterX, 2) + Math.pow(mBubbleCenterY - mCircleCenterY, 2));
                    if (mState == STATE_DRAG) {//如果可拖拽
                        //间距小于可黏连的最大距离
                        if (d < maxD - maxD / 4) {//减去(maxD/4)的像素大小，是为了让黏连小球半径到一个较小值快消失时直接消失
                            mCircleRadius = mBubbleRadius - d / 8;//使黏连小球半径渐渐变小
                        } else {//间距大于于可黏连的最大距离
                            mState = STATE_MOVE;//改为移动状态
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (mState == STATE_DRAG) {//正在拖拽时松开手指，气泡恢复原来位置并颤动一下
                    setBubbleRestoreAnim();
                } else if (mState == STATE_MOVE) {//正在移动时松开手指
                    //如果在移动状态下间距回到两倍半径之内，我们认为用户不想取消该气泡
                    if (d < 2 * mBubbleRadius) {//那么气泡恢复原来位置并颤动一下
                        setBubbleRestoreAnim();
                    } else {//气泡消失
                        setBubbleDismissAnim();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置气泡复原的动画
     */
    public void setBubbleRestoreAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubbleCenterX, mBubbleCenterY),
                new PointF(mCircleCenterX, mCircleCenterY));
        anim.setDuration(500);
        //自定义Interpolator差值器达到颤动效果
        anim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                //http://inloop.github.io/interpolator/
                float f = 0.571429f;
                return (float) (Math.pow(2, -4 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF curPoint = (PointF) animation.getAnimatedValue();
                mBubbleCenterX = curPoint.x;
                mBubbleCenterY = curPoint.y;
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后状态改为默认
                mState = STATE_DEFAULT;
            }
        });
        anim.start();
    }

    /**
     * 设置气泡消失的动画
     */
    public void setBubbleDismissAnim() {
        mState = STATE_DISMISS;//气泡改为消失状态
        mIsExplosionAnimStart = true;
        //做一个int型属性动画，从0开始，到气泡爆炸图片数组个数结束
        ValueAnimator anim = ValueAnimator.ofInt(0, mExplosionDrawables.length);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //拿到当前的值并重绘
                mCurExplosionIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后改变状态
                mIsExplosionAnimStart = false;
            }
        });
        anim.start();
    }

    /**
     * 气泡状态的监听器
     */
    public interface OnBubbleStateListener {

        /**
         * 气泡出现
         */
        void onShow();
        /**
         * 气泡隐藏
         */
        void onDismiss();
    }

    /**
     * 设置气泡状态的监听器
     */
    public void setOnBubbleStateListener(OnBubbleStateListener onBubbleStateListener) {
        mOnBubbleStateListener = onBubbleStateListener;
    }

    /**
     * 设置气泡消息个数文本
     *
     * @param text 消息个数文本
     */
    public void setText(String text) {
        mText = text;
        invalidate();
    }

    /**
     * 重新生成气泡
     */
    public void reCreate() {
        initData(getWidth(), getHeight());
        invalidate();
    }
}
