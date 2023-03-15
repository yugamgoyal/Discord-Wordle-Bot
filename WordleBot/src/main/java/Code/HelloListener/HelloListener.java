package Code.HelloListener;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HelloListener extends ListenerAdapter {


    boolean hasStarted;
    ArrayList<String> fullWordList;
    Map<User, CurrentUser> userMap;

    public HelloListener() throws FileNotFoundException {
        userMap = new HashMap<>();
        File f = new File("yugam.txt");
        Scanner file = new Scanner(f);
        importWordList(file);
    }

    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event){

        String messageSent = event.getMessage().getContentRaw();
        User user = event.getAuthor();
        CurrentUser tempUser;

       if(!event.getAuthor().isBot()) {
            if(userMap.get(user) == null){
             tempUser = new CurrentUser(user);
             userMap.put(user, tempUser);
            } else {
                tempUser = userMap.get(user);
            }

             TextChannel channel = event.getChannel();


            if (messageSent.equalsIgnoreCase("!wordle")) {
                // event.getMessage().delete().queue();
                event.getChannel().sendMessage("Here is your game").queue();
                event.getChannel().sendMessage("Welcome to Wordle: Java Edition\n Guess five letter words and try to figure out the correct word. \n * after a letter means it is correct and in the right spot \n ~ after a letter means it is in the word but not in the right spot \n letters that are not in the word don\'t have any character following them. \n  What\'s your first guess:").queue();

                tempUser.isPlaying = true;
                Collections.shuffle(fullWordList);
               // tempUser.word = fullWordList.get(0);
                tempUser.word = "vowel";
                // tempUser.word = fullWordList.get(0);
                tempUser.numChances = 5;
                tempUser.words = new ArrayList<>(fullWordList);
                tempUser.guess = new ArrayList<>();
                hasStarted = true;
                tempUser.charVals = new int[27];
                changeWord(tempUser.charVals, tempUser.word);

                Boolean gameOver = false;
                return;
            } else if (tempUser.isPlaying && tempUser.numChances > 0 && !(tempUser.word.equalsIgnoreCase(""))) {
                if(messageSent.length() != 5){
                    event.getChannel().sendMessage("please type a 5 letter word").queue();
                    return;
                }

                if(!fullWordList.contains(messageSent.toLowerCase())){
                    event.getChannel().sendMessage("this is not a valid word").queue();
                    return;
                }

                if(!tempUser.words.contains(messageSent.toLowerCase())){
                    event.getChannel().sendMessage("you have already guessed this word").queue();
                    return;
                }

                tempUser.guess.add(fixWord(messageSent));
                if((tempUser.word).equals(messageSent)){
                    event.getChannel().sendMessage(user.getAsMention()  + " You have Won").queue();
                    tempUser.numChances = 0;
                    String currentWord = tempUser.word;
                    tempUser.guess.add(gameRound(messageSent, currentWord, tempUser.charVals));
                    printEvent(event, tempUser.guess);
                    tempUser.word = "";
                    return;
                }

                tempUser.numChances--;
                if(tempUser.numChances == 0) {
                    tempUser.isPlaying = false;
                    event.getChannel().sendMessage(user.getAsMention() + " You have Lost word was: " + tempUser.word).queue();
                    tempUser.word = "";
                    tempUser.numChances = 0;
                    tempUser.isPlaying = false;
                    tempUser.guess.add(gameRound(messageSent, tempUser.word, tempUser.charVals));
                    printEvent(event, tempUser.guess);
                    return;
                }

                if(!(tempUser.word).equals(messageSent)){
                    String code = gameRound(messageSent, tempUser.word, tempUser.charVals);
                    tempUser.guess.add(code);
                    event.getChannel().sendMessage(user.getAsMention()).queue();
                    tempUser.words.remove(messageSent);
                    printEvent(event, tempUser.guess);
                    return;
                }
            }
           // event.getChannel().sendMessage("BOB").queue();
            return;
       }

    }

    private void changeWord(int[] charVals, String word) {
        for(int x = 0; x < word.length(); x++){
            charVals[word.charAt(x)-'a']++;
        }
    }

    private String fixWord(String messageSent) {
        String newWord = "";
        newWord += " " + messageSent.charAt(0);
        for(int x = 1; x <messageSent.length() - 3; x++ ){
            newWord += "   " + messageSent.charAt(x);
        }
        newWord += "  " + messageSent.charAt(messageSent.length() -3);
        newWord += "   " + messageSent.charAt(messageSent.length() -2);
        newWord += "   " + messageSent.charAt(messageSent.length() -1);
        return newWord;
    }

    private void printEvent(GuildMessageReceivedEvent event, ArrayList<String> guess) {
        int counter = 0;
        StringBuilder word = new StringBuilder();
        word.append("```");
        for(String w : guess){
                word.append( w );
                word.append("\n");
        }
        word.append("```");
        event.getChannel().sendMessage(word.toString()).queue();
    }

    private String gameRound(String word, String correctWord, int[] data) {
        int[] tempVals = new int[27];
        StringBuilder sb = new StringBuilder();
        String[] arr = new String[5];
        for(int i = 0; i < 5; i++) {
            if(word.charAt(i) == correctWord.charAt(i)) {
                tempVals[word.charAt(i) - 'a']++;
                arr[i] =  " \uD83D\uDFE9 ";
               sb.append(" \uD83D\uDFE9 ");
            } else if (correctWord.indexOf(word.charAt(i)) != -1 && tempVals[word.charAt(i) - 'a'] < data[word.charAt(i) - 'a']) {
                tempVals[word.charAt(i) - 'a']++;
                arr[i] = " \uD83D\uDFE8 ";
                sb.append(" \uD83D\uDFE8 ");
            } else {
               //  sb.append("i am here");
                tempVals[word.charAt(i) - 'a']++;
                arr[i] = " \uD83D\uDFE5 ";
                sb.append(" \uD83D\uDFE5 ");
            }
        }

        return sb.toString();
    }

    private ArrayList<String> getValidWords() throws FileNotFoundException{
        File f = new File("yugam.txt");
        Scanner sc = new Scanner(f);
        ArrayList<String> result = new ArrayList<>();
        while(sc.hasNext()){
            result.add(sc.next());
        }
        return result;
    }

    public void importWordList(Scanner sc) {
        fullWordList = new ArrayList<String>();
        while(sc.hasNext()) {
            fullWordList.add(sc.next());
        }
    }


}
