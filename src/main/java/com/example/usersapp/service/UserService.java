package com.example.usersapp.service;

import com.example.usersapp.model.User;
import com.example.usersapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveAll(Collection<User> users) {
        userRepository.saveAll(users);
    }

    public Page<User> findByValueWithHashOrdered(String value, int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, 50);
        String field = sortBy.substring(0, sortBy.lastIndexOf('_'));
        Sort.Direction direction = sortBy.endsWith("_asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<User> usersByValue = userRepository.findByValue(value.toLowerCase(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, field)));
        List<User> hashedUsers = getUsersWithHashedNames(usersByValue);
        return new PageImpl<>(hashedUsers, pageable, usersByValue.getTotalElements());
    }

    public Page<User> findAllWithHashOrdered(int page, String sortBy) {
        Pageable pageable = PageRequest.of(page, 50);
        String field = sortBy.substring(0, sortBy.lastIndexOf('_'));
        Sort.Direction direction = sortBy.endsWith("_asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<User> allUsers = userRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(direction, field)));
        List<User> hashedUsers = getUsersWithHashedNames(allUsers);
        return new PageImpl<>(hashedUsers, pageable, allUsers.getTotalElements());
    }

    private List<User> getUsersWithHashedNames(Page<User> allUsers) {
        return allUsers.getContent().stream()
                .peek(user -> user.setSurname(modifySurnameWithNameHash(user)))
                .toList();
    }

    private String modifySurnameWithNameHash(User user) {
        return user.getSurname() + "_" + DigestUtils.md5DigestAsHex(user.getName().getBytes());
    }
}
