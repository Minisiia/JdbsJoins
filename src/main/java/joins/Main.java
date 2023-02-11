package joins;

import java.sql.*;

/**
 * Создайте базу данных с именем “MyJoinsDB” используя Workbench.
 * Используя Workbench в данной базе данных создайте 3 таблицы, В 1-й таблице содержатся имена и номера телефонов сотрудников компании.
 * Во 2-й таблице содержатся ведомости о зарплате и должностях сотрудников: главный директор, менеджер, рабочий.
 * В 3-й таблице содержится информация о семейном положении, дате рождения, и месте проживания.
 * <p>
 * Используя IntelijIdea и JDBC cделайте выборку при помощи JOIN’s для таких заданий:
 * 1) Получите контактные данные сотрудников (номера телефонов, место жительства).
 * 2) Получите информацию о дате рождения всех холостых сотрудников и их номера.
 * 3) Получите информацию обо всех менеджерах компании: дату рождения и номер телефона.
 */

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/myjoinsjdbcdb";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        registerDriver();

        Connection connection = null;
        PreparedStatement ps = null;

        System.out.println("\n Получите контактные данные сотрудников (номера телефонов, место жительства) \t");
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            ps = connection.prepareStatement("SELECT staff.name, staff.phone, personalInfo.adress\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id;");
            showResult(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n Получите информацию о дате рождения всех холостых сотрудников и их номера \n" +
                "В данной бд холостых 3 человека \t");
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            ps = connection.prepareStatement("SELECT staff.name, personalInfo.birth_day, staff.phone\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id\n" +
                    "WHERE personalInfo.maritalStatus IN ('не женат', 'не замужем');");
            showResult(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n Получите информацию обо всех менеджерах компании: дату рождения и номер телефона \n" +
                "В данной бд холостых 3 человека \t");
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            ps = connection.prepareStatement("SELECT staff.name, personalInfo.birth_day, staff.phone\n" +
                    "FROM staff\n" +
                    "JOIN personalInfo\n" +
                    "ON staff.id = personalInfo.staff_id\n" +
                    "JOIN serviceInfo\n" +
                    "ON staff.id = serviceInfo.staff_id\n" +
                    "WHERE serviceInfo.position = 'Менеджер';");
            showResult(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showResult(PreparedStatement ps) throws SQLException {
        ResultSet resultSet = ps.executeQuery();
        ResultSetMetaData md = resultSet.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++)
            System.out.print(md.getColumnName(i) + "\t\t\t");
        System.out.println();
        while (resultSet.next()) {
            for (int j = 1; j <= md.getColumnCount(); j++) {
                System.out.print(resultSet.getString(j) + "\t\t");
            }
            System.out.println();
        }
    }

    private static void registerDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loading success!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
