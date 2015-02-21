package run.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import model.ModelParameters;

public class GraphicalParameters {

	private static final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static final int SCREEN_WIDTH = (int) Math.round(screenSize
			.getWidth());
	public static final int SCREEN_HEIGHT = (int) Math.round(screenSize
			.getHeight());
	public static final int COMPATIBILITY_SIZE = (Math.min(SCREEN_WIDTH,
			SCREEN_HEIGHT) * 90) / 100;

	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;

	public static final int INFO_PANEL_WIDTH = (WINDOW_WIDTH * 25) / 100;
	public static final int INFO_PANEL_HEIGHT = WINDOW_HEIGHT;

	public static final int GAME_PANEL_WIDTH = (WINDOW_WIDTH * 75) / 100;
	public static final int GAME_PANEL_HEIGHT = WINDOW_HEIGHT;

	public static final int SCALE_SMALL = 60;
	public static final int SCALE_MEDIUM = 45;
	public static final int SCALE_LARGE = 30;

	public static final int GRAPHICAL_GOBAN_SIZE_SMALL = SCALE_SMALL
			* ModelParameters.GOBAN_SIZE_SMALL;
	public static final int GRAPHICAL_GOBAN_SIZE_MEDIUM = SCALE_MEDIUM
			* ModelParameters.GOBAN_SIZE_MEDIUM;
	public static final int GRAPHICAL_GOBAN_SIZE_LARGE = SCALE_LARGE
			* ModelParameters.GOBAN_SIZE_LARGE;

	public static final String BACKGROUND_IMAGE_PATH_NAME = "img/Wood-Pattern-Background.jpg";
	
	public static final String ICON_PATH = "img/logo.png";
	
	public static final String SMALL_GOBAN = "img/size_9.png";
	public static final String MEDIUM_GOBAN = "img/size_13.png";
	public static final String LARGE_GOBAN = "img/size_19.png";
	
	public static final String SMALL_BLACK_STONE_PATH_NAME = "img/Black/20.png";
	public static final String SMALL_WHITE_STONE_PATH_NAME = "img/White/20-0.png";
	public static final String SMALL_CROSS_STONE_PATH_NAME = "img/Red_cross/redcross20.png";
	
	public static final String MEDIUM_BLACK_STONE_PATH_NAME = "img/Black/31.png";
	public static final String MEDIUM_WHITE_STONE_PATH_NAME = "img/White/31-0.png";
	public static final String MEDIUM_CROSS_STONE_PATH_NAME = "img/Red_cross/redcross31.png";
	
	public static final String LARGE_BLACK_STONE_PATH_NAME = "img/Black/43.png";
	public static final String LARGE_WHITE_STONE_PATH_NAME = "img/White/43-0.png";
	public static final String LARGE_CROSS_STONE_PATH_NAME = "img/Red_cross/redcross43.png";
	
	public static final int SMALL_STONE_SIZE = 43;
	public static final int MEDIUM_STONE_SIZE = 31;
	public static final int LARGE_STONE_SIZE = 20;
	
	public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

	public static final int MAX_RAND = 20;
	public static final int MIN_RAND = 1;
	
	public static final String COMPUTER_NAME_FIELD = "Computer";

}
