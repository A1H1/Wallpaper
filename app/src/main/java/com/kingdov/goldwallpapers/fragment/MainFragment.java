package com.kingdov.goldwallpapers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kingdov.goldwallpapers.activity.MainActivity;
import com.kingdov.goldwallpapers.SplashActivity;
import com.kingdov.goldwallpapers.config.admob;

import comm.kingdov.goldwallpapers.R;

import com.kingdov.goldwallpapers.adapter.WallpaperAdapter;

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
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        setRecyclerView(getActivity());
        return v;
    }

    public static void setRecyclerView(Context activity) {
        if(SplashActivity.data.size()>0) noWallpaper.setVisibility(View.GONE);
        else noWallpaper.setVisibility(View.VISIBLE);
        recyclerAdapter = new WallpaperAdapter(activity, SplashActivity.data);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
    }

}
