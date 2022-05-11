package dev.hyo.source.Utils.Threads;

import dev.hyo.source.Main;
import dev.hyo.source.Utils.AuctionHouseAPI;

public class UpdateTotalPageThread extends Thread {
    private AuctionHouseAPI api; // API
    public UpdateTotalPageThread() {
        this.setDaemon(true);
        this.start();
    }

    public void run() {
        while (true) {
            try {
                api = new AuctionHouseAPI(); // 새로운 객체를 생성하여 전역변수에 저장
                Main.data.maxPage = api.GetTotalActivePages(); // 전역변수에 저장된 객체를 통해 페이지 수를 가져옴
                Thread.sleep(2000); // 2초마다 업데이트
            } catch (InterruptedException e) { // 인터럽트 발생시 예외처리
                e.printStackTrace(); // 예외처리
            }
        }
    }
}
