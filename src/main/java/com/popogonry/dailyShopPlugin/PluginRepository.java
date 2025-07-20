
package com.popogonry.dailyShopPlugin;


import com.popogonry.dailyShopPlugin.shop.ShopRepository;
import com.popogonry.dailyShopPlugin.shopItem.ShopItemRepository;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PluginRepository {
    public static HashMap<UUID, Object> playerInputModeHashMap = new HashMap<>();
    public static HashMap<UUID, ItemStack> playerCurrentItemHashMap = new HashMap<>();

    private static final String CONFIG_FILE_NAME = "config.yml";
    private final String configBasePath = DailyShopPlugin.getServerInstance().getDataFolder().getAbsolutePath();
    private final PluginDataConfig pluginDataConfig;
    public static PluginConfig pluginConfig;

    public PluginRepository() {
        this.pluginDataConfig = new PluginDataConfig(this.configBasePath, "config.yml");
    }

    public void reloadConfig() {
        this.pluginDataConfig.reload();
    }

    public void saveConfig() {
        this.pluginDataConfig.store();
    }

    public void loadPluginDataConfig() {
        pluginConfig = this.pluginDataConfig.loadPluginDataConfig();
    }

    public static void loadAllData() {
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Data Load Start...");

        PluginRepository pluginRepository = new PluginRepository();
        pluginRepository.loadPluginDataConfig();

        ShopItemRepository shopItemRepository = new ShopItemRepository();
        shopItemRepository.loadShopItem();

        ShopRepository shopRepository = new ShopRepository();
        shopRepository.loadShopItem();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Data Load Complete!");
    }

    public static void saveAllData() {
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Data Store Start...");

        ShopItemRepository shopItemRepository = new ShopItemRepository();
        shopItemRepository.saveShopItem();

        ShopRepository shopRepository = new ShopRepository();
        shopRepository.saveShopItem();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Data Store Complete!");
    }
}
