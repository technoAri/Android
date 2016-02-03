package com.example.pricebaba.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pricebaba on 2/2/16.
 */
public class VisualizerView extends View {

    private static final int LINE_WIDTH = 1;
    private static final int LINE_SCALE = 75;
    private List<Float> amplitudes;
    private int width;
    private int height;
    private Paint linePaint;
    InputStream inputStream;


    public VisualizerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //inputStream=context.getResources().openRawResource(R.drawable.waveform);
        linePaint=new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(LINE_WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w;
        height=h;
        amplitudes=new ArrayList<Float>(width/LINE_WIDTH);
        //super.onSizeChanged(w, h, oldw, oldh);
    }

    public void clear(){
        amplitudes.clear();
    }

    public void addAmplitude(float amplitude){
        amplitudes.add(amplitude);
        if (amplitudes.size() * LINE_WIDTH >= width) {
            amplitudes.remove(0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int middle=height/2;
        float curX=0;

        for (float power : amplitudes) {
            float scaledHeight = power / LINE_SCALE;
            curX += LINE_WIDTH;

            canvas.drawLine(curX,middle+scaledHeight/2,curX,middle-scaledHeight/2,linePaint);
        }
    }
}
