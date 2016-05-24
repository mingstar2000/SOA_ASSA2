package au.edu.unsw.soacourse.hrSystem.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.soacourse.hrSystem.model.JobPosting;

/**
 * Created by Yun Zhang on 2016/05/14.
 */

public class JobPostingParser implements ResultSetParser<JobPosting>{
	public List<JobPosting> parse(ResultSet res) throws SQLException {
		System.out.print("start jobposting Parser!");
        List<JobPosting> files = new ArrayList<JobPosting>();
        while(res.next()){
        	String jobID = res.getString(1);
            String cmpID = res.getString(2);
            String name = res.getString(3);
            String salaryRate = res.getString(4);
            String posType = res.getString(5);
            String location = res.getString(6);
            String status = res.getString(7);
            String jobDsp = res.getString(8);

            
            JobPosting jobPosting = new JobPosting(jobID,cmpID,name,salaryRate, posType, location, status,jobDsp);
            files.add(jobPosting);
            System.out.print("begin to pass value!");
        }
        System.out.print("nothing from parser!");
		return files;
    }

}
