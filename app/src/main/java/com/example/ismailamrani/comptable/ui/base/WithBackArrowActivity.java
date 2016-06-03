package com.example.ismailamrani.comptable.ui.base;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.utils.ui.WindowUtils;

import butterknife.Bind;

/**
 * Created by Mohammed Aouf ZOUAG on 03/06/2016.
 *
 * Handles the state & behavior of the back arrow button.
 */
public abstract class WithBackArrowActivity extends ColoredStatusBarActivity {

    @Bind(R.id.backArrow)
    ImageView backArrow;

    @Override
    protected void onStart() {
        super.onStart();

        // Apply a small tint effect on click
        backArrow.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    backArrow.setColorFilter(Color.argb(50, 0, 0, 0));
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    backArrow.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
                        backArrow.setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
    }

    protected void onBackArrowPressed(View view) {
        WindowUtils.hideKeyboard(this);
        onBackPressed();
    }
}
