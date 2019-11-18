/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import ca.mcgill.ecse223.quoridor.model.*;

// line 6 "../../../../../PawnStateMachine.ump"
public class PawnBehavior
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum MoveDirection { East, South, West, North }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehavior Attributes
  private MoveDirection dir;

  //PawnBehavior State Machines
  public enum PawnSM { Idle, WallMove, PlayerMove }
  private PawnSM pawnSM;

  //PawnBehavior Associations
  private Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehavior(MoveDirection aDir)
  {
    dir = aDir;
    setPawnSM(PawnSM.Idle);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDir(MoveDirection aDir)
  {
    boolean wasSet = false;
    dir = aDir;
    wasSet = true;
    return wasSet;
  }

  public MoveDirection getDir()
  {
    return dir;
  }

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public boolean initMove()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case Idle:
        setPawnSM(PawnSM.PlayerMove);
        wasEventProcessed = true;
        break;
      case WallMove:
        setPawnSM(PawnSM.PlayerMove);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean initGrab()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case Idle:
        setPawnSM(PawnSM.WallMove);
        wasEventProcessed = true;
        break;
      case PlayerMove:
        setPawnSM(PawnSM.WallMove);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean dropWall()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case WallMove:
        setPawnSM(PawnSM.Idle);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move()
  {
    boolean wasEventProcessed = false;
    
    PawnSM aPawnSM = pawnSM;
    switch (aPawnSM)
    {
      case PlayerMove:
        if (!(isLegalStep(getDir()))&&!(isLegalJump(getDir())))
        {
          setPawnSM(PawnSM.PlayerMove);
          wasEventProcessed = true;
          break;
        }
        if (isLegalStep(getDir()))
        {
        // line 30 "../../../../../PawnStateMachine.ump"
          moveStep(dir);
          setPawnSM(PawnSM.Idle);
          wasEventProcessed = true;
          break;
        }
        if (isLegalJump(getDir()))
        {
        // line 31 "../../../../../PawnStateMachine.ump"
          moveJump(dir);
          setPawnSM(PawnSM.Idle);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;
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

  // line 42 "../../../../../PawnStateMachine.ump"
  public void moveStep(MoveDirection dir){
    GamePosition curPos = currentGame.getCurrentPosition();
		Player white = currentGame.getWhitePlayer();

		int whiteCol = curPos.getWhitePosition().getTile().getColumn();
		int whiteRow = curPos.getWhitePosition().getTile().getRow();
		int blackCol = curPos.getBlackPosition().getTile().getColumn();
		int blackRow = curPos.getBlackPosition().getTile().getRow();
		
		int rChange = 0, cChange = 0;
		if(dir == MoveDirection.North) {
			rChange = -1;
		} else if (dir == MoveDirection.South) {
			rChange = 1;
		} else if(dir == MoveDirection.West) {
			cChange = -1;
		} else if (dir == MoveDirection.East) {
			cChange = 1;
		}
		int targetRow, targetCol;
		if(curPos.getPlayerToMove().equals(white)) {
				targetRow = whiteRow + rChange;
				targetCol = whiteCol + cChange;
				PlayerPosition pos = new PlayerPosition(curPos.getPlayerToMove(), QuoridorController.findTile(whiteRow + rChange, whiteCol + cChange));
				curPos.setWhitePosition(pos);
		} else {
				targetRow = blackRow + rChange;
				targetCol = blackCol + rChange;
				PlayerPosition pos = new PlayerPosition(curPos.getPlayerToMove(), QuoridorController.findTile(blackRow + rChange, blackCol + cChange));
				curPos.setBlackPosition(pos);
		}
		
		StepMove move = new StepMove(currentGame.getMoves().size()+1, 
									 currentGame.getMoves().size()/2+1, 
									 curPos.getPlayerToMove(),
									 QuoridorController.findTile(targetRow, targetCol),
									 currentGame);
									 
		currentGame.addMove(move);
		QuoridorController.completeMove(curPos.getPlayerToMove());
  }

  // line 86 "../../../../../PawnStateMachine.ump"
  public void moveJump(MoveDirection dir){
    GamePosition curPos = currentGame.getCurrentPosition();
		Player white = currentGame.getWhitePlayer();

		int whiteCol = curPos.getWhitePosition().getTile().getColumn();
		int whiteRow = curPos.getWhitePosition().getTile().getRow();
		int blackCol = curPos.getBlackPosition().getTile().getColumn();
		int blackRow = curPos.getBlackPosition().getTile().getRow();
		
		int rChange = 0, cChange = 0;
		if(dir == MoveDirection.North) {
			rChange = -2;
		} else if (dir == MoveDirection.South) {
			rChange = 2;
		} else if(dir == MoveDirection.West) {
			cChange = -2;
		} else if (dir == MoveDirection.East) {
			cChange = 2;
		}
		int targetRow, targetCol;
		if(curPos.getPlayerToMove().equals(white)) {
				targetRow = whiteRow + rChange;
				targetCol = whiteCol + cChange;
				PlayerPosition pos = new PlayerPosition(curPos.getPlayerToMove(), QuoridorController.findTile(whiteRow + rChange, whiteCol + cChange));
				curPos.setWhitePosition(pos);
		} else {
				targetRow = blackRow + rChange;
				targetCol = blackCol + rChange;
				PlayerPosition pos = new PlayerPosition(curPos.getPlayerToMove(), QuoridorController.findTile(blackRow + rChange, blackCol + cChange));
				curPos.setBlackPosition(pos);
		}
		
		JumpMove move = new JumpMove(currentGame.getMoves().size()+1, 
									 currentGame.getMoves().size()/2+1, 
									 curPos.getPlayerToMove(),
									 QuoridorController.findTile(targetRow, targetCol),
									 currentGame);
									 
		currentGame.addMove(move);
		QuoridorController.completeMove(curPos.getPlayerToMove());
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 132 "../../../../../PawnStateMachine.ump"
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
  // line 143 "../../../../../PawnStateMachine.ump"
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
  // line 154 "../../../../../PawnStateMachine.ump"
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
  // line 191 "../../../../../PawnStateMachine.ump"
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
  // line 307 "../../../../../PawnStateMachine.ump"
  public void illegalMove(){
    //Taken care of in view?
    	//throw new RuntimeException("this is a illegal move");
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dir" + "=" + (getDir() != null ? !getDir().equals(this)  ? getDir().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentGame = "+(getCurrentGame()!=null?Integer.toHexString(System.identityHashCode(getCurrentGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}