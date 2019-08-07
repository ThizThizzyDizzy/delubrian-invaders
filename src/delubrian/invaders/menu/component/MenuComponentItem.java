package delubrian.invaders.menu.component;
import delubrian.invaders.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponentOptionButton;
public class MenuComponentItem extends MenuComponentOptionButton{
    public final ItemStack stack;
    public int selectedAmount = 0;
    public MenuComponentItem(ItemStack stack){
        super(0, 0, 200, 50, "", true, 1, new String[]{"Previous", "", "Next"});
        this.stack = stack;
        textInset = 5;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(getIndex()==0){
            if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))&&(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))){
                selectedAmount-=1000;
            }else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
                selectedAmount-=10;
            }else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
                selectedAmount-=100;
            }else{
                selectedAmount--;
            }
            setIndex(1);
        }
        if(getIndex()==2){
            if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))&&(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))){
                selectedAmount+=1000;
            }else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)){
                selectedAmount+=10;
            }else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
                selectedAmount+=100;
            }else{
                selectedAmount++;
            }
            setIndex(1);
        }
        if(selectedAmount<0){
            selectedAmount = 0;
        }
        if(selectedAmount>stack.amount){
            selectedAmount = stack.amount;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/gui/button.png"));
        drawRect(x, y, x+50, y+50, ImageStash.instance.getTexture("/textures/items/"+stack.item.toString()+".png"));
        GL11.glColor4d(0, 0, 0, 1);
        drawCenteredText(x+50+textInset, y+textInset, x+width-textInset, y+height/2-textInset, selectedAmount+"/"+stack.amount);
        drawCenteredText(x+50+textInset, y+height/2+textInset, x+width-textInset, y+height-textInset, stack.item.toString());
        GL11.glColor4d(1, 1, 1, 1);
    }
}