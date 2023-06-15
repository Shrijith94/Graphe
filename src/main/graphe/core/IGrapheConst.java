package src.main.graphe.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interface for constant graphs.
 */
public interface IGrapheConst {
    /** Represents that there is no edge between nodes */
    int NO_EDGE = -1;

    /**
     * Returns the list of nodes.
     *
     * @return the list of nodes
     */
    List<String> getSommets();

    /**
     * Returns the list of edges.
     *
     * @param sommet the node
     * @return the list of edges
     * @throws src.main.graphe.exceptions.SommetInexistantException if the node is not in the graph
     */
    List<String> getSucc(String sommet);

    /**
     * Returns the value of an edge.
     *
     * @param src  the source node
     * @param dest the destination node
     * @return the value of the edge, -1 if the edge is not in the graph
     * @see IGrapheConst#NO_EDGE
     */
    int getValuation(String src, String dest);

    /**
     * Verifies if a node is in the graph.
     *
     * @param sommet the node
     * @return true if the node is in the graph, false otherwise
     */
    boolean contientSommet(String sommet);

    /**
     * Verifies if an edge is in the graph.
     *
     * @param src  the source node
     * @param dest the destination node
     * @return true if the edge is in the graph, false otherwise
     */
    boolean contientArc(String src, String dest);

    /**
     * Returns a string representation of the graph.
     */
    default String toAString() {
        List<String> sommetsTries = new ArrayList<>(getSommets());
        Collections.sort(sommetsTries);

        List<String> descriptionsArcs = new ArrayList<>();

        for (String sommet : sommetsTries) {
            List<String> successeurs = getSucc(sommet);

            if (successeurs.isEmpty()) {
                descriptionsArcs.add(sommet + ":");
            } else {
                List<String> successeursTries = new ArrayList<>(successeurs);
                Collections.sort(successeursTries);

                for (String successeur : successeursTries) {
                    int poids = getValuation(sommet, successeur);
                    descriptionsArcs.add(sommet + "-" + successeur + "(" + poids + ")");
                }
            }
        }

        return String.join(", ", descriptionsArcs);
    }
}
