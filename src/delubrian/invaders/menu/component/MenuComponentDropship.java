package delubrian.invaders.menu.component;
import delubrian.invaders.Dropship;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import java.util.Iterator;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentDropship extends MenuComponent{
    public final Ship ship;
    public ArrayList<ItemStack> cargo;
    public MenuComponentDropship(Ship ship, Planet planet, ArrayList<ItemStack> cargo){
        super(0, 0, 50, 50);
        this.ship = ship;
        this.cargo = cargo;
    }
    public void doRender(){
        drawRect(x,y,x+width,y+height,ImageStash.instance.getTexture("/textures/dropship.png"));
    }
    @Override
    public void render(){
        removeRenderBound();
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
    public int getCargoAmount(){
        int cargoAmount = 0;
        for(ItemStack stack : cargo){
            cargoAmount += stack.amount;
        }
        return cargoAmount;
    }
    public boolean canHoldCargo(int i) {
        return Dropship.cargoSpace-getCargoAmount()
                >=i;
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
    public ArrayList<ItemStack> getCargo(){
        return cargo;
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
}