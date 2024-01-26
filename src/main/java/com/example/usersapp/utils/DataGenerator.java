package com.example.usersapp.utils;

import com.example.usersapp.model.User;
import com.example.usersapp.model.UserListWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.stream.*;
import java.io.*;
import java.util.*;

public class DataGenerator {
    private final String namesFilePath = "src/main/resources/entryData/firstNames.txt";
    private final String surnamesFilePath = "src/main/resources/entryData/surnames.txt";
    private final String filePath = "src/main/resources/usersData.xml";
    private List<String> names = new ArrayList<>();
    private List<String> surnames = new ArrayList<>();
    @JacksonXmlElementWrapper(localName = "users")
    @JacksonXmlProperty(localName = "user")
    private LinkedList<User> users = new LinkedList<>();


    public boolean prepareData() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = new File(filePath);
            readData(namesFilePath, names);
            readData(surnamesFilePath, surnames);
            createUserData(names, surnames);

            UserListWrapper userListWrapper = new UserListWrapper();
            userListWrapper.setUsers(users);
            xmlMapper.writeValue(file, userListWrapper);
            return true;
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void createUserData(List<String> names, List<String> surnames) throws XMLStreamException, IOException {
        Random random = new Random();

        for (int i = 0; i < 50000; i++) {
            String name = names.get(random.nextInt(names.size()));
            String surname = surnames.get(random.nextInt(surnames.size()));
            String login = createLogin(name, surname);
            users.add(new User(name, surname, login));
        }
    }

    private String createLogin(String name, String surname) {
        String prefix = name.length() >= 3 ? name.substring(0, 3) : "xxx";
        String suffix = surname.length() >= 3 ? surname.toLowerCase().substring(0, 3) : "xxx";
        return prefix + suffix;
    }

    private void readData(String filePath, List<String> destitationList) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while ((line = reader.readLine()) != null) {
            destitationList.add(line);
        }
        reader.close();
    }
}