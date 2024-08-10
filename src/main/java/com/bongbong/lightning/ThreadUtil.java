package com.bongbong.lightning;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class ThreadUtil {
    final Plugin plugin;

    public void runTask(boolean async, Runnable runnable) {
        if (async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
}
