package com.training.startandroid.trapp.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ImageFetcher {

    private static final String LOG_TAG = ImageFetcher.class.getSimpleName();
    private static ImageCache mImageCache;
    private boolean mExitTasksEarly = false;
    private boolean mPauseWork = false;

    public ImageFetcher(Fragment fragment) {
        FragmentManager fragmentManager = FragmentHelper.getLowLevelFragmentManager(fragment);
        mImageCache = ImageCache.getInstance(fragmentManager);
    }

    public ImageFetcher(FragmentManager fragmentManager) {
        mImageCache = ImageCache.getInstance(fragmentManager);
    }

    /**
     * Returns true if the current work has been canceled or if there was no work in
     * progress on this image view.
     * Returns false if the work in progress deals with the same data. The work is not
     * stopped in that case.
     */
    private static boolean cancelOtherBitmapLoadingTask(ImageView imageView, final String imagePath) {

        BitmapLoadingTask loadingTask = getBitmapLoadingTask(imageView);

        if (loadingTask != null) {
            ImageView imageViewInTask = loadingTask.mWeakReference.get();

            if (imageViewInTask != null) {

                if (loadingTask.imagePath.equals(imagePath)) {
                    return false;
                }

            }

            loadingTask.cancel(true);
            return true;
        }

        return true;
    }

    private static BitmapLoadingTask getBitmapLoadingTask(ImageView imageView) {

        if (imageView != null) {
            Drawable imageDrawable = imageView.getDrawable();

            if (imageDrawable != null) {
                if (imageDrawable instanceof AsyncDrawable) {
                    AsyncDrawable asyncDrawable = (AsyncDrawable) imageDrawable;
                    return asyncDrawable.getBitmapLoadingTask();
                }
            }
        }

        return null;
    }

    public void setPauseBackgroundLoadingTask(boolean pause) {
        mPauseWork = pause;
    }

    public void setExitTasksEarly(boolean exitTasks) {
        mExitTasksEarly = exitTasks;
    }

    public void loadImage(ImageView imageView, int reqHeight, int reqWidth, final String imagePath) {

        Bitmap bitmap = null;

        if (mImageCache != null) {
            bitmap = mImageCache.getBitmapFromCache(imagePath);
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {

            if (cancelOtherBitmapLoadingTask(imageView, imagePath)) {

                imageView.setImageBitmap(BitmapLoader.getImage(imagePath, reqHeight, reqWidth, mImageCache));

                BitmapLoadingTask loadingTask = new BitmapLoadingTask(imageView, reqHeight, reqWidth, mImageCache);
                AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getResources(), null, loadingTask);
                imageView.setImageDrawable(asyncDrawable);

                loadingTask.execute(imagePath);

//                loadingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePath);
//                task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR);
            }

        }
    }

    private class BitmapLoadingTask extends AsyncTask<String, Void, Bitmap> {

        private final int reqHeight;
        private final int reqWidth;
        private WeakReference<ImageView> mWeakReference;
        private ImageCache mImageCache;
        private String imagePath;

        public BitmapLoadingTask(ImageView imageView, int reqHeight, int reqWidth, ImageCache imageCache) {

            this.mWeakReference = new WeakReference(imageView);
            this.reqHeight = reqHeight;
            this.reqWidth = reqWidth;
            this.mImageCache = imageCache;
        }

        @Override
        protected Bitmap doInBackground(String... imagePaths) {

            Log.d(LOG_TAG, "doInBackground");

            Bitmap bitmap = null;
            this.imagePath = imagePaths[0];

            try {
//                if (mPauseWork && !isCancelled()) {
//                    synchronized (this) {
//                        this.wait();
//                    }
//                }

                if (mImageCache != null && !isCancelled() && !mExitTasksEarly) {
                    bitmap = mImageCache.getBitmapFromCache(imagePaths[0]);
                }

                if (bitmap == null && !isCancelled() && !mExitTasksEarly) {
                    bitmap = BitmapLoader.getImage(imagePaths[0], reqHeight, reqWidth, mImageCache);

                    if (mImageCache != null) {
                        mImageCache.addBitmapToCache(imagePaths[0], bitmap);
                    }
                }


            } catch (Exception ex) {
                Log.d(LOG_TAG, ex.toString());
            } finally {
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            Log.d(LOG_TAG, "onPostExecute");

            try {

                if (isCancelled() || mExitTasksEarly) {
                    bitmap = null;
                }

//                if (mPauseWork && bitmap != null) {
//                    synchronized (this) {
//                        this.wait();
//                    }
//                }

                ImageView imageView = mWeakReference.get();
                if (imageView != null && bitmap != null) {
                    Log.d(LOG_TAG, "setBitmap");
                    imageView.setImageBitmap(bitmap);

                    Log.d(LOG_TAG, "height: " + imageView.getHeight());
                    Log.d(LOG_TAG, "width: " + imageView.getWidth());

                } else if (imageView != null) {
                    Toast.makeText(imageView.getContext(), "Image not found!\nTry select other image.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                Log.d(LOG_TAG, ex.toString());
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(LOG_TAG, "onCancelled");
//            synchronized (this) {
//                this.notifyAll();
//            }
        }
    }

    private class AsyncDrawable extends BitmapDrawable {

        private WeakReference<BitmapLoadingTask> weakBitmapLoadingTaskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapLoadingTask loadingTask) {

            super(resources, bitmap);
            weakBitmapLoadingTaskReference = new WeakReference<BitmapLoadingTask>(loadingTask);
        }

        public BitmapLoadingTask getBitmapLoadingTask() {
            return weakBitmapLoadingTaskReference.get();
        }
    }
}
