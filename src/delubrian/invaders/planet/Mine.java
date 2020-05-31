package delubrian.invaders.planet;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentMine;
import java.io.Serializable;
import java.util.ArrayList;
public class Mine extends Building implements Serializable{
    private static final long serialVersionUID = 18193L;
    public ArrayList<ItemStack> cargo = new ArrayList<>();
    public Mine(String name, int x, int y, int length, int width){
        super(BuildingType.MINE, x, y, width, length, 1);
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
        return new MenuComponentMine((Mine)building);
    }
    @Override
    public Building copy(int x, int y){
        return new Mine(name, x, y, length, width);
    }
    @Override
    public Building copy(int x, int y, int length, int width, int height){
        return new Mine(name, x, y, length, width);
    }
}