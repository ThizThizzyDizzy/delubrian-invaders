package delubrian.invaders.menu.component;
import delubrian.invaders.menu.MenuEditShip;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.BlockType;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentScrollable;
public class MenuComponentPartPicker extends MenuComponentScrollable{
    public String category;
    private final ArrayList<MenuComponentButton> categories = new ArrayList<>(8);
    private final ArrayList<MenuComponentButton> buttons = new ArrayList<>();
    public final ArrayList<Block> cores = new ArrayList<>();
    private String oldCategory;
    public MenuComponent selected;
    private final MenuComponentButton hangars;
    public MenuComponentPartPicker(double width, double distanceFromBottom){
        super(0, 0, width, Display.getHeight()-distanceFromBottom, 25, 25, false, false);
        categories.add(add(new MenuComponentButton(0, 0, 100, 25, "Cores", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(100, 0, 100, 25, "Armor", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(0, 25, 100, 25, "Lasers", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(100, 25, 100, 25, "Blasters", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(0, 50, 100, 25, "Batteries", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(100, 50, 100, 25, "Generators", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(0, 75, 100, 25, "Collectors", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(100, 75, 100, 25, "Fuel Tanks", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(0, 100, 100, 25, "Thrusters", true, true, "/textures/gui/button")));
        categories.add(add(new MenuComponentButton(100, 100, 100, 25, "Cargo Bays", true, true, "/textures/gui/button")));
        hangars = add(new MenuComponentButton(0, 125, 100, 25, "Hangars", true, true, "/textures/gui/button"));
        categories.add(hangars);
        for(MenuComponentButton button : categories){
            button.textInset = 6.5;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(0, 0, 0.25, 0.5);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(0.75, 0.75, 0.75, 1);
        for(MenuComponent component : components){
            component.render();
        }
        GL11.glColor4d(1,1,1,1);
    }
    @Override
    public void tick(){
        MenuEditShip ship = (MenuEditShip) parent;
        hangars.enabled = !ship.grid.hangar.ship.containsBlock(BlockType.HANGAR);
        if(!hangars.enabled&&hangars.label.equals(category)){
            category = null;
            selected = null;
            category = null;
            updateButtons();
        }
        if(category!=null&&!category.equals(oldCategory)){
            updateButtons();
            components.removeAll(buttons);
            buttons.clear();
            int buttonCount = 0;
            BlockType type = null;
            switch(category){
                case "Cores":
                    type = BlockType.CORE;
                    break;
                case "Armor":
                    type = BlockType.ARMOR;
                    break;
                case "Lasers":
                    type = BlockType.LASER;
                    break;
                case "Blasters":
                    type = BlockType.BLASTER;
                    break;
                case "Batteries":
                    type = BlockType.BATTERY;
                    break;
                case "Generators":
                    type = BlockType.GENERATOR;
                    break;
                case "Collectors":
                    type = BlockType.COLLECTOR;
                    break;
                case "Fuel Tanks":
                    type = BlockType.FUEL_TANK;
                    break;
                case "Thrusters":
                    type = BlockType.THRUSTER;
                    break;
                case "Hangars":
                    type = BlockType.HANGAR;
                    break;
                case "Cargo Bays":
                    type = BlockType.CARGO;
                    break;
                default:
                    throw new AssertionError(category);
            }
            for(int i = 0; i<type.health.length; i++){
                buttons.add(add(new MenuComponentButton(0, buttonCount*25+150, 200, 25, type.name+" "+(i+1), true, true, "/textures/gui/button")));
                buttonCount++;
            }
        }
        oldCategory = category;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        selected = button;
        if(buttons.contains(button)){
            updateButtons();
            MenuEditShip ship = (MenuEditShip) parent;
            ship.grid.selected = null;
        }
        if(categories.contains(button)){
            category = button.label;
        }
    }
    public Block getSelected(){
        if(buttons.contains(selected)){
            MenuComponentButton button = (MenuComponentButton) selected;
            int level = Integer.parseInt(button.label.split(" ")[button.label.split(" ").length-1]);
            BlockType type = null;
            switch(category){
                case "Cores":
                    type = BlockType.CORE;
                    break;
                case "Armor":
                    type = BlockType.ARMOR;
                    break;
                case "Lasers":
                    type = BlockType.LASER;
                    break;
                case "Blasters":
                    type = BlockType.BLASTER;
                    break;
                case "Batteries":
                    type = BlockType.BATTERY;
                    break;
                case "Generators":
                    type = BlockType.GENERATOR;
                    break;
                case "Collectors":
                    type = BlockType.COLLECTOR;
                    break;
                case "Fuel Tanks":
                    type = BlockType.FUEL_TANK;
                    break;
                case "Thrusters":
                    type = BlockType.THRUSTER;
                    break;
                case "Hangars":
                    type = BlockType.HANGAR;
                    break;
                case "Cargo Bays":
                    type = BlockType.CARGO;
                    break;
                default:
                    throw new AssertionError(category);
            }
            return new Block(type, level-1);
        }
        return null;
    }
    public void updateButtons(){
        for(MenuComponentButton b : buttons){
            b.enabled = b!=selected;
        }
        for(MenuComponentButton b : categories){
            b.enabled = !b.label.equals(category);
        }
        if(category==null){
            components.removeAll(buttons);
            buttons.clear();
        }
    }
}