package au.edu.unsw.soacourse.hrSystem;

import java.io.StringWriter;

import org.junit.Test;

public class XQueryTest {
	@Test
	public void testQuery(){
        StringWriter writer = new StringWriter();
        //String xQuery = String.format("<dataList>{for &v in doc("+file+")/rss/channel/title/text()[contains(., \"Job\")] return &v }</dataList> ", "Job");
        //String xQuery = String.format("<dataList>{for $v in /channel/jobtitle/text()[contains(., \"Job\")] order by $v return $v }</dataList> ", "Job");
    	String xQuery = String.format("<jobalerts>{for $v in /channel/jobtitle/text() where contains($v, \"%s\") order by $v return <jobtitle> { $v } </jobtitle>}</jobalerts> ", "Java");
        System.out.println(xQuery);
        try {
            DataServiceModule.getXMLResource("C:/cs9322-Prac/t.xml", xQuery,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result =  writer.toString();
        System.out.println(result);	
	}

}
