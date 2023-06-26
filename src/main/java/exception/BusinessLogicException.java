package exception;

public class BusinessLogicException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BusinessLogicException(String msg){
        super(msg);
    }
}