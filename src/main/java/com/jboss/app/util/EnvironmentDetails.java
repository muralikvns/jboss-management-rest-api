package com.jboss.app.util;

import java.util.List;

public class EnvironmentDetails {
	
	private String _id;
	private String envType;
	private String serverStatus;
	private String applicationName;
	private String applicationUrl;
	private String appStatus;
	private List<DataSourceNameStatus> dataSourceList;
	/*private String dataSourceName;
	private String dataSourceStatus;*/
	private String url;
	
	public String getEnvType() {
		return envType;
	}
	public void setEnvType(String envType) {
		this.envType = envType;
	}
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	/*public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSourceStatus() {
		return dataSourceStatus;
	}
	public void setDataSourceStatus(String dataSourceStatus) {
		this.dataSourceStatus = dataSourceStatus;
	}*/
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public List<DataSourceNameStatus> getDataSourceList() {
		return dataSourceList;
	}
	public void setDataSourceList(List<DataSourceNameStatus> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}
	public String getApplicationUrl() {
		return applicationUrl;
	}
	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}
	
}
