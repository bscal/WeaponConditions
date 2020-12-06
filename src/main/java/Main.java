import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

	private static Main m_singleton;

	@Override
	public void onEnable()
	{

	}

	public static Main Get()
	{
		return m_singleton;
	}
}
