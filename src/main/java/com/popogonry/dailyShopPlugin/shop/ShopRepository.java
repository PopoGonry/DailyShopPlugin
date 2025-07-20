package com.popogonry.dailyShopPlugin.shop;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;
import com.popogonry.dailyShopPlugin.shopItem.ShopItem;

import java.util.HashMap;

public class ShopRepository {
    private final String configBasePath;
    private static final String FILE_NAME = "shop.yml";

    private final ShopDataConfig dataConfig;

    public static HashMap<Integer, ShopItem> shopItemHashMap = new HashMap<>();

    public ShopRepository() {
        this.configBasePath = DailyShopPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new ShopDataConfig(configBasePath, FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasShopItem() {
        return dataConfig.hasShopData();
    }

    public void storeShopItem() {
        dataConfig.storeShopData();
        shopItemHashMap.clear();
    }

    public void saveShopItem() {
        dataConfig.storeShopData();
    }

    public void loadShopItem() {
        shopItemHashMap = dataConfig.loadShopData();
    }

    public void removeShopItem() {
        dataConfig.removeShopData();
    }
}
