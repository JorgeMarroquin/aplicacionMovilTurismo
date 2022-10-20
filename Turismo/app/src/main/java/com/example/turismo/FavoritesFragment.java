package com.example.turismo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.turismo.adapter.FavoritesViewPagerAdapter;
import com.example.turismo.databinding.FragmentFavoritesBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;

    public FavoritesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FavoritesViewPagerAdapter favoritesViewPagerAdapter = new FavoritesViewPagerAdapter(this);
        binding.favoritesViewpager.setAdapter(favoritesViewPagerAdapter);
        binding.favoritesViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabFavoritesLayout.getTabAt(position)).select();
            }
        });
        binding.tabFavoritesLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.favoritesViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}