// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 1 

//This class makes a tile 
//flash if it is overlapping 
//another tile.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Indicator implements ActionListener
{
  private Piece piece;
  private Timer tim;
  private int seconds;
  
  public Indicator(Piece piece)
  {
   tim = new Timer(25,this);
   this.piece = piece;
   tim.start();
  }
  public void actionPerformed(ActionEvent e)
  //makes the tile flash red
  //for 5 seconds/counts
  {
   piece.overlapping = !piece.overlapping;
   piece.repaint();
   seconds++;
   if (seconds > 5)
   {
	piece.overlapping = false;
	tim.stop();
   }
  }
}
