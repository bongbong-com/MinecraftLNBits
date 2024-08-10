package com.bongbong.lightning;

import com.bongbong.lightning.responses.UserManagerCreateResponse;
import com.google.gson.internal.LinkedTreeMap;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class EventListeners implements Listener {
    final Requests requests;
    final ThreadUtil threadUtil;
    final DataManager dataManager;

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        threadUtil.runTask(true, () -> {
            DataProfile profile;
            profile = dataManager.getProfile(uuid);

            if (profile == null) {
                UserManagerCreateResponse response = requests.createWallet(uuid, player.getName());

                profile = new DataProfile();
                profile.setUuid(uuid);
                profile.setUserId(response.getId());

                profile.setInvoiceKey((String) ((LinkedTreeMap) response.getWallets().get(0)).get("inkey"));
                profile.setAdminKey((String) ((LinkedTreeMap) response.getWallets().get(0)).get("adminkey"));
                profile.setWalletId((String) ((LinkedTreeMap) response.getWallets().get(0)).get("id"));

                dataManager.pushProfile(profile);
            }
        });
    }

    private void onDisconnect(Player player) {

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onDisconnect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        onDisconnect(event.getPlayer());
    }

}
