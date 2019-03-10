package com.kingdov.goldwallpapers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kingdov.goldwallpapers.SplashActivity;
import com.kingdov.goldwallpapers.adapter.CategoryAdapter;
import com.kingdov.goldwallpapers.config.admob;

import comm.kingdov.goldwallpapers.R;

/**
 * Created by A1H1 on 28/01/2019.
 */

public class CategoryFragment extends Fragment {

    public static RecyclerView recyclerView;
    public static CategoryAdapter recyclerAdapter;
    public static android.support.v7.widget.AppCompatTextView noCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = v.findViewById(R.id.fragment_category_rv_list);
        noCategory = v.findViewById(R.id.fragment_category_tv_no_category);
        LinearLayout linearlayout = v.findViewById(R.id.fragment_category_ll_ad);
        admob.admobBannerCall(getActivity(), linearlayout);
        setRecyclerView(getActivity());
        return v;
    }

    public static void setRecyclerView(Context activity) {
        if(SplashActivity.data.size()>0)
            noCategory.setVisibility(View.GONE);
        else
            noCategory.setVisibility(View.VISIBLE);

        recyclerAdapter = new CategoryAdapter(activity, SplashActivity.data);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
    }
}
