package com.ushareit.lucene.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static String connectionUrl;
	private static String connectionUsername;
	private static String connectionPassword;
	private static String index0Path;
	private static String index1Path;
	private static int searchMax;
	private static int pageSize;
	
	private static Properties properties;
    private static  String docsPath ;

    static{
		properties = new Properties();
		InputStream in = Config.class.getResourceAsStream("/config.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (properties.containsKey("connection.url")) {
			connectionUrl = properties.getProperty("connection.url");
		}
		if (properties.containsKey("connection.username")) {
			connectionUsername = properties.getProperty("connection.username");
		}
		if (properties.containsKey("connection.password")) {
			connectionPassword = properties.getProperty("connection.password");
		}
		if (properties.containsKey("index0.path")) {
			index0Path = properties.getProperty("index0.path");
		}
		if (properties.containsKey("index1.path")) {
			index1Path = properties.getProperty("index1.path");
		}
        if (properties.containsKey("docs.path")) {
            docsPath = properties.getProperty("docs.path");
        }
		if (properties.containsKey("search.max")) {
			String max = properties.getProperty("search.max");
			searchMax = Integer.parseInt(max);
		}
		if (properties.containsKey("search.pagesize")) {
			String size = properties.getProperty("search.pagesize");
			pageSize = Integer.parseInt(size);
		}
	}
	public static String getConnectionUrl() {
		return connectionUrl;
	}
	public static String getConnectionUsername() {
		return connectionUsername;
	}
	public static String getConnectionPassword() {
		return connectionPassword;
	}
	public static String getIndex0Path() {
		return index0Path;
	}
	public static String getIndex1Path() {
		return index1Path;
	}
	public static String getDocsPath(){return docsPath;}
	public static int getSearchMax() {
		return searchMax;
	}
	public static int getPageSize() {
		return pageSize;
	}
	
}
