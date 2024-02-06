package com.kalyp.examples;

import com.kalyp.firefly.Firefly;

public class DataTypesExample {

    public static void main(String[] args) {
        Firefly firefly = new Firefly("http://localhost:5000");
        firefly.listDataTypes();
    }

}