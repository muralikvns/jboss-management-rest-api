package com.jboss.app.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jboss.app.model.DataResponse;
import com.jboss.app.util.DataSourceNameStatus;
import com.jboss.app.util.EnvironmentBO;
import com.jboss.app.util.EnvironmentDetails;
import com.jboss.app.util.JbossProperties;
import com.jboss.app.util.RestCall;
import com.jboss.app.util.UrlhostnameBO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@RestController
@RequestMapping(value = "/jboss")
@Api(value = "Jboss Management Client Rest Api Resource", description = "Jboss All Enviroment Details")
public class JbossManagementClientRestController {
	
	private static final Log LOG = LogFactory.getLog(JbossManagementClientRestController.class);
    private UrlhostnameBO urlhostnameBO = new UrlhostnameBO();
	private Map<String,ArrayList<String>> appNameDataSourceMap=new HashMap<String,ArrayList<String>>();//application Vs DataSource List
	private Map<String,ArrayList<String>> envAppMap=new HashMap<String,ArrayList<String>>(); // Environment  Vs appname list 
	private Map<String,Map<String,String>> envAppUrlMap=new HashMap<String,Map<String,String>>(); // Environment Vs List of( appname Vs application Url )
    private Map<String,UrlhostnameBO> envDetailsMap=new HashMap<String,UrlhostnameBO>();
    private final JbossProperties  jbossProperties;
    private final String DATEFORMAT="MM/dd/yyyy hh:mm:ss a";
    
    
    public JbossManagementClientRestController(JbossProperties  jbossProperties)
    {
    	this.jbossProperties = jbossProperties;
    }

    
    
	
    @RequestMapping(value = "/hello", method = GET)
    @ApiOperation(value = "Returns Hello World")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful Hello World")
            }
    )
    public String message() {
		
		return "Hello World";
	}
	
    // To populate in hygieia devops deploy dashboard
	@RequestMapping(value = "/hygieia/jboss-server-details", method = GET, produces = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Returns Hygieia Specific - Jboss All Environment Details (Server/Application/DataSource) Status")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful All Environment Status Hygieia specific JSON object")
            }
    )
	public List<EnvironmentDetails> getJbossServerDetails() {

		RestCall restcall = new RestCall();
		initialize();
		List<EnvironmentBO> environmentBOList = restcall.getEnvStatus(envDetailsMap,appNameDataSourceMap,envAppMap);
		return getEnvironmentDetailsList(environmentBOList);

	}
	
    private List<EnvironmentDetails> getEnvironmentDetailsList(List<EnvironmentBO> environmentBOList){
		
		EnvironmentDetails environmentDetails;
		EnvironmentBO environmentBO;
		List<EnvironmentDetails> environmentDetailsList=new ArrayList<EnvironmentDetails>();
		List<DataSourceNameStatus> dataSourceList=new ArrayList<DataSourceNameStatus>();
		DataSourceNameStatus dataSourceNameStatus = new DataSourceNameStatus();
		for(int envList=0;envList<environmentBOList.size();envList++) 
		{
			
			environmentBO = environmentBOList.get(envList);
			
			for(int i=0;i<environmentBO.getAppname().size();i++) 
			{
				environmentDetails = new EnvironmentDetails();
				environmentDetails.setEnvType(environmentBO.getEnvName());
				environmentDetails.setServerStatus(environmentBO.getServerStatus());
				environmentDetails.setApplicationName(environmentBO.getAppname().get(i).getAppName());
				environmentDetails.setAppStatus(environmentBO.getAppname().get(i).getAppStatus());
				environmentDetails.setUrl(environmentBO.getUrl());
				
				dataSourceList=new ArrayList<DataSourceNameStatus>();
				if(!environmentBO.getDataSource().isEmpty())
				{
						for(int j=0;j<environmentBO.getDataSource().size();j++)
						{
							if(environmentBO.getAppname().get(i).getAppName().equalsIgnoreCase(environmentBO.getDataSource().get(j).getAppname()))
							{
								dataSourceNameStatus = new DataSourceNameStatus();
								dataSourceNameStatus.setDataSourceName(environmentBO.getDataSource().get(j).getDataSourceName());
								dataSourceNameStatus.setDataSourceStatus(environmentBO.getDataSource().get(j).getDataSourceStatus());		
								dataSourceList.add(dataSourceNameStatus);
							}
						}
						
				}
				//envAppUrlMap.get(environmentBO.getEnvName()).get(environmentBO.getAppname().get(i).getAppName());
				environmentDetails.setApplicationUrl(envAppUrlMap.get(environmentBO.getEnvName()).get(environmentBO.getAppname().get(i).getAppName()));
				
				environmentDetails.setDataSourceList(dataSourceList);
				String uniqueID = UUID.randomUUID().toString();
				environmentDetails.set_id(uniqueID);
				environmentDetailsList.add(environmentDetails);
			}
			
		}
		return environmentDetailsList;
		
	}
	
	// To populate in jboss-dashboard-html
	
    @RequestMapping(value = "/api/jboss-server-details", method = GET, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns Jboss All Environment Details (Server/Application/DataSource) Status")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successful All Environment Status JSON object")
            }
    )
    public DataResponse<List<EnvironmentDetails>> getAllJbossServerDetails() {

		RestCall restcall = new RestCall();
		initialize();
		List<EnvironmentBO> environmentBOList = restcall.getEnvStatus(envDetailsMap,appNameDataSourceMap,envAppMap);
		return getAllEnvironmentDetailsList(environmentBOList);

	}
	
    private DataResponse<List<EnvironmentDetails>> getAllEnvironmentDetailsList(List<EnvironmentBO> environmentBOList){
		
		EnvironmentDetails environmentDetails;
		EnvironmentBO environmentBO;
		List<EnvironmentDetails> environmentDetailsList=new ArrayList<EnvironmentDetails>();
		List<DataSourceNameStatus> dataSourceList=new ArrayList<DataSourceNameStatus>();
		DataSourceNameStatus dataSourceNameStatus = new DataSourceNameStatus();
		for(int envList=0;envList<environmentBOList.size();envList++) 
		{
			
			environmentBO = environmentBOList.get(envList);
			
			for(int i=0;i<environmentBO.getAppname().size();i++) 
			{
				environmentDetails = new EnvironmentDetails();
				environmentDetails.setEnvType(environmentBO.getEnvName());
				environmentDetails.setServerStatus(environmentBO.getServerStatus());
				environmentDetails.setApplicationName(environmentBO.getAppname().get(i).getAppName());
				environmentDetails.setAppStatus(environmentBO.getAppname().get(i).getAppStatus());
				environmentDetails.setUrl(environmentBO.getUrl());
				dataSourceList=new ArrayList<DataSourceNameStatus>();
				if(!environmentBO.getDataSource().isEmpty())
				{
						for(int j=0;j<environmentBO.getDataSource().size();j++)
						{
							if(environmentBO.getAppname().get(i).getAppName().equalsIgnoreCase(environmentBO.getDataSource().get(j).getAppname()))
							{
								dataSourceNameStatus = new DataSourceNameStatus();
								dataSourceNameStatus.setDataSourceName(environmentBO.getDataSource().get(j).getDataSourceName());
								dataSourceNameStatus.setDataSourceStatus(environmentBO.getDataSource().get(j).getDataSourceStatus());		
								dataSourceList.add(dataSourceNameStatus);
							}
						}
						
				}
				
				environmentDetails.setApplicationUrl(envAppUrlMap.get(environmentBO.getEnvName()).get(environmentBO.getAppname().get(i).getAppName()));
				environmentDetails.setDataSourceList(dataSourceList);
				String uniqueID = UUID.randomUUID().toString();
				environmentDetails.set_id(uniqueID);
				environmentDetailsList.add(environmentDetails);
			}
			
		}
		
		return new DataResponse<>(environmentDetailsList, dateToString());
		
		
	}
	
	private String dateToString() {
		
		Date date = new Date();
		DateFormat dateFormat=new SimpleDateFormat(DATEFORMAT);
		String strDate=dateFormat.format(date);
		return strDate;
	}
	
	protected void initialize()
	{
			
    	ArrayList<String> dataSourceList=null;
    	ArrayList<String> applist=null;
    	Map<String,String> appUrlMap=new HashMap<String,String>();
    	for(int i=0;i<jbossProperties.getEnvironment().size();i++)
    	{
    		urlhostnameBO = new UrlhostnameBO();
    		 
    		urlhostnameBO.setUrl(isNull(jbossProperties.getEnvironment().get(i).getUrl()));
    		urlhostnameBO.setUserName(isNull(jbossProperties.getEnvironment().get(i).getUserName()));
    		urlhostnameBO.setPassword(isNull(jbossProperties.getEnvironment().get(i).getPassword()));
    		envDetailsMap.put(isNull(jbossProperties.getEnvironment().get(i).getEnvType()), urlhostnameBO);
    			
    		// To map Application (WAR) to DataSources (if any)
    		applist=new ArrayList<String>();
    		appUrlMap=new HashMap<String,String>();
    		for(int j=0;j<jbossProperties.getEnvironment().get(i).getAppDataSourceList().size();j++)
    		{
    			dataSourceList=new ArrayList<String>();
    			
    			
    			
    			//LOG.info("application Name:::"+isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getAppName()));
    			applist.add(isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getAppName())); // adding all appnames to list 
    			
    			
    			appUrlMap.put(isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getAppName()), isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getAppUrl()));// creation map for Appname and URL
    			envAppUrlMap.put(isNull(jbossProperties.getEnvironment().get(i).getEnvType()), appUrlMap);  // creating map for Environment vs List of app name Vs Url
    			
    			
    			
    			if(null!=jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getDataSourceName())
    			{
    			for(int k=0;k<jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getDataSourceName().size();k++)
    			 {
    				String dataSrcname=isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getDataSourceName().get(k));
    				if(!("".equalsIgnoreCase(dataSrcname)))
    				{
    				dataSourceList.add(isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getDataSourceName().get(k)));
    				//LOG.info("data source Name:::"+isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getDataSourceName().get(k)));
    				}
    			 }
    			}
    			appNameDataSourceMap.put(isNull(jbossProperties.getEnvironment().get(i).getAppDataSourceList().get(j).getAppName()), dataSourceList);
    			
    			 
    		}
    		envAppMap.put(isNull(jbossProperties.getEnvironment().get(i).getEnvType()), applist); // Mapping Envtype with application names to distinguish app calls
    	}
		
	}
    
    private String isNull(String name)
    {
    	if(null!=name)
    	{
    		return name;
    	}
    	else
    		return "";
    }
	
	
}
