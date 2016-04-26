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
//#	Created By :			Mariusz Jacyno
//#	Created Date :			2011-08-05
//#	Created for Project :	ROBUST
//#
//########################################################################

package cs.gui.stats;

import iplatform.batch.ModelRunner;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cs.ModelInterface;

public class StatsManager {

	static Logger logger = Logger.getLogger(ModelRunner.class);

		double threadsNew = 0;
	double threadsResolved = 0;
	
	double totalThreadsNew = 0;
	double totalThreadsResolved = 0;

	ArrayList<Double> threadResolutionTimesArray = new ArrayList<Double>();

	double totalPosts = 0;
	ArrayList<Double> threadRepliesNumberArray = new ArrayList<Double>();

	private ModelInterface modelInterface = null;
	
	public StatsManager()
	{
	}
	
	//======================================================================================
	//created-resolved thread stats
	public void updateThreadsStats(double threadsNewRef, double threadsResolvedRef)
	{
		//update short stats
		threadsNew = threadsNew + threadsNewRef;
		threadsResolved = threadsResolved + threadsResolvedRef;

		//update short stats
		totalThreadsNew = totalThreadsNew + threadsNewRef;
		totalThreadsResolved = totalThreadsResolved + threadsResolvedRef;
	}

	public void incrementTotalPostsNumber()
	{
		totalPosts = totalPosts + 1;
	}
	
	public double getTotalPostsNumber()
	{
		return totalPosts;
	}
	
	public void resetThreadsStats()
	{
		//threadsNew = 0;
		//threadsResolved = 0;
	}

	//======================================================================================
	//thread resolution time stats
	public void addThreadResolutionTime(double timeToResolveThread)
	{
		//remember that we count simulation ticks where each tick corresponds to 10 minutes of simulation
		timeToResolveThread = timeToResolveThread * 10;
		threadResolutionTimesArray.add(timeToResolveThread);
	}
	
	public double getMeanThreadResolutionTime()
	{
		double sum = 0;
		
		for(int i=0;i<threadResolutionTimesArray.size();i++)
		{
			sum = sum + threadResolutionTimesArray.get(i);
		}
		
		if(threadResolutionTimesArray.size() == 0)
		{
			return 0;
		}
		else
		{
			//format to days
			sum = sum/(double)threadResolutionTimesArray.size()/(double)60/(double)24;
			return sum;
		}
	}

	public void resetThreadResolutionTimes()
	{
		threadResolutionTimesArray.clear();
	}

	public void setModelInterface(ModelInterface modelInterfaceRef)
	{
		this.modelInterface = modelInterfaceRef;
	}

	//======================================================================================
	//thread replies stats
	//thread resolution time stats
	public void addThreadReply(double numberOfReplies)
	{
		threadRepliesNumberArray.add(numberOfReplies);
	}
	
	public double getAvereageThreadRepliesNumber()
	{
		double sum = 0;
		
		for(int i=0;i<threadRepliesNumberArray.size();i++)
		{
			sum = sum + threadRepliesNumberArray.get(i);
		}
		
		if(threadRepliesNumberArray.size() == 0)
		{
			return 0;
		}
		else
		{
			//format to hours
			return sum;
		}
	}

	public void resetThreadRepliesNumber()
	{
		threadRepliesNumberArray.clear();
	}

	//====================================================================================
	public double getThreadsNew() {
		return threadsNew;
	}

	public double getThreadsResolved() {
		return threadsResolved;
	}

	public double getTotalThreadsNew() {
		return totalThreadsNew;
	}

	public double getTotalThreadsResolved() {
		return totalThreadsResolved;
	}
	
	//======================= demand supply management ====================
	//this is according to the original change percentage
	double currentCommunityThreadGenerationPercentage = 0;

	public double getNewThreadArrivalRateChangePercentage()
	{
		/*
		ArrayList<Double> threadGenerationIncreasePercentageValues = new ArrayList<Double>();
		ArrayList<Double> threadGenerationDecreasePercentageValues = new ArrayList<Double>();
		
		double activityChangePercentage = 0;

		for(int i=0;i<modelInterface.getUsers().size();i++)
		{
			CommunityUser user = modelInterface.getUsers().get(i);
			
			if(user.getCreatedThreadsOriginal() > user.getCreatedThreads() && user.getCreatedThreadsOriginal() > 0)
			{
				activityChangePercentage = 100 - user.getCreatedThreads()/user.getCreatedThreadsOriginal() * (double)100;
				//add to list of percentage decrease values
				threadGenerationDecreasePercentageValues.add(activityChangePercentage);
			}
			else if(user.getCreatedThreadsOriginal() > 0)
			{
				activityChangePercentage = (user.getCreatedThreads()-user.getCreatedThreadsOriginal())/user.getCreatedThreadsOriginal() * (double)100;
				//add to list of percentage decrease values
				threadGenerationIncreasePercentageValues.add(activityChangePercentage);
			}
		}

		double increasePercentage = getAverage(threadGenerationIncreasePercentageValues, modelInterface.getUsers().size());
		double decreasePercentage = getAverage(threadGenerationDecreasePercentageValues, modelInterface.getUsers().size());

		currentCommunityThreadGenerationPercentage = increasePercentage - decreasePercentage;

		//logger.info("-> new thread generation increase percentage: "+increasePercentage+" dec per: "+decreasePercentage+" "+currentCommunityThreadGenerationPercentage);
		*/
		return currentCommunityThreadGenerationPercentage;
	}
	
}
