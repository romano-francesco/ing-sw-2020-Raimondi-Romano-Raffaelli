package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.exception.InvalidPositionException;
import it.polimi.ingsw.model.board.BoardChange;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.Position;

import java.util.List;
import java.util.Map;

/**
 * General class that implements the basic actions of a player
 * it will be specialized after the choice of the God through the Decorator pattern
 */
public class Player implements PlayerInterface {

    private final String nickName;
    private final PlayerIndex playerNum;
    private Cell oldCell;
    private Cell cellOccupied;
    private boolean cantGoUp;

    public Player(String nickName, PlayerIndex playerNum) {
        this.nickName = nickName;
        this.playerNum = playerNum;
        this.cantGoUp = false;
    }

    @Override
    public String getNickname() {
        return this.nickName;
    }

    /*Set values of the situation after the first insert of a worker*/
    @Override
    public void setStartingWorkerSituation(Cell cellOccupied, boolean cantGoUp) {
        this.cellOccupied = cellOccupied;
        this.cantGoUp = cantGoUp;
    }

    /*Set values of the current information about the selected tile*/
    @Override
    public void setWorkerSituation(Cell oldCell, Cell cellOccupied, boolean cantGoUp){
        this.oldCell = oldCell;
        this.cellOccupied = cellOccupied;
        this.cantGoUp = cantGoUp;
    }

    /*Set values of the situation after a move*/
    @Override
    public void setAfterMove(Cell oldCell, Cell cellOccupied){
        this.oldCell = oldCell;
        this.cellOccupied = cellOccupied;
    }

    @Override
    public void setCantGoUp(boolean cantGoUp){
        this.cantGoUp = cantGoUp;
    }

    @Override
    public boolean canMove(Map<Position,PlayerIndex> adjacentPlayerList, Cell moveCell) throws InvalidPositionException {
        if(moveCell.getPosition().col > 4 || moveCell.getPosition().row > 4 || moveCell.getPosition().col < 0 || moveCell.getPosition().row < 0) throw new InvalidPositionException(moveCell.getPosition().row, moveCell.getPosition().col);
        if(adjacentPlayerList == null) throw new NullPointerException("adjacentPlayerList is null!");

        for(Position p : adjacentPlayerList.keySet()){
            //check if there is a player
            if(p.equals(moveCell.getPosition())) return false;
        }

        if(this.cellOccupied.getPosition().isAdjacent(moveCell.getPosition())){
            //check if there is a dome
            if (moveCell.hasDome()) return false;
            //if cantGoUp is true, check if it is a level up move
            if (this.cantGoUp) {
                if (moveCell.getLevel() > this.cellOccupied.getLevel()) return false;
            }
            return moveCell.getLevel() - this.cellOccupied.getLevel() <= 1;
        }

        return false;
    }

    @Override
    public boolean canMoveWithPowers(Map<Position, PlayerIndex> adjacentPlayerList, List<Cell> moveCell, Cell occupiedCell, boolean cantGoUp) throws InvalidPositionException, NullPointerException {
        this.setStartingWorkerSituation(occupiedCell, cantGoUp);
        return moveCell.stream().anyMatch(cell -> canMove(adjacentPlayerList, cell));
    }

    @Override
    public boolean canBuild(Map<Position, PlayerIndex> adjacentPlayerList, Cell buildCell) {
        if (buildCell.getPosition().col > 4 || buildCell.getPosition().row > 4 || buildCell.getPosition().col < 0 || buildCell.getPosition().row < 0)
            throw new InvalidPositionException(buildCell.getPosition().row, buildCell.getPosition().col);
        if (adjacentPlayerList == null) throw new NullPointerException("adjacentPlayerList is null!");

        //check if the cell is occupied by a player
        for (Position p : adjacentPlayerList.keySet()) {
            if (p.equals(buildCell.getPosition())) return false;
        }

        if (this.cellOccupied.getPosition().isAdjacent(buildCell.getPosition())) {
            if (!buildCell.hasDome()) return true;
        }

        return false;
    }

    @Override
    public boolean hasWin() throws NullPointerException{
        if(this.oldCell == null) throw new NullPointerException("Worker never moved yet!");
        if(this.cellOccupied == null) throw new NullPointerException("Worker has not a cell occupation!");

        return this.oldCell.getLevel() == 2 && this.cellOccupied.getLevel() == 3;
    }

    @Override
    public void move(Cell newOccupiedCell) throws NullPointerException{
        if(newOccupiedCell == null) throw new NullPointerException("newOccupiedCell is null!");
        if(this.cellOccupied == null) throw new NullPointerException("cellOccupied is null!");

        this.oldCell = this.cellOccupied;
        this.cellOccupied = newOccupiedCell;
    }

    @Override
    public boolean canUsePower(List<Cell> adjacentList, Map<Position, PlayerIndex> adjacentPlayerList){
        return false;
    }

    @Override
    public BoardChange usePower(Cell powerCell){
        return null;
    }

    @Override
    public Cell getOldCell() throws NullPointerException{
        if(this.cellOccupied == null) throw new NullPointerException("Worker has not an old cell");

        return new Cell(this.oldCell);
    }

    @Override
    public Cell getCellOccupied() throws NullPointerException{
        if(this.cellOccupied == null) throw new NullPointerException("Worker has not a cell occupied");

        return new Cell(this.cellOccupied);
    }

    @Override
    public Position getSecondPowerPosition(Position firstPowerPos) {
        throw new IllegalStateException("this god does not have a second power position");
    }

    @Override
    public boolean getCantGoUp(){
        return this.cantGoUp;
    }

    @Override
    public PlayerIndex getPlayerNum() {
        return this.playerNum;
    }

    @Override
    public int getPowerListDimension() {
        return 0;
    }

    @Override
    public String getGodName() {
        return null;
    }

    @Override
    public GameState getPowerState(){
        return GameState.NULL;
    }

    @Override
    public GameState getNextState(){
        return GameState.NULL;
    }

    @Override
    public String toString() {
        return "Player nickname: "
                + this.nickName;
    }
}
