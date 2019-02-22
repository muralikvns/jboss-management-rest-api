package com.jboss.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public class RestCall {
	
	private static final Log LOG = LogFactory.getLog(RestCall.class);
	private static final String SERVER_UP="UP";
	private static final String SERVER_DOWN="DOWN";
	private static final String NOT_FOUND="NOT FOUND";
	private static final String SERVER_EXCEPTION="SERVER EXCEPTION";
	
	public List<EnvironmentBO> getEnvStatus(Map<String,UrlhostnameBO> envMap, Map<String,ArrayList<String>> appNameDataSourceMap,Map<String,ArrayList<String>> envAppMap)
	{
		//initialize();
		
		List<EnvironmentBO> environmentBOList=new ArrayList<EnvironmentBO>();
		EnvironmentBO environmentBO = null;
		
		for(Map.Entry<String, UrlhostnameBO> entry : envMap.entrySet())
		{	
			
		String envName=entry.getKey();
		
		environmentBO = new EnvironmentBO();
		//Server Running Rest Call 
		String envFlag=isUpAndRunning(envName,envMap); 
		//Get AppName Rest Call
	
		Map<String,String> appNameStatus=getApplicationNameStatus(envName,envMap,appNameDataSourceMap.keySet(),envAppMap);
		
		//LOG.info("******************");
		//LOG.info("appNameStatus::"+appNameStatus);
		//Get data Source 
		Map<String,String> dataSourcemap=getdataSourceNameStatus(envName,envMap,appNameDataSourceMap);
		//LOG.info("dataSourceList::"+dataSourceList);
		//Get Environment Name
		//String envName=getEnvironmentName();
		
		
		environmentBO.setEnvName(envName);
		environmentBO.setServerStatus(envFlag);
		environmentBO.setUrl(envMap.get(envName).getUrl());
		
		
		//iterating appname and status and setting to object
		List<Application> appname=new ArrayList<Application>();
		Application app=new Application();
		for (Map.Entry<String, String> entryApp : appNameStatus.entrySet()) 
		{
			app=new Application();		
			app.setAppName(entryApp.getKey());
			app.setAppStatus(entryApp.getValue());
			appname.add(app);
					
		}
		environmentBO.setAppname(appname);
		
		
		
		//iterating Data Source name and status and setting to object
		List<DataSource> dataSource=new ArrayList<DataSource>();
		DataSource datasrce=new DataSource();
		ArrayList<String> dlist=new ArrayList<String>();
		if(!dataSourcemap.isEmpty())
		{
		for (Map.Entry<String, String> datasourceentry : dataSourcemap.entrySet()) 
		{
				//LOG.info("datasourceentry Key : " + datasourceentry.getKey() + " datasourceentry Value : " + datasourceentry.getValue());
				for (Map.Entry<String, ArrayList<String>> appnamedatasourcename : appNameDataSourceMap.entrySet())
				{
					dlist=appnamedatasourcename.getValue();
					//LOG.info("dlist::"+dlist);
					//LOG.info("dlist.size:::"+dlist.size());
					for(int dl=0; dl<dlist.size();dl++)
					{
						//LOG.info("appnamedatasourcename Key : " + appnamedatasourcename.getKey() + "appnamedatasourcename Value : " + appnamedatasourcename.getValue());	
						//LOG.info("Data Source List::"+dlist.get(dl));
						//LOG.info("datasourceentry.getKey()::"+ datasourceentry.getKey());
						if (dlist.get(dl).equalsIgnoreCase(datasourceentry.getKey()))
						{
						datasrce=new DataSource();
						datasrce.setAppname(appnamedatasourcename.getKey());
						datasrce.setDataSourceName(datasourceentry.getKey());
						datasrce.setDataSourceStatus(datasourceentry.getValue());
							
						dataSource.add(datasrce);
						}
					
					}
				}
		}
		}
		
			environmentBO.setDataSource(dataSource);
			environmentBOList.add(environmentBO);
		}
		return environmentBOList;
		
		
	}

	private String isUpAndRunning(String envName,Map<String,UrlhostnameBO> envMap) 
	{
		//DEv Rest Call for Server Status
		LOG.info("isUpAndRunning - BEGIN");
		String serverStatusflag=SERVER_DOWN;
		
		if(envMap.containsKey(envName))
		{
			//make rest call with Url, username, password
			//restCall(envMap.get(envName).getUrl(),envMap.get(envName).getUserName(),envMap.get(envName).getPassword());
			LOG.info("Env Type::"+envName);
			LOG.info(envMap.get(envName).getUrl());
			//LOG.info(envMap.get(envName).getUserName());
			//LOG.info(envMap.get(envName).getPassword());
			
			Response response;
			try {
				WebTarget target = getClient(envMap.get(envName).getUserName(),envMap.get(envName).getPassword()).target(envMap.get(envName).getUrl()).
						queryParam("operation", "attribute").queryParam("name", "server-state").queryParam("json.pretty", "1");
				response = target.request(MediaType.APPLICATION_JSON).get();
				
				response.bufferEntity(); // to read response object multiple times

				if(response.getStatus()==200)
				{	
					
					LOG.info("server status success response::"+response.readEntity(String.class));
					if(null!=response && response.readEntity(String.class).contains("running"))
					{
						serverStatusflag=SERVER_UP;
						
					}
					else
					{
						serverStatusflag=SERVER_DOWN;
					}
					LOG.info("server status:::"+serverStatusflag);
				}
				else
				{
					serverStatusflag=NOT_FOUND;
					LOG.info("server status failed response::"+response.readEntity(String.class));
				}
				response.close();
				
			} catch (Exception e) {
				
				serverStatusflag=SERVER_EXCEPTION;
				LOG.error("exception in server status rest call::"+e.getMessage());
			}
			
		}
		else
		{
			serverStatusflag="Env Not Found";
		}
		
		LOG.info("isUpAndRunning - END");
		return serverStatusflag;
	}
	private Map<String,String> getApplicationNameStatus(String envName,Map<String,UrlhostnameBO> envMap,
			Set<String> applicationNames,
			Map<String,ArrayList<String>> envAppMap)
	{
		LOG.info("getApplicationNameStatus - BEGIN");
		Map<String,String> appStatusMap=new HashMap<String,String>();
		ArrayList<String> applicationNamesList=new ArrayList<String>();
		Response response = null;
		if(envMap.containsKey(envName))
		{
			LOG.info("Env Type::"+envName);
			
			applicationNamesList = envAppMap.get(envName);
			
			//for (String warName : applicationNames) 
				for (String warName : applicationNamesList) 
				{
					LOG.info("warName::"+warName);
					String value=SERVER_DOWN;
				    // 1 - rest Call to get app1 status and parse the response for value eg.value=Running
					try {
						WebTarget target = getClient(envMap.get(envName).getUserName(),envMap.get(envName).getPassword()).
								target(envMap.get(envName).getUrl()).path("deployment").
								path(warName).queryParam("operation", "attribute").queryParam("name", "status").queryParam("json.pretty", "1");
						
						response = target.request(MediaType.APPLICATION_JSON).get();
						
						response.bufferEntity(); // to read response object multiple times
						if(response.getStatus()==200)
						{	
							LOG.info("application status success response::"+response.readEntity(String.class));
            				if(null!=response && response.readEntity(String.class).contains("OK"))
							{
								value=SERVER_UP;
								
							}
							else
							{
								value=SERVER_DOWN;
							}
							LOG.info("application status:::"+value);
						}
						else
						{
							value=NOT_FOUND;
							LOG.info("application failed response::"+response.readEntity(String.class));
						}
						response.close();
					} catch (Exception e) {
						   value=SERVER_EXCEPTION;
						   LOG.error("exception in application status rest call::"+ e.getMessage() );
						   
					}
					appStatusMap.put(warName, value);
				}
		
		}
		LOG.info("getApplicationNameStatus - END");
		return appStatusMap;
	}
	
	
	private Map<String,String> getdataSourceNameStatus(String envName,Map<String,UrlhostnameBO> envMap,Map<String,ArrayList<String>> dataSourceMap)
	{
		LOG.info("getdataSourceNameStatus - BEGIN");
		ArrayList<String> datasourceNames = new ArrayList<String>();
		Map<String,String> dataSourceStatusMap=new HashMap<String,String>();
		Response response=null;
		if(!dataSourceMap.isEmpty()  &&  !dataSourceMap.values().isEmpty())
		{
			Iterator dataSourceListIterator = dataSourceMap.values().iterator();
		
			if(envMap.containsKey(envName))
			{
				LOG.info("Env Type::"+envName);
					while(dataSourceListIterator.hasNext())
					{
						datasourceNames=(ArrayList<String>) dataSourceListIterator.next();
						//LOG.info("Inner Map datasourceNames::"+datasourceNames);
						for (String dataSource : datasourceNames) 
						{
				
					    // 1 - rest Call to get datasource status and parse the response for value eg.value=Running
					    String value=SERVER_DOWN;
					    
					    try {
			                    //Non XA DataSource
								WebTarget target =getClient(envMap.get(envName).getUserName(),envMap.get(envName).getPassword()).target(envMap.get(envName).getUrl())
										.path("subsystem").path("datasources").path("data-source")
										.path(dataSource)
										.queryParam("operation", "attribute").queryParam("name", "enabled");
								
								//XA DataSource
								/*WebTarget target =getClient(envMap.get(envName).getUserName(),envMap.get(envName).getPassword()).target(envMap.get(envName).getUrl())
										.path("subsystem").path("datasources").path("xa-data-source")
										.path(dataSource)
										.queryParam("operation", "attribute").queryParam("name", "enabled");*/
								
								response = target.request(MediaType.APPLICATION_JSON).get();
								response.bufferEntity(); // to read response object multiple times

								if(response.getStatus()==200)
								{	
									
									LOG.info("datasource satus success response::"+response.readEntity(String.class));
									if(null!=response && "true".equalsIgnoreCase(response.readEntity(String.class)) )
									{
										value=SERVER_UP;
										
									}
									else
									{
										value=SERVER_DOWN;
									}
									LOG.info("datasource status:::"+value);
									
								}
								else
								{
									LOG.info("datasource failed response::"+response.readEntity(String.class));
									value=NOT_FOUND;
								}
								response.close();
								
						} catch (Exception e) 
					    	{
							 
							 value=SERVER_EXCEPTION;
							 LOG.error("exception in datasource status rest call::"+e.getMessage());
					    	}
			
						//return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("true");
					    
					    dataSourceStatusMap.put(dataSource, value);
						}//end of For
				}//end of While
			}
		}
		LOG.info("getdataSourceNameStatus - END");
		return dataSourceStatusMap;
	}
	
	private static ResteasyClient getClient(String userName,String password) {
		// Setting digest credentials
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
		credentialsProvider.setCredentials(AuthScope.ANY, credentials);
		HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, true);

		// Creating HTTP client
		return new ResteasyClientBuilder().httpEngine(engine).build();
	}
	

}
