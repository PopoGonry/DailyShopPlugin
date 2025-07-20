package com.popogonry.dailyShopPlugin.shopItem;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShopItemRepository {
    private final String configBasePath;
    private static final String FILE_NAME = "shopItem.yml";

    private final ShopItemDataConfig dataConfig;

    public static HashMap<UUID, ShopItem> shopItemHashMap = new HashMap<>();

    public ShopItemRepository() {
        this.configBasePath = DailyShopPlugin.getServerInstance().getDataFolder().getAbsolutePath();
        this.dataConfig = new ShopItemDataConfig(configBasePath, FILE_NAME);
    }

    public void reloadConfig() {
        dataConfig.reload();
    }

    public void saveConfig() {
        dataConfig.store();
    }

    public boolean hasShopItem() {
        return dataConfig.hasItemData();
    }

    public void storeShopItem() {
        dataConfig.storeItemData();
        shopItemHashMap.clear();
    }

    public void saveShopItem() {
        dataConfig.storeItemData();
    }

    public void loadShopItem() {
        shopItemHashMap = dataConfig.loadItemData();
    }

    public void removeShopItem() {
        dataConfig.removeItemData();
    }
}
