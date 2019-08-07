package delubrian.invaders.planet;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentHangar;
import java.io.Serializable;
import java.util.ArrayList;
public class Hangar extends Building implements Serializable{
    private static final long serialVersionUID = 97239456203458621L;
    public Ship ship;
    public Hangar(String name, int x, int y, int length, int width, int height, Ship ship){
        super(BuildingType.HANGAR, x, y, width, length, height);
        this.name = name;
        this.ship = ship;
    }
    public boolean hasShip(){
        if(ship==null||ship.blocks==null){
            return false;
        }
        for(Block[][] blocks : ship.blocks){
            for(Block[] blox : blocks){
                for(Block block : blox){
                    if(block!=null){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean canHold(Ship ship){
        return hasShip()?false:width>=ship.width()&&height>=ship.height()&&length>=ship.length();
    }
    @Override
    public MenuComponentBuilding getComponent(Galaxy galaxy, Building building){
        return new MenuComponentHangar((Hangar)building);
    }
    @Override
    public Building copy(int x, int y){
        return new Hangar(name, x, y, length, width, height, null);
    }
    @Override
    public Building copy(int x, int y, int length, int width, int height){
        return new Hangar(name, x, y, length, width, height, null);
    }
    @Override
    public int cargoSpace(){
        return 500-getCargoAmount();
    }
    @Override
    public ArrayList<ItemStack> getCargo(){
        if(ship==null)return super.getCargo();
        ArrayList<ItemStack> cargo = super.getCargo();
        for(ItemStack stack : ship.getCargo()){
            for(ItemStack stored : cargo){
                if(stored.item==stack.item){
                    stored.amount+=stack.amount;
                    break;
                }
            }
            cargo.add(new ItemStack(stack));
        }
        return cargo;
    }
}