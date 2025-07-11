package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import program.MapManager;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Rectangle;

public class ModeFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7520363334341273313L;
	
	Dimension screenSize;
	String chosenMode;

	public ModeFrame(String mode) throws IOException
	{
		
		Runnable receiver = new program.MapReceiver();				//Instantiate a map
		
		
		chosenMode = mode;
		ScreenSizeManager.fetchScreenInfo();
		screenSize = ScreenSizeManager.getScreenDimension();
		gui.MapPainter mapPainter;
		if(chosenMode=="Room")
		{
			mapPainter =  new gui.MapPainter(true);
		}
		else {
			mapPainter =  new gui.MapPainter(false);
			
			if(chosenMode=="Auto")														
			{
				//receiver.run();
				Thread mapReceiverThread = new Thread(receiver);						
				mapReceiverThread.start();
				
				Thread painterUpdateThread = new Thread(mapPainter);						
				painterUpdateThread.start();
				
			}
		}
		
		
		mapPainter.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(chosenMode=="Region")
				{
					//clipRegion(e.getX(), e.getY(),20);
					try {
					MapManager.updateRegionSettings(e.getX(), e.getY(),20);
					mapPainter.repaint();
					}
					catch(Exception ex) {}
				}
				else if(chosenMode == "Room")
				{
					try {
					MapManager.updateRoomSettings(e.getX(), e.getY());
					mapPainter.repaint();
					} catch(Exception ex) {}
				}
				
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(418, 679));
		setBounds(ScreenSizeManager.getScreenBounds());
		setPreferredSize(screenSize);
		setName("this");
		setVisible(true);
		setEnabled(true);
		
		

		  //MapStorage.printMap();
			
		
		  
		  JPanel mainPanel = new JPanel();
		  this.getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainPanel.setLayout(new BorderLayout(0, 0));
			
			JPanel northPanel = new JPanel();
			mainPanel.add(northPanel, BorderLayout.NORTH);
			northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
			
			JPanel northButtonPanel = new JPanel();
			northPanel.add(northButtonPanel);
			northButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));
			
			if(chosenMode!="Auto")
			{
			JButton loadButton = new JButton("Load");
			loadButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						MapManager.loadSettings();
						mapPainter.repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			loadButton.setFont(new Font("Roboto", Font.PLAIN, 20));
			northButtonPanel.add(loadButton);
			}
			
			JButton saveButton = new JButton("Save");
			saveButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					try {
						MapManager.saveSettings();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
			});
			saveButton.setFont(new Font("Roboto", Font.PLAIN, 20));
			northButtonPanel.add(saveButton);
			
			JPanel centerPanel = new JPanel();
			centerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
			centerPanel.setFont(new Font("Roboto", Font.PLAIN, 30));
			mainPanel.add(centerPanel, BorderLayout.CENTER);
			centerPanel.setLayout(new BorderLayout(0, 50));
		
			
			centerPanel.add(mapPainter);
			mapPainter.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			
			JPanel panel = new JPanel();
			FlowLayout flowLayout_2 = (FlowLayout) panel.getLayout();
			flowLayout_2.setHgap(8);
			centerPanel.add(panel, BorderLayout.WEST);
			
			JPanel westPanel = new JPanel();
			FlowLayout fl_westPanel = (FlowLayout) westPanel.getLayout();
			fl_westPanel.setHgap(20);
			mainPanel.add(westPanel, BorderLayout.WEST);
			
			JPanel eastPanel = new JPanel();
			FlowLayout flowLayout_1 = (FlowLayout) eastPanel.getLayout();
			flowLayout_1.setHgap(20);
			mainPanel.add(eastPanel, BorderLayout.EAST);
			
			JPanel southPanel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) southPanel.getLayout();
			flowLayout.setHgap(70);
			flowLayout.setVgap(70);
			mainPanel.add(southPanel, BorderLayout.SOUTH);
			

			JPanel southButtonPanel = new JPanel();
			southPanel.add(southButtonPanel);
			
			JButton resetButton = new JButton("Reset");
			resetButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					try {
					MapManager.resetMap();
					mapPainter.repaint();
					}
					catch(Exception ex) {}
				}
			});
			southButtonPanel.setLayout(new GridLayout(0, 2, 0, 0));
			resetButton.setFont(new Font("Roboto", Font.PLAIN, 20));
			southButtonPanel.add(resetButton);
			
			JButton OKbutton = new JButton("OK");
			OKbutton.setFont(new Font("Roboto", Font.PLAIN, 20));
			southButtonPanel.add(OKbutton);
			
			
			JLabel pageTitleLabel = new JLabel("       " + chosenMode + " Mode          ");
			pageTitleLabel.setPreferredSize(new Dimension(105, 100));
			pageTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
			pageTitleLabel.setFont(new Font("Roboto Light", Font.PLAIN, 30));
			getContentPane().add(pageTitleLabel, BorderLayout.NORTH);
			

			OKbutton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

				pageTitleLabel.setText("       Map has been sent!");
				}
			});
			
		
	}
	
		
}
