package com.github.shortcube.skeet.database;

/**
 * This class represents an exception in the DAO configuration which cannot be resolved at runtime,
 * such as a missing resource in the classpath, a missing property in the properties file, etcetera.
 *
 * @author Leon Merten
 */
public class DAOConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 6475605299818388205L;

    /**
     * Constructs a DAOConfigurationException with the given detail message.
     * @param message The detail message of the DAOConfigurationException.
     */
    public DAOConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a DAOConfigurationException with the given root cause.
     * @param cause The root cause of the DAOConfigurationException.
     */
    public DAOConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a DAOConfigurationException with the given detail message and root cause.
     * @param message The detail message of the DAOConfigurationException.
     * @param cause The root cause of the DAOConfigurationException.
     */
    public DAOConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
