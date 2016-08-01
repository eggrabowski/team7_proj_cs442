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

package edu.iit.cs442.team7.iitbazaar.fragments;



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
import java.util.List;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.Category;
import edu.iit.cs442.team7.iitbazaar.CursorAdapter;
import edu.iit.cs442.team7.iitbazaar.Item;
import edu.iit.cs442.team7.iitbazaar.MenuKeys;
import edu.iit.cs442.team7.iitbazaar.R;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements CursorAdapter {
    private static final String TAG = "CategoryAdapter";


    private LayoutInflater inflater;
    private BazaarActivity activity;

    private boolean mDataValid;
    private Cursor mCursor;


    private MenuKeys key;



    public CategoryAdapter(BazaarActivity activity,Cursor cursor, MenuKeys key) {
        this.activity = activity;
        this.key = key;
        boolean cursorPresent = cursor != null;
        mCursor = cursor;
        mDataValid = cursorPresent;
        Log.i("N local db categories", getItemCount() + "");
        setHasStableIds(true);

    }

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //  private final TextView textView;


        private TextView categoryName;


        public ViewHolder(View v) {
            super(v);
            categoryName = (TextView) v.findViewById(R.id.category_name);

        }


        public void setCategoryName(String categoryName) {
            this.categoryName.setText(categoryName);
        }


    }



    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_fragment, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder (ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");


        mCursor.moveToPosition(position);
        final Category category = new Category();


        Log.d("cursor", mCursor.getColumnName(0));
        Log.d("cursor",mCursor.getColumnName(1));
        Log.d("cursor",mCursor.getColumnName(2));


        // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_number
        // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_listing_end_date
        // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_title
        // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_description
        // 11-23 22:05:34.241 29185-29185/edu.iit.cs442.team7.iitbazaar D/cursor: item_picture_thumbnail


        try {

            category.setCategoryNumber(mCursor.getInt(0));
            category.setParentCategoryNumber(mCursor.getInt(1));
            category.setCategoryName(mCursor.getString(2));




            viewHolder.setCategoryName(category.getCategoryName());



            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    activity.getCategoryOrItemList(category.getCategoryNumber());

                  //  activity.itemSelected(item.getItemNumber(),key);


                }
            });

            if (!mDataValid) {
                throw new IllegalStateException("this should only be called when the cursor is valid");
            }
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }

        }catch (CursorIndexOutOfBoundsException cioobe){
            Log.e("CategoryAdapter",mCursor.toString());
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
    /*Swathi Shenoy: To fix repetition of categories in list*/
    @Override
    public long getItemId(int position){
        return mCursor.getPosition();
    }
}
