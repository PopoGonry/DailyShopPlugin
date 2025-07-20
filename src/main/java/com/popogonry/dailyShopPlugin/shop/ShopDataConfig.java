package com.popogonry.dailyShopPlugin.shop;

import com.popogonry.dailyShopPlugin.Config;
import com.popogonry.dailyShopPlugin.shopItem.ShopItem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ShopDataConfig extends Config {
    private static final String dataKey = "Shop";

    public ShopDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
        loadDefaults();
    }

    /** 저장 */
    public void storeShopData() {
        // 기존 데이터 초기화
        getConfig().set(dataKey, null);
        for (Map.Entry<Integer, ShopItem> entry : ShopRepository.shopItemHashMap.entrySet()) {
            getConfig().set(dataKey + "." + entry.getKey().toString(), entry.getValue());
        }
        super.store(); // or saveConfig()
    }

    /** 로드 */
    public HashMap<Integer, ShopItem> loadShopData() {
        HashMap<Integer, ShopItem> result = new HashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection(dataKey);
        if (section == null) {
            return result;
        }

        for (String key : section.getKeys(false)) {
            ShopItem item = section.getSerializable(key, ShopItem.class);
            if (item != null) {
                Integer i = Integer.parseInt(key);
                result.put(i, item);
            }
        }
        return result;
    }

    public boolean hasShopData() {
        return getConfig().contains(dataKey);
    }

    public void removeShopData() {
        getConfig().set(dataKey, null);
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void applySettings() {
        getConfig().options().copyDefaults(true);
    }
}
