package au.edu.unsw.soacourse.hrSystem.dao;

import au.edu.unsw.soacourse.hrSystem.model.HiringTeam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HiringTeamParser implements ResultSetParser<HiringTeam> {

    @Override
    public List<HiringTeam> parse(ResultSet res) throws SQLException {
        List<HiringTeam> files = new ArrayList<HiringTeam>();
        while(res.next()){
            String userID = res.getString(1);
            String password = res.getString(2);
            String cmpID = res.getString(3);
            String name = res.getString(4);
            String skills = res.getString(5);

            HiringTeam hiringTeam = new HiringTeam(userID,password,cmpID,name,skills);
            files.add(hiringTeam);
        }
        return files;
    }
}
