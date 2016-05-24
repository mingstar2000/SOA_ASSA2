package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.Application;


/**
 * Created by Yun Zhang on 2016/05/14.
 */
public class ApplicationParser implements ResultSetParser<Application> {
    public List<Application> parse(ResultSet res) throws SQLException {
        List<Application> files = new ArrayList<Application>();
        while(res.next()){
            String appID = res.getString(1);
            String userID = res.getString(2);
            String cvLetter = res.getString(3);
            String autoStatus = res.getString(4);
            String jobID = res.getString(5);


            Application application = new Application(appID,userID,cvLetter,autoStatus,jobID);
            files.add(application);
        }
		return files;
    }
}
