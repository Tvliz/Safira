package cn.nukkit;

import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.ServerKiller;

import com.google.common.base.Preconditions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class Nukkit
{

	public final static String VERSION = "1.0dev";

	public final static String API_VERSION = "1.0.0";

	public final static String CODENAME = "蘋果(Apple)派(Pie)";

	public final static String MINECRAFT_VERSION = "v0.15.10 alpha";

	public final static String MINECRAFT_VERSION_NETWORK = "0.15.10";

	public final static String PATH = System.getProperty("user.dir") + "/";

	public final static String DATA_PATH = System.getProperty("user.dir") + "/";

	public final static String PLUGIN_PATH = DATA_PATH + "plugins";

	public static final long START_TIME = System.currentTimeMillis();

	public static boolean shortTitle = false;

	public static int DEBUG = 1;

	public static void main(String[] args)
	{

		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("windows"))
		{
			if (osName.contains("windows 8") || osName.contains("2012"))
			{
				shortTitle = true;
			}
		}

		MainLogger.getLogger().info("Starting Nukkit Server For Minecraft: PE");

		new Server(PATH, DATA_PATH, PLUGIN_PATH);

		MainLogger.getLogger().info("Stopping server");
		MainLogger.getLogger().info("Stopping other threads");

		Thread.getAllStackTraces().keySet().stream().filter(thread -> thread instanceof InterruptibleThread).forEach(thread -> {
			MainLogger.getLogger().debug(String.format("Stopping %s thread", thread.getClass().getSimpleName()));
			if (thread.isAlive())
			{
				thread.interrupt();
			}
		});

		var killer = new ServerKiller(8);

		killer.start();

		MainLogger.getLogger().info("Server stopped");

		System.exit(0);
	}

	public static Level getLogLevel()
	{
		if (LogManager.getContext(false) instanceof LoggerContext context)
		{
			var configuration = context.getConfiguration();
			var loggerConfiguration = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			return loggerConfiguration.getLevel();
		}
		return Level.INFO;
	}

	public static void setLogLevel(
		Level level
	)
	{
		Preconditions.checkNotNull(level, "Logger level cannot be null");
		if (LogManager.getContext(false) instanceof LoggerContext context)
		{
			var configuration = context.getConfiguration();
			var loggerConfiguration = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			loggerConfiguration.setLevel(level);
			context.updateLoggers();
		}
	}

}
