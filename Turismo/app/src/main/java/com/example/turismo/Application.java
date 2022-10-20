package com.example.turismo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.turismo.databinding.ActivityApplicationBinding;

public class Application extends AppCompatActivity {

    private int navOptionSelected;
    private LugarFragment lugarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        ActivityApplicationBinding binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        replaceFragment(new LugarFragment("TURISTICO"));
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.turisticos:
                    if(item.getItemId() == navOptionSelected){
                        lugarFragment.goToTop();
                    }else{
                        this.lugarFragment = new LugarFragment("TURISTICO");
                        replaceFragment(this.lugarFragment);
                    }
                    break;
                case R.id.restaurantes:
                    if(item.getItemId() == navOptionSelected){
                        lugarFragment.goToTop();
                    }else{
                        this.lugarFragment = new LugarFragment("RESTAURANTE");
                        replaceFragment(this.lugarFragment);
                    }
                    break;
                case R.id.favoritos:
                    replaceFragment(new FavoritesFragment());
                    break;

            }
            navOptionSelected = item.getItemId();
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}