package delubrian.invaders;
import delubrian.invaders.planet.BlockType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
public class Recipe implements Serializable{
    public static final ArrayList<Recipe> factoryRecipes = new ArrayList<>();
    public static final ArrayList<Recipe> refineryRecipes = new ArrayList<>();
    private static final long serialVersionUID = 2345213452345L;
    static{
        //materials
        factoryRecipe(Item.STEEL, .5, false, new ItemStack(Item.IRON, 2));
        //misc.
        factoryRecipe(new ItemStack(Item.WIRE1, 25), 5, false, new ItemStack(Item.CHILLTANIUM), new ItemStack(Item.ORICHALCUM, 2));
        factoryRecipe(new ItemStack(Item.MICROTHRUSTER, 6), 1, true, new ItemStack(Item.WIRE1, 12), new ItemStack(Item.AVENTURINE));
        factoryRecipe(Item.MOTOR, 10, true, new ItemStack(Item.MICROTHRUSTER, 4), new ItemStack(Item.STEEL, 10), new ItemStack(Item.WIRE1, 10));
        factoryRecipe(Item.LASER_BATTERY, 30, true, new ItemStack(Item.STEEL, 5), new ItemStack(Item.WIRE1, 10), new ItemStack(Item.RUBY));
        factoryRecipe(Item.BLASTER_BATTERY, 30, true, new ItemStack(Item.STEEL, 5), new ItemStack(Item.WIRE1, 25), new ItemStack(Item.RUBY));
        factoryRecipe(Item.POWER_CELL, 30, true, new ItemStack(Item.STEEL, 10), new ItemStack(Item.GOLD, 10), new ItemStack(Item.IRON, 5));
        //blocks
        for(BlockType t : BlockType.values()){
            for(int i = 0; i<t.item.length; i++){
                factoryRecipe(t.item[i], t.getTime(i), true, t.cost[i]);
            }
        }
        for(Item ore : Item.ItemType.ORE.items.keySet()){
            Object[] r = Item.ItemType.ORE.items.get(ore);
            int amount = (int) r[0];
            ItemStack result = (ItemStack) r[1];
            double time = (double) r[2];
            refineryRecipe(new ItemStack(result), time, false, new ItemStack(ore, amount));
        }
        //Carbonate and Sulfate Refinery recipes
        //Carbonates (base time, amount mult)
        for(Item carbonate : Item.ItemType.CARBONATE.items.keySet()){
            for(Item sulfate : Item.ItemType.SULFATE.items.keySet()){
                Object[] c = Item.ItemType.CARBONATE.items.get(carbonate);
                Object[] s = Item.ItemType.SULFATE.items.get(sulfate);
                double time = (double)c[0]*(double)s[1];
                double amount = (double)s[0]*(double)c[1];
                ArrayList<ItemStack> waste = new ArrayList<>();
                for(int i = 2; i<c.length; i++){
                    ItemStack n = (ItemStack)c[i];
                    DO:do{
                        for(ItemStack stack : waste){
                            if(stack.item.equals(n.item)){
                                stack.amount+=n.amount;
                                break DO;
                            }
                        }
                        waste.add(new ItemStack(n));
                    }while(false);
                }
                for(int i = 2; i<s.length; i++){
                    ItemStack n = (ItemStack)c[i];
                    DO:do{
                        for(ItemStack stack : waste){
                            if(stack.item.equals(n.item)){
                                stack.amount+=n.amount;
                                break DO;
                            }
                        }
                        waste.add(new ItemStack(n));
                    }while(false);
                }
                //(six parts carbonate, four parts sulfate, three parts stone). 
                refineryRecipe(new ItemStack(Item.FUEL, (int)amount), time, false, new ItemStack(carbonate, 6), new ItemStack(sulfate, 4), new ItemStack(Item.STONE, 3));
            }
        }
    }
    public final ItemStack[] input;
    public final ItemStack output;
    public final ItemStack[] waste;
    public final int ticks;
    public final boolean reversible;
    public Recipe(ItemStack[] input, ItemStack output, int ticks, boolean reversible, ItemStack... waste){
        this.input = input;
        this.output = output;
        this.ticks = ticks;
        this.reversible = reversible;
        this.waste = waste;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Recipe){
            Recipe r = (Recipe) obj;
            return Arrays.equals(input, r.input)&&Arrays.equals(waste, r.waste)&&output.equals(r.output);
        }
        return false;
    }
    public static void factoryRecipe(ItemStack output, double seconds, boolean reversible, ItemStack... inputs){
        factoryRecipes.add(new Recipe(inputs, output, (int) (seconds*20), reversible));
    }
    public static void factoryRecipe(Item output, double seconds, boolean reversible, ItemStack... inputs){
        factoryRecipe(new ItemStack(output), seconds, reversible, inputs);
    }
    public static void refineryRecipe(ItemStack output, double seconds, boolean reversible, ItemStack... inputs){
        refineryRecipes.add(new Recipe(inputs, output, (int) (seconds*20), reversible));
    }
    public static void refineryRecipe(Item output, double seconds, boolean reversible, ItemStack... inputs){
        refineryRecipe(new ItemStack(output), seconds, reversible, inputs);
    }
    public static void refineryRecipe(Item output, double seconds, ItemStack waste, ItemStack... inputs){
        refineryRecipe(new ItemStack(output), seconds, waste, inputs);
    }
    public static void refineryRecipe(ItemStack output, double seconds, ItemStack waste, ItemStack... inputs){
        refineryRecipes.add(new Recipe(inputs, output, (int) (seconds*20), false, waste));
    }
}