package com.popogonry.dailyShopPlugin;

import com.popogonry.dailyShopPlugin.shop.ShopGUI;
import com.popogonry.dailyShopPlugin.shop.ShopService;
import com.popogonry.dailyShopPlugin.shopItem.ShopItemGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DailyShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0 && sender instanceof Player) {
            ShopGUI shopGUI = new ShopGUI();
            Player player = (Player) sender;
            shopGUI.openShopGUI(player);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            return true;
        }

        if (!sender.isOp()) {
            sendCommandHelp(sender);
            return false;
        }

        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("편집") && sender instanceof Player) {
                ShopItemGUI shopItemGUI = new ShopItemGUI();
                Player player = (Player) sender;
                shopItemGUI.openItemListGUI(player, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                return true;
            }
            else if (args[0].equalsIgnoreCase("변경")) {
                ShopService shopService = new ShopService();
                shopService.reloadShopItem();
                sender.sendMessage(Reference.prefix_normal + "일일상점에 물품들을 다시 불러왔습니다.");
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("적용")) {
                ShopService shopService = new ShopService();
                shopService.applyShopItem();
                sender.sendMessage(Reference.prefix_normal + "수정 사항을 일일상점에 적용하였습니다.");
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
                return true;
            }
        }
        sendCommandHelp(sender);
        return false;
    }
    // 명령어 목록 출력
    private void sendCommandHelp(CommandSender sender) {
        sender.sendMessage("§e----- [ 일일상점 명령어 목록 ] -----");
        sender.sendMessage("§6/일일상점 §f- 일일상점을 엽니다.");
        if (sender.isOp()) {
            sender.sendMessage("§6/일일상점 편집 §f- 상점 아이템 편집 GUI를 엽니다.");
            sender.sendMessage("§6/일일상점 변경 §f- 상점 아이템을 다시 불러옵니다.");
            sender.sendMessage("§6/일일상점 적용 §f- 편집한 내용을 상점에 적용합니다.");
        }
        sender.sendMessage("§e-----------------------------------");
    }
}

