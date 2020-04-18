package it.polimi.ingsw.exception;

public class MissingWorkerException extends RuntimeException {
    public MissingWorkerException(int workersNumber){
        super("A player has" + workersNumber + "workers on the map, must be 2");
    }
}