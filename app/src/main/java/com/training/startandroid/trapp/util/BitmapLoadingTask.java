package com.training.startandroid.trapp.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * In future using method  executeOnExecutor() for starting AsyncTask threads.
 * <p/>
 * In Android 3.0 and higher, working with one background thread.
 * Other threads waiting.
 * If you want using really multithreading working - use method
 * executeOnExecutor for starting thread working.
 */

public class BitmapLoadingTask extends AsyncTask<String, Void, Bitmap> {

    private final int reqHeight;
    private final int reqWidth;
    private WeakReference<ImageView> weakReference;

    public BitmapLoadingTask(ImageView imageView) {

        this.weakReference = new WeakReference(imageView);
        this.reqHeight = imageView.getHeight();
        this.reqWidth = imageView.getWidth();
    }

    @Override
    public Bitmap doInBackground(String... imagePaths) {

        Bitmap bitmap = BitmapLoader.getImage(imagePaths[0], reqHeight, reqWidth, null);
        return bitmap;
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = weakReference.get();
        if (imageView != null && bitmap != null) {

            if (this == MediaFileLoader.getBitmapLoadingTask(imageView)) {
                imageView.setImageBitmap(bitmap);
            }
        }

    }
}
