// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 5

// Creates the playing board and the 
// initial pieces/tiles on the sides.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.*;

public class Board
{
  public static final int LENGTH = 8;
  private static Dimension square_size;
  private int scale;
  private int windowWidth;
  private int windowHeight;
  public static int numTiles;
  public static long seconds;
  private GridBagConstraints gbcWEST;
  private GridBagConstraints gbcEAST;
  public static Piece[][] JParr; //2-d Array to hold pieces
  public static Piece[] OrderedArray; //Array to hold ordered pieces
  public static String inputFile = "input/default.mze";
  private int[][] boardPositions; //2-d Array to hold board positions
  private static boolean solved;
  
  public Board(int scale, int windowWidth, int windowHeight)
  {
   //creates a JPanel for the grid to rest in
   JPanel board = new JPanel();
   
   //allows us to set the location of the constraints
   gbcWEST = new GridBagConstraints();
   gbcEAST = new GridBagConstraints();
   this.scale = scale;
   square_size = new Dimension(100*scale,100*scale);
	
   board.setLayout(new GridLayout(LENGTH, LENGTH));
   board.setVisible(true);
   
   this.windowWidth = windowWidth;
   this.windowHeight = windowHeight;
   
   JParr = new Piece[4][4];
   OrderedArray = new Piece[16];
   solved = false;
   setBoardPositions();
   return;
  }
  
  public void setWidth(int width)
  {
	  windowWidth = width;
  }
  
  public void setHeight(int height)
  {
	  windowHeight = height;
  }
  
  public Piece createPiece(GridBagConstraints gbc, int x, int y)
  /*Takes in a constraint set and generates
    the tile pieces that are along the side.*/
  {
   GridBagLayout pieceL = new GridBagLayout();
   Piece piece = new Piece(scale,windowWidth,windowHeight,x,y);
   pieceL.setConstraints(piece, gbc);
   piece.setPreferredSize(square_size);
	
   piece.setSize(square_size);
   piece.setBackground(Color.white);
   piece.move(piece);
   return piece;
  }
  
  public Square createSquare(GridBagConstraints gbc)
  /*Creates immovable squares that are the spots
    for the tiles. */
  {
   GridBagLayout squareL = new GridBagLayout();
   Square square = new Square(scale);
   squareL.setConstraints(square, gbc);
   square.setPreferredSize(square_size);
	
   square.setSize(square_size);
   square.setBackground(Color.white);
   square.setBorder(BorderFactory.createLineBorder(Color.black, 1));
   return square;
  }
    
  public void createBoardWEST(GridBagConstraints basic, JPanel window)
  //Creates the tiles on the right side of the board
  {
   gbcWEST.anchor = GridBagConstraints.WEST;
   gbcWEST.weighty = 1;
   gbcWEST.gridx = 0;
  
   int yWidth = (windowHeight-60)/8;
   int yStart = 20;
   for (int i = 0; i < 8; i++) 
   {
	Piece piece = createPiece(basic,10,yStart+i*yWidth);
	window.add(piece, gbcWEST);
	if(i < 4)
	{
	 JParr[0][i] = piece;
	}
	else
	{
	 JParr[1][i-4] = piece;
	}
   }
  }
  
  public void createBoardEAST(GridBagConstraints basic, JPanel window)
  //Creates the tiles on the right side of the board
  {
   gbcEAST.anchor = GridBagConstraints.EAST;
   gbcEAST.weighty = 1;
   gbcEAST.gridx = 7;
   
   int yWidth = (windowHeight-60)/8;
   int yStart = 20;
   for (int i = 0; i < 8; i++) 
   {
    Piece piece = createPiece(basic,windowWidth-126
							,yStart+i*yWidth);
	window.add(piece, gbcEAST);
	if(i < 4)
	{
	 JParr[2][i] = piece;
	}
	else
	{
	 JParr[3][i-4] = piece;
	}
   }
  }
  
  public void createGrid(GridBagConstraints basic, JPanel window)
  //Creates a grid for the game board
  {
   GridBagConstraints grid = new GridBagConstraints();
   basic.anchor = GridBagConstraints.CENTER;
   grid.gridx = 2;
   grid.gridy = 2;
   grid.gridheight = 4;
   grid.weightx = 0.1;
		
   //Create an outer square to hold all 16 squares. 
   Square mainSquare = new Square(scale);
   GridBagLayout mainSqreL = new GridBagLayout();
   mainSquare.setPreferredSize(new Dimension(400,400));
		
   mainSqreL.setConstraints(mainSquare, basic);
   mainSquare.setBackground(Color.white);
   window.add(mainSquare, grid);
   
   GridLayout gridLayout = new GridLayout(4, 4);
   mainSquare.setLayout(gridLayout);
	//creates a 4x4 grid of squares.
   for (int i = 0; i < 4; i++)
   {
    for (int j = 0; j < 4; j++)
    {
     JPanel square = new JPanel(); 
     square.setPreferredSize(square_size);
	 square.setBackground(Color.white);
	 mainSquare.add(square, basic);
     square.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }
   }	 
  }
  
  public void createBoard(GridBagConstraints basic, JPanel window)
  /*Creates the tile placeholders along the sides
   *and calls the createGrid function. */
  {
   gbcWEST.anchor = GridBagConstraints.WEST;
   gbcWEST.weighty = 1;
   gbcWEST.gridx = 0;
     
   for (int i = 0; i < 8; i++) 
   {
	gbcWEST.gridy = i;
	Square square = createSquare(basic);
	window.add(square, gbcWEST);
   }
   
   gbcEAST.anchor = GridBagConstraints.EAST;
   gbcEAST.weighty = 1;
   gbcEAST.gridx = 7;
      
   for (int i = 0; i < 8; i++) 
   {
	gbcEAST.gridy = i;
	Square square = createSquare(basic);
	window.add(square, gbcEAST);
   }
   createGrid(basic,window);
  }
  
  public void createTiles(GridBagConstraints basic, JPanel window) throws IOException
  /* Calls other methods to create and draw the tiles along
   * the sides. It also checks that there are no more than 
   * 4 tiles in "up" position.
   */
  {
   createBoardWEST(basic, window);
   createBoardEAST(basic, window);
   readFile();
   rotationChecker();
  }
  
  public void reset()
  /* Resets the tiles to their original position. */
  {
   for (int i = 0; i < 4; i++)
   {
	for (int j = 0; j < 4; j++)
	{
	 JParr[i][j].snapBack(JParr[i][j]);
	}
   }
   solved = false;
   MseListen.modified = false;
   Timer.running = false;
   Timer.reset();
  }
 
  public Piece getRandomPiece(int tileNum)
  /* Assigns a tile number to a random tile in the array. 
   * This allows the tiles to be randomized each time 
   * the board is created. 
   * 
   * Also, makes sure that the tile does not 
   * already have a tile number.
   */
  {
   Random rand = new Random();
   int index1 = rand.nextInt(3);
   int index2 = rand.nextInt(3);
   boolean found = false;
   while (!found)
   {
	if (JParr[index1][index2].isSet())
	//checks to see if the tile already
	//has a tile number
	{
	 index2++;
	 if (index2 == 4)
	 {
	  index2 = 0;
	  index1++;
	  if (index1 == 4)
	  {
	   index1 = 0;
	  }
	 }
	}
	else
	{
	 found = true;
	}
   }
   JParr[index1][index2].setTileNum(tileNum);
   return JParr[index1][index2];
  }
  
  public Piece getPiece(int tileNum, int i)
  {
   int index1 = i % 4;
   int index2 = i / 4;
   int x = boardPositions[tileNum][0];
   int y = boardPositions[tileNum][1];
   JParr[index1][index2].setOriginX(x);
   JParr[index1][index2].setOriginY(y);
   JParr[index1][index2].setLocation(x, y);
   JParr[index1][index2].setTileNum(i);
   return JParr[index1][index2];
  }
  
  public void setBoardPositions()
  {
   //store x & y positions of tile placements into 2d array
   boardPositions = new int[32][2];
   int yWidth = (windowHeight-60)/8;
   int yStart = 20;
   int xStartGrid = ((windowWidth-15)/2) - 200;
   int yStartGrid = ((windowHeight-40)/2) - 200;
   for (int i = 0; i < 8; i++) 
   {
	//West side tiles
	boardPositions[i][0] = 10;
	boardPositions[i][1] = yStart+i*yWidth;
	//East side tiles
	boardPositions[i+8][0] = windowWidth-126;
	boardPositions[i+8][1] = yStart+i*yWidth;
   }
   int index = 16;
   for (int i = 0; i < 4; i++)
   {
	for (int j = 0; j < 4; j++)
	{
	 //Grid tile positions
	 boardPositions[index][0] = xStartGrid + 100*i;
	 boardPositions[index][1] = yStartGrid + 100*j;
	 index++;
	}
   }
  }
  
  public int getBoardPosition(Piece piece)
  {
   int boardPos = -1;
   for (int i = 0; i < 32; i++)
   {
	if (piece.getCurrentX() == boardPositions[i][0]
	 && piece.getCurrentY() == boardPositions[i][1])
	{
	 boardPos = i;
	}
   }
   return boardPos;
  }
  
  public void readFile() throws IOException
  /* Reads in the file that creates
   * the maze. Sets the line coordinates
   * for each line and tile. */
  {
   if(inputFile != null) 
   {
    int fileSize = (int) (new File(inputFile)).length();
    int tileNum;
    int numLines;
    int position;
    int num = 0;
    int rotation;
    float[][] tileCoords;
   
    ReadFile inputStream = new ReadFile(inputFile);
	inputStream.readFile();
	numTiles = inputStream.readInt(1);
	seconds = inputStream.readLong(2);
	int i = 4;
	while (i < fileSize/4)
	{
	 tileNum = inputStream.readInt(i);
	 rotation = inputStream.readInt(i+1);
	 numLines = inputStream.readInt(i+2);
	 tileCoords = new float[numLines][4];
	 position = i + 3;
	 for (int j = 0; j < numLines; j++)
	 {
	  for (int k = 0; k < 4; k++)
	  {
	   tileCoords[j][k] = inputStream.readFloat(position);
	   position++;
	  }
	 }
	 if (inputStream.isPlayed())
	 {
	  Piece piece = getPiece(tileNum,num);
	  piece.setTileCoordinates(tileCoords);
	  piece.setNumLines(numLines);
	  piece.rot = (Math.PI/2)*rotation;
	  piece.previousRot = (Math.PI/2)*rotation;
	  piece.setOrientation();
	  piece.setPreviousOrientation();
	  piece.repaint();
	  Timer.setElapsedTime(seconds);
	 }
	 else
	 {
	  Piece piece = getRandomPiece(tileNum);
	  piece.setTileCoordinates(tileCoords);
	  piece.setNumLines(numLines);
      inputStream.setCFormat();
	 }
	 
	 i = position;
	 num++;
	}
	inputStream.close();
   }
  }
  
  public static boolean isOverlapping(Piece piece)
  /* Checks if a tile is overlapping with
   * any other tile on the board.
   */
  {
   boolean overlapping = false;
   for (int i = 0; i < 4; i++)
   {
	for (int j = 0; j < 4; j++)
	{
	 if (JParr[i][j].getTileNum() != piece.getTileNum()
	 && JParr[i][j].getCurrentX() == piece.getCurrentX()
	 && JParr[i][j].getCurrentY() == piece.getCurrentY())
	 {
	  overlapping = true;
	 }
	}
   }
   return overlapping;
  }
  
  public void rotationChecker()
  /* Checks that there are no more than 4 tiles 
   * in "up" position (or 0 degree rotation) 
   * on the board.
   */
  {
   int count = 0;
   int degree;
   Random rand = new Random();
   for (int i = 0; i < 4; i++)
   {
	for (int j = 0; j < 4; j++)
	{
     if (JParr[i][j].getOrientation().equals("up"))
     {
      count++;
      if (count > 4)
      {
       //Randomly assigns the tile a new orientation.
       degree = 90*(rand.nextInt(3)+1);
       JParr[i][j].rot += Math.toRadians(degree);
       JParr[i][j].previousRot = JParr[i][j].rot;
       JParr[i][j].setOrientation();
       JParr[i][j].repaint();
      }
     }
	}
   }
  }
  
  public static boolean isModified()
  /* Checks to see if any tiles
   * have been moved or rotated. 
   */
  {
   boolean modified = false;
   if (MseListen.modified)
   {
	modified = true;
   }
   return modified;
  }
  
  public static void fillOrderedArray()
  {
   int tileNum;
   for (int i = 0; i < 4; i++)
   {
	for (int j = 0; j < 4; j++)
	{
	 tileNum = JParr[i][j].getTileNum();
	 OrderedArray[tileNum] = JParr[i][j];
	}
   }
  }  
  
  public static boolean isSolved()
  /* Checks if the pieces are in the correct 
   * position and that they are all oriented
   * up.
   * 
   * The solution is stored in the 
   * class Solution.
   */
  {
   boolean incorrect = false;
   int i = 0;
   int j = 0;
   while (!incorrect && i < 4)
   {
	if (!Solution.isPositioned(JParr[i][j]) 
	|| JParr[i][j].rot != Piece.UP)
	{
	 incorrect = true;
	}
	else
	{
	 j++;
	 if (j == 4)
	 {
	  i++;
	  j = 0;
	 }
	}
   }
   if (!incorrect)
   {
	solved = true;
	Timer.stop();
	JOptionPane.showMessageDialog(null, "Congrats!! You solved the puzzle!"
		+  "\nYour time was " + Timer.convertToTime(), 
		"Congratulations!", JOptionPane.PLAIN_MESSAGE); 
	System.exit(0);
   }
   return solved;
  }
};