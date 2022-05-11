package dev.hyo.source;

import dev.hyo.source.Commands.LowballSuggesterCommand;
import dev.hyo.source.Modules.LowballSuggester;
import dev.hyo.source.Utils.Threads.LowestBinUpdateThread;
import dev.hyo.source.Utils.Threads.UpdateTotalPageThread;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = "pdm", name = "Pieces de Monnaie", version = "0.0.1", acceptedMinecraftVersions = "[1.8.9]")
@SideOnly(Side.CLIENT)
public class Main {
    public static final String NAME = "Pieces de Monnaie";
    public static final String VERSION = "1.0";
    public static LowballSuggester lowballSuggestion = new LowballSuggester();
    public static Minecraft mc = Minecraft.getMinecraft();
    public static List<LowestBinUpdateThread> updateThreads = new ArrayList<>();
    public static UpdateTotalPageThread updateTotalPageThread;
    public static Data data = new Data();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        register(this);
        register(lowballSuggestion);
        try {
            for (int i = 0; i < 2; i++) { // 5개의 스레드 생성
                updateThreads.add(new LowestBinUpdateThread());
            }
            updateTotalPageThread = new UpdateTotalPageThread();
        } catch (Exception e) {
            // 에러 출력
            System.out.println("[Pièces de Monnaie] Error while loading");
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        registerCommand((ICommand) new LowballSuggesterCommand());
    }

    private static void register(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }
    private static void registerCommand(ICommand command) {
        ClientCommandHandler.instance.registerCommand(command);
    }
}
