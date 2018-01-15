package com.example.saka.moviepreview.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.saka.moviepreview.MovieAdapter;
import com.example.saka.moviepreview.R;
import com.example.saka.moviepreview.activities.MovieDetailActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MovieAdapter.MovieClickListener, AdapterView.OnItemSelectedListener {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView txFilter;
    Spinner spinner;

    List<JSONObject> movies = new ArrayList<>();
    MovieAdapter movieAdapter;
    String movieFilter = "popular";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_movie, container, false);
        swipeRefreshLayout = root.findViewById(R.id.movie_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        RecyclerView recyclerView = root.findViewById(R.id.movie_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        txFilter = root.findViewById(R.id.movie_txt_filter);
        spinner = root.findViewById(R.id.movie_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.movies_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        loadMovieApi(movieFilter);
        movieAdapter = new MovieAdapter(getContext(), movies);
        movieAdapter.setMovieClickListener(this);
        recyclerView.setAdapter(movieAdapter);

        return root;
    }


    // get movies api from "the movie db"
    private void loadMovieApi(String filter) {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String API_KEY = getResources().getString(R.string.MOVIE_API_KEY);
        String url = "https://api.themoviedb.org/3" + "/movie/" + filter + "?api_key="
                + API_KEY + "&language=en-US&page=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("app", "movies response");
                        boolean isError = false;
                        movies.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                movies.add(object);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isError = true;
                        }
                        if (isError) {
                            Toast.makeText(getContext(), "Error loading movies", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        movieAdapter.setMovies(movies);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error while loading movies", Toast.LENGTH_LONG).show();
                Log.d("app", "Error load movies: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onRefresh() {
        loadMovieApi(movieFilter);
        Log.d("app", "onRefresh called");
    }


    @Override
    public void onClassClicked(View view, int position) {
        final JSONObject movie = movieAdapter.getItem(position);
        Gson gson = new Gson();
        String data = gson.toJson(movie);
        startActivity(new Intent(getContext(), MovieDetailActivity.class)
                .putExtra("movie", data));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        movieFilter = adapterView.getItemAtPosition(i).toString();
        loadMovieApi(movieFilter);
        Log.d("app", movieFilter);
        String filter = adapterView.getItemAtPosition(i).toString().toUpperCase() + " Movies";
        txFilter.setText(filter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
