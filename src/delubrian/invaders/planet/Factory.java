package delubrian.invaders.planet;
import delubrian.invaders.ItemStack;
import delubrian.invaders.Job;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentFactory;
import java.io.Serializable;
import java.util.ArrayList;
public class Factory extends Building implements Serializable{
    private static final long serialVersionUID = 189183L;
    public ArrayList<ItemStack> cargo = new ArrayList<>();
    public ArrayList<Job> jobs = new ArrayList<>();
    public Factory(String name, int x, int y, int length, int width, int height){
        super(BuildingType.FACTORY, x, y, width, length, height);
        this.name = name;
    }
    @Override
    public MenuComponentBuilding getComponent(Galaxy galaxy, Building building){
        return new MenuComponentFactory((Factory)building);
    }
    @Override
    public Building copy(int x, int y){
        return new Factory(name, x, y, length, width, height);
    }
    @Override
    public Building copy(int x, int y, int length, int width, int height){
        return new Factory(name, x, y, length, width, height);
    }
    @Override
    public int cargoSpace(){
        int space = (width*length*height)*500;
        for(ItemStack stack : cargo){
            space -= stack.amount;
        }
        return space;
    }
}