<<<<<<< HEAD

=======
>>>>>>> iteration-4
Feature: Drop Wall
  As a player, I wish to drop a wall after I navigated it to a designated 
  (valid) target position in order to register my wall placement as my move.

  Background: 
    Given The game is running
    Given The following walls exist:
      | wrow | wcol | wdir       |
      |    1 |    1 | horizontal |
      |    7 |    4 | vertical   |
    And It is my turn to move
    And I have a wall in my hand over the board

  Scenario Outline: Valid wall placement 
<<<<<<< HEAD
    Given The wall move candidate with "<dir>" at position <row>, <col> is valid
    When I release the wall in my hand
    Then A wall move shall be registered with "<dir>" at position <row>, <col>
=======
    Given The wall move candidate with "<dir>" at position (<row>, <col>) is valid
    When I release the wall in my hand
    Then A wall move shall be registered with "<dir>" at position (<row>, <col>)
>>>>>>> iteration-4
    And I shall not have a wall in my hand
    And My move shall be completed
    And It shall not be my turn to move

    Examples: 
      | dir        | row | col |
      | horizontal |   3 |   2 |
      | vertical   |   5 |   6 |

  Scenario Outline: Invalid wall placement
<<<<<<< HEAD
    Given The wall move candidate with "<dir>" at position <row>, <col> is invalid
=======
    Given The wall move candidate with "<dir>" at position (<row>, <col>) is invalid
>>>>>>> iteration-4
    When I release the wall in my hand
    Then I shall be notified that my wall move is invalid
    And I shall have a wall in my hand over the board
    And It shall be my turn to move
<<<<<<< HEAD
  	But No wall move shall be registered with "<dir>" at position <row>, <col>
=======
  	But No wall move shall be registered with "<dir>" at position (<row>, <col>)
>>>>>>> iteration-4
  	
    Examples: 
      | dir        | row | col |
      | vertical 	 |   1 |   1 |
      | horizontal |   1 |   2 |
  	  | horizontal |   7 |   4 |
<<<<<<< HEAD
      | vertical 	 |   6 |   6 |

  	
=======
      | vertical 	 |   6 |   4 |
  	
>>>>>>> iteration-4
