// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 2 

import javax.swing.*;
import java.awt.*;

public class Main 
{
 public static void main(String[] args)
 {
  // This is the play area
  GameWindow game = new GameWindow("Group Charlie Maze");
    
  game.setSize(new Dimension(900, 1000));
  
  game.setWidth();
  game.setHeight();
    
  game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  game.getContentPane().setBackground(new Color(130,178,199));
  game.setUp();
  game.setVisible(true);
 }
};