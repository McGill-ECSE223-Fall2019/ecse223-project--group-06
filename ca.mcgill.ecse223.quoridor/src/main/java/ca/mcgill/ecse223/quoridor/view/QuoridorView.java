package ca.mcgill.ecse223.quoridor.view;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import QuoridorView.MouseEventListener;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public class QuoridorView extends JFrame implements KeyListener {
	private static final long serialVersionUID = -4426310869335015542L;
	
	
	private JButton newGame = new JButton("New Game");
	private JButton loadGame = new JButton("Load Game");
	private JLabel title = new JLabel("Quoridor");
		
	private JLabel p1Name = new JLabel();
	private JLabel p2Name = new JLabel();
	private JLabel p1Time = new JLabel();
	private JLabel p2Time = new JLabel();
	private JLabel p1Walls = new JLabel("Walls: 10");
	private JLabel p2Walls = new JLabel("Walls: 10");
	public JRadioButton p1Turn = new JRadioButton("White Turn", true); //Don't put an action listener on this!
	public JRadioButton p2Turn = new JRadioButton("Black Turn", false); //                ||
	public JLabel notification = new JLabel(); //To use for any errors, make sure it's being cleared though
	public JFrame confirmFrame = new JFrame("Confirmation");
	public JButton saveButton = new JButton("Save");
	public JButton undoButton = new JButton("Undo");
	private JButton exitButton = new JButton("Exit");
	private JButton rotateButton=new JButton("Rotate Wall");
	private JButton grabButton = new JButton("Grab Wall");
	private JButton validateButton = new JButton("Validate Position");
	private JPanel board;
	private JPanel wall;
	private GroupLayout gameLayout;
	private String fileName; //Just used to store save file name- eclipse get angry otherwise
	
	//First screen user sees, just title and two buttons
	public void initLoadScreen() {
		getContentPane().removeAll();
		setTitle("Quoridor Application");
		title.setText("Quoridor");
		title.setFont(new Font("Serif", Font.BOLD, 80));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(title)
										.addComponent(newGame)
										.addComponent(loadGame));
		layout.setVerticalGroup(layout.createSequentialGroup()
									  .addComponent(title)
									  .addComponent(newGame)
									  .addComponent(loadGame));
		newGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				initSetParams(); //Go to intialize game screen
			}
		});
		loadGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Fill this with load game stuff
			}
		});	
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {newGame, loadGame});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newGame, loadGame});		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	//Initialize the game screen. This is where usernames and think time is set
	public void initSetParams() {
		getContentPane().removeAll();
		
		//All the components to be placed on the window
		JTextField whiteName = new JTextField(20);
		JTextField blackName = new JTextField(20);
		JTextField minutesField = new JTextField(2);
		JTextField secondsField = new JTextField(2);
		JButton useExisting = new JButton("Use existing names");
		JButton useExistingClone = new JButton("Use existing names");
		JLabel tmpMinutes = new JLabel("Minutes");
		JLabel tmpSeconds = new JLabel("Seconds");
		JLabel tmpP1 = new JLabel("Player 1");
		JLabel tmpP2 = new JLabel("Player 2");
		JLabel tmpTimeTitle = new JLabel("Set Total Time");
		title.setText("Quoridor Game");
		title.setFont(new Font("Serif", Font.BOLD, 18));
		
		//placing components on the window
		
		GroupLayout layout = new GroupLayout(getContentPane());
		GroupLayout.Group horizontal = layout.createParallelGroup(GroupLayout.Alignment.CENTER)
											.addComponent(title)
											.addGroup(layout.createSequentialGroup()
															.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																			.addComponent(tmpP1)
																			.addComponent(whiteName)
																			.addComponent(useExisting)
																	)
															.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																			.addComponent(tmpP2)
																			.addComponent(blackName)
																			.addComponent(useExistingClone)
																	)
															
													)
											.addComponent(tmpTimeTitle)
											.addGroup(layout.createSequentialGroup()
													.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																	.addComponent(tmpMinutes)
																	.addComponent(minutesField)
															)
													.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																	.addComponent(tmpSeconds)
																	.addComponent(secondsField)
															)
													
													)
											 .addGroup(layout.createSequentialGroup()
													 		 .addComponent(newGame)
													 		 .addComponent(loadGame)
													 );
		GroupLayout.Group vertical = layout.createSequentialGroup()
										   .addComponent(title)
										   .addGroup(layout.createParallelGroup()
												          .addComponent(tmpP1)
												          .addComponent(tmpP2)
												  	)
										   .addGroup(layout.createParallelGroup()
												  		  .addComponent(whiteName)
												  		  .addComponent(blackName)
												   )
										   .addGroup(layout.createParallelGroup()
												  		  .addComponent(useExisting)
												  		  .addComponent(useExistingClone)
												   )
										   .addComponent(tmpTimeTitle)
										   .addGroup(layout.createParallelGroup()
												  		  .addComponent(tmpMinutes)
												  		  .addComponent(tmpSeconds)
											 	   )
										   .addGroup(layout.createParallelGroup()
												  		  .addComponent(minutesField)
												  		  .addComponent(secondsField)
												   )
										   .addGroup(layout.createParallelGroup()
												  		  .addComponent(newGame)
												  		  .addComponent(loadGame)
												    );
		
		
		
		
		
		
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);
		
		//This is the 'select existing username' part'
		//I define a list of usernames, and have it fill in fields when enter is clicked
		
		JScrollPane pane = new JScrollPane();
		
		DefaultListModel<String> l = new DefaultListModel<>();  
		//TODO: Make this show actual existing names
	        l.addElement("LongComplicatedUsername1"); 
	        l.addElement("LongComplicatedUsername2"); 
	        l.addElement("LongComplicatedUsername3"); 
	        l.addElement("LongComplicatedUsername4"); 
	        l.addElement("LongComplicatedUsername5"); 
	        l.addElement("LongComplicatedUsername6"); 
	        l.addElement("LongComplicatedUsername7"); 
	        l.addElement("LongComplicatedUsername8"); 
	        l.addElement("LongComplicatedUsername9"); 
	        l.addElement("LongComplicatedUsername10"); 
	        l.addElement("LongComplicatedUsername11"); 
	        l.addElement("LongComplicatedUsername11"); 
	        l.addElement("LongComplicatedUsername12"); 
	        l.addElement("LongComplicatedUsername13"); 
	        l.addElement("LongComplicatedUsername14"); 
	        l.addElement("LongComplicatedUsername15"); 
	        l.addElement("LongComplicatedUsername16"); 
	        l.addElement("LongComplicatedUsername17"); 
        JList<String> list = new JList<String>(l);
        //Will work on enter
        //TODO: make it specific to box
        list.addKeyListener(new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent evt) {}
			public void keyTyped(java.awt.event.KeyEvent evt) {}
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					whiteName.setText(list.getSelectedValue());
					blackName.setText(list.getSelectedValue());
				}
				
			}
		});
        pane.setViewportView(list);
        
        //Define action for use existing button
		useExisting.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontal)
																		.addComponent(pane));
				layout.setVerticalGroup(layout.createParallelGroup().addGroup(vertical)
																	  .addComponent(pane));
				getContentPane().setLayout(layout);
				pack();
			}
		});
		useExistingClone.addActionListener(useExisting.getActionListeners()[0]);
		
		//Redefine what the newGame button does (start the board this time)
		newGame.removeActionListener(newGame.getActionListeners()[0]);
		newGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Put the initialization stuff in here (When new game is clicked)
				//TODO: Set stuff like the timer labels that we'll need
				p1Name.setText(whiteName.getText());
				p2Name.setText(blackName.getText());
				p1Time.setText("Time: "+minutesField.getText()+" m " + secondsField.getText() +" s ");
				p2Time.setText("Time: "+minutesField.getText()+" m " + secondsField.getText() +" s ");
				initGame();
			}
		});
		//Fill in and resize
		getContentPane().setLayout(layout);
		pack();
	}
	
	//This is the actual meat of the game. Board, actions, info, etc.
	public void initGame() { 
		getContentPane().removeAll();	
		setTitle("Quoridor");
		

		
		//Defining action listeners- updates screen with components after each
		
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Creates window prompting game name and confirming if it overrides
				confirmSaveAction();
				refresh();
			}
		});
		exitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(!QuoridorController.isUpdated(fileName)) {
					confirmExitAction();
				} else {
					//Reboot
					clearActionListeners();
					initLoadScreen();
				}
				refresh();
			}
		});
		undoButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Implement Undo	
				refresh();
			}
		});
		grabButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Implement Grab Wall	
				//I figured out a way. Put the wall on in the game's candidate wall
				//And call refresh. It should work
				wall = new JPanel();
				wall.setBounds(11,423,5,85);
				wall.setSize(5, 76);
				wall.setBackground(Color.BLACK);
				getContentPane().add(wall,JLayeredPane.DRAG_LAYER);
				MouseEventListener mouseListener = new MouseEventListener(wall);
				wall.addMouseListener(mouseListener);
				wall.addMouseMotionListener(mouseListener);
				refresh();
			}
		});
		rotateButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Implement Validate Position- you can set the text of notification to tell user
				//Remember to set the notification to visible
				wall.setSize(76,5);
				refresh();
			}
		});
		validateButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: Implement Validate Position- you can set the text of notification to tell user
				//Remember to set the notification to visible
				refresh();
			}
		});
		//These are some things I'll need (component, layout, board)
		//I have a method- switchPlayerButton - that will switch the p1Turn/p2Turn
		p1Turn.setEnabled(false);
		p2Turn.setEnabled(false);
		JLabel white = new JLabel("Color: White");
		JLabel black = new JLabel("Color: Black");	
		gameLayout = new GroupLayout(getContentPane());
		board = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int width = 40;
				int height = width;
				g.setColor(new Color(201, 156, 84));
				for(int i = 0; i < 81; i++) {

					g.fillRect((i % 9)*width,
							(i/9)*height,
							width - 5, height - 5);
				}
				g.setColor(new Color(255, 164, 66));
				for(WallMove wall : QuoridorController.getWalls()) {
					if(wall.getWallDirection() == Direction.Horizontal) {
						g.drawRect(wall.getTargetTile().getRow() * 40, 
								   wall.getTargetTile().getColumn() * 40 - 5, 
								   10, 5);
						
						g.fillRect(wall.getTargetTile().getRow() * 40, 
									   wall.getTargetTile().getColumn() * 40 - 5, 
									   10, 5);
					} else {
						g.drawRect(wall.getTargetTile().getRow() * 40 - 5, 
								   wall.getTargetTile().getColumn() * 40, 
								   5, 10);

						g.fillRect(wall.getTargetTile().getRow() * 40 - 5, 
								   wall.getTargetTile().getColumn() * 40, 
								   5, 10);
					}
				}
				WallMove candidate = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();
				if(candidate != null) {
					if(candidate.getWallDirection() == Direction.Horizontal) {
						g.drawRect(candidate.getTargetTile().getRow() * 40, 
								   candidate.getTargetTile().getColumn() * 40 - 5, 
								   10, 5);
						g.fillRect(candidate.getTargetTile().getRow() * 40, 
									   candidate.getTargetTile().getColumn() * 40 - 5, 
									   10, 5);
					} else {
						g.drawRect(candidate.getTargetTile().getRow() * 40 - 5, 
								   candidate.getTargetTile().getColumn() * 40, 
								   5, 10);
						g.fillRect(candidate.getTargetTile().getRow() * 40 - 5, 
								   candidate.getTargetTile().getColumn() * 40, 
								   5, 10);
					}
				}
				
			}
		};
		board.setPreferredSize(new Dimension(40*9, 40*9));
		
		//Just throwing everything in layout in an organized way
		gameLayout.setAutoCreateGaps(true);
		gameLayout.setAutoCreateContainerGaps(true);
		GroupLayout.Group horizontal = gameLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(p2Name) 
														 			 .addComponent(p2Time)
														 			 .addComponent(p2Walls)
														  )
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(black) 
														 			 .addComponent(p2Turn)
														 )
												 .addComponent(board)
												 .addComponent(notification)
												 .addGroup(gameLayout.createSequentialGroup()
											 			 			 .addComponent(p1Name) 
											 			 			 .addComponent(p1Time)
											 			 			 .addComponent(p1Walls)
														  )
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(white) 
														 			 .addComponent(p1Turn)
														  )
												 .addGroup(gameLayout.createSequentialGroup()
											 			 			 .addComponent(grabButton) 
											 			 			 .addComponent(rotateButton)
											 			 			 .addComponent(saveButton)
											 			 			 .addComponent(undoButton)
														  )
												 .addGroup(gameLayout.createSequentialGroup()
																	 .addComponent(exitButton)
																	 .addComponent(validateButton));
		
		GroupLayout.Group vertical = gameLayout.createSequentialGroup()
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(p2Name) 
				 													.addComponent(p2Time)
				 													.addComponent(p2Walls)
				 										)
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(black) 
				 													.addComponent(p2Turn)
				 										 )
				 								.addComponent(board)
				 								.addComponent(notification)
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(p1Name) 
				 													.addComponent(p1Time)
				 													.addComponent(p1Walls)
				 										)
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(white) 
				 													.addComponent(p1Turn)
				 										)
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(grabButton) 
				 													.addComponent(rotateButton)
				 													.addComponent(saveButton)
				 													.addComponent(undoButton)
				 										)
												.addGroup(gameLayout.createParallelGroup()
																	.addComponent(exitButton)
																	.addComponent(validateButton));
		
		
		
		gameLayout.setHorizontalGroup(horizontal);
		gameLayout.setVerticalGroup(vertical);
		
		getContentPane().setLayout(gameLayout);
		pack();
	}
	
	class MouseEventListener implements MouseInputListener{
		Point origin;
		JPanel wall;
		public MouseEventListener(JPanel wall) {
			this.wall=wall;
			origin = new Point();
		}
		 @Override
		    public void mouseClicked(MouseEvent e) {}
		 
		    /**
		    * ��¼��갴��ʱ�ĵ�
		    */
		    @Override
		    public void mousePressed(MouseEvent e) {
		      origin.x = e.getX(); 
		      origin.y = e.getY();
		    }
		 
		    @Override
		    public void mouseReleased(MouseEvent e) {}
		    @Override
		    public void mouseEntered(MouseEvent e) {
		      this.wall.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		    }    
		    @Override
		    public void mouseExited(MouseEvent e) {
		      this.wall.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    }
		 
		    @Override
		    public void mouseDragged(MouseEvent e) {
		      Point p = this.wall.getLocation();
		      this.wall.setLocation(
		        p.x + (e.getX() - origin.x), 
		        p.y + (e.getY() - origin.y)); 
		    }
		 
		    @Override
		    public void mouseMoved(MouseEvent e) {}
		     
		  }
	//Not implemented, but eventually was where I was planning on doing the timer stuff.
	//I just don't know how
	public void updateView() {
		//TODO: This needs to update Time left
		refresh();
	}
	
	//This is just to refresh the screen with any changes to the components
	public void refresh() {
		if(board != null) board.repaint();
		SwingUtilities.updateComponentTreeUI(this);
		pack();
	}
	
	
	//Displays red text on the screen with a given message
	public void notifyInvalid(String message) {
		notification.setText(message);
		notification.setForeground(Color.RED);
		notification.setVisible(true);
	}
	
	//Creates a confirmation window. Idk how to pass a method, so this is specific to SaveAction
	public void confirmSaveAction() {

		confirmFrame.getContentPane().removeAll();

		GroupLayout layout = new GroupLayout(confirmFrame.getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		

		JLabel notification = new JLabel();
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");
		JButton saveButton = new JButton("Save");
		JButton exitButton = new JButton("Exit");
		JTextField gameName = new JTextField(20);
		JLabel gameNameExplain = new JLabel("Save File Name: (empty will auto-generate)");
		notification.setText("Saving will override previous save data. Do you wish to continue?");
		notification.setForeground(Color.red);
		GroupLayout.Group horiz = layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addGroup(layout.createSequentialGroup()
														.addComponent(gameNameExplain)
														.addComponent(gameName)
											     )
										.addGroup(layout.createSequentialGroup()
														.addComponent(saveButton)
														.addComponent(exitButton)	   
												 );
		GroupLayout.Group vert = layout.createSequentialGroup()
									   .addGroup(layout.createParallelGroup()
											   		   .addComponent(gameNameExplain)
											   		   .addComponent(gameName)
											    )
									   .addGroup(layout.createParallelGroup()
											   		   .addComponent(saveButton)
											   		   .addComponent(exitButton)	   
											    );
		layout.setHorizontalGroup(horiz);
		layout.setVerticalGroup(vert);
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {saveButton, exitButton});
		
		
		
		
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Save the game
				QuoridorController.savePosition(fileName);
				File f = new File(fileName);
				f.setLastModified(0); //TODO: Remove all the f stuff
				//Exit the frame
				confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		noButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				confirmFrame.remove(notification);
				layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
												.addGroup(layout.createSequentialGroup()
																.addComponent(gameNameExplain)
																.addComponent(gameName))
												.addGroup(layout.createSequentialGroup()
																.addComponent(saveButton)
																.addComponent(exitButton)));
				layout.setVerticalGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup()
																.addComponent(gameNameExplain)
																.addComponent(gameName))
												.addGroup(layout.createParallelGroup()
																.addComponent(saveButton)
																.addComponent(exitButton)));
				SwingUtilities.updateComponentTreeUI(confirmFrame);
				confirmFrame.pack();
				File f = new File(fileName);
				f.setLastModified(1000000000); //TODO: Remove all the f stuff
			}
		});
		
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name = gameName.getText();
				if(name.equals("")) {
					name = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getId() + ".dat";
				} else if(name.length() <=  4 ||
						!name.substring(name.length() - 4, name.length()).equals(".dat")) {
					
					name += ".dat";
				}
				fileName = name;
				if(QuoridorController.containsFile(fileName)) {
					confirmFrame.remove(gameName);
					layout.replace(gameNameExplain, notification);
					layout.replace(saveButton, yesButton);
					layout.replace(exitButton, noButton);
					SwingUtilities.updateComponentTreeUI(confirmFrame);
					confirmFrame.pack();
				} else {	
					//Save the game
					QuoridorController.savePosition(fileName);
					File f = new File(fileName); 
					f.setLastModified(0);
					//Exit the frame
					confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		
		exitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Exit the frame
				confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		
		confirmFrame.getContentPane().setLayout(layout);
		confirmFrame.pack();
		confirmFrame.setVisible(true);
		
	}
	public void confirmExitAction() {
		confirmFrame.getContentPane().removeAll();
		JLabel notification = new JLabel("You have unsaved data. Do you wish to continue?");
		notification.setForeground(Color.red);
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Reboot
				clearActionListeners();
				initLoadScreen();
				//Exit the frame
				confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		JButton noButton = new JButton("No");
		noButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Exit the frame
				confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		GroupLayout layout = new GroupLayout(confirmFrame.getContentPane());
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(notification)
										.addGroup(layout.createSequentialGroup()
												  .addComponent(yesButton)
												  .addComponent(noButton)	   
																	   ));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(notification)
				.addGroup(layout.createParallelGroup()
							.addComponent(yesButton)
							.addComponent(noButton)	   
							   ));
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {yesButton, noButton});
		confirmFrame.getContentPane().setLayout(layout);
		confirmFrame.pack();
		confirmFrame.setVisible(true);
	}
	
	
	//This will clear the action listers assigned to the various buttons
	private void clearActionListeners() {
		//Clear action listeners
		//TODO: Remove all the ifs once all button actions are implemented
		if(newGame.getActionListeners().length > 0)newGame.removeActionListener(newGame.getActionListeners()[0]);
		if(loadGame.getActionListeners().length > 0)loadGame.removeActionListener(loadGame.getActionListeners()[0]);
		if(saveButton.getActionListeners().length > 0)saveButton.removeActionListener(saveButton.getActionListeners()[0]);
		if(exitButton.getActionListeners().length > 0)exitButton.removeActionListener(exitButton.getActionListeners()[0]);
		if(grabButton.getActionListeners().length > 0)grabButton.removeActionListener(grabButton.getActionListeners()[0]);
		if(rotateButton.getActionListeners().length > 0)rotateButton.removeActionListener(rotateButton.getActionListeners()[0]);
		if(undoButton.getActionListeners().length > 0)undoButton.removeActionListener(undoButton.getActionListeners()[0]);
	}
	
	//Just toggling radio buttons
	public void switchPlayerButton() {
		notification.setVisible(false);
		if(p1Turn.isSelected()) {
			p1Turn.setSelected(false);
			p2Turn.setSelected(true);
		} else {
			p1Turn.setSelected(true);
			p2Turn.setSelected(false);
		}
	}
	
	
	public void keyTyped(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			DropWall();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {	
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {	
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {	
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
		}
	}
	public void keyPressed(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void DropWall() {
		if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
			if(QuoridorController.wallIsValid()) {
				if(p1Turn.isSelected()) {
					Integer numWalls = Integer.parseInt(p1Walls.getText().replace("Walls: ", ""));
					p1Walls.setText("Walls: " + Integer.toString(numWalls - 1));
				} else {
					Integer numWalls = Integer.parseInt(p2Walls.getText());
					p2Walls.setText(Integer.toString(numWalls - 1));
				}
				QuoridorController.dropWall();
				switchPlayerButton();
				refresh();
			} else {
				notifyInvalid("Invalid Wall Placement");
				refresh();
			}
		}
	}

}
