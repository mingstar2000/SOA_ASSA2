package au.edu.unsw.soacourse.hrSystem.dao;

import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaxIDParser implements ResultSetParser<String> {

    @Override
    public List<String> parse(ResultSet res) throws SQLException {
        List<String> IDs = new ArrayList<String>();
        while(res.next()){
            String ID = res.getString(1);
            IDs.add(ID);
        }
        return IDs;
    }
    
}
