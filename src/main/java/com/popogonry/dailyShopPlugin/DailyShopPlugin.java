package com.popogonry.dailyShopPlugin;

import com.popogonry.dailyShopPlugin.shop.ShopGUIEvent;
import com.popogonry.dailyShopPlugin.shop.ShopService;
import com.popogonry.dailyShopPlugin.shopItem.ShopItem;
import com.popogonry.dailyShopPlugin.shopItem.ShopItemGUIEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DailyShopPlugin extends JavaPlugin {
    private static Economy economy;
    private static DailyShopPlugin serverInstance;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault 또는 Economy 플러그인을 찾을 수 없습니다. 플러그인을 비활성화합니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Vault 연동 성공: Economy 서비스 준비됨.");

        serverInstance = this;

        ConfigurationSerialization.registerClass(ShopItem.class, "ShopItem");
        getServer().getPluginManager().registerEvents(new ShopItemGUIEvent(), this);
        getServer().getPluginManager().registerEvents(new ShopGUIEvent(), this);
        getServer().getPluginCommand("일일상점").setExecutor(new DailyShopCommand());


        saveDefaultConfig();

        PluginRepository.loadAllData();

        ShopService shopService = new ShopService();
        shopService.start();

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Plugin Enabled (Developer: PopoGonry)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        PluginRepository.saveAllData();
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "DailyShop Plugin Disabled (Developer: PopoGonry)");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp =
                getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /** 다른 클래스에서 사용하려면 getter 제공 */
    public static Economy getEconomy() {
        return economy;
    }

    public static DailyShopPlugin getServerInstance() {
        return serverInstance;
    }
}
