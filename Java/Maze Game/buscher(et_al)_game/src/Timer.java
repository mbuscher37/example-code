import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Timer extends JPanel
{
  private static long startTime = 0;
  private static long stopTime = 0;
  public static long secondCount = 0;
  public static long elapsedTime = 0;
  public static boolean running = false;
  
  public Timer() 
  {
   ;
  }
    
  public static void start() 
  {
   startTime = System.currentTimeMillis();
   running = true;
  }
  
  public static void reset() 
  {
   startTime = 0;
   stopTime = 0;
   running = false;
  }
  
  public static void stop() 
  {
   stopTime = System.currentTimeMillis();
   running = false;
   secondCount = 0;
  }
  
  public static long timeElapsed() 
  {
   if (MseListen.modified && !running)
   {
	start();
   }
   else if(MseListen.modified && running) 
   {
	elapsedTime = System.currentTimeMillis() - startTime;
   }
   else
   {
    elapsedTime = stopTime - startTime;
   }
   return elapsedTime += secondCount;
  }
  
  public static void setElapsedTime(long seconds)
  {
   secondCount = seconds;
   reset();
  }
  
  public static String convertToTime() 
  {
   int seconds = 0;
   int minutes = 0;
   int hours = 0;
   String secondsString;
   String minutesString;
   String hoursString;
   
   long elapsed = timeElapsed();
   elapsed = elapsed / 1000;
   hours = (int)(elapsed / 3600);
   minutes = (int)(elapsed / 60)%60;
   seconds = (int)(elapsed)%60;
   
   hoursString = "" + hours;
   minutesString = "" + minutes;
   secondsString = "" + seconds;
   if (hours < 10)
   {
	hoursString = "0" + hours;
   }
   if (minutes < 10)
   {
	minutesString = "0" + minutes;
   }
   if (seconds < 10)
   {
	secondsString = "0" + seconds;
   }
   return hoursString + ":" + minutesString + ":" + secondsString;
  }
  
  @Override
  public void paint(Graphics g)
  {
   g.drawString(convertToTime(), 10, 10);
   this.setBackground(new Color(130,178,199));
   repaint();
  }
}
