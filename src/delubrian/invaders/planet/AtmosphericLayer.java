package delubrian.invaders.planet;
import java.util.ArrayList;
import java.util.Random;
public enum AtmosphericLayer{
    EARTH_ATMOSPHERE("Earth Atmosphere", "earth/atmosphere.png", new AtmosphericGas(AtmosphericGasType.NITROGEN, 0.70), new AtmosphericGas(AtmosphericGasType.OXYGEN, 0.27), new AtmosphericGas(AtmosphericGasType.ARGON, 0.009)),
    SHIELGIC_ATMOSPHERE("Shielgic Atmosphere", "shielgic/atmosphere.png", new AtmosphericGas(AtmosphericGasType.NITROGEN, 0.6), new AtmosphericGas(AtmosphericGasType.OXYGEN, 0.35), new AtmosphericGas(AtmosphericGasType.MANA, 0.05)),
    HYDROGEN("Hydrogen Layer", "gas/hydrogen.png", new AtmosphericGas(AtmosphericGasType.HYDROGEN, 1)),
    HELIUM("Helium Layer", "gas/helium.png", new AtmosphericGas(AtmosphericGasType.HELIUM, 1)),
    ARGON("Argon Layer", "gas/argon.png", new AtmosphericGas(AtmosphericGasType.ARGON, 1)),
    OXYGEN("Oxygen Layer", "gas/oxygen.png", new AtmosphericGas(AtmosphericGasType.OXYGEN, 1)),
    NITROGEN("Nitrogen Layer", "gas/nitrogen.png", new AtmosphericGas(AtmosphericGasType.NITROGEN, 1)),
    WATER_VAPOR("Water Vapor Layer", "gas/water vapor.png", new AtmosphericGas(AtmosphericGasType.WATER_VAPOR, 1)),
    CARBON_DIOXIDE("Carbon Dioxide Layer", "gas/carbon dioxide.png", new AtmosphericGas(AtmosphericGasType.CARBON_DIOXIDE, 1)),
    MANA("Mana Layer", "gas/mana.png", new AtmosphericGas(AtmosphericGasType.MANA, 1)),
    DELUBRIA_ATMOSPHERE("Delubria Atmosphere", "delubria/atmosphere", new AtmosphericGas(AtmosphericGasType.NITROGEN, .4), new AtmosphericGas(AtmosphericGasType.OXYGEN, .15), new AtmosphericGas(AtmosphericGasType.MANA, .01));
    public final String name;
    public final String texture;
    public final AtmosphericGas[] gasses;
    AtmosphericLayer(String name, String texture, AtmosphericGas... gasses){
        this.name = name;
        this.texture = "/textures/planet/atmosphere/"+texture;
        this.gasses = gasses;
    }
    public static final ArrayList<AtmosphericLayer> atmospheres = new ArrayList<>();
    public static final ArrayList<AtmosphericLayer> gasGiantLayers = new ArrayList<>();
    private static final Random randolayer = new Random();
    static{
        atmospheres.add(EARTH_ATMOSPHERE);
        atmospheres.add(SHIELGIC_ATMOSPHERE);
        gasGiantLayers.add(HYDROGEN);
        gasGiantLayers.add(HELIUM);
        gasGiantLayers.add(ARGON);
        gasGiantLayers.add(OXYGEN);
        gasGiantLayers.add(NITROGEN);
        gasGiantLayers.add(WATER_VAPOR);
        gasGiantLayers.add(CARBON_DIOXIDE);
        gasGiantLayers.add(MANA);
    }
    public static AtmosphericLayer[] randomAtmosphere(){
        return new AtmosphericLayer[]{
            atmospheres.get(randolayer.nextInt(atmospheres.size()))
        };
    }
    public static AtmosphericLayer[] randomGasAtmosphere(){
        AtmosphericLayer[] layers = new AtmosphericLayer[randolayer.nextInt(5)+5];
        for(int i = 0; i<layers.length; i++){
            while(true){
                layers[i] = gasGiantLayers.get(randolayer.nextInt(gasGiantLayers.size()));
                if(i==0||layers[i]!=layers[i-1]){
                    break;
                }
            }
        }
        return layers;
    }
}