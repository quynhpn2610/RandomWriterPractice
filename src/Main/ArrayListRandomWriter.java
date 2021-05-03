package Main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


public class ArrayListRandomWriter {

    public void writer() throws IOException {
        Scanner reader = new Scanner(System.in);
        //Get file name arguments from command line or interactively as entered by user

        System.out.println("Welcome to the RandomWriter – The ArrayList Version\nEnter your file name of choice: ");
        String sourceFileName = reader.next();

        System.out.println("\nNow enter what you would like the resultant file to be called: ");
        String resultFileName = reader.next();

        System.out.println("\nFinally, enter the amount of words to generate: ");
        int N = reader.nextInt();
        int nWords = 0;
        long startTime; //for emperical time measurement
        long stopTime;  //elapsed time is difference in millisec
        Random rand = new Random();

        java.net.URL inputUrl = ArrayListRandomWriter.class.getResource(sourceFileName);

        //Data structure declarations go here
        //...........

        //An ArrayList of unique words/Strings
        ArrayList<String> unique = new ArrayList<>();

        //An ArrayList of LLs of all the words that follow those unique words
        ArrayList<LinkedList<String>> follows = new ArrayList<>();

        //Prepare files
        assert inputUrl != null;
        Scanner dataFile = new Scanner(new FileReader(new File(inputUrl.getPath())));
        PrintWriter outFile = new PrintWriter(new FileWriter(resultFileName));
        //Read a line from the source file until end of file
        String firstWord = dataFile.next();
        String secondWord;
        startTime = System.currentTimeMillis();
        while (dataFile.hasNext()) {
            secondWord = dataFile.next();
            nWords++;

            //look for first word in the structure
            // and add the second word as the follow

            if (!(unique.contains(firstWord))) {
                unique.add(firstWord); //Adding unique words to unique LL
                follows.add(unique.indexOf(firstWord), new LinkedList<>()); //Making a LL for that word in follows, even if the next word is unique
            }
            follows.get(unique.indexOf(firstWord)).add(secondWord); //Add the following word to it's corresponding LL in follows

            firstWord = secondWord;
        }
        unique.add(firstWord);
        System.out.println("Last word in the file: " + firstWord); //Is our calculated last word the actual last word?
        follows.add(follows.get(0)); //Last word points to the follow LL of the first word


        //add the final word to the structure
        //it may be the only entry without a follow word
        //but it needs to be in the list of unique words
        //...........
        stopTime = System.currentTimeMillis();
        System.out.println("Elapsed input time = " + (stopTime - startTime) + " msecs.");

        startTime = stopTime;
        // Level 1: Randomly choose a word from the "follows" list as the next word in the output
        outFile.println("\n------------random follows list----------------");
        outFile.flush();

        int rn = rand.nextInt(unique.size());
        firstWord = unique.get(rn);
        outFile.print(firstWord + " ");

        for (int i = 0; i < N; i++) {
            LinkedList<String> LL = follows.get(unique.indexOf(firstWord));
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
        reader.close();
    }

}
