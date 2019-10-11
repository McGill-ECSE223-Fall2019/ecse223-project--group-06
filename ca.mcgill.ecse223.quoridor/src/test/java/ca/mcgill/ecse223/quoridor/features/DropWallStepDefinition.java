package ca.mcgill.ecse223.quoridor.features;

import java.sql.Time;
import java.util.List;
import java.util.Timer;

import org.junit.Assert;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import cucumber.api.PendingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



/** Drop Wall Step Definition File
 * 
 * @author yajal
 *
 */
public class DropWallStepDefinition {
	Quoridor quoridor;
	
	
	//BACKGROUND
	@Given("The game is running")
	public void theGameIsRunning() {
		//USER INPUT (fill in for cucumber)
		
		ProvideSelectUserName.createUsername("User1", quoridor);
		ProvideSelectUserName.createUsername("User2", quoridor); //In user input screen
		//3600 seconds allocated per player = 1 hour
		SetTotalThinkingTime.setTimefor2(new Time(3600 * 1000),  //Set the thinking time for created players
				 quoridor.getCurrentGame().getWhitePlayer(), 
				 quoridor.getCurrentGame().getBlackPlayer());
		
		//Boot it all up
		StartNewGame.start(quoridor); //Create a quoridor game
		InitializeBoard.init(quoridor); //Create the board and display
	}
		
	@And("The following walls exist:")
	public void theFollowingWallsExist(DataTable table) {
		//Places walls
		List<List<String>> walls = table.asLists(); //Input as List
		
		for(int i = 0; i < walls.size(); i++) {
			//set dir. Row/Column use parseInt
			Direction dir = walls.get(i).get(3).equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			//Grab a wall, rotate it, move it, and drop it
			GrabWall.grab(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
			RotateWall.rotate(quoridor.getCurrentGame(), dir);
			MoveWall.move(Integer.parseInt(walls.get(i).get(0)), 
						  Integer.parseInt(walls.get(i).get(1)), 
						  quoridor.getCurrentGame());
			
			DropWall.dropWall(quoridor.getCurrentGame());
			
			if(i % 2 == 0) {
				//Starts w/ white moves first, so if(even) move = white's
				//TODO: ask them it Time... not Timer
				Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer());
			} else {
				Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getBlackPlayer(), quoridor.getCurrentGame().getWhitePlayer());
			}
			
		}
	}
	
	@And("It is my turn to move")
	public void itIsMyTurnToMove() { //Assumes player = white
		if(! ( quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove()
				== quoridor.getCurrentGame().getWhitePlayer()  )            ) {
			//switch to white
			Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getBlackPlayer(), quoridor.getCurrentGame().getWhitePlayer());
		}
	}
	
	@And("I have a wall in my hand over the board")
	public void iHaveAWallOverTheBoard() {
		GrabWall.grab(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
	}
	
	
	
	//SCENARIO 1
	
	@Given("The wall move candidate with {string} at position {int}, {int} is valid")
	public void theWallMoveCandidateWithDirAtPosIsValid(String dir, int row, int col) throws InvalidInputException {
		//Get a string- make a dir
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, quoridor.getCurrentGame());
		MoveWall.move(row, col, quoridor.getCurrentGame());	
		//Fail if invalid wall given
		if(!DropWall.wallIsValid(quoridor.getCurrentGame().getWallMoveCandidate(), 
							quoridor.getCurrentGame().getMoves())  ) {
			Assert.fail();
		}
	}
	
	@When("I release the wall in my hand") 
	public void iReleaseTheWallInMyHand(){
		DropWall.dropWall(quoridor.getCurrentGame());
	}
	
	
	@Then("A wall move shall be registered with {string} at position {int}, {int}")
	public void aWallMoveIsRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		Assert.assertTrue("Move wasn't registered after dropping", DropWall.moveIsRegistered(quoridor.getCurrentGame(), direction, row, col));
	}
	
	@And("I shall not have a wall in my hand") 
	public void iDoNotHaveAWallInMyHand() {
		//Ensures the candidate wallmove is null. Might be a grab wall feature, but this is easy
		Assert.assertFalse("A wall is still in hand after dropping", quoridor.getCurrentGame().hasWallMoveCandidate());
	}

	@And("My move shall be completed")
	public void myMoveIsCompleted() {
		//What does it want me to check here? This step == next
		//And I already made sure move was added
		
		//Best I could think of, checks to see if there is no more candidate wall move
		Assert.assertNull(quoridor.getCurrentGame().getWallMoveCandidate());
	}
	
	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
		//Or maybe I'm supposed to change it last step and check it this one? 
		Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer());
	}
	
	
	//SCENARIO 2
	
	@Given("The wall move candidate with {string} at position {int}, {int} is invalid")
	public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException {
		//Background ensures I have a wall in hand
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, quoridor.getCurrentGame());
		MoveWall.move(row, col, quoridor.getCurrentGame());
		//Check
		if(DropWall.wallIsValid(quoridor.getCurrentGame().getWallMoveCandidate(), 
								quoridor.getCurrentGame().getMoves())) {
			Assert.fail(); //If you reached here, the parameters being passed in are wrong
		}
	}
	
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		//The boolean will need to cause notification
		//Assert.assertFalse(DropWall.dropWall(quoridor.getCurrentGame()));
		//GUI stuff
		throw new PendingException();
	}
	
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		Assert.assertTrue(quoridor.getCurrentGame().hasWallMoveCandidate());
	}
	
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
		Assert.assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove().equals(
				quoridor.getCurrentGame().getWhitePlayer())   );
	}
	
	//THE PROBLEM WAS THE GHERKIN FILE THIS WHOLE TIME. (<row>,<col>) MAKES IT OPTIONAL....
	
	@But("No wall move shall be registered with {string} at position {int}, {int}")
	public void noWallMoveShallBeRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		Assert.assertFalse(DropWall.moveIsRegistered(quoridor.getCurrentGame(), direction, row, col));
	}
}
