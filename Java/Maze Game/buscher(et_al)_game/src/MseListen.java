// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 3 

//This class is responsible
//for moving the pieces. 
//It has the functionality
//for grid and side snapping,
//and it returns the tile to its
//previous location if it was 
//placed somewhere "illegal".

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class MseListen extends MouseAdapter  
{
  public int x;
  public int y;
  public Piece piece;
  public static boolean modified = false;
  
 public MseListen(Piece piece)
 {
  this.piece = piece;
 }
  
 public void mouseDragged(MouseEvent mve) 
 {
   //Gets and tracks mouse location
  mve.translatePoint(mve.getComponent().getLocation().x,
      mve.getComponent().getLocation().y);
  x = mve.getX();
  y = mve.getY();
  
  mve.getComponent().setLocation(x,y);
  if (inGrid(x,y))
  {
   piece.setCurrentX(piece.gridSnapX(x));
   piece.setCurrentY(piece.gridSnapY(y));
  } 
  else if (onSide(x,y))
  {
   piece.setCurrentX(piece.sideSnapX(x));
   piece.setCurrentY(piece.sideSnapY(y));
  } 
  else 
  {
   piece.setCurrentX(x);
   piece.setCurrentY(y);
  }
  modified = true;
}
 
 public void mouseReleased(MouseEvent mve) 
 /* Controls where the tile goes upon the release 
  * of the mouse.
  * 
  * If a tile is in the grid or on the side, then 
  * it snaps to position. Otherwise, if it is in an 
  * illegal position or overlapping, it goes back 
  * to its previous position
  */
 {
  mve.translatePoint(mve.getComponent().getLocation().x,
   mve.getComponent().getLocation().y);
  x = mve.getX();
  y = mve.getY();
  if (Board.isOverlapping(piece))
  {
   mve.getComponent().setLocation(piece.getPreviousX(),
		   piece.getPreviousY());
   piece.setCurrentX(piece.getPreviousX());
   piece.setCurrentY(piece.getPreviousY());
   Indicator ind = new Indicator(piece);
  }
  else if (inGrid(x,y))
  {
   mve.getComponent().setLocation(piece.gridSnapX(x),
		   piece.gridSnapY(y));
   piece.setCurrentX(piece.gridSnapX(x));
   piece.setCurrentY(piece.gridSnapY(y));
  }
  else if (onSide(x,y))
  {
   mve.getComponent().setLocation(piece.sideSnapX(x), 
		   piece.sideSnapY(y));
   piece.setCurrentX(piece.sideSnapX(x));
   piece.setCurrentY(piece.sideSnapY(y));
  }
  if (!inGrid(x,y) && !onSide(x,y))
  {
   mve.getComponent().setLocation(piece.getPreviousX(),
		   piece.getPreviousY());
   piece.setCurrentX(piece.getPreviousX());
   piece.setCurrentY(piece.getPreviousY());
  } 
  piece.setPreviousX(piece.getCurrentX());
  piece.setPreviousY(piece.getCurrentY());
  piece.overlapping = false;
  Board.isSolved();
  modified = true;
 }
 
 @Override
 public void mouseClicked(MouseEvent cl)
 /* Rotates a tile clockwise on click. */
 {
  if (SwingUtilities.isRightMouseButton(cl)) 
  {
   piece.rot += Math.PI/2;
   if (piece.rot >= 2*Math.PI)
   {
	piece.rot = 0.0;
   }
   piece.repaint();
   piece.setOrientation();
   Board.isSolved();
   modified = true;
  }
 }
  
 public boolean inGrid(int x, int y)
 /* Checks to see if the tile is 
  * in the grid.
  */
 {
  int xStart = ((Piece.windowWidth-15)/2) - 200;
  int yStart = ((Piece.windowHeight-40)/2) - 200;
  if (x > xStart-50 && x < xStart+400 
   && y > yStart-50 && y < yStart+400)
  {
   return true;
  }
  else
  {
   return false;
  }
 }
  
 public boolean onSide(int x, int y)
 /* Checks to see if the tile is 
  * on the side.
  */
 {
  if ((x < 110 || x > Piece.windowWidth - 170) 
  || y < 20 && y > Piece.windowHeight - 20)
  {
   return true;
  }
  else
  {
   return false;
  }
 }
 
};