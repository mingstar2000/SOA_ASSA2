package au.edu.unsw.soacourse.hrSystem;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.DocumentWrapper;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class DataServiceModule {

  public static String tmpDir = System.getProperty("java.io.tmpdir");

    //handle path difference
    static {
        if(!tmpDir.endsWith("/") && !tmpDir.endsWith("\\")){
            tmpDir = tmpDir + "/";
        }
    }

     public static void getXMLResource(String eventSetId, Writer writer) throws IOException {
        String savedFilePath = tmpDir + eventSetId + ".xml";
        BufferedReader reader = new BufferedReader(new FileReader(savedFilePath));
        String line = null;
        while((line = reader.readLine()) != null){
          writer.write(line + "\n");
        }
    }

    public static void getXMLResource(String path, String xQuery, Writer writer) throws IOException, ParserConfigurationException, SAXException, XPathException {
        String savedFilePath = path;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new File(savedFilePath));
        document.normalize();

        //static context
        Configuration configuration = new Configuration();
        StaticQueryContext context = new StaticQueryContext(configuration);
        XQueryExpression expression = context.compileQuery(xQuery);

        //dynamic context
        DynamicQueryContext context2 = new DynamicQueryContext(configuration);
        context2.setContextItem(new DocumentWrapper(document, null, configuration));

        Properties props = new Properties();
        props.setProperty(OutputKeys.METHOD, "xml");
        props.setProperty(OutputKeys.INDENT, "yes");
        props.setProperty(OutputKeys.ENCODING, "UTF-8");
        props.setProperty(OutputKeys.VERSION, "1.0");

        //execute query
        try {
            expression.run(context2, new StreamResult(writer), props);
        } catch (XPathException e) {
            e.printStackTrace();
        }
    }


}
