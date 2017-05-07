package org.linky.qasystem.util;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public final class Tools {

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e2) {
            }
        }

        return image;
    }
}
