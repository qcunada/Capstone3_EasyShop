package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.UserDao;
import org.yearup.data.mysql.MySqlProfileDao;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("profile")
public class ProfileController {
private MySqlProfileDao mySqlProfileDao;
private UserDao userDao;


    @Autowired
    public ProfileController(MySqlProfileDao mySqlProfileDao, UserDao userDao) {
        this.mySqlProfileDao = mySqlProfileDao;
        this.userDao = userDao;
    }

    @GetMapping("")
    public Profile getProfile(Principal principal) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        Profile profile = mySqlProfileDao.getUserById(user.getId());

        if (profile == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");

        return profile;
    }
    @PutMapping("")
    public void update(Principal principal, @RequestBody Profile profile) {
        String username = principal.getName();
        User user = userDao.getByUserName(username);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        profile.setUserId(user.getId());
        mySqlProfileDao.update(profile);
    }
}
