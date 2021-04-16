package com.automatodev.e_conommiza_app.view.bindingAdapter;

import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.airbnb.lottie.LottieAnimationView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BinderAdapter {

    @BindingAdapter("android:animation")
    public static void animation(LottieAnimationView lottieAnimationView, String animation){
        try{
            lottieAnimationView.setAnimation(animation);
            lottieAnimationView.playAnimation();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error setAnimation: "+e.getMessage());
        }
    }

    @BindingAdapter("android:setValue")
    public static void setValue(TextView text,Long date){
        Locale locale = new Locale("pt", "br");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy", locale);
        text.setText(dateFormat.format(date));
    }

    @BindingAdapter("android:setRawValue")
    public static void setRawValue(TextView textView, BigDecimal value){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        textView.setText(format.format(value));

    }

}
