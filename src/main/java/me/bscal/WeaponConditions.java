import com.bergerkiller.bukkit.common.math.Vector4;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.plugin.java.JavaPlugin;

public class WeaponConditions extends JavaPlugin {

    private static WeaponConditions m_singleton;

    @Override
    public void onEnable() {
        m_singleton = this;

        BukkitLibraryManager libs = new BukkitLibraryManager(this);
        libs.addMavenLocal();
        libs.addRepository("https://ci.mg-dev.eu/plugin/repository/everything/");

        Library BKCommonLib = Library.builder().groupId("com.bergerkiller.bukkit").artifactId("BKCommonLib")
                .version("1.16.4-v2").classifier("shaded").build();
        libs.loadLibrary(BKCommonLib);



        Vector4 vec4 = new Vector4(1, 2, 3, 4);
        getLogger().info(vec4.toString());

    }

    public static WeaponConditions Get() {
        return m_singleton;
    }
}
