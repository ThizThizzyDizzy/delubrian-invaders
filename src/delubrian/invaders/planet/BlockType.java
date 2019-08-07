package delubrian.invaders.planet;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.Recipe;
public enum BlockType{
    //<editor-fold defaultstate="collapsed" desc="CORE">
    CORE("Core",
            new int[]    {25,50/*Health*/},
            new double[] {1, 5,/*Weight*/},
            new int[]    {0, 0/*Damage*/},
            new double[] {0, 0/*Fire Rate*/},
            new int[]    {0, 0/*Ammo Consumption*/},
            new double[] {1, 10,/*Mana Consumption*/},
            new double[] {0, 0/*Mana Storage*/},
            new double[] {0, 0/*Mana Generation*/},
            new double[] {0, 0/*Mana Collection*/},
            new double[] {0, 0/*Fuel Storage*/},
            new double[] {0, 0/*Fuel Usage*/},
            new double[] {1, 10/*Thrust*/},
            new int[]    {10,25/*Cargo*/},
            new Item[]   {Item.CORE1,Item.CORE2},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25),
                                              new ItemStack(Item.MICROTHRUSTER, 12),
                                              new ItemStack(Item.WIRE1, 50)}, 
                              new ItemStack[]{new ItemStack(Item.STEEL, 40),
                                              new ItemStack(Item.MICROTHRUSTER, 36),
                                              new ItemStack(Item.WIRE1, 100)}},
            new String[] {"The Core 1 is mostly useless, as it cannot support any other blocks. it's only purpose is as an extra engine",
                            "The Core 2 is capable of supporting any tier 1 blocks directly bordering it, and also acts as an engine"
                            /*Description*/}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ARMOR">
    ARMOR("Armor",
            new int[]   {25/*Health*/},
            new double[]{0.1/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.ARMOR1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25)}},
            new String[]{"Armor protects your ship from incoming fire"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LASER">
    LASER("Laser",
            new int[]   {20/*Health*/},
            new double[]{0.1/*Weight*/},
            new int[]   {1/*Damage*/},
            new double[]{0.1/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{1/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.LASER1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25),
                                              new ItemStack(Item.LASER_BATTERY, 1)}},
            new String[]{"Lasers fire continuous beams, with low damage, but high fire rate"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BLASTER">
    BLASTER("Blaster",
            new int[]   {20/*Health*/},
            new double[]{0.1/*Weight*/},
            new int[]   {10/*Damage*/},
            new double[]{1/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{10/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.BLASTER1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25),
                                              new ItemStack(Item.BLASTER_BATTERY, 1)}},
            new String[]{"Blasters fire bolts of mana, with high damage, but low fire rate"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BATTERY">
    BATTERY("Battery",
            new int[]   {15/*Health*/},
            new double[]{0.25/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{100/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.BATTERY1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 10),
                                              new ItemStack(Item.POWER_CELL, 4)}},
            new String[]{"Batteries hold mana"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GENERATOR">
    GENERATOR("Generator",
            new int[]   {25/*Health*/},
            new double[]{5/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{1/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0.1/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.GENERATOR1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25),
                                              new ItemStack(Item.MOTOR, 10),
                                              new ItemStack(Item.WIRE1, 100)}},
            new String[]{"Generators create mana using fuel"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="COLLECTOR">
    COLLECTOR("Collector",
            new int[]   {20/*Health*/},
            new double[]{0.5/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{5/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.COLLECTOR1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 25),
                                              new ItemStack(Item.WIRE1, 100)}},
            new String[]{"Collectors collect mana from the surroundings"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FUEL TANK">
    FUEL_TANK("Fuel tank",
            new int[]   {10/*Health*/},
            new double[]{10/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{250/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.FUEL_TANK1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 50)}},
            new String[]{"Fuel tanks hold fuel"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="THRUSTER">
    THRUSTER("Thruster",
            new int[]   {20/*Health*/},
            new double[]{1/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{1/*Fuel Usage*/},
            new double[]{25/*Thrust*/},
            new int[]   {0/*Cargo*/},
            new Item[]  {Item.THRUSTER1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 15),
                                              new ItemStack(Item.MOTOR, 2),
                                              new ItemStack(Item.WIRE1, 20),
                                              new ItemStack(Item.RUBY)}},
            new String[]{"Thrusters use fuel to create thrust"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANGAR">
    HANGAR("Hangar",
            new int[]   {25/*Health*/},
            new double[]{5/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {10/*Cargo*/},
            new Item[]  {Item.HANGAR1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 100),
                                              new ItemStack(Item.MOTOR, 4)}},
            new String[]{"Hangars hold dropships"}),
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CARGO">
    CARGO("Cargo Bay",
            new int[]   {25/*Health*/},
            new double[]{10/*Weight*/},
            new int[]   {0/*Damage*/},
            new double[]{0/*Fire Rate*/},
            new int[]   {0/*Ammo Consumption*/},
            new double[]{0/*Mana Consumption*/},
            new double[]{0/*Mana Storage*/},
            new double[]{0/*Mana Generation*/},
            new double[]{0/*Mana Collection*/},
            new double[]{0/*Fuel Storage*/},
            new double[]{0/*Fuel Usage*/},
            new double[]{0/*Thrust*/},
            new int[]   {100/*Cargo*/},
            new Item[]  {Item.CARGO1},
            new ItemStack[][]{new ItemStack[]{new ItemStack(Item.STEEL, 100)}},
            new String[]{"Cargo Bays hold cargo"});
//</editor-fold>
    public final String        name;
    public final int[]         health;
    public final double[]      weight;
    public final int[]         damage;
    public final double[]      fireRate;
    public final int[]         ammoConsumption;
    public final double[]      manaConsumption;
    public final double[]      manaStorage;
    public final double[]      manaGeneration;
    public final double[]      manaCollection;
    public final double[]      fuelStorage;
    public final double[]      fuelUsage;
    public final double[]      thrust;
    public final int[]         cargo;
    public final Item[]        item;
    public final String[]      description;
    public final ItemStack[][] cost;
    BlockType(
            String   name,
            int[]    health,
            double[] weight,
            int[]    damage,
            double[] fireRate,
            int[]    ammoConsumption,
            double[] manaConsumption,
            double[] manaStorage,
            double[] manaGeneration,
            double[] manaCollection,
            double[] fuelStorage,
            double[] fuelUsage,
            double[] thrust,
            int[]    cargo,
            Item[]   item,
            ItemStack[][] cost,
            String[] description){
        this.name = name;
        this.health = health;
        this.weight = weight;
        this.damage = damage;
        this.fireRate = fireRate;
        this.ammoConsumption = ammoConsumption;
        this.manaConsumption = manaConsumption;
        this.manaStorage = manaStorage;
        this.manaGeneration = manaGeneration;
        this.manaCollection = manaCollection;
        this.fuelStorage = fuelStorage;
        this.fuelUsage = fuelUsage;
        this.thrust = thrust;
        this.cargo = cargo;
        this.item = item;
        this.description = description;
        this.cost = cost;
    }
    static BlockType get(String get) {
        for(BlockType type : BlockType.values()){
            if(get.equalsIgnoreCase(type.name)){
                return type;
            }
        }
        return BlockType.ARMOR;
    }
    public double getTime(int level){
        double time = 0;
        ItemStack[] costs = cost[level];
        for(ItemStack s : costs){
            for(Recipe r : Recipe.factoryRecipes){
                if(r.output.item==s.item){
                    int waste = r.output.amount;
                    for(ItemStack w : r.waste)waste+=w.amount;
                    time+=r.ticks*(s.amount+waste)/r.output.amount;
                }
            }
        }
        if(time==0)return costs.length;
        return time/10;
    }
}