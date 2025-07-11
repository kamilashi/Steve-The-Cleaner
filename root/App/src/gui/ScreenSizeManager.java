package gui;

import java.awt.Dimension;
import java.awt.Rectangle;

public abstract class ScreenSizeManager {
	private static int screenWidth, screenHeight;
	
	public static void fetchScreenInfo()
	{
		screenWidth = 414;
		screenHeight = 736;
	}
	
	public static Dimension getScreenDimension()
	{
		return new Dimension(screenWidth,screenHeight);
	}
	
	public static Rectangle getScreenBounds()
	{
		return new Rectangle(0,0,screenWidth,screenHeight);
	}
	
	
	public static int getScreenWidth()
	{
		
		return screenWidth;
	}
	
	public static int getScreenHeight()
	{
		
		return screenHeight;
	}
}
