set rootDir=./

set operatingPlatform=windows
set libPath=.;..\lib\*;..\classes;
set simplatformConfigurationFilePath=c:\mj\models\ROBUST\CatSim\bin\configuration.txt

java -Xmx1024m -cp "%libPath%" uk.ac.soton.itinnovation.prestoprime.iplatform.batch.ModelRunner %operatingPlatform% %libPath% %simplatformConfigurationFilePath%
 






