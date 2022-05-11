package dev.hyo.source.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AuctionHouseAPI {
    public static class Auction {
        public String item_name;
        public String item_lore;
        public String extra;
        public String category;
        public String tier;
        public int starting_bid;
        public boolean claimed;
        public boolean bin;

        public Auction(String item_name, String item_lore, String extra, String category, String tier, int starting_bid, boolean claimed, boolean bin) {
            this.item_name = item_name;
            this.item_lore = item_lore;
            this.extra = extra;
            this.category = category;
            this.tier = tier;
            this.starting_bid = starting_bid;
            this.claimed = claimed;
            this.bin = bin;
        }
    }

    public int GetTotalActivePages() {
        HttpURLConnection conn;
        StringBuilder content = new StringBuilder();
        int total_pages = 0;
        try {
            URL url = new URL("https://api.hypixel.net/skyblock/auctions");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                System.out.println("[AuctionHouseAPI.GetTotalPages] Error: " + status);
                return 0;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            if (content.toString().startsWith("{")) {
                JsonObject json = new JsonParser().parse(content.toString()).getAsJsonObject();
                total_pages = json.get("totalPages").getAsInt();
            } else {
                System.out.println("[AuctionHouseAPI.GetTotalPages] Error: " + content);
                return 0;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        conn.disconnect();
        return total_pages;
    }

    public List<Auction> GetActiveAuctionsFromPage(int page) throws IOException {
        HttpURLConnection conn;
        List<Auction> auctions = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL("https://api.hypixel.net/skyblock/auctions?page=" + page);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                System.out.println("[AuctionHouseAPI.GetAuctionsFromPage] Error: " + status);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            if (content.toString().startsWith("{")) {
                JsonObject json = new JsonParser().parse(content.toString()).getAsJsonObject();
                json.get("auctions").getAsJsonArray().forEach(auction -> {
                    JsonObject auctionObject = auction.getAsJsonObject();
                    auctions.add(new Auction(auctionObject.get("item_name").getAsString(),
                            auctionObject.get("item_lore").getAsString(),
                            auctionObject.get("extra").getAsString(),
                            auctionObject.get("category").getAsString(),
                            auctionObject.get("tier").getAsString(),
                            auctionObject.get("starting_bid").getAsInt(),
                            auctionObject.get("claimed").getAsBoolean(),
                            auctionObject.get("bin").getAsBoolean()));
                });
            } else {
                System.out.println("[AuctionHouseAPI.GetAuctionsFromPage] Error: " + content.toString());
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        conn.disconnect();
        return auctions;
    }
}
