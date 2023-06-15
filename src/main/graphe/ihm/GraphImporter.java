package src.main.graphe.ihm;


import src.main.graphe.core.Arc;
import src.main.graphe.core.IGraphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public enum GraphImporter {
    ;

    public static int importerReponse(final String nomFichier, final List<Integer> listeEntiers) throws IOException {
        int distance = -1;

        try (final Scanner scanner = new Scanner(new File(nomFichier), StandardCharsets.UTF_8)) {
            if (!scanner.hasNextLine()) {
                throw new IOException("Le fichier est vide.");
            }
            final String premiereLigne = scanner.nextLine();

            if (premiereLigne.startsWith("pas de chemin entre")) {
                final String[] ligneDecoupee = premiereLigne.split(" ");
                if (7 != ligneDecoupee.length) {
                    throw new IOException("Format incorrect pour la premiere ligne.");
                }
                listeEntiers.add(Integer.parseInt(ligneDecoupee[4]));
                listeEntiers.add(Integer.parseInt(ligneDecoupee[6]));
            } else {
                if (!scanner.hasNextLine()) {
                    throw new IOException("Deuxieme ligne non trouvee.");
                }
                distance = scanner.nextInt();
                scanner.nextLine();
                if (!scanner.hasNextLine()) {
                    throw new IOException("Troisieme ligne non trouvee.");
                }
                final String troisiemeLigne = scanner.nextLine();
                final List<Integer> tempList = Arrays.stream(troisiemeLigne.split(" "))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                listeEntiers.addAll(tempList);
            }
        } catch (final NumberFormatException e) {
            throw new IOException("Le fichier contient des données mal formatees. " + e, e);
        }

        return distance;
    }

    public static Arc importer(final String filePath, final IGraphe g) {
        final File file = new File(filePath);
        return GraphImporter.importer(file, g);
    }

    // retourne une instance vide de la classe de g
    // utile quand on importe plusieurs fichiers
    // pour repartir d'un graphe vide pour chaque fichier
    public static IGraphe spawn(final IGraphe g) {
        try {
            return g.getClass().getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new RuntimeException("Impossible de spawn un graphe de type " + g.getClass().getSimpleName());
        }
    }

    public static Arc importer(final File file, final IGraphe g) {
        try (final Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
            String line;
            if (!sc.hasNextLine()) {
                throw new IllegalArgumentException("Pas de graphe dans " + file);
            }
            line = sc.nextLine(); // nom d'algorithme non utilise
            Arc a = null;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                a = GraphImporter.parse(line);
                if (sc.hasNextLine())
                    g.ajouterArc(a.getSource(), a.getDestination(), a.getValuation());
            }
            return a;
        } catch (final IOException e) {
            throw new IllegalArgumentException("Pas de graphe dans " + file);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("valuation incorrecte dans " + file);
        }
    }

    public static Arc parse(final String string) {
        String[] parts = string.split(" ");
        final String source;
        final int valuation;
        final String destination;
        if (2 == parts.length) { // derniere ligne
            final String[] newParts = new String[3];
            newParts[0] = parts[0];
            newParts[1] = "00";
            newParts[2] = parts[1];
            parts = newParts;
        }

        try {
            source = parts[0];
            valuation = Integer.valueOf(parts[1]);
            destination = parts[2];
        } catch (final Exception e) {
            throw new IllegalArgumentException(string + " n'est pas un arc");
        }
        return new Arc(source, destination, valuation);
    }
}
