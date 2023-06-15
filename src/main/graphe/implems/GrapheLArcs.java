package src.main.graphe.implems;

import src.main.graphe.core.Arc;
import src.main.graphe.core.IGraphe;
import src.main.graphe.core.IGrapheConst;
import src.main.graphe.exceptions.ArcExistantException;
import src.main.graphe.exceptions.ArcInexistantException;
import src.main.graphe.exceptions.SommetInexistantException;

import java.util.*;

import static src.main.graphe.core.Arc.EMPTY_EDGE_DESTINATION;

/**
 * Representation of a graph with a list of arcs.
 *
 * @see IGraphe
 */
public class GrapheLArcs implements IGraphe {
    /**
     * List of arcs
     *
     * @see Arc
     */
    private final List<Arc> arcs;

    /**
     * Default constructor of the class.
     * Creates an empty graph.
     */
    public GrapheLArcs() {
        arcs = new ArrayList<>();
    }

    /**
     * Creates a graph from a string representation.
     *
     * @param str the string representation of the graph
     * @see IGraphe#peupler(String)
     */
    public GrapheLArcs(String str) {
        this();
        peupler(str);
    }

    @Override
    public void ajouterSommet(String noeud) {
        if (!contientSommet(noeud))
            arcs.add(new Arc(noeud));
    }

    @Override
    public void ajouterArc(String source, String destination, Integer valeur) {
        if (contientArc(source, EMPTY_EDGE_DESTINATION))
            oterArc(source, EMPTY_EDGE_DESTINATION);
        else if (contientArc(source, destination))
            throw new ArcExistantException();
        if (!contientSommet(destination))
            ajouterSommet(destination);
        arcs.add(new Arc(source, destination, valeur));
    }

    @Override
    public void oterSommet(String noeud) {
        arcs.removeIf(arc -> arc.getSource().equals(noeud) || arc.getDestination().equals(noeud));
    }

    @Override
    public void oterArc(String source, String destination) {
        if (!arcs.removeIf(arc -> arc.getSource().equals(source) && arc.getDestination().equals(destination)))
            throw new ArcInexistantException();
    }

    @Override
    public List<String> getSommets() {
        HashSet<String> nodes = new HashSet<>();
        for (Arc arc : arcs)
            nodes.add(arc.getSource());
        List<String> nodesList = new ArrayList<>(nodes);
        Collections.sort(nodesList);
        return nodesList;
    }

    @Override
    public List<String> getSucc(String sommet) {
        if (!contientSommet(sommet))
            throw new SommetInexistantException();
        List<String> succ = new ArrayList<>();
        for (Arc arc : arcs)
            if (Objects.equals(arc.getSource(), sommet) && !Objects.equals(arc.getDestination(), EMPTY_EDGE_DESTINATION))
                succ.add(arc.getDestination());
        Collections.sort(succ);
        return succ;
    }

    @Override
    public int getValuation(String src, String dest) {
        for (Arc arc : arcs)
            if (arc.equals(new Arc(src, dest, 0)))
                return arc.getValuation();
        return IGrapheConst.NO_EDGE;
    }

    @Override
    public boolean contientSommet(String sommet) {
        for (Arc arc : arcs)
            if (Objects.equals(arc.getSource(), sommet) || Objects.equals(arc.getDestination(), sommet))
                return true;
        return false;
    }

    @Override
    public boolean contientArc(String src, String dest) {
        for (Arc arc : arcs)
            if (arc.equals(dest.equals(EMPTY_EDGE_DESTINATION) ? new Arc(src) : new Arc(src, dest, 0)))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return this.toAString();
    }
}
