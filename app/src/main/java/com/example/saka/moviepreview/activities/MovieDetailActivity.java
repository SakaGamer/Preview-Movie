package com.example.saka.moviepreview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.saka.moviepreview.R;
import com.example.saka.moviepreview.VolleyRequestQueue;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkImageView imgBackDrop;
    NetworkImageView imgPoster;
    TextView txTitle;
    TextView txPlayTrailer;
    TextView txOverview;
    TextView txVoteScore;
    TextView txOriginalLanguage;
    TextView txRuntime;
    TextView txBudget;
    TextView txRevenue;
    TextView txHomepage;
    ProgressBar progressBar;

    String data;
    JSONObject movie;
    String trailerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imgBackDrop = findViewById(R.id.mv_detail_img_backdrop);
        imgPoster = findViewById(R.id.mv_detail_img_poster);
        txTitle = findViewById(R.id.mv_detail_txt_title);
        txPlayTrailer = findViewById(R.id.mv_detail_txt_play_trailer);
        txOverview = findViewById(R.id.mv_detail_txt_overview);
        txVoteScore = findViewById(R.id.mv_detail_txt_vote);
        txOriginalLanguage = findViewById(R.id.mv_detail_txt_original_language);
        txRuntime = findViewById(R.id.mv_detail_txt_runtime);
        txBudget = findViewById(R.id.mv_detail_txt_budget);
        txRevenue = findViewById(R.id.mv_detail_txt_revenue);
        txHomepage = findViewById(R.id.mv_detail_txt_homepage);
        progressBar = findViewById(R.id.mv_detail_progress_bar);

        txPlayTrailer.setOnClickListener(this);

        data = getIntent().getStringExtra("movie");
        Gson gson = new Gson();
        movie = gson.fromJson(data, JSONObject.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int movieId = movie.getInt("id");
            loadMovieTrailer(movieId);
            String baseUrl = "http://image.tmdb.org/t/p/w300";
            String backdropPathPath = movie.getString("backdrop_path");
            String posterPath = movie.getString("poster_path");
            final String fullImgBackDropUrl = baseUrl + backdropPathPath;
            final String fullImgPosterUrl = baseUrl + posterPath;

            ImageLoader imageLoader = VolleyRequestQueue.getInstance(getApplicationContext())
                    .getImageLoader();
            imageLoader.get(fullImgBackDropUrl, ImageLoader.getImageListener(imgBackDrop,
                    R.drawable.ic_launcher_background,
                    android.R.drawable.ic_dialog_alert));
            imgBackDrop.setImageUrl(fullImgBackDropUrl, imageLoader);
            imageLoader.get(fullImgPosterUrl, ImageLoader.getImageListener(imgPoster,
                    R.drawable.ic_launcher_background,
                    android.R.drawable.ic_dialog_alert));
            imgPoster.setImageUrl(fullImgPosterUrl, imageLoader);

            txTitle.setText(movie.getString("title"));
            txOverview.setText(movie.getString("overview"));

            String votePercentage = movie.getInt("vote_average") * 10 + "";
            txVoteScore.setText(votePercentage);

            String origLang = movie.getString("original_language");
            if (origLang.equals("en")) origLang = "English";
            txOriginalLanguage.setText(origLang);

//            int runTime = movie.getInt("runtime");
//            int hour = runTime / 60;
//            int minute = (runTime % 60) * 60;
//            String runtime = hour + "h " + minute + "m";
//            txRuntime.setText(runtime);

        } catch (JSONException e) {
            Log.d("app", e.getMessage());
        }
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.mv_detail_txt_play_trailer) {
            Log.d("app", "trailer key: " + trailerKey);
            startActivity(new Intent(getApplicationContext(), PlayYoutubeTrailerActivity.class)
                    .putExtra("movie_trailer_key", trailerKey));
        }
    }


    private void loadMovieTrailer(int movieId) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String API_KEY = getResources().getString(R.string.MOVIE_API_KEY);
        String url = "https://api.themoviedb.org/3/movie/" + movieId
                + "/videos?api_key=" + API_KEY + "&language=en-US";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("app", "movies response");
                        boolean isError = false;
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (object.getString("type").equals("Trailer")) {
                                    trailerKey = object.getString("key");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isError = true;
                        }
                        if (isError) {
                            Toast.makeText(getApplicationContext(), "Error loading movies", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error while loading movies", Toast.LENGTH_LONG).show();
                Log.d("app", "Error load movies: " + error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
