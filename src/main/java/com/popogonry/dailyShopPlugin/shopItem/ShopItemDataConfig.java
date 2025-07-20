package com.popogonry.dailyShopPlugin.shopItem;

import com.popogonry.dailyShopPlugin.Config;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ShopItemDataConfig extends Config {
    private static final String dataKey = "ShopItem";

    public ShopItemDataConfig(String basePath, String fileName) {
        super(basePath, fileName);
        loadDefaults();
    }

    /** 저장 */
    public void storeItemData() {
        // 기존 데이터 초기화
        getConfig().set(dataKey, null);
        // shopItemHashMap: Map<UUID, ShopItem>
        for (Map.Entry<UUID, ShopItem> entry : ShopItemRepository.shopItemHashMap.entrySet()) {
            // key: UUID 문자열, value: ShopItem (ConfigurationSerializable)
            getConfig().set(dataKey + "." + entry.getKey().toString(), entry.getValue());
        }
        super.store(); // or saveConfig()
    }

    /** 로드 */
    public HashMap<UUID, ShopItem> loadItemData() {
        HashMap<UUID, ShopItem> result = new HashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection(dataKey);
        if (section == null) {
            return result;
        }

        for (String key : section.getKeys(false)) {
            // 각 서브키(key) 는 UUID 문자열
            ShopItem item = section.getSerializable(key, ShopItem.class);
            if (item != null) {
                UUID uuid = UUID.fromString(key);
                result.put(uuid, item);
            }
        }
        return result;
    }

    public boolean hasItemData() {
        return getConfig().contains(dataKey);
    }

    public void removeItemData() {
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

