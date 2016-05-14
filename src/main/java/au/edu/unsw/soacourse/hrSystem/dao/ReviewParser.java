package au.edu.unsw.soacourse.hrSystem.dao;

import au.edu.unsw.soacourse.hrSystem.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewParser implements ResultSetParser<Review> {

    @Override
    public List<Review> parse(ResultSet res) throws SQLException {
        List<Review> files = new ArrayList<Review>();
        while(res.next()){
            String revID = res.getString(1);
            String appID = res.getString(2);
            String userID = res.getString(3);
            String reStatus1 = res.getString(4);
            String reStatus2 = res.getString(5);
            String magStatus = res.getString(6);

            Review review = new Review(revID,appID,userID,reStatus1,reStatus2,magStatus);
            files.add(review);
        }
        return files;
    }
}
