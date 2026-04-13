package org.example.utils;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
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
}