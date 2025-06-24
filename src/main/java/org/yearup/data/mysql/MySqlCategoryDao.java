package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    private DataSource dataSource;

    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> getAllCategories() {
        String query = "SELECT * from categories";
        List<Category> categories = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ){
            while (resultSet.next()) {
                categories.add(mapRow(resultSet));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id
        String query = "SELECT * from categories\n WHERE category_id = ?;";
        Category category = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1,categoryId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                     category = mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return category;
    }
    @Override
    public Category create(Category category) {
        // create a new category
        String query = "INSERT INTO categories (name) VALUES (?);";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ){
            preparedStatement.setString(1,category.getName());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                category.setCategoryId(resultSet.getInt(1));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String query = "UPDATE categories SET name = ?,                       description = ? \n" +
                        "WHERE category_id = ?; ";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            int rowsAffected =  preparedStatement.executeUpdate();

            if (rowsAffected == 1){
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows affected.");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String query = "DELETE FROM categories \n" +
                "WHERE category_id = ?;";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setInt(1, categoryId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("Row deleted.");;
            } else {
                System.out.println("No row affected.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }
}
