package com.bongbong.lightning.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;

import static co.aikar.commands.ACFBukkitUtil.sendMsg;

@CommandAlias("ln|btc|lncli|lightning")
public class LightningCommand extends BaseCommand {

    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        sendMsg(sender, ("Help"));
        help.showHelp();
    }
//
//    @Subcommand("test")
//    public void onTest(CommandSender sender) {
//
//        HttpUrl.Builder urlBuilder
//                = HttpUrl.parse(LightningPlugin.BASE_URL + "/usermanager/api/v1/users").newBuilder();
//
//        Request request = new Request.Builder()
//                .url(urlBuilder.build().toString())
//                .addHeader("X-Api-Key", "21f492f090cc4c97b5ff8e9cbb81fdd6")
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            public void onResponse(Call call, Response response)
//                    throws IOException {
//                sender.sendMessage(response.toString());
//            }
//
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//    }


}
