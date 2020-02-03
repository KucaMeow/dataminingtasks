package ru.kpfu.stepan.auth;

import com.vk.api.sdk.client.actors.UserActor;
import ru.kpfu.stepan.App;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Login {
    public static final String REDIRECT_URI = "https://oauth.vk.com/authorize?client_id=7306492&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=friends,groups&response_type=token&v=0.5.12";

    public static UserActor login () throws IOException, URISyntaxException {
        String temp = askToken(REDIRECT_URI);
        UserActor actor = new UserActor(Integer.parseInt(temp.substring(0, temp.indexOf(':')-1)), temp.substring(temp.indexOf(':')+1));
        return actor;
    }

    public static String askToken(String link) throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI(link));
        System.out.println("Enter url from browser after login");
        String url = App.SC.nextLine();
        String id = url.substring(url.indexOf("user_id=") + 8);
        String tok = url.substring(url.indexOf('=') + 1, url.indexOf('&'));
        return id+":"+tok;
    }
}
