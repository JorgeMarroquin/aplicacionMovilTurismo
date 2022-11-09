package com.example.turismo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.turismo.LugarFragment;

public class FavoritesViewPagerAdapter extends FragmentStateAdapter {


    public FavoritesViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LugarFragment("TURISTICO", 1, true);
            case 1:
                return new LugarFragment("RESTAURANTE", 1, true);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
