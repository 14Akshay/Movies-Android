package com.fi2infinity.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fi2infinity.movies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView heroImage;
    private CollapsingToolbarLayout appBarLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();


        Movie movieIntent = (Movie) intent.getSerializableExtra("Movie");
        if (movieIntent != null) {
            // Load Title text
            /*TextView titleText = (TextView) findViewById(R.id.detail_title);
            titleText.setText(movieIntent.getTitle());*/

            // Load image in Image View
            heroImage = (ImageView)findViewById(R.id.backdrop);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w185" + movieIntent.getBackdrop_path())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(heroImage);
            heroImage.setContentDescription(movieIntent.getTitle());

            appBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
            appBarLayout.setTitle(movieIntent.getTitle());

            ImageView detailImage = (ImageView) findViewById(R.id.detail_poster_image_view);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w185" + movieIntent.getPoster_path())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(detailImage);

            // Load Release Date
            TextView releaseDateText = (TextView) findViewById(R.id.release_date_text_view);
            releaseDateText.setText(movieIntent.getRelease_date());

            /*// Load Popularity
            TextView popularityText = (TextView) findViewById(R.id.detail_popularity);
            popularityText.setText("Popularity: " + movieIntent.getPopularity());

            //Load Overview
            TextView overviewText = (TextView) findViewById(R.id.detail_overview);
            overviewText.setText(movieIntent.getOverview());

            // Load Average
            TextView voteAverageText = (TextView) findViewById(R.id.detail_vote_average);
            voteAverageText.setText("Voter Average: " + movieIntent.getVote_average());*/

            // Load Vote Count
            TextView voteCountText = (TextView) findViewById(R.id.user_rating_text_view);
            voteCountText.setText("Voter Count: " + movieIntent.getVote_count());
        } else {
            Toast.makeText(this, "ERROR No data was read",
                    Toast.LENGTH_LONG).show();
        }
    }
    }

