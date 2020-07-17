package testsuite.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import testsuite.provider.Test_6_Provider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import comp.IPlugin;
import comp.IRequest;
import comp.IResponse;


/* Placeholder */
public class Test_6_Temperature_Plugin extends AbstractTestFixture<Test_6_Provider> {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected Test_6_Provider createInstance() {
		return new Test_6_Provider();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void HelloWorld() throws Exception {
		Test_6_Provider ueb = createInstance();
		ueb.helloWorld();
	}

	/***************************************** Temperature plugin *****************************************/ 
	@Test
	public void temp_plugin_HelloWorld()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_get_url()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("IUEB6.getTemperatureUrl returned null", url);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_get_url_2()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url_1 = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("IUEB6.getTemperatureUrl returned null", url_1);
		
		String url_2 = ueb.getTemperatureUrl(LocalDate.of(2015, 1, 1), LocalDate.of(2014, 5, 2));
		assertNotNull("IUEB6.getTemperatureUrl returned null", url_2);
		
		assertNotEquals(url_1, url_2);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_get_rest_url()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url = ueb.getTemperatureRestUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("IUEB6.getTemperatureUrl returned null", url);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_get_different_urls()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url_html = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("UEB6.getTemperatureUrl returned null", url_html);
		
		String url_rest = ueb.getTemperatureRestUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("UEB6.getTemperatureUrl returned null", url_rest);
		
		assertNotEquals(url_html, url_rest);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_handle() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url = ueb.getTemperatureUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("UEB6.getTemperatureUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url));
		assertNotNull("UEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertEquals("text/html", resp.getContentType());
		assertTrue(resp.getContentLength() > 0);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void temp_plugin_handle_rest_call() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getTemperaturePlugin();
		assertNotNull("UEB6.getTemperaturePlugin returned null", obj);

		String url = ueb.getTemperatureRestUrl(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 2));
		assertNotNull("IUEB6.getTemperatureUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url));
		assertNotNull("IUEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertEquals("text/xml", resp.getContentType());
		assertTrue(resp.getContentLength() > 0);
	}
	
	/***************************************** Navigation plugin *****************************************/ 
	@Test
	public void navi_plugin_HelloWorld()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getNavigationPlugin();
		assertNotNull("UEB6.GetNavigationPlugin returned null", obj);
	}

	@Test
	public void navi_plugin_get_url()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getNavigationPlugin();
		assertNotNull("UEB6.GetNavigationPlugin returned null", obj);

		String url = ueb.getNaviUrl();
		assertNotNull("IUEB6.getNaviUrl returned null", url);
	}
	
	@Test
	public void navi_plugin_handle() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getNavigationPlugin();
		assertNotNull("UEB6.getNavigationPlugin returned null", obj);

		String url = ueb.getNaviUrl();
		assertNotNull("IUEB6.getNaviUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "street=Hauptplatz"));
		assertNotNull("IUEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
	}
	
	@Test
	public void navi_plugin_contains_summary() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getNavigationPlugin();
		assertNotNull("UEB6.getNavigationPlugin returned null", obj);

		String url = ueb.getNaviUrl();
		assertNotNull("IUEB6.getNaviUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "street=Hauptplatz"));
		assertNotNull("IUEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
		
		StringBuilder body = getBody(resp);
		assertTrue("Not found: Orte gefunden", body.toString().contains("Orte gefunden")); // 42 Orte gefunden
	}
	@Test
	public void navi_plugin_handle_empty() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getNavigationPlugin();
		assertNotNull("UEB6.getNavigationPlugin returned null", obj);

		String url = ueb.getNaviUrl();
		assertNotNull("UEB6.getNaviUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "street="));
		assertNotNull("UEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
		
		StringBuilder body = getBody(resp);
		assertTrue("Not found: Bitte geben Sie eine Anfrage ein", body.toString().contains("Bitte geben Sie eine Anfrage ein"));
	}
	/***************************************** ToLower plugin *****************************************/ 
	@Test
	public void lower_plugin_HelloWorld()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getToLowerPlugin();
		assertNotNull("UEB6.getToLowerPlugin returned null", obj);
	}

	@Test
	public void lower_plugin_get_url()
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getToLowerPlugin();
		assertNotNull("UEB6.getToLowerPlugin returned null", obj);

		String url = ueb.getToLowerUrl();
		assertNotNull("IUEB6.getToLowerUrl returned null", url);
	}
	
	@Test
	public void lower_plugin_handle() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getToLowerPlugin();
		assertNotNull("UEB6.getToLowerPlugin returned null", obj);

		String url = ueb.getToLowerUrl();
		assertNotNull("IUEB6.getToLowerUrl returned null", url);
		
        String textToTest = String.format("Hello - WorlD! %s", UUID.randomUUID());

		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", String.format("text=%s",  textToTest)));
		assertNotNull("IUEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
		
		StringBuilder body = getBody(resp);
		String textToTestLower = textToTest.toLowerCase();
		assertTrue(String.format("Not found: %s", textToTestLower), body.toString().contains(textToTestLower));
	}
	
	@Test
	public void lower_plugin_handle_empty() throws Exception
	{
		Test_6_Provider ueb = createInstance();

		IPlugin obj = ueb.getToLowerPlugin();
		assertNotNull("UEB6.getToLowerPlugin returned null", obj);

		String url = ueb.getToLowerUrl();
		assertNotNull("IUEB6.getToLowerUrl returned null", url);
		
		IRequest req = ueb.getRequest(RequestHelper.getValidRequestStream(url, "POST", "text="));
		assertNotNull("IUEB6.GetRequest returned null", req);

		float canHandle = obj.canHandle(req);
		assertTrue(canHandle > 0 && canHandle <= 1);

		IResponse resp = obj.handle(req);
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertTrue(resp.getContentLength() > 0);
		
		StringBuilder body = getBody(resp);
		assertTrue("Not found: Bitte geben Sie einen Text ein", body.toString().contains("Bitte geben Sie einen Text ein"));
	}
}
