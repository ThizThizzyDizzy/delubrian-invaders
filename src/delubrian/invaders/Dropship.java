package delubrian.invaders;
import delubrian.invaders.menu.component.MenuComponentParkedDropship;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import java.io.Serializable;
import java.util.ArrayList;
import simplelibrary.config2.Config;
public class Dropship implements Serializable{
    public static int cargoSpace = 100;
    public ArrayList<ItemStack> cargo = new ArrayList<>();
    public final int y;
    public final int x;
    public Ship ship;
    public Planet planet;
    public Dropship(MenuComponentParkedDropship dropship){
        x = (int) (dropship.x/50);
        y = (int) (dropship.y/50);
        cargo = dropship.dropship.cargo;
        ship = dropship.dropship.ship;
    }
    public Dropship(int x, int y, ArrayList<ItemStack> cargo){
        this.x = x;
        this.y = y;
        this.cargo = cargo;
    }
    public static Dropship load(Config config, Planet parent){
        if(!config.hasProperty("x")){
            return null;
        }
        int x = config.get("x", 3);
        int y = config.get("y", 0);
        ArrayList<ItemStack> cargo = new ArrayList<>();
        Config cfg = config.get("cargo", Config.newConfig());
        for(int i = 0; i<cfg.get("count", 0); i++){
            cargo.add(ItemStack.load(cfg.get(i+"", Config.newConfig())));
        }
        Ship ship = Ship.load(config.get("ship", Config.newConfig()), null);
        Dropship drop = new Dropship(x,y,cargo);
        drop.ship = ship;
        drop.planet = parent;
        return drop;
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("x", x);
        config.set("y", y);
        Config cfg = Config.newConfig();
        cfg.set("count", cargo.size());
        for(int i = 0; i<cargo.size(); i++){
            cfg.set(i+"", cargo.get(i).save());
        }
        config.set("cargo", cfg);
        config.set("ship", ship.save());
        return config;
    }
}