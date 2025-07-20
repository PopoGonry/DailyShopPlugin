package com.popogonry.dailyShopPlugin.shopItem;

import java.util.UUID;

public class ShopItemService {
    public boolean createItem(ShopItem item) {
        if(ShopItemRepository.shopItemHashMap.containsKey(item.getUuid())) {
            return false;
        }

        ShopItemRepository.shopItemHashMap.put(item.getUuid(), item);

        ShopItemRepository shopItemRepository = new ShopItemRepository();
        shopItemRepository.saveShopItem();

        return true;
    }

    public boolean removeItem(UUID uuid) {

        if(!ShopItemRepository.shopItemHashMap.containsKey(uuid)) {
            return false;
        }
        ShopItemRepository shopItemRepository = new ShopItemRepository();

        ShopItemRepository.shopItemHashMap.remove(uuid);
        shopItemRepository.saveShopItem();

        return true;
    }
}
