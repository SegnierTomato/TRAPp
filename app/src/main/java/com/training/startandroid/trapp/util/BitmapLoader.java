package com.training.startandroid.trapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;


public class BitmapLoader {

    public static synchronized Bitmap getImage(final String imagePath, final int reqHeight, final int reqWidth, ImageCache imageCache) {

        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            addInBitmapOptions(options, imageCache);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        }

        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, final int reqWidth, final int reqHeight) {

        final int imageHeight = options.outHeight;
        final int imageWidth = options.outWidth;
        int inSampleSize = 1;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth / 2;

            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth) {

                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static void addInBitmapOptions(BitmapFactory.Options options, ImageCache imageCache) {

        // inBitmap only works with mutable bitmaps, so force the decoder to
        // return mutable bitmaps.
        options.inMutable = true;

        if (imageCache != null) {
            Bitmap inBitmap = imageCache.getBitmapFromReusableSet(options);

            if (inBitmap != null) {
                options.inBitmap = inBitmap;
            }
        }
    }


}
