// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 5 

//This will be the main file for 
//our piece class.
//This class defines the methods and overall functions
//of a maze piece.

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Piece extends Square
{
  public static int windowWidth;
  public static int windowHeight;
  private int originX;
  private int originY;
  private int previousX;
  private int previousY;
  private int currentX;
  private int currentY;
  private int tileNum;
  private int numLines;
  private String previousOrientation; //up, right, down, left
  private String orientation; //up, right, down, left
  private float[][] tileCoords;
  private boolean set;
  public double rot;
  public double previousRot;
  public boolean overlapping = false;
  public static final double UP = 0.0;
  public static final double RIGHT = Math.PI/2;
  public static final double DOWN = Math.PI;
  public static final double LEFT = 3*Math.PI/2;
	
  public Piece(int scale, int width, int height, int x, int y)
  {
    super(scale);
    windowWidth = width;
    windowHeight = height;
    originX = x;
    originY = y;
    currentX = originX;
    currentY = originY;
    previousX = originX;
    previousY = originY;
		
    String[] arr = {"up", "right", "down", "left"};
    double[] angle = {0.0, Math.PI/2, Math.PI, 3*Math.PI/2};
    int rand = (int)(Math.random()*4);
    orientation = arr[rand];
    rot = angle[rand];
    previousRot = rot;
    previousOrientation = orientation;
  }

  public int getOriginX()
  {
   return originX;
  }
	
  public int getOriginY()
  {
   return originY;
  }
  
  public void setOriginX(int x)
  {
   originX = x;
   previousX = originX;
   currentX = originX;
  }
	
  public void setOriginY(int y)
  {
   originY = y;
   previousY = originY;
   currentY = originY;
  }
  
  public int getCurrentX()
  {
   return currentX;
  }
	
  public int getCurrentY()
  {
   return currentY;
  }
  
  public void setCurrentX(int x)
  {
   currentX = x;
  }
	
  public void setCurrentY(int y)
  {
   currentY = y;
  }
  
  public int getPreviousX()
  {
   return previousX;
  }
	
  public int getPreviousY()
  {
   return previousY;
  }
  
  public void setPreviousX(int x)
  {
   previousX = x;
  }
	
  public void setPreviousY(int y)
  {
   previousY = y;
  }
  
  public String getOrientation()
  {
   return orientation;
  }
	
  public void setOrientation()
  /* Sets the String orientation
   * variable based on the value
   * of rot.
   */
  {
   if (rot == Math.toRadians(90.0))
   {
	orientation = "right";
   }
   else if (rot == Math.toRadians(180.0))
   {
	orientation = "down";
   }
   else if (rot == Math.toRadians(270.0))
   {
	orientation = "left";
   }
   else
   {
	orientation = "up";
   }
  }
  
  public void setPreviousOrientation()
  /* Sets the String previousOrientation
   * variable based on the value
   * of rot.
   */
  {
   if (previousRot == Math.toRadians(90.0))
   {
	previousOrientation = "right";
   }
   else if (previousRot == Math.toRadians(180.0))
   {
	previousOrientation = "down";
   }
   else if (previousRot == Math.toRadians(270.0))
   {
	previousOrientation = "left";
   }
   else
   {
	previousOrientation = "up";
   }
  }
  
  public int getTileNum()
  {
   return tileNum;
  }
  
  public void setTileNum(int num)
  /* Allows the tile number to be
   * set outside of the constructor.
   */
  {
   tileNum = num;
  }
  
  public int getNumLines()
  {
   return numLines;
  }
  
  public void setNumLines(int numLines)
  /* Allows the number of lines on the
   * tile to be set outside the constructor. 
   */
  {
   this.numLines = numLines;
  }
  
  public float getTileCoord(int line, int i)
  /* Returns the specified float in the 
   * tile coordinates array. 
   * 
   * Needed for saving the game.
   */
  {
   return tileCoords[line][i];
  }
	
  public void move(Piece piece)
  /* Moves the piece. */
  {
   MseListen listen = new MseListen(piece);
   MseListen listen2 = new MseListen(piece);
   piece.addMouseMotionListener(listen);
   piece.addMouseListener(listen2);
  }
	
  public int gridSnapX(int x) 
  /* Adds grid snapping functionality when a piece 
   * is within the area of the grid on the x-axis.
   */
  {
   int xStart = ((windowWidth-15)/2) - 200;
   int adjustedX = 0;
   if (x > xStart-50 && x < xStart + 100)
   {
    adjustedX = xStart;
   } 
   else if (x >= xStart + 100 && x < xStart + 200 )
   {
    adjustedX = xStart + 100;
   } 
   else if (x >= xStart + 200 && x < xStart + 300)
   {
    adjustedX = xStart + 200;
   } 
   else if (x >= xStart + 300 && x < xStart + 450)
   {
    adjustedX = xStart + 300;
   }
   return adjustedX;
  }
  
  public int gridSnapY(int y) 
  /* Adds grid snapping functionality when a piece 
   * is within the area of the grid on the y-axis.
   */
  {
   int yStart = ((windowHeight-40)/2) - 200;
   int adjustedY = 0;
   if (y > yStart-50 && y < yStart + 100)
   {
    adjustedY = yStart;
   } 
   else if (y >= yStart + 100 && y < yStart + 200)
   {
    adjustedY = yStart + 100;
   } 
   else if (y >= yStart + 200 && y < yStart + 300)
   {
    adjustedY = yStart + 200;
   } 
   else if (y >= yStart + 300 && y < yStart + 450)
   {
    adjustedY = yStart + 300;
   }
   return adjustedY;
  }
  
  public int sideSnapX(int x) 
  /* Adds side snapping functionality when a piece 
   * is within the area of the sides on the x-axis.
   */
  {
   int xStart = 10;
   int xEnd = windowWidth-110;
   int adjustedX = previousX;
   if (x > xStart-10 && x < xStart + 110)
   {
    adjustedX = xStart;
   } 
   else if (x > xEnd-60 && x < windowWidth )
   {
    adjustedX = xEnd-16;
   } 
   return adjustedX;
  }
  
  public int sideSnapY(int y) 
  /* Adds side snapping functionality when a piece 
   * is within the area of the sides on the y-axis.
   */
  {
   int yStart = 20;
   int yWidth = (windowHeight-60)/8;
   int adjustedY = previousY;
   if (y > yStart && y < yStart + yWidth)
   {
    adjustedY = yStart;
   } 
   else if (y >= yStart + yWidth && y < yStart + 2*yWidth)
   {
	adjustedY = yStart + yWidth;
   } 
   else if (y >= yStart + 2*yWidth && y < yStart + 3*yWidth)
   {
	adjustedY = yStart + 2*yWidth;
   } 
   else if (y >= yStart + 3*yWidth && y < yStart + 4*yWidth)
   {
	adjustedY = yStart + 3*yWidth;
   }
   else if (y >= yStart + 4*yWidth && y < yStart + 5*yWidth)
   {
	adjustedY = yStart + 4*yWidth;
   } 
   else if (y >= yStart + 5*yWidth && y < yStart + 6*yWidth)
   {
	adjustedY = yStart + 5*yWidth;
   } 
   else if (y >= yStart + 6*yWidth && y < yStart + 7*yWidth)
   {
    adjustedY = yStart + 6*yWidth;
   }
   else if (y >= yStart + 7*yWidth && y < windowHeight)
   {
	adjustedY = yStart + 7*yWidth;
   }
   return adjustedY;
  }

  public void snapBack(Piece piece)
  /* Used for resetting the tiles.*/
  {
   piece.setLocation(originX,originY);
   orientation = previousOrientation;
   rot = previousRot;
   piece.repaint();
   previousX = originX;
   previousY = originY;
   currentX = originX;
   currentY = originY;
   overlapping = false;
  }
  
  public void setTileCoordinates(float[][] tileCoords)
  /* Allows the tile coordinates to be
   * set outside of the constructor.
   */
  {
   this.tileCoords = tileCoords;
   set = true;
  }
  
  @Override
  public void paintComponent(Graphics g)
  /* Draws the lines on the tiles given 
   * their endpoints.
   * 
   * Also changes the rotation given the 
   * value of rot and the color of the tile
   * if it's overlapping another tile.
   */	  
  {
   if(Board.inputFile != null){

   super.paintComponent(g);
   Graphics2D g2d = (Graphics2D) g;
   if (overlapping)
   {
	this.setBackground(Color.RED);
   }
   else
   {
	this.setBackground(Color.WHITE);
   }
   g2d.rotate(rot, getWidth()/2, getHeight()/2);
   for (int i = 0; i < tileCoords.length; i++)
   {
	g2d.setColor(Color.BLACK);
	g2d.setStroke(new BasicStroke(3f));
	g2d.draw(new Line2D.Float(tileCoords[i][0], tileCoords[i][1],
			tileCoords[i][2], tileCoords[i][3]));
   }
   }
  }
 
  public boolean isSet()
  /* Checks to see if the tile coordinates 
   * array has already been set.
   */
  {
   return set;
  }
};