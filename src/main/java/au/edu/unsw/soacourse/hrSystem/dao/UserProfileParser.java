package au.edu.unsw.soacourse.hrSystem.dao;



import au.edu.unsw.soacourse.hrSystem.model.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yun Zhang on 2016/05/09.
 */
public class UserProfileParser implements ResultSetParser<UserProfile> {

    @Override
    public List<UserProfile> parse(ResultSet res) throws SQLException {
        List<UserProfile> files = new ArrayList<UserProfile>();
        while(res.next()){
            String userID = res.getString(1);
            String name = res.getString(2);
            String email = res.getString(3);
            String addr = res.getString(4);
            String telNum = res.getString(5);
            String curPos = res.getString(6);
            String education = res.getString(7);
            String skills = res.getString(8);
            String experience = res.getString(9);
            String perDsp = res.getString(10);
            Double amount = res.getDouble(3);

            UserProfile userProfile = new UserProfile(userID,name,email,addr,telNum,curPos,education,skills,experience,perDsp);
            files.add(userProfile);
        }
        return files;
    }
}
