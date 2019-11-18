/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import ca.mcgill.ecse223.quoridor.model.*;

// line 6 "../../../../../PawnStateMachine.ump"
public class PawnBehavior
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehavior State Machines
  public enum PawnSM { CanMove, CanJump, CanMoveAndJump, CantMoveAndJump }
  private PawnSM pawnSM;

  //PawnBehavior Associations
  private Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehavior()
  {
    setPawnSM(PawnSM.CanMove);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public boolean setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;
    return true;
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }

  public boolean hasCurrentGame()
  {
    boolean has = currentGame != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentGame(Game aNewCurrentGame)
  {
    boolean wasSet = false;
    currentGame = aNewCurrentGame;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPlayer(Player aNewPlayer)
  {
    boolean wasSet = false;
    player = aNewPlayer;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    currentGame = null;
    player = null;
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 54 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnRow(){
    GamePosition curPos = currentGame.getCurrentPosition();
		Player white = currentGame.getWhitePlayer();
		if(curPos.getPlayerToMove().equals(white)) {
			return curPos.getWhitePosition().getTile().getRow();
			
		} else {
			return curPos.getBlackPosition().getTile().getRow();
		}
  }


  /**
   * Returns the current column number of the pawn
   */
  // line 65 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnColumn(){
    GamePosition curPos = currentGame.getCurrentPosition();
		Player white = currentGame.getWhitePlayer();
		if(curPos.getPlayerToMove().equals(white)) {
			return curPos.getWhitePosition().getTile().getColumn();
		
		} else {
			return curPos.getBlackPosition().getTile().getColumn();
		}
  }


  /**
   * Returns if it is legal to step in the given direction
   */
  // line 76 "../../../../../PawnStateMachine.ump"
  public boolean isLegalStep(MoveDirection dir){
    GamePosition curPos = currentGame.getCurrentPosition();
		Player white = currentGame.getWhitePlayer();
		int[] toCheckPos = new int[2];
		int[] existingPos = new int[2];
		if(curPos.getPlayerToMove().equals(white)) {
			toCheckPos[0] = curPos.getWhitePosition().getTile().getColumn();
			toCheckPos[1] = curPos.getWhitePosition().getTile().getRow();
			
			existingPos[0] = curPos.getBlackPosition().getTile().getColumn();
			existingPos[1] = curPos.getBlackPosition().getTile().getRow();
			
		} else {
			toCheckPos[0] = curPos.getBlackPosition().getTile().getColumn();
			toCheckPos[1] = curPos.getBlackPosition().getTile().getRow();
			
			existingPos[0] = curPos.getWhitePosition().getTile().getColumn();
			existingPos[1] = curPos.getWhitePosition().getTile().getRow();
		}
		//0 = column, 1 = row
		
		if(dir.equals(MoveDirection.North)) {
			if(toCheckPos[1] - 1 == existingPos[1] && toCheckPos[0] == existingPos[0]) return false;
			return QuoridorController.noWallBlock(curPos.getPlayerToMove(), -1, 0);
		} else if(dir.equals(MoveDirection.South)) {
			if(toCheckPos[1] + 1 == existingPos[1] && toCheckPos[0] == existingPos[0]) return false;
			return QuoridorController.noWallBlock(curPos.getPlayerToMove(), 1, 0);
		} else if(dir.equals(MoveDirection.East)) {
			if(toCheckPos[0] - 1 == existingPos[0] && toCheckPos[1] == existingPos[1]) return false;
			return QuoridorController.noWallBlock(curPos.getPlayerToMove(), 0, 1);
		} else if(dir.equals(MoveDirection.West)) {
			if(toCheckPos[0] + 1 == existingPos[0] && toCheckPos[1] == existingPos[1]) return false;
			return QuoridorController.noWallBlock(curPos.getPlayerToMove(), 0, -1);
		}
		return false;
  }


  /**
   * Returns if it is legal to jump in the given direction
   */
  // line 113 "../../../../../PawnStateMachine.ump"
  public boolean isLegalJump(MoveDirection dir){
    GamePosition curPos = currentGame.getCurrentPosition();
			Player white = currentGame.getWhitePlayer();
			int whiteCol = curPos.getWhitePosition().getTile().getColumn();
			int whiteRow = curPos.getWhitePosition().getTile().getRow();
			int blackCol = curPos.getBlackPosition().getTile().getColumn();
			int blackRow = curPos.getBlackPosition().getTile().getRow();
			
			int rChange = 0, cChange = 0;
			if(dir == MoveDirection.North) rChange = -2;
			else if(dir == MoveDirection.South) rChange = 2;
			else if(dir == MoveDirection.East) cChange = 2;
			else if(dir == MoveDirection.South) rChange = -2;
			if(curPos.getPlayerToMove().equals(white)) {
				//Moving left or right wall check
				if(cChange != 0) {
					if(blackRow != whiteRow || blackCol != (whiteCol + cChange / 2) ) return false;
					whiteCol += cChange;
					if(whiteCol < 1 || whiteCol > 9) return false;
					for(WallMove w : QuoridorController.getWalls()) {
						if(w.getWallDirection() == Direction.Vertical) {
							
							//If left- check col -1, -2. If right- check col +0, +1
							int tmp;
							if(cChange < 0) tmp = -2;
							else tmp = 0;
							
							int checkCol = (whiteCol -cChange) + tmp;
							if((w.getTargetTile().getColumn() == checkCol ||w.getTargetTile().getColumn() == checkCol + 1)  && 
							   (w.getTargetTile().getRow() == whiteRow || w.getTargetTile().getRow() == whiteRow - 1)) {
								return false;
							}
						}
						//Horizontal Wall can't block right/left path
					}	
				}
				//Moving up or down wall check
				if(rChange != 0) {
					if(blackCol != whiteCol || blackRow != (whiteRow + rChange / 2) ) return false;
					whiteRow += rChange;
					if(whiteRow < 1 || whiteRow > 9) return false;
					for(WallMove w : QuoridorController.getWalls()) {
						
						if(w.getWallDirection() == Direction.Horizontal) {
							//If up- check row -1, -2. If down- check row +0, +1
							int tmp;
							if(rChange < 0) tmp = -2;
							else tmp = 0;
							
							int checkRow = (whiteRow -rChange) + tmp;
							
							if((w.getTargetTile().getRow() == checkRow || w.getTargetTile().getRow() == checkRow + 1)
								&& (w.getTargetTile().getColumn() == whiteCol || w.getTargetTile().getColumn() == whiteCol - 1)) {
								return false;
							}
						}
						//Vertical Wall can't block up/down path
					}
				}
				
				if((blackRow == whiteRow) && (blackCol == whiteCol)) return false;
			} else {
				//Moving left or right wall check
				if(cChange != 0) {
					if(blackRow != whiteRow || whiteCol != (blackCol + cChange / 2) ) return false;
					blackCol += cChange;
					if(blackCol < 1 || blackCol > 9) return false;
					for(WallMove w : QuoridorController.getWalls()) {
						if(w.getWallDirection() == Direction.Vertical) {
							
							//If left- check col -1, -2. If right- check col +0, +1
							int tmp;
							if(cChange < 0) tmp = -2;
							else tmp = 0;
							
							int checkCol = (blackCol -cChange) + tmp;

							if((w.getTargetTile().getColumn() == checkCol ||w.getTargetTile().getColumn() == checkCol + 1)  && 
							   (w.getTargetTile().getRow() == blackRow || w.getTargetTile().getRow() == blackRow - 1)) {
								return false;
							}
							
						}
						//Horizontal Wall can't block right/left path
					}	
				}
				//Moving up or down wall check
				if(rChange != 0) {
					if(blackCol != whiteCol || whiteRow != (blackRow + rChange / 2) ) return false;
					blackRow += rChange;
					if(blackRow < 1 || blackRow > 9) return false;
					for(WallMove w : QuoridorController.getWalls()) {
						if(w.getWallDirection() == Direction.Horizontal) {
							//If up- check row -1, -2. If down- check row +0, +1
							int tmp;
							if(rChange < 0) tmp = -2;
							else tmp = 0;
							
							int checkRow = (blackRow -rChange) + tmp;
							
							if((w.getTargetTile().getRow() == checkRow || w.getTargetTile().getRow() == checkRow + 1)
								&& (w.getTargetTile().getColumn() == blackCol || w.getTargetTile().getColumn() == blackCol - 1)) {
								return false;
							}

						}
						//Vertical Wall can't block up/down path
					}
				}
				
				if((blackRow == whiteRow) && (blackCol == whiteCol)) return false;
			}
			return true;
  }


  /**
   * Action to be called when an illegal move is attempted
   */
  // line 229 "../../../../../PawnStateMachine.ump"
  public void illegalMove(){
    //Taken care of in view?
    	//throw new RuntimeException("this is a illegal move");
  }

  // line 235 "../../../../../PawnStateMachine.ump"
  public void MakeMove(){
    
  }

  // line 238 "../../../../../PawnStateMachine.ump"
  public void MakeJump(){
    
  }

  // line 240 "../../../../../PawnStateMachine.ump"
  public void illegalJump(){
    //Taken care of in view?
    	//throw new RuntimeException("this is a illegal jump");
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 245 "../../../../../PawnStateMachine.ump"
  enum MoveDirection 
  {
    East, South, West, North;
  }

  
}