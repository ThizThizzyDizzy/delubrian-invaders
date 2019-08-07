package delubrian.invaders.planet;
import java.util.Random;
public class AsteroidBelt extends Planet{
    private static final Random randoBelt = new Random();
    public static AsteroidBelt generateAsteroidBelt(StarSystem system, int location){
        return new AsteroidBelt(system.name+"-"+location,
                           system, 
                           randoBelt.nextInt(20)+10, 
                           Mineral.randomMinerals());
    }
    public AsteroidBelt(String name, StarSystem parent, int size, Mineral[] minerals, Hangar... hangars){
        super(name, parent, size, new PlanetaryLayer[0], new AtmosphericLayer[0], minerals, -455, hangars);
    }
}