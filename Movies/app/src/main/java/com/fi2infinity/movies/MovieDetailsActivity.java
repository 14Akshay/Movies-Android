package com.fi2infinity.movies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fi2infinity.movies.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView heroImage;
    Toolbar mToolbar;
    private CollapsingToolbarLayout appBarLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_movie_detail);
        mToolbar = (Toolbar)findViewById(R.id.movie_details_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        setContentView(R.layout.detail_demo);
        Intent intent = getIntent();
        Toolbar mToolbar= (Toolbar) findViewById(R.id.movie_details_toolbar);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.movie_details_collapsing_toolbar_layout);

        Movie movieIntent = (Movie) intent.getSerializableExtra("Movie");
        if (movieIntent != null) {
            // Load Title text
            /*TextView titleText = (TextView) findViewById(R.id.detail_title);
            titleText.setText(movieIntent.getTitle());*/
            setSupportActionBar(mToolbar);
            mCollapsingToolbarLayout.setTitle(movieIntent.getTitle());
            mCollapsingToolbarLayout.setContentDescription(movieIntent.getTitle());
            // Load image in Image View
            heroImage = (ImageView)findViewById(R.id.movie_details_header_image);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w185" + movieIntent.getBackdrop_path())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(heroImage);
            heroImage.setContentDescription(movieIntent.getTitle());

           /* appBarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
            appBarLayout.setTitle(movieIntent.getTitle());

            ImageView detailImage = (ImageView) findViewById(R.id.detail_poster_image_view);
            Picasso.with(this).load("https://image.tmdb.org/t/p/w185" + movieIntent.getPoster_path())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(detailImage);*/

            // Load Release Date
            TextView releaseDateText = (TextView) findViewById(R.id.movie_details_release_date_text);
            releaseDateText.setText(movieIntent.getRelease_date());

            /*TextView title = (TextView)findViewById(R.id.title_movie);
            title.setText(movieIntent.getTitle());
*/

            int voteAverage = Math.round((movieIntent.getVote_average() / 10f) * 100f);


            TextView popularityText = (TextView) findViewById(R.id.movie_details_vote_average_text);
            popularityText.setText(String.valueOf(voteAverage)+"%");

            //Load Overview
             TextView overviewText = (TextView) findViewById(R.id.movie_details_overview_text);
            overviewText.setText(movieIntent.getOverview());

     /*       // Load Average
            TextView voteAverageText = (TextView) findViewById(R.id.movie_details_vote_average_text);
            voteAverageText.setText(movieIntent.getVote_average());*/

            // Load Vote Count
            TextView voteCountText = (TextView) findViewById(R.id.movie_details_vote_total_text);
            voteCountText.setText(String.valueOf(movieIntent.getVote_count())+" votes");
        } else {
            Toast.makeText(this, "ERROR No data was read",
                    Toast.LENGTH_LONG).show();
        }
    }
    }

