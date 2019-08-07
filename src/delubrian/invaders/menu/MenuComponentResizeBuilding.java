package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentWarehouse;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.BuildingType;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.planet.Warehouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MenuComponentResizeBuilding extends MenuComponent{
    public MenuComponentBuilding building = null;
    private final Galaxy galaxy;
    public MenuComponentResizeBuilding(Galaxy galaxy){
        super(0, 0, 0, 0);
        this.galaxy = galaxy;
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange) {
        if(building==null){
            return;
        }
        MenuEditSurface surface = (MenuEditSurface) parent;
        Building b = building.getBuilding();
        do{
            if(pressed){
                if(button==0){
                    if(isClickWithinBounds(x, y, building.x, building.y-50, building.x+building.width, building.y)&&canResize(building, "North")){
                        b.length++;
                        b.y--;
                        building.y-=50;
                        building.height+=50;
                        surface.removeResources(b.copy(b.x, b.y-1, 1, b.width, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x, building.y+building.height, building.x+building.width, building.y+building.height+50)&&canResize(building, "South")){
                        b.length++;
                        building.height+=50;
                        surface.removeResources(b.copy(b.x, b.y+1, 1, b.width, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x+building.width, building.y, building.x+building.width+50, building.y+building.height)&&canResize(building, "East")){
                        b.width++;
                        building.width+=50;
                        surface.removeResources(b.copy(b.x+1, b.y, b.length, 1, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x-50, building.y, building.x, building.y+building.height)&&canResize(building, "West")){
                        b.width++;
                        b.x--;
                        building.x-=50;
                        building.width+=50;
                        surface.removeResources(b.copy(b.x-1, b.y, b.length, 1, b.height));
                        break;
                    }
                    if(building.getBuilding().type!=BuildingType.MINE&&isClickWithinBounds(x, y, building.x, building.y, building.x+building.width, building.y+building.height)&&canResize(building, "Up")){
                        b.height++;
                        surface.removeResources(b.copy(b.x, b.y, b.length, b.width, 1));
                        break;
                    }
                }else if(button==1){
                    if(isClickWithinBounds(x, y, building.x, building.y-50, building.x+building.width, building.y)&&canResize(building, "-North")){
                        b.length--;
                        b.y++;
                        building.y+=50;
                        building.height-=50;
                        surface.addResources(b.copy(b.x, b.y-1, 1, b.width, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x, building.y+building.height, building.x+building.width, building.y+building.height+50)&&canResize(building, "-South")){
                        b.length--;
                        building.height-=50;
                        surface.addResources(b.copy(b.x, b.y+1, 1, b.width, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x+building.width, building.y, building.x+building.width+50, building.y+building.height)&&canResize(building, "-East")){
                        b.width--;
                        building.width-=50;
                        surface.addResources(b.copy(b.x+1, b.y, b.length, 1, b.height));
                        break;
                    }
                    if(isClickWithinBounds(x, y, building.x-50, building.y, building.x, building.y+building.height)&&canResize(building, "-West")){
                        b.width--;
                        b.x++;
                        building.x+=50;
                        building.width-=50;
                        surface.addResources(b.copy(b.x-1, b.y, b.length, 1, b.height));
                        break;
                    }
                    if(building.getBuilding().type!=BuildingType.MINE&&isClickWithinBounds(x, y, building.x, building.y, building.x+building.width, building.y+building.height)&&canResize(building, "-Up")){
                        b.height--;
                        surface.addResources(b.copy(b.x, b.y, b.length, b.width, 1));
                        break;
                    }
                }
            }
        }while(false);
        if(b.height<1||b.width<1||b.length<1){
            surface.planet.buildings.remove(b);
            surface.componentsToRemoveFromScroller.add(building);
            building = null;
            surface.unlockScroll();
        }
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Keyboard.KEY_ESCAPE&&pressed){
            building = null;
            MenuEditSurface surface = (MenuEditSurface) parent;
            surface.unlockScroll();
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        width = building==null?0:Display.getWidth();
        height = building==null?0:Display.getHeight();
        GL11.glColor4d(.5, .5, .5, .5);
        drawRect(x, y, x+width, y+height, 0);
        if(building==null){
            GL11.glColor4d(1, 1, 1, 1);
            return;
        }
        GL11.glColor4d(1, 0, 0, .25);
        if(canResize(building, "North")){
            GL11.glColor4d(0, 1, 0, .25);
        }
        drawRect(building.x, building.y-50, building.x+building.width, building.y, 0);
        GL11.glColor4d(1, 0, 0, .25);
        if(canResize(building, "South")){
            GL11.glColor4d(0, 1, 0, .25);
        }
        drawRect(building.x, building.y+building.height, building.x+building.width, building.y+building.height+50, 0);
        GL11.glColor4d(1, 0, 0, .25);
        if(canResize(building, "East")){
            GL11.glColor4d(0, 1, 0, .25);
        }
        drawRect(building.x+building.width, building.y, building.x+building.width+50, building.y+building.height, 0);
        GL11.glColor4d(1, 0, 0, .25);
        if(canResize(building, "West")){
            GL11.glColor4d(0, 1, 0, .25);
        }
        drawRect(building.x-50, building.y, building.x, building.y+building.height, 0);
        if(building.getBuilding().type!=BuildingType.MINE){
            GL11.glColor4d(1, 0, 0, .25);
            if(canResize(building, "Up")){
                GL11.glColor4d(0, 1, 0, .25);
            }
            drawRect(building.x, building.y, building.x+building.width, building.y+building.height, 0);
        }
        GL11.glColor4d(1, 1, 1, 1);
        MenuEditSurface surface = (MenuEditSurface) parent;
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), building.x, building.y-50, building.x+building.width, building.y)){
            Building b = building.getBuilding();
            b = b.copy(b.x, b.y-1, 1, b.width, b.height);
            double x = 0;
            for(ItemStack stack : b.getCost()){
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                x = Math.max(x,FontManager.getLengthForStringWithHeight(str, 8));
            }
            int y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.hasResources(stack)){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
            int amount = 0;
            y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.canHoldResources(new ItemStack(stack, stack.amount+amount))){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                amount += stack.amount;
                String str = surface.getSpace()+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()+10, Display.getHeight()-Mouse.getY()+y, Mouse.getX()+Display.getWidth(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
        }
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), building.x, building.y+building.height, building.x+building.width, building.y+building.height+50)){
            Building b = building.getBuilding();
            b = b.copy(b.x, b.y+1, 1, b.width, b.height);
            double x = 0;
            for(ItemStack stack : b.getCost()){
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                x = Math.max(x,FontManager.getLengthForStringWithHeight(str, 8));
            }
            int y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.hasResources(stack)){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
            int amount = 0;
            y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.canHoldResources(new ItemStack(stack, stack.amount+amount))){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                amount += stack.amount;
                String str = surface.getSpace()+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()+10, Display.getHeight()-Mouse.getY()+y, Mouse.getX()+Display.getWidth(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
        }
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), building.x+building.width, building.y, building.x+building.width+50, building.y+building.height)){
            Building b = building.getBuilding();
            b = b.copy(b.x+1, b.y, b.length, 1, b.height);
            double x = 0;
            for(ItemStack stack : b.getCost()){
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                x = Math.max(x,FontManager.getLengthForStringWithHeight(str, 8));
            }
            int y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.hasResources(stack)){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
            int amount = 0;
            y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.canHoldResources(new ItemStack(stack, stack.amount+amount))){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                amount += stack.amount;
                String str = surface.getSpace()+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()+10, Display.getHeight()-Mouse.getY()+y, Mouse.getX()+Display.getWidth(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
        }
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), building.x-50, building.y, building.x, building.y+building.height)){
            Building b = building.getBuilding();
            b = b.copy(b.x-1, b.y, b.length, 1, b.height);
            double x = 0;
            for(ItemStack stack : b.getCost()){
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                x = Math.max(x,FontManager.getLengthForStringWithHeight(str, 8));
            }
            int y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.hasResources(stack)){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
            int amount = 0;
            y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.canHoldResources(new ItemStack(stack, stack.amount+amount))){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                amount += stack.amount;
                String str = surface.getSpace()+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()+10, Display.getHeight()-Mouse.getY()+y, Mouse.getX()+Display.getWidth(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
        }
        if(building.getBuilding().type!=BuildingType.MINE&&isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), building.x, building.y, building.x+building.width, building.y+building.height)){
             Building b = building.getBuilding();
            b = b.copy(b.x, b.y, b.length, b.width, 1);
            double x = 0;
            for(ItemStack stack : b.getCost()){
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                x = Math.max(x,FontManager.getLengthForStringWithHeight(str, 8));
            }
            int y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.hasResources(stack)){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                String str = surface.amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
            int amount = 0;
            y = 0;
            for(ItemStack stack : b.getCost()){
                GL11.glColor4d(1, 0, 0, 1);
                if(surface.canHoldResources(new ItemStack(stack, stack.amount+amount))){
                    GL11.glColor4d(0, 1, 0, 1);
                }
                amount += stack.amount;
                String str = surface.getSpace()+"/"+stack.amount+" "+stack.item.toString();
                drawText(Mouse.getX()+10, Display.getHeight()-Mouse.getY()+y, Mouse.getX()+Display.getWidth(), Display.getHeight()-Mouse.getY()+y+8, str);
                y+=9;
            }
        }
    }
    private boolean canResize(MenuComponentBuilding building, String direction){
        Building b = building.getBuilding();
        MenuEditSurface surface = (MenuEditSurface) parent;
        if(building instanceof MenuComponentWarehouse&&!canWarehouseResize((MenuComponentWarehouse) building, direction)){
            return false;
        }
        switch(direction){
            case "-North":
                return surface.canHoldResources(b.copy(b.x, b.y-1, 1, b.width, b.height));
            case "-South":
                return surface.canHoldResources(b.copy(b.x, b.y+1, 1, b.width, b.height));
            case "-East":
                return surface.canHoldResources(b.copy(b.x+1, b.y, b.length, 1, b.height));
            case "-West":
                return surface.canHoldResources(b.copy(b.x-1, b.y, b.length, 1, b.height));
            case "-Up":
                return surface.canHoldResources(b.copy(b.x, b.y, b.length, b.width, 1));
            case "North":
                if(canPlace(b.getComponent(galaxy, b.copy(b.x, b.y-1, 1, b.width, b.height)))&&surface.hasResources(b.copy(b.x, b.y-1, 1, b.width, b.height))){
                    return true;
                }
                break;
            case "South":
                if(canPlace(b.getComponent(galaxy, b.copy(b.x, b.y+b.height, 1, b.width, b.height)))&&surface.hasResources(b.copy(b.x, b.y+1, 1, b.width, b.height))){
                    return true;
                }
                break;
            case "East":
                if(canPlace(b.getComponent(galaxy, b.copy(b.x+b.width, b.y, b.length, 1, b.height)))&&surface.hasResources(b.copy(b.x+1, b.y, b.length, 1, b.height))){
                    return true;
                }
                break;
            case "West":
                if(canPlace(b.getComponent(galaxy, b.copy(b.x-1, b.y, b.length, 1, b.height)))&&surface.hasResources(b.copy(b.x-1, b.y, b.length, 1, b.height))){
                    return true;
                }
                break;
            case "Up":
                return surface.hasResources(b.copy(b.x, b.y, b.length, b.width, 1));
        }
        return false;
    }
    private boolean canWarehouseResize(MenuComponentWarehouse warehouse, String direction){
        Warehouse w = warehouse.getWarehouse();
        int cargo;
        Warehouse ware;
        switch(direction){
            case "-North":
                cargo = w.getCargoAmount();
                ware = (Warehouse) w.copy(w.x, w.y-1, 1, w.width, w.height);
                return cargo>=ware.cargoSpace();
            case "-South":
                cargo = w.getCargoAmount();
                ware = (Warehouse) w.copy(w.x, w.y+1, 1, w.width, w.height);
                return cargo>=ware.cargoSpace();
            case "-East":
                cargo = w.getCargoAmount(); 
                ware = (Warehouse) w.copy(w.x+1, w.y, w.length, 1, w.height);
                return cargo>=ware.cargoSpace();
            case "-West":
                cargo = w.getCargoAmount();  
                ware = (Warehouse) w.copy(w.x-1, w.y, w.length, 1, w.height);
                return cargo>=ware.cargoSpace();
            case "-Up":
                cargo = w.getCargoAmount();
                ware = (Warehouse) w.copy(w.x, w.y, w.length, w.width, 1);
                return cargo>=ware.cargoSpace();
        }
        return true;
    }
    public boolean canPlace(MenuComponentBuilding building){
        MenuEditSurface surface = (MenuEditSurface) parent;
        for(MenuComponent component : surface.scroller.components){
            if(component instanceof MenuComponentBuilding&&component!=building){
                for(int X = 0; X<building.building.width; X++){
                    for(int Y = 0; Y<building.building.length; Y++){
                        if(isClickWithinBounds(building.x+X*50+25, building.y+Y*50+25, component.x+1, component.y+1, component.x+component.width-1, component.y+component.height-1)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}