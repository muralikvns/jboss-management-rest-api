
This project displays environment-dashboard for all environments by invoking inbuilt-jboss management api
java,spring-boot,maven,angular 1.x

Step1:key-in values in application.yaml based on your application name/env-details/data-source/ status for all the environments

Step2:run the project => mvn clean install

Step3:Start the application
java -jar target\jboss-management-client-rest-api.jar --spring.config.name=jboss-management-client-rest-api --spring.config.location=..\jboss-management-client-rest-api\application.yaml

Step4:http://localhost:8088/jbossDashBoard.html

To view the swagger 
http://localhost:8088/swagger-ui.html



############################################################
Action Items to be worked upon
update to java 8 standards,
provide more ds statistics by consuming the ds-statistics api  instead of basic status like (up or down)

#############################################################
Appendix  - Basic jboss provided management apis

server status
http://localhost:9990/management?operation=attribute&name=server-state
http://localhost:9990/management?operation=attribute&name=server-state&json.pretty=1

server-version
http://localhost:9990/management?operation=attribute&name=product-version

deployment status
http://localhost:9990/management/deployment/WAR1.war?operation=attribute&name=status
http://localhost:9990/management/deployment/WAR2.war?operation=attribute&name=status

datasource status
http://localhost:9990/management/subsystem/datasources/data-source/DS1?operation=attribute&name=enabled
http://localhost:9990/management/subsystem/datasources/data-source/DS2?operation=attribute&name=enabled

complete datasource statistics
http://localhost:9990/management/subsystem/datasources/data-source/DS1/statistics?read-resource&include-runtime=true&recursive&json.pretty

ds connection url
http://localhost:9990/management/subsystem/datasources/data-source/DS?operation=attribute&name=connection-url&json.pretty=1

