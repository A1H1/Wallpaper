package in.devco.creativesuperheroes4kartworks.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import in.devco.creativesuperheroes4kartworks.GlideApp;
import in.devco.creativesuperheroes4kartworks.activity.WallpapersList;
import in.devco.creativesuperheroes4kartworks.config.admob;
import in.devco.creativesuperheroes4kartworks.func.DataUrl;

import java.util.List;

import in.devco.creativesuperheroes4kartworks.R;

/**
 * Created by A1H1 on 10/03/2019.
 */

public class WallpaperListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<DataUrl> data;

    public WallpaperListAdapter(Context context, List<DataUrl> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view_category, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        DataUrl current = data.get(position);
        myHolder.textView.setText(current.title);

        GlideApp.with(context).load(current.url)
                .error(R.drawable.ic_placeholder_wallpaper)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_wallpaper)
                .into(myHolder.imageView);
        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpapersList.category = data.get(position).title;
                Intent intent = new Intent(context, WallpapersList.class);
                context.startActivity(intent);

                admob.showInterstitial(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MyHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_thumb);
            textView = itemView.findViewById(R.id.category_title);
        }
    }
}
