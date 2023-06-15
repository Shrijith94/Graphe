package src.main.graphe.exceptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * Exception thrown when a node does not exist, and it is expected to.
 */
public class SommetInexistantException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 5L;

    /**
     * Default constructor with predefined message.
     */
    public SommetInexistantException() {
        super("Le sommet n'existe pas.");
    }

}
