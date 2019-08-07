package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.Dropship;
import delubrian.invaders.Item;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentBuilding;
import delubrian.invaders.menu.component.MenuComponentFactory;
import delubrian.invaders.menu.component.MenuComponentHangar;
import delubrian.invaders.menu.component.MenuComponentMine;
import delubrian.invaders.menu.component.MenuComponentParkedDropship;
import delubrian.invaders.menu.component.MenuComponentPlot;
import delubrian.invaders.menu.component.MenuComponentRefinery;
import delubrian.invaders.menu.component.MenuComponentWarehouse;
import delubrian.invaders.planet.Building;
import delubrian.invaders.planet.BuildingType;
import delubrian.invaders.planet.Factory;
import delubrian.invaders.planet.Hangar;
import delubrian.invaders.planet.Mine;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.Refinery;
import delubrian.invaders.planet.Warehouse;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibraryextended.opengl.gui.LayoutMenu;
public class MenuEditSurface extends LayoutMenu{
    private final Galaxy galaxy;
    final Planet planet;
    private final MenuComponentButton done;
    private MenuComponentBuilding selectedBuilding;
    private int delay;
    private ArrayList<MenuComponentBuilding> buildingButtons = new ArrayList<>();
    private MenuComponentResizeBuilding resize;
    private int textHeight = 10;
    public MenuEditSurface(GUI gui, Menu parent, Galaxy galaxy, Planet planet){
        super(gui, parent, 0, 0, 200, 50, 50, false, LayoutMenu.SNAP_LEFT, 25);
        addWithoutScroller(new MenuComponentButton(Display.getWidth()-50, 0, 50, Display.getHeight(), "", true){
            @Override
            public void render(){
                GL11.glColor4d(0.5, 0.5, 0.5, 1);
                drawRect(x, y, x+width, y+height, 0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        });
        scroller.width-=50;
        this.galaxy = galaxy;
        this.planet = planet;
        for(int x = 0; x<planet.size; x++){
            THAT:for(int y = 0; y<planet.size; y++){
                addWithoutLayout(new MenuComponentPlot(planet,x*50,y*50,50,50));
            }
        }
        for(Building building : planet.buildings){
            addWithoutLayout(building.getComponent(galaxy, building));
        }
        done = addWithoutScroller(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Done Editing", true, true, "/textures/gui/button"));
        done.textInset = 15;
        if(planet.dropship!=null){
            MenuComponentParkedDropship ship = new MenuComponentParkedDropship(planet.dropship.x*50, planet.dropship.y*50, planet.dropship);
            addWithoutLayout(ship);
            planet.dropship = new Dropship(ship);
        }
        for (int i = 0; i < BuildingType.values().length; i++) {
            BuildingType type = BuildingType.values()[i];
            switch(type){
                case HANGAR:
                    buildingButtons.add(addWithoutScroller(new MenuComponentHangar(new Hangar("", 0, 0, 1, 1, 1, null))));
                    break;
                case WAREHOUSE:
                    buildingButtons.add(addWithoutScroller(new MenuComponentWarehouse(galaxy, new Warehouse("", 0, 0, 1, 1, 1))));
                    break;
                case FACTORY:
                    buildingButtons.add(addWithoutScroller(new MenuComponentFactory(new Factory("", 0, 0, 1, 1, 1))));
                    break;
                case REFINERY:
                    buildingButtons.add(addWithoutScroller(new MenuComponentRefinery(new Refinery("", 0, 0, 1, 1, 1))));
                    break;
                case MINE:
                    buildingButtons.add(addWithoutScroller(new MenuComponentMine(new Mine("", 0, 0, 1, 1))));
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown building type: "+type);
            }
            buildingButtons.get(i).x = Display.getWidth()-50;
            buildingButtons.get(i).y = i*50;
        }
        resize = addWithoutScroller(new MenuComponentResizeBuilding(galaxy));
    }
    @Override
    public void renderBackground(){}
    @Override
    public void onGUIClosed() {
        for(MenuComponent component : scroller.components){
            if(component instanceof MenuComponentBuilding){
                MenuComponentBuilding b = (MenuComponentBuilding) component;
                b.active = true;
            }
        }
    }
    @Override
    public void onGUIOpened() {
        for(MenuComponent component : scroller.components){
            if(component instanceof MenuComponentBuilding){
                MenuComponentBuilding b = (MenuComponentBuilding) component;
                b.active = false;
            }
        }
        for(MenuComponentBuilding b : buildingButtons){
            b.active = false;
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(resize!=null&&resize.building!=null){
            return;
        }
        if(button==done){
            gui.open(new MenuSurface(gui, parent, galaxy, planet));
        }
        if(selectedBuilding!=null&&canPlace()){
            if(buildingButtons.contains(selectedBuilding)){
                if(!isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), Display.getWidth()-50, 0, Display.getWidth(), Display.getHeight())){
                    removeResources(selectedBuilding.getBuilding());
                    Building building = selectedBuilding.building.copy((int)Math.round(selectedBuilding.x/50), (int)Math.round(selectedBuilding.y/50));
                    addWithoutLayout(building.getComponent(galaxy, building));
                    planet.buildings.add(building);
                    selectedBuilding.x = Display.getWidth()-50+scroller.getHorizScroll();
                    selectedBuilding.y = scroller.getHorizScroll();
                    switch(selectedBuilding.building.type){
                        case WAREHOUSE:
                            selectedBuilding.y += 50;
                            break;
                        case FACTORY:
                            selectedBuilding.y += 100;
                            break;
                        case MINE:
                            selectedBuilding.y += 150;
                            break;
                    }
                    selectedBuilding = null;
                    delay = 2;
                }else{
                    removeResources(selectedBuilding.building);
                    selectedBuilding.building.x = (int) Math.round(selectedBuilding.x/50);
                    selectedBuilding.building.y = (int) Math.round(selectedBuilding.y/50);
                    selectedBuilding = null;
                    delay = 2;
                }
            }else{
                selectedBuilding.building.x = (int) Math.round(selectedBuilding.x/50);
                selectedBuilding.building.y = (int) Math.round(selectedBuilding.y/50);
                selectedBuilding = null;
                delay = 2;
            }
        }
        if(button instanceof MenuComponentBuilding&&selectedBuilding==null&&delay<=0){
            selectedBuilding = (MenuComponentBuilding) button;
        }
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange) {
        if(selectedBuilding==null&&button==1&&resize!=null&&resize.building==null&&pressed){
            synchronized(scroller.components){
                for(MenuComponent component : scroller.components){
                    if(component instanceof MenuComponentBuilding&&isClickWithinBounds(x, y, component.x, component.y, component.x+component.width, component.y+component.height)){
                        if(buildingButtons.contains(component)){
                            continue;
                        }
                        if(component instanceof MenuComponentHangar){
                            MenuComponentHangar h = (MenuComponentHangar) component;
                            if(h.getHangar().hasShip()){
                                return;
                            }
                        }
                        resize.building = (MenuComponentBuilding)component;
                        lockScroll();
                        return;
                    }
                }
            }
        }
        super.mouseEvent(button, pressed, x, y, xChange, yChange, wheelChange);
    }
    @Override
    public void tick(){
        super.tick();
        delay --;
    }
    @Override
    public void render(int millisSinceLastTick){
        if(selectedBuilding==null){
            super.render(millisSinceLastTick);
            FontManager.setFont("small");
            for(MenuComponentBuilding b : buildingButtons){
                if(b.isMouseOver){
                    double x = 0;
                    for(ItemStack stack : b.getBuilding().type.costs){
                        String str = amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                        x = Math.max(x,FontManager.getLengthForStringWithHeight(str, textHeight));
                    }
                    int y = 0;
                    for(ItemStack stack : b.getBuilding().type.costs){
                        GL11.glColor4d(1, 0, 0, 1);
                        if(hasResources(stack)){
                            GL11.glColor4d(0, 1, 0, 1);
                        }
                        String str = amountOfResources(stack.item)+"/"+stack.amount+" "+stack.item.toString();
                        drawText(Mouse.getX()-x, Display.getHeight()-Mouse.getY()+y, Mouse.getX(), Display.getHeight()-Mouse.getY()+y+textHeight, str);
                        y+=textHeight+1;
                    }
                }
            }
            FontManager.setFont("font");
        }else{
            canPlace();
            selectedBuilding.render = false;
            super.render(millisSinceLastTick);
            selectedBuilding.doRender();
            GL11.glColor4d(1, 0, 0, 0.5);
            if(canPlace()){
                GL11.glColor4d(0, 1, 0, 0.5);
            }
            drawRect(selectedBuilding.x, selectedBuilding.y, selectedBuilding.x+selectedBuilding.width, selectedBuilding.y+selectedBuilding.height, 0);
        }
        resize.render();
    }
    public boolean canPlace(){
        if(isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), Display.getWidth()-50, 0, Display.getWidth(), Display.getHeight())&&(buildingButtons.contains(selectedBuilding))){
            selectedBuilding.x = Display.getWidth()-50+scroller.getHorizScroll();
            selectedBuilding.y = scroller.getHorizScroll();
            switch(selectedBuilding.building.type){
                case WAREHOUSE:
                    selectedBuilding.y += 50;
                    break;
                case FACTORY:
                    selectedBuilding.y += 100;
                    break;
                case MINE:
                    selectedBuilding.y += 150;
                    break;
            }
            return true;
        }
        selectedBuilding.x = (Math.round((Mouse.getX()-25+(scroller.getHorizScroll()%50))/50D)-selectedBuilding.building.width/2)*50-scroller.getHorizScroll()%50;
        selectedBuilding.y = (Math.round((Display.getHeight()-Mouse.getY()-25+(scroller.getVertScroll()%50))/50D)-selectedBuilding.building.length/2)*50-scroller.getVertScroll()%50;
        if(selectedBuilding.width%2==0){
            selectedBuilding.x = (Math.round((Mouse.getX()+(scroller.getHorizScroll()%50))/50D)-selectedBuilding.building.width/2)*50-scroller.getHorizScroll()%50;
        }
        if(selectedBuilding.height%2==0){
            selectedBuilding.y = (Math.round((Display.getHeight()-Mouse.getY()+(scroller.getVertScroll()%50))/50D)-selectedBuilding.building.length/2)*50-scroller.getVertScroll()%50;
        }
        if(selectedBuilding.building.width==1){
            selectedBuilding.x = (Math.round((Mouse.getX()-25+(scroller.getHorizScroll()%50))/50D)-1/2)*50-scroller.getHorizScroll()%50;
        }
        if(selectedBuilding.building.length==1){
            selectedBuilding.y = (Math.round((Display.getHeight()-Mouse.getY()-25+(scroller.getVertScroll()%50))/50D)-1/2)*50-scroller.getVertScroll()%50;
        }
        if((buildingButtons.contains(selectedBuilding))&&!hasResources(selectedBuilding.getBuilding())){
            return false;
        }
        int X = (int) (selectedBuilding.x+scroller.getHorizScroll());
        int Y = (int) (selectedBuilding.y+scroller.getVertScroll());
        int width = (int) (selectedBuilding.width);
        int height = (int) (selectedBuilding.height);
        if(X<0||Y<0||X+width>planet.size*50||Y+height>planet.size*50||selectedBuilding.x+width>Display.getWidth()-100){
            return false;
        }
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if((component instanceof MenuComponentBuilding||component instanceof MenuComponentParkedDropship)&&component!=selectedBuilding){
                    for(int x = 0; x<selectedBuilding.building.width; x++){
                        for(int y = 0; y<selectedBuilding.building.length; y++){
                            if(isClickWithinBounds(selectedBuilding.x+x*50+25, selectedBuilding.y+y*50+25, component.x+1, component.y+1, component.x+component.width-1, component.y+component.height-1)){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean hasResources(ItemStack stack){
        for(ItemStack s : getCargo()){
            if(s.item.equals(stack.item)&&s.amount>=stack.amount){
                return true;
            }
        }
        return false;
    }
    public boolean hasResources(Building b){
        for(ItemStack stack : b.type.costs){
            if(!hasResources(new ItemStack(stack.item, stack.amount*(b.length*b.width*b.height)))){
                return false;
            }
        }
        return true;
    }
    public ArrayList<ItemStack> getCargo(){
        ArrayList<ItemStack> cargo = new ArrayList<>();
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if(component instanceof MenuComponentParkedDropship){
                    MenuComponentParkedDropship drop = (MenuComponentParkedDropship) component;
                    for(ItemStack stack : drop.dropship.cargo){
                        FOR:for(int i = 0; i<stack.amount; i++){
                            for(ItemStack s : cargo){
                                if(s.item.equals(stack.item)){
                                    s.amount++;
                                    continue FOR;
                                }
                            }
                            cargo.add(new ItemStack(stack));
                            break;
                        }
                    }
                }
                if(component instanceof MenuComponentWarehouse){
                    MenuComponentWarehouse warehouse = (MenuComponentWarehouse) component;
                    for(ItemStack stack : warehouse.getWarehouse().cargo){
                        FOR:for(int i = 0; i<stack.amount; i++){
                            for(ItemStack s : cargo){
                                if(s.item.equals(stack.item)){
                                    s.amount++;
                                    continue FOR;
                                }
                            }
                            cargo.add(new ItemStack(stack));
                            break;
                        }
                    }
                }
            }
        }
        return cargo;
    }
    public void removeResources(ItemStack stack){
        for(int i = 0; i<stack.amount; i++){
            removeResources(stack.item);
        }
    }
    public void removeResources(Item item){
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if(component instanceof MenuComponentParkedDropship){
                    MenuComponentParkedDropship drop = (MenuComponentParkedDropship) component;
                    for(ItemStack stack : drop.dropship.cargo){
                        if(stack.item.equals(item)){
                            stack.amount--;
                            return;
                        }
                    }
                }
                if(component instanceof MenuComponentWarehouse){
                    MenuComponentWarehouse warehouse = (MenuComponentWarehouse) component;
                    for(ItemStack stack : warehouse.getWarehouse().cargo){
                        if(stack.item.equals(item)){
                            stack.amount--;
                            return;
                        }
                    }
                }
            }
        }
    }
    public void removeResources(Building b){
        for(ItemStack stack : b.type.costs){
            removeResources(new ItemStack(stack.item, stack.amount*(b.length*b.width*b.height)));
        }
    }
    public void addResources(ItemStack stack){
        for(int i = 0; i<stack.amount; i++){
            addResources(stack.item);
        }
    }
    public void addResources(Item item){
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if(component instanceof MenuComponentWarehouse){
                    MenuComponentWarehouse warehouse = (MenuComponentWarehouse) component;
                    if(warehouse.getWarehouse().cargoSpace()>0){
                        warehouse.getWarehouse().addCargo(item);
                        return;
                    }
                }
            }
        }
    }
    public void addResources(Building b){
        for(ItemStack stack : b.type.costs){
            addResources(new ItemStack(stack.item, stack.amount*(b.length*b.width*b.height)));
        }
    }
    public int amountOfResources(Item item){
        int i = 0;
        for(ItemStack s : getCargo()){
            if(s.item.equals(item)){
                i += s.amount;
            }
        }
        return i;
    }
    public int getSpace(){
        int space = 0;
        synchronized(scroller.components){
            for(MenuComponent component : scroller.components){
                if(component instanceof MenuComponentHangar){
                    MenuComponentHangar h = (MenuComponentHangar) component;
                    if(!h.getHangar().hasShip()){
                        continue;
                    }
                    space += h.getHangar().ship.cargoSpace();
                }
                if(component instanceof MenuComponentParkedDropship){
                    MenuComponentParkedDropship drop = (MenuComponentParkedDropship) component;
                    int amount = 0;
                    for(ItemStack stack : drop.dropship.cargo){
                        amount += stack.amount;
                    }
                    space += Dropship.cargoSpace-amount;
                }
                if(component instanceof MenuComponentWarehouse){
                    MenuComponentWarehouse warehouse = (MenuComponentWarehouse) component;
                    space += warehouse.getWarehouse().cargoSpace();
                }
            }
        }
        return space;
    }
    public boolean canHoldResources(Building b){
        int amount = 0;
        for(ItemStack stack : b.type.costs){
            amount += stack.amount*(b.length*b.width*b.height);
        }
        return getSpace()>=amount;
    }
    public boolean canHoldResources(ItemStack stack){
        return getSpace()>=stack.amount;
    }
}