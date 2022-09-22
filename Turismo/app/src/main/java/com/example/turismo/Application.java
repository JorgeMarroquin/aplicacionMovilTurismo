package com.example.turismo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.turismo.databinding.ActivityApplicationBinding;
import com.example.turismo.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Application extends AppCompatActivity {

    private ActivityApplicationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        replaceFragment(new LugarFragment("TURISTICO"));
        binding.bottomNavigationView.setOnItemSelectedListener( item -> {
            switch (item.getItemId()){
                case R.id.turisticos:
                    replaceFragment(new LugarFragment("TURISTICO"));
                    break;
                case R.id.restaurantes:
                    replaceFragment(new LugarFragment("RESTAURANTE"));
                    break;
            }
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