package delubrian.invaders.menu;
import delubrian.invaders.menu.component.MenuComponentDust;
import delubrian.invaders.ItemStack;
import delubrian.invaders.Item;
import delubrian.invaders.menu.component.MenuComponentAsteroid;
import delubrian.invaders.menu.component.MenuComponentDroppedItem;
import delubrian.invaders.menu.component.MenuComponentBattleship;
import delubrian.invaders.menu.component.MenuComponentEnemy;
import delubrian.invaders.menu.component.MenuComponentExplosion;
import delubrian.invaders.menu.component.MenuComponentLaser;
import delubrian.invaders.menu.component.MenuComponentManaBolt;
import delubrian.invaders.menu.component.MenuComponentManaLaser;
import delubrian.invaders.menu.component.MenuComponentParticle;
import delubrian.invaders.menu.component.MenuComponentShipStats;
import delubrian.invaders.planet.AsteroidBelt;
import delubrian.invaders.planet.Mineral;
import delubrian.invaders.planet.Ship;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.Menu;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuBattle extends Menu{
    public static boolean debugMode = false;
    public MenuComponentBattleship ship;
    public final ArrayList<MenuComponentEnemy> enemies = new ArrayList<>();
    private final Random randobaddy = new Random();
    private ArrayList<int[]> stars = new ArrayList<>();
    private Random starRand = new Random();
    private int starCount = 10000;
    final ArrayList<MenuComponent> backgroundComponents = new ArrayList<>();
    final MenuComponentShipStats stats;
    private ArrayList<MenuComponent> componentsToRemove = new ArrayList<>();
    private ArrayList<MenuComponent> nextComponentsToRemove = new ArrayList<>();
    public final ArrayList<MenuComponentEnemy> explode = new ArrayList<>();
    ArrayList<MenuComponentParticle> particles = new ArrayList<>();
    public ArrayList<MenuComponentDroppedItem> droppedItems = new ArrayList<>();
    public final ArrayList<MenuComponentAsteroid> asteroidsToSplit = new ArrayList<>();
    /**
     * The asteroid belt resources are gotten from for asteroid splitting
     */
    public AsteroidBelt belt = null;
    public MenuBattle(GUI gui, Menu parent, Ship ship){
        this(gui, parent, new MenuComponentBattleship(ship));
    }
    public MenuBattle(GUI gui, Menu parent, MenuComponentBattleship ship){
        super(gui, parent);
        backgroundComponents.addAll(setupBackgroundComponents());
        this.ship = add(ship);
        stats = add(new MenuComponentShipStats(ship.getShip(), 250, true));
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth()+50;
        int maxY = Display.getHeight();
        for(int i = 0; i<starCount; i++){
            stars.add(new int[]{starRand.nextInt(maxX-minX)+minX, 
                                starRand.nextInt((maxY-minY)*2)+minY,
                                (starRand.nextInt(100)),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80)});
        }
    }
    public MenuBattle(GUI gui, Menu parent, MenuComponentBattleship ship, boolean showStats){
        super(gui, parent);
        backgroundComponents.addAll(setupBackgroundComponents());
        this.ship = add(ship);
        if(showStats){
            stats = add(new MenuComponentShipStats(ship.getShip(), 250, true));
        }else{
            stats = null;
        }
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth()+50;
        int maxY = Display.getHeight()+1;
        for(int i = 0; i<starCount; i++){
            stars.add(new int[]{starRand.nextInt(maxX-minX)+minX, 
                                starRand.nextInt((maxY-minY)*2)+minY,
                                (starRand.nextInt(100)),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80)});
        }
    }
    @Override
    public void renderBackground(){
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth();
        int maxY = Display.getHeight();
        GL11.glColor4f(0, 0, 0, 1);//black
        drawRect(minX, minY, maxX, maxY, 0);
        GL11.glColor4f(1, 1, 1, 1);//white
        GL11.glBegin(GL11.GL_POINTS);
        for(int[] star : stars){
            GL11.glColor4d(star[3]/100d, star[4]/100d, star[5]/100d, star[2]/100D);
            GL11.glVertex2i(star[0], star[1]);//Draw the stars!
            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glEnd();
        for(MenuComponent component : backgroundComponents){
            component.render();
        }
    }
    public MenuComponentBattleship getShip(){
        return ship;
    }
    public MenuComponentDroppedItem add(MenuComponentDroppedItem d){
        droppedItems.add(d);
        return super.add(d);
    }
    public MenuComponentParticle add(MenuComponentParticle p){
        particles.add(p);
        return super.add(p);
    }
    public MenuComponentAsteroid add(MenuComponentAsteroid a){
       enemies.add(a);
       return super.add(a);
    }
    @Override
    public void tick(){
        removeComponents(nextComponentsToRemove);
        for(MenuComponentEnemy enemy : enemies){
            enemy.tick();
        }
        for (Iterator<MenuComponentParticle> it = particles.iterator(); it.hasNext();) {
            MenuComponentParticle particle = it.next();
            particle.tick();
            if(particle.opacity<=0){
                it.remove();
            }
        }
        for(MenuComponent component : droppedItems){
            component.tick();
        }
        ship.tick();
        if(ship.getShip().dead){
            gui.open(new MenuGameOver(gui, belt.parent.parent));
        }
        for(int[] ex : ship.getShip().explosions){
            explodeBlock(ex);
        }
        int j = 10;
        FOR:for (Iterator<ItemStack> it = ship.getShip().cargoToEject.iterator(); it.hasNext();) {
            ItemStack stack = it.next();
            int I = stack.amount;
            for(int i  = 0; i<I; i++){
                j--;
                if(j<=0){
                    break FOR;
                }
                ejectCargo(stack.item);
                stack.amount--;
            }
            it.remove();
        }
        ship.getShip().explosions.clear();
        if(ship.x<0){
            ship.x = 0;
        }
        if(ship.y<0){
            ship.y = 0;
        }
        if(ship.x>Display.getWidth()-ship.width){
            ship.x = Display.getWidth()-ship.width;
        }
        if(ship.y>Display.getHeight()-ship.height){
            ship.y = Display.getHeight()-ship.height;
        }
        for(MenuComponent component : components){
            if(component instanceof MenuComponentManaBolt){
                MenuComponentManaBolt bolt = (MenuComponentManaBolt) component;
                bolt.tick();
                if(bolt.y<-Display.getHeight()/2){
                    componentsToRemove.add(component);
                }
                for(MenuComponentEnemy enemy : enemies){
                    if(enemy.isHit(bolt)){
                        enemy.hurt(bolt.damage());
                        componentsToRemove.add(component);
                    }
                }
                for(MenuComponentDroppedItem item : droppedItems){
                    if(item.isHit(bolt)){
                        item.hurt(bolt.damage());
                        componentsToRemove.add(component);
                    }
                }
            }
            if(component instanceof MenuComponentManaLaser){
                MenuComponentManaLaser laser = (MenuComponentManaLaser) component;
                laser.tick();
                WHILE:while(laser.y>-Display.getHeight()/2){
                    for(MenuComponentEnemy enemy : enemies){
                        if(enemy.isHit(laser)){
                            enemy.hurt(laser.damage());
                            break WHILE;
                        }
                    }
                    for(MenuComponentDroppedItem item : droppedItems){
                        if(item.isHit(laser)){
                            item.hurt(laser.damage());
                            break WHILE;
                        }
                    }
                    laser.tick();
                }
                nextComponentsToRemove.add(component);
            }
            if(component instanceof MenuComponentLaser){
                MenuComponentLaser laser = (MenuComponentLaser) component;
                laser.tick();
                WHILE:while(laser.y+laser.height<Display.getHeight()){
                    if(isClickWithinBounds(laser.x+laser.width/2, laser.y+laser.height, ship.x, ship.y, ship.x+ship.width, ship.y+ship.height)){
                        ship.laserHurt(laser.damage(), laser.x+laser.width/2-ship.x, randobaddy.nextInt(ship.getShip().height()));
                        break;
                    }
                    for(MenuComponentDroppedItem item : droppedItems){
                        if(item.isHit(laser)){
                            item.hurt(laser.damage());
                            break WHILE;
                        }
                    }
                    for(MenuComponentEnemy enemy : enemies){
                        if(!(enemy instanceof MenuComponentAsteroid))continue;
                        if(enemy.isHit(laser)){
                            enemy.hurt(laser.damage());
                            break WHILE;
                        }
                    }
                    laser.tick();
                }
                nextComponentsToRemove.add(component);
            }
        }
        for(MenuComponentDroppedItem item : droppedItems){
            if(isClickWithinBounds(item.x+item.width/2, item.y+item.height/2, ship.x, ship.y, ship.x+ship.width, ship.y+ship.height)&&ship.getShip().cargoSpace()>=1){
                ship.getShip().addCargo(item.item);
                componentsToRemove.add(item);
            }
            if(item.x+item.width<0||item.y+item.height<0||item.x>Display.getWidth()||item.y>Display.getHeight()||item.health<=0){
                componentsToRemove.add(item);
                add(new MenuComponentDust(item.x+item.width/2, item.y+item.height/2, item.width));
            }
        }
        for(MenuComponentEnemy enemy : enemies){
            if(enemy.x+enemy.width<0||enemy.y+enemy.height<0||enemy.x>Display.getWidth()||enemy.y>Display.getHeight()||enemy.health<=0){
                componentsToRemove.add(enemy);
                add(new MenuComponentDust(enemy.x+enemy.width/2, enemy.y+enemy.height/2, Math.sqrt(enemy.width*enemy.height)));
            }
        }
        for(MenuComponentEnemy enemy: explode){
            explode(enemy);
        }
        explode.clear();
        removeComponents(componentsToRemove);
        for(MenuComponentAsteroid asteroid: asteroidsToSplit){
            split(asteroid);
        }
        asteroidsToSplit.clear();
    }
    public <V extends MenuComponentEnemy> V add(V enemy){
        enemy.x = randobaddy.nextInt((int)(Display.getWidth()-Math.round(enemy.width)));
        enemies.add(enemy);
        return super.add(enemy);
    }
    public abstract ArrayList<MenuComponent> setupBackgroundComponents();
    @Override
    public void render(int millisSinceLastTick){
        super.render(millisSinceLastTick);
        for(MenuComponent item : droppedItems){
            item.render();
        }
        for(MenuComponentEnemy enemy : enemies){
            enemy.render();
        }
        ship.render();
        for(MenuComponent c : components){
            if(c instanceof MenuComponentManaBolt||c instanceof MenuComponentManaLaser||c instanceof MenuComponentLaser){
                c.render();
            }
        }
        for(MenuComponentParticle p : particles){
            p.render();
        }
        if(stats!=null){
            stats.render();
        }
    }
    public void dropRandomItems(MenuComponentEnemy enemy){
        for(Item item : enemy.getDrops(starRand)){
            add(new MenuComponentDroppedItem(enemy.x+starRand.nextInt((int) enemy.width), enemy.y+starRand.nextInt((int) enemy.width), item, enemy.getXSpeed(), enemy.getYSpeed()));
        }
    }
    public void dropRandomItems(MenuComponentAsteroid asteroid){
        for(int i = 0; i<asteroid.width*asteroid.width*Math.PI/5000; i++){
            for(Mineral mineral : belt.minerals){
                if(randobaddy.nextDouble()<=mineral.chance){
                    add(new MenuComponentDroppedItem(asteroid.x+randobaddy.nextInt((int) asteroid.width), asteroid.y+randobaddy.nextInt((int) asteroid.width),mineral.mineral.toItem(), asteroid.getXSpeed(), asteroid.getYSpeed()));
                }
            }
        }
    }
    public void explode(MenuComponentEnemy enemy){
        dropRandomItems(enemy);
        components.remove(enemy);
        MenuComponentParticle p = enemy.getExplosion();
        if(p!=null)
            add(p);
        enemies.remove(enemy);
    }
    private void removeComponents(ArrayList<MenuComponent> cm){
        for(MenuComponent c : cm){
            if(c instanceof MenuComponentDroppedItem){
                droppedItems.remove(c);
            }
            if(c instanceof MenuComponentParticle){
                particles.remove(c);
            }
            if(c instanceof MenuComponentEnemy){
                enemies.remove(c);
            }
            components.remove(c);
        }
        cm.clear();
    }
    private void ejectCargo(Item item){
        boolean right = randobaddy.nextBoolean();
        double xSpeed = (right?1:-1)*Math.abs(randobaddy.nextGaussian()*10);
        double ySpeed = randobaddy.nextGaussian()*5;
        add(new MenuComponentDroppedItem(right?ship.x+ship.width:ship.x-25, ship.y+ship.height/2, item, xSpeed, ySpeed, randobaddy.nextGaussian()*50));
    }
    public void split(MenuComponentAsteroid asteroid){
        if(asteroid.hasSplit)return;
        asteroid.hasSplit = true;
        dropRandomItems(asteroid);
        MenuComponentParticle p = asteroid.getExplosion();
        if(p!=null)
            add(p);
        components.remove(asteroid);
        enemies.remove(asteroid);
        if(asteroid.width<=50){
            return;
        }
        MenuComponentAsteroid a = add(new MenuComponentAsteroid(false, asteroid, 2));
        MenuComponentAsteroid b = add(new MenuComponentAsteroid(false, asteroid, 1));
        MenuComponentAsteroid c = add(new MenuComponentAsteroid(false, asteroid, 3));
        MenuComponentAsteroid d = add(new MenuComponentAsteroid(false, asteroid, 4));
    }
    public void explodeBlock(int[] coords){
        MenuComponentExplosion explosion = new MenuComponentExplosion(ship.x+(coords[0]+.5)*ship.blockSize(), ship.y+(coords[1]+.5)*ship.blockSize(), .5);
        explosion.width = explosion.height = 25;
        add(explosion);
    }
}