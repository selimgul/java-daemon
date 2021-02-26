package service.config;

import java.util.ResourceBundle;

public class Config {
	private static final String DEFULT_BUNDLE_NAME = "application";

	public static String getProperty(String pKeyName) {
		return ResourceBundle.getBundle(DEFULT_BUNDLE_NAME).getString(pKeyName);
	}

	public static String getProperty(String pBundleName, String pKeyName) {
		return ResourceBundle.getBundle(pBundleName).getString(pKeyName);
	}

	public static <T> T getProperty(String pKeyName, Class<T> pValueType) throws Exception {
		return (T) pValueType.getConstructor(String.class)
				.newInstance(ResourceBundle.getBundle(DEFULT_BUNDLE_NAME).getString(pKeyName));
	}

	public static <T> T getProperty(String pBundleName, String pKeyName, Class<T> pValueType) throws Exception {
		return (T) pValueType.getConstructor(String.class)
				.newInstance(ResourceBundle.getBundle(pBundleName).getString(pKeyName));
	}
}
