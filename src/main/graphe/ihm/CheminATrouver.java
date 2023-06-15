package src.main.graphe.ihm;

import src.main.graphe.core.Arc;
import src.main.graphe.core.IGraphe;

import java.util.List;

public class CheminATrouver {
    private final String fileName;
    private final String repFileName;
    private final IGraphe g;
    private final Arc sd_arc;
    private final int distance_attendue;
    private final List<Integer> chemin_possible;

    public CheminATrouver(String fileName, String repFileName, IGraphe g,
                          Arc sd_arc, int distance_attendue,
                          List<Integer> chemin_possible) {
        this.fileName = fileName;
        this.repFileName = repFileName;
        this.g = g;
        this.sd_arc = sd_arc;
        this.distance_attendue = distance_attendue;
        this.chemin_possible = chemin_possible;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRepFileName() {
        return repFileName;
    }

    public IGraphe getGraph() {
        return g;
    }

    public Arc getSD_arc() {
        return sd_arc;
    }

    public int getDistance_attendue() {
        return distance_attendue;
    }

    public List<Integer> getChemin_possible() {
        return chemin_possible;
    }
}
