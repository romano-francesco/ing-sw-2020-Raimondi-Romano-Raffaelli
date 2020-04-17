package it.polimi.ingsw;

import java.util.List;
import java.util.Map;

public class ApolloDecorator extends PlayerMoveDecorator {

    private PlayerIndex playerOpponent;


    public ApolloDecorator() {
        String godName = "Apollo";
        super.setGodName(godName);
        String description = "Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.";
        super.setGodDescription(description);
    }


    public void setChosenGod(Boolean condition) {
        super.setChosenGod(condition);
    }

    @Override
    public int getPowerListDimension() {
        return 1;
    }

    @Override
    public boolean canMove(Map<Position, PlayerIndex> adjacentPlayerList, Cell moveCell) throws InvalidPositionException {

        if(moveCell.getPosition().col > 4 || moveCell.getPosition().row > 4 || moveCell.getPosition().col < 0 || moveCell.getPosition().row < 0) throw new InvalidPositionException(moveCell.getPosition().row, moveCell.getPosition().col);
        if(adjacentPlayerList == null) throw new NullPointerException("adjacentPlayerList is null!");

        super.setActivePower(false);
        for(Position p : adjacentPlayerList.keySet()){
            //check if there is a player
            if ((p.equals(moveCell.getPosition())) && ((moveCell.getLevel() - super.getCellOccupied().getLevel()) <= 1)) {
                if(!(adjacentPlayerList.get(p).equals(super.getPlayerNum()))){
                    super.setActivePower(true);}
                return true;
            }
        }

        if(super.getCellOccupied().getPosition().isAdjacent(moveCell.getPosition())){
            //check if there is a dome
            if(moveCell.hasDome()) return false;
            //if cantGoUp is true, check if it is a level up move
            if(super.getCantGoUp()){
                if(moveCell.getLevel() > super.getCellOccupied().getLevel()) return false;
            }
            return moveCell.getLevel() - super.getCellOccupied().getLevel() <= 1;
        }
        return false;
    }

    @Override
    public boolean canUsePower(List<Cell> adjacentList, Map<Position, PlayerIndex> adjacentPlayerList) {
        if(super.getActivePower()){
            for(Position p : adjacentPlayerList.keySet()){
                if(adjacentList.get(0).getPosition().equals(p)){
                    this.playerOpponent = adjacentPlayerList.get(p);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public BoardChange usePower(Cell powerCell) {
        super.setActivePower(false);
        Position startPosition = super.getCellOccupied().getPosition();
        BoardChange boardChange = new BoardChange(super.getCellOccupied().getPosition(),powerCell.getPosition(),super.getPlayerNum());
        boardChange.addPlayerChanges(powerCell.getPosition(),startPosition,this.playerOpponent);
        return boardChange;

    }
}
