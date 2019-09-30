/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 39 "../../../../../main.ump"
public class Tile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private String tilePosition;

  //Tile Associations
  private Board board;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(String aTilePosition, Board aBoard)
  {
    tilePosition = aTilePosition;
    boolean didAddBoard = setBoard(aBoard);
    if (!didAddBoard)
    {
      throw new RuntimeException("Unable to create tile due to board");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTilePosition(String aTilePosition)
  {
    boolean wasSet = false;
    tilePosition = aTilePosition;
    wasSet = true;
    return wasSet;
  }

  public String getTilePosition()
  {
    return tilePosition;
  }
  /* Code from template association_GetOne */
  public Board getBoard()
  {
    return board;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setBoard(Board aBoard)
  {
    boolean wasSet = false;
    //Must provide board to tile
    if (aBoard == null)
    {
      return wasSet;
    }

    //board already at maximum (81)
    if (aBoard.numberOfTiles() >= Board.maximumNumberOfTiles())
    {
      return wasSet;
    }
    
    Board existingBoard = board;
    board = aBoard;
    if (existingBoard != null && !existingBoard.equals(aBoard))
    {
      boolean didRemove = existingBoard.removeTile(this);
      if (!didRemove)
      {
        board = existingBoard;
        return wasSet;
      }
    }
    board.addTile(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Board placeholderBoard = board;
    this.board = null;
    if(placeholderBoard != null)
    {
      placeholderBoard.removeTile(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "tilePosition" + ":" + getTilePosition()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "board = "+(getBoard()!=null?Integer.toHexString(System.identityHashCode(getBoard())):"null");
  }
}