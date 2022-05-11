package dev.hyo.source.Modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.hyo.source.Main;
import dev.hyo.source.Utils.Functions;
import dev.hyo.source.Utils.ModuleBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.List;

public class LowballSuggester extends ModuleBase {
    int itemDecrementPercent = 10;
    int enchantDecrementPercent = 50;

    public LowballSuggester() {
        super("Lowball Suggester", "Suggest optimal lowballing price for an item you are hovering over. (Only works in trade menu)");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @SubscribeEvent
    public void onHoverItem(ItemTooltipEvent event) {
        if (!isEnabled())
            return;
        if (Main.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) Main.mc.thePlayer.openContainer;
            if (!chest.getLowerChestInventory().getDisplayName().getUnformattedText().startsWith("You "))
                return;

            event.toolTip.add("");
            event.toolTip.add("");
            event.toolTip.add(ChatFormatting.LIGHT_PURPLE.toString() + ChatFormatting.BOLD + "LOWBALL SUGGGESTION");

            //TODO: If the item is recombobulated, take it from recombed lbins.

            String itemName = Functions.CleanName(Functions.FormatName(event.itemStack.getDisplayName()));
            List<String> enchants = Functions.GetItemEnchants(event.toolTip.toArray(new String[0]));

            int totalPrice = CalculateTotal(itemName, enchants);
            event.toolTip.add(String.format("%sTotal price: %s%s Coins (-%d%%)", ChatFormatting.AQUA, ChatFormatting.YELLOW, Functions.PunctuateNumber(totalPrice), itemDecrementPercent));

            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                event.toolTip.add(String.format("%sHold Shift To See More Info (Lowball Suggester)", ChatFormatting.GRAY.toString() + ChatFormatting.ITALIC));

            event.toolTip.add("");

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                if (Main.data.lowestPrices.containsKey(itemName)) {
                    event.toolTip.add(String.format("%s%s: %s%s Coins (-%d%%)", ChatFormatting.DARK_AQUA, itemName, ChatFormatting.YELLOW, Functions.PunctuateNumber(Main.data.lowestPrices.get(itemName) * (100 - itemDecrementPercent) / 100), itemDecrementPercent));
                }
                for (String enchant : enchants) {
                    if (Main.data.lowestPrices.containsKey(enchant)) {
                        if (Main.data.lowestPrices.get(enchant) >= 250000)
                            event.toolTip.add(String.format("%s%s: %s%s Coins (-%d%%)", ChatFormatting.GREEN, enchant, ChatFormatting.YELLOW, Functions.PunctuateNumber(Main.data.lowestPrices.get(enchant) * (100 - enchantDecrementPercent) / 100), enchantDecrementPercent));
                    }
                }
            }
        }
    }

    public int CalculateTotal(String itemName, List<String> enchants) {
        // TODO: Recombed LBINs
        int totalPrice = 0;
        if (Main.data.lowestPrices.containsKey(itemName))
            totalPrice += Main.data.lowestPrices.get(itemName) * (100 - itemDecrementPercent) / 100;
        for (String enchant : enchants) {
            if (Main.data.lowestPrices.containsKey(enchant))
                if (Main.data.lowestPrices.get(enchant) >= 250000)
                    totalPrice += Main.data.lowestPrices.get(enchant) * (100 - enchantDecrementPercent) / 100;
        }
        return totalPrice;
    }
    public void SetItemPercent(int percent) {
        itemDecrementPercent = percent;
    }
    public void SetEnchantPercent(int percent) {
        enchantDecrementPercent = percent;
    }
}
