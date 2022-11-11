package com.example.turismo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.turismo.adapter.CercaViewPagerAdapter;
import com.example.turismo.adapter.FavoritesViewPagerAdapter;
import com.example.turismo.databinding.FragmentCercaBinding;
import com.example.turismo.databinding.FragmentFavoritesBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class CercaFragment extends Fragment {

    private FragmentCercaBinding binding;

    public CercaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCercaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        CercaViewPagerAdapter cercaViewPagerAdapter = new CercaViewPagerAdapter(this);
        binding.cercaViewpager.setAdapter(cercaViewPagerAdapter);
        binding.cercaViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabCercaLayout.getTabAt(position)).select();
            }
        });

        binding.tabCercaLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.cercaViewpager.setCurrentItem(tab.getPosition());
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