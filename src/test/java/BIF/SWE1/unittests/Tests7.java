package BIF.SWE1.unittests;

import BIF.SWE1.uebungen.Tests7Provider;
import BIF.SWE1.uebungen.UEB6;
import comp.IPlugin;
import comp.IRequest;
import comp.IResponse;
import org.junit.*;

import java.io.*;
import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Tests7  extends AbstractTestFixture<Tests7Provider> {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Override
    protected Tests7Provider createInstance() {
        return new Tests7Provider();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private StringBuilder getBody(IResponse resp) throws UnsupportedEncodingException, IOException {
        StringBuilder body = new StringBuilder();

        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        try {
            resp.send(ms);
            BufferedReader sr = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
            String line;
            while ((line = sr.readLine()) != null) {
                body.append(line + "\n");
            }
        } finally {
            ms.close();
        }
        return body;
    }

    @Test
    public void HelloWorld() throws Exception {
        Tests7Provider ueb = createInstance();
        ueb.helloWorld();
    }

    @Test
    public void self_test(){
        Tests7Provider ueb = createInstance();
        assertNotNull("Tests7.getNavigationPlugin returned null", ueb);

        IPlugin obj = ueb.getOraclePlugin();
        assertNotNull("Tests7.getOraclePlugin returned null", obj);

        String url = ueb.getOracleUrl();
        assertNotNull("Tests7.getOracleUrl returned null", url);

    }

    @Test
    public void oracle_returns_list_of_tables() throws Exception
    {
        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getOraclePlugin();
        assertNotNull("Tests7.getOraclePlugin returned null", obj);

        String url = ueb.getOracleUrl();
        assertNotNull("Tests7.getOracleUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT owner, table_name FROM all_tables"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        StringBuilder body = getBody(resp);
        //assertTrue("Not found: Bitte geben Sie eine Anfrage ein", body.toString().contains("Bitte geben Sie eine Anfrage ein"));
        assertTrue("Data in response body missing!", body.toString().contains("OWNER"));
        assertTrue("Data in response body missing!", body.toString().contains("SYS"));
        assertTrue("Data in response body missing!", body.toString().contains("TABLE_NAME"));
        assertTrue("Data in response body missing!", body.toString().contains("DUAL"));
        assertTrue("Data in response body missing!", body.toString().contains("SYSTEM_PRIVILEGE_MAP"));
        assertTrue("Data in response body missing!", body.toString().contains("ODCI_WARNINGS"));
    }

    @Test
    public void static_utility_method_for_content_type_returns(){

        String type = IPlugin.util.getContentType("myImg.png");
        assertTrue(type.equals("image/png"));
        type = IPlugin.util.getContentType("myImg.html");
        assertTrue(type.equals("text/html"));
        type = IPlugin.util.getContentType("myImg.jpg");
        assertTrue(type.equals("image/jpeg"));
        type = IPlugin.util.getContentType("myStyle.css");
        assertTrue(type.equals("text/css"));
        type = IPlugin.util.getContentType("myText.txt");
        assertTrue(type.equals("text/plain"));
        type = IPlugin.util.getContentType("myCode.js");
        assertTrue(type.equals("text/javascript"));
    }

    @Test
    public void static_utility_method_file_not_found_returns() throws Exception{

        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getTemperaturePlugin();
        assertNotNull("Tests7.getTemperaturePlugin returned null", obj);

        String url = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
        assertNotNull("Tests7.getTemperatureUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM temperatures"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        IPlugin.util.fileNotFound(resp, "someFile.xyz");
        assertEquals(404, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        String body = getBody(resp).toString();
        assertTrue(body.contains("File not found"));
        assertTrue(body.contains("<p>File not found! ... Sorry!</p>"));
        assertTrue(body.contains("Webio"));
        assertTrue(body.contains("<div id = \"NotFoundBox\" class = \"warm\">"));
        assertTrue(body.contains("<div id = \"Information\">"));
        assertTrue(body.contains("body"));
    }

    @Test
    public void oracle_returns_list_of_stars() throws Exception
    {
        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getOraclePlugin();
        assertNotNull("Tests7.getOraclePlugin returned null", obj);

        String url = ueb.getOracleUrl();
        assertNotNull("Tests7.getOracleUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM stars"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        StringBuilder body = getBody(resp);
        //assertTrue("Not found: Bitte geben Sie eine Anfrage ein", body.toString().contains("Bitte geben Sie eine Anfrage ein"));
        assertTrue("Data in response body missing!", body.toString().contains("NAME"));
        assertTrue("Data in response body missing!", body.toString().contains("ID"));
        assertTrue("Data in response body missing!", body.toString().contains("CODE"));
        assertTrue("Data in response body missing!", body.toString().contains("CREATED"));
        assertTrue("Data in response body missing!", body.toString().contains("Sun"));
        assertTrue("Data in response body missing!", body.toString().contains("ORBITAL_OBJECT_ID"));
    }

    @Test
    public void temp_plugin_returns_list_of_temperatures() throws Exception
    {
        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getTemperaturePlugin();
        assertNotNull("Tests7.getNavigationPlugin returned null", obj);

        String url = "Temp";//ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
        assertNotNull("Tests7.getNaviUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM temperatures"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        StringBuilder body = getBody(resp);
        //assertTrue("Not found: Bitte geben Sie eine Anfrage ein", body.toString().contains("Bitte geben Sie eine Anfrage ein"));
        assertTrue("Data in response body missing!", body.toString().contains("created"));
        assertTrue("Data in response body missing!", body.toString().contains("id"));
        assertTrue("Data in response body missing!", body.toString().contains("value"));
        assertTrue("Data in response body missing!", body.toString().contains(":23.43"));
        assertTrue("Data in response body missing!", body.toString().contains("},{"));
        //assertTrue("Data in response body missing!", body.toString().contains(":45"));
    }

    @Test
    public void temp_plugin_returns_list_of_temperatures_as_xml() throws Exception
    {
        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getTemperaturePlugin();
        assertNotNull("Tests7.getNavigationPlugin returned null", obj);

        String url = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
        assertNotNull("Tests7.getNaviUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM temperatures"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        StringBuilder body = getBody(resp);
        //assertTrue("Not found: Bitte geben Sie eine Anfrage ein", body.toString().contains("Bitte geben Sie eine Anfrage ein"));
        assertTrue("Data in response body missing!", body.toString().contains("created"));
        assertTrue("Data in response body missing!", body.toString().contains("id"));
        assertTrue("Data in response body missing!", body.toString().contains("value"));
        assertTrue("Data in response body missing!", body.toString().contains("<value>16.0509</"));
        assertTrue("Data in response body missing!", body.toString().contains("<created>"));
        //assertTrue("Data in response body missing!", body.toString().contains(":45"));
    }

    @Test
    public void test_plugin_utility_and_sql_setup(){
        String[] commands = new String[0];
        File file = new File("db/", "setup.sql");
        int fileLength = (int) file.length();
        try {
            byte[] fileData = IPlugin.util.readFileData(file, fileLength);
            assertTrue(fileData!=null);
            assertTrue(fileData.length>0);
            String query = new String(fileData);
            assertTrue(query!=null);
            commands = query.split("--<#SPLIT#>--");
            assertTrue("", commands.length>=7);
            for(String command : commands){
                assertTrue("Statement does not conatin 'temperatures'!", command.contains("temperatures"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception occurred! Could not read 'setup.sql'!", false);
        }

    }


    @Test
    public void index_file_must_contain(){
        File file = new File("webroot/", "index.html");
        int fileLength = (int) file.length();
        try {
            byte[] fileData = IPlugin.util.readFileData(file, fileLength);
            assertTrue(fileData!=null);
            assertTrue(fileData.length>0);
            String index = new String(fileData);
            assertTrue(index!=null);
            assertTrue(index.contains("Java"));
            assertTrue(index.contains("Webio"));
            assertTrue(index.contains("Oracle"));
            assertTrue(index.contains("query"));
            assertTrue(index.contains("data"));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception occurred! Could not read 'setup.sql'!", false);
        }

    }

    @Test
    public void index_file_must_contain2(){
        File file = new File("webroot/", "index.html");
        int fileLength = (int) file.length();
        try {
            byte[] fileData = IPlugin.util.readFileData(file, fileLength);
            assertTrue(fileData!=null);
            assertTrue(fileData.length>0);
            String index = new String(fileData);
            assertTrue(index!=null);
            assertTrue(index.contains("table"));
            assertTrue(index.contains("SubmitToLower"));
            assertTrue(index.contains("$(\"#LoweredContent\").html(data);"));
            assertTrue(index.contains("<input type=\"text\" id=\"ToBeLowered\">"));
            assertTrue(index.contains("id=\"TempResult\""));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception occurred! Could not read 'setup.sql'!", false);
        }

    }

    @Test
    public void html_404_file_must_contain(){
        File file = new File("webroot/", "404.html");
        int fileLength = (int) file.length();
        try {
            byte[] fileData = IPlugin.util.readFileData(file, fileLength);
            assertTrue(fileData!=null);
            assertTrue(fileData.length>0);
            String html = new String(fileData);
            assertTrue(html!=null);
            assertTrue(html.contains("<p>File not found! ... Sorry!</p>"));
            assertTrue(html.contains("Webio"));
            assertTrue(html.contains("<div id = \"NotFoundBox\" class = \"warm\">"));
            assertTrue(html.contains("<div id = \"Information\">"));
            assertTrue(html.contains("body"));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception occurred! Could not read 'setup.sql'!", false);
        }

    }

    @Test
    public void temp_plugin_temp_database_gets_bigger_over_time() throws Exception
    {
        Tests7Provider ueb = createInstance();

        IPlugin obj = ueb.getTemperaturePlugin();
        assertNotNull("Tests7.getNavigationPlugin returned null", obj);

        String url = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
        assertNotNull("Tests7.getNaviUrl returned null", url);

        IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM temperatures"));
        assertNotNull("Tests7.GetRequest returned null", req);

        float canHandle = obj.canHandle(req);
        assertTrue(canHandle > 0 && canHandle <= 1);

        IResponse resp = obj.handle(req);
        assertNotNull(resp);
        assertEquals(200, resp.getStatusCode());
        assertTrue(resp.getContentLength() > 0);

        String body = getBody(resp).toString();

        //WAITING!
        Thread.sleep(3500);

        IRequest req2 = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "SELECT * FROM temperatures"));
        assertNotNull("Tests7.GetRequest returned null", req2);
        canHandle = obj.canHandle(req2);
        assertTrue(canHandle > 0 && canHandle <= 1);
        IResponse resp2 = obj.handle(req);
        assertNotNull(resp2);
        assertEquals(200, resp2.getStatusCode());
        assertTrue(resp2.getContentLength() > 0);

        String body2 = getBody(resp2).toString();

        assertTrue(body2.length()>body.length());
        assertTrue(body2.split("id").length>body.split("id").length);
    }



}
