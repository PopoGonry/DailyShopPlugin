package com.popogonry.dailyShopPlugin.shop;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;
import com.popogonry.dailyShopPlugin.GUI;
import com.popogonry.dailyShopPlugin.PluginRepository;
import com.popogonry.dailyShopPlugin.Reference;
import com.popogonry.dailyShopPlugin.shopItem.ShopItem;
import com.popogonry.dailyShopPlugin.shopItem.ShopItemRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShopGUI {
    public boolean openShopGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, PluginRepository.pluginConfig.getDailyShopName());

        for(int i = 1; i <= 4; i++){
            if(ShopRepository.shopItemHashMap.containsKey(i)) {
                inventory.setItem(10 + 2*(i-1), getShopItemStack(ShopRepository.shopItemHashMap.get(i), player));
            }
        }

        ShopService shopService = new ShopService();

        inventory.setItem(0, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(1, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(2, GUI.getCustomItemStack(Material.HEART_POTTERY_SHERD, " "));
        inventory.setItem(3, GUI.getCustomItemStack(Material.MINER_POTTERY_SHERD, " "));
        inventory.setItem(4, GUI.getCustomItemStack(Material.PLENTY_POTTERY_SHERD, " "));
        inventory.setItem(5, GUI.getCustomItemStack(Material.PRIZE_POTTERY_SHERD, " "));
        inventory.setItem(6, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(7, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(8, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(9, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(11, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(13, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(15, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(17, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(18, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(19, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(20, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(21, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(23, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(24, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));
        inventory.setItem(25, GUI.getCustomItemStack(Material.SHELTER_POTTERY_SHERD, " "));
        inventory.setItem(26, GUI.getCustomItemStack(Material.SNORT_POTTERY_SHERD, " "));

        inventory.setItem(22, GUI.getCustomItemStack(Material.CLOCK, "§f§l남은 시간 : §6§l" + shopService.formatTicksToHMS(shopService.getTicksUntilNextMidnight()) + "§f§l"));

        player.openInventory(inventory);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getOpenInventory().getTopInventory().equals(inventory)) {
                    // 플레이어가 이 인벤토리를 더 이상 보고 있지 않다면 스케줄 취소
                    this.cancel();
                    return;
                }
                inventory.setItem(22, GUI.getCustomItemStack(Material.CLOCK, "§f§l남은 시간 : §6§l" + shopService.formatTicksToHMS(shopService.getTicksUntilNextMidnight()) + "§f§l"));
            }
        }.runTaskTimerAsynchronously(DailyShopPlugin.getServerInstance(), 0L, 20L);
        return true;
    }

    public ItemStack getShopItemStack(ShopItem shopItem, Player player) {

        ItemStack returnItemStack = new ItemStack(shopItem.getItemStack());
        ItemMeta returnItemMeta = returnItemStack.getItemMeta();

        returnItemMeta.setDisplayName(returnItemMeta.getDisplayName());

        List<String> lore = returnItemStack.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        List<String> itemLore = PluginRepository.pluginConfig.getItemLore();
        for (String s : itemLore) {

            if((s.contains("{개인최대수량}") || s.contains("{개인잔여수량}")) && shopItem.getPlayerMaxCount() == 0) {
                continue;
            }
            if((s.contains("{전체최대수량}") || s.contains("{전체잔여수량}")) && shopItem.getMaxCount() == 0) {
                continue;
            }

            s = s.replaceAll("\\{가격}", String.valueOf(shopItem.getPrice()));
            s = s.replaceAll("\\{개인잔여수량}", String.valueOf(shopItem.getPlayerMaxCount() - shopItem.getPlayerCountMap().getOrDefault(player, 0)));
            s = s.replaceAll("\\{개인최대수량}", String.valueOf(shopItem.getPlayerMaxCount()));
            s = s.replaceAll("\\{전체잔여수량}", String.valueOf(shopItem.getMaxCount() - shopItem.getCount()));
            s = s.replaceAll("\\{전체최대수량}", String.valueOf(shopItem.getMaxCount()));
            s = s.replaceAll("\\{돈}", String.valueOf(DailyShopPlugin.getEconomy().getBalance(player)));
            s = s.replaceAll("&", "§");
            lore.add(s);
        }

        returnItemMeta.setLore(lore);
        returnItemStack.setItemMeta(returnItemMeta);

        return returnItemStack;
    }

}
