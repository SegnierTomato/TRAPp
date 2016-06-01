package com.training.startandroid.trapp.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.training.startandroid.trapp.util.BitmapLoadingTask;

import java.lang.ref.WeakReference;

/**
 * Instead using ImageView.getDrawable for identification ImageView
 * try use some else, without Drawable objects.
 * Why? For loading bitmaps from filesystem, constructor Drawable
 * class mark like @Deprecated.
 */
public class AsyncDrawable extends BitmapDrawable {

    private WeakReference<BitmapLoadingTask> weakBitmapLoadingTaskReference;

    public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapLoadingTask loadingTask) {
        super(resources, bitmap);
        weakBitmapLoadingTaskReference = new WeakReference<BitmapLoadingTask>(loadingTask);
    }

    public BitmapLoadingTask getBitmapLoadingTask() {
        return weakBitmapLoadingTaskReference.get();
    }
}
