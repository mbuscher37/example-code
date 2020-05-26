// @authors Lane Snively, Meghan Buscher, Andy Enders,
//  John DesEnfants
// Date: May 1, 2020
// version 2

//This file converts bytes to integers
//and floats and vice versa.

import java.nio.ByteBuffer;

public class ConverterClass 
{
 public static float convertToFloat(byte[] array) 	
 {
  ByteBuffer buffer = ByteBuffer.wrap(array);
  return buffer.getFloat();
 }
 
 public static int convertToInt(byte[] array)
 {
  ByteBuffer buffer = ByteBuffer.wrap(array);
  return buffer.getInt();
 }
 
 public static long convertToLong(byte[] array)
 {
  ByteBuffer buffer = ByteBuffer.wrap(array);
  return buffer.getLong();
 }
 
 public static byte[] convertIntToByte(int num)
 {
  ByteBuffer buffer = ByteBuffer.allocate(4);
  buffer.putInt(num);
  buffer.flip();
  return buffer.array();
 }
 
 public static byte[] convertFloatToByte(float num)
 {
  ByteBuffer buffer = ByteBuffer.allocate(4);
  buffer.putFloat(num);
  buffer.flip();
  return buffer.array();
 }
 
 public static byte[] convertLongToByte(long num)
 {
  ByteBuffer buffer = ByteBuffer.allocate(8);
  buffer.putLong(num);
  buffer.flip();
  return buffer.array();
 }
 
 public static byte hexToByte(String hexString) 
 /* Converts a 2-digit Hex to a byte array. */
 {
  int firstDigit = charToDigit(hexString.charAt(0));
  int secondDigit = charToDigit(hexString.charAt(1));
  return (byte) ((firstDigit << 4) + secondDigit);
 }
	 
 private static int charToDigit(char hexChar) 
 /* Needed to convert Hex to Byte. */
 {
  int digit = Character.digit(hexChar, 16);
  if(digit == -1) 
  {
   throw new IllegalArgumentException(
	"Invalid Hexadecimal Character: "+ hexChar);
  }
  return digit;
 }

 public static byte[] convertHexStringToByte(String hexString) 
 /* Converts a Hexadecimal String to a byte array. */
 {
  if (hexString.length() % 2 == 1) 
  {
   throw new IllegalArgumentException(
    "Invalid hexadecimal String supplied.");
  }
  byte[] bytes = new byte[hexString.length() / 2];
  for (int i = 0; i < hexString.length(); i += 2) 
  {
   bytes[i/2] = hexToByte(hexString.substring(i, i + 2));
  }
  return bytes;
 }
}