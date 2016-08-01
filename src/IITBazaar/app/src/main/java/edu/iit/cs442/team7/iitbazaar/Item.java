package edu.iit.cs442.team7.iitbazaar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.graphics.drawable.VectorDrawable;
import android.util.Log;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;



import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;


import static edu.iit.cs442.team7.iitbazaar.common.MysqlDataSizes.*;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public class Item {

    public final int JPEG_COMPRESSION = 100;

    public final int JPEG_COMPRESSION_INCREMENT = 1;

    public final long OPTIMUM_LENGTH = BLOB.getBytes();

    public final String prefix = "iitbazaar://item.";


    private int itemNumber = -1;
    private String title;
    private Date listingStartDate;
    private Date listingEndDate;
    private Long listingStartDateLong;
    private Long listingEndDateLong;
    private String itemDescription;
    private String listingUserEmail;
    private String price;
    private int category;

    private com.google.zxing.qrcode.encoder.QRCode qrCode;

    private Bitmap imageBitmap;
    private Bitmap thumbnailBitmap;
    private byte[] imageByte;
    private byte[] thumbByte;



    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Date getListingStartDate() {

        if (null == listingStartDate && null != listingEndDateLong) {
            listingStartDate = new Date(listingEndDateLong);
        }

        return listingStartDate;
    }

    public Long getListingStartDateLong() {


        if (null == listingStartDateLong && null != listingStartDate) {
            listingStartDateLong = listingStartDate.getTime();
        }
        return listingStartDateLong;

    }

    public void setListingStartDate(final Long listingStartDateLong) {
        this.listingStartDateLong = listingStartDateLong;
    }

    public void setListingStartDate(final Date listingStartDate) {
        this.listingStartDate = listingStartDate;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(final String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getListingUserEmail() {
        return listingUserEmail;
    }

    public void setListingUserEmail(final String listingUserEmail) {
        this.listingUserEmail = listingUserEmail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getListingEndDate() {

        if (null == listingEndDate && null != listingEndDateLong) {
            listingEndDate = new Date(listingEndDateLong);
        }

        return listingEndDate;
    }

    public Long getListingEndDateLong() {


        if (null == listingEndDateLong && null != listingEndDate) {
            listingEndDateLong = listingEndDate.getTime();
        }
        return listingEndDateLong;

    }


    public void setListingEndDate(final Date listingEndDate) {
        this.listingEndDate = listingEndDate;
    }

    public void setListingEndDate(final Long listingEndDateLong) {
        this.listingEndDateLong = listingEndDateLong;
    }


    public int getCategory() {
        return category;
    }

    public void setCategory(final int category) {
        this.category = category;
    }

    public com.google.zxing.qrcode.encoder.QRCode getQRCode() {

        if (null == qrCode && -1 < itemNumber){
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            try {
                qrCode = Encoder.encode(prefix+itemNumber, ErrorCorrectionLevel.H, hintMap);
                Log.d("getQRCode","QRCode is set.");
            } catch (WriterException e) {
                e.printStackTrace();
                Log.e("getQRCode", "QRCode is null.");
                return null;
            }
        }

        Log.e("getQRCode", "returning QRCode.");

        return qrCode;
    }



    public void setImage(final Bitmap image) {
        this.imageBitmap = image;
    }

    public void setThumbnail(final Bitmap thumbnail) {
        this.thumbnailBitmap = thumbnail;
    }

    //used for when decoding db blob
    public void setImage(final byte[] rawImage) {
        imageByte = rawImage;
    }

    //used for when decoding db blob
    public void setThumbnail(final byte[] rawThumb) {
        thumbByte = rawThumb;
    }

    public Bitmap getImageBitmap() {

        if (null != imageBitmap) {
            return imageBitmap;
        } else {
            Log.d("Item","getImageBitmap -> imageBitmap is null, attempting to create one from bytes.");
            if (null != imageByte) {
                Log.d("Item","getImageBitmap -> imageByte is not null and "+imageByte.length+" bytes, creating bitmap.");
                BitmapFactory.Options options = new BitmapFactory.Options();
                imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, options);
                return imageBitmap;
            }
            else{
                Log.d("Item","getImageBitmap -> imageByte is null, can't create bitmap.");
            }
        }

        return null;

    }

    public Bitmap getThumbBitmap() {

        if (null != thumbnailBitmap) {
            return thumbnailBitmap;
        } else {
            if (null != thumbByte) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                thumbnailBitmap = BitmapFactory.decodeByteArray(thumbByte, 0, thumbByte.length, options);
                return thumbnailBitmap;
            }
        }

        return null;


    }

    //this is for database blob
    public byte[] getImageByte() {

        if (null != imageByte) {
            Log.d("ImageByte", imageByte.length + " bytes");
            return imageByte;
        } else {
            if (null != imageBitmap) {
                //encodeBitMap

                int compressionSize = JPEG_COMPRESSION;
                do {
                    Log.d("ImageByte", "Compressing using " + compressionSize + " quality.");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, compressionSize, stream);
                    imageByte = stream.toByteArray();
                    compressionSize = compressionSize - JPEG_COMPRESSION_INCREMENT;
                } while (imageByte.length > OPTIMUM_LENGTH);
                Log.d("ImageByte", imageByte.length + " bytes");
                return imageByte;


            }
        }

        return null;

    }

    //this is for database blob
    public byte[] getThumbByte() {


        if (null != thumbByte) {
            Log.d("ThumbByte", thumbByte.length + " bytes");
            return thumbByte;

        } else {
            if (null != thumbnailBitmap) {

                int compressionSize = JPEG_COMPRESSION;
                do {
                    Log.d("ThumbByte", "Compressing using " + compressionSize + " quality.");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, compressionSize, stream);
                    thumbByte = stream.toByteArray();
                    compressionSize = compressionSize - JPEG_COMPRESSION_INCREMENT;
                } while (thumbByte.length > OPTIMUM_LENGTH);
                Log.d("ThumbByte", thumbByte.length + " bytes");
                return thumbByte;

            }
        }

        return null;


    }


    public VectorDrawable getQRcode() {


/*


        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);*/


        return null;
    }


    @Override
    public String toString() {
        return "Item{" +
                "itemNumber=" + itemNumber +
                ", title='" + title + '\'' +
                ", listingStartDate='" + listingStartDate + '\'' +
                ", listingEndDate='" + listingEndDate + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", listingUserEmail='" + listingUserEmail + '\'' +
                ", price='" + price + '\'' +
                ", category='" + category + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", imageBitmap=" + imageBitmap +
                ", thumbnailBitmap=" + thumbnailBitmap +
                ", imageByte=" + Arrays.toString(imageByte) +
                ", thumbByte=" + Arrays.toString(thumbByte) +
                '}';
    }
}
