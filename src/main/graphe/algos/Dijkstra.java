package src.main.graphe.algos;

import src.main.graphe.core.IGrapheConst;

import java.util.*;

/**
 * Classe qui calcule le chemin le plus court entre la source et les autres sommets à l'aide de l'algorithme de Dijkstra.
 */
public final class Dijkstra {
    /**
     * Infini pour représenter la distance entre deux sommets qui ne sont pas connectés.
     * Utilisé à la place de -1 dans l'algorithme pour éviter les problèmes lors de la comparaison des distances.
     */
    private static final Integer INFINITY = Integer.MAX_VALUE;
    /** Graphe qui sera utilisé pour calculer le chemin le plus court */
    private final IGrapheConst graph;
    /** Distance entre la source et les autres sommets */
    private final Map<String, Integer> distance;
    /** Nœuds précédents pour chaque nœud, afin de revenir à la source */
    private final Map<String, String> previous;
    /** File d'attente prioritaire pour obtenir le prochain nœud à visiter */
    private final PriorityQueue<String> queue;
    /** Etat de chaque nœud, pour savoir s'il a été visité ou non */
    private final Map<String, DijkstraState> state;

    /** Enum qui représente l'état d'un nœud */
    private enum DijkstraState{
        /** Noeud encore à visiter */
        NOT_VISITED,
        /** Nœud visité mais non traité */
        VISITED,
        /** Nœud visité et traité */
        VISITED_AND_PROCESSED
    }

    /**
     * Constructeur de la classe Dijkstra qui initialise les structures de données.
     * @param graph Graphe qui sera utilisé pour calculer le chemin le plus court
     */
    private Dijkstra(IGrapheConst graph) {
        this.graph = graph;
        int nbSommets = graph.getSommets().size();
        distance = new HashMap<>(nbSommets);
        previous = new HashMap<>(nbSommets);
        queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        state = new HashMap<>(nbSommets);
        for (String u : graph.getSommets()) {
            distance.put(u, Dijkstra.INFINITY);
            previous.put(u, null);
            state.put(u, DijkstraState.NOT_VISITED);
        }
    }

    /**
     * Calcule le chemin le plus court entre la source et les autres sommets.
     * @param graphe Graphe qui sera utilisé pour calculer le chemin le plus court
     * @param source Nœud source
     * @param dist [out] distance entre la source et les autres sommets
     * @param pred [out] nœuds précédents pour chaque nœud, afin de revenir à la source
     */
    public static void dijkstra(IGrapheConst graphe, String source, Map<String, Integer> dist, Map<String, String> pred) {
        Dijkstra pcc = new Dijkstra(graphe);
        pcc.dijkstra(source);
        for (String sommet : graphe.getSommets()) {
            int distanceSommet = pcc.distance.get(sommet);
            if (distanceSommet == INFINITY)
                dist.put(sommet, IGrapheConst.NO_EDGE);
            else
                dist.put(sommet, distanceSommet);
            pred.put(sommet, pcc.previous.get(sommet));
        }
    }

    /**
     * Diminue la clé du nœud u dans la file d'attente prioritaire.
     * @param u Noeud pour diminuer la clé
     */
    private void decreaseKey(String u) {
        queue.remove(u);
        queue.add(u);
    }

    /**
     * Calcule le chemin le plus court entre la source et les autres sommets à l'aide de l'algorithme de Dijkstra
     * @param source Nœud source
     */
    private void dijkstra(String source) {
        distance.put(source, 0);
        queue.add(source);
        state.put(source, DijkstraState.VISITED);
        while (!queue.isEmpty()) {
            String currentNode = queue.poll();
            for (String neighbourNode : graph.getSucc(currentNode)) {
                int newLength = graph.getValuation(currentNode, neighbourNode) + distance.get(currentNode);
                if (newLength < distance.get(neighbourNode)) {
                    distance.put(neighbourNode, newLength);
                    previous.put(neighbourNode, currentNode);
                    DijkstraState neighbourState = state.get(neighbourNode);
                    if (neighbourState == DijkstraState.NOT_VISITED) {
                        state.put(neighbourNode, DijkstraState.VISITED);
                        queue.add(neighbourNode);
                    } else if (neighbourState == DijkstraState.VISITED) {
                        decreaseKey(neighbourNode);
                    }
                }
            }
            state.put(currentNode, DijkstraState.VISITED_AND_PROCESSED); // Visité et traité
        }
    }
}