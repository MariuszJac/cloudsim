#!/bin/sh

myroot='pwd'/..
rootDir=`pwd`
echo $rootDir
configsDir=$rootDir"/configs"
echo $configsDir
resultsDir=$rootDir"/results"
echo $resultsDir
operatingPlatform=linux
echo $operatingPlatform
libPath=../classes:../lib/*
echo $libPath
simplatformConfigurationFilePath=$rootDir"/configuration.txt"
echo $simplatformConfigurationFilePath
java -Xmx1024m -cp $libPath iplatform.batch.ModelRunner $operatingPlatform $libPath $simplatformConfigurationFilePath
 






