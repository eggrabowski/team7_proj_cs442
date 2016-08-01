package edu.iit.cs442.team7.iitbazaar.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public interface BitMapWrap {

    Bitmap getBitmap();

    Bitmap getBitmap(BitmapFactory.Options options);

}
