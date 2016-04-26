//########################################################################
//#
//# � University of Southampton IT Innovation Centre, 2011 
//# Copyright in this library belongs to the University of Southampton 
//# University Road, Highfield, Southampton, UK, SO17 1BJ 
//# This software may not be used, sold, licensed, transferred, copied 
//# or reproduced in whole or in part in any manner or form or in or 
//# on any media by any person other than in accordance with the terms 
//# of the Licence Agreement supplied with the software, or otherwise 
//# without the prior written consent of the copyright owners.
//#
//# This software is distributed WITHOUT ANY WARRANTY, without even the 
//# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, 
//# except where stated in the Licence Agreement supplied with the software.
//#
//#	Created By :	 Mariusz Jacyno
//#	Created Date :	 2011-08-05
//#	Created for Project :	PrestoPRIME
//# Modifications for projects: ROBUST
//# Contact IT Innovation for details of individual project contributions
//#
//########################################################################

package iplatform.model.config;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationManager;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.SimulationModelSchema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

//this class will read json configuration file 
public class ModelConfigurationManager {

	static Logger logger = Logger.getLogger(ModelRunner.class);

	public static ModelConfigurationManager manager = null;
	public ToolConfigurationTemplate platformConfiguration = null;
	
	public static ModelConfigurationManager getManager(String simplatformConfigurationFilePath)
	{
		if(manager == null)
		{
			manager = new ModelConfigurationManager(simplatformConfigurationFilePath);
			return manager;
		}
		else
		{
			return manager;
		}
	}
	
	public ModelConfigurationManager(String simplatformConfigurationFilePath)
	{
		//now recreate the output into an object
		platformConfiguration = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);	
		//buildDefaultModelConfiguration("defaultConfigs/configurationTemplate-1.txt");
	}
	
	public Object getModelConfigurationFromFile(String file)
	{
		Object modelTemplateObjectRecreated = null;
		Class<? extends ModelConfigurationTemplate> theClass = null;
		
		try
		{
			String readJsonFileContent = readJsonFile(file);
			//logger.info(readJsonFileContent);
			//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			String theType = platformConfiguration.getModelConfigurationClassToInstantiate();
			theClass = Class.forName(theType).asSubclass(ModelConfigurationTemplate.class);
			modelTemplateObjectRecreated = gson.fromJson(readJsonFileContent, theClass);
			//Class<? extends ModelConfigurationTemplate> modelTemplateRecreated = gson.fromJson(readJsonFileContent, theClass);
			
			ModelConfigurationTemplate modelConfigurationTemplateRecreated = (ModelConfigurationTemplate)modelTemplateObjectRecreated; 
			logger.info("model configuration template recreated from text configuration file, template name: "+modelConfigurationTemplateRecreated.getTemplateName());
		}
		catch(Exception exc)
		{
			logger.error("error: ",exc);
		}
		
		return modelTemplateObjectRecreated;
	}
	
	public void buildDefaultModelConfiguration(String fileName)
	{
		String theType = platformConfiguration.getModelConfigurationClassToInstantiate();
		Class<? extends ModelConfigurationTemplate> theClass = null;

		try
		{
			theClass = Class.forName(theType).asSubclass(ModelConfigurationTemplate.class);
			//store it into a file
			ModelConfigurationTemplate modelConfigurationTemplateObject = theClass.newInstance();
			//ModelManager obj = theClass.cast(simulationModelObject);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonOutput = gson.toJson(modelConfigurationTemplateObject);
			
			//logger.info("json configuration: "+jsonOutput);
			try
			{
				writeJsonStream(jsonOutput, fileName);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception exc)
		{
			logger.error("error: ",exc);
		}
	}
	
 	public void writeJsonStream(String output, String fileName) throws IOException 
	{
		String teamName = "";
		
		try
		{
			BufferedWriter bw = getWriter(fileName);
			String lineToWrite = "";
			bw.write( output+"\n");
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

		//capture in each time snapshot which services are offered by which providers
		public String readJsonFile(String fileName)
		{
			String fileLine="";
			String content = "";
			try{
				BufferedReader bw = getReader(fileName);
				while ((fileLine = bw.readLine()) != null)
				{
					content = content + fileLine;
				}

				bw.close();
			}
			catch(Exception e)
			{
				e.printStackTrace(); 
			}
			
			return content;
		}

	  public BufferedWriter getWriter(String fileLocation)
	  {
		  BufferedWriter bw = null;
          try 
          {
              bw=new BufferedWriter(new FileWriter(fileLocation));
          } 
          catch (IOException e) 
          {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          }
          
          return bw;
	    }

		public BufferedReader getReader(String fileLocation)
		{
			BufferedReader br = null;
            try 
            {
            	FileInputStream fis = new FileInputStream(new File(fileLocation));
            	InputStreamReader inputstreamreader = new InputStreamReader(fis);
            	br = new BufferedReader(inputstreamreader);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return br;
	    }
}
