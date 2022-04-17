package Code.HelloListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainGame {

    public static void playGame() throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);
        int numChances = Integer.parseInt(sc.nextLine());

        WordleGame n = new WordleGame(numChances, false);

        File f = new File("yugam.txt");

        Scanner file = new Scanner(f);

        n.importWordList(file);

        n.game();


    }
}
