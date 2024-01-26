package com.example.usersapp;

import com.example.usersapp.utils.DataGenerator;

public class mainFileGenerator {
    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.prepareData();
        System.out.println("Plik utworzony pomyślnie");
    }
}
