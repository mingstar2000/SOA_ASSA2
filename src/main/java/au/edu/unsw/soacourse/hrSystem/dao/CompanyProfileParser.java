package au.edu.unsw.soacourse.hrSystem.dao;

import au.edu.unsw.soacourse.hrSystem.model.CompanyProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyProfileParser implements ResultSetParser<CompanyProfile> {

    @Override
    public List<CompanyProfile> parse(ResultSet res) throws SQLException {
        List<CompanyProfile> files = new ArrayList<CompanyProfile>();
        while(res.next()){
            String cmpID = res.getString(1);
            String name = res.getString(2);
            String email = res.getString(3);
            String addr = res.getString(4);
            String telNum = res.getString(5);
            String indType = res.getString(6);
            String webSite = res.getString(7);
            String cmpDsp = res.getString(8);

            CompanyProfile companyProfile = new CompanyProfile(cmpID,name,email,addr,telNum,indType,webSite,cmpDsp);
            files.add(companyProfile);
        }
        return files;
    }
}
