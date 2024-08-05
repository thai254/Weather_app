package com.ltuddd.weatherapi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomizeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomizeTextView(Context context) {
        super(context);
    }

    public CustomizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint p = new Paint();
    private String mText;

    @Override
    protected void onDraw(Canvas canvas){
        mText = this.getText().toString();

        p.clearShadowLayer();
        p.setTextSize(this.getTextSize());
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeWidth(5);
        p.setColor(Color.BLACK);
        canvas.drawText(mText, 10, getLineHeight()-10, p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(this.getTextColors().getDefaultColor());
        canvas.drawText(mText, 10, getLineHeight()-10, p);
    }
}
