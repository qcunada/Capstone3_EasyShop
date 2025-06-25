package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT * FROM shopping_cart AS sc  " +
                "JOIN products AS p ON sc.product_id = p.product_id " +
                "WHERE user_id = ?";

        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);


            try(ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    Product product = new Product(rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("category_id"),
                            rs.getString("description"),
                            rs.getString("color"),
                            rs.getInt("stock"),
                            rs.getBoolean("featured"),
                            rs.getString("image_url"));
                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem(rs.getInt("quantity"), product);
                    shoppingCart.add(shoppingCartItem);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return shoppingCart;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1){
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows affected.");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cart cleared.");;
            } else {
                System.out.println("Cart already empty.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected static Product mapRow(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }

}
