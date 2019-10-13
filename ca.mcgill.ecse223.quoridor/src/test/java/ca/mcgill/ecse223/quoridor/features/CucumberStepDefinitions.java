package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import cucumber.api.PendingException;
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
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {

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
	
	
	
	
	/** Drop Wall Step Definition File
	 * @author Yanis Jallouli
	 */
	// ***********************************************
	// Drop Wall definitions
	// ***********************************************
	//Scenario 1
	@Given("The wall move candidate with {string} at position {int}, {int} is valid")
	public void theWallMoveCandidateWithDirAtPosIsValid(String dir, int row, int col) throws InvalidInputException {
		//Get a string- make a dir
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, QuoridorApplication.getQuoridor().getCurrentGame());
		MoveWall move = new MoveWall(row, col);
		MoveWall.move(  QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() , move.findTile(row, col));
		//Fail if invalid wall given
		if(!DropWall.wallIsValid(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate(), 
				QuoridorApplication.getQuoridor().getCurrentGame().getMoves(), 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition())  ) {
			Assert.fail();
		}
	}
	
	@When("I release the wall in my hand") 
	public void iReleaseTheWallInMyHand(){
		DropWall.dropWall(QuoridorApplication.getQuoridor().getCurrentGame());
	}
	
	@Then("A wall move shall be registered with {string} at position {int}, {int}")
	public void aWallMoveIsRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		Assert.assertTrue("Move wasn't registered after dropping", DropWall.moveIsRegistered(QuoridorApplication.getQuoridor().getCurrentGame(), direction, row, col));
	}
	
	@And("I shall not have a wall in my hand") 
	public void iShallNotHaveAWallInMyHand() {
		//Ensures the candidate wallmove is null. Might be a grab wall feature, but this is easy
		Assert.assertFalse(QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate());
	}

	@And("My move shall be completed")
	public void myMoveIsCompleted() {
		//Make sure I can't do anything...I guess? How???? Switching turns does that...
		throw new PendingException(); //I'm assuming this is a User confirming move
	}
	
	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
		//Or maybe I'm supposed to change it last step and check it this one? 
		Switchplayer.makeTurn(new Timer(), new Timer(), QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(), QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
		Assert.assertTrue( !QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())     );
	}
	//SCENARIO 2
	@Given("The wall move candidate with {string} at position {int}, {int} is invalid")
	public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException {
		//Background ensures I have a wall in hand
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, QuoridorApplication.getQuoridor().getCurrentGame());
		MoveWall move = new MoveWall(row, col);
		MoveWall.move(  QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() , move.findTile(row, col));
		//Check
		if(DropWall.wallIsValid(QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate(), 
				QuoridorApplication.getQuoridor().getCurrentGame().getMoves(), 
				QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition())) {
			Assert.fail(); //If you reached here, the parameters being passed in are wrong
		}
	}
	
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		throw new PendingException(); //GUI stuff
	}
	
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().hasWallMoveCandidate());
	}
	
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
		Assert.assertTrue(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
				QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer())   );
	}
	
	//THE PROBLEM WAS THE GHERKIN FILE THIS WHOLE TIME. (<row>,<col>) MAKES IT OPTIONAL
	
	@But("No wall move shall be registered with {string} at position {int}, {int}")
	public void noWallMoveShallBeRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		Assert.assertFalse(DropWall.moveIsRegistered(QuoridorApplication.getQuoridor().getCurrentGame(), direction, row, col));
	}
	
	
	/** Save Position Step Definition File
	 * @author Yanis Jallouli
	 */
	// ***********************************************
	// Save Position definitions
	// ***********************************************
	//Scenario 1
	@Given("No file {string} exists in the filesystem")
	public void noFileExistsInTheSystem(String fileName) {
		//I can't find anything on using givens as control flow
		Assert.assertFalse(SavePosition.containsFile(fileName));
	}
	
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithName(String fileName) {
		//GUI related
		throw new PendingException();
	}
	
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithNameShallBeCreated(String fileName) {
		SavePosition.createFile(QuoridorApplication.getQuoridor().getCurrentGame(), fileName); 
		Assert.assertTrue(SavePosition.containsFile(fileName));
	}
	//Scenario 2
	@Given("File {string} exists in the filesystem")
	public void fileNameExistsInSystem(String fileName) {
		//This is confusing me. We have one for it doesn't exist so it
		//seems like an if thing. Should I assert it's true? or make it true somehow?
		Assert.assertTrue(SavePosition.containsFile(fileName));
	}
	
	@And("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwrite() {
		//GUI related
		throw new PendingException();
	}
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithNameShallBeUpdatedInSystem(String fileName) {
		SavePosition.savePosition(QuoridorApplication.getQuoridor().getCurrentGame(), fileName);
		Assert.assertTrue(SavePosition.isUpdated(QuoridorApplication.getQuoridor().getCurrentGame(), fileName));
	}

	@And("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwrite() {
		//GUI related
		throw new PendingException();
	}
	@Then("File {string} shall not be changed in the filesystem")
	public void fileWithNameShallNotBeUpdatedInSystem(String fileName) {
		Assert.assertFalse(SavePosition.isUpdated(QuoridorApplication.getQuoridor().getCurrentGame(), fileName));
	}

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
