package delubrian.invaders.planet;
import java.util.ArrayList;
import java.util.Random;
public enum PlanetaryLayer{
    SOLID_IRON_INNER_CORE("Solid Iron Inner Core", "core/inner/iron.png"),
    LIQUID_IRON_OUTER_CORE("Liquid Iron Outer Core", "core/outer/liquid iron.png"),
    MANTLE("Mantle", "mantle.png"),
    HABITABLE_CRUST("Habitable Crust", "/crust/habitable.png"),
    MARS_CRUST("Mars Crust", "/crust/mars.png"),
    WATER_CRUST("Water Crust", "/crust/water.png");
    public static final ArrayList<PlanetaryLayer> innerCores = new ArrayList<>();
    public static final ArrayList<PlanetaryLayer> outerCores = new ArrayList<>();
    public static final ArrayList<PlanetaryLayer> crusts = new ArrayList<>();
    private static final Random randoplanet = new Random();
    static{
        innerCores.add(SOLID_IRON_INNER_CORE);
        outerCores.add(LIQUID_IRON_OUTER_CORE);
        crusts.add(HABITABLE_CRUST);
        crusts.add(MARS_CRUST);
        crusts.add(WATER_CRUST);
    }
    public static PlanetaryLayer randomInnerCore(){
        return innerCores.get(randoplanet.nextInt(innerCores.size()));
    }
    public static PlanetaryLayer randomOuterCore(){
        return outerCores.get(randoplanet.nextInt(outerCores.size()));
    }
    public static PlanetaryLayer randomCrust(){
        return crusts.get(randoplanet.nextInt(crusts.size()));
    }
    public final String name;
    public final String texture;
    PlanetaryLayer(String name, String texture){
        this.name = name;
        this.texture = "/textures/planet/layers/"+texture;
    }
}