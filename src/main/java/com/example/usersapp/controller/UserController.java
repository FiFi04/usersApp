package com.example.usersapp.controller;

import com.example.usersapp.model.User;
import com.example.usersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homeView() {
        return "home";
    }

    @GetMapping("/table")
    public String showUsersPage(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "name_asc") String sortBy,
                                @RequestParam(required = false) String search) {

        Page<User> userPage;
        if (search != null && !search.isEmpty()) {
            userPage = userService.findByValueWithHashOrdered(search, page, sortBy);
            model.addAttribute("search", search);
        } else {
            userPage = userService.findAllWithHashOrdered(page, sortBy);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        return "table";
    }
}
