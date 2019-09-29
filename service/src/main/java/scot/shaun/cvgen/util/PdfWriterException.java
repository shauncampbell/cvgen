package scot.shaun.cvgen.util;

public class PdfWriterException extends RuntimeException
{
    public PdfWriterException(String errorMessage, Throwable cause)
    {
        super(errorMessage, cause);
    }
}
