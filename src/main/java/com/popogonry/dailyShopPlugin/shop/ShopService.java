package com.popogonry.dailyShopPlugin.shop;

import com.popogonry.dailyShopPlugin.DailyShopPlugin;
import com.popogonry.dailyShopPlugin.shopItem.ShopItem;
import com.popogonry.dailyShopPlugin.shopItem.ShopItemRepository;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ShopService {

    public void reloadShopItem() {
        List<Map.Entry<UUID, ShopItem>> entryList = new ArrayList<>(ShopItemRepository.shopItemHashMap.entrySet());

        // 리스트를 섞는다
        Collections.shuffle(entryList);

        // 최대 4개까지만 뽑기
        int pickCount = Math.min(4, entryList.size());
        List<Map.Entry<UUID, ShopItem>> randomEntries = entryList.subList(0, pickCount);

        ShopRepository.shopItemHashMap.clear();

        for (int i = 1; i <= randomEntries.size(); i++) {
            ShopRepository.shopItemHashMap.put(i, randomEntries.get(i-1).getValue());
        }

        ShopRepository shopRepository = new ShopRepository();
        shopRepository.saveShopItem();
    }

    public void applyShopItem() {
        HashMap<Integer, ShopItem> shopItemHashMap = ShopRepository.shopItemHashMap;
        for (Integer i : shopItemHashMap.keySet()) {
            ShopRepository.shopItemHashMap.put(i, ShopItemRepository.shopItemHashMap.get(shopItemHashMap.get(i).getUuid()));
        }
    }

    public void start() {
        long delayTicks = getTicksUntilNextMidnight(); // 자정까지 대기 시간 계산

        // 자정까지 기다린 후 실행
        new BukkitRunnable() {
            @Override
            public void run() {
                // 첫 실행
                executeCycle();

                // 이후 24시간마다 반복
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        executeCycle();
                    }
                }.runTaskTimerAsynchronously(DailyShopPlugin.getServerInstance(), 24 * 60 * 60 * 20L, 24 * 60 * 60 * 20L);
            }
        }.runTaskLater(DailyShopPlugin.getServerInstance(), delayTicks);
    }

    public long getTicksUntilNextMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).toLocalDate().atStartOfDay();
        Duration duration = Duration.between(now, nextMidnight);
        return duration.getSeconds() * 20L; // 초 → 틱
    }

    public String formatTicksToHMS(long ticks) {
        long totalSeconds = ticks / 20; // 1초 = 20틱
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // 0시간, 0분은 생략하고 싶다면 조건적으로 문자열 생성
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("시간 ");
        if (minutes > 0) sb.append(minutes).append("분 ");
        sb.append(seconds).append("초");

        return sb.toString().trim();
    }

    private void executeCycle() {
        reloadShopItem();
    }
}
