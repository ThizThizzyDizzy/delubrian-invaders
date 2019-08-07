package delubrian.invaders.planet;
import java.io.Serializable;
import java.util.ArrayList;
public class Galaxy implements Serializable{
    private static final long serialVersionUID = 10923847509123L;
    public ArrayList<StarSystem> starSystems = new ArrayList<>();
    public StarSystem system; //The system the game was last saved at
    public Planet planet; //The planet the game was last saved at
    public String name;
    public Galaxy(String name){
        generate();
        this.name = name;
    }
    private void generate(){
        StarSystem system;
        starSystems.add(system = new StarSystem(this, "Shaldon", 3));
        starSystems.get(0).planets = new Planet[]{
            Planet.generatePlanet(system, 1),
            AsteroidBelt.generateAsteroidBelt(system, 2),
            new Planet("Shielgic",
                system, 
                100, 
                new PlanetaryLayer[]{
                    PlanetaryLayer.SOLID_IRON_INNER_CORE, 
                    PlanetaryLayer.LIQUID_IRON_OUTER_CORE, 
                    PlanetaryLayer.MANTLE, 
                    PlanetaryLayer.HABITABLE_CRUST
                }, 
                new AtmosphericLayer[]{
                    AtmosphericLayer.SHIELGIC_ATMOSPHERE
                }, new Mineral[]{
                    new Mineral(MineralType.CHILLTANIUM, 0.04),
                    new Mineral(MineralType.AVENTURINE, 0.002),
                    new Mineral(MineralType.RUBY, 0.0015),
                    new Mineral(MineralType.CALCITE, 0.01),
                    new Mineral(MineralType.SIDERITE, 0.004),
                    new Mineral(MineralType.ANHYDRITE, 0.002),
                    new Mineral(MineralType.GOLD, 0.001),
                    new Mineral(MineralType.IRON, 0.2),
                    new Mineral(MineralType.STONE, 1),
                    new Mineral(MineralType.ORICHALCUM, .07)
                }, 58.3, 
                new Hangar("Hangar", 0, 0, 3, 3, 3, defaultShip()),
                new Factory("Factory", 3, 2, 2, 2, 1),
                new Warehouse("Warehouse", 3, 0, 2, 2, 1)),
            AsteroidBelt.generateAsteroidBelt(system, 4),
            new Planet("Delubria",
                system,
                120,
                new PlanetaryLayer[]{
                    PlanetaryLayer.SOLID_IRON_INNER_CORE,
                    PlanetaryLayer.LIQUID_IRON_OUTER_CORE,
                    PlanetaryLayer.MANTLE,
                    PlanetaryLayer.MARS_CRUST
                },
                new AtmosphericLayer[]{
                    AtmosphericLayer.DELUBRIA_ATMOSPHERE
                },
                new Mineral[]{
                    
                }, 63.8
            ),
            Planet.generatePlanet(system, 6),
            Planet.generatePlanet(system, 7)};
    }
    private static Ship defaultShip(){
        return new Ship("Ship", new Block[][][]{
            new Block[][]{
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.CARGO, 0),
                    new Block(BlockType.BATTERY, 0),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.COLLECTOR, 0),
                    new Block(BlockType.ARMOR, 0)
                }
            },
            new Block[][]{
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.LASER, 0),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.HANGAR, 0),
                    new Block(BlockType.CORE, 1),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.COLLECTOR, 0),
                    new Block(BlockType.ARMOR, 0)
                }
            },
            new Block[][]{
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.CARGO, 0),
                    new Block(BlockType.BATTERY, 0),
                    new Block(BlockType.ARMOR, 0)
                },
                new Block[]{
                    new Block(BlockType.ARMOR, 0),
                    new Block(BlockType.COLLECTOR, 0),
                    new Block(BlockType.ARMOR, 0)
                }
            }
        });
    }
    public StarSystem getStarSystem(String name) {
        for(StarSystem system : starSystems){
            if(system.name.equalsIgnoreCase(name)){
                return system;
            }
        }
        return null;
    }
}