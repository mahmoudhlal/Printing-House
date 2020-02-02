package com.mahmoud.printinghouse.Utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.mahmoud.printinghouse.R;
import com.squareup.picasso.Picasso;

public class ImageBinding {

    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView image,String url) {
        if (!url.equals("")) {
            if (url.endsWith(".pdf")) {
                image.setPadding(35, 5, 35, 5);
                image.setImageDrawable(image.getContext().getResources().getDrawable(R.drawable.ic_pdf));
            } else
                Picasso.get().load(url).
                        placeholder(image.getContext().getResources().getDrawable(R.drawable.ic_launcher_background)).
                        into(image);
        } else {
            image.setPadding(35,5,35,5);
            image.setImageDrawable(image.getContext().getResources().getDrawable(R.drawable.ic_group));
        }
    }

    @BindingAdapter("bind:checkType")
    public static void loadImage(ImageView iv, Bitmap bitmap) {
        if (bitmap != null)
            iv.setImageBitmap(bitmap);
    }

    @BindingAdapter("bind:checkVisibility")
    public static void visible(TextView iv, String type) {
        switch (iv.getId()) {
            case R.id.content:
            /*case R.id.formula:*/
                if (!type.equals("shields"))
                    iv.setVisibility(View.GONE);
                else
                    iv.setVisibility(View.VISIBLE);
                break;
            case R.id.paperType:
            case R.id.paperSize:
                if (!type.equals("files"))
                    iv.setVisibility(View.GONE);
                else
                    iv.setVisibility(View.VISIBLE);
                break;
            case R.id.copiesNum:
                if (type.equals("shields"))
                    iv.setVisibility(View.GONE);
                else
                    iv.setVisibility(View.VISIBLE);
                break;
        }
    }

    /*@BindingAdapter("bind:checkType")
    public static void loadImage(ImageView iv, AttachItem attachItem) {
        if (attachItem.bitmap != null)
            iv.setImageBitmap(attachItem.bitmap);
        else {
            if (attachItem.getType().equals("image"))
                Picasso.get().load(attachItem.getFile()).
                        placeholder(iv.getContext().getResources().getDrawable(R.drawable.ic_launcher_background)).
                        into(iv);
            else {
                try {
                    iv.setImageBitmap(UploadVideo.getInstance().retrieveVideoFrameFromVideo(attachItem.getFile()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }*/
}
