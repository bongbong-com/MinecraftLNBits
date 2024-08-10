package com.bongbong.lightning;

import co.aikar.commands.PaperCommandManager;
import com.bongbong.lightning.commands.BalanceCommand;
import com.bongbong.lightning.commands.LightningCommand;
import com.bongbong.lightning.commands.PayCommand;
import com.bongbong.lightning.commands.ReceiveCommand;
import com.bongbong.lightning.database.Mongo;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import okhttp3.OkHttpClient;
import org.bson.UuidRepresentation;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningPlugin extends JavaPlugin {

    // set environment variables with
    // export KEY="value"
    // in ~/.profile &or ~/.bashrc
    // do this for all the environment variables
    //
    // example:
    // export HOST_BASE_URL_LNBIT="https://wallet.example.com"
    // export HOST_ADMIN_KEY_LNBIT="adminaccountlnbitadminkey"
    // export HOST_INVOICE_KEY_LNBI="adminaccountlnbitinvoicekey"
    //
    // you must have UserManager installed on your LNBits node.
    // mongodb installed is also a requirement.

    public static String HOST_BASE_URL_LNBIT = System.getenv("HOST_BASE_URL_LNBIT");
    protected static String HOST_ADMIN_KEY_LNBIT = System.getenv("HOST_ADMIN_KEY_LNBIT");
    protected static String HOST_INVOICE_KEY_LNBIT = System.getenv("HOST_INVOICE_KEY_LNBIT");

    @Override
    public void onEnable() {
        MongoClientSettings mongoSettings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> builder
                        .minSize(5)
                        .maxSize(10))
                .uuidRepresentation(UuidRepresentation.STANDARD).build();

        Mongo mongo = new Mongo(MongoClients.create(mongoSettings).getDatabase("lightning-minecraft"));

        OkHttpClient client = new OkHttpClient().newBuilder().followSslRedirects(true).build();
        Requests requests = new Requests(client);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        ThreadUtil threadUtil = new ThreadUtil(this);

        DataManager dataManager = new DataManager(mongo);

        registerListener(new EventListeners(requests, threadUtil, dataManager));

        commandManager.registerCommand(new LightningCommand());
        commandManager.registerCommand(new PayCommand(threadUtil, dataManager, requests));
        commandManager.registerCommand(new ReceiveCommand(threadUtil, dataManager, requests));
        commandManager.registerCommand(new BalanceCommand(threadUtil, dataManager, requests));
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}