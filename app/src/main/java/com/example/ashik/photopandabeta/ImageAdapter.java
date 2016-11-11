package com.example.ashik.photopandabeta;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.ashik.photopandabeta.databinding.LayoutSimilarPhotosBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ashik on 8/11/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<String> mAlbumModelArrayList;
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<String> duplisfull) {
        mAlbumModelArrayList = duplisfull;
        mContext = context;

    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageAdapter.ViewHolder vh;
        LayoutSimilarPhotosBinding layoutSimilarPhotosBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_similar_photos,
                parent,
                false
        );
        vh = new ImageAdapter.ViewHolder(layoutSimilarPhotosBinding);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setImage(holder, position);
    }

    private void setImage(final ViewHolder holder, int position) {
        RequestCreator requestCreator;
        Picasso picasso;

        String imagePath = mAlbumModelArrayList.get(position);
        Context context = holder.itemView.getContext();


        if (!imagePath.equals("1")) {
            if (PandaUtility.isUrl(imagePath)) {
                picasso = Picasso.with(context);
                requestCreator = picasso.load(imagePath);
            } else {

                picasso = Picasso.with(context);
                requestCreator = picasso.load(
                        new File(imagePath)
                );
            }
            requestCreator.resizeDimen(R.dimen.height, R.dimen.width)
                    .centerCrop()
                    .into(holder.mLayoutSimilarPhotosBinding.imageViewSimilar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }


    @Override
    public int getItemCount() {
        return mAlbumModelArrayList != null ? mAlbumModelArrayList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LayoutSimilarPhotosBinding mLayoutSimilarPhotosBinding;

        public ViewHolder(LayoutSimilarPhotosBinding layoutSimilarPhotosBinding) {
            super(layoutSimilarPhotosBinding.getRoot());
            mLayoutSimilarPhotosBinding = layoutSimilarPhotosBinding;
        }
    }
}
