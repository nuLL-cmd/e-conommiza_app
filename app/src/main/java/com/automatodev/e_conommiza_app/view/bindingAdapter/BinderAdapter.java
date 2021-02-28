package com.automatodev.e_conommiza_app.view.bindingAdapter;

import android.util.Log;

import androidx.databinding.BindingAdapter;

import com.airbnb.lottie.LottieAnimationView;

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
}
