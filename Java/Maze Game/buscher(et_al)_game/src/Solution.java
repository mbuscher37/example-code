// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 1 

//This class holds the position of the solution
//for each tile. It also checks if the tile
//is in the correct position.

import java.awt.Point;

public class Solution 
{
  // The solution for each tile:
  private static final Point tile00 = new Point(242,280);
  private static final Point tile01 = new Point(342,280);
  private static final Point tile02 = new Point(442,280);
  private static final Point tile03 = new Point(542,280);
  private static final Point tile04 = new Point(242,380);
  private static final Point tile05 = new Point(342,380);
  private static final Point tile06 = new Point(442,380);
  private static final Point tile07 = new Point(542,380);
  private static final Point tile08 = new Point(242,480);
  private static final Point tile09 = new Point(342,480);
  private static final Point tile10 = new Point(442,480);
  private static final Point tile11 = new Point(542,480);
  private static final Point tile12 = new Point(242,580);
  private static final Point tile13 = new Point(342,580);
  private static final Point tile14 = new Point(442,580);
  private static final Point tile15 = new Point(542,580);
  
  public static Point getSolution(int tileNum)
  /* Returns the correct solution given
   * a tile number.
   */
  {
   if (tileNum == 0)
   {
	return tile00;
   }
   else if (tileNum == 1)
   {
	return tile01;
   }
   else if (tileNum == 2)
   {
	return tile02;
   }
   else if (tileNum == 3)
   {
	return tile03;
   }
   else if (tileNum == 4)
   {
	return tile04;
   }
   else if (tileNum == 5)
   {
	return tile05;
   }
   else if (tileNum == 6)
   {
	return tile06;
   }
   else if (tileNum == 7)
   {
	return tile07;
   }
   else if (tileNum == 8)
   {
	return tile08;
   }
   else if (tileNum == 9)
   {
	return tile09;
   }
   else if (tileNum == 10)
   {
	return tile10;
   }
   else if (tileNum == 11)
   {
	return tile11;
   }
   else if (tileNum == 12)
   {
	return tile12;
   }
   else if (tileNum == 13)
   {
	return tile13;
   }
   else if (tileNum == 14)
   {
	return tile14;
   }
   else
   {
    return tile15;
   }
  }
  
  public static boolean isPositioned(Piece piece)
  /* Checks if the tile is in the correct position. */
  {
   Point tileSol = getSolution(piece.getTileNum());
   if (piece.getCurrentX() == tileSol.x
	&& piece.getCurrentY() == tileSol.y)
   {
	return true;
   }
   else
   {
	return false;
   }
  }
}
