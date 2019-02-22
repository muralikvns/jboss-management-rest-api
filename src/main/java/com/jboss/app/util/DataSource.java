package com.jboss.app.util;

public class DataSource {

	private String dataSourceName;
	private String dataSourceStatus;
	private String appname;
	public String getDataSourceName() {
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
	}
	/**
	 * @return the appname
	 */
	public String getAppname() {
		return appname;
	}
	/**
	 * @param appname the appname to set
	 */
	public void setAppname(String appname) {
		this.appname = appname;
	}
}
