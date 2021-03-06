package it.polimi.ingsw.Client;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.message.ActionType;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.PlayerIndex;

import java.util.List;

/**
 * Interface that allows View (CLI/GUI) to access to some getter of the ClientModel
 */
public interface ViewModelInterface {

    List<String> getGods();

    List<String> getGodsDescription();

    List<Position> getActionPositions(Position workerPos, ActionType type);

    Position getSelectedWorkerPos();

    GameState getCurrentState();

    GameState getPowerGodState();

    PlayerIndex getPlayerIndex();

    List<String> getChosenGodsByGodLike();

    boolean isThreePlayersGame();

    boolean isGodLikeChoosingCards();

    String getNickname(PlayerIndex index);

    List<String> getNicknames();

    List<Position> getPlayerIndexPosition(PlayerIndex playerIndex);

    boolean isAmICurrentPlayer();

    boolean isNotThereASelectedWorker();

    boolean isOccupiedPosition(Position pos);

    void setSelectedWorkerPos(Position position);

    String getGodChosenByPlayer(PlayerIndex index);
}
