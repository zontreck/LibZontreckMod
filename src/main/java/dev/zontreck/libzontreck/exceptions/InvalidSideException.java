package dev.zontreck.libzontreck.exceptions;

/**
 * Thrown when requesting a world position's level on the client when in the wrong dimension.
 */
public class InvalidSideException extends Exception
{
    public InvalidSideException(String msg){
        super(msg);
    }
}
