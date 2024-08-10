package com.bongbong.lightning.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import com.bongbong.lightning.DataManager;
import com.bongbong.lightning.DataProfile;
import com.bongbong.lightning.Requests;
import com.bongbong.lightning.ThreadUtil;
import com.bongbong.lightning.responses.ReceiveResponse;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@CommandAlias("ln|lightning|lncli|btc")
public class ReceiveCommand extends BaseCommand {
    final ThreadUtil threadUtil;
    final DataManager dataManager;
    final Requests requests;


    @Subcommand("createinvoice|receive|request")
    @Description("Receive payment by generating an invoice")
    public void onRequest(Player player, int amount, @Optional String memo) {
        UUID uuid = player.getUniqueId();

        if (amount == 0) {
            player.sendMessage("Usage: /ln createinvoice <amount> [memo]");
            return;
        }

        threadUtil.runTask(true, () -> {
            DataProfile profile;
            profile = dataManager.getProfile(uuid);

            if (profile != null) {
                ReceiveResponse response = requests.createInvoice(amount, memo, profile.getAdminKey());

                player.sendMessage("Success! Hash: " + response.getPayment_hash());
                player.sendMessage("Request: " + response.getPayment_request());


                TextComponent message = new TextComponent("Click me");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + response.getPayment_request()));
                player.spigot().sendMessage(message);
            } else {
                player.sendMessage("Error: You do not have a wallet. Try again in 10 seconds or contact an admin.");
            }
        });
    }
}