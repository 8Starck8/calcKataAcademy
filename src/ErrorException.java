import static java.lang.System.*;

public class ErrorException extends Exception{
    public ErrorException (String description)
    {
        out.println("throw Exception //т.к. "+description);
        System.exit(0);
    }

}
