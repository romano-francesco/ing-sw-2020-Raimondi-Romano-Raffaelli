package it.polimi.ingsw.Client;

import it.polimi.ingsw.model.board.BuildType;
import it.polimi.ingsw.network.ServerConnection;
import it.polimi.ingsw.utils.*;

import javax.swing.*;

public class ClientManager {

    private final ServerConnection serverConnection;
    private final ClientModel clientModel;
    //private final ClientView clientView;

    public ClientManager(ServerConnection serverConnection){
        this.serverConnection = serverConnection;
        this.clientModel = new ClientModel();
    }

    /**
     * Method that receives notifies from the model and modifies the client representation of the model
     * @param message used to take the value to use to modify the model client
     */
    public void updateClient(Message message){
        if(message == null)
            throw new NullPointerException("message");
        switch (message.getType()){
            case NICKNAME:
                updateNicknames((NicknameMessage) message);
                break;
            case CURRENT_PLAYER:
                updateCurrentPlayer((CurrentPlayerMessage) message);
            case PLAYERINDEX_CONNECTION:
                updateIndex((ConnectionPlayerIndex) message);
            case UPDATE_STATE:
                updateState((UpdateStateMessage) message);
                break;
            case ACTION_MESSAGE:
                updateAction((ActionMessage) message);
                break;
            case GODLIKE_CHOOSE_CARDS:
                updateGodCards((GodLikeChoseMessage) message);
                break;
            case SELECT_CARD:
                updateSelectedCard((PlayerSelectGodMessage) message);
                break;
            case GODLIKE_CHOOSE_FIRST_PLAYER:
                //client-view
                break;
            case PUT_WORKER:
                updatePutWorkerMessage((PutWorkerMessage) message);
                break;
            case MOVE:
                updateMoveMessage((MoveMessage) message);
                break;
            case BUILD:
                updateBuildMessage((BuildMessage) message);
                break;
            case BUILD_POWER:
                updateBuildPowerMessage((BuildPowerMessage) message);
                break;
            case LOSER:
                updateLoserMessage((LoserMessage) message);
                break;
            case ERROR:
                //clientView.receiveErrorMessage(message.getErrorMessage());
                default:
                //error message
                break;
        }


    }

    /**
     * Method that receives input from user and send the message to the server
     * @param message is the message to send
     * */
    public void sendToServer(Message message){
        if(message == null)
            throw new NullPointerException("message");
        serverConnection.sendToServer(message);
    }

    /**
     * The following methods will update the model rep of the client
     * */

    public void updateNicknames(NicknameMessage message){
        clientModel.addNickname(message.getNickname());
        if(message.getClient() == clientModel.getPlayerIndex()){
            clientModel.setPlayerNickname(message.getNickname());
        }
    }

    public void updateCurrentPlayer(CurrentPlayerMessage message){
        if(message.getCurrentPlayerIndex() == clientModel.getPlayerIndex()){
            clientModel.setAmICurrentPlayer(true);
        }
        else
            clientModel.setAmICurrentPlayer(false);
    }

    public void updateIndex(ConnectionPlayerIndex message){
        clientModel.setPlayerIndex(message.getPlayerIndex());
    }

    public void updateState(UpdateStateMessage message){
        clientModel.setCurrentState(message.getGameState());
    }

    public void updateAction(ActionMessage message){
        clientModel.setActionPositions(message.getActionType(), message.getPossiblePosition());
    }

    public void updateGodCards(GodLikeChoseMessage message){
        for(String god : message.getGodNames()){
            clientModel.addGodChosen(god);
        }
    }

    public void updateSelectedCard(PlayerSelectGodMessage message){
        if(clientModel.isAmICurrentPlayer()){
            clientModel.setClientGod(message.getGodName());
        }
        //TODO: altri giocatori cosa vedono?
    }

    public void updatePutWorkerMessage(PutWorkerMessage message){
        clientModel.putWorker(message.getClient(), message.getPositionOne());
        clientModel.putWorker(message.getClient(), message.getPositionTwo());
    }

    public void updateMoveMessage(MoveMessage message){
        clientModel.movePlayer(message.getWorkerPosition(), message.getMovePosition());
    }

    public void updateBuildMessage(BuildMessage message){
        clientModel.incrementLevel(message.getBuildPosition());
    }

    public void updateBuildPowerMessage(BuildPowerMessage message){
        if(message.getBuildType() == BuildType.DOME){
            clientModel.addDome(message.getBuildPosition());
        }
        else
            clientModel.incrementLevel(message.getBuildPosition());
    }

    public void updateLoserMessage(LoserMessage message){
        //clientModel.lose(message.getLoserPlayer());
    }
}