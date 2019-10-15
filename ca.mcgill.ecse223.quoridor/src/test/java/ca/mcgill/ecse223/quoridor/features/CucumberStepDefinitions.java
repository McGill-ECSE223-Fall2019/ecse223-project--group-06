package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class CucumberStepDefinitions {
	
	
	
	private Quoridor quoridor;
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private Game game;
	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndStartGame(createUsersAndPlayers);
	}

	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}

	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}
	
	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}
	
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Feature: #2 Provide or select user name
	//Name: Keanu, Natchev
	//ID#: 260804586
	
	@Given("A new game is initializing")
	public void aNewGameIsInitializing() {
		QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
		assertEquals(true, GameStatus.Initializing);
	    throw new cucumber.api.PendingException();
	}

	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIs(String string) {
		if(!(string == "black") && !(string == "white")) {
			throw new IllegalArgumentException();
		}
		else {
			if(string == "black") {
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			}
			if(string == "white") {
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			}
		}
				
	    throw new cucumber.api.PendingException();
	}

	@Given("There is existing user {string}")
	public void thereIsExistingUser(String string) {
	    List<User> existingUsers = QuoridorApplication.getQuoridor().getUsers();
	    for(int i = 0; i < existingUsers.size(); i++) {
	    	assertEquals(string, existingUsers.get(i).getName());
	    }
	    throw new cucumber.api.PendingException();
	}

	@When("The player selects existing {string}")
	public void thePlayerSelectsExisting(String string) {
		assertEquals(true, QuoridorController.ExistingUserName(string));
	    throw new cucumber.api.PendingException();
	}

	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerInTheNewGameShallBe(String string, String string2) {
		if(string == "black") {
			QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().setName(string2);
			
		}
		if(string == "white") {
			QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().setName(string2);
		}
	    
	    throw new cucumber.api.PendingException();
	}

	@Given("There is no existing user {string}")
	public void thereIsNoExistingUser(String string) {
	    assertEquals(false, QuoridorController.ExistingUserName(string));
	    throw new cucumber.api.PendingException();
	}

	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUserName(String string) {
	    QuoridorApplication.getQuoridor().addUser(string);
	    throw new cucumber.api.PendingException();
	}

	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatAlreadyExists(String string) {
	    if(QuoridorController.ExistingUserName(string)) {
	    	System.out.println("The user with the name: " + string + "already exists.");
	    }
	    throw new cucumber.api.PendingException();
	}

	@Then("Next player to set user name shall be {string}")
	public void nextPlayerToSetUserNameShallBe(String string) {
		if(!string.equals("black") && !string.equals("white")) {
			throw new IllegalArgumentException();
		}
		else {
			if(string.equals("black")) {
				QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
			}
			if(string.equals("white")) {
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
			}
		}
	    throw new cucumber.api.PendingException();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Feature: #11 Validate position
	//Name: Keanu, Natchev
	//ID#: 260804586
	
	@Given("A game position is supplied with pawn coordinate {int}:{int}")
	public void aGamePositionIsSuppliedWithPawnCoordinate(Integer int1, Integer int2) {
		if(int1 < 1 || int1 > 9 || int2 < 1 || int2 > 9) {
			System.out.println("Invalid coordinates given. Values must be between 1 and 9.");
		}
		else {			
			Integer row;
			Integer column;
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
				column = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
				assertEquals(row, int1);
				assertEquals(column, int2);
			}
			if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
				row = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
				column = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
				assertEquals(row, int1);
				assertEquals(column, int2);
			}
		}
	    
		
	    throw new cucumber.api.PendingException();
	}

	@When("Validation of the position is initiated")
	public void validationOfThePositionIsInitiated() {
	    QuoridorController.validatePosition();
	    throw new cucumber.api.PendingException();
	}

	@Then("The position shall be {string}")
	public void thePositionShallBe(String string) {
		if(QuoridorController.validatePosition()) {
			string = "ok";
		}
		else {
			string = "error";
		}
	    throw new cucumber.api.PendingException();
	}

	@Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
	public void aGamePositionIsSuppliedWithWallCoordinate(Integer int1, Integer int2, String string) {		
		Direction directionGiven = null;

		if(string == "vertical") {
			directionGiven = Direction.Vertical;					
		}
		if(string == "horizontal") {
			directionGiven = Direction.Horizontal;
		}

		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()) {
			List<Wall> blackWalls =  QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsOnBoard();
			Integer row;
			Integer column;
			Direction direction;
			for(int i = 0; i < blackWalls.size(); i++) {

				row = blackWalls.get(i).getMove().getTargetTile().getRow();
				column = blackWalls.get(i).getMove().getTargetTile().getColumn();
				direction = blackWalls.get(i).getMove().getWallDirection();
				assertEquals(int1, row);
				assertEquals(int2, column);
				assertEquals(directionGiven, direction);
			}
		}
		
		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsWhite()) {
			List<Wall> whiteWalls =  QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsOnBoard();
			Integer row;
			Integer column;
			Direction direction;
			for(int i = 0; i < whiteWalls.size(); i++) {

				row = whiteWalls.get(i).getMove().getTargetTile().getRow();
				column = whiteWalls.get(i).getMove().getTargetTile().getColumn();
				direction = whiteWalls.get(i).getMove().getWallDirection();
				assertEquals(int1, row);
				assertEquals(int2, column);
				assertEquals(directionGiven, direction);
			}
		}


	    throw new cucumber.api.PendingException();
	}

	@Then("The position shall be valid")
	public void thePositionShallBeValid() {
	    assertEquals(true, QuoridorController.validatePosition());
	    throw new cucumber.api.PendingException();
	}

	@Then("The position shall be invalid")
	public void thePositionShallBeInvalid() {
		assertEquals(false, QuoridorController.validatePosition());
	    throw new cucumber.api.PendingException();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
	@After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		//@formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);
		
		return playersList;
	}

	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
	}

}
