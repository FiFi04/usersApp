package com.example.usersapp.controller;

import com.example.usersapp.service.DataReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Controller
public class FileUploadController {
    private final DataReaderService reader;

    @Autowired
    public FileUploadController(DataReaderService reader) {
        this.reader = reader;
    }

    @GetMapping("/upload")
    public String uploadView() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public CompletableFuture<String> handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        CompletableFuture<Integer> readResult = reader.readFeed(file);
        return readResult.thenApply(numberOfObjects -> {
            model.addAttribute("numberOfObjects", numberOfObjects);
            model.addAttribute("fileUploaded", true);
            return "uploadForm";
        });
    }
}
