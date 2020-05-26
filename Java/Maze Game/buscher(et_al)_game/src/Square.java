// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 2 

import java.awt.Dimension;
import javax.swing.JPanel;

public class Square extends JPanel
{
 private int scale;
 private JPanel square;
 public static Dimension square_size;
  
 public Square(int scale)
 {
  this.scale = scale;
  square_size = new Dimension(100*scale,100*scale);
  
  square = new JPanel();
  square.setPreferredSize(square_size);
 }
	
 public void setScale(int scale)
 {
  this.scale = scale;
 }
  	
 public int getScale()
 {
  return scale;
 }
}