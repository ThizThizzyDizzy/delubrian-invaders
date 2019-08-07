package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.component.MenuComponentPartPicker;
import delubrian.invaders.menu.component.MenuComponentShipGrid;
import delubrian.invaders.menu.component.MenuComponentShipStats;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.Hangar;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuEditShip extends Menu{
    private final MenuComponentButton done;
    private final MenuComponentButton repair;
    private final Galaxy galaxy;
    private final Hangar hangar;
    public final MenuComponentPartPicker parts;
    public final MenuComponentShipGrid grid;
    private final MenuComponentShipStats stats;
    public MenuEditShip(GUI gui, Menu parent, Galaxy galaxy, Hangar hangar){
        super(gui, parent);
        if(hangar.ship==null||hangar.ship.blocks==null){
            hangar.ship = new Ship("Ship", new Block[hangar.width][hangar.length][hangar.height]);
        }
        Block[][][] blox = hangar.ship.blocks;
        if(hangar.width>hangar.ship.width()){
            int amount = hangar.width-hangar.ship.width();
            if(amount%2==0){
                amount--;
            }
            hangar.ship.blocks = new Block[hangar.width][hangar.length][hangar.height];
            for(int z = 0; z<blox[0][0].length; z++){
                for(int y = 0; y<blox[0].length; y++){
                    for(int x = 0; x<blox.length; x++){
                        hangar.ship.blocks[x+amount/2][y][z] = blox[x][y][z];
                        blox[x][y][z] = null;
                    }
                }
            }
        }
        if(hangar.length>hangar.ship.length()){
            int amount = hangar.length-hangar.ship.length();
            if(amount%2==0){
                amount--;
            }
            hangar.ship.blocks = new Block[hangar.width][hangar.length][hangar.height];
            for(int z = 0; z<blox[0][0].length; z++){
                for(int y = 0; y<blox[0].length; y++){
                    for(int x = 0; x<blox.length; x++){
                        hangar.ship.blocks[x][y+amount/2][z] = blox[x][y][z];
                        blox[x][y][z] = null;
                    }
                }
            }
        }
        if(hangar.height>hangar.ship.height()){
            int amount = hangar.height-hangar.ship.height();
            if(amount%2==0){
                amount--;
            }
            hangar.ship.blocks = new Block[hangar.width][hangar.length][hangar.height];
            for(int z = 0; z<blox[0][0].length; z++){
                for(int y = 0; y<blox[0].length; y++){
                    for(int x = 0; x<blox.length; x++){
                        hangar.ship.blocks[x][y][z+amount/2] = blox[x][y][z];
                        blox[x][y][z] = null;
                    }
                }
            }
        }
        stats = add(new MenuComponentShipStats(hangar.ship, 250, false));
        grid = add(new MenuComponentShipGrid(200, 0, Display.getWidth()-450, Display.getHeight(), 25, 25, hangar));
        parts = add(new MenuComponentPartPicker(200, 100));
        done = add(new MenuComponentButton(0, Display.getHeight()-50, 200, 50, "Done", true, true, "/textures/gui/button"));
        repair = add(new MenuComponentButton(0, Display.getHeight()-100, 200, 50, "Repair All", true, true, "/textures/gui/button"));
        this.galaxy = galaxy;
        this.hangar = hangar;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        if(button==done){
            gui.open(new MenuHangar(gui, parent,  galaxy, ((MenuHangar)parent).planet, hangar));
        }
        if(button==repair){
            for(Block b : hangar.ship.getBlocks()){
                ArrayList<ItemStack> cost = getRepairCost(b);
                if(hangar.hasCargo(cost)){
                    b.health = b.type.health[b.level];
                    hangar.removeCargo(cost);
                }
            }
        }
    }
    @Override
    public void tick() {
        super.tick();
        parts.tick();
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat) {
        if(key==Keyboard.KEY_SPACE&&pressed&&!repeat){
            grid.selected = null;
            parts.selected = null;
            parts.updateButtons();
        }
        if(key==Keyboard.KEY_ESCAPE&&pressed&&!repeat){
            grid.selected = null;
            parts.selected = null;
            parts.category = null;
            parts.updateButtons();
        }
        if(key==Keyboard.KEY_PRIOR&&pressed&&!repeat){
            if(grid.z<hangar.ship.blocks[0][0].length-1){
                grid.up();
            }
        }
        if(key==Keyboard.KEY_NEXT&&pressed&&!repeat){
            if(grid.z>0){
                grid.down();
            }
        }
    }
    @Override
    public void renderBackground(){
        if(selected==grid){
            stats.block = grid.getSelected();
        }else if(selected==parts){
            stats.block = parts.getSelected();
        }else{
            stats.block = null;
        }
        drawRect(0, 0, Display.getWidth(), Display.getHeight(), ImageStash.instance.getTexture("/textures/gui/background/hangar.png"));
    }
    @Override
    public void render(int millisSinceLastTick){
        FontManager.setFont("small");
        super.render(millisSinceLastTick);
        ArrayList<ItemStack> repairCost = getRepairCost();
        if(repair.isMouseOver&&!repairCost.isEmpty()){
            int width = 50;
            int height = 50;
            int x = Mouse.getX();
            int y = Math.min(Display.getHeight()-(height*repairCost.size()), Math.max(0, Display.getHeight()-Mouse.getY()));
            for(int i = 0; i<repairCost.size(); i++){
                ItemStack stack = repairCost.get(i);
                drawRect(x, y+i*height, x+width, y+(i+1)*height, ImageStash.instance.getTexture("/textures/items/"+stack.item.toString()+".png"));
                drawText(x+width, y+height*i+height/3, x+width*2, y+height*i+height/3+height/3, stack.amount+"");
            }
        }
    }
    private ArrayList<ItemStack> getRepairCost(){
        ArrayList<ItemStack> cost = new ArrayList<>();
        for(Block b : hangar.ship.getBlocks()){
            FOR:for(ItemStack stack : getRepairCost(b)){
                for(ItemStack s : cost){
                    if(s.item==stack.item){
                        s.amount+=stack.amount;
                        continue FOR;
                    }
                }
                cost.add(new ItemStack(stack));
            }
        }
        return cost;
    }
    private ArrayList<ItemStack> getRepairCost(Block b){
        ArrayList<ItemStack> cost = new ArrayList<>();
        if(b==null)return cost;
        if(b.health<b.type.health[b.level]){
            double percent = 1-(b.health/(double)b.type.health[b.level]);
            FOR:for(ItemStack stack : b.type.cost[b.level]){
                int a = (int) (stack.amount*percent);
                if(a==0)continue;
                for(ItemStack s : cost){
                    if(s.item==stack.item){
                        s.amount+=a;
                        continue FOR;
                    }
                }
                cost.add(new ItemStack(stack.item, a));
            }
        }
        return cost;
    }
}