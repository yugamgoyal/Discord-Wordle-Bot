package Code.HelloListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WordleGame {

    private int numChances;
    private boolean evil;

    private static ArrayList<String> fullWordList;

    String correctWord;

    public WordleGame(int numChances, boolean evil) {

        this.numChances = numChances;
        this.evil = evil;


    }

    public WordleGame() {

        this.numChances = 6;
        this.evil = false;

    }

    public void importWordList(Scanner sc) {

        fullWordList = new ArrayList<String>();

        while(sc.hasNext()) {

            fullWordList.add(sc.next());

        }

        Collections.shuffle(fullWordList);
        correctWord = fullWordList.get(0);

    }

    public void game() {

        boolean gameWon = false;
        int numRound = 0;

//		System.out.println("Welcome to Wordle: Java Edition");
//		System.out.println("Guess five letter words and try to figure out the correct word.");
//		System.out.println("* after a letter means it is correct and in the right spot");
//		System.out.println("~ after a letter means it is in the word but not in the right spot");
//		System.out.println("letters that are not in the word don\'t have any character following them.");

        Scanner sc = new Scanner(System.in);

        while(!gameWon && numRound < numChances) {

            System.out.print("\n Enter a five letter word: ");

            String word = sc.nextLine();
            gameWon = gameRound(word.toLowerCase());
            numRound++;

        }

        sc.close();

        if(gameWon == true) {

            System.out.println("\nYou won the game!");


        } else {

            System.out.println("\nToo bad!");

        }

    }

    private boolean gameRound(String word) {

        if(word.equals(correctWord)) {

            return true;

        }

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 5; i++) {

            if(word.charAt(i) == correctWord.charAt(i)) {

                sb.append("*");

            } else if(correctWord.indexOf(word.charAt(i)) != -1){

                sb.append("~");

            } else {

                sb.append("_");

            }



        }
        System.out.println(word);
        System.out.println(sb.toString());

        return false;

    }




}
