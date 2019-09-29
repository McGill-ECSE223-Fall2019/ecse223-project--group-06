/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/


import java.util.*;

// line 24 "main.ump"
public class Pawn
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private boolean color;
  private String pawnPosition;

  //Pawn Associations
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(boolean aColor, String aPawnPosition, Player aPlayer)
  {
    color = aColor;
    pawnPosition = aPawnPosition;
    if (aPlayer == null || aPlayer.getPawn() != null)
    {
      throw new RuntimeException("Unable to create Pawn due to aPlayer");
    }
    player = aPlayer;
  }

  public Pawn(boolean aColor, String aPawnPosition, String aUsernameForPlayer, Game aGameForPlayer)
  {
    color = aColor;
    pawnPosition = aPawnPosition;
    player = new Player(aUsernameForPlayer, this, aGameForPlayer);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setColor(boolean aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public boolean setPawnPosition(String aPawnPosition)
  {
    boolean wasSet = false;
    pawnPosition = aPawnPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean getColor()
  {
    return color;
  }

  public String getPawnPosition()
  {
    return pawnPosition;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isColor()
  {
    return color;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public void delete()
  {
    Player existingPlayer = player;
    player = null;
    if (existingPlayer != null)
    {
      existingPlayer.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "color" + ":" + getColor()+ "," +
            "pawnPosition" + ":" + getPawnPosition()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null");
  }
}