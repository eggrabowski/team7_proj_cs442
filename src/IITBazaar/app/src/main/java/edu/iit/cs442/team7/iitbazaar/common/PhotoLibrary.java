package edu.iit.cs442.team7.iitbazaar.common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.util.Pair;
import android.util.Log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class PhotoLibrary {

    final public int PICTURE = 500;
    final public int PICTURE_THUMB = 100;




    final Context parentActivity;

    public PhotoLibrary(final Context c) {
        this.parentActivity = c;
    }


    //called by camera (as opposed to gallery).


    public Pair<Bitmap, Bitmap> loadPhotoPairFromPath(String currentPhotoPath) {


        Uri mFile = Uri.fromFile(new File(currentPhotoPath));

        return loadPhotoPairFromURI(mFile);

    }



    //width x height

    //used on gallery
    public Pair<Bitmap, Bitmap> loadPhotoPairFromURI(Uri uriPath) {



        try{


            Bitmap fullBitmap = getCorrectlyOrientedAndSizedImage(uriPath,PICTURE);
            if (null == fullBitmap){
                Log.e("loadPhotoPairFromURI","Full Bitmap is null");
            }
            //nullable
            Bitmap thumbNail = getCorrectlyOrientedAndSizedImage(uriPath, PICTURE_THUMB);
            if (null == thumbNail){
                Log.e("loadPhotoPairFromURI","Thumbnail  is null");
            }

            return new Pair<Bitmap, Bitmap>(fullBitmap, thumbNail);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    private int calculateInSampleSize(
            int inHeight, int inWidth, int reqWidth, int reqHeight) {
        // Raw height and width of image

        int inSampleSize = 1;

        if (inHeight > reqHeight || inWidth > reqWidth) {

            final int halfHeight = inHeight / 2;
            final int halfWidth = inWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private Bitmap getSampledBitmap(Uri photoUri, int width, int height, int maxDimension) throws IOException {

        Bitmap bitmap;
        InputStream is = parentActivity.getContentResolver().openInputStream(photoUri);
        if (width > maxDimension || height > maxDimension) {

            int maxRatio = calculateInSampleSize(width, height, maxDimension, maxDimension);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            bitmap = BitmapFactory.decodeStream(is, null, options);





        } else {
            //no decoding needed
            bitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        return bitmap;

    }


    private Pair<Integer, Integer> calculateScaledSize(

            int inWidth, int  inHeight, int reqEdge) {
        // Raw height and width of image

        int largest_side = Math.max(inHeight, inWidth);
        double normalScaleFactor = ((double) largest_side / (double) reqEdge);

        int scaledW = (int) Math.floor(inWidth / normalScaleFactor);
        int scaledH = (int) Math.floor(inHeight / normalScaleFactor);

        return new Pair<Integer, Integer>(scaledW, scaledH);

    }


    private int getOrientationByMediaStore(Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = parentActivity.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    private int getOrientationByExif(Uri photoUri) {


        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoUri.getPath());

            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Log.d("getOrientationByExif","exifOrientation = "+rotation);

            switch(rotation){


                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+90);
                    return 90;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+(-90));
                    return -90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+180);
                    return 180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+(-180));
                    return -180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+270);
                    return 270;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    Log.d("getOrientationByExif","exifOrientation resolved= "+(-270));
                    return -270;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    Log.d("getOrientationByExif","exifOrientation resolved= Undefined");
                    return 0;
                case ExifInterface.ORIENTATION_NORMAL:
                    Log.d("getOrientationByExif","exifOrientation resolved= Normal");
                    return 0;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    Log.d("getOrientationByExif","exifOrientation resolved= Normal+left/right flip");
                    return -1;
                default:
                    Log.d("getOrientationByExif","exifOrientation resolved= Unknown");
                    return -2;



            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }

    private Bitmap getCorrectlyOrientedAndSizedImage(Uri photoUri, int maxDimension) throws IOException, FileNotFoundException{
        Log.d("getCorrectlyOrientedAnd","file -> "+photoUri.getPath());

        final int inOrientation = getOrientationByExif(photoUri);
        int orientation;

        switch (inOrientation){
            case -1:
                orientation = 0;
                break;
            case -180:
                orientation = 180;
                break;
            case -90:
                orientation = 90;
                break;
            case -270:
                orientation = 270;
                break;
            default:
                orientation = inOrientation;
                break;
        }

        Log.d("getCorrectlyOrientedAnd","Orientation -> "+orientation);
        final Pair <Integer,Integer> origwXh = getWidthAndHeight(photoUri);
        final int origWidth = origwXh.first;
        final int origHeight = origwXh.second;
        final Pair <Integer,Integer> wXh = getWidthAndHeightByOrientation(origWidth, origHeight, orientation);
        final int width = wXh.first;
        final int height = wXh.second;
        final Bitmap sampleBitmap = getSampledBitmap(photoUri, width, height, maxDimension);
        final int sampledOrigWidth = sampleBitmap.getWidth();
        final int sampledOrigHeight = sampleBitmap.getHeight();
        final Pair <Integer,Integer> scaled_wXh = getWidthAndHeightByOrientation(sampledOrigWidth, sampledOrigHeight, orientation);
        final int sampledWidth = scaled_wXh.first;
        final int sampledHeight = scaled_wXh.second;
        final Bitmap scaledBitMap = getScaledBitmap(sampleBitmap, sampledWidth, sampledHeight, maxDimension);
        final Bitmap rotatedBitMap = rotate(orientation, scaledBitMap);

        Bitmap bitmap;
        switch(inOrientation){
            case -1:
                bitmap = flipHorizontal(rotatedBitMap);
                break;
            case -90:
                bitmap = flipHorizontal(rotatedBitMap);
                break;
            case -180:
                bitmap = flipHorizontal(rotatedBitMap);
                break;
            case -270:
                bitmap = flipHorizontal(rotatedBitMap);
                break;
            default:
                bitmap = rotatedBitMap;

        }


        return bitmap;

    }

    private Bitmap getScaledBitmap(Bitmap sampleBitmap, int sampledWidth, int sampledHeight, int maxDimension) {
        Pair <Integer,Integer> wXh = calculateScaledSize(sampledWidth,sampledHeight,maxDimension);
        final int scaledW = wXh.first;
        final int scaledH = wXh.second;

        final Bitmap bitmap = Bitmap.createScaledBitmap(sampleBitmap, scaledH, scaledW, false);
        return bitmap;
    }


    private Pair<Integer, Integer> getWidthAndHeightByOrientation(int origWidth, int origHeight, int orientation) throws FileNotFoundException,IOException{

        final int width = origWidth;
        final int height = origHeight;
        final int rotatedWidth;
        final int rotatedHeight;
        //this is complete wrong, why should this be rotated

        if (orientation != 90 || orientation != 270) {
            rotatedWidth = height;
            rotatedHeight = width;
        } else {
            rotatedWidth = width;
            rotatedHeight = height;
        }

        return new Pair<Integer,Integer>(rotatedWidth,rotatedHeight);
    }


    private Pair<Integer, Integer> getWidthAndHeight(Uri photoUri) throws FileNotFoundException,IOException{
        InputStream is = null;


        Log.d("getWidthAndHeight","Getting width and height from "+photoUri);
        is = parentActivity.getContentResolver().openInputStream(photoUri);

        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int photoW = dbo.outWidth;
        int photoH = dbo.outHeight;


        return new Pair<Integer,Integer>(photoW,photoH);




    }

    private Bitmap flipHorizontal(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;

    }


    private Bitmap flipVertical(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;

    }

    private Bitmap rotate(int orientation, Bitmap bitmap){


        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }

        return bitmap;


    }





}
