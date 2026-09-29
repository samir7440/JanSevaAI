package com.janseva.backend.service;

import com.janseva.backend.model.User;
import com.janseva.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class UserService {

@Autowired
private UserRepository repository;

@Autowired
private PasswordEncoder passwordEncoder;

public User login(

        String mobile,

        String password

) {

    User user =
            repository.findByMobile(
                    mobile
            );

    if (user == null) {
        return null;
    }

    if (!user.isActive()) {
        return null;
    }

    boolean matched =

            passwordEncoder.matches(

                    password,

                    user.getPassword()
            );

    if (matched) {
        return user;
    }

    return null;
}

public User createUser(

        User user

) {

    user.setPassword(

            passwordEncoder.encode(
                    user.getPassword()
            )
    );

    return repository.save(
            user
    );
}

public User getUserByMobile(
        String mobile
) {

    return repository.findByMobile(
            mobile
    );
}


}
