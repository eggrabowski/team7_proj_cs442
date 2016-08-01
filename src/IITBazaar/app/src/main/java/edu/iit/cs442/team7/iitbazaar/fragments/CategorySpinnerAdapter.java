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
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import edu.iit.cs442.team7.iitbazaar.BazaarActivity;
import edu.iit.cs442.team7.iitbazaar.Category;
import edu.iit.cs442.team7.iitbazaar.CursorAdapter;
import edu.iit.cs442.team7.iitbazaar.MenuKeys;
import edu.iit.cs442.team7.iitbazaar.R;


/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */

public class CategorySpinnerAdapter implements SpinnerAdapter {
    private static final String TAG = "CategorySpinnerAdapter";


    private LayoutInflater inflater;
    private BazaarActivity activity;

    private boolean mDataValid;
    private Cursor mCursor;






    public CategorySpinnerAdapter(BazaarActivity activity, Cursor cursor) {
        this.activity = activity;

        boolean cursorPresent = cursor != null;
        mCursor = cursor;
        mDataValid = cursorPresent;

    }





    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {

       mCursor.moveToPosition(position);
        Category category = new Category();
        category.setCategoryNumber(mCursor.getInt(0));
        category.setParentCategoryNumber(mCursor.getInt(1));
        category.setCategoryName(mCursor.getString(2));
        return category;



    }

    @Override
    public long getItemId(int position){
        return mCursor.getPosition();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = (TextView) View.inflate(activity, android.R.layout.simple_spinner_item, null);
        mCursor.moveToPosition(position);
        String text = mCursor.getString(2);
        textView.setText(text);
        return textView;

    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {

        return !mCursor.moveToFirst();


    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(activity, android.R.layout.simple_spinner_dropdown_item, null);
        mCursor.moveToPosition(position);
        String text = mCursor.getString(2);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
