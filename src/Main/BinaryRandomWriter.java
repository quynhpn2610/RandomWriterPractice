package Main;

// BinaryRandomWriter.java
// ******************************************************************
// This program under development *will* [eventually] generate random
//    passages based on an input source file (likely a classic).  It
//    will choose a random word from the source, and based on the
//    probability of a word following the previously chosen word,
//    choose the next word.
//
//    Command line inputs: sourcefile resultfile NWordsToGenerate
//
//         Arman Barraghi  04/29/2021
//		  Quynh Nguyen  04/29/2021
//
// -----------------------------------------------------------------


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BinaryRandomWriter {

    public static void main(String[] args) throws IOException {

        Scanner reader = new Scanner(System.in);
        //Get file name arguments from command line or interactively as entered by user

        System.out.println("Welcome to the RandomWriter – Iteration 1\nEnter your file name of choice: ");
        String sourceFileName = reader.next();

        System.out.println("\nNow enter what you would like the resultant file to be called: ");
        String resultFileName = reader.next();

        System.out.println("\nFinally, enter the amount of words to generate: ");
        int N = reader.nextInt();
        int nWords = 0;
        long startTime; //for emperical time measurement
        long stopTime;  //elapsed time is difference in millisec
        Random rand = new Random();

        java.net.URL inputUrl = BinaryRandomWriter.class.getResource(sourceFileName);

        //Data structure declarations go here
        //...........

        //An ArrayList of unique words/Strings
        ArrayList<String> unique = new ArrayList<String>();

        LinkedList<String> follows = new LinkedList<String>();

        //BST of Unique as the Key, and Follows LL as the Value
        TreeMap<String, LinkedList<String>> bstRand = new TreeMap<String, LinkedList<String>>();

        //Prepare files
        Scanner dataFile = new Scanner(new FileReader(new File(inputUrl.getPath())));
        PrintWriter outFile = new PrintWriter(new FileWriter(resultFileName));
        //Read a line from the source file until end of file
        String firstWord = dataFile.next();
        String secondWord;
        String firstRandomWord = "";
        startTime = System.currentTimeMillis();
        while (dataFile.hasNext()) {
            secondWord = dataFile.next();
            if (!(bstRand.containsKey(firstWord))) { //new unique word
                nWords++;
                if (rand.nextInt(nWords) == 0) firstRandomWord = firstWord;
                //create new linked followlist
                unique.add(firstWord); // assume that unique and follows have the same length
                follows.add(unique.indexOf(firstWord), secondWord);
                bstRand.put(firstWord, new LinkedList<>());
            }

            bstRand.put(firstWord, bstRand.get(firstWord));
            firstWord = secondWord;
        }
//        bstRand.put(firstWord, follows);  //Last word points to the follow LL of the first wordunique.add(firstWord);
        System.out.println("Last word in the file: " + firstWord); //Is our calculated last word the actual last word?

        stopTime = System.currentTimeMillis();
        System.out.println("Elapsed input time = " + (stopTime - startTime) + " msecs.");

        startTime = stopTime;
        // Level 1: Randomly choose a word from the "follows" list as the next word in the output
        outFile.println("\n------------random follows list----------------");
        outFile.flush();

        outFile.print(firstRandomWord + " ");
        firstWord = firstRandomWord;

        for (int i = 0; i < N; i++) {
            LinkedList<String> LL = bstRand.get(firstWord);
            int nrn = 0;
            if (LL.size() > 1)
                nrn = rand.nextInt(LL.size() - 1); //Only try to compute a random number if LL has more than 1 element
            secondWord = LL.get(nrn);
            firstWord = unique.get(unique.indexOf(secondWord));
            outFile.print(secondWord + " ");
//            System.out.println("Next random number: " + nrn);
        }

        outFile.flush();
        outFile.close();
        stopTime = System.currentTimeMillis();
        System.out.println("Elapsed output time = " + (stopTime - startTime) + " msecs.");
    }


}



