package delubrian.invaders.planet;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
public enum BuildingType{
    HANGAR("Hangar", new ItemStack[]{
        new ItemStack(Item.STEEL, 50)
    }),
    WAREHOUSE("Warehouse", new ItemStack[]{
        new ItemStack(Item.STEEL, 50)
    }),
    FACTORY("Factory", new ItemStack[]{
        new ItemStack(Item.STEEL, 100)
    }),
    REFINERY("Refinery", new ItemStack[]{
        new ItemStack(Item.STEEL, 150)
    }),
    MINE("Mine", new ItemStack[]{
        new ItemStack(Item.STEEL, 10)
    });
    private final String name;
    public final ItemStack[] costs;
    private BuildingType(String name, ItemStack[] costs){
        this.name = name;
        this.costs = costs;
    }
    @Override
    public String toString(){
        return name;
    }
}