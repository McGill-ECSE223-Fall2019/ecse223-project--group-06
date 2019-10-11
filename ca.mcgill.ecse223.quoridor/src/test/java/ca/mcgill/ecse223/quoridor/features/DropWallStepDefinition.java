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
		//One of these methods should almost certainly set the game to running
	}
		
	@And("The following walls exist:")
	public void theFollowingWallsExist(DataTable table) {
		//I made this method literally just place all the given walls
		
		
		//We don't have any classes with constructors fitting input perfectly
		List<List<String>> walls = table.asLists();
		
		for(int i = 0; i < walls.size(); i++) {
			//set dir. Row/Column use parseInt
			Direction dir = walls.get(i).get(3).equals("vertical") ? Direction.Vertical : Direction.Horizontal;
			
			//Grab a wall, rotate it, move it, and drop it
			GrabWall.grab(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
			//row, column, direction, game: may need round number(i) later
			RotateWall.rotate(dir, quoridor.getCurrentGame());
			MoveWall.move(Integer.parseInt(walls.get(i).get(0)), 
						  Integer.parseInt(walls.get(i).get(1)), 
						  quoridor.getCurrentGame());	
			DropWall.dropWall(quoridor.getCurrentGame());
			
			if(i % 2 == 0) {
				//Starts w/ white moves first, so if even it's white's move
				//switch to the next player
				//You could probably just pass players or even the game... but it's not my method
				//oml it uses timer instead of time too... And Timer acts like a stopwatch!!!
				//TODO: ask them to fix that
				//Switchplayer.makeTurn(quoridor.getCurrentGame().getWhitePlayer().getRemainingTime(), quoridor.getCurrentGame().getBlackPlayer().getRemainingTime(), quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer());
				Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer());
			} else {
				Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getBlackPlayer(), quoridor.getCurrentGame().getWhitePlayer());
			}
			
		}
	}
	
	@And("It is my turn to move")
	public void itIsMyTurnToMove() {
		//Well- we're assuming it's player 1 here
		if(! ( quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove()
				== quoridor.getCurrentGame().getWhitePlayer()  )            ) {
			//switch to the next player
			Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getBlackPlayer(), quoridor.getCurrentGame().getWhitePlayer());
		}

	}
	
	@And("I have a wall in my hand over the board")
	public void iHaveAWallOverTheBoard() {
		//grab might just take the game... guess I'll find out
		GrabWall.grab(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove());
	}
	
	
	
	@Given("The wall move candidate with {string} at position {int}, {int} is valid")
	public void theWallMoveCandidateWithDirAtPosIsValid(String dir, int row, int col) throws InvalidInputException {
		//Get a string- make a dir
		//Also, previous given's ensure we have a wall move candidate slot taken
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, quoridor.getCurrentGame());
		MoveWall.move(row, 
				      col, 
				      quoridor.getCurrentGame());	
		//We're assuming it is valid, and just moving it there
		//Might want to update this to just take a game
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
		DropWall.registerMove(quoridor.getCurrentGame(), direction, row, col);
	}
	
	@And("I shall not have a wall in my hand") 
	public void iDoNotHaveAWallInMyHand() {
		//Do I need to do this with controller? Maybe GrabWall has a wallInHand method? Create one if not before turn in
		Assert.assertFalse("A wall is still in hand after dropping", quoridor.getCurrentGame().hasWallMoveCandidate());
	}
	
	
	
	
	@And("My move shall be completed")
	public void myMoveIsCompleted() {
		//Ok I think it wants me to make dropwall method to complete turn
		//Then switch turn in next
		DropWall.endMove(quoridor.getCurrentGame());
		//Could add a check to see if this worked later. OR just make endMove throw an error if it didn't
		
		
		//What does it want me to check here? This step and the next seem the same
		//And I already made sure move was added last method
		//Best I could think of, checks to see if ther is no more candidate wall move
		//Assert.assertNull(quoridor.getCurrentGame().getWallMoveCandidate());
	
	}
	
	@And("It shall not be my turn to move")
	public void itIsNotMyTurnToMove() {
		Switchplayer.makeTurn(new Timer(), new Timer(), quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer());
	}
	
	
	@Given("The wall move candidate with {string} at position {int}, {int} is invalid")
	public void theWallMoveCandidateWithDirAtPosIsInvalid(String dir, int row, int col) throws InvalidInputException {
		//Background ensures I have a wall in hand
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		RotateWall.rotate(direction, quoridor.getCurrentGame());
		MoveWall.move(row, 
				  col, 
				  quoridor.getCurrentGame());
		
		//We're assuming it is valid, and just moving it there
		//Might want to update this to just take a game
		if(DropWall.wallIsValid(quoridor.getCurrentGame().getWallMoveCandidate(), 
								quoridor.getCurrentGame().getMoves())) {
			Assert.fail(); //If you reached here, the parameters being passed in are wrong
		}
	}
	
	
	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid() {
		//The boolean it returns IS your notification. Rather not do try/catch stuff
		//Assert.assertFalse(DropWall.dropWall(quoridor.getCurrentGame()));
		//GUI stuff
		throw new PendingException();
	}
	
	//Here's where I get confused... Drop wall method takes care of all of this...
	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() {
		Assert.assertNotNull(quoridor.getCurrentGame().getWallMoveCandidate());
	}
	
	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove() {
		Assert.assertTrue(quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove()
				       == quoridor.getCurrentGame().getWhitePlayer());
	}
	//THE PROBLEM WAS THE GHERKIN FILE THIS WHOLE TIME. (<row>,<col>) MAKES IT OPTIONAL....
	
	@But("No wall move shall be registered with {string} at position {int}, {int}")
	public void noWallMoveShallBeRegisteredAtPosition(String dir, int row, int col) throws InvalidInputException {
		Direction direction = dir.equals("vertical") ? Direction.Vertical : Direction.Horizontal;
		//Checks last move
		int index = quoridor.getCurrentGame().getMoves().size() - 1;
		Move check = quoridor.getCurrentGame().getMoves().get(index);
		if( check instanceof WallMove) {
			WallMove move = (WallMove) check;
			boolean directionSame = direction == move.getWallDirection();
			boolean rowSame = move.getTargetTile().getRow() == row;
			boolean columnSame = move.getTargetTile().getColumn() == col;
			Assert.assertFalse(directionSame && rowSame && columnSame);
		}
		Assert.fail();
	}
}
