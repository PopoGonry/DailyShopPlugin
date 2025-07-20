
package com.popogonry.dailyShopPlugin;

import java.util.List;

public class PluginConfig {
    private final String dailyShopName;
    private final List<String> itemLore;

    public PluginConfig(String dailyShopName, List<String> itemLore) {
        this.dailyShopName = dailyShopName;
        this.itemLore = itemLore;
    }

    @Override
    public String toString() {
        return "PluginConfig{" +
                "dailyShopName='" + dailyShopName + '\'' +
                ", itemLore=" + itemLore +
                '}';
    }

    public String getDailyShopName() {
        return dailyShopName;
    }

    public List<String> getItemLore() {
        return itemLore;
    }
}
