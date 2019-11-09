package adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;

import org.parceler.Parcels;

import java.util.List;

import model.Movie;

public class ComplexMovieAdapter extends RecyclerView.Adapter<ComplexMovieAdapter.ViewHolder> {
    private static final int POPULAR = 1;
    private static final int ORDINARY = 0;
    Context context;
    List<Movie> movies;
    public final static String MOVIE_KEY = "movie";
    public ComplexMovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        RelativeLayout container;
        public ViewHolder(@NonNull View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.tvOverview = itemView.findViewById(R.id.tvOverview);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.ivPoster = itemView.findViewById(R.id.ivPoster);
            this.container = itemView.findViewById(R.id.container);
        }

        // new since Glide v4
        //@GlideModule
        //public final class MyAppGlideModule extends AppGlideModule {}


        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdrop_path();
            }
            else {
                imageUrl = movie.getPoster_path();
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));

            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(ivPoster);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // first parameter is the context, second is the class of the activity to launch
                    Intent intent = new Intent(context, DetailActivity.class);
                    // put "extras" into the bundle for access in the second activity
                    intent.putExtra(MOVIE_KEY, Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }

    public class ViewHolderPopular extends ViewHolder {
        ImageView ivPoster;

        public ViewHolderPopular(@NonNull View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.ivPoster = itemView.findViewById(R.id.ivPoster);
        }

        public void bind_popular(final Movie movie) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(20));
            Glide.with(context)
                    .load(movie.getBackdrop_path())
                    .apply(requestOptions)
                    .into(ivPoster);

            ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // first parameter is the context, second is the class of the activity to launch
                    Intent intent = new Intent(context, DetailActivity.class);
                    // put "extras" into the bundle for access in the second activity
                    intent.putExtra(MOVIE_KEY, Parcels.wrap(movie));
                    context.startActivity(intent);
                }
            });
        }
    }



    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).getVote_average() <= 7.5) {
            return ORDINARY;
        } else {
            return POPULAR;
        }
    }


    // inflating a layout in XML file and return to holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView;
        // a popular movie and we are on the portrait mode
        if (viewType == POPULAR &&
                context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie_popular,parent,false);
            return new ViewHolderPopular(movieView);
        }
        // a ordinary movie, with star <= 5
        else {
            movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false);
            return new ViewHolder(movieView);
        }
    }

    // populating data into items through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the position of movie
        Movie movie = movies.get(position);
        // ordinary movie or we are on the landscape mode
        // the display of popular movie will be the same
        // as the ordinary one in the landscape mode
        if (holder.getItemViewType() == ORDINARY ||
                context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // bind the movie data into the viewholder
            holder.bind(movie);
        }
        else {
            ViewHolderPopular holderPopular = (ViewHolderPopular) holder;
            holderPopular.bind_popular(movie);
        }
    }

    // return the total count of the item list
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
