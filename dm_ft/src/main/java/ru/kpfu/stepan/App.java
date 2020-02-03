package ru.kpfu.stepan;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.kpfu.stepan.logic.Friends;
import ru.kpfu.stepan.auth.Login;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;


public class App {

    public final static Scanner SC = new Scanner(System.in);
    public static VkApiClient vk;

    public static void main(String[] args) throws IOException, URISyntaxException, ClientException, ApiException, InterruptedException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        UserActor user = Login.login();
        System.out.println(Friends.getCloseIDs(user));
        System.out.println(Friends.getSameIDs(user));
        System.out.println(Friends.getFarIDs(user));
    }
}
