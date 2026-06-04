package search.search.Exception;

public class SearchServiceException extends RuntimeException {
    public SearchServiceException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
