package delubrian.invaders;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
public class Item implements Serializable{
    private static final long serialVersionUID = 2343049015213452345L;
    public static final ArrayList<Item> items = new ArrayList<>();
    public static final Item FUEL = new Item("Fuel");
    public static final Item STONE = new Item("Stone");
    public static final Item IRON = new Item("Iron");
    public static final Item ORE_IRON = new Item("Iron Ore").addProperty(ItemType.ORE, 2, new ItemStack(IRON), 1d);
    public static final Item CHILLTANIUM = new Item("Chilltanium");
    public static final Item ORICHALCUM = new Item("Orichalcum");
    public static final Item ORE_CHILLTANIUM = new Item("Chilltanium Ore").addProperty(ItemType.ORE, 5, new ItemStack(CHILLTANIUM), 5d);
    public static final Item GOLD = new Item("Gold");
    public static final Item ORE_GOLD = new Item("Gold Ore").addProperty(ItemType.ORE, 7, new ItemStack(GOLD), 1d);
    public static final Item STEEL = new Item("Steel");
    //Fuel refining crystals
    /*
    METHOD 1:
    use two crystals, one carbonate mineral and one sulfate mineral. Add stone and refine. (six parts carbonate, four parts sulfate, three parts stone). 
    The carbonate mineral sets the base time for how long the refining will take.
    The sulfate mineral sets the time multiplier for how long the refining will take.
    The sulfate mineral sets the base amount for how much fuel will be created.
    The carbonate mineral sets the amount multiplier for how much fuel will be created.
    WASTE CREATED depends on the chemical formulas of the components.
    */
    //wastes
    public static final Item CALCIUM_RESIDUE = new Item("Calcium Residue");
    public static final Item IRON_RESIDUE = new Item("Iron Residue").addProperty(ItemType.ORE, 10, new ItemStack(IRON), 10d);
    //Carbonates (base time, amount mult)
    public static final Item CALCITE = new Item("Calcite").addProperty(ItemType.CARBONATE, 10d, 1d, new ItemStack(CALCIUM_RESIDUE));//CaCO3 (use for optical lenses)
    public static final Item SIDERITE = new Item("Siderite").addProperty(ItemType.CARBONATE, 15d, 1.1d, new ItemStack(IRON_RESIDUE));//FeCO3
    //Sulfates (base amount, time mult)
    public static final Item ANHYDRITE = new Item("Anhydrite").addProperty(ItemType.SULFATE, 3d, 1d, new ItemStack(CALCIUM_RESIDUE));//CaSO4
    //crystals
    public static final Item AVENTURINE = new Item("Aventurine Crystals");//THRUST
    public static final Item RUBY = new Item("Rubies");//WEAPON
    //crystal ores
    public static final Item ORE_AVENTURINE = new Item("Raw Aventurine Crystals").addProperty(ItemType.ORE, 20, new ItemStack(AVENTURINE), 20d);//THRUST
    public static final Item ORE_RUBY = new Item("Raw Rubies").addProperty(ItemType.ORE, 30, new ItemStack(RUBY), 30d);//WEAPON
    //components
    public static final Item WIRE1 = new Item("Wire 1");
    public static final Item MICROTHRUSTER = new Item("Microthruster");
    public static final Item LASER_BATTERY = new Item("Laser Battery");
    public static final Item BLASTER_BATTERY = new Item("Blaster Battery");
    public static final Item POWER_CELL = new Item("Power Cell");
    public static final Item MOTOR = new Item("Motor");
    //blocks
    public static final Item CORE1 = new Item("Core 1");
    public static final Item CORE2 = new Item("Core 2");
    public static final Item ARMOR1 = new Item("Armor 1");
    public static final Item LASER1 = new Item("Laser 1");
    public static final Item BLASTER1 = new Item("Blaster 1");
    public static final Item BATTERY1 = new Item("Battery 1");
    public static final Item GENERATOR1 = new Item("Generator 1");
    public static final Item COLLECTOR1 = new Item("Collector 1");
    public static final Item FUEL_TANK1 = new Item("Fuel tank 1");
    public static final Item THRUSTER1 = new Item("Thruster 1");
    public static final Item HANGAR1 = new Item("Hangar 1");
    public static final Item CARGO1 = new Item("Cargo Bay 1");
    private final String name;
    private Item(String name){
        this.name = name.toLowerCase();
        items.add(this);
    }
    @Override
    public String toString(){
        return name;
    }
    /**
     * @return the name
     */
    public String getName(){
        return name;
    }
    public static Item getItem(String name) {
        for(Item item : items){
            if(item.name.equalsIgnoreCase(name)){
                return item;
            }
        }
        return null;
    }
    private Item addProperty(ItemType type, Object... values){
        type.items.put(this, values);
        return this;
    }
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Item)){
            return false;
        }
        Item item = (Item) obj;
        return item.getName().equals(name);
    }
    public static enum ItemType{
        CARBONATE,
        SULFATE,
        ORE;
        public HashMap<Item, Object[]> items = new HashMap<>();
    }
}