set rootDir=./

set operatingPlatform=windows
set libPath=classes;lib\collections-generic-4.01.jar;lib\colt-1.2.0.jar;lib\gson-1.7.1.jar;lib\jcommon-1.0.0.jar;lib\jfreechart-1.0.0.jar;lib\jung-algorithms-2.0.1.jar;lib\jung-api-2.0.1.jar;lib\jung-graph-impl-2.0.1.jar;lib\jung-visualization-2.0.1.jar;lib\log4j-1.2.15.jar;lib\RoSim-0.0.1-SNAPSHOT.jar;lib\rsf-AgentCore-0.0.2-SNAPSHOT.jar;lib\Simplatform-0.0.3-SNAPSHOT.jar;lib\swing-layout-1.0.jar; 
set simplatformConfigurationFilePath=c:\mj\models\ROBUST\CatSim\bin\configuration.txt

java -Xmx1024m -cp "%libPath%" uk.ac.soton.itinnovation.robust.catsim.service.TestService





