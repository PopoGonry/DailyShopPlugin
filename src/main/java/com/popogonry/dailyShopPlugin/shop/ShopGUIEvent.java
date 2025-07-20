package com.popogonry.dailyShopPlugin.shop;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;
import com.popogonry.dailyShopPlugin.PluginRepository;
import com.popogonry.dailyShopPlugin.Reference;
import com.popogonry.dailyShopPlugin.shopItem.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ShopGUIEvent implements Listener {
    @EventHandler
    public static void onClickItemListGUI(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(PluginRepository.pluginConfig.getDailyShopName())
                && event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR) {

            event.setCancelled(true);

            if(!event.isLeftClick()) return;

            Player player = (Player) event.getWhoClicked();
            double balance = DailyShopPlugin.getEconomy().getBalance(player);
            Inventory inventory = event.getInventory();

            int slot = event.getRawSlot();

            ShopItem shopItem = null;

            switch (slot) {
                case 10:
                case 12:
                case 14:
                case 16:
                    if(ShopRepository.shopItemHashMap.containsKey(slot%10/2 + 1)) {
                        shopItem = ShopRepository.shopItemHashMap.get(slot%10/2 + 1);
                    }
                    break;
            }

            if(shopItem == null) return;

            if(shopItem.getMaxCount() != 0 && shopItem.getMaxCount() - shopItem.getCount() == 0) {
                // 전체 잔여 수량 0 구매 불가능
                player.sendMessage(Reference.prefix_normal + "구매 불가능한 상품입니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            if(shopItem.getPlayerMaxCount() != 0 && shopItem.getPlayerMaxCount() - shopItem.getPlayerCountMap().getOrDefault(player, 0) == 0) {
                // 개인 잔여 수량 0 구매 불가능
                player.sendMessage(Reference.prefix_normal + "구매 불가능한 상품입니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            if(shopItem.getPrice() > balance) {
                // 돈 부족 구매 불가능
                player.sendMessage(Reference.prefix_normal + "돈이 부족합니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            // 인벤토리 공간 확인
            boolean hasEmptySlot = false;
            for(int playerInventorySlot = 0; playerInventorySlot < 36; playerInventorySlot++) {
                ItemStack itemStack = player.getInventory().getItem(playerInventorySlot);
                if(itemStack == null || itemStack.getType() == Material.AIR) hasEmptySlot = true;
            }

            if(!hasEmptySlot) {
                player.sendMessage(Reference.prefix_error + "인벤토리에 빈 공간이 없습니다.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }


            // 구매 로직

            if(shopItem.getPlayerMaxCount() != 0) {
                shopItem.getPlayerCountMap().put(player, shopItem.getPlayerCountMap().getOrDefault(player, 0) + 1);
            }

            if(shopItem.getMaxCount() != 0) {
                shopItem.setCount(shopItem.getCount() + 1);
            }

            DailyShopPlugin.getEconomy().withdrawPlayer(player, shopItem.getPrice());
            player.getInventory().addItem(new ItemStack(shopItem.getItemStack()));

            player.sendMessage(Reference.prefix_normal + "구매 하였습니다.");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            ShopGUI shopGUI = new ShopGUI();

            Bukkit.getScheduler().runTask(DailyShopPlugin.getServerInstance(), () -> {
                shopGUI.openShopGUI(player);
            });
        }
    }
}
