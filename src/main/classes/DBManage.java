import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBManage {


    private  String DRIVER = "com.mysql.cj.jdbc.Driver";
    private  String URL = "jdbc:mysql://";
    private  String USERNAME ;
    private  String PASSWORD ;
    private  String DBName;

    public DBManage(String IP, String USERNAME, String PASSWORD,String Port,String DBName) {
        this.URL = URL+IP+":"+Port+"/"+DBName+"?serverTimezone=UTC";
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.DBName=DBName;
    }

    private static final String SQL = "SELECT * FROM ";// 数据库操作

    public  Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public boolean TCon(){
        boolean result=true;
        Connection con=null;
        try {
            Class.forName(DRIVER);
            con=DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("数据库连接失败");
            result=false;
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);


        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }
    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //获得所有表名
    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(DBName, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tableNames;
    }
    //获得列名

    public  List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columnNames;
    }

    //获得字段类型
    public  List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1)+rsmd.getColumnDisplaySize(i+1));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columnTypes;
    }

}
