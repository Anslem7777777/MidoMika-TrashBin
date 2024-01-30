package me.mika.midomikatrashbin;

import me.mika.midomikatrashbin.InventoryUntils.TrashBinListeners;
import me.mika.midomikatrashbin.Utils.TrashBinUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MidoMika_TrashBin extends JavaPlugin {

    private static MidoMika_TrashBin instance;
    private TrashBinUtil trashBinUtils = new TrashBinUtil();

    @Override
    public void onEnable() {
        instance = this;

        trashBinUtils.registerTrashBin();
        getServer().getPluginManager().registerEvents(new TrashBinListeners(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MidoMika_TrashBin getInstance() {
        return instance;

    }
}
