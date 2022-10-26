package org.surf;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.SpawnEggMeta;
import org.surf.command.CommandHandler;
import org.surf.command.NotInPluginYMLException;
import org.surf.moudles.BlockPlace;
import org.surf.moudles.*;
import org.surf.moudles.antiillegal.*;
import org.surf.moudles.antilag.*;
import org.surf.moudles.patches.*;
import org.surf.util.SecondPassEvent;
import org.surf.util.TenSecondPassEvent;
import org.surf.util.Utils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin {
	public static long startTime;
	private final PluginManager pluginManager = getServer().getPluginManager();
	private final ItemUtils itemUtils = new ItemUtils(this);
	SecondPassEvent secondPassEvent = new SecondPassEvent(getLogger(), this);
	private final HashMap<String, Integer> entityIntegerHashMap = new HashMap<>();
	ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
	ConnectionEvent ConnectionEvent = new ConnectionEvent(this);
	TenSecondPassEvent tenSecondPassEvent = new TenSecondPassEvent(getLogger(), this);
	public CommandHandler commandHandler;
	public final Queue<String> discordAlertQueue = new LinkedList<>();

	public void onEnable() {
		new Utils(this);
		saveDefaultConfig();
		commandHandler = new CommandHandler(this);
		startTime = System.currentTimeMillis();
		getLogger().info("Surf enabled. By Dreeam.");
		pluginManager.registerEvents(new BlockPlace(this), this);
		pluginManager.registerEvents(new Offhand(this), this);
		if (PaperLib.isPaper()) {
			pluginManager.registerEvents(new GateWay(), this);
		}
		try {
			commandHandler.registerCommands();
		} catch (NotInPluginYMLException e) {
			e.printStackTrace();
		}
		pluginManager.registerEvents(new BookBan(), this);
		pluginManager.registerEvents(new ChunkBan(this), this);
		pluginManager.registerEvents(new MoveEvent(this), this);
		pluginManager.registerEvents(new EntityDamageEvent(this), this);
		pluginManager.registerEvents(new WitherSpawn(), this);
		pluginManager.registerEvents(new BlockPhysics(this), this);
		pluginManager.registerEvents(new BucketEvent(this), this);
		pluginManager.registerEvents(new MinecartLag(this), this);
//		pluginManager.registerEvents(new ChestLagFix(this), this);
		pluginManager.registerEvents(new Dispensor(this), this);
		pluginManager.registerEvents(ConnectionEvent, this);
		// AntiIllegal events
		pluginManager.registerEvents(new CleanIllegal(this), this);
		//Alert system events
		PaperLib.suggestPaper(this);
		//Server specific events
		service.scheduleAtFixedRate(() -> pluginManager.callEvent(secondPassEvent), 1, 1, TimeUnit.SECONDS);
		service.scheduleAtFixedRate(() -> pluginManager.callEvent(tenSecondPassEvent), 1, 10, TimeUnit.SECONDS);
	}

	public void onDisable() {
		getLogger().info("Surf disabled. By Dreeam");
	}

	public boolean getConfigBoolean(String path) {
		return getConfig().getBoolean(path);
	}
	
	public ItemUtils getItemUtils() {
		return itemUtils;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

}
