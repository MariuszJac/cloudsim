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

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import cs.bootstrap.BootstrapManager;

/**
 * 
 * @author ve
 */
public class DateUtil
{
	static Logger logger = Logger.getLogger(BootstrapManager.class);

    /**
     * The date string should be in one of the following formats:
     *    YYYY-MM-DD
     *    YYYY/MM/DD
     *    DD-MM-YYYY
     *    DD/MM/YYYY
     * 
     * No American nonsense please ;-)
     * @param date
     * @throws Exception if the date string is not in a valid format
     * @return 
     */
    public static Long getEpoch (String date) throws Exception
    {
        String[] strArray = date.split("[/-]");
        Calendar calendar = Calendar.getInstance();
        
        if (!isDateStringValid(strArray, calendar))
            throw new RuntimeException("Date string not valid, please refer to the documentation");

        return calendar.getTimeInMillis();
    }
    
    /**
     * The date string should be in one of the following formats:
     *    YYYY-MM-DD
     *    YYYY/MM/DD
     *    DD-MM-YYYY
     *    DD/MM/YYYY
     * 
     * No American nonsense please ;-)
     * @param date
     * @throws Exception if the date string is not in a valid format
     * @return 
     */
    public static Date getDateObject (String date) throws Exception
    {
        return new Date (getEpoch(date));
    }
    
    /**
     * Returns a string in the format: YYYY-MM-DD
     * @param date A Date object specifying a date.
     * @return a string in the format: YYYY-MM-DD
     */
    public String getDateString(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        
        return getDateString(cal);
    }
    
    /**
     * Returns a string in the format: YYYY-MM-DD
     * @param cal A Calendar object specifying a date.
     * @return a string in the format: YYYY-MM-DD
     */
    public static String getDateString(Calendar cal)
    {
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        
        return sdf.format(cal.getTime());
    }
    
    /**
     * Validates a date String, which should be in one of the following formats:
     *    YYYY-MM-DD
     *    YYYY/MM/DD
     *    DD-MM-YYYY
     *    DD/MM/YYYY
     * 
     * No American nonsense please ;-)
     * @param date
     * @throws Exception if the date string is not in a valid format
     * @return 
     */
    public static boolean isDateStringValid (String date) throws Exception
    {
        String[] strArray = date.split("[/-]");
        Calendar calendar = Calendar.getInstance();
        return isDateStringValid(strArray, calendar);
    }
    
    private static boolean isDateStringValid (String[] strArray, Calendar calendar) throws Exception
    {
        if (strArray == null)
            return false;
        if (strArray.length != 3)
            return false;
        if (strArray[0].length() == 2) // first is day
        {
            if ((strArray[1].length() != 2) || (strArray[2].length() != 4))
                return false;
            calendar.set(Integer.parseInt(strArray[2]), Integer.parseInt(strArray[1])-1, Integer.parseInt(strArray[0]));
        }
        else if (strArray[0].length() == 4) // first is year
        {
            if ((strArray[1].length() != 2) || (strArray[2].length() != 2))
                return false;
            calendar.set(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1])-1, Integer.parseInt(strArray[2]));
        }
        else
            return false;
        
        return true;
    }

    public static boolean isFirstDateOlder(Date d1, Date d2) throws Exception
    {
		//logger.info("----> "+(d1.getTime() - d2.getTime()));

    	if(d1.getTime() - d2.getTime() > 0)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    public static int getNumDaysBetweenDates(Date d1, Date d2) throws Exception
    {
        return (int)( Math.abs(d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
