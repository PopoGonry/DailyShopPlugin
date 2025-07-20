package com.popogonry.dailyShopPlugin.shopItem;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;
import com.popogonry.dailyShopPlugin.PluginRepository;
import com.popogonry.dailyShopPlugin.Reference;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.http.WebSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShopItemGUIEvent implements Listener {
    @EventHandler
    public static void onClickItemListGUI(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Reference.prefix_normal + "ShopItem List GUI")
                && event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR) {

            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();

            int slot = event.getRawSlot();

            ShopItemGUI itemGUI = new ShopItemGUI();
            ShopItemService itemService = new ShopItemService();


            String[] strings1 = inventory.getItem(49).getItemMeta().getDisplayName().split("/");
            String[] strings2 = strings1[0].split(" ");
            int page = Integer.parseInt(strings2[1].replaceAll(" ", ""));

            // Item List
            if (0 <= slot && slot <= 44) {
                PluginRepository.playerCurrentItemHashMap.put(player.getUniqueId(), event.getCurrentItem());
                if (!event.getClick().isShiftClick() && event.getClick().isLeftClick()) {
                    PluginRepository.playerInputModeHashMap.put(player.getUniqueId(), ShopItemInputMode.ItemPrice);
                    player.closeInventory();
                    player.sendMessage(Reference.prefix_normal + "가격을 입력 해주세요. ( 취소: -c )");
                }
                else if (!event.getClick().isShiftClick() && event.getClick().isRightClick()) {
                    PluginRepository.playerInputModeHashMap.put(player.getUniqueId(), ShopItemInputMode.PlayerItemLimitAmount);
                    player.closeInventory();
                    player.sendMessage(Reference.prefix_normal + "개인 제한 수량을 입력 해주세요. ( 취소: -c )");
                }
                else if (event.getClick().isShiftClick() && event.getClick().isLeftClick()) {
                    PluginRepository.playerInputModeHashMap.put(player.getUniqueId(), ShopItemInputMode.ItemLimitAmount);
                    player.closeInventory();
                    player.sendMessage(Reference.prefix_normal + "전체 제한 수량을 입력 해주세요. ( 취소: -c )");
                }
                else if (event.getClick().isShiftClick() && event.getClick().isRightClick()) {
                    ItemStack itemStack = inventory.getItem(slot);
                    List<String> loreList = itemStack.getItemMeta().getLore();
                    for (String lore : loreList) {
                        if (lore.contains("UUID:")) {
                            String[] strings = lore.split(": §e");
                            UUID uuid = UUID.fromString(strings[1].replaceAll(" ", ""));
                            itemService.removeItem(uuid);
                        }
                    }

                    player.sendMessage(Reference.prefix_normal + inventory.getItem(slot).getItemMeta().getDisplayName() + "아이템이 제거 되었습니다.");
                    itemGUI.openItemListGUI(player, page);
                }


            } else if (48 <= slot && slot <= 50) {
                ItemStack itemStack = inventory.getItem(slot);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.getDisplayName().contains("To")) {
                    String[] strings = itemMeta.getDisplayName().split(" ");
                    itemGUI.openItemListGUI(player, Integer.parseInt(strings[1]));
                }
            }

            // Player Inventory
            else if (54 <= slot && slot <= 89) {
                itemService.createItem(new ShopItem(UUID.randomUUID(), new ItemStack(event.getCurrentItem()), 0, 0, new HashMap<>(), 0, 0));
                player.sendMessage(Reference.prefix_normal + "아이템이 추가 되었습니다.");
                int maxPage = ShopItemRepository.shopItemHashMap.size() / 45;
                maxPage += ShopItemRepository.shopItemHashMap.size() % 45 == 0 ? 0 : 1;
                itemGUI.openItemListGUI(player, maxPage);

//                player.sendMessage(event.getCurrentItem().toString());
            }
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(PluginRepository.playerInputModeHashMap.getOrDefault(event.getPlayer().getUniqueId(), null) instanceof ShopItemInputMode
                && PluginRepository.playerCurrentItemHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            ShopItemInputMode mode = (ShopItemInputMode) PluginRepository.playerInputModeHashMap.get(player.getUniqueId());

            ShopItemGUI itemGUI = new ShopItemGUI();

            ShopItem item = null;

            List<String> loreList = PluginRepository.playerCurrentItemHashMap.get(player.getUniqueId()).getItemMeta().getLore();
            for (String lore : loreList) {
                if (lore.contains("UUID:")) {
                    String[] strings = lore.split(": §e");
                    UUID uuid = UUID.fromString(strings[1].replaceAll(" ", ""));
                    item = ShopItemRepository.shopItemHashMap.get(uuid);
                    break;
                }
            }

            if(event.getMessage().equalsIgnoreCase("-c")) {
                PluginRepository.playerInputModeHashMap.remove(player.getUniqueId());
                PluginRepository.playerCurrentItemHashMap.remove(player.getUniqueId());

                Bukkit.getScheduler().runTask(DailyShopPlugin.getServerInstance(), () -> {
                    itemGUI.openItemListGUI(player, 1);
                });

                return;
            }

            else if(StringUtils.isNumeric(event.getMessage()) && item != null) {
                int value = Integer.parseInt(event.getMessage());

                switch(mode) {
                    case ItemPrice:
                        item.setPrice(value);
                        player.sendMessage(Reference.prefix_normal + "아이템의 가격이 " + item.getPrice() + "로 변경 되었습니다.");
                        break;

                    case PlayerItemLimitAmount:
                        item.setPlayerMaxCount(value);
                        item.setPlayerCountMap(new HashMap<>());
                        player.sendMessage(Reference.prefix_normal + "아이템의 개인 제한 수량이 " + item.getPlayerMaxCount() + "로 변경 되었습니다.");
                        break;

                    case ItemLimitAmount:
                        item.setMaxCount(value);
                        item.setCount(0);
                        player.sendMessage(Reference.prefix_normal + "아이템의 전체 제한 수량이 " + item.getMaxCount() + "로 변경 되었습니다.");
                        break;

                    default:
                        player.sendMessage(Reference.prefix_normal + "잘못된 입력입니다.");
                        break;
                }

                PluginRepository.playerInputModeHashMap.remove(player.getUniqueId());
                PluginRepository.playerCurrentItemHashMap.remove(player.getUniqueId());
                Bukkit.getScheduler().runTask(DailyShopPlugin.getServerInstance(), () -> {
                    itemGUI.openItemListGUI(player, 1);
                });

            }
        }
    }
}