package com.fi2infinity.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fi2infinity.movies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 01-May-17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Movie> items;
    private Context context;
    ImageAdapterOnClickHandler mClickHandler;

    public interface ImageAdapterOnClickHandler {
        void onClick(int movieId, int adapterPosition);
    }

    public ImageAdapter(ImageAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public ImageAdapter(Context context, List<Movie> android,ImageAdapterOnClickHandler clickHandler) {
        this.items = android;
        this.context = context;
        mClickHandler = clickHandler;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }


    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ImageViewHolder holder, int position) {
        Movie movieDb = getItem(position);
        View convertView = holder.itemView;
        ImageView imageViewcustom = (ImageView) convertView.findViewById(R.id.img_row);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + movieDb.getPoster_path())
                .placeholder(R.drawable.poster_place_holder)
                .into(imageViewcustom);
        int clickedPosition = holder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_android;

        public ImageViewHolder(View view) {
            super(view);
            img_android = (ImageView) view.findViewById(R.id.img_row);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            //String weatherForDay = mWeatherData[adapterPosition];
            int movieId = items.get(adapterPosition).getId();
            mClickHandler.onClick(movieId, adapterPosition);
        }
    }
}
