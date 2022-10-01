package Code;

import Code.HelloListener.HelloListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;

public class WordleBot {

    public static JDABuilder builder;

    public static void main(String[] args) throws LoginException, FileNotFoundException {
        String token = "TOKEN";
        builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);

        builder.setBulkDeleteSplittingEnabled(false);

        builder.setCompression(Compression.NONE);

        builder.setActivity(Activity.playing("Wordle"));

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        resgisterListeners();
        builder.build();

    }

    public static void resgisterListeners() throws FileNotFoundException {
        builder.addEventListeners(new HelloListener());
    }

}
