/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs185.foliostation.searchbyranking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.ucsb.cs.cs185.foliostation.R;
import edu.ucsb.cs.cs185.foliostation.editentry.EditTabsActivity;
import edu.ucsb.cs.cs185.foliostation.models.ItemCards;
import edu.ucsb.cs.cs185.foliostation.mycollections.CardViewHolder;
import edu.ucsb.cs.cs185.foliostation.share.ShareActivity;

/**
 * Created by xuanwang on 3/5/17.
 */

public class TagsAdapter extends RecyclerView.Adapter<CardViewHolder>
        implements View.OnClickListener {

    List<ItemCards.TagAndImages> mTagAndImages;

    Context mContext = null;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public TagsAdapter(Context context, List<ItemCards.TagAndImages> tagsAndImages){
        mContext = context;
        mTagAndImages = tagsAndImages;
    }

    protected String getTag(int position){
        if (mTagAndImages != null){
            return mTagAndImages.get(position).tag;
        }
        return "";
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tag, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);

        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {

        if(mContext == null){
            Log.e("mContext", "null");
        }

        String tag = mTagAndImages.get(position).tag;
        holder.title.setText(tag);
        holder.title.setTag(position);
        holder.title.setOnClickListener(this);

    }

    public void startEditActivity(View view, int position){
        Intent intent = new Intent(view.getContext(), EditTabsActivity.class);
        intent.putExtra("CARD_INDEX", position);
        intent.putExtra("EDIT", true);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mTagAndImages.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //define Item click interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            // get tag
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
