package delubrian.invaders.menu.component;
import delubrian.invaders.Item;
import delubrian.invaders.menu.MenuBattle;
import java.util.ArrayList;
import java.util.Random;
public class MenuComponentSmallFighter extends MenuComponentEnemy{
    public MenuComponentSmallFighter(){
        super(100, 100, "fighter/small", 1000);
        y = -height;
        ySpeed = .5;
    }
    @Override
    public void tick(){
        MenuBattle battle = (MenuBattle)parent;
        double X = x;
        if(battle.getShip().x+battle.getShip().width/2<X+width/2){
            X-=.5;
        }
        if(battle.getShip().x+battle.getShip().width/2>X+width/2){
            X+=.5;
        }
        xSpeed = X-x;
        battle.add(new MenuComponentLaser(x+width/2, y+height, 1));
        super.tick();
    }
    @Override
    public void die(){
        ((MenuBattle)parent).explode.add(this);
    }
    @Override
    public ArrayList<Item> getDrops(Random r){
        ArrayList<Item> items = new ArrayList<>();
        for(int i = 0; i<r.nextInt(50)+25; i++){
            items.add(Item.STEEL);
        }
        return items;
    }
}