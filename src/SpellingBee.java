import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
// Sam Reznik
// 03/15/2024
// Cs2
/**
 * Spelling Bee
 * <p>
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 * <p>
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 * <p>
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 * <p>
 * Written on March 5, 2023 for CS2 @ Menlo School
 * <p>
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateWord(letters);
    }
    // Makes it easier for me to just pass in a set of letters instead of all the parameters
    public void generateWord(String letters) {
        generateWord("", letters);
    }
    // Generates all the combinations and permutations of the letters
    public void generateWord(String currentWord, String letters) {
        // Adds the word to the list
        words.add(currentWord);

        // Goes through the String of letters until there are no letters left
        for (int i = 0; i < letters.length(); i++) {
            // Recursively adds the leftover letters to the word then makes a new string of the letters not used
            generateWord(currentWord + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergeSort(words);
    }

    // Returns the new Arraylist of sorted words
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        // Creates new arraylist of words
        ArrayList<String> merged = new ArrayList<>();
        int a = 0;
        int b = 0;
        // Goes through the arraylists until one of them is fully used
        while (a < arr1.size() && b < arr2.size()) {
            // Checks which arraylist to add from
            if (arr1.get(a).compareTo(arr2.get(b)) <= 0) {
                merged.add(arr1.get(a++));
            }
            else {
                merged.add(arr2.get(b++));
            }
        }
        // Adds the rest of the arraylist
        while (a < arr1.size()) {
            merged.add(arr1.get(a++));
        }
        while (b < arr2.size()) {
            merged.add(arr2.get(b++));
        }
        return merged;
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr) {
        return mergeSort(arr, 0, arr.size() - 1);
    }
    // Returns the Arraylist of new words
    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high) {
        // Creates arraylist of size 1
        if (high ==low){
            ArrayList<String> newArr = new ArrayList<String>();
            newArr.add(arr.get(low));
            return newArr;
        }
            // Finds the middle point
            int mid = (high + low) / 2;
            // Splits the Arraylist into 2 and runs the algorithm on those arraylists
            ArrayList<String> leftSublist = mergeSort(arr, low, mid);
            ArrayList<String> rightSublist = mergeSort(arr, mid +1 , high);

            // Merges the sorted arraylists
            return merge(leftSublist, rightSublist);
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int i = 0;
        // Goes through the arraylist
        while (i < words.size()) {
            // Checks if it is a word
            if (!isWord(words.get(i))) {
                // Removes the word if not a word
                words.remove(i);
            } else {
                i++;
            }
        }
    }

    public boolean isWord(String word) {
        return isWord(word, 0, DICTIONARY_SIZE - 1);
    }
    // Returns true if it is a word
    public boolean isWord(String word, int low, int high) {
        // Goes through the dictionary until it pin points a word
        while (low <= high) {
            // Finds the mid point
            int mid = (high + low) / 2;
            // Compares that point to the word
            int compare = DICTIONARY[mid].compareTo(word);
            // Checks if it above or below the mid point
            if (compare < 0) {
                low = mid + 1;
            } else if (compare > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while (s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
