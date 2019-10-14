package ca.mcgill.ecse223.quoridor.controller;

import java.util.List;

import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

public class QuoridorController {

	public QuoridorController() {
		
	}
	/** Move Wall Feature
	 * Will fail if position is not valid
	 * Updates game position with candidate wall move 
	 * @param move - wall move candidate
	 * @param targetTile - new tile to move to
	 * @return whether or not the wall successfully moved
	 */
	
	public boolean moveWall(WallMove curMove, Tile targetTile) {
		
		//take in a WallMove created in GrabWall feature and put the wall in the targetTile
		//will validate position to ensure no overlapping
		
		throw new java.lang.UnsupportedOperationException();
	}
	
	public static boolean wallIsValid(WallMove checkMove, List<WallMove> wallMoves, PlayerPosition position1, PlayerPosition position2) {
		//loop through wall moves to see if any interfere with desired move to be made
		//check to see if wall to be moved overlaps with players or is out of bounds
		throw new java.lang.UnsupportedOperationException();	
	}
	
	public Tile findTile(int r, int c) {
		//use row and col to find the tile we want
		throw new java.lang.UnsupportedOperationException();
	}
	
	/** Grab Wall feature
	 * Checks to make sure player has walls and creates WallMove object to be passed on to move wall feature
	 * @param aWall - wall to grab
	 * @return
	 */
	
	public WallMove grabWall(Wall aWall) {
		
		throw new java.lang.UnsupportedOperationException();
	}
	
	public boolean isSide(WallMove aWallMove) {
		throw new java.lang.UnsupportedOperationException();
	}
}
