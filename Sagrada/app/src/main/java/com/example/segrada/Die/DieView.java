package com.example.segrada.Die;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.example.segrada.Game;
import com.example.segrada.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class DieView extends View {
    private final int[] dieImages = {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};

    private Die draftPool = Game.getInstance(null).getDraftPool();

    public DieView(Context context) {
        super(context);
        setup();
    }

    public DieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public DieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        draftPool.setRandomDiceViewVariables();
    }

    public void startRollingAnimation(){
        Runnable tick = () -> {
            // move all die
            draftPool.moveDie();
            // redraw the die
            invalidate();
        };

        // start timer thread
        Timer timer = new Timer( 10, tick, draftPool.areRolling());
        timer.start();
    }

    // A randomizer to use in onDraw()
    Random random = new Random();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < draftPool.count(); i++) {
            Dice dice = draftPool.get(i);
            int value = dice.getValue();
            int color = getResId(dice.getColor(), R.color.class);

            setLeft((int)dice.getX());
            setTop(((int)dice.getY()));
            if (dice.isRolling())
                setBackground(getResources().getDrawable(dieImages[random.nextInt(6)+1]));
            else
                setBackground(getResources().getDrawable(dieImages[value]));
            setBackgroundTintList(getResources().getColorStateList(color));
        }
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
}
