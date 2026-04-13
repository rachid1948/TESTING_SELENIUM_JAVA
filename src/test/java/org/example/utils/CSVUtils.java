package org.example.utils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class to read CSV files and return data
 * compatible with TestNG DataProvider
 */
public class CSVUtils {
    private CSVUtils() {
    }
    /**
     * Reads a CSV file from src/test/resources and returns Object[][]
     *
     * @param filePath ex: "testdata/loginData.csv"
     * @return Object[][]
     */
    public static Object[][] readCSV(String filePath) {
        List<Object[]> data = new ArrayList<>();
        try {
            InputStream inputStream = CSVUtils.class
                    .getClassLoader()
                    .getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new RuntimeException("Fichier CSV introuvable : " + filePath);
            }
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            String[] line;
            boolean skipHeader = true;
            while ((line = reader.readNext()) != null) {
                // Ignorer l'entête
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                data.add(line);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du CSV", e);
        }
        return data.toArray(new Object[0][]);
    }

    /**
     * Met à jour la valeur d'une cellule dans un fichier CSV (src/test/resources).
     * Cherche la ligne contenant oldValue dans la colonne columnIndex et la remplace par newValue.
     *
     * @param filePath    ex: "testdata/signupData.csv"
     * @param columnIndex index de la colonne à mettre à jour (0 = première colonne)
     * @param oldValue    valeur actuelle à rechercher
     * @param newValue    nouvelle valeur à écrire
     */
    public static void updateCSVValue(String filePath, int columnIndex, String oldValue, String newValue) {
        try {
            // 1) Chemin dans target/test-classes (classpath courant)
            URL resourceUrl = CSVUtils.class.getClassLoader().getResource(filePath);
            if (resourceUrl == null) {
                throw new RuntimeException("Fichier CSV introuvable : " + filePath);
            }
            Path targetCsvPath = Paths.get(resourceUrl.toURI());

            // 2) Chemin dans src/test/resources (source, pour persister entre les builds Maven)
            //    On remonte depuis target/test-classes jusqu'à la racine du projet,
            //    puis on descend dans src/test/resources.
            Path projectRoot = targetCsvPath;
            while (projectRoot != null && !Files.exists(projectRoot.resolve("pom.xml"))) {
                projectRoot = projectRoot.getParent();
            }
            if (projectRoot == null) {
                throw new RuntimeException("Impossible de trouver la racine du projet (pom.xml introuvable).");
            }
            Path sourceCsvPath = projectRoot.resolve("src").resolve("test").resolve("resources").resolve(filePath);

            // 3) Lecture des lignes depuis target (déjà copié par Maven)
            List<String[]> allLines = new ArrayList<>();
            try (CSVReader reader = new CSVReader(Files.newBufferedReader(targetCsvPath))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    allLines.add(line);
                }
            }

            // 4) Mise à jour de la cellule ciblée
            boolean updated = false;
            for (String[] row : allLines) {
                if (row.length > columnIndex && row[columnIndex].trim().equals(oldValue.trim())) {
                    row[columnIndex] = newValue;
                    updated = true;
                    break;
                }
            }

            if (!updated) {
                throw new RuntimeException(
                    "Valeur '" + oldValue + "' introuvable dans la colonne " + columnIndex + " du fichier " + filePath
                );
            }

            // 5) Réécriture dans target/test-classes (pour le test courant)
            try (CSVWriter writer = new CSVWriter(new FileWriter(targetCsvPath.toFile()))) {
                writer.writeAll(allLines);
            }

            // 6) Réécriture dans src/test/resources (pour persister aux prochains builds)
            if (Files.exists(sourceCsvPath)) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(sourceCsvPath.toFile()))) {
                    writer.writeAll(allLines);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise a jour du CSV : " + filePath, e);
        }
    }
}