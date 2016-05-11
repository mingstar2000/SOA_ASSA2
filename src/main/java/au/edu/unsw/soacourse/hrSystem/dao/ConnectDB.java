package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ConnectDB {

	public static <T> List<T> executeSQL(String dbAddr,String sql, ResultSetParser<T> parser) throws SQLException {
		Connection conn = null;
        try {
        	 Class.forName("org.sqlite.JDBC");  
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
