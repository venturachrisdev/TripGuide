package com.blancgrupo.apps.tripguide;

import android.net.Uri;
import android.support.v4.content.FileProvider;

/**
 * Created by venturachrisdev on 10/2/17.
 */

public class ImageFileProvider extends FileProvider {
    @Override
    public String getType(Uri uri) {
        return "image/jpeg";
    }
}
