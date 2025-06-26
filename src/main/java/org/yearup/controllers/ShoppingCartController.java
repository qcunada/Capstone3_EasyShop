package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
// only logged in users should have access to these actions
public class ShoppingCartController {
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ProductDao productDao, UserDao userDao, ShoppingCartDao shoppingCartDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping("cart")
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged in username
            // find database user by userId
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            int userId = getUserId(principal);
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (cart == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user.");
            }

            // use the shoppingcartDao to get all items in the cart and return the cart
            return cart;
        } catch(Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("cart/products/{productId}")
    public void addProduct(Principal principal, @PathVariable int productId) {
        try {
            // get the currently logged in username
            // find database user by userId
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            int userId = user.getId();
            shoppingCartDao.addItem(userId, productId, 1);

        } catch (Exception e) {
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
        // add a PUT method to update an existing product in the cart - the url should be
        // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
        // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("cart/products/{productId}")
    public void updateProductQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            int userId = getUserId(principal);

            shoppingCartDao.updateItem(userId, productId, item.getQuantity());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update quantity.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping("cart")
     @ResponseStatus(HttpStatus.NO_CONTENT)
        public void clearCart(Principal principal) {
            try {
                int userId = getUserId(principal);
                shoppingCartDao.clearCart(userId);
            } catch (ResponseStatusException ex) {
                throw ex;

            } catch(Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", ex);

        }
    }
    private int getUserId(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.");
        }
        return user.getId();
    }

    }







