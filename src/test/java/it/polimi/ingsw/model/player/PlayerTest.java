package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.exception.InvalidPositionException;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerIndex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerTest {

    private static Board board;
    private static Player player;
    private static Cell oldCell;
    private static Cell cellOccupied;
    private static List<Cell> adjacentCells;
    private static Map<Position, PlayerIndex> adjacentPlayerList;

    @Before
    public void init(){
        board = new Board();
        player = new Player("Jack", PlayerIndex.PLAYER0);
        oldCell = new Cell(1,2);
        cellOccupied = new Cell(1,1);
        adjacentCells = new ArrayList<>();
        adjacentPlayerList = new HashMap<>();
    }

    @Test
    public void canGoUpTest(){
        player.setCantGoUp(true);
        assertTrue(player.getCantGoUp());
    }

    @Test
    public void nickNameIsCorrectTest(){
        assertEquals("Jack",player.getNickname());
    }

    @Test
    public void canMoveTest(){

        player.setWorkerSituation(oldCell, cellOccupied, false);
        //Create a board situation, with different cell levels and player on the board
        Cell cell00 = new Cell(0,0);
        Cell cell01 = new Cell(0,1);
        Cell cell02 = new Cell(0,2);
        Cell cell10 = new Cell(1,0);
        Cell cell12 = new Cell(1,2);
        Cell cell20 = new Cell(2,0);
        Cell cell21 = new Cell(2,1);
        Cell cell22 = new Cell(2,2);

        board.constructBlock(new Position(0,1));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.putWorker(new Position(2,0), PlayerIndex.PLAYER0);
        board.constructBlock(new Position(2,1));
        board.putWorker(new Position(2,1), PlayerIndex.PLAYER1);
        board.constructBlock(new Position(2,2));
        board.constructBlock(new Position(2,2));
        board.putWorker(new Position(2,2), PlayerIndex.PLAYER2);

        //adjacentCells = board.getAdjacentCells(cellOccupied.getPosition());

        adjacentPlayerList = board.getAdjacentPlayers(cellOccupied.getPosition());

        /*Adjacent cells tested representation:
         *
         *   0      1      2
         *   3      X      3+D
         *   0+P0   1+P1   2+P2
         *
         * where X is the current worker cell
         * */

        /* Test when cantGoUp is false! */

        //Check move level 0 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 0 -> level 1
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell01.getPosition())));
        //Check move level 0 -> level 2
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell02.getPosition())));
        //Check move level 0 -> level 3
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell10.getPosition())));
        //Check move level 0 -> level 0 + occupation == PLAYER1
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell20.getPosition())));
        //Check move level 0 -> level 1 + occupation == PLAYER2
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell21.getPosition())));
        //Check move level 0 -> level 2 + occupation == PLAYER3
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell22.getPosition())));

        //Upgrade the level of the Cell where worker is to level 1
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 1);

        //Check move level 1 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 1 -> level 2
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell02.getPosition())));
        //Check move level 1 -> level 3
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell10.getPosition())));

        //Upgrade the level of the Cell where worker is to level 2
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 2);

        //Check move level 2 -> level 3 + occupation == DOME
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell12.getPosition())));
        //Check move level 2 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 2 -> level 1
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell01.getPosition())));
        //Check move level 2 -> level 3
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell10.getPosition())));

        //Upgrade the level of the Cell where worker is to level 3
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 3);

        //Check move level 3 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 3 -> level 1
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell01.getPosition())));
        //Check move level 3 -> level 2
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell02.getPosition())));
        //Check move level 3 -> level 0 + occupation == PLAYER1
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell20.getPosition())));

        //Check move on a not in adjacentCells cell
        Cell notAdj = new Cell(2,3);
        assertFalse(player.canMove(adjacentPlayerList, notAdj));

        //InvalidPositionException check:
        try{
            player.canMove(adjacentPlayerList, new Cell(5,4));
        }
        catch(InvalidPositionException e){
            assertEquals("You cannot have a position in : [" + 5 + "][" + 4 + "]", e.getMessage());
        }

        //NullPointerException for adjacentPlayerList check:
        try{
            player.canMove(new HashMap<Position,PlayerIndex>(), new Cell(3,2));
        }
        catch(NullPointerException e){
            assertEquals("adjacentPlayerList is null!", e.getMessage());
        }
    }

    @Test
    public void canMoveCantGoUp(){
        player.setWorkerSituation(oldCell, cellOccupied, true);
        //Create a board situation, with different cell levels and player on the board
        Cell cell00 = new Cell(0,0);
        Cell cell01 = new Cell(0,1);
        Cell cell02 = new Cell(0,2);
        Cell cell10 = new Cell(1,0);
        Cell cell12 = new Cell(1,2);
        /*Cell cell20 = new Cell(2,0);
        Cell cell21 = new Cell(2,1);
        Cell cell22 = new Cell(2,2);*/

        board.constructBlock(new Position(0,1));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.putWorker(new Position(2,0), PlayerIndex.PLAYER0);
        board.constructBlock(new Position(2,1));
        board.putWorker(new Position(2,1), PlayerIndex.PLAYER1);
        board.constructBlock(new Position(2,2));
        board.constructBlock(new Position(2,2));
        board.putWorker(new Position(2,2), PlayerIndex.PLAYER2);

        //adjacentCells = board.getAdjacentCells(cellOccupied.getPosition());

        adjacentPlayerList = board.getAdjacentPlayers(cellOccupied.getPosition());
        List<Cell> powers = new ArrayList<>();
        powers.add(board.getCell(new Position(4,4)));
        assertFalse(player.canMoveWithPowers(adjacentPlayerList,powers,player.getCellOccupied(),true));

        /*Adjacent cells tested representation:
         *
         *   0      1      2
         *   3      X      3+D
         *   0+P0   1+P1   2+P2
         *
         * where X is the current worker cell
         * */

        /* Test when cantGoUp is true! */

        //Check move level 0 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 0 -> level 1
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell01.getPosition())));
        //Check move level 0 -> level 2
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell02.getPosition())));

        //Upgrade the level of the Cell where worker is to level 1
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), true);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 1);

        //Check move level 1 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 1 -> level 2
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell02.getPosition())));

        //Upgrade the level of the Cell where worker is to level 2
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), true);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 2);

        //Check move level 2 -> level 3 + occupation == DOME
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell12.getPosition())));
        //Check move level 2 -> level 0
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check move level 2 -> level 1
        assertTrue(player.canMove(adjacentPlayerList, board.getCell(cell01.getPosition())));
        //Check move level 2 -> level 3
        assertFalse(player.canMove(adjacentPlayerList, board.getCell(cell10.getPosition())));
    }

    @Test
    public void canBuildTest(){
        player.setAfterMove(oldCell, cellOccupied);

        //Create a board situation, with different cell levels and player on the board
        Cell cell00 = new Cell(0,0);
        //Cell cell01 = new Cell(0,1);
        //Cell cell02 = new Cell(0,2);
        Cell cell10 = new Cell(1,0);
        Cell cell12 = new Cell(1,2);
        Cell cell20 = new Cell(2,0);
        Cell cell21 = new Cell(2,1);
        //Cell cell22 = new Cell(2,2);

        board.constructBlock(new Position(0,1));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(0,2));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,0));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.constructBlock(new Position(1,2));
        board.putWorker(new Position(2,0), PlayerIndex.PLAYER0);
        board.constructBlock(new Position(2,1));
        board.putWorker(new Position(2,1), PlayerIndex.PLAYER1);
        board.constructBlock(new Position(2,2));
        board.constructBlock(new Position(2,2));
        board.putWorker(new Position(2,2), PlayerIndex.PLAYER2);

        adjacentCells = board.getAdjacentCells(cellOccupied.getPosition());

        adjacentPlayerList = board.getAdjacentPlayers(cellOccupied.getPosition());

        /*Adjacent cells tested representation:
         *
         *   0      1      2
         *   3      X      3+D
         *   0+P0   1+P1   2+P2
         *
         * where X is the current worker cell
         * */


        //Check build on level 0
        assertTrue(player.canBuild(adjacentPlayerList, board.getCell(cell00.getPosition())));
        //Check build on level 3
        assertTrue(player.canBuild(adjacentPlayerList, board.getCell(cell10.getPosition())));
        //Check build on level 3 + DOME
        assertFalse(player.canBuild(adjacentPlayerList, board.getCell(cell12.getPosition())));
        //Check build on level 0 + PLAYER1
        assertFalse(player.canBuild(adjacentPlayerList, board.getCell(cell20.getPosition())));
        //Check build on level 1 + PLAYER2
        assertFalse(player.canBuild(adjacentPlayerList, board.getCell(cell21.getPosition())));

        //Check build on a not in adjacentCells cell
        Cell notAdj = new Cell(2,3);
        assertFalse(player.canBuild(adjacentPlayerList, notAdj));

        try{
            player.canBuild(adjacentPlayerList,new Cell(5,5));
        }catch (InvalidPositionException e){
            assertEquals("You cannot have a position in : [5][5]",e.getMessage());
        }

        try{
            player.canBuild(adjacentPlayerList,new Cell(-1,-1));
        }catch (InvalidPositionException e){
            assertEquals("You cannot have a position in : [-1][-1]",e.getMessage());
        }

        List<Cell> powers = new ArrayList<>();
        powers.add(board.getCell(new Position(4,4)));
        assertFalse(player.canUsePower(powers,adjacentPlayerList));
        assertEquals(null,player.usePower(new Cell(4,3)));

        //Upgrade the level of the Cell where worker is to level 1
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 1);
        //Upgrade the level of the Cell where worker is to level 2
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 2);
        //Upgrade the level of the Cell where worker is to level 3
        board.constructBlock(cellOccupied.getPosition());
        player.setStartingWorkerSituation(board.getCell(cellOccupied.getPosition()), false);
        assertEquals(board.getCell(cellOccupied.getPosition()).getLevel(), 3);

        //Check build from level 3 to level 0
        assertTrue(player.canBuild(adjacentPlayerList, board.getCell(cell00.getPosition())));

        //InvalidPositionException check:
        try{
            player.canBuild(adjacentPlayerList, new Cell(5,4));
        }
        catch(InvalidPositionException e){
            assertEquals("You cannot have a position in : [" + 5 + "][" + 4 + "]", e.getMessage());
        }

        //NullPointerException for adjacentPlayerList check:
        try{
            player.canBuild(new HashMap<Position,PlayerIndex>(), new Cell(3,2));
        }
        catch(NullPointerException e){
            assertEquals("adjacentPlayerList is null!", e.getMessage());
        }
    }

    @Test
    public void hasWinTest(){

        //NullPointerException check: player has not oldCell
        player.setStartingWorkerSituation(cellOccupied, false);
        try{
            player.hasWin();
        }
        catch(NullPointerException e){
            assertEquals("Worker never moved yet!", e.getMessage());
        }

        //NullPointerException check: player has not a cellOccupied
        player.setAfterMove(oldCell, null);
        try{
            player.hasWin();
        }
        catch(NullPointerException e){
            assertEquals("Worker has not a cell occupation!", e.getMessage());
        }

        //hasWin() after a move level 0 -> level 0
        player.setAfterMove(oldCell, cellOccupied);

        assertFalse(player.hasWin());

        //hasWin() after a move level 0 -> level 1
        Position lvl1 = new Position(1,2);
        board.constructBlock(lvl1);
        player.setAfterMove(cellOccupied, board.getCell(lvl1));

        assertFalse(player.hasWin());

        //hasWin() after a move level 1 -> level 2
        Position lvl2 = new Position(1,3);
        board.constructBlock(lvl2);
        board.constructBlock(lvl2);

        player.setAfterMove(board.getCell(lvl1), board.getCell(lvl2));

        assertFalse(player.hasWin());

        //hasWin() after a move level 2 -> level 3 (Win Condition)
        Position lvl3 = new Position(1,4);
        board.constructBlock(lvl3);
        board.constructBlock(lvl3);
        board.constructBlock(lvl3);

        player.setAfterMove(board.getCell(lvl2), board.getCell(lvl3));

        assertTrue(player.hasWin());

        //hasWin after a move level 3 -> level 0
        Position lvl0 = new Position(2,4);

        player.setAfterMove(board.getCell(lvl3), board.getCell(lvl0));

        assertFalse(player.hasWin());
    }

    @Test
    public void moveTest(){
        try{
            player.move(cellOccupied);
        }
        catch (NullPointerException e){
            assertEquals("cellOccupied is null!", e.getMessage());
        }

        player.setStartingWorkerSituation(oldCell, false);
        try{
            player.move(null);
        }
        catch (NullPointerException e){
            assertEquals("newOccupiedCell is null!", e.getMessage());
        }

        player.move(cellOccupied);

        assertEquals(player.getOldCell(), oldCell);
        assertEquals(player.getCellOccupied(), cellOccupied);
    }

    @Test
    public void toStringTest(){
        assertEquals(player.toString(), "Player nickname: Jack");
    }


    @Test
    public void testMethodOver(){
        assertEquals(null,player.getGodName());
        assertEquals(GameState.NULL,player.getPowerState());
        assertEquals(GameState.NULL,player.getNextState());
        assertEquals(0,player.getPowerListDimension());
        assertEquals(PlayerIndex.PLAYER0,player.getPlayerNum());

    }


    @After
    public void afterAll(){

        board = null;
        player = null;
        adjacentCells = null;
        adjacentPlayerList = null;
        cellOccupied = null;
        oldCell = null;
    }
}
