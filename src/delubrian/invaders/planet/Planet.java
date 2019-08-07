package delubrian.invaders.planet;
import delubrian.invaders.Dropship;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class Planet implements Serializable{
    public static Random randoplanet = new Random();
    private static final long serialVersionUID = 184520934750298347L;
    public static Planet generatePlanet(StarSystem system, int location){
        return new Planet(system.name+"-"+location,
                           system,
                           randoplanet.nextInt(50)+50,
                           (location<=5?new PlanetaryLayer[]{
                               PlanetaryLayer.randomInnerCore(),
                               PlanetaryLayer.randomOuterCore(),
                               PlanetaryLayer.MANTLE,
                               PlanetaryLayer.randomCrust()
                           }:new PlanetaryLayer[]{
                               PlanetaryLayer.randomInnerCore()
                           }),
                           location<=5?AtmosphericLayer.randomAtmosphere():AtmosphericLayer.randomGasAtmosphere(),
                           location<=5?Mineral.randomMinerals():new Mineral[0],
                           Math.round(randoplanet.nextDouble()*10000)/10D);
    }
    public static Planet generate(StarSystem system, int location){
        if(randoplanet.nextBoolean()&&randoplanet.nextBoolean()&&randoplanet.nextBoolean()&&randoplanet.nextBoolean()){
            return AsteroidBelt.generateAsteroidBelt(system, location);
        }
        return new Planet(system.name+"-"+location,
                           system,
                           randoplanet.nextInt(50)+50,
                           (location<=5?new PlanetaryLayer[]{
                               PlanetaryLayer.randomInnerCore(),
                               PlanetaryLayer.randomOuterCore(),
                               PlanetaryLayer.MANTLE,
                               PlanetaryLayer.randomCrust()
                           }:new PlanetaryLayer[]{
                               PlanetaryLayer.randomInnerCore()
                           }),
                           location<=5?AtmosphericLayer.randomAtmosphere():AtmosphericLayer.randomGasAtmosphere(),
                           location<=5?Mineral.randomMinerals():new Mineral[0],
                           Math.round(randoplanet.nextDouble()*10000)/10D);
    }
    public StarSystem parent;
    public String texture;
    public String surfaceTexture;
    public String atmosphereTexture;
    public int size;
    public String undergroundTexture;
    public PlanetaryLayer[] layers;
    public AtmosphericLayer[] atmosphericLayers;
    public Mineral[] minerals;
    public double temperature;
    public String name;
    public ArrayList<Building> buildings = new ArrayList<>();
    public Dropship dropship;
    public Planet(String name, StarSystem parent, int size, PlanetaryLayer[] layers, AtmosphericLayer[] atmosphericLayers, Mineral[] minerals, double temperature, Building... buildings) {
        this.name = name;
        this.parent = parent;
        this.texture = "/textures/"+parent.name.toLowerCase()+"/"+name.toLowerCase()+"/planet.png";
        this.surfaceTexture = "/textures/"+parent.name.toLowerCase()+"/"+name.toLowerCase()+"/surface.png";
        this.undergroundTexture = "/textures/"+parent.name.toLowerCase()+"/"+name.toLowerCase()+"/underground.png";
        this.atmosphereTexture = "/textures/"+parent.name.toLowerCase()+"/"+name.toLowerCase()+"/atmosphere.png";
        this.size = size;
        this.layers = layers;
        this.atmosphericLayers = atmosphericLayers;
        this.minerals = minerals;
        this.temperature = temperature;
        this.buildings.addAll(Arrays.asList(buildings));
    } 
}