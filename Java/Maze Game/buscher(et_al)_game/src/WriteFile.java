// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 1 

//This class writes the tile information
//from Board and converts it into byte arrays
//so that the file can be saved. 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class WriteFile
{
  private FileOutputStream fileWriter;
  private File file;
  private Board gameBoard;
  
  public WriteFile(String filename, Board gameBoard) throws IOException 
  {
   file = new File(filename);
   fileWriter = new FileOutputStream(file);
   this.gameBoard = gameBoard;
  }
  
  public void write() throws IOException
  {
   //Write hexadecimal that determines if the game was played or not
   if (!gameBoard.isModified())
   {
	JOptionPane.showMessageDialog(null, "No changes made", 
				"Save Error", JOptionPane.ERROR_MESSAGE);
   }
   else
   {
	byte[] bytes = ConverterClass.convertHexStringToByte("cafedeed");
	fileWriter.write(bytes);   
    //Write number of tiles
	writeInt(Board.numTiles);
	//Write number of seconds played
	writeLong(Timer.timeElapsed());
    int tileNum;
    int numLines;
    float tileCoord;
    gameBoard.fillOrderedArray();
    //Loop through ordered array of tiles
    for (int i = 0; i < 16; i++)
    {
	 Piece piece = gameBoard.OrderedArray[i];
	 tileNum = gameBoard.getBoardPosition(piece);
	 writeInt(tileNum);
     writeInt(getRotation(piece));
     numLines = piece.getNumLines();
     writeInt(numLines);
     //Get coordinates for each line on the tile
     for (int j = 0; j < numLines; j++)
     {
      //get x & y values from start & end of line
      for (int k = 0; k < 4; k++)
      {
       tileCoord = piece.getTileCoord(j,k);
       bytes = ConverterClass.convertFloatToByte(tileCoord);
       fileWriter.write(bytes);
      }
     }
    }
   }
  }
  
  public void writeInt(int num) throws IOException
  {
   byte[] bytes = ConverterClass.convertIntToByte(num);
   fileWriter.write(bytes);
  }
  
  public void writeFloat(float num) throws IOException
  {
   byte[] bytes = ConverterClass.convertFloatToByte(num);
   fileWriter.write(bytes);
  }
  
  public void writeLong(long num) throws IOException
  {
   byte[] bytes = ConverterClass.convertLongToByte(num);
   fileWriter.write(bytes);
  }
  
  public int getRotation(Piece piece)
  {
   if (piece.getOrientation().equals("right"))
   {
	return 1;
   }
   else if (piece.getOrientation().equals("down"))
   {
	return 2;
   }
   else if (piece.getOrientation().equals("left"))
   {
	return 3;
   }
   else
   {
	return 0;
   }
  }
  
}
