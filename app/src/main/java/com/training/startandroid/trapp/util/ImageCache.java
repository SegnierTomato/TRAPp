package com.training.startandroid.trapp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ImageCache {

    /**
     * I use cache size in kBs
     */
    private static final int DEFAULT_MEMORY_CACHE_SIZE = 1024 * 5; // 5 MB
//    private static final String TAG = "ImageCache";

    private LruCache<String, Bitmap> memoryCache;
    private Set<SoftReference<Bitmap>> reusableBitmaps;

    private ImageCache(){
        init(DEFAULT_MEMORY_CACHE_SIZE);
    }

    public static ImageCache getInstance(FragmentManager fragmentManager){

        RetainFragment retainFragment = (RetainFragment) findOrCreateRetainFragment(fragmentManager);
        ImageCache imageCache =  (ImageCache) retainFragment.getSavedObject();

        if(imageCache==null){
            imageCache = new ImageCache();
            retainFragment.setSavedObject(imageCache);
        }
        return imageCache;
    }

    private void init(final int cacheSize) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            reusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        }
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                final int bitmapSize = bitmap.getByteCount() / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    reusableBitmaps.add(new SoftReference<Bitmap>(oldValue));
                }
            }
        };
    }

    /**
     * This method iterates through the reusable bitmaps, looking for one
     * to use for inBitmap:
     */
    public Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {

        Bitmap bitmap = null;

        if (reusableBitmaps != null && !reusableBitmaps.isEmpty()) {
            Iterator<SoftReference<Bitmap>> iterator = reusableBitmaps.iterator();

            while (iterator.hasNext()) {
                Bitmap candidateForReusable = iterator.next().get();
                if (candidateForReusable != null && candidateForReusable.isMutable()) {

                    if (canUseForInBitmap(candidateForReusable, options)) {
                        bitmap = candidateForReusable;
                        iterator.remove();
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        return bitmap;
    }

    private boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            final int width = targetOptions.outWidth / targetOptions.inSampleSize;
            final int height = targetOptions.outHeight / targetOptions.inSampleSize;

            final int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
            return byteCount <= candidate.getAllocationByteCount();
        } else {

            return candidate.getWidth() == targetOptions.outWidth &&
                    candidate.getHeight() == targetOptions.outHeight && targetOptions.inSampleSize == 1;
        }
    }

    private int getBytesPerPixel(Bitmap.Config config) {

        switch (config) {
            case ARGB_8888:
                return 4;

            case ARGB_4444:
                return 2;

            case RGB_565:
                return 2;

            case ALPHA_8:
                return 1;

            default:
                return 0;
        }
    }

    public void addBitmapToCache(final String key, Bitmap bitmap) {

        if (getBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }

    }

    public Bitmap getBitmapFromCache(String key) {

        Bitmap bitmap = null;
        if (memoryCache != null) {
            bitmap = memoryCache.get(key);
        }

        return bitmap;
    }

    public void clearCache() {

        if (memoryCache != null) {
            memoryCache.evictAll();
        }
    }

    private static RetainFragment findOrCreateRetainFragment(FragmentManager fragmentManager) {

        RetainFragment retainFragment  = (RetainFragment) fragmentManager.findFragmentByTag(RetainFragment.class.getName());

        if(retainFragment == null){
            retainFragment = new RetainFragment();
            fragmentManager.beginTransaction().add(retainFragment, RetainFragment.class.getName()).commitAllowingStateLoss();
        }
        return retainFragment;
    }

    public static class RetainFragment extends Fragment {

        private Object savedObject;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }

        public void setSavedObject(Object object){
            this.savedObject = object;
        }

        public Object getSavedObject(){
            return savedObject;
        }
    }
}
