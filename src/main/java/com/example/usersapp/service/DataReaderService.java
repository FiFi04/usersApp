package com.example.usersapp.service;

import com.example.usersapp.model.User;
import com.example.usersapp.model.UserListWrapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DataReaderService {
    UserService userService;

    @Autowired
    public DataReaderService(UserService userService) {
        this.userService = userService;
    }

    @Async
    public CompletableFuture<Integer> readFeed(MultipartFile file) {
        try {
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            XmlMapper xmlMapper = new XmlMapper();
            UserListWrapper userListWrapper = xmlMapper.readValue(bufferedInputStream, UserListWrapper.class);
            List<User> users = userListWrapper.getUsers();
            userService.saveAll(users);
            return CompletableFuture.completedFuture(users.size());
        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku");
            return CompletableFuture.failedFuture(e);
        }
    }
}
