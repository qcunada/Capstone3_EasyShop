package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.mysql.MySqlProfileDao;
import org.yearup.models.Product;
import org.yearup.models.Profile;

@RestController
@CrossOrigin
public class ProfileController {
private MySqlProfileDao mySqlProfileDao;

    @Autowired
    public ProfileController(MySqlProfileDao mySqlProfileDao) {
        this.mySqlProfileDao = mySqlProfileDao;
    }

    @GetMapping("profile/{id}")
    public Profile getById(@PathVariable int id ) {
        try {
            var product = mySqlProfileDao.getUserById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        } catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
    @PutMapping("profile/{id}")
    public void update(@PathVariable int id, @RequestBody Profile profile) {
        try {
            mySqlProfileDao.update(id,profile);

        } catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
