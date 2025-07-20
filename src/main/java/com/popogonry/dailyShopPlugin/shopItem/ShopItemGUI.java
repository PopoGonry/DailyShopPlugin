package com.popogonry.dailyShopPlugin.shopItem;

import com.popogonry.dailyShopPlugin.GUI;
import com.popogonry.dailyShopPlugin.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class ShopItemGUI {
    public boolean openItemListGUI(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(player, 54, Reference.prefix_normal + "ShopItem List GUI");

        List<ShopItem> shopItemList = new ArrayList<>(ShopItemRepository.shopItemHashMap.values());
        shopItemList.sort(Comparator.comparing(ShopItem::getUuid));

        for (int i = 0 + (45*(page-1)); i < 45 + (45*(page-1)) && i < shopItemList.size(); i++) {
            inventory.addItem(getItemStack(shopItemList.get(i)));
        }

        // 48 49 50
        int maxPage = shopItemList.size() / 45;
        maxPage += shopItemList.size() % 45 == 0 ? 0 : 1;

        inventory.setItem(49, GUI.getCustomItemStack(Material.EMERALD, Reference.prefix + "Page " + page + " / " + maxPage, Collections.singletonList(ChatColor.GOLD + "Amount of Items: " + shopItemList.size())));

        if(page > 1) {
            inventory.setItem(48, GUI.getCustomItemStack(Material.PAPER, Reference.prefix + "To " + (page - 1)));
        }

        if(page < maxPage) {
            inventory.setItem(50, GUI.getCustomItemStack(Material.PAPER, Reference.prefix + "To " + (page + 1)));
        }

        player.openInventory(inventory);

        return true;
    }

    public ItemStack getItemStack(ShopItem shopItem) {

        ItemStack returnItemStack = new ItemStack(shopItem.getItemStack());
        ItemMeta returnItemMeta = returnItemStack.getItemMeta();

        returnItemMeta.setDisplayName(returnItemMeta.getDisplayName());

        List<String> lore = returnItemStack.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.WHITE + "---------------------");
        // 정보 추가 (색상 포함)
        lore.add(ChatColor.GREEN + "가격: " + ChatColor.YELLOW + shopItem.getPrice());
        lore.add(ChatColor.GREEN + "개인 제한 수량: " + ChatColor.YELLOW + shopItem.getPlayerMaxCount());
//        lore.add(ChatColor.GREEN + "전체 잔여 수량: " + ChatColor.YELLOW + shopItem.getCount());
        lore.add(ChatColor.GREEN + "전체 제한 수량: " + ChatColor.YELLOW + shopItem.getMaxCount());
        lore.add(ChatColor.GREEN + "UUID: " + ChatColor.YELLOW + shopItem.getUuid());


        lore.add(ChatColor.WHITE + "---------------------");
        lore.add(ChatColor.GOLD + "- 좌클릭: 아이템 가격 설정");
        lore.add(ChatColor.GOLD + "- 우클릭: 개인당 아이템 수량설정 (설정 안하면 표시 X)");
        lore.add(ChatColor.GOLD + "- 쉬프트 + 좌클릭: 모든 유저 공용 한정판매 수량설정 (설정 안하면 표시 X)");
        lore.add(ChatColor.GOLD + "- 쉬프트 + 우클릭: 아이템 제거");
        lore.add(ChatColor.GOLD + "- 플레이어 인벤토리 좌클릭: 아이템 추가");

        returnItemMeta.setLore(lore);
        returnItemStack.setItemMeta(returnItemMeta);

        return returnItemStack;
    }
}
