    package delubrian.invaders.menu.component;
import delubrian.invaders.Controls;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.MenuSurface;
import delubrian.invaders.planet.Building;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentTruck extends MenuComponent{
    public static final int capacity = 1000;
    private int direction;
    public final ArrayList<ItemStack> cargo;
    public MenuComponentTruck(Building b, ArrayList<ItemStack> selectedCargo) {
        super((b.x+b.width/2)*50, (b.y+b.height)*50, 25, 25);
        this.cargo = selectedCargo;
    }
    public MenuComponentTruck(double x, double y, ArrayList<ItemStack> selectedCargo) {
        super(x, y, 25, 25);
        this.cargo = selectedCargo;
    }
    public int cargoAmount(){
        int i = 0;
        for(ItemStack s : cargo){
            i+=s.amount;
        }
        return i;
    }
    @Override
    public void tick(){
        if(Keyboard.isKeyDown(Controls.unload)){
            MenuSurface surface = (MenuSurface) parent.parent;
            for(MenuComponent component : surface.scroller.components){
                if(component instanceof MenuComponentBuilding&&isClickWithinBounds(x+12.5, y+12.5, component.x, component.y, component.x+component.width, component.y+component.height)){
                    MenuComponentBuilding building = (MenuComponentBuilding) component;
                    if(building.getBuilding().canHoldCargo(cargoAmount())){
                        building.getBuilding().addCargo(cargo);
                    }else{
                        break;
                    }
                    surface.componentsToRemoveFromScroller.add(this);
                    return;
                }
                if(component instanceof MenuComponentParkedDropship&&isClickWithinBounds(x+12.5, y+12.5, component.x, component.y, component.x+component.width, component.y+component.height)){
                    MenuComponentParkedDropship dropship = (MenuComponentParkedDropship) component;
                    if(dropship.canHoldCargo(cargoAmount())){
                        dropship.addCargo(cargo);
                    }else{
                        break;
                    }
                    surface.componentsToRemoveFromScroller.add(this);
                    return;
                }
            }
        }
        if(Keyboard.isKeyDown(Controls.up)){
            if(Keyboard.isKeyDown(Controls.left)){
                direction = 7;
                y-=3;
                x-=3;
            }else if(Keyboard.isKeyDown(Controls.right)){
                direction = 1;
                y-=3;
                x+=3;
            }else{
                direction = 0;
                y-=5;
            }
        }else if(Keyboard.isKeyDown(Controls.down)){
            if(Keyboard.isKeyDown(Controls.left)){
                direction = 5;
                y+=3;
                x-=3;
            }else if(Keyboard.isKeyDown(Controls.right)){
                direction = 3;
                y+=3;
                x+=3;
            }else{
                direction = 4;
                y+=5;
            }
        }else if(Keyboard.isKeyDown(Controls.left)){
            direction = 6;
            x-=5;
        }else if(Keyboard.isKeyDown(Controls.right)){
            direction = 2;
            x+=5;
        }
        if(x<0){
            x=0;
        }
        if(y<0){
            y=0;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        switch(direction){
            case 0:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/up.png"));
                break;
            case 1:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/up-right.png"));
                break;
            case 2:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/right.png"));
                break;
            case 3:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/down-right.png"));
                break;
            case 4:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/down.png"));
                break;
            case 5:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/down-left.png"));
                break;
            case 6:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/left.png"));
                break;
            case 7:
                drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/vehicles/truck/up-left.png"));
                break;
        }
    }
}