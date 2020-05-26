// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 1 

//This class is responsible for the saving and 
//loading of a game. 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ActionListen implements ActionListener 
{
  private Board gameBoard;
  
  public ActionListen(Board gameBoard)
  {
   this.gameBoard = gameBoard;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) 
  /* Checks if the user selected "Load" or 
   * "Save" and performs the requested action. 
   */
  {
   JFrame frame = new JFrame();
   JFileChooser f1 = new JFileChooser();

   String command = e.getActionCommand();
   if (command.equals("Save"))
   {
    GameWindow.saved = save(f1,frame);
   }
	
   if(command.equals("Load")) 
   {
	load(f1,frame);
   }	   
  }
  
  public boolean load(JFileChooser f1, JFrame frame)
  {
   f1.setDialogTitle("Load file");
   boolean loaded = false;
   int userSelection = f1.showOpenDialog(frame);
   if (userSelection == f1.APPROVE_OPTION)
   {
	File fileToLoad = f1.getSelectedFile();
	if (!fileToLoad.exists())
	{
	 JOptionPane.showMessageDialog(null, "File not found." 
             + " Please try again", "File Loading Error", 
             JOptionPane.ERROR_MESSAGE);
	 load(f1,frame); //opens up file directory again
	}
	else
	{
	 String filePath = fileToLoad.getAbsolutePath();
	 gameBoard.inputFile = filePath;
	 try
	 {
	  gameBoard.readFile(); 
	  loaded = true;
	 }
	 catch (FileNotFoundException e)
	 {
	  int choice = JOptionPane.showConfirmDialog(null, gameBoard.inputFile + " was not valid or couldn't"
      + " be found.\nPress 'OK' to Select a different file.", "Alert",
      JOptionPane.WARNING_MESSAGE);
      if (choice == 0)
      {
       load(f1,frame);
	  }
	  else
	  {
	   System.exit(0);
	  }
	 }
	 catch (IOException e)
	 {
	  int choice = JOptionPane.showConfirmDialog(null, gameBoard.inputFile +" was not valid or couldn't"
	  + "be opened.\nPress 'OK' to Select a different file.", "Alert",
	  JOptionPane.WARNING_MESSAGE);
	  if (choice == 0)
	  {
	   load(f1,frame);
	  }
	  else
	  {
	   System.exit(0);
	  }
	 }
	}
   }
   return loaded;
  }

  public boolean save(JFileChooser f1, JFrame frame)
  /* Returns true if the file was correctly saved. */
  {
   boolean saved = false;
   f1.setDialogTitle("Save file");
   int userSelection = f1.showSaveDialog(frame);
   if (userSelection == f1.APPROVE_OPTION)
   {
	File fileToSave = f1.getSelectedFile();
	if (fileToSave.exists())
	{
	 String[] array = {"yes","no"};
	 int choice = JOptionPane.showOptionDialog(null, "This file will be overriden."
		+ "\nWould you like to continue?", "File already exists",
		JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
		null, array, array[0]);
	 if (choice == 1) //user selects "no"
	 {
	  save(f1,frame); //opens up file directory again
	 }
	}
	String filePath = fileToSave.getAbsolutePath();
	try 
	{
	 WriteFile fileWriter = new WriteFile(filePath, gameBoard);
	 fileWriter.write();
	 saved = true;
    }
	catch (IOException e)
	{
	 JOptionPane.showMessageDialog(null, "Error trying to save file."
	 	+ "\nExiting.",
	 	"Save Error", JOptionPane.ERROR_MESSAGE);
	 e.printStackTrace();
    }  
   }
   return saved;
  }
}