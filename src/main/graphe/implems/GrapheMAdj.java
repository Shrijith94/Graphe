package src.main.graphe.implems;

import src.main.graphe.core.IGraphe;
import src.main.graphe.core.IGrapheConst;
import src.main.graphe.exceptions.*;

import java.util.*;

/**
 * Represents a graph with an adjacency matrix.
 *
 * @see IGraphe
 */
public class GrapheMAdj implements IGraphe {
    /**
     * Map of the indexes of the nodes in the adjacency matrix, matches a name to an index
     */
    private final Map<String, Integer> indices;
    /**
     * Adjacency matrix representation of the graph.
     */
    private int[][] matrice;

    /**
     * Default constructor of the class.
     * Creates an empty graph.
     */
    public GrapheMAdj() {
        indices = new HashMap<>();
        matrice = new int[0][0];
    }

    /**
     * Creates a graph from a string representation.
     *
     * @param str the string representation of the graph
     * @see IGraphe#peupler(String)
     */
    public GrapheMAdj(String str) {
        this();
        peupler(str);
    }

    @Override
    public void ajouterSommet(String noeud) {
        if (!contientSommet(noeud)) {
            indices.put(noeud, matrice.length);
            addSommetToMatrix();
        }
    }

    private void addSommetToMatrix() {
        int newSize = matrice.length + 1;
        int[][] newMatrice = new int[newSize][newSize];
        for (int i = 0; i < newSize; i++)
            for (int j = 0; j < newSize; j++) {
                if (i < newSize - 1 && j < newSize - 1)
                    newMatrice[i][j] = matrice[i][j];
                else
                    newMatrice[i][j] = IGrapheConst.NO_EDGE;
            }
        matrice = newMatrice;
    }

    @Override
    public void ajouterArc(String source, String destination, Integer valeur) {
        if (contientArc(source, destination))
            throw new ArcExistantException();
        if (source.isEmpty() || destination.isEmpty())
            throw new EmptySommetException();
        if (0 > valeur)
            throw new ArcValuationNegativeException();
        if (!contientSommet(source))
            ajouterSommet(source);
        if (!contientSommet(destination))
            ajouterSommet(destination);
        matrice[indices.get(source)][indices.get(destination)] = valeur;
    }

    @Override
    public void oterSommet(String noeud) {
        if (contientSommet(noeud)) {
            int index = indices.get(noeud);
            removeIndexFromMatrix(index);
            removeIndexFromMap(noeud, index);
        }
    }

    private void removeIndexFromMap(String noeud, int index) {
        indices.remove(noeud);
        for (Map.Entry<String, Integer> entry : indices.entrySet()) {
            if (entry.getValue() > index)
                indices.put(entry.getKey(), entry.getValue() - 1);
        }
    }

    private void removeIndexFromMatrix(int index) {
        int newSize = matrice.length - 1;
        int[][] newMatrice = new int[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                if (i < index && j < index)
                    newMatrice[i][j] = matrice[i][j];
                else if (i < index)
                    newMatrice[i][j] = matrice[i][j + 1];
                else if (j < index)
                    newMatrice[i][j] = matrice[i + 1][j];
                else
                    newMatrice[i][j] = matrice[i + 1][j + 1];
            }
        }
        matrice = newMatrice;
    }

    @Override
    public void oterArc(String source, String destination) {
        if (contientArc(source, destination))
            matrice[indices.get(source)][indices.get(destination)] = IGrapheConst.NO_EDGE;
        else
            throw new ArcInexistantException();
    }

    @Override
    public List<String> getSommets() {
        List<String> sommets = new ArrayList<>();
        if (null == this.indices)
            return sommets;
        sommets.addAll(indices.keySet());
        Collections.sort(sommets);
        return sommets;
    }

    @Override
    public List<String> getSucc(String sommet) {
        if (!contientSommet(sommet))
            throw new SommetInexistantException();
        List<String> succ = new ArrayList<>();
        Integer index = indices.get(sommet);
        int length = matrice.length;
        for (int i = 0; i < length; ++i)
            if (IGrapheConst.NO_EDGE != this.matrice[index][i])
                succ.add(getKeyFromValue(i));
        Collections.sort(succ);
        return succ;
    }

    @Override
    public int getValuation(String src, String dest) {
        if (!contientSommet(src) || !contientSommet(dest))
            throw new SommetInexistantException();
        if (!contientArc(src, dest))
            return IGrapheConst.NO_EDGE;
        return matrice[indices.get(src)][indices.get(dest)];
    }

    @Override
    public boolean contientSommet(String sommet) {
        return indices.containsKey(sommet);
    }

    @Override
    public boolean contientArc(String src, String dest) {
        if (!contientSommet(src) || !contientSommet(dest))
            return false;
        return IGrapheConst.NO_EDGE != this.matrice[this.indices.get(src)][this.indices.get(dest)];
    }

    /**
     * Utility method to get the key from a value in a map.
     *
     * @param value the value to search for
     * @return the key associated with the value, or null if the value is not found
     */
    private String getKeyFromValue(int value) {
        for (Map.Entry<String, Integer> entry : indices.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        throw new SommetInexistantException();
    }

    @Override
    public String toString() {
        return this.toAString();
    }
}
