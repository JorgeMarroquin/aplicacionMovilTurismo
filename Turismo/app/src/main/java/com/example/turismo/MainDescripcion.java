package com.example.turismo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.turismo.databinding.ActivityMainDescripcionBinding;

public class MainDescripcion extends AppCompatActivity {

    private int lugarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_descripcion);
        ActivityMainDescripcionBinding binding = ActivityMainDescripcionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        lugarId = getIntent().getIntExtra("lugarId", -1);
        replaceFragment(new DescriptionFragment(lugarId));

        binding.bottomNavigationDescription.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.lugar_descripcion_button:
                    replaceFragment(new DescriptionFragment(lugarId));
                    break;
                case R.id.lugar_comentarios_button:
                    replaceFragment(new CommentFragment(lugarId));
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_description, fragment);
        fragmentTransaction.commit();
    }
}