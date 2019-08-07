package delubrian.invaders.menu.component;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.BlockType;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentShipStats extends MenuComponent{
    private final Ship ship;
    private int textY = 0;
    public Block block;
    private final boolean battle;
    private static final int textHeight = 10;
    private ArrayList<MenuComponentButton> buttons = new ArrayList<>();
    public MenuComponentShipStats(Ship ship, double width, boolean battle){
        super(Display.getWidth()-width, 0, width, Display.getHeight());
        this.ship = ship;
        this.battle = battle;
    }
    @Override
    public void mouseEvent(int button, boolean pressed, float x, float y, float xChange, float yChange, int wheelChange){
        if(button==0&&pressed){
            for(MenuComponentButton b : buttons){
                if(isClickWithinBounds(x, y, b.x, b.y, b.x+b.width, b.y+b.height)){
                    Item item = Item.getItem(b.label.split("-")[0]);
                    if(b.label.contains("All")){
                        ship.ejectAllCargo(item);
                    }else{
                        ship.ejectCargo(new ItemStack(item, Integer.parseInt(b.label.split("-")[1])));
                    }
                }
            }
        }
    }
    @Override
    public void render(){
        FontManager.setFont("small");
        removeRenderBound();
        buttons.clear();
        textY = 0;
        GL11.glColor4d(0, 0, 0.25, 0.5);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(0.875, 0.875, 0.875, 1);
        if(battle){
            int hull = 0;
            int maxHull = 0;
            double dps = 0;
            double mana = 0;
            double manaStorage = 0;
            double fuel = 0;
            double fuelStorage = 0;
            int cargoStorage = 0;
            for(Block block : ship.getBlocks()){
                if(block==null){
                    continue;
                }
                hull+=block.health;
                maxHull += block.type.health[block.level];
                dps += block.damage/block.fireRate;
                mana += block.manaStorage;
                manaStorage += block.type.manaStorage[block.level];
                fuel += block.fuelStorage;
                fuelStorage += block.type.fuelStorage[block.level];
                cargoStorage += block.type.cargo[block.level];
            }
            dps = Math.round(dps*10)/10D;
            text("Hull: "+hull+"/"+maxHull);
            divider();
            if(dps>0){
                text("DPS: "+dps);
                divider();
            }
            text("Mana: "+Math.round(mana)+"/"+Math.round(manaStorage));
            text("Fuel: "+Math.round(fuel)+"/"+Math.round(fuelStorage));
            divider();
            text("Cargo:");
            for(ItemStack stack : ship.getCargo()){
                cargoText(stack);
            }
            divider();
            text(ship.getCargoAmount()+" Cargo");
            text(cargoStorage+" Space");
            text(Math.round((ship.getCargoAmount()/(double)cargoStorage)*10000)/100D+"% Full");
            GL11.glColor4d(1, 1, 1, 1);
            FontManager.setFont("font");
            return;
        }
        if(block!=null){
            text(block.type.name+" "+(block.level+1));
            text("Hull: "+block.health+"/"+block.type.health[block.level]);
            text("Weight: "+block.weight+" tons");
            if(block.fireRate>0){
                text("Firing delay: "+block.fireRate+" seconds");
                text("Damage: "+block.damage);
                text("DPS: "+block.damage/block.fireRate);
                text("Ammo Consumption: "+block.ammoConsumption+"/sec");
            }
            if(block.manaConsumption>0){
                if(block.fireRate>0){
                    text("Mana Consumption: "+block.manaConsumption/block.fireRate+"/sec");
                }else{
                    text("Mana Consumption: "+block.manaConsumption+"/sec");
                }
            }
            if(block.manaStorage>0){
                text("Mana Storage: "+block.manaStorage+"/"+block.type.manaStorage[block.level]);
            }
            if(block.manaGeneration>0){
                text("Mana Generation: "+block.manaGeneration+"/sec");
            }
            if(block.manaCollection>0){
                text("Max Mana Collection: "+block.manaCollection+"/sec");
            }
            if(block.fuelStorage>0){
                text("Fuel Storage: "+block.fuelStorage);
            }
            if(block.fuelUsage>0){
                text("Fuel Usage: "+block.fuelUsage+"/sec");
            }
            if(block.thrust>0){
                text("Thrust: "+block.thrust);
            }
            if(block.cargoCapacity>0){
                text("Cargo: "+block.cargoCapacity);
            }
            text(block.type.description[block.level]);
        }else{
            if(ship==null){
                text("This hangar is empty.");
            }else{
                text("This ship "+(ship.canLaunch()?"can":"cannot")+" launch");
                String core = "";
                int cores = 0;
                for(Block block : ship.getBlocks(BlockType.CORE)){
                    if(core.isEmpty()){
                        core += (block.level+1);
                    }else{
                        core+=", "+(block.level+1);
                    }
                    cores++;
                }
                text("Cores: "+cores+ "("+core+")", ship.redCores);
                int hull = 0;
                int maxHull = 0;
                double weight = 0;
                double dps = 0;
                double ammoConsumption = 0;
                double manaConsumption = 0;
                double manaStorage = 0;
                double maxManaStorage = 0;
                double manaGeneration = 0;
                double manaCollection = 0;
                double fuelStorage = 0;
                double fuelUsage = 0;
                double thrust = 0;
                int cargo = 0;
                for(Block block : ship.getBlocks()){
                    if(block==null){
                        continue;
                    }
                    hull+=block.health;
                    maxHull += block.type.health[block.level];
                    weight+=block.weight;
                    dps += block.damage/block.fireRate;
                    ammoConsumption += block.ammoConsumption/block.fireRate;
                    if(block.fireRate>0){
                        manaConsumption += block.manaConsumption/block.fireRate;
                    }else{
                        manaConsumption += block.manaConsumption;
                    }
                    manaStorage += block.manaStorage;
                    maxManaStorage += block.type.manaStorage[block.level];
                    manaGeneration += block.manaGeneration;
                    manaCollection += block.manaCollection;
                    fuelStorage += block.fuelStorage;
                    fuelUsage += block.fuelUsage;
                    thrust += block.thrust;
                    cargo += block.cargoCapacity;
                }
                weight = Math.round(weight*100)/100D;
                text("Hull: "+hull+"/"+maxHull);
                text("Weight: "+weight+"t");
                if(dps>0){
                    text("DPS: "+dps);
                    text("Ammo Consumption: "+ammoConsumption+"/sec");
                }else{
                    text("DPS: 0");
                    text("Ammo Consumption: 0/sec");
                }
                text("Mana Consumption: "+manaConsumption+"/sec");
                text("Mana Storage: "+manaStorage+"/"+maxManaStorage, ship.redManaStorage);
                text("Mana Generation: "+manaGeneration+"/sec", ship.redManaGeneration);
                text("Mana Collection: "+manaCollection+"/sec");
                text("Fuel Storage: "+fuelStorage, ship.redFuelStorage);
                text("Fuel Usage: "+fuelUsage+"/sec");
                text("Thrust: "+thrust);
                text("Cargo: "+cargo);
                if(ship.redStructuralIntegrity){
                    text("Invalid Structural Integrity", true);
                }
            }
        }
        GL11.glColor4d(1, 1, 1, 1);
        FontManager.setFont("font");
    }
    private void text(String str){
        text(str, false);
    }
    private void text(String str, boolean red){
        GL11.glColor4d(0.875, red?0:0.875, red?0:0.875, 1);
        while(str!=null&&!str.isEmpty()){
            textY += textHeight+1;
            str = drawTextWithWrap(x+textHeight, textY+textHeight, x+width-16, textY+textHeight*2, str);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    private void divider() {
        textY += textHeight;
        drawRect(x+10, textY+14, x+width-20, textY+16, 0);
    }
    private void cargoText(ItemStack stack){
        text(" "+stack.amount+" "+stack.item.toString());
        button(x+width-32, textY+textHeight, x+width-16, textY+16, stack.item, "All");
        if(stack.amount>1){
            if(stack.amount<10){
                button(x+width-50, textY+textHeight, x+width-33, textY+16, stack.item, "1");
            }else if(stack.amount<100){
                button(x+width-50, textY+textHeight, x+width-33, textY+16, stack.item, "10");
                button(x+width-68, textY+textHeight, x+width-51, textY+16, stack.item, "1");
            }else if(stack.amount<1000){
                button(x+width-50, textY+textHeight, x+width-33, textY+16, stack.item, "100");
                button(x+width-68, textY+textHeight, x+width-51, textY+16, stack.item, "10");
                button(x+width-86, textY+textHeight, x+width-69, textY+16, stack.item, "1");
            }
        }
    }
    private void button(double left, int top, double right, int bottom, Item item, String text) {
        GL11.glColor4d(0.25, 0.25, 0.25, 1);
        drawRect(left-1, top-1, right+1, bottom+1, 0);
        GL11.glColor4d(.875, .875, .875, 1);
        drawText(left, top, right, bottom, text);
        MenuComponentButton button = new MenuComponentButton(left-x, top, right-left, bottom-top, item.getName()+"-"+text, true);
        buttons.add(button);
    }
}