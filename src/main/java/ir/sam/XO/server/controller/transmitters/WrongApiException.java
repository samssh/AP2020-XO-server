package ir.sam.XO.server.controller.transmitters;

public class WrongApiException extends Exception{
    public WrongApiException(ClassCastException cause) {
        super(cause);
    }
}
