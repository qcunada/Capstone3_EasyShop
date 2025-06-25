package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addItem(int userId, int productId, int quantity);
    void updateItem(int userId, int productId, int quantity);
    void clearCart(int userId);
}
