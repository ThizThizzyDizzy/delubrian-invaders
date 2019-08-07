package delubrian.invaders.menu.component;
import delubrian.invaders.menu.MenuEditShip;
import delubrian.invaders.planet.Block;
import delubrian.invaders.planet.Hangar;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentScrollable;
public class MenuComponentShipGrid extends MenuComponentScrollable{
    public int z = 0;
    final Hangar hangar;
    public MenuComponentShipGrid(double x, double y, double width, double height, double horizScrollbarHeight, double vertScrollbarWidth, Hangar hangar){
        super(x, y, width, height, horizScrollbarHeight, vertScrollbarWidth, false, false);
        for(int Y = 0; Y<hangar.ship.blocks[0].length; Y++){
            for(int X = 0; X<hangar.ship.blocks.length; X++){
                add(new MenuComponentShipSlot(X*110+60,Y*110+60,100,100,hangar.ship.blocks[X][Y][z]));
            }
        }
        this.hangar = hangar;
    }
    @Override
    public void render(){
        removeRenderBound();
        super.render();
        for(int i = 0; i<hangar.ship.blocks[0][0].length; i++){
            GL11.glColor4d(0.5, 0.5, 0.5, 1);
            if(i==-z+hangar.ship.blocks[0][0].length-1){
                GL11.glColor4d(0, 0.125, 0.5, 1);
            }
            drawRect(x, i*5, x+50, i*5+5, 0);
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void renderBackground(){
        components.stream().forEach((component) -> {
            component.isSelected = selected==component;
        });
    }
    public Block getSelected(){
        MenuComponentShipSlot slot = (MenuComponentShipSlot) selected;
        if(slot==null){
            return null;
        }
        return slot.block;
    }
    @Override
    public void buttonClicked(MenuComponentButton button){
        MenuComponentShipSlot slot = (MenuComponentShipSlot) button;
        MenuEditShip ship = (MenuEditShip) parent;
        if(ship.parts.getSelected()==null){
            return;
        }
        if(hangar.hasCargo(ship.parts.getSelected().getItem())){
            slot.block = ship.parts.getSelected();
            hangar.ship.blocks[(int)(slot.x-60)/110][(int)(slot.y-60)/110][z] = ship.parts.getSelected();
            hangar.removeCargo(ship.parts.getSelected().getItem());
        }
    }
    public void up(){
        z++;
        update();
    }
    public void down(){
        z--;
        update();
    }
    public void update(){
        components.clear();
        for(int Y = 0; Y<hangar.ship.blocks[0].length; Y++){
            for(int X = 0; X<hangar.ship.blocks.length; X++){
                add(new MenuComponentShipSlot(X*110+60,Y*110+60,100,100,hangar.ship.blocks[X][Y][z]));
            }
        }
    }
}