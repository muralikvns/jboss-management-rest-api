package com.jboss.app.util;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentBO {
	
  
    private String envName;
    private String serverStatus;
    private List<Application> appname = new ArrayList<Application>();
    private List<DataSource> dataSource = new ArrayList<DataSource>();
    private String url;
    
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
	public List<Application> getAppname() {
		return appname;
	}
	public void setAppname(List<Application> appname) {
		this.appname = appname;
	}
	public List<DataSource> getDataSource() {
		return dataSource;
	}
	public void setDataSource(List<DataSource> dataSource) {
		this.dataSource = dataSource;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
   
    
   
	
}
