package Code.HelloListener;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class CurrentUser {

    public boolean isPlaying;
    public String word;
    public int numChances;
    public ArrayList<String> words;
    public ArrayList<String> guess;
    public int[] charVals;
    User user;

    public CurrentUser(User user){
        numChances = 5;
        this.user = user;
        word = "";
    }

}
