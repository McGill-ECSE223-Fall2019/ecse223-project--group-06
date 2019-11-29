
package ca.mcgill.ecse223.quoridor.view;


import java.awt.BasicStroke;
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
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.MoveDirection;
import ca.mcgill.ecse223.quoridor.controller.PawnBehavior.PawnSM;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.StepMove;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public class QuoridorView extends JFrame{
	private static final long serialVersionUID = -4426310869335015542L;
	
	public PawnBehavior white;
	public PawnBehavior black; 
	public JButton newGame = new JButton("New Game");
	public JButton loadGame = new JButton("Load Game");
	public JButton replayGame = new JButton("Replay");
	private JLabel title = new JLabel("Quoridor");
	
	//Load screen vars
	public JTextField whiteName = new JTextField(20);
	public JTextField blackName = new JTextField(20);
	public JTextField minutesField = new JTextField(2);
	public JTextField secondsField = new JTextField(2);
	public JButton useExistingWhite = new JButton("Use existing names");
	public JButton useExistingBlack = new JButton("Use existing names");
	public String userSelecting = "white";
	public JList<String> userList;
	public JScrollPane filePane = new JScrollPane();
		
	public JLabel p1Name = new JLabel();
	public JLabel p2Name = new JLabel();
	private JLabel p1Time = new JLabel();
	private JLabel p2Time = new JLabel();
	private JLabel p1Walls = new JLabel("Walls: 10");
	private JLabel p2Walls = new JLabel("Walls: 10");
	public JRadioButton p1Turn = new JRadioButton("White Turn", true); //Don't put an action listener on this!
	public JRadioButton p2Turn = new JRadioButton("Black Turn", false); //                ||
	public JLabel notification = new JLabel(); //To use for any errors, make sure it's being cleared though
	public JLabel explanation = new JLabel("<html><center>Press 'g' to grab a wall"
										+  "<br>Or press 'm' to move</center></html>", SwingConstants.CENTER);
	public JLabel roundNum = new JLabel("Round: 1");
	public JLabel moveNum = new JLabel("Move: 0");
	
	public JFrame confirmFrame = new JFrame("Confirmation");
	public JButton saveButton = new JButton("Save");
	public JButton undoButton = new JButton("Undo");
	private JButton exitButton = new JButton("Exit");
	public JButton rotateButton=new JButton("Rotate Wall");
	public JButton startPlayerMoveButton = new JButton("Player Move");
	public JButton grabButton = new JButton("Grab Wall");
	public JButton moveButton = new JButton("Move Pawn");
	public JButton validateButton = new JButton("Validate Position");
	public JButton continueButton;
	public JButton resignButton = new JButton("Resign");
	
	public JPanel board;
	private MouseListener boardMouseListener;
	private MouseInputListener wallMouseListener;
	public JPanel wall;
	private Point origin;
	private GroupLayout gameLayout;
	public String fileName; //Just used to store save file name- eclipse get angry otherwise
	public Timer whiteTimer;
	public int whiteSeconds;
	public Timer blackTimer;
	public int blackSeconds;
	
	public boolean[] outlineTile = new boolean[81];
	
	//First screen user sees, just title and two buttons
	public void initLoadScreen() {
		getContentPane().removeAll();
		Arrays.fill(outlineTile, false);
		
		setTitle("Quoridor Application");
		title.setText("Quoridor");
		title.setFont(new Font("Serif", Font.BOLD, 80));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		GroupLayout.Group horizontal = layout.createParallelGroup(GroupLayout.Alignment.CENTER)
											.addComponent(title)
											.addComponent(newGame)
											.addComponent(loadGame)
											.addComponent(replayGame);
		GroupLayout.Group vertical = layout.createSequentialGroup()
				  						   .addComponent(title)
				  						   .addComponent(newGame)
				  						   .addComponent(loadGame)
				  						   .addComponent(replayGame);
		
		
		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);
		
		DefaultListModel<String> l = new DefaultListModel<>(); 
		
		File dir = new File(System.getProperty("user.dir"));
		
		File[] saveFiles = dir.listFiles();
		String name;
		if(saveFiles != null) {
			for(File f : saveFiles) {
				name = f.getName();
				if( name.length() > 4 && (name.substring(name.length() - 4, name.length()).equals(".dat") 
					||name.substring(name.length() - 4, name.length()).equals(".mov"))) {
					l.addElement(f.getName());
				}
				
			}
		}
		
		newGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				initSetParams(); //Go to intialize game screen
			}
		});

		JList<String> fileList = new JList<String>(l);
	    //Will work on enter
		
		replayGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//TODO: This won't work
				
				if(!getContentPane().contains(new Point(500, 50))) {
					explanation.setText("<html>Press Enter to Load <br>Press 'R' for Replay</html>");
					explanation.setVisible(true);

					layout.setHorizontalGroup(layout.createSequentialGroup()
													.addGroup(layout.createParallelGroup(Alignment.CENTER)
																	.addGroup(horizontal)
																	.addComponent(explanation))
													.addComponent(filePane));
					layout.setVerticalGroup(layout.createParallelGroup()
												 .addGroup(layout.createSequentialGroup()
														 		.addGroup(vertical)
														 		.addComponent(explanation))
												 .addComponent(filePane));
					getContentPane().setLayout(layout);
					pack();
					return;
				}
				if(fileList.getSelectedValuesList().size() == 0) {
					fileList.setSelectedIndex(0);
				}
				if(fileList.getSelectedValue() == null) return;
				
				
				if(QuoridorController.startReplay(fileList.getSelectedValue())) {
					
					fileName = fileList.getSelectedValue();
					p1Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
					p2Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
					
					
					
					whiteSeconds = 60*10;
					blackSeconds = 60*10;
					p1Time.setText("Time: "+10+" m " + 0 +" s ");
					p2Time.setText("Time: "+10+" m " + 0 +" s ");
					
					QuoridorController.setTotaltime(10, 0);
					
					
					initReplay();
					
					board.requestFocusInWindow();
					fileList.removeKeyListener(fileList.getKeyListeners()[0]);
					
				} else {
					notifyInvalid("Replay File Error- Invalid Position");
					layout.setHorizontalGroup(layout.createSequentialGroup()
													.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																	.addGroup(horizontal)
																	.addComponent(notification))
													.addComponent(filePane));
					layout.setVerticalGroup(layout.createParallelGroup()
											.addGroup(layout.createSequentialGroup()
															.addGroup(vertical)
															.addComponent(notification))
											.addComponent(filePane));

					getContentPane().setLayout(layout);
					pack();
				}

			}
		});
		
		loadGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(!getContentPane().contains(new Point(500, 50))) {
					explanation.setText("<html>Press Enter to Load <br>Press 'R' for Replay</html>");
					

					layout.setHorizontalGroup(layout.createSequentialGroup()
													.addGroup(layout.createParallelGroup(Alignment.CENTER)
																	.addGroup(horizontal)
																	.addComponent(explanation))
													.addComponent(filePane));
					layout.setVerticalGroup(layout.createParallelGroup()
												 .addGroup(layout.createSequentialGroup()
														 		.addGroup(vertical)
														 		.addComponent(explanation))
												 .addComponent(filePane));
					explanation.setVisible(true);
					getContentPane().setLayout(layout);
					pack();
					return;
				}
				
				if(QuoridorController.isEnded(fileList.getSelectedValue())) {
					replayGame.doClick();
					return;
				}
				if(QuoridorController.loadGame(fileList.getSelectedValue())) {
					fileName = fileList.getSelectedValue();
					File f = new File(fileName); 
					f.setLastModified(0);
					
					p1Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
					p2Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
					
					whiteSeconds = 60*10;
					blackSeconds = 60*10;
					p1Time.setText("Time: "+10+" m " + 0 +" s ");
					p2Time.setText("Time: "+10+" m " + 0 +" s ");
					
					p1Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
					p2Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
					
					
					QuoridorController.setTotaltime(10, 0);
					
					initGame();
					
					if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
							.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
								p2Turn.setSelected(true);
								p1Turn.setSelected(false);
							}
					
					for(WallMove w : QuoridorController.getWalls()) {
						
						JPanel newWall = new JPanel();
						int row = w.getTargetTile().getRow();
				    	int col = w.getTargetTile().getColumn();
					    refresh(); 
					    if(w.getWallDirection() == Direction.Vertical) {
					    	
					    	newWall.setSize(5, 75);
					    	newWall.setLocation( 
								     board.getX() - 5 + col*40, 
								     board.getY() + row * 40 - 40);
					      } else {
					    	  newWall.setSize(75, 5);
					    	  newWall.setLocation( 
								        board.getX() + col*40 - 40, 
								        board.getY() - 5 + row * 40);
					      }
						newWall.setBackground(Color.BLACK);
						getContentPane().add(newWall);
						
					}
					board.requestFocusInWindow();
					fileList.removeKeyListener(fileList.getKeyListeners()[0]);
					
				} else {
					notifyInvalid("Load File Error- Invalid Position");
					layout.setHorizontalGroup(layout.createSequentialGroup()
													.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																	.addGroup(horizontal)
																	.addComponent(notification))
													.addComponent(filePane));
					layout.setVerticalGroup(layout.createParallelGroup()
											.addGroup(layout.createSequentialGroup()
															.addGroup(vertical)
															.addComponent(notification))
											.addComponent(filePane));
					explanation.setVisible(false);
					getContentPane().setLayout(layout);
					pack();
				}
			}
		});	

	    
	    fileList.addKeyListener(new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent evt) {}
			public void keyTyped(java.awt.event.KeyEvent evt) {}
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
						loadGame.doClick();
					}
				else if(evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					confirmDeleteFile(fileList.getSelectedValue());
				}
				else if(evt.getKeyCode() == KeyEvent.VK_R) {
					replayGame.doClick();
				}
				
			}
		});
        filePane.setViewportView(fileList);
		
		
		layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {newGame, loadGame});
		layout.linkSize(SwingConstants.VERTICAL, new java.awt.Component[] {newGame, loadGame});		
		
		this.getContentPane().setBackground(new Color(191, 222, 217, 255));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	//Initialize the game screen. This is where usernames and think time is set
	public void initSetParams() {
		
		getContentPane().removeAll();
		
		//All the components to be placed on the window
		
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
																			.addComponent(useExistingWhite)
																	)
															.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																			.addComponent(tmpP2)
																			.addComponent(blackName)
																			.addComponent(useExistingBlack)
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
												  		  .addComponent(useExistingWhite)
												  		  .addComponent(useExistingBlack)
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
		for(User r : QuoridorApplication.getQuoridor().getUsers()) {
			l.addElement(r.getName());
		}
		l.addElement("ExampleUserName");
		l.addElement("JoeBoe");
		l.addElement("Corridor");
		l.addElement("Rodirroc");
		l.addElement("Xx_EpicGamer_xX");
		//TODO: Make it show actual user names
		
		
        userList = new JList<String>(l);
        //Will work on enter
        
        userList.addKeyListener(new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent evt) {}
			public void keyTyped(java.awt.event.KeyEvent evt) {}
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
					if(userSelecting.equals("white")) {
						whiteName.setText(userList.getSelectedValue());
					} else {
						blackName.setText(userList.getSelectedValue());
					}

				}
				
			}
		});
        
        
		
		DefaultListModel<String> listoo = new DefaultListModel<>(); 
	
		File dir = new File(System.getProperty("user.dir"));
		
		File[] saveFiles = dir.listFiles();
		String name;
		if(saveFiles != null) {
			for(File f : saveFiles) {
				name = f.getName();
				if( name.length() > 4 && (name.substring(name.length() - 4, name.length()).equals(".dat") 
					||name.substring(name.length() - 4, name.length()).equals(".mov"))) {
					listoo.addElement(f.getName());
				}
				
			}
		}
        
        //Load Game Button Definition

		JList<String> fileList = new JList<String>(listoo);
	    //Will work on enter
	    
	    fileList.addKeyListener(new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent evt) {}
			public void keyTyped(java.awt.event.KeyEvent evt) {}
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
						if(QuoridorController.isEnded(fileList.getSelectedValue())) {
							replayGame.doClick();
							return;
						}
						if(QuoridorController.loadGame(fileList.getSelectedValue())) {
							fileName = fileList.getSelectedValue();
							File f = new File(fileName); 
							f.setLastModified(0);
							
							p1Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
							p2Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
							
							whiteSeconds = 60*10;
							blackSeconds = 60*10;
							p1Time.setText("Time: "+10+" m " + 0 +" s ");
							p2Time.setText("Time: "+10+" m " + 0 +" s ");
							
							p1Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
							p2Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
							
							QuoridorController.setTotaltime(10, 0);
							
							initGame();
							if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
									.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
										p2Turn.setSelected(true);
										p1Turn.setSelected(false);
									}
							for(WallMove w : QuoridorController.getWalls()) {
								
								JPanel newWall = new JPanel();
								int row = w.getTargetTile().getRow();
						    	int col = w.getTargetTile().getColumn();
							    refresh(); 
							    if(w.getWallDirection() == Direction.Vertical) {
							    	
							    	newWall.setSize(5, 75);
							    	newWall.setLocation( 
										     board.getX() - 5 + col*40, 
										     board.getY() + row * 40 - 40);
							      } else {
							    	  newWall.setSize(75, 5);
							    	  newWall.setLocation( 
										        board.getX() + col*40 - 40, 
										        board.getY() - 5 + row * 40);
							      }
								newWall.setBackground(Color.BLACK);
								getContentPane().add(newWall);
								
							}
							board.requestFocusInWindow();
							fileList.removeKeyListener(fileList.getKeyListeners()[0]);
						} else {
							notifyInvalid("Load File Error- Invalid Position");
							layout.setHorizontalGroup(layout.createSequentialGroup()
															.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																			.addGroup(horizontal)
																			.addComponent(notification))
															.addComponent(pane));
							layout.setVerticalGroup(layout.createParallelGroup()
													.addGroup(layout.createSequentialGroup()
																	.addGroup(vertical)
																	.addComponent(notification))
													.addComponent(pane));

							getContentPane().setLayout(layout);
							pack();
						}
	
						
					}
			}
		});  
        loadGame.removeActionListener(loadGame.getActionListeners()[0]);
		loadGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontal)
						.addComponent(pane));
				layout.setVerticalGroup(layout.createParallelGroup().addGroup(vertical)
					  .addComponent(pane));
				pane.setViewportView(fileList);
				getContentPane().setLayout(layout);
				pack();
			}
		});	
        
        
        
        //Define action for use existing button
		useExistingWhite.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontal)
																		.addComponent(pane));
				layout.setVerticalGroup(layout.createParallelGroup().addGroup(vertical)
																	  .addComponent(pane));
				getContentPane().setLayout(layout);
				pane.setViewportView(userList);
				pack();
				userSelecting = "white";
			}
		});
		useExistingBlack.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(horizontal)
																		.addComponent(pane));
				layout.setVerticalGroup(layout.createParallelGroup().addGroup(vertical)
																	  .addComponent(pane));
				getContentPane().setLayout(layout);
				pane.setViewportView(userList);
				pack();
				userSelecting = "black";
			}
		});
		
		//Redefine what the newGame button does (start the board this time)
		newGame.removeActionListener(newGame.getActionListeners()[0]);
		newGame.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				userList.removeKeyListener(userList.getKeyListeners()[0]);
				p1Name.setText(whiteName.getText());
				p2Name.setText(blackName.getText());
				
				if(!QuoridorController.ExistingUserName(whiteName.getText())) {
					QuoridorController.createUser(whiteName.getText());
				} else {
					confirmExistingName();
					return;
				}
				if(!QuoridorController.ExistingUserName(blackName.getText())) {
					QuoridorController.createUser(blackName.getText());
				} else {
					confirmExistingName();
					return;
				}
				QuoridorController.startGame();
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setUser(QuoridorController.findUserName(whiteName.getText()));
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setUser(QuoridorController.findUserName(blackName.getText()));
				try {
					int minutes = Integer.parseInt(minutesField.getText());
					int seconds = Integer.parseInt(secondsField.getText());
					whiteSeconds = 60*minutes + seconds;
					blackSeconds = 60*minutes + seconds;
					QuoridorController.setTotaltime(minutes, seconds);
					p1Time.setText("Time: "+minutes+" m " + seconds +" s ");
					p2Time.setText("Time: "+minutes+" m " + seconds +" s ");
				} catch (Exception e) {
					whiteSeconds = 60*10;
					blackSeconds = 60*10;
					p1Time.setText("Time: "+10+" m " + 0 +" s ");
					p2Time.setText("Time: "+10+" m " + 0 +" s ");
					QuoridorController.setTotaltime(10, 0);
				}
				
				QuoridorController.initializeBoard();
				initGame();
			}
		});
		//Fill in and resize
		getContentPane().setLayout(layout);
		pack();
	}
	
	//This is the actual meat of the game. Board, actions, info, etc.
	public void initGame() { 
		explanation.setText("<html><center>Press 'g' to grab a wall"
										+  "<br>Or press 'm' to move</center></html>");
		white = new PawnBehavior(MoveDirection.North);
		black = new PawnBehavior(MoveDirection.North);
		white.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
		black.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
		explanation.setBorder(BorderFactory.createLineBorder(new Color(94, 151, 219, 255)));
		notification.setVisible(false);
		QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.Running);
		getContentPane().removeAll();	
		setTitle("Quoridor");
		
		
		
		
		whiteTimer = QuoridorController.runwhiteclock(this);
		blackTimer = QuoridorController.runblackclock(this);
		
		
		
		wall = new JPanel();
		boardMouseListener = new MouseListener() {
			
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				int col = e.getX() / 40; 
				col++;
				int row = e.getY() / 40; 
				row++;
				//Tile of current player to move
				Tile pToMove = QuoridorController.getCurrentPlayerTile();
				//Should only be outlined if in player move mode
				PawnBehavior toMove = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
						QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()) ? 
								white : black;
				if(toMove.getPawnSM() == PawnSM.PlayerMove) {
					if(outlineTile[(col-1) + (row-1) * 9]) {

						int rChange = row - pToMove.getRow();
						int cChange = col - pToMove.getColumn();
						if(Math.abs(rChange) == 2) rChange /=2;
						if(Math.abs(cChange) == 2) cChange /=2;
						if(rChange > 0) {
							if(cChange > 0) movePlayer(MoveDirection.SouthEast);
							else if (cChange < 0) movePlayer(MoveDirection.SouthWest);
							else movePlayer(MoveDirection.South);
						} else if(rChange < 0) {
							if(cChange > 0) movePlayer(MoveDirection.NorthEast);
							else if (cChange < 0) movePlayer(MoveDirection.NorthWest);
							else movePlayer(MoveDirection.North);
						} else {
							if(cChange > 0) movePlayer(MoveDirection.East);
							else movePlayer(MoveDirection.West);
						}

					} else {
						notifyInvalid("Invalid Player Move");
					}
				}else if(QuoridorController.findTile(row, col).equals(pToMove)) {
					moveButton.doClick();
				}
			}
			
		};
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Creates window prompting game name and confirming if it overrides
				if(!QuoridorController.containsFile(fileName)) {
					confirmSaveAction();
				} else {
					QuoridorController.savePosition(fileName);
					File f = new File(fileName); 
					f.setLastModified(0);
					notifyValid("Saved Successfully");
				}
				
				refresh();
				board.requestFocusInWindow();
			}
		});
		
		if(exitButton.getActionListeners().length == 1) exitButton.removeActionListener(exitButton.getActionListeners()[0]);
		exitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(!QuoridorController.isUpdated(fileName)) {
					confirmExitAction();
				} else {
					//Reboot
					QuoridorController.stopwhiteclock(whiteTimer);
					QuoridorController.stopblackclock(blackTimer);
					fileName = null;
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
				board.requestFocusInWindow();
			}
		});
		
		
		grabButton.addActionListener(new java.awt.event.ActionListener() {
			
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				notification.setVisible(false);
				if(QuoridorController.grabWall()) {
					
					if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
							QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
						white.initGrab(); //Update state machines
					} else {
						black.initGrab();
					}
					
					
					if(wall != null) getContentPane().remove(wall);
					wall = new JPanel();
					wall.setBounds(47, 60, 5, 75);
					wall.setBackground(Color.BLACK);
					wall.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
					
					if(board.getMouseMotionListeners().length == 0) {
						board.addMouseListener(wallMouseListener);
						board.addMouseMotionListener(wallMouseListener);
					}
					if(board.getKeyListeners().length == 0) {
						System.out.println("This should never happen");
					}
					
					
					
					getContentPane().add(wall,JLayeredPane.DRAG_LAYER);
					
					 wall.setLocation( 
						        board.getX() - 5 + QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn() *40, 
						        board.getY() + QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow() * 40 - 40);
					/*
					if(p1Turn.isSelected()) {
						Integer numWalls = Integer.parseInt(p1Walls.getText().replace("Walls: ", ""));
						p1Walls.setText("Walls: " + Integer.toString(numWalls - 1));
					} else {
						Integer numWalls = Integer.parseInt(p2Walls.getText().replace("Walls: ", ""));
						p2Walls.setText("Walls: " + Integer.toString(numWalls - 1));
					}
					*/
					 //TODO: See if this works
					p1Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
					p2Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());

					explanation.setText("<html><center>Click and Drag to move the wall - or press enter to Drop"
							+ 			"<br>Press 'r' or select the Rotate Button to rotate</center></html>");
					explanation.setVisible(true);
					Arrays.fill(outlineTile, false);
					board.requestFocusInWindow();
					refresh();
				} else {
					if(QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate()) {
						notifyInvalid("Can only grab 1 wall at a time");
					}
					else {
						notifyInvalid("No walls in stock");
					}
				}
		}});

		moveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				if(wall != null) {
					getContentPane().remove(wall);

					if(p1Turn.isSelected()) {
						Integer numWalls = Integer.parseInt(p1Walls.getText().replace("Walls: ", ""));
						p1Walls.setText("Walls: " + Integer.toString(numWalls + 1));
					} else {
						Integer numWalls = Integer.parseInt(p2Walls.getText().replace("Walls: ", ""));
						p2Walls.setText("Walls: " + Integer.toString(numWalls + 1));
					}
					
					QuoridorController.undoGrabWall();
					
					wall = null;
				}
				
				
				QuoridorController.findAllowedTiles(outlineTile);
				refresh();
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
						QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
					white.initMove();
				} else {
					black.initMove();
				}
				//Mmmhmm
				QuoridorApplication.getQuoridor().getCurrentGame().setMoveMode(MoveMode.PlayerMove);
				
				explanation.setText("Select a highlighted tile to move to that position!");
				explanation.setVisible(true);
				board.requestFocusInWindow();
				pack();
			}
		});
		rotateButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				board.requestFocusInWindow();
				
				RotateWall();
				
			}
		});
		validateButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(!QuoridorController.validatePosition()) {
					notifyInvalid("Invalid Quoridor Position");
					board.requestFocusInWindow();
					refresh();
					return;
				}
				
				if(!QuoridorController.pathExists(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
					if(!QuoridorController.pathExists(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
						notifyInvalid("Both Players' Quoridor Positions Are Invalid");
					} else {
						notifyInvalid("Invalid White Quoridor Position");
					}
				} else if(!QuoridorController.pathExists(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
					notifyInvalid("Invalid Black Quoridor Position");
				} else {
					notifyValid("Quoridor Position is Valid");
				}
				
				board.requestFocusInWindow();
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
			private static final long serialVersionUID = 4202341228982165L;
			@Override
			public void paintComponent(Graphics gIn) {
				Graphics2D g = (Graphics2D) gIn;

				int width = 40;
				int height = width;
				g.setColor(new Color(201, 156, 84));
				g.setStroke(new BasicStroke(2));
				for(int i = 0; i < 81; i++) {
					if(outlineTile[i] ) {
						
						
						g.fillRect((i % 9)*width,
								(i/9)*height,
								width - 5, height - 5);
						
						
						g.setColor(new Color(0, 255, 0));
						g.drawRect((i % 9)*width,
									(i/9)*height,
									width - 5, height - 5);
						g.setColor(new Color(201, 156, 84));
					} else {
						g.fillRect((i % 9)*width,
								(i/9)*height,
								width - 5, height - 5);
					}
				}
				g.setStroke(new BasicStroke(1));
				
				PlayerPosition whitePos;
				PlayerPosition blackPos;
				if(QuoridorApplication.getQuoridor().getCurrentGame() != null) {
					whitePos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
					blackPos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
				} else {
					whitePos = null;
					blackPos = null;
				}
				if(whitePos != null) {
					g.setColor(new Color(255, 255, 255));
					g.fillOval( whitePos.getTile().getColumn() * 40 - 35, 
								whitePos.getTile().getRow() * 40 - 35, 
								25, 25);
				}
				if(blackPos != null) {
					g.setColor(new Color(0, 0, 0));
					g.fillOval( blackPos.getTile().getColumn() * 40 - 35, 
								blackPos.getTile().getRow() * 40 - 35, 
								25, 25);
				}
				
			}
		};
		
		board.setPreferredSize(new Dimension(40*9, 40*9));
		board.setFocusable(true);
		

		//Defining action listeners- updates screen with components after each
		board.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
					public void keyReleased(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER) {
							DropWall();
						} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
							if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.PlayerMove) {
								movePlayer(MoveDirection.North);
							} else if (QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
								
								int row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
						    	int col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
						    	if(row == 1) return;
								if(!QuoridorController.moveWall(QuoridorController.findTile(row - 1, col))) {
						    		  return;
						    	 }

							     if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
							    	  wall.setLocation( 
										        board.getX() - 5 + col*40, 
										        board.getY() + (row-1) * 40 - 40);
							      } else {
							    	  wall.setLocation( 
										        board.getX() + col*40 - 40, 
										        board.getY() - 5 + (row-1) * 40);
							      }
							}
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
							if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.PlayerMove) {
								movePlayer(MoveDirection.South);
							}else if (QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
								int row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
						    	int col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
						    	if(row == 8) return;
								if(!QuoridorController.moveWall(QuoridorController.findTile(row + 1, col))) {
						    		  return;
						    	 }

							     if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
							    	  wall.setLocation( 
										        board.getX() - 5 + col*40, 
										        board.getY() + (row+1) * 40 - 40);
							      } else {
							    	  wall.setLocation( 
										        board.getX() + col*40 - 40, 
										        board.getY() - 5 + (row+1) * 40);
							      }
							}
						} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
							if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.PlayerMove) {
								movePlayer(MoveDirection.East);
							}else if (QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
								
								int row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
						    	int col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
						    	if(col == 8) return;
								if(!QuoridorController.moveWall(QuoridorController.findTile(row, col+1))) {
						    		  return;
						    	 }

							     if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
							    	  wall.setLocation( 
										        board.getX() - 5 + (col+1)*40, 
										        board.getY() + (row) * 40 - 40);
							      } else {
							    	  wall.setLocation( 
										        board.getX() + (col+1)*40 - 40, 
										        board.getY() - 5 + (row) * 40);
							      }
							}
						} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
							if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.PlayerMove) {
								movePlayer(MoveDirection.West);
							}else if (QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
								
								int row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
						    	int col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
						    	if(col == 1) return;
								if(!QuoridorController.moveWall(QuoridorController.findTile(row, col-1))) {
						    		  return;
						    	 }

							     if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
							    	  wall.setLocation( 
										        board.getX() - 5 + (col-1)*40, 
										        board.getY() + (row) * 40 - 40);
							      } else {
							    	  wall.setLocation( 
										        board.getX() + (col-1)*40 - 40, 
										        board.getY() - 5 + (row) * 40);
							      }
							}
						} else if (e.getKeyCode() == KeyEvent.VK_R) {
							RotateWall();
						} else if (e.getKeyCode() == KeyEvent.VK_G) {
							grabButton.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_M) {
							moveButton.doClick();
						}
						
					}
				});	
		wallMouseListener = new MouseInputListener() {
			@Override
		    public void mouseClicked(MouseEvent e) {}
		    @Override
		    public void mousePressed(MouseEvent e) {
		      origin.x = e.getX(); 
		      origin.y = e.getY();
		      
		    		if(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus()
				    		  == GameStatus.Running &&
				    		  QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
				    	  
					    	board.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				      }
		    		
		    }
		 
		    @Override
		    public void mouseReleased(MouseEvent e) {
		    	board.requestFocusInWindow();
		    	board.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    }
		    @Override
		    public void mouseEntered(MouseEvent e) {
		    	
		    }    
		    @Override
		    public void mouseExited(MouseEvent e) {}
		 
		    @Override
		    public void mouseDragged(MouseEvent e) {

		      int relX = e.getXOnScreen() - board.getX() - 23;
	    	  int relY = e.getYOnScreen() - board.getY() - 45;
		      
		      if(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus()
		    		  == GameStatus.Running &&
		    		  QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
		    	  int row = relY / 40 + 1;
		    	  int col = relX / 40 + 1;
		    	  if(row < 1 || row > 9) return;
		    	  if(col < 1 || col > 9) return;
		    	  if(!QuoridorController.moveWall(QuoridorController.findTile(row, col))) {
		    		  return;
		    	  }
		    	  row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
		    	  col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
			     
			      if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
			    	  wall.setLocation( 
						        board.getX() - 5 + col*40, 
						        board.getY() + row * 40 - 40);
			      } else {
			    	  wall.setLocation( 
						        board.getX() + col*40 - 40, 
						        board.getY() - 5 + row * 40);
			      }
			      refresh(); 
			     
		      }
		    }
		 
		    @Override
		    public void mouseMoved(MouseEvent e) { 
		    	
		    	
		    }
		};
		
		
		origin = new Point(board.getX(), board.getY());
		board.addMouseListener(boardMouseListener);
		
		board.requestFocusInWindow();
		
		p1Turn.setBackground(new Color(191, 222, 217, 255));
		p2Turn.setBackground(new Color(191, 222, 217, 255));
		//Just throwing everything in layout in an organized way
		gameLayout.setAutoCreateGaps(true);
		gameLayout.setAutoCreateContainerGaps(true);
		GroupLayout.Group horizontal = gameLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(p2Name) 
														 			 .addComponent(p2Time)
														 			 .addComponent(p2Walls))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(black) 
														 			 .addComponent(p2Turn))
												 .addComponent(board)
												 .addComponent(notification)
												 .addGroup(gameLayout.createSequentialGroup()
											 			 			 .addComponent(p1Name) 
											 			 			 .addComponent(p1Time)
											 			 			 .addComponent(p1Walls))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(white) 
														 			 .addComponent(p1Turn))
												 .addGroup(gameLayout.createSequentialGroup()
		 													.addComponent(grabButton) 
		 													.addComponent(moveButton))
												 .addGroup(gameLayout.createSequentialGroup()
		 													.addComponent(rotateButton)
		 													.addComponent(undoButton)
		 													.addComponent(validateButton))
												 .addGroup(gameLayout.createSequentialGroup()
															.addComponent(saveButton)
															.addComponent(exitButton))
												 .addComponent(explanation);
		
		GroupLayout.Group vertical = gameLayout.createSequentialGroup()
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(p2Name) 
				 													.addComponent(p2Time)
				 													.addComponent(p2Walls))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(black) 
				 													.addComponent(p2Turn))
				 								.addComponent(board)
				 								.addComponent(notification)
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(p1Name) 
				 													.addComponent(p1Time)
				 													.addComponent(p1Walls))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(white) 
				 													.addComponent(p1Turn))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(grabButton) 
				 													.addComponent(moveButton))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(rotateButton)
				 													.addComponent(undoButton)
				 													.addComponent(validateButton))
												.addGroup(gameLayout.createParallelGroup()
																	.addComponent(saveButton)
																	.addComponent(exitButton))
												.addComponent(explanation);
		
		
		
		gameLayout.setHorizontalGroup(horizontal);
		gameLayout.setVerticalGroup(vertical);
		
		getContentPane().setLayout(gameLayout);
		pack();
	}
	

	
	
	//This is the Replay Mode. Emulates board, but none of the fancier mechanics
	public void initReplay() { 
		//TODO: Make it just default to white in play here- including switching player buttons
		//& Changing current position
		
		p1Walls.setText("Walls: " +10);
		p2Walls.setText("Walls: " + 10);
		explanation.setBorder(BorderFactory.createLineBorder(new Color(94, 151, 219, 255)));
		explanation.setText("Use Arrow Keys to Step through Game");
		notification.setVisible(false);
		
		getContentPane().removeAll();	
		setTitle("Quoridor Replay");
		
		JButton stepForward = new JButton(">");
		JButton stepBackwards = new JButton("<");
		JButton jumpForward = new JButton(">>");
		JButton jumpBackwards = new JButton("<<");
		continueButton = new JButton("Continue");
		if(continueButton.getActionListeners().length > 0)continueButton.removeActionListener(continueButton.getActionListeners()[0]);
		
		roundNum.setText("Round: 1");
		moveNum.setText("Move: 0");
		roundNum.setFont(new Font(roundNum.getFont().getName(), Font.BOLD, 30));
		moveNum.setFont(new Font(moveNum.getFont().getName(), Font.BOLD, 30));
		
		
		stepForward.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				//Implement Step Forward. To add walls to the board, you
				// can see how drop wall does it (create JPanel, resize/move,
				//and add). All the moves are listed in the current games Moves
				//and were set to either WallMoves with a target Tile+direction
				//or step moves with a player to move and target tile
				
				//Be sure to update the roundNum/moveNum JLabels. 
				
				System.out.println("TODO: Step Forward- line 1302 in View");
				board.requestFocusInWindow();
				
				refresh();
			}
		});
		stepBackwards.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				//Implement Step Backwards. To remove walls form the board...
				//Well, idk. Maybe keep track of the step forward walls in an
				//ArrayList<JPanel> variable?
				//All the moves are listed in the current games Moves
				//and were set to either WallMoves with a target Tile+direction
				//or step moves with a player to move and target tile
				
				//Removing Moves will not work, and probably shouldn't be done anyways
				
				//Be sure to update the roundNum/moveNum JLabels.
				//You might even be able to use it to find which one we're on!
				
				System.out.println("TODO: Step Backwards- line 1324 in View");
				board.requestFocusInWindow();
				
				refresh();
			}
		});
		jumpForward.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				//Implement Jump Forward. To add walls to the board, you
				// can see how drop wall does it (create JPanel, resize/move,
				//and add). All the moves are listed in the current games Moves
				//and were set to either WallMoves with a target Tile+direction
				//or step moves with a player to move and target tile
				
				//Legit basically step forward but with last index every time.
				//You might be able to set the button to enabled(false) if you want
				
				//Be sure to update the roundNum/moveNum JLabels. 
				
				System.out.println("TODO: Jump Forward- line 1345 in View");
				board.requestFocusInWindow();
				
				refresh();
			}
		});
		jumpBackwards.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				//Implement Jump Backwards. 
				//See Jump Forwards for info. 
				
				//Be sure to update the roundNum/moveNum JLabels. 
				
				System.out.println("TODO: Jump Backwards- line 1360 in View");
				board.requestFocusInWindow();
				
				refresh();
			}
		});
		continueButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				//TODO: HERE IS A MARKER FOR MY SCROLL BAR
				//TODO: Fix HDC component null bug
				//Figure out why junit tests says explanation has no horizontal group
				if(QuoridorController.isEnded(fileName)) {
					notifyInvalid("Cannot continue a finished game");
					return;
				}
				
				
				//This seconds stuff hopefully shouldn't do anything
				//Psssshhhhhh innefficiency? Did you mean to say practicality?
				whiteSeconds = 60*10;
				blackSeconds = 60*10;
				p1Time.setText("Time: "+10+" m " + 0 +" s ");
				p2Time.setText("Time: "+10+" m " + 0 +" s ");
				
				QuoridorController.setTotaltime(10, 0);
				
				initGame();
				
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
						.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
							p2Turn.setSelected(true);
							p1Turn.setSelected(false);
						}
				
				//Add the moves up to this point on the board
				
				int moveNumber = Integer.parseInt(moveNum.getText().replace("Move: ", ""));
				int roundNumber = Integer.parseInt(roundNum.getText().replace("Round: ", ""));
				//int moveNumber = moveNumber + (roundNumber-1)*2; // 0-1 -> 0. 1-1 -> 1
				List<Move> moveList = QuoridorApplication.getQuoridor().getCurrentGame().getMoves();
				for(Move m : moveList) {
					//If in greater move break. If same move, check round number
					if(m.getMoveNumber() > moveNumber)  break;
					if(m.getMoveNumber() == moveNumber && m.getRoundNumber() > roundNumber) break;
					//3:2 break if 4:1  3:1 break if 3:2
					
					if(m instanceof WallMove) {
						WallMove w = (WallMove) m;
						JPanel newWall = new JPanel();
						int row = w.getTargetTile().getRow();
				    	int col = w.getTargetTile().getColumn();
					    refresh(); 
					    if(w.getWallDirection() == Direction.Vertical) {
					    	
					    	newWall.setSize(5, 75);
					    	newWall.setLocation( 
								     board.getX() - 5 + col*40, 
								     board.getY() + row * 40 - 40);
					      } else {
					    	  newWall.setSize(75, 5);
					    	  newWall.setLocation( 
								        board.getX() + col*40 - 40, 
								        board.getY() - 5 + row * 40);
					      }
						newWall.setBackground(Color.BLACK);
						getContentPane().add(newWall);
					} else {
						if(m.getPlayer().equals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()) ) {
							//White step move
							QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().setTile(m.getTargetTile());	
						} else {
							QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().setTile(m.getTargetTile());
						}
					}
					
				}
				//Add remaining moves back in stock
				QuoridorController.addReplayWallsBack(moveNumber, roundNumber);
				

				p1Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
				p2Walls.setText("Walls: " +QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
				
				
				board.requestFocusInWindow();
				refresh();
			}
		});
		
		exitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fileName = null;
				clearActionListeners();
				initLoadScreen();
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
			private static final long serialVersionUID = 4202341228982165L;
			@Override
			public void paintComponent(Graphics gIn) {
				Graphics2D g = (Graphics2D) gIn;

				int width = 40;
				int height = width;
				g.setColor(new Color(201, 156, 84));
				g.setStroke(new BasicStroke(2));
				for(int i = 0; i < 81; i++) {
					if(outlineTile[i] ) {
						
						
						g.fillRect((i % 9)*width,
								(i/9)*height,
								width - 5, height - 5);
						
						
						g.setColor(new Color(0, 255, 0));
						g.drawRect((i % 9)*width,
									(i/9)*height,
									width - 5, height - 5);
						g.setColor(new Color(201, 156, 84));
					} else {
						g.fillRect((i % 9)*width,
								(i/9)*height,
								width - 5, height - 5);
					}
				}
				g.setStroke(new BasicStroke(1));
				
				PlayerPosition whitePos;
				PlayerPosition blackPos;
				if(QuoridorApplication.getQuoridor().getCurrentGame() != null) {
					whitePos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
					blackPos = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
				} else {
					whitePos = null;
					blackPos = null;
				}
				if(whitePos != null) {
					g.setColor(new Color(255, 255, 255));
					g.fillOval( whitePos.getTile().getColumn() * 40 - 35, 
								whitePos.getTile().getRow() * 40 - 35, 
								25, 25);
				}
				if(blackPos != null) {
					g.setColor(new Color(0, 0, 0));
					g.fillOval( blackPos.getTile().getColumn() * 40 - 35, 
								blackPos.getTile().getRow() * 40 - 35, 
								25, 25);
				}
				
			}
		};
		
		board.setPreferredSize(new Dimension(40*9, 40*9));
		board.setFocusable(true);
		

		//Defining action listeners- updates screen with components after each
		board.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {}
					public void keyPressed(KeyEvent e) {}
					public void keyReleased(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER) {
							//
						} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
							stepForward.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
							stepBackwards.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
							stepForward.doClick();
						} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
							stepBackwards.doClick();
						}	
					}
				});	
		
		board.requestFocusInWindow();
		
		p1Turn.setBackground(new Color(191, 222, 217, 255));
		p2Turn.setBackground(new Color(191, 222, 217, 255));
		//Just throwing everything in layout in an organized way
		gameLayout.setAutoCreateGaps(true);
		gameLayout.setAutoCreateContainerGaps(true);
		GroupLayout.Group horizontal = gameLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
												 .addGroup(gameLayout.createSequentialGroup()
																	.addComponent(roundNum)
																	.addComponent(moveNum))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(p2Name)
														 			 .addComponent(p2Walls))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(black) 
														 			 .addComponent(p2Turn))
												 .addComponent(board)
												 .addComponent(notification)
												 .addGroup(gameLayout.createSequentialGroup()
											 			 			 .addComponent(p1Name) 
											 			 			 .addComponent(p1Walls))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(white) 
														 			 .addComponent(p1Turn))
												 .addGroup(gameLayout.createSequentialGroup()
														 			 .addComponent(jumpBackwards)
														 			.addComponent(stepBackwards)
														 			.addComponent(stepForward)
														 			.addComponent(jumpForward))
												 .addGroup(gameLayout.createSequentialGroup()			
											 						 .addComponent(exitButton)
											 						 .addComponent(continueButton))
												 .addComponent(explanation);
		
		GroupLayout.Group vertical = gameLayout.createSequentialGroup()
												.addGroup(gameLayout.createParallelGroup()
														.addComponent(roundNum)
														.addComponent(moveNum))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(p2Name) 
				 													.addComponent(p2Walls))
				 								.addGroup(gameLayout.createParallelGroup()
				 													.addComponent(black) 
				 													.addComponent(p2Turn))
				 								.addComponent(board)
				 								.addComponent(notification)
				 								.addGroup(gameLayout.createParallelGroup()
								 			 			 .addComponent(p1Name) 
								 			 			 .addComponent(p1Walls))
				 								.addGroup(gameLayout.createParallelGroup()
											 			 .addComponent(white) 
											 			 .addComponent(p1Turn))
				 								.addGroup(gameLayout.createParallelGroup()
											 			 .addComponent(jumpBackwards)
											 			.addComponent(stepBackwards)
											 			.addComponent(stepForward)
											 			.addComponent(jumpForward))
				 								.addGroup(gameLayout.createParallelGroup()			
								 						 .addComponent(exitButton)
								 						 .addComponent(continueButton))
												.addComponent(explanation);
		
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
			
			origin = new Point(board.getX(), board.getY());
		}
		 @Override
		    public void mouseClicked(MouseEvent e) {}
		 
		    /**
		    * è„—å½•è„™éˆ¥âˆ¶å†£ï¿½æ¯­å�®æµŽå…£çŠ†å…Ÿè¶�å�®æ³µå…Ÿî€¢å�®æ‡Šå�®ç–µå†¿æ‹·è„™éˆ¥æ¯­å…£çŠ†å�®æ³µå�®å¾—å†£ï¿½ç�»å�®å¾—å…Ÿï¿½
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
		      board.requestFocus();
		      Point p = this.wall.getLocation();
		      int relX, relY;
		      if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
		    	  relX = p.x + (e.getX() - origin.x) - board.getX() - 15;
			      relY = p.y + (e.getY() - origin.y) - board.getY()  + 15;
		      } else {
		    	  relX = p.x + (e.getX() - origin.x) - board.getX() + 15;
			      relY = p.y + (e.getY() - origin.y) - board.getY() - 15;
		      }
		      
		      if(QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus()
		    		  == GameStatus.Running &&
		    		  QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate()) {
		    	  int row = relY / 40 + 1;
		    	  int col = relX / 40 + 1;
		    	  if(row < 1 || row > 9) return;
		    	  if(col < 1 || col > 9) return;
		    	  if(!QuoridorController.moveWall(QuoridorController.findTile(row, col))) {
		    		  return;
		    	  }
		    	  row = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
		    	  col = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
			      refresh(); 
			      if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Vertical) {
			    	  this.wall.setLocation( 
						        board.getX() - 5 + col*40, 
						        board.getY() + row * 40 - 40);
			      } else {
			    	  this.wall.setLocation( 
						        board.getX() + col*40 - 40, 
						        board.getY() - 5 + row * 40);
			      }
			     
		      }
		    }
		 
		    @Override
		    public void mouseMoved(MouseEvent e) { }
		     
		  }

	//Not implemented, but eventually was where I was planning on doing the timer stuff.
	//I just don't know how
	public void updateView() {
		if(p1Turn.isSelected()) {
			whiteSeconds--;
			p1Time.setText("Time: " + (whiteSeconds / 60) + " m " + (whiteSeconds % 60) +" s ");
			if(QuoridorApplication.getQuoridor().hasCurrentGame())
			QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setRemainingTime(new Time(whiteSeconds * 1000));
		} else {
			blackSeconds--;
			p2Time.setText("Time: "+(blackSeconds / 60)+" m " + (blackSeconds % 60) +" s ");
			if(QuoridorApplication.getQuoridor().hasCurrentGame())
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setRemainingTime(new Time(blackSeconds * 1000));
		}
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
		refresh();
	}
	//Displays red text on the screen with a given message
	public void notifyValid(String message) {
		
		notification.setText(message);
		notification.setForeground(Color.GREEN);
		notification.setForeground(new Color(21, 148, 38, 255));
		notification.setVisible(true);
		refresh();
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
				notifyValid("Saved Successfully");
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
						(!name.substring(name.length() - 4, name.length()).equals(".dat") &&
						!name.substring(name.length() - 4, name.length()).equals(".mov"))) {
					name += ".dat";
				}
				fileName = name;
				if(QuoridorController.containsFile(name)) {
					confirmFrame.remove(gameName);
					layout.replace(gameNameExplain, notification);
					layout.replace(saveButton, yesButton);
					layout.replace(exitButton, noButton);
					
					layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
														.addComponent(notification)
														.addGroup(layout.createSequentialGroup()
																		.addComponent(yesButton)
																		.addComponent(noButton)));
					layout.setVerticalGroup(layout.createSequentialGroup()
													.addComponent(notification)
													.addGroup(layout.createParallelGroup()
																	.addComponent(yesButton)
																	.addComponent(noButton)));
					
					
					
					SwingUtilities.updateComponentTreeUI(confirmFrame);
					confirmFrame.pack();
				} else {	
					//Save the game
					QuoridorController.savePosition(fileName);
					File f = new File(fileName); 
					f.setLastModified(0);
					notifyValid("Saved Successfully");
					//Exit the frame
					confirmFrame.dispatchEvent(new WindowEvent(confirmFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		
		exitButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//Exit the frame
				fileName = null;
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
				QuoridorController.stopwhiteclock(whiteTimer);
				QuoridorController.stopblackclock(blackTimer);
				fileName = null;
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
	
	public void confirmExistingName() {
		confirmFrame.getContentPane().removeAll();
		JLabel notification = new JLabel("The selected user name already exists. Continue?");
		notification.setForeground(Color.red);
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				QuoridorController.startGame();
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().setUser(QuoridorController.findUserName(whiteName.getText()));
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().setUser(QuoridorController.findUserName(blackName.getText()));
				try {
					int minutes = Integer.parseInt(minutesField.getText());
					int seconds = Integer.parseInt(secondsField.getText());
					whiteSeconds = 60*minutes + seconds;
					blackSeconds = 60*minutes + seconds;
					QuoridorController.setTotaltime(minutes, seconds);
					p1Time.setText("Time: "+minutes+" m " + seconds +" s ");
					p2Time.setText("Time: "+minutes+" m " + seconds +" s ");
				} catch (Exception e) {
					whiteSeconds = 60*10;
					blackSeconds = 60*10;
					p1Time.setText("Time: "+10+" m " + 0 +" s ");
					p2Time.setText("Time: "+10+" m " + 0 +" s ");
					QuoridorController.setTotaltime(10, 0);
				}
				
				QuoridorController.initializeBoard();
				initGame();		
				
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
	
	
	public void confirmDeleteFile(String fileName) {
		confirmFrame.getContentPane().removeAll();
		JLabel notification = new JLabel("<html><center>Are you sure you wish to delete this file?" +
										"<br> Deleted Files can not be recovered.</center></html>", SwingConstants.CENTER);
		notification.setForeground(Color.red);
		JButton yesButton = new JButton("Yes");
		yesButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				QuoridorController.deleteFile(fileName);	
				
				//Ah, that's why. This isn't running after the confirmDelete
				File dir = new File(System.getProperty("user.dir"));
				
				File[] saveFiles = dir.listFiles();
				String name;
				DefaultListModel<String> l = new DefaultListModel<>();
				l.clear();
				if(saveFiles != null) {
					for(File f : saveFiles) {
						name = f.getName();
						if( name.length() > 4 && (name.substring(name.length() - 4, name.length()).equals(".dat") 
							||name.substring(name.length() - 4, name.length()).equals(".mov"))) {
							l.addElement(f.getName());
						}
						
					}
				}
				JList<String> fileList = new JList<String>(l);
				
				 fileList.addKeyListener(new java.awt.event.KeyListener() {
						public void keyPressed(java.awt.event.KeyEvent evt) {}
						public void keyTyped(java.awt.event.KeyEvent evt) {}
						@Override
						public void keyReleased(java.awt.event.KeyEvent evt) {
							if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
									if(QuoridorController.isEnded(fileList.getSelectedValue())) {
										replayGame.doClick();
										return;
									}
									if(QuoridorController.loadGame(fileList.getSelectedValue())) {
				
										File f = new File(fileList.getSelectedValue()); 
										f.setLastModified(0);
										
										p1Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
										p2Name.setText(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
										
										whiteSeconds = 60*10;
										blackSeconds = 60*10;
										p1Time.setText("Time: "+10+" m " + 0 +" s ");
										p2Time.setText("Time: "+10+" m " + 0 +" s ");
										
										
										
										QuoridorController.setTotaltime(10, 0);
										
										initGame();
										
										if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
												.equals(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer())) {
													p2Turn.setSelected(true);
													p1Turn.setSelected(false);
												}
										
										for(WallMove w : QuoridorController.getWalls()) {
											
											JPanel newWall = new JPanel();
											int row = w.getTargetTile().getRow();
									    	int col = w.getTargetTile().getColumn();
										    refresh(); 
										    if(w.getWallDirection() == Direction.Vertical) {
										    	
										    	newWall.setSize(5, 75);
										    	newWall.setLocation( 
													     board.getX() - 5 + col*40, 
													     board.getY() + row * 40 - 40);
										      } else {
										    	  newWall.setSize(75, 5);
										    	  newWall.setLocation( 
													        board.getX() + col*40 - 40, 
													        board.getY() - 5 + row * 40);
										      }
											newWall.setBackground(Color.BLACK);
											getContentPane().add(newWall);
											
										}
										board.requestFocusInWindow();
										fileList.removeKeyListener(fileList.getKeyListeners()[0]);
									} else {
										GroupLayout layout = new GroupLayout(getContentPane());
										getContentPane().setLayout(layout);
										layout.setAutoCreateGaps(true);
										layout.setAutoCreateContainerGaps(true);
										GroupLayout.Group horizontal = layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																			.addComponent(title)
																			.addComponent(newGame)
																			.addComponent(loadGame);
										GroupLayout.Group vertical = layout.createSequentialGroup()
												  						   .addComponent(title)
												  						   .addComponent(newGame)
												  						   .addComponent(loadGame);
										
										
										notifyInvalid("Load File Error- Invalid Position");
										layout.setHorizontalGroup(layout.createSequentialGroup()
																		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
																						.addGroup(horizontal)
																						.addComponent(notification))
																		.addComponent(filePane));
										layout.setVerticalGroup(layout.createParallelGroup()
																.addGroup(layout.createSequentialGroup()
																				.addGroup(vertical)
																				.addComponent(notification))
																.addComponent(filePane));

										getContentPane().setLayout(layout);
										pack();
									}
				
									
								}
							if(evt.getKeyCode() == KeyEvent.VK_DELETE || evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
								confirmDeleteFile(fileList.getSelectedValue());
							}
						}
					});
				
				
				
				filePane.setViewportView(fileList);
				refresh();
				
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
		if(newGame.getActionListeners().length > 0)newGame.removeActionListener(newGame.getActionListeners()[0]);
		if(loadGame.getActionListeners().length > 0)loadGame.removeActionListener(loadGame.getActionListeners()[0]);
		if(replayGame.getActionListeners().length > 0)replayGame.removeActionListener(replayGame.getActionListeners()[0]);
		if(saveButton.getActionListeners().length > 0)saveButton.removeActionListener(saveButton.getActionListeners()[0]);
		if(exitButton.getActionListeners().length > 0)exitButton.removeActionListener(exitButton.getActionListeners()[0]);
		if(grabButton.getActionListeners().length > 0)grabButton.removeActionListener(grabButton.getActionListeners()[0]);
		if(rotateButton.getActionListeners().length > 0)rotateButton.removeActionListener(rotateButton.getActionListeners()[0]);
		if(undoButton.getActionListeners().length > 0)undoButton.removeActionListener(undoButton.getActionListeners()[0]);
		if(continueButton != null && continueButton.getActionListeners().length > 0)continueButton.removeActionListener(continueButton.getActionListeners()[0]);
		if(board.getMouseListeners().length > 1)board.removeMouseListener(board.getMouseListeners()[1]);
		if(board.getMouseListeners().length > 0)board.removeMouseListener(board.getMouseListeners()[0]);
		if(board.getKeyListeners().length > 0)board.removeKeyListener(board.getKeyListeners()[0]);
		if(board.getMouseMotionListeners().length > 0)board.removeMouseMotionListener(board.getMouseMotionListeners()[0]);
	}
	
	//Just toggling radio buttons
	public void switchPlayerButton() {
		Arrays.fill(outlineTile, false);
		notification.setVisible(false);
		if(p1Turn.isSelected()) {
			p1Turn.setSelected(false);
			p2Turn.setSelected(true);
		} else {
			p1Turn.setSelected(true);
			p2Turn.setSelected(false);
		}
		explanation.setText("<html><center>Press 'g' to grab a wall"
						+  "<br>Or press 'm' to move</center></html>");
		refresh();
	}
	
	public void DropWall() {
		if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
			if(QuoridorController.wallIsValid() && QuoridorController.dropWall()) {
				
				JPanel newWall = new JPanel();
				int row = ((WallMove) QuoridorApplication.getQuoridor().getCurrentGame().getMoves().get(QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size() - 1)).getTargetTile().getRow();
		    	int col = ((WallMove) QuoridorApplication.getQuoridor().getCurrentGame().getMoves().get(QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size() - 1)).getTargetTile().getColumn();
			    refresh(); 
			    if(((WallMove) QuoridorApplication.getQuoridor().getCurrentGame().getMoves().get(QuoridorApplication.getQuoridor().getCurrentGame().getMoves().size() - 1)).getWallDirection() == Direction.Vertical) {
			    	
			    	newWall.setSize(5, 75);
			    	newWall.setLocation( 
						     board.getX() - 5 + col*40, 
						     board.getY() + row * 40 - 40);
			      } else {
			    	  newWall.setSize(75, 5);
			    	  newWall.setLocation( 
						        board.getX() + col*40 - 40, 
						        board.getY() - 5 + row * 40);
			      }
				newWall.setBackground(Color.BLACK);
				getContentPane().add(newWall);
				if(wall != null) getContentPane().remove(wall);
				wall = null;
				
				
				
				
				switchPlayerButton();
			} else {
				notifyInvalid("Invalid Wall Placement");
			}
			
		}
	}
	public void RotateWall() {
		
		if(QuoridorApplication.getQuoridor().getCurrentGame().getMoveMode() == MoveMode.WallMove) {
			QuoridorController.rotateWall();
			if(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getWallDirection() == Direction.Horizontal) {
				if(wall != null) {
					wall.setBounds(wall.getX() - 35, wall.getY() + 35, 75, 5);
				}
			} else {
				if(wall != null) {
					wall.setBounds(wall.getX() + 35, wall.getY() - 35, 5, 75);
				}
			}
			
			refresh();
		}
	}
	public void movePlayer(MoveDirection dir) {
		white.setDir(dir);
		black.setDir(dir);
		
		
		
		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())) {
			if(white.move()) {
				switchPlayerButton();
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow() == 1) {
					QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.WhiteWon);
				}
			}
			else notifyInvalid("Invalid Player Move");

		} else {
			if(black.move())  {
				switchPlayerButton();
				
				if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow() == 9) {
					QuoridorApplication.getQuoridor().getCurrentGame().setGameStatus(GameStatus.BlackWon);
				}
				
			}
			else notifyInvalid("Invalid Player Move");
		}
		QuoridorApplication.getQuoridor().getCurrentGame().setMoveMode(MoveMode.PlayerMove);

	}

}


