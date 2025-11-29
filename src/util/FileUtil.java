package util;

import java.io.*;
import java.util.TreeMap;

public class FileUtil {

    private static final String DICTIONARY_FILE = "data/dictionary.txt";

    public static TreeMap<String, String> readDictionaryFromFile() {
        TreeMap<String, String> map = new TreeMap<>();
        File file = new File(DICTIONARY_FILE);
        try {
            if (!file.exists())
                file.getParentFile().mkdirs();
            if (!file.exists())
                file.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(":"))
                    continue;
                String[] parts = line.split(":", 2);
                map.put(parts[0].trim(), parts[1].trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void writeWordToFile(String word, String definition) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DICTIONARY_FILE, true))) {
            bw.write(word + ": " + definition);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
