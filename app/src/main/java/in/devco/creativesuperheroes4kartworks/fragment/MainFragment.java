package in.devco.creativesuperheroes4kartworks.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.devco.creativesuperheroes4kartworks.activity.MainActivity;
import in.devco.creativesuperheroes4kartworks.SplashActivity;
import in.devco.creativesuperheroes4kartworks.config.admob;

import in.devco.creativesuperheroes4kartworks.R;

import in.devco.creativesuperheroes4kartworks.adapter.WallpaperAdapter;

/**
 * Created by kingdov on 17/01/2017.
 */

public class MainFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static WallpaperAdapter recyclerAdapter;
    public static android.support.v7.widget.AppCompatTextView noWallpaper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.wallpaperList);
        noWallpaper = v.findViewById(R.id.no_wallpaper);
        LinearLayout linearlayout = v.findViewById(R.id.unitads);
        admob.admobBannerCall(getActivity(), linearlayout);
        setRecyclerView(getActivity());
        return v;
    }

    public static void setRecyclerView(Context activity) {
        if(SplashActivity.allWallpapers.size()>0){
            noWallpaper.setVisibility(View.GONE);
            recyclerAdapter = new WallpaperAdapter(activity, SplashActivity.allWallpapers);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        } else
            noWallpaper.setVisibility(View.VISIBLE);
    }
}
