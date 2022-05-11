package dev.hyo.source.Utils.Threads;

import dev.hyo.source.Main;
import dev.hyo.source.Utils.AuctionHouseAPI;
import dev.hyo.source.Utils.Functions;

import java.io.IOException;
import java.util.List;


public class LowestBinUpdateThread extends Thread {
    public int intervalBetweenIterationMS = 5; // 5밀리초
    public AuctionHouseAPI api;
    public LowestBinUpdateThread(){
        this.setDaemon(true); // 데몬 쓰레드로 설정
        this.start(); // 쓰레드 시작
    }

    public void run() {
        // 업데이트 실행
        while (true) try {
            Main.data.currentPage++;
            int page = Main.data.currentPage; // 현재 페이지
            // 현재 페이지를 가져온다.
            if (Main.data.currentPage >= Main.data.maxPage) {
                Main.data.currentPage= 0;
                System.out.println("[AuctionsUpdateThread] Reset current page to 0.");
                sleep(1000);
                continue;
            }
            api = new AuctionHouseAPI(); // AuctionHouseAPI 객체 생성

            List<AuctionHouseAPI.Auction> activeAuctions = api.GetActiveAuctionsFromPage(page); // 현재 페이지의 경매중인 경매들을 가져온다.

            for (AuctionHouseAPI.Auction auction : activeAuctions) {
                if (!auction.bin)
                    continue;
                if (auction.item_name.equals("Enchanted Book"))
                {
                    String enchant = Functions.FormatName(auction.item_lore.split("\n")[0]);
                    if (!Main.data.lowestPrices.containsKey(enchant)) {
                        Main.data.lowestPrices.put(enchant, auction.starting_bid);
                        System.out.println("[LowestBinUpdateThread] Add new lowest price for " + enchant + ": " + auction.starting_bid);
                        continue;
                    }
                    if (auction.starting_bid < Main.data.lowestPrices.get(enchant))
                        Main.data.lowestPrices.put(enchant, auction.starting_bid);
                    System.out.println("[LowestBinUpdateThread] Update lowest price for " + enchant + ": " + auction.starting_bid);
                    continue;
                }
                String itemName = Functions.FormatName(Functions.CleanName(auction.item_name));
                if (!Main.data.lowestPrices.containsKey(itemName)) {
                    Main.data.lowestPrices.put(itemName, auction.starting_bid);
                    System.out.println("[LowestBinUpdateThread] Add " + itemName + " to lowest prices.");
                    continue;
                }
                if (auction.starting_bid < Main.data.lowestPrices.get(itemName))
                    Main.data.lowestPrices.replace(itemName, auction.starting_bid);
                System.out.println("[LowestBinUpdateThread] Update " + itemName + " to lowest prices.");
            }
            System.out.println("[AuctionsUpdateThread] Page " + page + " is updated.");
            sleep(intervalBetweenIterationMS); // 업데이트 주기
        } catch (InterruptedException | IOException e) { // 에러 발생시
            e.printStackTrace(); // 에러 출력
        }
    }
}
