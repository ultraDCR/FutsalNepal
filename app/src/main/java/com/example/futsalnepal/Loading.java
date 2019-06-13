package com.example.futsalnepal;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class Loading {
    Activity activity;
    Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public Loading(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.loading_dialog);

        //...initialize the imageView form infalted layout
//        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
//
//        /*
//        it was never easy to load gif into an ImageView before Glide or Others library
//        and for doing this we need DrawableImageViewTarget to that ImageView
//        */
//        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
//
//        //...now load that gif which we put inside the drawble folder here with the help of Glide
//
//        Glide.with(activity)
//                .load(R.drawable.loading_anim)
//                .placeholder(R.drawable.loading_anim)
//                .centerCrop()
//                .into(imageViewTarget);

        //...finaly show it
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }

}

