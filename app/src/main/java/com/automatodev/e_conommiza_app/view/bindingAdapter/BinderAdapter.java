package com.automatodev.e_conommiza_app.view.bindingAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

@SuppressLint("ResourceType")
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
        textView.setText(format.format(value != null ? value : BigDecimal.ZERO));

    }

    @BindingAdapter("android:setColor")
    public static void setColor(View imageView, int color){
        try{
            imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(), color), PorterDuff.Mode.SRC);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error setBackgroundCOlor: "+e.getMessage());
        }
    }
    @BindingAdapter("android:setStatus")
    public static void setStatus(View imageView, int status){
        try{
            switch (status){
                case 0:
                    imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.white_fff), PorterDuff.Mode.SRC);
                    break;
                case 1:
                    imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.blue_58a5f0), PorterDuff.Mode.SRC);
                    break;
                case 2:
                    imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(), R.color.yellow_ffca28), PorterDuff.Mode.SRC);
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error setBackgroundCOlor: "+e.getMessage());
        }
    }

    @BindingAdapter("android:setImage")
    public static void setImage(ImageView image, int resource){
        try{
            image.setImageResource(resource);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error binding setImage: "+e.getMessage());
        }
    }

    @BindingAdapter({"android:changeDrawable","android:imageDrawable"})
    public static void changeDrawable(ImageView imageView, TypeEnum type, int resource){
        try{
            switch (type){
                case INPUT:
                    imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(),R.color.green_00c853), PorterDuff.Mode.SRC);
                    if (resource != R.drawable.ic_uber){
                        VectorChildFinder vector = new VectorChildFinder(imageView.getContext(),resource,imageView);

                        VectorDrawableCompat.VFullPath path = vector.findPathByName("modify");
                        if (path != null) {
                            path.setFillColor(Color.parseColor("#00c853"));
                            path.setFillAlpha(1f);


                        }

                        VectorDrawableCompat.VFullPath path2 = vector.findPathByName("modifyTwo");
                        if (path2 != null) {
                            path2.setFillColor(Color.parseColor("#00c853"));
                            path2.setFillAlpha(1f);

                        }
                    }
                    break;
                case OUTPUT:
                    imageView.getBackground().setColorFilter(ContextCompat.getColor(imageView.getContext(),R.color.red_e65100), PorterDuff.Mode.SRC);
                    if (resource != R.drawable.ic_uber){
                        VectorChildFinder vector = new VectorChildFinder(imageView.getContext(),resource,imageView);

                        VectorDrawableCompat.VFullPath path = vector.findPathByName("modify");
                        if (path != null) {
                            path.setFillColor(Color.parseColor("#e65100"));
                            path.setFillAlpha(1f);

                        }

                        VectorDrawableCompat.VFullPath path2 = vector.findPathByName("modifyTwo");
                        if (path2 != null) {
                            path2.setFillColor(Color.parseColor("#e65100"));
                            path2.setFillAlpha(1f);

                        }
                    }
                    break;
                case DEFAULT:
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error binding changeDrawable: "+e.getMessage());
        }
    }
}
