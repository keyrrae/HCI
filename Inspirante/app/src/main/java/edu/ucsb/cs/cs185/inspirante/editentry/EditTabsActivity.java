/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs185.inspirante.editentry;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import edu.ucsb.cs.cs185.inspirante.models.ItemCards;
import edu.ucsb.cs.cs185.inspirante.R;

public class EditTabsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ImageButton mLeftButton,mRightButton,mDeleteButton;
    TextView mTitle;
    Fragment mSelectCoverFragment;
    Fragment mEditInfoFragment;

    int cardIndex = 0;
    boolean isEdit = false;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tabs);

        Intent intent = getIntent();
        cardIndex = intent.getIntExtra("CARD_INDEX", 0);
        isEdit = intent.getBooleanExtra("EDIT", false);
        from = intent.getStringExtra("FROM");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mLeftButton = (ImageButton) findViewById(R.id.edit_toolbar_leftbutton);
        mDeleteButton = (ImageButton) findViewById(R.id.edit_toolbar_deletebutton);
        mRightButton = (ImageButton) findViewById(R.id.edit_toolbar_rightbutton);
        mTitle = (TextView) findViewById(R.id.edit_toolbar_title);

        setSelectCoverToolbar();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setSelectCoverToolbar();
                        setCoverImageInCoverTab();
                        break;
                    case 1:
                        setEnterInfoToolbar();
                        setCoverImageInEditTab();
                        break;
                }
                Log.d("page", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void setCoverImageInCoverTab(){
        SelectCoverFragment fragment = (SelectCoverFragment) mSelectCoverFragment;
        fragment.setUpdatedCoverImage();
    }

    protected void setCoverImageInEditTab(){
        EditInfoFragment fragment = (EditInfoFragment) mEditInfoFragment;
        fragment.setUpdatedCoverImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ItemCards itemCards = ItemCards.getInstance(getApplicationContext());
        if(!isEdit) {
            itemCards.cards.remove(cardIndex);
        }
    }

    public void setSelectCoverToolbar(){
        //mLeftButton.setText("Back");
        mLeftButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp));
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("left", "pressed");
                onBackPressed();
            }
        });

        mTitle.setText("Select a Cover");

        mDeleteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_forever_white_24dp));
        if(isEdit){
            mDeleteButton.setVisibility(View.VISIBLE);

            mDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d("Index", String.valueOf(cardIndex));

                    new AlertDialog.Builder(EditTabsActivity.this)
                            .setTitle("Delete Image")
                            .setMessage("Are you sure you want to delete this image?")
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    ItemCards.Card card = ItemCards.
                                            getInstance(getApplicationContext())
                                            .cards.get(cardIndex);
                                    card.deleteCoverImage();
                                    if(card.getImages().size() == 0){
                                        ItemCards.getInstance(getApplicationContext()).
                                                deleteIthCard(cardIndex);
                                        setResult(RESULT_OK, null);
                                        finish();
                                    }else {
                                        setCoverImageInCoverTab();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
            });

        } else {
            mDeleteButton.setVisibility(View.GONE);
        }

        //mRightButton.setText("Next");
        mRightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigate_next_white_24dp));

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1, true);
            }
        });
    }

    public void setEnterInfoToolbar(){
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0, true);
            }
        });

        mTitle.setText("Edit Details");

        mDeleteButton.setVisibility(View.GONE);

        //mRightButton.setText("Publish");
        mRightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_24dp));

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mViewPager.setCurrentItem(1, true);
                EditInfoFragment fragment = (EditInfoFragment) mEditInfoFragment;
                fragment.publishCard();
                finish();
            }
        });
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    mSelectCoverFragment = new SelectCoverFragment();
                    return mSelectCoverFragment;
                case 1:
                    mEditInfoFragment = new EditInfoFragment();
                    return mEditInfoFragment;
            }
            return null;
                //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
