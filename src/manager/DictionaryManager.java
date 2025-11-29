package manager;

import util.FileUtil;
import java.util.TreeMap;

public class DictionaryManager {

    private TreeMap<String, String> dictionary;

    public DictionaryManager() {
        dictionary = new TreeMap<>();
        loadDictionary();
    }

    private void loadDictionary() {
        dictionary = FileUtil.readDictionaryFromFile();
    }

    public String searchWord(String word) {
        if (word == null)
            return null;
        for (String key : dictionary.keySet()) {
            if (key.equalsIgnoreCase(word.trim())) {
                return dictionary.get(key);
            }
        }
        return null;
    }

    public void addWord(String word, String definition) {
        if (word == null || definition == null)
            return;
        word = word.trim();
        definition = definition.trim();

        String existingKey = null;
        for (String key : dictionary.keySet()) {
            if (key.equalsIgnoreCase(word)) {
                existingKey = key;
                break;
            }
        }

        if (existingKey != null) {
            dictionary.put(existingKey, definition);
        } else {
            dictionary.put(word, definition);
        }

        FileUtil.writeWordToFile(word, definition);
    }

    public TreeMap<String, String> getDictionary() {
        return dictionary;
    }
}
