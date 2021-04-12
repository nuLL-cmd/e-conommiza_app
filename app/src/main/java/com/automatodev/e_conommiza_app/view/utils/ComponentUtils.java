package com.automatodev.e_conommiza_app.view.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.automatodev.e_conommiza_app.R;
import com.kishandonga.csbx.CustomSnackbar;

public class ComponentUtils {

    private Context context;

    public ComponentUtils(Context context){
        this.context = context;
    }

    public void fadeViewEffect(ImageButton[] buttons, Integer[]resources, long duration, boolean condition){
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons[0], "alpha",1f,0f);
        animator.setDuration(duration);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttons[0].setBackground(context.getResources().getDrawable(resources[0]));
                buttons[0].setImageDrawable(context.getResources().getDrawable(resources[1]));
                if (condition){
                    buttons[1].setBackground(context.getResources().getDrawable(resources[2]));
                    buttons[1].setImageDrawable(context.getResources().getDrawable(resources[3]));
                }

                ObjectAnimator animator = ObjectAnimator.ofFloat(buttons[0],"alpha",0f,1f);
                animator.setDuration(duration);
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void fadeViewEffect(Button button, int resource, long duration){
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "alpha",1f,0f);
        animator.setDuration(duration);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                button.setBackground(context.getResources().getDrawable(resource));
                ObjectAnimator animator = ObjectAnimator.ofFloat(button,"alpha",0f,1f);
                animator.setDuration(duration);
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void onTextListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    editText.setBackgroundResource(R.drawable.layout_shadow_white);
                else
                    editText.setBackgroundResource(R.drawable.bg_edt_global_error);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void showSnackbar(String message, int duration){
        CustomSnackbar snackbar = new CustomSnackbar(context);
        snackbar.message(message);
        snackbar.padding(25);
        snackbar.cornerRadius(15);
        snackbar.duration(duration);
        snackbar.show();
    }







}
