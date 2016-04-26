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

package cs.dataexport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileCopy 
{
  public static void main(String[] args) 
  {
    try 
    {
      copy("fromFile.txt", "toFile.txt");
    } 
    catch (IOException e) 
    {
      System.err.println(e.getMessage());
    }
  }

  public static void copy(String fromFileName, String toFileName)
      throws IOException {
    File fromFile = new File(fromFileName);
    File toFile = new File(toFileName);
    if(fromFile.length()>0){

        if (!fromFile.exists())
            throw new IOException("FileCopy: " + "no such source file: "
                + fromFileName);
          if (!fromFile.isFile())
            throw new IOException("FileCopy: " + "can't copy directory: "
                + fromFileName);
          if (!fromFile.canRead())
            throw new IOException("FileCopy: " + "source file is unreadable: "
                + fromFileName);

          if (toFile.isDirectory())
            toFile = new File(toFile, fromFile.getName());

            {
            String parent = toFile.getParent();
            if (parent == null)
              parent = System.getProperty("user.dir");
            File dir = new File(parent);
            if (!dir.exists())
              throw new IOException("FileCopy: "
                  + "destination directory doesn't exist: " + parent);
            if (dir.isFile())
              throw new IOException("FileCopy: "
                  + "destination is not a directory: " + parent);
            if (!dir.canWrite())
              throw new IOException("FileCopy: "
                  + "destination directory is unwriteable: " + parent);
          }

          FileInputStream from = null;
          FileOutputStream to = null;
          try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1)
              to.write(buffer, 0, bytesRead); // write
          } finally {
            if (from != null)
              try {
                from.close();
              } catch (IOException e) {
                ;
              }
            if (to != null)
              try {
                to.close();
              } catch (IOException e) {
                ;
              }
          }
    }

  }
}