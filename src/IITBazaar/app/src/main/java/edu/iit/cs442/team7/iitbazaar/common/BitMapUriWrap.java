package edu.iit.cs442.team7.iitbazaar.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class BitMapUriWrap implements BitMapWrap{

    private Uri bitMapUri;

    public BitMapUriWrap(Uri bitmapUri){
        this.bitMapUri = bitMapUri;
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public Bitmap getBitmap(BitmapFactory.Options options) {
        return null;
    }
}
