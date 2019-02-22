package com.jboss.app.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "jboss")
public class JbossProperties {
	
	private String dockerLocalHostIP; //null if not running in docker on http://localhost
	private List<Environment> environment = new ArrayList<Environment>();
	 
	public static class Environment{
	
	 private String envType;
	 private String url;
	 private String userName;
	 private String password;
	 private List<ApplicationDataSource> appDataSourceList;
	 
	 
	public String getEnvType() {
		return envType;
	}
	public void setEnvType(String envType) {
		this.envType = envType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<ApplicationDataSource> getAppDataSourceList() {
		return appDataSourceList;
	}
	public void setAppDataSourceList(List<ApplicationDataSource> appDataSourceList) {
		this.appDataSourceList = appDataSourceList;
	}
	 
	}
	
	public static class ApplicationDataSource{
		private String appName;
		private String appUrl;
		 private List<String> dataSourceName;
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public List<String> getDataSourceName() {
			return dataSourceName;
		}
		public void setDataSourceName(List<String> dataSourceName) {
			this.dataSourceName = dataSourceName;
		}
		public String getAppUrl() {
			return appUrl;
		}
		public void setAppUrl(String appUrl) {
			this.appUrl = appUrl;
		}
	}

	public List<Environment> getEnvironment() {
		return environment;
	}

	public void setEnvironment(List<Environment> environment) {
		this.environment = environment;
	}

	public void setDockerLocalHostIP(String dockerLocalHostIP) {
        this.dockerLocalHostIP = dockerLocalHostIP;
    }

	//Docker NATs the real host localhost to 10.0.2.2 when running in docker
	//as localhost is stored in the JSON payload from jboss-management-client we need
	//this hack to fix the addresses
    public String getDockerLocalHostIP() {
    	
    		//we have to do this as spring will return NULL if the value is not set vs and empty string
    	String localHostOverride = "";
    	if (dockerLocalHostIP != null) {
    		localHostOverride = dockerLocalHostIP;
    	}
        return localHostOverride;
    }
}
