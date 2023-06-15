package src.main.graphe.implems;

import src.main.graphe.core.Arc;
import src.main.graphe.core.IGraphe;
import src.main.graphe.core.IGrapheConst;
import src.main.graphe.exceptions.ArcExistantException;
import src.main.graphe.exceptions.ArcInexistantException;
import src.main.graphe.exceptions.EmptySommetException;
import src.main.graphe.exceptions.SommetInexistantException;

import java.util.*;

import static src.main.graphe.core.Arc.EMPTY_EDGE_DESTINATION;

/**
 * Representation of a graph with an adjacency list.
 *
 * @see IGraphe
 */
public class GrapheLAdj implements IGraphe {
    /**
     * Adjacency list : map that matches a node name to the list of their outgoing arcs.
     *
     * @see Arc
     */
    private final Map<String, List<Arc>> ladj;

    /**
     * Default constructor of the class.
     * Creates an empty graph.
     */
    public GrapheLAdj() {
        ladj = new HashMap<>();
    }

    /**
     * Creates a graph from a string representation.
     *
     * @param str the string representation of the graph
     * @see IGraphe#peupler(String)
     */
    public GrapheLAdj(String str) {
        this();
        peupler(str);
    }

    @Override
    public void ajouterSommet(String noeud) {
        ladj.putIfAbsent(noeud, new ArrayList<>());
    }

    @Override
    public void ajouterArc(String source, String destination, Integer valeur) {
        if (source.isEmpty() || destination.isEmpty())
            throw new EmptySommetException();
        if (contientArc(source, EMPTY_EDGE_DESTINATION))
            oterArc(source, EMPTY_EDGE_DESTINATION);
        else if (contientArc(source, destination))
            throw new ArcExistantException();
        Arc arc = new Arc(source, destination, valeur);
        if (!contientSommet(source))
            ajouterSommet(source);
        if (!contientSommet(destination))
            ajouterSommet(destination);
        ladj.get(source).add(arc);
    }

    @Override
    public void oterSommet(String noeud) {
        ladj.remove(noeud);
        for (List<Arc> arcs : ladj.values())
            arcs.removeIf(arc -> arc.getDestination().equals(noeud));
    }

    @Override
    public void oterArc(String source, String destination) {
        if (!contientArc(source, destination))
            throw new ArcInexistantException();
        ladj.get(source).removeIf(arc -> arc.getDestination().equals(destination));
    }

    @Override
    public List<String> getSommets() {
        List<String> sommets = new ArrayList<>(ladj.keySet());
        Collections.sort(sommets);
        return sommets;
    }

    @Override
    public List<String> getSucc(String sommet) {
        if (!contientSommet(sommet))
            throw new SommetInexistantException();
        List<String> succ = new ArrayList<>();
        for (Arc arc : ladj.get(sommet))
            succ.add(arc.getDestination());
        Collections.sort(succ);
        return succ;
    }

    @Override
    public int getValuation(String src, String dest) {
        if (!contientSommet(src) || !contientSommet(dest))
            throw new SommetInexistantException();
        for (Arc arc : ladj.get(src))
            if (arc.getDestination().equals(dest))
                return arc.getValuation();
        return IGrapheConst.NO_EDGE;
    }

    @Override
    public boolean contientSommet(String sommet) {
        return ladj.containsKey(sommet);
    }

    @Override
    public boolean contientArc(String src, String dest) {
        if (!contientSommet(src))
            return false;
        for (Arc arc : ladj.get(src))
            if (arc.getDestination().equals(dest))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return this.toAString();
    }
}
