package dev.hyo.source.Utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.hyo.source.Main;
import net.minecraft.util.ChatComponentText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// 수업 시작 전에 사용하는 기능을 제공하는 클래스
public class Functions {
    public static String FormatName(String name) {
        return name
                .replaceAll("\u00a7[0-9a-fk-or]", "")
                .replaceAll("\\P{Print}", "");
    } // 아이템 이름에서 색상 표시를 제거하는 함수

    public static String CleanName(String name) {
        String cleanName = FormatName(name);
        for (String reforge : Main.data.reforgeNames) {
            if (name.contains(reforge)) {
                return name.replace(reforge, "");
            }
        }
        return cleanName.replace("✪", "").trim();
    } // 아이템 이름에서 리워지 이름을 제거하는 함수

    public static int GetItemStars(String name) {
        int stars = 0;
        // for each character in the name check if it is a star
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '✪') {
                stars++;
            }
        }
        return stars;
    } // 아이템의 별의 수를 가져오는 메소드

    public static List<String> GetItemEnchants(String[] lore) {
        List<String> enchants = new ArrayList<>();
        for (String line : lore) {
            String formattedLine = FormatName(line);

            for (String enchant : Main.data.enchantNames) {
                if (formattedLine.startsWith(enchant)) {
                    if (formattedLine.contains(", ")) {
                        String[] split = formattedLine.split(", ");
                        for (String s : split) {
                            enchants.add(s);
                        }
                        break;
                    }
                    enchants.add(formattedLine);
                    break;
                }
            }
        }
        return enchants;
    } // 아이템의 인챈트를 가져오는 메소드

    public static void SendMessage(String message) {
        if (Main.mc.thePlayer != null) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    } // 메시지를 출력하는 메소드

    public static String PunctuateNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    } // 숫자를 콤마로 구분하여 변환하는 메소드
}
