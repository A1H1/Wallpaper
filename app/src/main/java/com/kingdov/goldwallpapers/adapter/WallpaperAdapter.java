package com.kingdov.goldwallpapers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kingdov.goldwallpapers.FavoritesFragment;
import com.kingdov.goldwallpapers.GlideApp;
import com.kingdov.goldwallpapers.activity.MainActivity;
import com.kingdov.goldwallpapers.WallpaperDetail;
import com.kingdov.goldwallpapers.config.admob;
import com.kingdov.goldwallpapers.func.DataUrl;

import java.util.List;

import comm.kingdov.goldwallpapers.R;


public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private List<DataUrl> data;

    public WallpaperAdapter(Context context, List<DataUrl> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view_wallpaper, parent, false);
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
                WallpaperDetail.sData = data.get(position);
                Intent intent = new Intent(context, WallpaperDetail.class);
                context.startActivity(intent);

                admob.showInterstitial(true);
            }
        });

        try {
            if(MainActivity.listFavorites.contains(data.get(position).url)) {
                myHolder.favorite =true;
                myHolder.imageViewFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_true));
            } else {
                myHolder.favorite =false;
                myHolder.imageViewFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_false));
            }
        }catch (Exception ignored){}

        myHolder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String shareText = context.getString(R.string.share_text) + " "
                        + context.getString(R.string.app_name) + " app by "
                        + context.getString(R.string.dev) + ": \n\n"
                        + data.get(position).title + "\n"
                        + data.get(position).url;

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        context.startActivity(Intent.createChooser(sendIntent, "Share wallpaper via:"));


            }
        });

        myHolder.imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.text_dialog, null);
                final TextView mssg = alertLayout.findViewById(R.id.messageDelete);
                if(myHolder.favorite) mssg.setText("Do you want remove it from favorites wallpapers");
                else  mssg.setText("Do you want add if to favorites wallpapers");
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                if(myHolder.favorite) alert.setTitle("Remove from favorites wallpapers");
                else alert.setTitle("Add to favorite wallpapers");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(myHolder.favorite){
                            MainActivity.listFavorites.remove(data.get(position).url);
                            MainActivity.favoriteData.remove(data.get(position));
                            FavoritesFragment.setRecyclerView(context);
                            myHolder.imageViewFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_false));
                            myHolder.favorite =false;
                        } else {
                                MainActivity.listFavorites.add(data.get(position).url);
                                MainActivity.favoriteData.add(data.get(position));
                                try {FavoritesFragment.setRecyclerView(context);}catch (Exception ex){}
                                myHolder.imageViewFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_true));
                                myHolder.favorite = true;
                                //try {}catch (Exception ex){}
                        }

//                        MainActivity.getFavoritesContacts(mContext);
//                        Tab3.adapterArticles(mContext);
                        String json = MainActivity.gson.toJson(MainActivity.listFavorites);
                        MainActivity.editor.putString("favorites", json);
                        MainActivity.editor.commit();
                        Toast.makeText(context, "Operation done Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

                admob.showInterstitial(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageViewFavorite, imageShare;
        TextView textView;
        boolean favorite = false;

        public MyHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.wallpaperThumb);
            imageViewFavorite = itemView.findViewById(R.id.wallpaperFavorite);
            imageShare = itemView.findViewById(R.id.wallpaperShare);
            textView = itemView.findViewById(R.id.wallpaperTitle);
        }
    }
}
