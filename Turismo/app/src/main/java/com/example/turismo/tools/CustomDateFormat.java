package com.example.turismo.tools;

import java.text.SimpleDateFormat;

public class CustomDateFormat {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public CustomDateFormat() {
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
