package com.example.shield;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Random;

/**
 * Created by effy on 2018/10/31.
 */

public class ColorCircleDrawable extends Drawable {
    String mTitle;
    Paint mPaint;
    int size;
    float titleSpace = 0.5f;
    Paint backgroundPaint;
    int cx, cy;
    private int radius;
    private int tx, ty;

    static int i =0;

    String[] color_arr = {"#FF3366","#CC33FF","#99CCFF","#FF3333","#99FFFF","#66FFCC","#CCFF00","#CCFFFF","#CCFFFF","#FF6633","#FFFFF","#CCFFCC","#66FF33","#99FF00","#99FF99","#00FFFF","#FF0066","#9933FF","#FF3300","#6633FF","#33FFCC","#FF0033","#00FFCC","#CC00FF","#99FF66","#00e09e","#0c0ebd7"};



    /**
     *
     * @param title 标题
     * @param color 背景色
     */
    public ColorCircleDrawable(String title) {
        mTitle = title;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        //文字水平居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

//        int index = (int)Math.random()*(color_arr.length-1);
//        int index = (int)Math.random()*(color_arr.length-1);
if (i<color_arr.length) {

    backgroundPaint.setColor(Color.parseColor(color_arr[i]));
    i++;
}else{
    i = 0;
    backgroundPaint.setColor(Color.parseColor(color_arr[i]));
}


//        backgroundPaint.setColor(color);
        backgroundPaint.setAntiAlias(true);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, backgroundPaint);
        //drawText中的，x和文字的Paint的Align属性有关
        //y是指文字baseLine的位置。
        canvas.drawText(mTitle, cx, ty, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        size = Math.min(bounds.height(), bounds.width());
        int textSize = (int) (size * titleSpace / mTitle.length());
        mPaint.setTextSize(textSize);
        radius = size / 2;
        cx = bounds.width() / 2;
        cy = bounds.height() / 2;
        //正确获取字体的高度，在绘制的时候需要向上偏移fontMetricsInt.bottom
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int fontHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        ty = cy + fontHeight / 2 - fontMetricsInt.bottom;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}

