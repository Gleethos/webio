package testsuite.provider;

import java.io.InputStream;
import java.time.LocalDate;

import comp.IPlugin;
import comp.IPluginManager;
import comp.IRequest;
import comp.imp.PluginManager;
import comp.imp.Request;
import comp.imp.plugins.Navigator;
import comp.imp.plugins.TemperatureReader;
import comp.imp.plugins.ToLower;

public class Test_6_Provider {

	public void helloWorld() {

	}

	public IRequest getRequest(InputStream inputStream) {
		return new Request(inputStream);
	}

	public IPluginManager getPluginManager() {
		return new PluginManager();
	}

	public IPlugin getTemperaturePlugin() {
		return new TemperatureReader();
	}

	public IPlugin getNavigationPlugin() {
		return new Navigator();
	}

	public IPlugin getToLowerPlugin() {
		return new ToLower();
	}

	public String getTemperatureUrl(LocalDate localDate, LocalDate localDate1) {
		return "Temp?from="+localDate.toString()+"&to="+localDate1.toString()+"&asHtml=true";
	}

	public String getTemperatureRestUrl(LocalDate localDate, LocalDate localDate1) {
		return "Temp?from="+localDate.toString()+"&to="+localDate1.toString();
	}

	public String getNaviUrl() {
		return "Navigation";//null
	}

	public String getToLowerUrl() {
		return "ToLower";//null
	}
}
