package test;


import java.sql.*;

/**
 * Created by LXX on 2019/2/25.
 */
public class JDBC {

    public static void main(String[] args){
        try {
            new JDBC().getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getConnection() throws ClassNotFoundException,SQLException{
        String sql = "SELECT * FROM USER where id = ?";
        String produce = "SELECT * FROM USER ";
        //加载jdbc驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");
//        Class.forName("oracle.jdbc.driver.OracleDriver");
        String oracleURL = "jdbc:oracle:thin:@localhost:1521:orcl";
        try (//创建数据库连接
             Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_tmall_springboot?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC","root","123456");
             //创建statement
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             CallableStatement callableStatement= connection.prepareCall(produce);){
            //执行SQL
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");
            System.out.println("statement execute result");
            //遍历结果集
            while (resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
            preparedStatement.setInt(1,1);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            System.out.println("prepareStatement execute result");
            while (resultSet1.next()){
                System.out.println(resultSet1.getInt(1));
            }
            ResultSet resultSet2 = callableStatement.executeQuery();
            System.out.println("callableStatement execute result");
            while (resultSet2.next()){
                System.out.println(resultSet2.getInt(1));
            }
            //关闭jdbc对象资源
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (resultSet1 != null){
                try {
                    resultSet1.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (resultSet2 != null){
                try {
                    resultSet2.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }
}
