// randomwriter.java
// ******************************************************************
// This program under development *will* [eventually] generate random
//    passages based on an input source file (likely a classic).  It
//    will choose a random word from the source, and based on the
//    probability of a word following the previously chosen word,
//    choose the next word.
//
//    Command line inputs: sourcefile resultfile NWordsToGenerate
//
// Author: L. Rhodes  start 3/12/04
//                    revision 3/2015
//                    revision 3/2016
//
//
// -----------------------------------------------------------------
package Main;

import java.io.*;
import java.util.*;

public class RandomWriterLL
{
    public static void main(String[] args) throws IOException
    {
        Scanner reader = new Scanner(System.in);
        //Get file name arguments from command line or interactively as entered by user

        System.out.println("Welcome to the RandomWriter – Iteration 1\nEnter your file name of choice: ");
        String sourceFileName = reader.next();

        System.out.println("\nNow enter what you would like the resultant file to be called: ");
        String resultFileName  = reader.next();

        System.out.println("\nFinally, enter the amount of words to generate: ");
        int N = reader.nextInt();
        int nWords = 0;
        int nFirst = 100;
        long startTime; //for emperical time measurement
        long stopTime;  //elapsed time is difference in millisec
        Random rand = new Random();

        java.net.URL inputUrl = RandomWriterLL.class.getResource(sourceFileName);

        //Data structure declarations go here
        //...........
        //this is a simple linked list of all words (strings)
        LinkedList <String> words= new LinkedList <String>();

        //An ArrayList of unique words/Strings
        ArrayList <String> unique = new ArrayList <String>();

        //A LL of all the words that follow those unique words
        ArrayList <LinkedList<String>> follows = new ArrayList <LinkedList<String>>();

        //this is an arraylist of linked lists of strings
        ArrayList <LinkedList<String>> byFirstLetter = new ArrayList <LinkedList<String>>(27);
        //initialize this array of linkedlists
        for(int i=0; i<27; i++){
            byFirstLetter.add(i,new LinkedList<String>());
        }

        //Prepare files
        Scanner dataFile = new Scanner(new FileReader(new File(inputUrl.getPath())));
        PrintWriter outFile     = new PrintWriter(new FileWriter(resultFileName));
        //Read a line from the source file until end of file
        String firstWord = dataFile.next();
        String secondWord;
        startTime = System.currentTimeMillis();
        while(dataFile.hasNext()){
            secondWord = dataFile.next();
            nWords++;
            if(nWords % 1000 ==0) System.out.println(nWords+" words");
            //only print the first N words for debugging
            //remove or comment out when you have built in a
            //a storage structure
            //..............
            if(nWords<= nFirst){
                outFile.print(firstWord+" ");
                outFile.flush();
            }

            words.add(firstWord);//adding word to lone linked list for demo only


            //and put words into lists by their first letter
            char c = Character.toLowerCase(firstWord.charAt(0));
            int ci = (c>='a' && c<='z')? c-'a' : 26;
            ((LinkedList)(byFirstLetter.get(ci))).add(firstWord);

            //look for first word in the structure
            // and add the second word as the follow
            //...........

            if(!(unique.contains(firstWord))) {
                unique.add(firstWord); //Adding unique words to unique LL
                follows.add(unique.indexOf(firstWord),new LinkedList<String>()); //Making an LL for that word in follows, even if the next word is unique
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
        System.out.println("Elapsed input time = "+(stopTime-startTime)+" msecs.");

        startTime = stopTime;
        //  Let's dump the front of the list to verify same sequence
        outFile.println(N+"\n------------from list----------------");
        outFile.flush();
        System.gc();
        for(int i=0; i<N; i++){
            outFile.print(words.get(i)+" ");
        }
        //   Level 0: random selection of words from lone list
        outFile.println("\n------------random list----------------");
        outFile.flush();
        for (int i=0; i<N; i++){
            outFile.print(words.get(rand.nextInt(nWords))+" ");
        }

        //  Random words from each letter category
        outFile.println("\n------------random alpha list----------------");
        outFile.flush();
        for (int i=0; i<27; i++){
            LinkedList ll = (LinkedList)(byFirstLetter.get(i));
            int lllen = ll.size();
            //     outFile.print(ll);
            if(lllen>1)outFile.print(ll.get(rand.nextInt(lllen))+" ");
        }


        // Level 1: Randomly choose a word from the "follows" list as the next word in the output
        outFile.println("\n------------random follows list----------------");
        outFile.flush();

        for(int i=0;i<N;i++) {
            int rn = rand.nextInt(unique.size());
            firstWord = unique.get(rn);
            LinkedList<String> LL = follows.get(unique.indexOf(firstWord));
            int nrn = 0;
            if(!(LL.size()<=1)) nrn = rand.nextInt(LL.size() - 1);
            secondWord = LL.get(nrn);
            firstWord = unique.get(unique.indexOf(secondWord));
            outFile.print(firstWord + " ");
        }

        outFile.flush();
        outFile.close();
        stopTime = System.currentTimeMillis();
        System.out.println("Elapsed output time = "+(stopTime-startTime)+" msecs.");
    }



}


