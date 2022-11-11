package com.example.turismo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.turismo.LugarFragment;

public class CercaViewPagerAdapter extends FragmentStateAdapter {
    public CercaViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LugarFragment("TURISTICO", 2);
            case 1:
                return new LugarFragment("RESTAURANTE", 2);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
