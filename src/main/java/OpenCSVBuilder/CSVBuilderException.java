package OpenCSVBuilder;

public class CSVBuilderException extends RuntimeException{
    public enum ExceptionType{
        CENSUS_FILE_PROBLEM,
        UNABLE_TO_PARSE,
        FIlE_NULL_EXCEPTION
    }

    public ExceptionType type;

    public CSVBuilderException(String message, ExceptionType type){
        super(message);
        this.type=type;
    }


}
