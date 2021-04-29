package com.automatodev.e_conommiza_app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.automatodev.e_conommiza_app.R;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kishandonga.csbx.CustomSnackbar;
@SuppressLint("UseCompatLoadingForDrawables")
public class ComponentUtils {

    private Context context;


    public ComponentUtils(Context context){
        this.context = context;
    }

    public void stateColorComponent(View views[], Integer[]resources, boolean condition){

       if (condition){
           ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
           ((Activity) context).getWindow().setStatusBarColor(context.getResources().getColor(R.color.background_form));
           //btnUpItem
            views[0].getBackground().setColorFilter(ContextCompat.getColor(context, R.color.background_form), PorterDuff.Mode.SRC);
           ((ImageButton)views[0]).setImageDrawable(context.getResources().getDrawable(resources[0]));
           //AppBar
           views[1].setBackground(context.getResources().getDrawable(R.color.background_form));
           //txtWindowItem
           ((TextView)views[2]).setTextColor(context.getResources().getColor(android.R.color.black));
           //txtAppItem
           ((TextView)views[3]).setTextColor(context.getResources().getColor(R.color.color_gray_CDCBCB));
           //txtPerspectiveItem
           views[4].setBackgroundResource(R.drawable.bg_edt_blue);
           //btnSave
            //views[5].getBackground().setColorFilter(ContextCompat.getColor(context, R.color.background_form), PorterDuff.Mode.SRC);
           ((Button) views[5]).setTextColor(context.getResources().getColor(R.color.button_positive));
           //btnDate
           views[6].getBackground().setColorFilter(ContextCompat.getColor(context, R.color.button_positive), PorterDuff.Mode.SRC);


       }else{
           ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(views[0].getSystemUiVisibility());
           ((Activity)context).getWindow().setStatusBarColor(context.getResources().getColor(resources[0]));
           //btnUpItem
           views[1].getBackground().setColorFilter(ContextCompat.getColor(context, resources[0]), PorterDuff.Mode.SRC);
           ((ImageButton)views[1]).setImageDrawable(context.getResources().getDrawable(resources[1]));
           //btnDownItem
           views[2].getBackground().setColorFilter(ContextCompat.getColor(context, R.color.background_form), PorterDuff.Mode.SRC);
           ((ImageButton)views[2]).setImageDrawable(context.getResources().getDrawable(resources[2]));
           //btnDate
            views[3].getBackground().setColorFilter(ContextCompat.getColor(context,resources[0]), PorterDuff.Mode.SRC);
            //btnSave
           //views[4].getBackground().setColorFilter(ContextCompat.getColor(context, resources[0]), PorterDuff.Mode.SRC);
           ((Button) views[4]).setTextColor(context.getResources().getColor(resources[0]));
           //appbar
           views[5].setBackground(context.getResources().getDrawable(resources[0]));
           //txtWindowItem
           ((TextView)views[6]).setTextColor(context.getResources().getColor(android.R.color.white));
           //txtAppItem
           ((TextView)views[7]).setTextColor(context.getResources().getColor(android.R.color.white));
           //txtPerspectiveItem
           views[8].setBackgroundResource(resources[3]);
       }

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
        snackbar.padding(120);
        snackbar.cornerRadius(15);
        snackbar.duration(duration);
        snackbar.show();
    }

    public RequestListener<Drawable> listenerFadeImage(View image, int duration){
       return new RequestListener<Drawable>() {
           @Override
           public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
               image.animate().setDuration(duration).alpha(1f).start();
               return false;
           }

           @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }
        };
    }


}
