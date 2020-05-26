// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 5

//This class creates the window that
//the game operates from.
//It creates the actual game board
//and handles all the layouts. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class GameWindow extends JFrame implements ActionListener
{
  private JPanel window;
  private GridBagConstraints basic;
  private static int scale;
  private int windowWidth;
  private int windowHeight;
  private Board gameBoard;
  public static boolean saved;
  public static final long serialVersionUID=1;
  //This makes the starting point easier to change.
  private int startAt=1;
  public static Timer time = new Timer();
  
  public GameWindow(String s)
  /* Constructor sets the window name using super(), 
   * changes the layout, and sets the window size. 
   *
   * @param s
   */
  {
   super(s);
   window = new JPanel();
   GridBagLayout gbl = new GridBagLayout();
   setContentPane(window);
   window.setBorder(new EmptyBorder(10, 10, 10, 10));

   window.setLayout(gbl);
   scale = 1;
   windowWidth = getSize().width;
   windowHeight = getSize().height;
   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void actionPerformed(ActionEvent e) 
  /* Checks if a button has been pushed and then,
   * depending on the button, performs the
   * requested task (i.e. File, Reset, and Quit).
   */
  {
   if("Quit".equals(e.getActionCommand()))
   {
	/* Checks if the user moved or rotated
	 * pieces without saving before exiting.
	 */
	if (MseListen.modified && !saved) 
	{
     int choice = JOptionPane.showConfirmDialog(null, "Would you like to " 
    	+ "save your game first?","Stop! Save?",
    	JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
	 if(choice == JOptionPane.YES_OPTION) 
	 {
	  save();
	 }
	 else if(choice != JOptionPane.CANCEL_OPTION) 
	 {
	  System.exit(0);
	 }
	}
	else 
	{
	 System.exit(0);		
	}
   }
  if("Reset".equals(e.getActionCommand()))
    gameBoard.reset();
   if("File".equals(e.getActionCommand()))
	this.loadOrSave();
   //time.revalidate();
  }

  public void setUp()
  {
   /*
   *  Establishes the initial board
   */
    
   basic = new GridBagConstraints();
   basic.gridx=startAt;
   basic.gridy=0;
   basic.gridwidth=8;
   basic.gridheight=8;
   basic.fill=GridBagConstraints.BOTH;
    
   //Creates the game board
   gameBoard = new Board(scale,windowWidth,windowHeight);
   
   //Creates the tiles that make up the maze.
   //gameBoard.createTiles(basic, window);
   createTiles();

   //Creates the placeholders along the side and the grid.
   gameBoard.createBoard(basic,window);
   
   this.addButtons();
   GridBagConstraints nb = new GridBagConstraints();
   nb.gridwidth = 4;
   nb.weightx = 1.0;
   nb.weighty = 1.0;
   nb.gridx =1;
   nb.gridy =0;
   nb.anchor = GridBagConstraints.NORTH;
   time.setPreferredSize(new Dimension(100, 100));
   this.add(time,nb);
   return;
  }
 

	  
  
  public void addButtons()
  /*
   * Used by setUp() to configure the buttons on a button bar and
   * add it to the gameBoard
   */
  {
  //this is the basic menu at the top of
  //the screen.
   basic.anchor = GridBagConstraints.NORTH;
   basic.gridwidth = 4;
   basic.weightx = 1.0;
   basic.weighty = 1.0;
   basic.gridx = 1;
   basic.gridy = 1;
   basic.fill = GridBagConstraints.NONE;
   


   JPanel bttns = new JPanel();
   bttns.setBackground(new Color(130,178,199));
   bttns.setVisible(true);
   add(bttns, basic);
	 
   //declares each button as a new JButton
   JButton file = new JButton("File");
   JButton reset = new JButton("Reset");
   JButton quit = new JButton("Quit");
	
   //sets the dimensions of the square menu tiles.
   file.setPreferredSize(new Dimension(100, 100));
   reset.setPreferredSize(new Dimension(100, 100));
   quit.setPreferredSize(new Dimension(100, 100));
  // time.setPreferredSize(new Dimension(100, 100));
   //adds the buttons into the main square
   bttns.add(file, basic);
   bttns.add(reset, basic);
   bttns.add(quit, basic);
 //  bttns.add(time, nb);
   //allows for the buttons to be clicked inside 
   //the menu.
   file.addActionListener(this);
   reset.addActionListener(this);
   quit.addActionListener(this);
	  
   return;
  }
  
  
  
  public void createTiles()
  {
   try
   {
	if (ReadFile.isCorrectFormat())
	{
	 gameBoard.createTiles(basic, window);
	/* if (ReadFile.isPlayed())
	 {
	  time.setStartTime(gameBoard.seconds);
	 }*/
	}
	else
	{
	 gameBoard.inputFile = null;
	 return;
	}
   }
   catch (FileNotFoundException e)
   {
    JOptionPane.showMessageDialog(null, gameBoard.inputFile + " was not valid or couldn't"
    + " be found.", "Alert", JOptionPane.WARNING_MESSAGE);
    gameBoard.inputFile = null;
    return;
   }
   catch (IOException e)
   {
	JOptionPane.showMessageDialog(null, gameBoard.inputFile +" was not valid or couldn't"
    + "be opened.", "Alert",
    JOptionPane.WARNING_MESSAGE);
	gameBoard.inputFile = null;
    return;
   }
  }
  
  public void setWidth() 
  {
   windowWidth = getSize().width;
  }
  
  public void setHeight() 
  {
   windowHeight = getSize().height;
  }
  
  public void save()
  /* Calls save method from ActionListen and sets 
   * saved to true or false depending on if the 
   * file was correctly saved. 
   */
  {
   ActionListen al = new ActionListen(gameBoard);
   JFileChooser f1 = new JFileChooser();
   JFrame frame = new JFrame();
   saved = al.save(f1, frame);
  }
  
  public void loadOrSave() 
  /* If the user selected the "File" button
   * then a popup asks the user if they would
   * like to "Load" a game or "Save" their
   * current game, and then performs the 
   * requested task.
   */
  {
   String[] array = {"Save", "Load"};
   int choice = JOptionPane.showOptionDialog(null, "Would you like to "
		+ "Save your game or Load an existing game?", "File", 
		JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, 
		null, array, array[0]);
   ActionListen al = new ActionListen(gameBoard);
   JFileChooser f1 = new JFileChooser();
   JFrame frame = new JFrame();
   if (choice == 0) //user selects "Save"
   {
	save();
   }
   else if (choice == 1) //user selects "Load"
   {
	/* Checks if the user has moved or rotated 
	 * any tiles without saving before loading
	 * a new game.    
	 */
	if (gameBoard.isModified() && !saved)
	{
	 String[] array2 = {"yes", "no"};
	 choice = JOptionPane.showOptionDialog(null, "Would you like to "
		+ "save your game first?", "STOP! Save?",
		JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
		null, array2, array2[0]);
	 if (choice == 0) //user selects "yes"
	 {
	  save();
	 }
	}
	al.load(f1, frame);
	MseListen.modified = false;
   }
  } 
}