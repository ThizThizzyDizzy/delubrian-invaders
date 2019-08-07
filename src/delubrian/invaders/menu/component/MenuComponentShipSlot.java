package delubrian.invaders.menu.component;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Block;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentShipSlot extends MenuComponentButton{
    public Block block;
    public MenuComponentShipSlot(double x, double y, double width, double height, Block block){
        super(x, y, width, height, "", true);
        this.block = block;
    }
    @Override
    public void mouseEvent(double x, double y, int button, boolean isDown) {
        super.mouseEvent(x, y, button, isDown);
        if(isDown&&button==1){
            if(block==null||block.health<block.type.health[block.level])return;
            MenuComponentShipGrid grid = (MenuComponentShipGrid) parent;
            if(block!=null){
                grid.hangar.addCargo(block.getItem());
            }
            block = null;
            grid.hangar.ship.blocks[(int)(this.x-10)/110][(int)(this.y-10)/110][grid.z] = null;
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(-(isSelected?5:0)+x, -(isSelected?5:0)+y, x+width+(isSelected?5:0), y+height+(isSelected?5:0), ImageStash.instance.getTexture("/textures/gui/shipSlot.png"));
        if(block!=null){
            drawRect(-(isSelected?5:0)+x, -(isSelected?5:0)+y, x+width+(isSelected?5:0), y+height+(isSelected?5:0), ImageStash.instance.getTexture("/textures/ship/parts/"+block.type.name+" "+(block.level+1)+".png"));
            GL11.glColor4d(1, 0, 0, 1-(block.health/(double)block.type.health[block.level]));
            drawRect(-(isSelected?5:0)+x, -(isSelected?5:0)+y, x+width+(isSelected?5:0), y+height+(isSelected?5:0), 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
        ArrayList<ItemStack> repairCost = getRepairCost();
        if(isMouseOver&&!repairCost.isEmpty()){
            double W = 50;
            double H = 50;
            double X = Mouse.getX()-((MenuComponentShipGrid)parent).x;
            double Y = Math.min(Display.getHeight()-(H*repairCost.size()), Math.max(0, Display.getHeight()-Mouse.getY()))-((MenuComponentShipGrid)parent).y;
            for(int i = 0; i<repairCost.size(); i++){
                ItemStack stack = repairCost.get(i);
                drawRect(X, Y+i*H, X+W, Y+(i+1)*H, ImageStash.instance.getTexture("/textures/items/"+stack.item.toString()+".png"));
                drawText(X+W, Y+H*i+H/3, X+W*2, Y+H*i+H/3+H/3, stack.amount+"");
            }
        }
    }
    private ArrayList<ItemStack> getRepairCost(){
        ArrayList<ItemStack> cost = new ArrayList<>();
        if(block==null)return cost;
        if(block.health<block.type.health[block.level]){
            double percent = 1-(block.health/(double)block.type.health[block.level]);
            FOR:for(ItemStack stack : block.type.cost[block.level]){
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