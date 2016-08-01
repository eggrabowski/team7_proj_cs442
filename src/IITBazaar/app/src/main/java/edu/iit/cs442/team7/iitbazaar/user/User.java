package edu.iit.cs442.team7.iitbazaar.user;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import static edu.iit.cs442.team7.iitbazaar.common.MysqlDataSizes.BLOB;

public class User {


    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String major_department;
    private byte[] picture_byte;
    private byte[] picture_thumbnail_byte;
    private Bitmap picture_bitmap;
    private Bitmap picture_thumbnail_bitmap;

    public User() {}

    public final int JPEG_COMPRESSION = 100;

    public final int JPEG_COMPRESSION_INCREMENT = 1;

    public final long OPTIMUM_LENGTH = BLOB.getBytes();


    public String getFirstName() {
        return firstName;
    }

    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }


    public String getLastName() {
        return lastName;
    }

    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getMajor_department() {
        return major_department;
    }

    
    public void setMajor_department(String major_department) {
        this.major_department = major_department;
    }

    
    public void setPicture(Bitmap pictureBitmap) {
        this.picture_bitmap = pictureBitmap;
    }

    
    public void setPictureThumbnail(Bitmap pictureThumbnailBitmap) {
        this.picture_thumbnail_bitmap = pictureThumbnailBitmap;
    }

    
    public void setPicture(byte[] picture) {
        this.picture_byte = picture;
    }

    
    public void setPictureThumbnail(byte[] picture_thumbnail) {
        this.picture_thumbnail_byte = picture_thumbnail;
    }


    public byte[] getPictureByte() {

            if (null != picture_byte){
                Log.d("ImageByte", picture_byte.length + " bytes");
                return picture_byte;
            }
            else{
                if (null != picture_bitmap){
                    //encodeBitMap

                    int compressionSize = JPEG_COMPRESSION;
                    do {
                        Log.d("ImageByte", "Compressing using "+compressionSize+" quality.");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        picture_bitmap.compress(Bitmap.CompressFormat.JPEG, compressionSize, stream);
                        picture_byte = stream.toByteArray();
                        compressionSize = compressionSize - JPEG_COMPRESSION_INCREMENT;
                    }while (picture_byte.length>OPTIMUM_LENGTH);
                    Log.d("ImageByte", picture_byte.length + " bytes");
                    return picture_byte;


                }
            }

            return null;

        }

    
    public Bitmap getPictureBitmap() {
        if (null != picture_bitmap){
            return picture_bitmap;
        }
        else{
            if (null != picture_byte){
                BitmapFactory.Options options = new BitmapFactory.Options();
                picture_bitmap = BitmapFactory.decodeByteArray(picture_byte, 0, picture_byte.length, options);
                return picture_bitmap;
            }
        }

        return null;
    }


    public byte[] getPictureThumbnailByte() {



            if (null != picture_thumbnail_byte){
                Log.d("ThumbByte", picture_thumbnail_byte.length + " bytes");
                return picture_thumbnail_byte;

            }
            else{
                if (null != picture_thumbnail_bitmap){

                    int compressionSize = JPEG_COMPRESSION;
                    do {
                        Log.d("picture_thumbnail_byte", "Compressing using "+compressionSize+" quality.");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        picture_thumbnail_bitmap.compress(Bitmap.CompressFormat.JPEG, compressionSize, stream);
                        picture_thumbnail_byte = stream.toByteArray();
                        compressionSize = compressionSize - JPEG_COMPRESSION_INCREMENT;
                    }while (picture_thumbnail_byte.length>OPTIMUM_LENGTH);
                    Log.d("picture_thumbnail_byte", picture_thumbnail_byte.length + " bytes");
                    return picture_thumbnail_byte;

                }
            }

            return null;


        }

    
    public Bitmap getPictureThumbnailBitmap() {

        if (null != picture_thumbnail_bitmap){
            return picture_thumbnail_bitmap;
        }
        else{
            if (null != picture_thumbnail_byte){


               // Log.d("user picture ",Integer.toString(picture_thumbnail_byte.length));

               // picture_thumbnail_bitmap = BitmapFactory.decodeByteArray(picture_thumbnail_byte, 0, picture_byte.length-1, options);



                InputStream is = new ByteArrayInputStream(picture_thumbnail_byte);
                picture_thumbnail_bitmap = BitmapFactory.decodeStream(is);


                return picture_thumbnail_bitmap;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", major_department='" + major_department + '\'' +
                ", picture_byte=" + Arrays.toString(picture_byte) +
                ", picture_thumbnail_byte=" + Arrays.toString(picture_thumbnail_byte) +
                ", picture_bitmap=" + picture_bitmap +
                ", picture_thumbnail_bitmap=" + picture_thumbnail_bitmap +
                '}';
    }
}
