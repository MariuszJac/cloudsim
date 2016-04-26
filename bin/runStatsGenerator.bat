set rootDir=./

set operatingPlatform=windows
set libPath=.;..\classes;..\lib\jcommon-1.0.0.jar;..\lib\log4j-1.2.15.jar;..\logs;..\lib\gson-1.7.1.jar;..\lib\simplatform-0.0.4-SNAPSHOT.jar;..\lib\RoSim-Core-0.1.0-SNAPSHOT.jar;..\lib\jcommon-1.0.0.jar;..\lib\jfreechart-1.0.0.jar;..\lib\collections-generic-4.01.jar;..\lib\colt-1.2.0.jar;..\lib\jung-algorithms-2.0.1.jar;..\lib\jung-api-2.0.1.jar;..\lib\jung-graph-impl-2.0.1.jar;..\lib\jung-visualization-2.0.1.jar;..\lib\swing-layout-1.0.jar;
set resultsFolderPath=c:\\mj\\models\\ROBUST\\CatSim\\bin\\results\\
java -Xmx512m -cp "%libPath%" uk.ac.soton.itinnovation.robust.catsim.stats.TRTStatsAnalysis %resultsFolderPath%
