// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 2 

//This class reads a file
//that is made up of bytes
//and stores it in an array.
//It also converts subarrays
//of bytes into integers and
//floats depending on the
//position. 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile extends FileInputStream 
{
  private static byte[] byteArray;
  private static int val;
  public static boolean correctFormat = true;
  
  public ReadFile(String filename) throws FileNotFoundException
  {
   super(filename);
   File file = new File(filename);
   byteArray = new byte[(int)file.length()];
  }
  
  public int readFile() throws IOException
  /* Reads in the file. If the end of the
   * file is reached, then it returns -1.
   */
  {
   val = read(byteArray);
   close();
   return val;
  }
  
  public int readInt(int position)
  /* Takes a position in the byte
   * array (divided by four) and 
   * creates a sub-array to convert
   * to an integer. 
   */
  {
   int i = -1;
   int j = 0;
   int arrayPosition = 4*position;
   byte[] subArray = new byte[4];
   
   if (val != -1) //if end of file is not reached
   {
	for (int k = arrayPosition; k < arrayPosition+4; k++)
	{
	 subArray[j] = byteArray[k];
	 j++;
	}
	i = ConverterClass.convertToInt(subArray);
   }
   return i;
  }
  
  public float readFloat(int position)
  /* Takes a position in the byte
   * array (divided by four) and 
   * creates a sub-array to convert
   * to a float. 
   */
  {
   float f = -1;
   int j = 0;
   int arrayPosition = 4*position;
   byte[] subArray = new byte[4];
   if (val != -1) //if end of file is not reached
   {
	for (int i = arrayPosition; i < arrayPosition+4; i++)
	{
	 subArray[j] = byteArray[i];
	 j++;
	}
	f = ConverterClass.convertToFloat(subArray);
   }
   return f;
  }
  
  public long readLong(int position)
  {
   long l = -1;
   int j = 0;
   int arrayPosition = 4*position;
   byte[] subArray = new byte[8];
   if (val != -1)
   {
	for (int i = arrayPosition; i <arrayPosition+8; i++)
	{
     subArray[j] = byteArray[i];
     j++;
	}
	l = ConverterClass.convertToLong(subArray);
   }
   return l;
  }
  
  public static boolean isPlayed()
  /* Given first four bytes of file, determines
   * if the game is a new game or a previous
   * played game. 
   */
  {
   boolean played = false;
   if (val != -1)
   {
	if(Integer.toHexString(byteArray[0]).endsWith("ca") 
	&& Integer.toHexString(byteArray[1]).endsWith("fe") 
	&& Integer.toHexString(byteArray[2]).endsWith("be") 
	&& Integer.toHexString(byteArray[3]).endsWith("ef") )
	{
     correctFormat = true;
	}
	else if (Integer.toHexString(byteArray[0]).endsWith("ca") 
		  && Integer.toHexString(byteArray[1]).endsWith("fe") 
		  && Integer.toHexString(byteArray[2]).endsWith("de") 
		  && Integer.toHexString(byteArray[3]).endsWith("ed") )
	{
	 correctFormat = true;
	 played = true;
	}
	else 
	{
	 correctFormat = false;
	}
   }
   return played;
  }
  
  public void setCFormat() 
  {
   correctFormat = true;
  }
  
  public static boolean isCorrectFormat() 
  {
   if(!correctFormat) 
   {
	return false;
   }
   return true;
  }
}