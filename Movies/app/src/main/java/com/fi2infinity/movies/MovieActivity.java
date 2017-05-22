package com.fi2infinity.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fi2infinity.movies.data.Movie;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Akshay on 01-May-17.
 */

public class MovieActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ImageAdapter.ImageAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
   ImageAdapter mImageAdapter;
    private Spinner sortSpinner;
    private int lastSelectedSpinnerPosition;
    private ImageView imageView;
    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopVotedList;
    final static String POP_LIST = "popList";
    final static String TOP_VOTE_LIST = "topVoteList";
    final static String TAG = "Movie Results";
    private final static String KEY_SPINNER_POSITION = "spinner_position";

    RecyclerView recyclerView;

    @Override
    public void onClick(int movieId, int adapterPosition) {
        Movie movie =(Movie)mImageAdapter.getItem(adapterPosition);
        Context context = MovieActivity.this;
        Class destinationActivity = MovieDetailsActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("item_id", movieId);
        startChildActivityIntent.putExtra("Movie", movie);
        startActivity(startChildActivityIntent);
      /*  String transName = getString(R.string.poster_transition);
        ImageAdapter.ImageViewHolder currentPositionViewHolder = (ImageAdapter.ImageViewHolder) recyclerView.findViewHolderForAdapterPosition(adapterPosition);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieActivity.this, currentPositionViewHolder.img_android, transName);
        ActivityCompat.startActivity(MovieActivity.this, startChildActivityIntent, optionsCompat.toBundle());
*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
       // mImageAdapter = new ImageAdapter(this);
        imageView = (ImageView) findViewById(R.id.img_row);
        if (savedInstanceState != null) {
            lastSelectedSpinnerPosition = savedInstanceState.getInt(KEY_SPINNER_POSITION, 0);
        }
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        setupSortSpinner(menu);

        return true;
    }

    private void setupSortSpinner(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order, menu);

        MenuItem item = menu.findItem(R.id.sort_order_spinner);
        sortSpinner = (Spinner) MenuItemCompat.getActionView(item);

        if (getSupportActionBar() != null) {
            Context themedContext = getSupportActionBar().getThemedContext();

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(themedContext,
                    R.array.sort_order_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sortSpinner.setAdapter(adapter);

            sortSpinner.setSelection(lastSelectedSpinnerPosition);

            sortSpinner.setOnItemSelectedListener(this);
        }
    }

    private void initViews() {
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        prepareData();
    }

    private void prepareData() {
        new FetchImageTask().execute();
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void URLResult(String webAddress, ArrayList<Movie> _List) {
        try {
            URL url = new URL(webAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            jsonParser(results, _List);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jsonParser(String s, ArrayList<Movie> movies) {
        try {
            JSONObject mainObject = new JSONObject(s);
            JSONArray resultsArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject indexObject = resultsArray.getJSONObject(i);
                Movie indexMovie = new Movie();
                indexMovie.setBackdrop_path(indexObject.getString("backdrop_path"));
                indexMovie.setId(indexObject.getInt("id"));
                indexMovie.setOriginal_title(indexObject.getString("original_title"));
                indexMovie.setOverview(indexObject.getString("overview"));
                indexMovie.setRelease_date(indexObject.getString("release_date"));
                indexMovie.setPoster_path(indexObject.getString("poster_path"));
                indexMovie.setPopularity(indexObject.getDouble("popularity"));
                indexMovie.setTitle(indexObject.getString("title"));
                indexMovie.setVote_average(indexObject.getInt("vote_average"));
                indexMovie.setVote_count(indexObject.getInt("vote_count"));
                movies.add(indexMovie); // Add each item to the list
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Log.e(TAG, "JSON Error", e);
        }

    }

    private void showImageDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            loadMovieAdapter(mPopularList);
        else
            loadMovieAdapter(mTopVotedList);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class FetchImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override

        protected Void doInBackground(Void... params) {
            String WebAddress;
            String WebAddressVote;
            WebAddress = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
                    + "3f4578bb196cea70b23fd495e6358083";
            WebAddressVote = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="
                    + "3f4578bb196cea70b23fd495e6358083";
            mPopularList = new ArrayList<>();
            mTopVotedList = new ArrayList<>();
            URLResult(WebAddress, mPopularList);
            URLResult(WebAddressVote, mTopVotedList);
            mPopularList.toString();
            return null;

        }

        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            loadMovieAdapter(mPopularList);
        }

    }

    private void loadMovieAdapter(ArrayList<Movie> _list) {
        mImageAdapter = new ImageAdapter(MovieActivity.this, _list,this);
        recyclerView.setAdapter(mImageAdapter);
    }
}
