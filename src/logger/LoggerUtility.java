package logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtility {
	private static final String HTML_CONFIG = "src/logger/log4j-html.properties";

	public static Logger getLogger(Class<?> logClass) {
		PropertyConfigurator.configure(HTML_CONFIG);
		String name = logClass.getName();
		return Logger.getLogger(name);
	}
}
