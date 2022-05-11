package dev.hyo.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Data {
    public int currentPage = 0;
    public int maxPage = 0;
    public List<String> reforgeNames;
    public List<String> enchantNames;

    public Hashtable<String, Integer> lowestPrices = new Hashtable<>();
    // TODO: public Hashtable<String, Integer> lowestPricesRecombed = new Hashtable<>();

    public Data() {
        reforgeNames = GetReforgeNames();
        enchantNames = GetEnchantNames();
    }

    private List<String> GetReforgeNames() {
        //Load reforge names from resources/data/Reforges.txt
        List<String> reforgeNames = new ArrayList<>();

        System.out.println("Loading reforge names...");

        URL resource = getClass().getClassLoader().getResource("data/Reforges.txt");
        BufferedReader br;
        try {
            assert resource != null;
            br = new BufferedReader(new java.io.InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                reforgeNames.add(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reforgeNames;
    } // 리포지션 이름 반환

    private List<String> GetEnchantNames() {
        //Load enchant names from resources/data/Enchantments.txt
        List<String> enchantNames = new ArrayList<>();

        System.out.println("Loading enchant names...");

        URL resource = getClass().getClassLoader().getResource("data/Enchantments.txt");
        BufferedReader br;
        try {
            assert resource != null;
            br = new BufferedReader(new java.io.InputStreamReader(resource.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                enchantNames.add(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return enchantNames;
    } // 인챈트 이름 반환
}