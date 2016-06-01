package com.training.startandroid.trapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.ui.AsyncDrawable;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MediaFileLoader {

//    public setLoadingBitmap(Bitmap bitmap) {
//        loadingBitmap = bitmap;
//    }

    public static void testFunc(final String imagePath, ImageView imageView) {

//        if(cacheExist(imagePath))


        if (!isTaskForCurrentImageExist(imageView)) {

            BitmapLoadingTask loadingTask = new BitmapLoadingTask( imageView);
            loadingTask.execute(imagePath);

            Bitmap bitmap = BitmapFactory.decodeResource(imageView.getContext().getResources(), R.drawable.empty_photo);

            AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getContext().getResources(), bitmap, loadingTask);
            imageView.setImageDrawable(asyncDrawable);
        }
    }

    public static boolean isTaskForCurrentImageExist(ImageView imageView) {

        BitmapLoadingTask loadingTask = getBitmapLoadingTask(imageView);
        if ( loadingTask!= null) {
            return true;
        }
        return false;
    }

    public static BitmapLoadingTask getBitmapLoadingTask(ImageView imageView) {

        if (imageView != null) {

            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapLoadingTask();
            }
        }
        return null;
    }







}
