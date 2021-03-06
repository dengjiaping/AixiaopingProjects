package com.weslide.lovesmallscreen.view_yy.customview;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weslide.lovesmallscreen.models.ImageText;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by YY on 2017/6/10.
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageText imageText = (ImageText) path;
        Glide.with(context).load(imageText.getImage()).into(imageView);
    }
}
