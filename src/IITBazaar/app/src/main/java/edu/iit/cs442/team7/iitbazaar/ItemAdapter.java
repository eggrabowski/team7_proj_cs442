/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package edu.iit.cs442.team7.iitbazaar;



import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements CursorAdapter {
    private static final String TAG = "ItemAdapter";


    private LayoutInflater inflater;
    private BazaarActivity activity;

    private boolean mDataValid;
    private Cursor mCursor;


    private MenuKeys key;



    public ItemAdapter(BazaarActivity activity,Cursor cursor, MenuKeys key) {
        this.activity = activity;
        this.key = key;
        boolean cursorPresent = cursor != null;
        mCursor = cursor;
        mDataValid = cursorPresent;
        Log.i("N local db items", getItemCount() + "");
        setHasStableIds(true);

    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //  private final TextView textView;

        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemDescription;
        private TextView itemEndTime;

        public ViewHolder(View v) {
            super(v);
            itemImage = (ImageView) v.findViewById(R.id.photo);
            itemName = (TextView) v.findViewById(R.id.name);
            itemPrice = (TextView) v.findViewById(R.id.price);
            itemDescription = (TextView) v.findViewById(R.id.itemDescription);
            itemEndTime = (TextView) v.findViewById(R.id.endtime);
        }

        public void setItemImage(Bitmap bm) {
            //imageView.setImageBitmap(BitmapFactory.decodeStream(is));
            itemImage.setImageBitmap(bm);
        }

        public void setItemName(String itemName) {
            this.itemName.setText(itemName);
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice.setText(itemPrice);
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription.setText(itemDescription);
        }

        public void setItemEndTime(long endTime) {
            Date date=new Date(endTime);
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
            String dateText = df.format(date);
            itemEndTime.setText(dateText);
        }
    }

    /*public static class ViewHolder extends RecyclerView.ViewHolder {
      //  private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
        //    v.setOnClickListener(new View.OnClickListener() {
         //       @Override
          //      public void onClick(View v) {
            //        Log.d(TAG, "Element " + getPosition() + " clicked.");
 //                   Toast.makeText(IITBazaar.getAppContext(), "Element " + getPosition() + " clicked.", Toast.LENGTH_SHORT).show();


//                }
  //          });
    //        textView = (TextView) v.findViewById(R.id.textView);
        }

      //  public TextView getTextView() {
        //    return textView;
        //}
    }*/
    // END_INCLUDE(recyclerViewSampleViewHolder)



    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_fragment, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder (ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");


        mCursor.moveToPosition(position);
        final Item item = new Item();


        Log.d("cursor", mCursor.getColumnName(0));
        Log.d("cursor",mCursor.getColumnName(1));
        Log.d("cursor",mCursor.getColumnName(2));
        Log.d("cursor",mCursor.getColumnName(3));
        Log.d("cursor",mCursor.getColumnName(4));
        Log.d("cursor",mCursor.getColumnName(5));

       // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_number
       // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_listing_end_date
       // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_title
       // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_description
       // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_picture_thumbnail


        try {

            item.setItemNumber(mCursor.getInt(0));
            item.setListingEndDate(mCursor.getLong(1));
            item.setTitle(mCursor.getString(2));
            item.setItemDescription(mCursor.getString(3));
            item.setThumbnail(mCursor.getBlob(4));
            item.setPrice(mCursor.getString(5));

            viewHolder.setItemEndTime(item.getListingEndDateLong());
            viewHolder.setItemName(item.getTitle());
            viewHolder.setItemDescription(item.getItemDescription());
            viewHolder.setItemImage(item.getThumbBitmap());
            viewHolder.setItemPrice("$"+item.getPrice());


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.itemSelected(item.getItemNumber(),key);

                }
            });

            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }

        }catch (CursorIndexOutOfBoundsException cioobe){
            Log.e("ItemAdapter",mCursor.toString());
            cioobe.printStackTrace();
        }



        // Get element from your dataset at this position and replace the contents of the view
        // with that element
     //   viewHolder.getTextView().setText(mDataSet[position]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)


    @Override
    public void notifyCursorChanged(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }
    /*Swathi Shenoy: To fix repetition of items in list*/
    @Override
    public long getItemId(int position){
        return mCursor.getPosition();
    }

}
