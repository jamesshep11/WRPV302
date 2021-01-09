package com.example.segrada.Die;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.segrada.R;

import java.lang.reflect.Field;

public class DiceView extends View {
    private final int[] diceImages = {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};
    private String border;

    Dice dice = new Dice("white");

    public DiceView(Context context) {
        super(context);
        setup();
    }

    public DiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public DiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup(){
        border = "white";
    }

    public void setDice(Dice dice){
        this.dice = dice;

        setBackground(getResources().getDrawable(diceImages[dice.getValue()]));

        int color = getResId(dice.getColor(), R.color.class);
        setBackgroundTintList(getResources().getColorStateList(color));
        setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
    }

    public void setBorder(String border){
        this.border = border;
    }

    private int getResId(String rec, Class<?> aClass) {
        try {
            Field field = aClass.getDeclaredField(rec);
            return field.getInt(field);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Dice getDice(){
        return dice;
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @Override
    protected void onDraw(Canvas canvas) {

        /*int borderColor = getResId(border, R.color.class);
        paint.setColor(borderColor);

        canvas.drawRect(dice.getX(), dice.getY(), dice.getX() + getMeasuredWidth(), dice.getY() + getMeasuredHeight(), paint);*/

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(dice.getWidth(), dice.getHeight());
    }
}
