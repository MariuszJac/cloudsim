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

package iplatform.config;

import iplatform.batch.ModelRunner;
import iplatform.model.config.ModelConfigurationTemplate;

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
public class ToolConfigurationManager {

	static Logger logger = Logger.getLogger(ModelRunner.class);

	public static ToolConfigurationManager manager = null;
	
	public static ToolConfigurationManager getManager()
	{
		if(manager == null)
		{
			manager = new ToolConfigurationManager();
			return manager;
		}
		else
		{
			return manager;
		}
	}
	
	public ToolConfigurationManager()
	{
		//buildDefaultToolConfiguration("defaultConfigs/configuration.txt");
		//System.exit(0);
	}
	
	public void buildDefaultToolConfiguration(String fileName)
	{
		//create default instance of configuration file
		ToolConfigurationTemplate modelTemplate = new ToolConfigurationTemplate();
		//store it into a file
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		String jsonOutput = gson.toJson(modelTemplate);
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

	public void generateToolConfigurationFile(String fileName,ToolConfigurationTemplate modelTemplate)
	{
		//store it into a file
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		String jsonOutput = gson.toJson(modelTemplate);
		//logger.info("json configuration: "+jsonOutput);
		
		String currentDir = new File(".").getAbsolutePath();
		logger.info("attempting to generate simplatform configuration file with name: "+fileName+ " at location (working dir): "+currentDir+" content: "+jsonOutput);
		
		try
		{
			writeJsonStream(jsonOutput, fileName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void generateModelConfigurationFile(String fileName, ModelConfigurationTemplate modelTemplate)
	{
		//store it into a file
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		String jsonOutput = gson.toJson(modelTemplate);
		//logger.info("json configuration: "+jsonOutput);
		
		String currentDir = new File(".").getAbsolutePath();
		logger.info("attempting to generate model configuration file with name: "+fileName+ " at location (working dir): "+currentDir+" content: "+jsonOutput);

		try
		{
			writeJsonStream(jsonOutput, fileName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

    public ToolConfigurationTemplate readInitialToolConfiguration(String fileName)
    {
		String readJsonFileContent = readJsonFile(fileName);
		logger.info(readJsonFileContent);
		
		//now recreate the output into an object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		ToolConfigurationTemplate modelConfiguration = gson.fromJson(readJsonFileContent, ToolConfigurationTemplate.class);
		logger.info("read model configuration: operation mode: "+modelConfiguration.getOperationMode()+" heap size: "+modelConfiguration.getHeapSize());
		
		return modelConfiguration;
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
			
			String currentDir = new File(".").getAbsolutePath();
			//System.out.println("file writtent to location: "+currentDir+" file name: "+fileName+" ouput: "+output);
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
            {	fileLocation = fileLocation.trim();
            	logger.info("reading file from location: "+fileLocation);
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
