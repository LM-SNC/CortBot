package art.courtbot;

import net.dv8tion.jda.api.entities.Guild;

import java.sql.*;
import java.util.ArrayList;

public class ConnectionManager {
    String url = "jdbc:mysql://46.173.221.33:3306/user1002711_unionsecond?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String username = "";
    String password = "";


    public void onBdCreate() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS servers(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                        guild VARCHAR(32),\n" +
                    "                        verfCh VARCHAR(32),\n" +
                    "                        supCh VARCHAR(32),\n" +
                    "                        PRIMARY KEY(id)\n" +
                    "                        );");

            System.out.println("Successful connection");

            statement.close();
            connection.close();


        } catch (
                SQLException e) {
            e.printStackTrace();
        }

    }

    public void onRemoveScore(String id, int score) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                        userId VARCHAR(32),\n" +
                    "                        isTicket BOOL,\n" +
                    "                        PRIMARY KEY(id)\n" +
                    "                        );");

            System.out.println("Successful connection");

            ResultSet rsMoney = statement.executeQuery("SELECT money FROM users WHERE userId = '" + id + "';");
            int money = 0;
            if (rsMoney.next()) {
                System.out.println(rsMoney.getString(1) + " Значение в бд на данный момент");
                money = Integer.parseInt(rsMoney.getString(1));
            }

            rsMoney.close();
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE userId = '" + id + "';");

            if (rs.next()) {
                if (money >= score) {
                    money = money - score;
                    statement.executeUpdate("UPDATE users SET money = '" + money + "' WHERE userId = " + id);
                    System.out.println("Покупка совершена успешно!");
                } else {
                    System.out.println("Покупка не совершена!");
                }
            } else {
                System.out.println("Юзера не существует: " + id + ". Добавляем!");
                statement.executeUpdate("INSERT INTO users (userId, money) VALUES('" + id + "','" + 0 + "');");
            }

//            if(rs == null){
//                System.out.println("Записи не существует");
//                System.out.println(rs.toString());
//            }else {
//                System.out.println("Запись существует");
//                System.out.println(rs.toString());
//            }
//            System.out.println("Sucsess!");
            System.out.println("Close!");
            rs.close();
            connection.close();
            statement.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public void onAddQuestion(String guildId, String question) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS srv" + guildId + "(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                                            question VARCHAR(512),\n" +
                    "                                            PRIMARY KEY(id)\n" +
                    "                                            );");

            System.out.println("Successful!");

            statement.executeUpdate("INSERT INTO srv" + guildId + "(question) VALUES('" + question + "');");


            System.out.println("Close!");
            connection.close();
            statement.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getQuestion(String guildId) {
        ArrayList<String> questionList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS srv" + guildId + "(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                                            question VARCHAR(512),\n" +
                    "                                            PRIMARY KEY(id)\n" +
                    "                                            );");

            ResultSet rs = statement.executeQuery("select * from srv" + guildId);
            // Количество колонок в результирующем запросе
//            int columns = rs.getMetaData().getColumnCount();
            // Перебор строк с данными
            int count = 0;
            while(rs.next()){
                count++;
                    System.out.print(rs.getString(2) + "\t");
                questionList.add(count + "\t" + rs.getString(2));
                    questionList.add("\n");
                System.out.println();
            }
            System.out.println();
            if(rs != null)
                rs.close();
            if(statement != null)
                statement.close();
            if(connection != null)
                statement.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    public void deleteQuestion(String guildId, int questionNumber) {
        ArrayList<String> questionList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS srv" + guildId + "(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "                                            question VARCHAR(512),\n" +
                    "                                            PRIMARY KEY(id)\n" +
                    "                                            );");

            ResultSet rs = statement.executeQuery("select * from srv" + guildId);
            // Количество колонок в результирующем запросе
//            int columns = rs.getMetaData().getColumnCount();
            // Перебор строк с данными
            int count = 0;
            while(rs.next()){
                count++;
                System.out.print(rs.getString(2) + "\t");
                questionList.add(count + "\t" + rs.getString(2));
                questionList.add("\n");
                System.out.println();
            }
            System.out.println();
            if(rs != null)
                rs.close();
            if(statement != null)
                statement.close();
            if(connection != null)
                statement.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean onVerificationChannel(String guildId, String verfChannelId, String supChannelId) {
        boolean isSuccessful = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            System.out.println("Successful!");

            ResultSet rs = statement.executeQuery("SELECT verfCh FROM servers WHERE guild = '" + guildId + "';");

            if (verfChannelId != null) {
                if (rs.next()) {
                    statement.executeUpdate("UPDATE servers SET verfCh = '" + verfChannelId + "' WHERE guild = " + guildId);
                    isSuccessful = true;
                } else {
                    System.out.println("Сервера не существует: " + guildId + ". Добавляем!");
                    statement.executeUpdate("INSERT INTO servers (guild, verfCh) VALUES('" + guildId + "','" + verfChannelId + "');");
                    isSuccessful = false;
                }
            }

            if (supChannelId != null) {
                if (rs.next()) {
                    statement.executeUpdate("UPDATE servers SET supCh = '" + supChannelId + "' WHERE guild = " + guildId);
                    isSuccessful = true;
                } else {
                    System.out.println("Сервера не существует: " + guildId + ". Добавляем!");
                    statement.executeUpdate("INSERT INTO servers (guild, supCh) VALUES('" + guildId + "','" + supChannelId + "');");
                    isSuccessful = false;
                }
            }


            System.out.println("Close!");
            rs.close();
            connection.close();
            statement.close();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }

    public String getVerificationChannel(String guildId) {
        String channel = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            ResultSet rsMoney = statement.executeQuery("SELECT verfCh FROM servers WHERE guild = '" + guildId + "';");
            if (rsMoney.next()) {
                System.out.println(rsMoney.getString(1) + " Значение в бд на данный момент");
                channel = rsMoney.getString(1);
            }
            rsMoney.close();

            connection.close();
            statement.close();
            System.out.println("Close!");

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return channel;
    }
}
