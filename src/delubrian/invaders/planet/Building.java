package delubrian.invaders.planet;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
public abstract class Building implements Serializable{
    private static final long serialVersionUID = 134123313443589L;
    public String name;
    public final BuildingType type;
    public int x;
    public int y;
    public int width;
    public int length;
    public int height;
    public ArrayList<ItemStack> cargo = new ArrayList<>();
    public Building(BuildingType type, int x, int y, int width, int length, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        this.height = height;
    }
    public ItemStack[] getCost(){
        ItemStack[] costs = new ItemStack[type.costs.length];
        for(int i = 0; i<type.costs.length; i++){
            costs[i] = new ItemStack(type.costs[i], type.costs[i].amount*length*width*height);
        }
        return costs;
    }
    public void addCargo(Item item){
        for(ItemStack stack : cargo){
            if(stack.item.equals(item)){
                stack.amount++;
                return;
            }
        }
        cargo.add(new ItemStack(item, 1));
    }
    public void addCargo(ItemStack stack){
        for(int i = 0; i<stack.amount; i++){
            addCargo(stack.item);
        }
    }
    public void addCargo(ItemStack[] stacks){
        for(ItemStack stack : stacks){
            addCargo(stack);
        }
    }
    public int getCargoAmount(){
        int cargoAmount = 0;
        for(ItemStack stack : cargo){
            cargoAmount += stack.amount;
        }
        return cargoAmount;
    }
    public boolean canHoldCargo(int i) {
        return cargoSpace()>=i;
    }
    public void addCargo(ArrayList<ItemStack> cargo) {
        for(ItemStack stack : cargo){
            for(int i = 0; i<stack.amount; i++){
                addCargo(stack.item);
            }
        }
    }
    public void removeCargo(ItemStack[] stack){
        for(ItemStack s : stack){
            removeCargo(s);
        }
    }
    public boolean hasCargo(Item item){
        return hasCargo(new ItemStack(item));
    }
    public boolean hasCargo(ItemStack stack){
        for(ItemStack s : cargo){
            if(s.item.equals(stack.item)&&s.amount>=stack.amount){
                return true;
            }
        }
        return false;
    }
    public boolean hasCargo(ItemStack[] c){
        for(ItemStack stack : c){
            if(!hasCargo(new ItemStack(stack.item, stack.amount))){
                return false;
            }
        }
        return true;
    }
    public boolean hasCargo(Iterable<ItemStack> c){
        for(ItemStack stack : c){
            if(!hasCargo(new ItemStack(stack.item, stack.amount))){
                return false;
            }
        }
        return true;
    }
    public ArrayList<ItemStack> getCargo(){
        ArrayList<ItemStack> copy = new ArrayList<>();
        for(ItemStack stack : cargo){
            copy.add(new ItemStack(stack));
        }
        return copy;
    }
    public void removeCargo(ArrayList<ItemStack> cargo){
        for(ItemStack stack : cargo){
            removeCargo(stack);
        }
    }
    public void removeCargo(ItemStack cargo){
        for(int i = 0; i<cargo.amount; i++){
            removeCargo(cargo.item);
        }
    }
    public void removeCargo(Item item){
        for (Iterator<ItemStack> it = cargo.iterator(); it.hasNext();) {
            ItemStack stack = it.next();
            if(stack.item.equals(item)){
                stack.amount--;
                if(stack.amount==0){
                    it.remove();
                }
                return;
            }
        }
    }
    public abstract int cargoSpace();
    public abstract MenuComponentBuilding getComponent(Galaxy galaxy, Building building);
    public abstract Building copy(int x, int y);
    public abstract Building copy(int x, int y, int length, int width, int height);
}
