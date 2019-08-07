package delubrian.invaders.menu.component;
import delubrian.invaders.Dropship;
import delubrian.invaders.ItemStack;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentParkedDropship extends MenuComponentButton{
    public final MenuComponentDropship dropship;
    private double textY = 0;
    public MenuComponentParkedDropship(double x, double y, MenuComponentDropship dropship){
        super(x, y, 50, 50, "", true);
        this.dropship = dropship;
    }
    public MenuComponentParkedDropship(double x, double y, Dropship dropship){
        super(x, y, 50, 50, "", true);
        this.dropship = new MenuComponentDropship(dropship.ship, dropship.planet, dropship.cargo);
    }
    @Override
    public void render(){
        removeRenderBound();
        drawRect(x,y,x+width,y+height,ImageStash.instance.getTexture("/textures/dropship.png"));
        if(isMouseOver){
            textY = 0;
            for(ItemStack stack : dropship.cargo){
                text(stack.amount+" "+stack.item.toString());
            }
        }
    }
    private void text(String str){
        GL11.glColor4d(0.875, 0.875, 0.875, 1);
        while(str!=null&&!str.isEmpty()){
            textY += 9;
            str = drawTextWithWrap(x, y+textY, x+width, y+textY+8, str);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    public boolean canHoldCargo(int cargoAmount){
        return dropship.ship.cargoSpace()-dropship.getCargoAmount()>=cargoAmount;
    }
    void addCargo(ArrayList<ItemStack> cargo){
        dropship.addCargo(cargo);
    }
}