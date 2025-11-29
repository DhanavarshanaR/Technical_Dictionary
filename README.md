Technical Dictionary (Java Swing)

A simple and fast Java Swing–based Technical Dictionary that allows users to search, view, and add technical terms. Includes autocomplete suggestions, persistent storage, and a clean UI.

Project Structure

TechnicalDictionary/data/dictionary.txt

TechnicalDictionary/src/app/DictionaryApp.java

TechnicalDictionary/src/manager/DictionaryManager.java

TechnicalDictionary/src/util/FileUtil.java


Features

1)Search technical words (case-insensitive)

2)Autocomplete suggestions while typing

3)Add new words with definitions

4)Automatic saving to dictionary.txt

5)Simple and clean Swing UI

Technologies Used

1)Java 8+

2)Swing (JFrame, JTextField, JTextArea, JList, JPopupMenu)

3)File I/O (BufferedReader, BufferedWriter)

4)TreeMap for sorted dictionary storage

How to Run

Compile:

javac src/app/DictionaryApp.java src/manager/DictionaryManager.java src/util/FileUtil.java -d out

Run:

java -cp out app.DictionaryApp

Dictionary Format

Each line must follow->

Word: Definition


Example:

Algorithm: A step-by-step method to solve a problem.

Array: A collection of items stored in contiguous memory locations.
