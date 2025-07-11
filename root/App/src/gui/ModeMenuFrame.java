package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ModeMenuFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4248591531475710136L;
	Dimension screenSize;
	
	public ModeMenuFrame()
	{
		ScreenSizeManager.fetchScreenInfo();
		screenSize = ScreenSizeManager.getScreenDimension();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(418, 679));
		setBounds(ScreenSizeManager.getScreenBounds());
		setPreferredSize(screenSize);
		setVisible(true);
		setEnabled(true);
		
		JPanel mainPanel = new JPanel();
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel northPanel = new JPanel();
		mainPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
		
		JLabel lblNewLabel = new JLabel("Choose mode");
		lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		northPanel.add(lblNewLabel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setFont(new Font("Roboto", Font.PLAIN, 30));
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));
		
		JButton autoModeButton = new JButton("Auto");
		autoModeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					gui.ModeFrame autoModeFrame = new gui.ModeFrame("Auto");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		autoModeButton.setFont(new Font("Roboto", Font.PLAIN, 30));
		centerPanel.add(autoModeButton);
		
		JButton regionModeButton = new JButton("Region");
		regionModeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					gui.ModeFrame regionModeFrame = new gui.ModeFrame("Region");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		regionModeButton.setFont(new Font("Roboto", Font.PLAIN, 30));
		centerPanel.add(regionModeButton);
		
		JButton roomModeButton = new JButton("Room");
		roomModeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					gui.ModeFrame roomModeFrame = new gui.ModeFrame("Room");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		roomModeButton.setFont(new Font("Roboto", Font.PLAIN, 30));
		centerPanel.add(roomModeButton);
	}

}
