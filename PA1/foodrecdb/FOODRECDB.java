package ceng.ceng351.foodrecdb;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class FOODRECDB implements IFOODRECDB{
    private static String user = "e2521821";// username;
    private static String password = "zb4o0Yp1jYwkKWqW"; // password;
    private static String host = "momcorp.ceng.metu.edu.tr"; //"host name";
    private static String database = "db2521821";// database name;
    private static int port = 8080; // 8080;

    private Connection con;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con =  DriverManager.getConnection(url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {
        int numberofTablesInserted = 0;

        //MenuItems(itemID:int, itemName:varchar(40), cuisine:varchar(20), price:int)
        String queryCreateMenuItemsTable = "create table if not exists MenuItems (" +
                "itemID int ," +
                "itemName varchar(40) ," +
                "cuisine varchar(20) ," +
                "price int ," +
                "primary key (itemID))";

        //Ingredients(ingredientID:int, ingredientName:varchar(40))
        String queryCreateIngredientsTable = "create table if not exists Ingredients (" +
                "ingredientID int ," +
                "ingredientName varchar(40) ," +
                "primary key (ingredientID))";

        //Includes(itemID:int, ingredientID:int)
        String queryCreateIncludesTable = "create table if not exists Includes (" +
                "itemID int," +
                "ingredientID int," +
                "primary key (itemID, ingredientID)," +
                "foreign key (itemID) references MenuItems(itemID) on delete cascade on update cascade," +
                "foreign key (ingredientID) references Ingredients(ingredientID) on delete cascade on update cascade)";

        //Ratings(ratingID:int, itemID:int, rating:int, ratingDate:date)
        String queryCreateRatingsTable = "create table if not exists Ratings (" +
                "ratingID int," +
                "itemID int," +
                "rating int," +
                "ratingDate date," +
                "primary key (ratingID)," +
                "foreign key (itemID) references MenuItems(itemID) on delete cascade on update cascade)";

        //DietaryCategories(ingredientID:int, dietaryCategory:varchar(20))
        String queryCreateDietaryCategoriesTable = "create table if not exists DietaryCategories (" +
                "ingredientID int," +
                "dietaryCategory varchar(20)," +
                "primary key (ingredientID, dietaryCategory)," +
                "foreign key (ingredientID) references Ingredients(ingredientID) on delete cascade on update cascade)";


        try {
            Statement statement = this.con.createStatement();

            //MenuItems Table
            statement.executeUpdate(queryCreateMenuItemsTable);
            numberofTablesInserted++;

            //Ingredients Table
            statement.executeUpdate(queryCreateIngredientsTable);
            numberofTablesInserted++;

            //Includes Table
            statement.executeUpdate(queryCreateIncludesTable);
            numberofTablesInserted++;

            //Ratings Table
            statement.executeUpdate(queryCreateRatingsTable);
            numberofTablesInserted++;

            //DietaryCategories Table
            statement.executeUpdate(queryCreateDietaryCategoriesTable);
            numberofTablesInserted++;

            //close
            statement.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return numberofTablesInserted;
    }

    @Override
    public int dropTables() {
        int numberofTablesDropped = 0;

        String queryDropMenuItemsTable = "drop table if exists MenuItems";

        String queryDropIngredientsTable = "drop table if exists Ingredients";

        String queryDropIncludesTable = "drop table if exists Includes";

        String queryDropRatingsTable = "drop table if exists Ratings";

        String queryDropDietaryCategoriesTable = "drop table if exists DietaryCategories";

        try{
            Statement statement = this.con.createStatement();

            statement.executeUpdate(queryDropIncludesTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropRatingsTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropDietaryCategoriesTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropMenuItemsTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropIngredientsTable);
            numberofTablesDropped++;

            statement.close();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return numberofTablesDropped;
    }

    @Override
    public int insertMenuItems(MenuItem[] items) {
        int numberofRowsInserted = 0;

        for (int i = 0; i < items.length; i++) {
            MenuItem item = items[i];

            String query = "insert into MenuItems values('"+
                    item.getItemID()+"','"+
                    item.getItemName()+"','"+
                    item.getCuisine()+"','"+
                    item.getPrice()+"')";

            try {
                Statement stament = this.con.createStatement();
                stament.executeUpdate(query);
                stament.close();
                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertIngredients(Ingredient[] ingredients) {
        int numberofRowsInserted = 0;

        for (int i = 0; i < ingredients.length; i++) {
            Ingredient ingredient = ingredients[i];

            String query = "insert into Ingredients values('" +
                    ingredient.getIngredientID()+"','"+
                    ingredient.getIngredientName()+"')";

            try {
                Statement stament = this.con.createStatement();
                stament.executeUpdate(query);
                stament.close();
                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertIncludes(Includes[] includes) {
        int numberofRowsInserted = 0;

        for (int i = 0; i < includes.length; i++) {
            Includes include = includes[i];

            String query = "insert into Includes values('" +
                    include.getItemID()+"','"+
                    include.getIngredientID()+"')";
            try {
                Statement statement=this.con.createStatement();
                statement.executeUpdate(query);
                statement.close();
                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertDietaryCategories(DietaryCategory[] categories) {
        int numberofRowsInserted = 0;

        for (int i = 0; i < categories.length; i++) {
            DietaryCategory category = categories[i];

            String query = "insert into DietaryCategories values('" +
                    category.getIngredientID()+"','"+
                    category.getDietaryCategory()+"')";
            try {
                Statement statement=this.con.createStatement();
                statement.executeUpdate(query);
                statement.close();
                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertRatings(Rating[] ratings) {
        int numberofRowsInserted = 0;

        for (int i = 0; i < ratings.length; i++) {
            Rating rating = ratings[i];

            String query = "insert into Ratings values('"+
                    rating.getRatingID()+"','"+
                    rating.getItemID()+"','"+
                    rating.getRating()+"','"+
                    rating.getRatingDate()+"')";
            try {
                Statement statement=this.con.createStatement();
                statement.executeUpdate(query);
                statement.close();
                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public MenuItem[] getMenuItemsWithGivenIngredient(String name) {
        ResultSet result;
        ArrayList<MenuItem> resultlist = new ArrayList<>();

        String query = "SELECT DISTINCT M.itemID, M.itemName, M.cuisine, M.price "+
                        "FROM MenuItems M, Ingredients G, Includes C "+
                        "WHERE G.ingredientName = '" + name +
                        "' and C.ingredientID = G.ingredientID and C.itemID = M.itemID "+
                        "ORDER BY M.itemID ASC;";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                Integer itemID = result.getInt("itemID");
                String itemName = result.getString("itemName");
                String cuisine = result.getString("cuisine");
                Integer price = result.getInt("price");

                MenuItem item = new MenuItem(itemID, itemName, cuisine, price);
                resultlist.add(item);
            }
            statement.close();
        }
        catch (SQLException e) {
        e.printStackTrace();
        }

        MenuItem[] resultarray = new MenuItem[resultlist.size()];
        return resultlist.toArray(resultarray);
    }

    @Override
    public MenuItem[] getMenuItemsWithoutAnyIngredient() {
        ResultSet result;
        ArrayList<MenuItem> resultlist = new ArrayList<>();

        String query = "SELECT DISTINCT *\n" +
                "FROM MenuItems M \n" +
                "WHERE NOT EXISTS (SELECT *\n" +
                "\t\t\t\t      FROM Includes C\n" +
                "\t\t\t\t      WHERE C.itemID = M.itemID)\n" +
                "ORDER BY M.itemID ASC;";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                Integer itemID = result.getInt("itemID");
                String itemName = result.getString("itemName");
                String cuisine = result.getString("cuisine");
                Integer price = result.getInt("price");

                MenuItem item = new MenuItem(itemID, itemName, cuisine, price);
                resultlist.add(item);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] resultarray = new MenuItem[resultlist.size()];
        return resultlist.toArray(resultarray);
    }

    @Override
    public Ingredient[] getNotIncludedIngredients() {
        ResultSet result;
        ArrayList<Ingredient> resultlist = new ArrayList<>();

        String query = "SELECT DISTINCT *\n" +
                "FROM Ingredients G\n" +
                "WHERE G.ingredientID NOT IN (SELECT DISTINCT G1.ingredientID\n" +
                "\t\t\t\t\t\t       FROM Ingredients G1, Includes C\n" +
                "\t\t\t\t\t\t       WHERE C.ingredientID = G1.ingredientID)\n" +
                "ORDER BY G.ingredientID ASC;";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                Integer ingredientID = result.getInt("ingredientID");
                String ingredientName = result.getString("ingredientName");


                Ingredient item = new Ingredient(ingredientID, ingredientName);
                resultlist.add(item);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Ingredient[] resultarray = new Ingredient[resultlist.size()];
        return resultlist.toArray(resultarray);

    }

    @Override
    public MenuItem getMenuItemWithMostIngredients() {
        ResultSet result;
        MenuItem item = null;

        String query = "SELECT DISTINCT M.itemID, M.itemName, M.cuisine, M.price\n" +
                "FROM MenuItems M, (SELECT C.itemID, COUNT(*) as num\n" +
                "\t\t\t\t      FROM Includes C\n" +
                "\t\t\t\t      GROUP BY C.itemID) as M1\n" +
                "WHERE M1.itemID = M.itemID and M1.num = (SELECT MAX(M2.num)\n" +
                "\t\t\t\t\t\t\t\t\t       FROM (SELECT C.itemID, COUNT(*) as num\n" +
                "\t\t\t\t      \t\t\t\t\t\t\t    FROM Includes C\n" +
                "\t\t\t\t      \t\t\t\t\t\t\t    GROUP BY C.itemID) as M2);";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            result.next();
            Integer itemID = result.getInt("itemID");
            String itemName = result.getString("itemName");
            String cuisine = result.getString("cuisine");
            Integer price = result.getInt("price");

            item = new MenuItem(itemID, itemName, cuisine, price);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public QueryResult.MenuItemAverageRatingResult[] getMenuItemsWithAvgRatings() {
        ResultSet result;
        ArrayList<QueryResult.MenuItemAverageRatingResult> resultlist = new ArrayList<>();

        String query = "SELECT DISTINCT *\n" +
                "FROM (SELECT M.itemID, M.itemName\n" +
                "\t     FROM MenuItems M) as T1\n" +
                "\t     NATURAL LEFT OUTER JOIN \n" +
                "\t     (SELECT M1.itemID, M1.itemName, AVG(R.rating) as avgRating\n" +
                "\t       FROM MenuItems M1, Ratings R\n" +
                "\t       WHERE M1.itemID = R.itemID\n" +
                "\t       GROUP BY M1.itemID) as T2\n" +
                "ORDER BY avgRating DESC;";
        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                String itemID = result.getString("itemID");
                String itemName = result.getString("itemName");
                String avgRating = result.getString("avgRating");


                QueryResult.MenuItemAverageRatingResult res = new QueryResult.MenuItemAverageRatingResult(itemID, itemName,avgRating);
                resultlist.add(res);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.MenuItemAverageRatingResult[] resultarray = new QueryResult.MenuItemAverageRatingResult[resultlist.size()];
        return resultlist.toArray(resultarray);
    }

    @Override
    public MenuItem[] getMenuItemsForDietaryCategory(String category) {
        ResultSet result;
        ArrayList<MenuItem> resultlist = new ArrayList<>();

        String query = "\n" +
                "SELECT DISTINCT *\n" +
                "FROM MenuItems M\n" +
                "WHERE EXISTS (SELECT * FROM Includes C2 WHERE C2.itemID = M.itemID) and  NOT EXISTS (SELECT C.ingredientID\n" +
                "\t\t\t\t            \t\t     \t\t\t\t\t\t\t\t\t\t     \t\t\t      FROM Includes C\n" +
                "\t\t\t\t             \t\t    \t\t\t\t\t\t\t\t\t\t     \t\t\t      WHERE M.itemID = C.itemID and NOT EXISTS (SELECT C1.ingredientID\n" +
                "\t\t\t\t       \t\t\t\t\t    \t\t\t \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t       FROM Includes C1, DietaryCategories D\n" +
                "\t\t\t\t       \t\t\t\t\t    \t\t\t\t\t\t                              \t\t\t\t\t\t\t\t\t\t\t\t       WHERE C.ingredientID = C1.ingredientID and C1.itemID = M.itemID and C1.ingredientID = D.ingredientID and D.dietaryCategory = '" + category + "'))\n" +
                "ORDER BY M.itemID ASC;";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                Integer itemID = result.getInt("itemID");
                String itemName = result.getString("itemName");
                String cuisine = result.getString("cuisine");
                Integer price = result.getInt("price");

                MenuItem item = new MenuItem(itemID, itemName, cuisine, price);
                resultlist.add(item);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] resultarray = new MenuItem[resultlist.size()];
        return resultlist.toArray(resultarray);

    }

    @Override
    public Ingredient getMostUsedIngredient() {
        ResultSet result;
        Ingredient ingredient = null;

        String query = "SELECT DISTINCT G.ingredientID, G.ingredientName\n" +
                "FROM Ingredients G, (SELECT C.ingredientID, COUNT(*) as num\n" +
                "\t\t\t\t      FROM Includes C\n" +
                "\t\t\t\t      GROUP BY C.ingredientID) as C1\n" +
                "WHERE C1.ingredientID = G.ingredientID and C1.num = (SELECT MAX(C2.num)\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t FROM (SELECT C.ingredientID, COUNT(*) as num\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t \t      FROM Includes C\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t      GROUP BY C.ingredientID) as C2);";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            result.next();
            Integer ingredientID = result.getInt("ingredientID");
            String ingredientName = result.getString("ingredientName");


            ingredient = new Ingredient(ingredientID, ingredientName);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredient;

    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgRating() {
        ResultSet result;
        ArrayList<QueryResult.CuisineWithAverageResult> resultlist = new ArrayList<>();

        String query = "SELECT DISTINCT *\n" +
                "FROM (SELECT M.cuisine\n" +
                "\t     FROM MenuItems M) as T1\n" +
                "             NATURAL LEFT OUTER JOIN \n" +
                "\t     (SELECT M1.cuisine, AVG(R.rating) as averageRating\n" +
                "\t      FROM MenuItems M1, Ratings R\n" +
                "\t      WHERE M1.itemID = R.itemID\n" +
                "\t      GROUP BY M1.cuisine) as T2\n" +
                "ORDER BY averageRating DESC;";
        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                String cuisineName = result.getString("cuisine");
                String average = result.getString("averageRating");

                QueryResult.CuisineWithAverageResult res = new QueryResult.CuisineWithAverageResult(cuisineName, average);
                resultlist.add(res);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.CuisineWithAverageResult[] resultarray = new QueryResult.CuisineWithAverageResult[resultlist.size()];
        return resultlist.toArray(resultarray);
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgIngredientCount() {
        ResultSet result;
        ArrayList<QueryResult.CuisineWithAverageResult> resultlist = new ArrayList<>();

        String query = "SELECT cuisine, IFNULL(SUM(num),0) / COUNT(*) as averageCount\n" +
                "FROM  (SELECT DISTINCT M.itemID, M.cuisine FROM MenuItems M) as T1\n" +
                "\t      NATURAL LEFT OUTER JOIN\n" +
                "\t     (SELECT DISTINCT C.itemID, COUNT(*) as num FROM Includes C GROUP BY C.itemID) as T2\n" +
                "GROUP BY cuisine\n" +
                "ORDER BY averageCount DESC;";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            while(result.next()){
                String cuisineName = result.getString("cuisine");
                String average = result.getString("averageCount");

                QueryResult.CuisineWithAverageResult res = new QueryResult.CuisineWithAverageResult(cuisineName, average);
                resultlist.add(res);
            }
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.CuisineWithAverageResult[] resultarray = new QueryResult.CuisineWithAverageResult[resultlist.size()];
        return resultlist.toArray(resultarray);
    }

    @Override
    public int increasePrice(String ingredientName, String increaseAmount) {
        int numberofRowsAffected = 0;

        String query = "UPDATE MenuItems M, Includes C, Ingredients G\n" +
                       "SET M.price = M.price + '" + increaseAmount + "'\n" +
                       "WHERE M.itemID = C.itemID and G.ingredientName = '" + ingredientName + "' and G.ingredientID = C.ingredientID;";

        try {
            Statement statement = this.con.createStatement();
            numberofRowsAffected = statement.executeUpdate(query);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return numberofRowsAffected;
    }

    @Override
    public Rating[] deleteOlderRatings(String date) {
        ResultSet result;
        ArrayList<Rating> resultlist = new ArrayList<>();

        String query1 = "SELECT *\n" +
                "FROM Ratings R\n" +
                "WHERE R.ratingDate < '" + date + "'\n" +
                "ORDER BY R.ratingID ASC;";

        String query2 = "DELETE FROM Ratings R\n" +
                "WHERE R.ratingDate < '" + date + "';";

        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query1);

            while(result.next()){
                Integer ratingID = result.getInt("ratingID");
                Integer itemID = result.getInt("itemID");
                Integer rating = result.getInt("rating");
                String ratingDate = result.getString("ratingDate");

                Rating rating_obj = new Rating(ratingID, itemID, rating, ratingDate);
                resultlist.add(rating_obj);
            }

            try {
                Statement statement2 = this.con.createStatement();
                statement2.executeUpdate(query2);
                statement2.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Rating[] resultarray = new Rating[resultlist.size()];
        return resultlist.toArray(resultarray);
    }
}
