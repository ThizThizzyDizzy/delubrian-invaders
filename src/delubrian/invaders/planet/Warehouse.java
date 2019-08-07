package delubrian.invaders.planet;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentWarehouse;
import java.io.Serializable;
public class Warehouse extends Building implements Serializable{
    private static final long serialVersionUID = 191034L;
    public Warehouse(String name, int x, int y, int length, int width, int height){
        super(BuildingType.WAREHOUSE, x, y, width, length, height);
        this.name = name;
    }
    @Override
    public int cargoSpace(){
        int space = (width*length*height)*500;
        for(ItemStack stack : cargo){
            space -= stack.amount;
        }
        return space;
    }
    @Override
    public MenuComponentBuilding getComponent(Galaxy galaxy, Building building){
        return new MenuComponentWarehouse(galaxy, (Warehouse)building);
    }
    @Override
    public Building copy(int x, int y){
        return new Warehouse(name, x, y, length, width, height);
    }
    @Override
    public Building copy(int x, int y, int length, int width, int height){
        return new Warehouse(name, x, y, length, width, height);
    }
}