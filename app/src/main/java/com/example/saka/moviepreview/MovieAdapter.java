package com.example.saka.moviepreview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<JSONObject> movies = new ArrayList<>();
    private static MovieClickListener movieClickListener;


    public MovieAdapter(Context context, List<JSONObject> movies) {
        this.movies = movies;
        this.context = context;
    }


    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_movie, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        final JSONObject movie = movies.get(position);
        try {
            String baseUrl = "http://image.tmdb.org/t/p/w300";
            String postPath = movie.getString("backdrop_path");
            String fullImgUrl = baseUrl + postPath;
            ImageLoader imageLoader = VolleyRequestQueue.getInstance(context).getImageLoader();
            imageLoader.get(fullImgUrl, ImageLoader.getImageListener(holder.networkImg, R.drawable.no_image,
                    R.drawable.no_image));
            String title = movie.getString("title");
            String overview = movie.getString("overview");
            if (title.length() > 25) title = title.substring(0, 25) + "...";
            if (overview.length() > 30) overview = overview.substring(0, 30) + "...";
            holder.networkImg.setImageUrl(fullImgUrl, imageLoader);
            holder.txTitle.setText(title);
            holder.txOverview.setText(overview);
            holder.txVote.setText(movie.getString("vote_average"));
            holder.txYear.setText(movie.getString("release_date").substring(0, 4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }


    public JSONObject getItem(int position) {
        return movies.get(position);
    }


    public void setMovies(List<JSONObject> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }


    public void setMovieClickListener(MovieClickListener clickListener2) {
        movieClickListener = clickListener2;
    }


    public interface MovieClickListener {
        void onClassClicked(View view, int position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NetworkImageView networkImg;
        private TextView txTitle, txOverview, txVote, txYear;

        private ViewHolder(View itemView) {
            super(itemView);
            networkImg = itemView.findViewById(R.id.card_movie_img);
            txTitle = itemView.findViewById(R.id.card_movie_txt_title);
            txOverview = itemView.findViewById(R.id.card_movie_txt_description);
            txVote = itemView.findViewById(R.id.card_movie_txt_vote);
            txYear = itemView.findViewById(R.id.card_movie_txt_year);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (movieClickListener != null) {
                movieClickListener.onClassClicked(view, getAdapterPosition());
            }
        }
    }


}
