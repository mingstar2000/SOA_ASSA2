package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.UserProfile;

public class ConnectDB {

	public static void updateDB(String dbAddr, String sqlstate) {
		Connection conn = null;
	    Statement stmt = null;
		 try  
	        {  
	         //连接SQLite的JDBC  
	         Class.forName("org.sqlite.JDBC");  
	         //建立一个数据库名employees.db的连接，如果不存在就在当前目录下创建之  
	         conn = DriverManager.getConnection(dbAddr);  
	         conn.setAutoCommit(false);
	         stmt = conn.createStatement();  

	         stmt.executeUpdate(sqlstate);  
	         conn.commit();
	         stmt.close();  
	         conn.close(); //结束数据库的连接   
	        }  
	        catch( Exception e )  
	        {  
	         e.printStackTrace ( );  
	        }  
	}

	public static ResultSet queryDB(String dbAddr, String sqlState) {
		 try  
	        {  
	         //连接SQLite的JDBC  
	         Class.forName("org.sqlite.JDBC");  
	         //建立一个数据库名employees.db的连接，如果不存在就在当前目录下创建之  
	         Connection conn = DriverManager.getConnection(dbAddr);  
	         System.out.println("sql is "+sqlState+"\n");
	         ResultSet rsExist = conn.createStatement().executeQuery(sqlState);  
	         if(rsExist != null)  
	         {
	        	 System.out.println("rsExist is "+rsExist.getString("name")+"\n");

	             //conn.close(); //结束数据库的连接   
	        	 return rsExist;
	         }

	         conn.close(); //结束数据库的连接   
	        }  
	        catch( Exception e )  
	        {  
	         e.printStackTrace ( );  
	        }  
		return null;
	}
	public static <T> List<T> executeSQL(String dbAddr,String sql, ResultSetParser<T> parser) throws SQLException {
		Connection conn = null;
        try {
        	 Class.forName("org.sqlite.JDBC");  
	         //建立一个数据库名employees.db的连接，如果不存在就在当前目录下创建之  
	         conn = DriverManager.getConnection(dbAddr); 
	         Statement statement = conn.createStatement();
	         boolean hasRes =   statement.execute(sql);  
	          
            if (hasRes && parser != null) {
                ResultSet results = statement.getResultSet();
                return parser.parse(results);
            }

        }catch( Exception e )  
        {  
	         e.printStackTrace ( );  
	    }  
        finally {
            assert (conn != null);
            conn.close();
        }
        return null;
    }
	
	
	
}
