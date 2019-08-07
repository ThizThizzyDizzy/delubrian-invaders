package delubrian.invaders.menu.component;
import delubrian.invaders.ItemStack;
import delubrian.invaders.planet.Building;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentButton;
public class MenuComponentBuilding extends MenuComponentButton{
    public final Building building;
    private final String texture;
    public boolean render = false;
    public boolean active = true;
    private int textY;
    private int textHeight = 10;
    public MenuComponentBuilding(Building building, String texture){
        super(building.x*50, building.y*50, building.width*50, building.length*50, "", true);
        this.building = building;
        this.texture = "/textures/buildings/"+texture+".png";
    }
    @Override
    public void render(){
        removeRenderBound();
        if(!render){
            render = true;
            return;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture(texture));
        if(isMouseOver){
            textY = 0;
            FontManager.setFont("small");
            for(ItemStack stack : building.getCargo()){
                text(stack.amount+" "+stack.item.toString());
            }
            FontManager.setFont("font");
        }
    }
    public void doRender(){
        render();
    }
    /**
     * @return the building
     */
    public Building getBuilding(){
        return building;
    }
    private void text(String str){
        GL11.glColor4d(0.875, 0.875, 0.875, 1);
        while(str!=null&&!str.isEmpty()){
            textY += textHeight+1;
            str = drawTextWithWrap(x, y+textY, Display.getWidth(), y+textY+textHeight, str);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
}