package app;

import manager.DictionaryManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class DictionaryApp extends JFrame {

    private JTextField searchField;
    private JEditorPane resultArea;
    private JButton searchButton;
    private JButton addButton;
    private DictionaryManager manager;

    private JPopupMenu suggestionPopup;
    private int selectedIndex = -1;

    public DictionaryApp() {
        super("Technical Dictionary");
        manager = new DictionaryManager();

        // --- GUI Components ---
        searchField = new JTextField(20);
        resultArea = new JEditorPane();
        resultArea.setContentType("text/html");
        resultArea.setEditable(false);

        searchButton = new JButton("Search");
        addButton = new JButton("Add Word");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter Word:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);

        JScrollPane resultScroll = new JScrollPane(resultArea);

        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(resultScroll, BorderLayout.CENTER);

        // --- Autocomplete popup ---
        suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        // Document listener for suggestions
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }
        });

        // Keyboard navigation
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int size = suggestionPopup.getComponentCount();
                if (size == 0)
                    return;

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedIndex = (selectedIndex + 1) % size;
                    highlightSuggestion();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedIndex = (selectedIndex - 1 + size) % size;
                    highlightSuggestion();
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selectedIndex >= 0 && selectedIndex < size) {
                        JMenuItem item = (JMenuItem) suggestionPopup.getComponent(selectedIndex);
                        selectSuggestion(item.getText());
                    } else {
                        searchWord();
                    }
                    suggestionPopup.setVisible(false);
                    e.consume();
                } else {
                    selectedIndex = -1;
                }
            }
        });

        // --- Button Actions ---
        searchButton.addActionListener(e -> searchWord());
        addButton.addActionListener(e -> addNewWordManually());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // --- Update suggestions ---
    private void updateSuggestions() {
        String text = searchField.getText().trim().toLowerCase();
        suggestionPopup.setVisible(false);

        if (text.isEmpty())
            return;

        suggestionPopup.removeAll();
        selectedIndex = -1;

        int count = 0;
        for (String key : manager.getDictionary().keySet()) {
            if (key.toLowerCase().startsWith(text)) {
                JMenuItem item = new JMenuItem(key);
                item.setFocusable(false);
                item.addActionListener(ae -> selectSuggestion(key));
                suggestionPopup.add(item);
                count++;
                if (count >= 5)
                    break;
            }
        }

        if (suggestionPopup.getComponentCount() > 0) {
            SwingUtilities.invokeLater(() -> {
                suggestionPopup.show(searchField, 0, searchField.getHeight());
            });
        }
    }

    // --- Highlight suggestion ---
    private void highlightSuggestion() {
        for (int i = 0; i < suggestionPopup.getComponentCount(); i++) {
            JMenuItem item = (JMenuItem) suggestionPopup.getComponent(i);
            item.setBackground(i == selectedIndex ? Color.LIGHT_GRAY : Color.WHITE);
        }
    }

    // --- Select suggestion ---
    private void selectSuggestion(String word) {
        searchField.setText(word);
        displayDefinition(word);
        suggestionPopup.setVisible(false);
    }

    // --- Search word manually ---
    private void searchWord() {
        String word = searchField.getText().trim();
        if (word.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a word to search.");
            return;
        }
        displayDefinition(word);
    }

    // --- Add new word manually ---
    private void addNewWordManually() {
        String word = JOptionPane.showInputDialog("Enter new word:");
        if (word == null || word.trim().isEmpty())
            return;

        String definition = JOptionPane.showInputDialog("Enter definition for " + word + ":");
        if (definition == null || definition.trim().isEmpty())
            return;

        manager.addWord(word.trim(), definition.trim());
        JOptionPane.showMessageDialog(this, "Word added successfully!");
        displayDefinition(word.trim());
    }

    // --- Display definition with all letters bold ---
    private void displayDefinition(String word) {
        String definition = manager.searchWord(word);
        if (definition != null) {
            StringBuilder boldWord = new StringBuilder();
            for (char c : word.toCharArray()) {
                boldWord.append("<b>").append(c).append("</b>");
            }
            resultArea.setText("<html>" + boldWord + "<br>" + definition + "</html>");
        } else {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Word not found! Would you like to add \"" + word + "\"?",
                    "Word Not Found",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String newDefinition = JOptionPane.showInputDialog("Enter definition for " + word + ":");
                if (newDefinition != null && !newDefinition.trim().isEmpty()) {
                    manager.addWord(word.trim(), newDefinition.trim());
                    JOptionPane.showMessageDialog(this, "\"" + word + "\" added successfully!");
                    displayDefinition(word.trim());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DictionaryApp::new);
    }
}
