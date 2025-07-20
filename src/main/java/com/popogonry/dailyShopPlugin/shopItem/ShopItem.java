package com.popogonry.dailyShopPlugin.shopItem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ShopItem implements ConfigurationSerializable {
    private UUID uuid;
    private ItemStack itemStack;
    private int price;

    // 개인당 구매 가능 횟수
    private int playerMaxCount;
    private HashMap<Player, Integer> playerCountMap;

    // 전체 구매 가능 횟수
    private int maxCount;
    private int count;

    public ShopItem(UUID uuid, ItemStack itemStack, int price, int playerMaxCount, HashMap<Player, Integer> playerCountMap, int maxCount, int count) {
        this.uuid = uuid;
        this.itemStack = itemStack;
        this.price = price;
        this.playerMaxCount = playerMaxCount;
        this.playerCountMap = playerCountMap;
        this.maxCount = maxCount;
        this.count = count;
    }

    @SuppressWarnings("unchecked")
    private ShopItem(Map<String, Object> map) {
        // 1) UUID 복원
        this.uuid             = UUID.fromString((String) map.get("uuid"));

        this.itemStack        = (ItemStack) map.get("itemStack");
        this.price            = (int)      map.get("price");
        this.playerMaxCount   = (int)      map.get("playerMaxCount");

        // playerCountMap 복원 (UUID 문자열 → Player 객체)
        Map<String, Object> raw = (Map<String, Object>) map.get("playerCountMap");
        this.playerCountMap = raw.entrySet().stream()
                .map(e -> {
                    UUID playerUuid = UUID.fromString(e.getKey());
                    Player p        = Bukkit.getPlayer(playerUuid);
                    return p != null
                            ? new AbstractMap.SimpleEntry<>(p, (Integer) e.getValue())
                            : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b,
                        HashMap::new
                ));

        this.maxCount         = (int) map.get("maxCount");
        this.count            = (int) map.get("count");
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        // 1) UUID 저장
        map.put("uuid", uuid.toString());

        map.put("itemStack",      itemStack);
        map.put("price",          price);
        map.put("playerMaxCount", playerMaxCount);

        // Player → UUID 문자열 맵으로 변환
        Map<String, Integer> tmp = new HashMap<>();
        playerCountMap.forEach((player, cnt) ->
                tmp.put(player.getUniqueId().toString(), cnt)
        );
        map.put("playerCountMap", tmp);

        map.put("maxCount",       maxCount);
        map.put("count",          count);
        return map;
    }

    public static ShopItem deserialize(Map<String, Object> args) {
        return new ShopItem(args);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPlayerMaxCount() {
        return playerMaxCount;
    }

    public void setPlayerMaxCount(int playerMaxCount) {
        this.playerMaxCount = playerMaxCount;
    }

    public HashMap<Player, Integer> getPlayerCountMap() {
        return playerCountMap;
    }

    public void setPlayerCountMap(HashMap<Player, Integer> playerCountMap) {
        this.playerCountMap = playerCountMap;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ShopItem{" +
                "uuid=" + uuid +
                ", itemStack=" + itemStack +
                ", price=" + price +
                ", playerMaxCount=" + playerMaxCount +
                ", playerCountMap=" + playerCountMap +
                ", maxCount=" + maxCount +
                ", count=" + count +
                '}';
    }
}
