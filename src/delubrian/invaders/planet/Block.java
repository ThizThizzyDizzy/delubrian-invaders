package delubrian.invaders.planet;
import delubrian.invaders.Core;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.MenuBattle;
import delubrian.invaders.menu.component.MenuComponentShip;
import java.io.Serializable;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
public class Block implements Serializable{
    private static final long serialVersionUID = 98914891L;
    public BlockType type;
    public int level;
    public int health;
    public double weight;
    public int damage;
    public double fireRate;
    public int ammoConsumption;
    public double manaConsumption;
    public double manaStorage;
    public double manaGeneration;
    public double manaCollection;
    public double fuelStorage;
    public double fuelUsage;
    public double thrust;
    public int cargoCapacity;
    public ArrayList<ItemStack> cargo = new ArrayList<>();
    public boolean explode = false;
    public boolean exploded = false;
    public Block(BlockType type, int level){
        this.type = type;
        this.level = level;
        health = type.health[level];
        weight = type.weight[level];
        damage = type.damage[level];
        fireRate = type.fireRate[level];
        ammoConsumption = type.ammoConsumption[level];
        manaConsumption = type.manaConsumption[level];
        manaStorage = type.manaStorage[level];
        manaGeneration = type.manaGeneration[level];
        manaCollection = type.manaCollection[level];
        fuelStorage = type.fuelStorage[level];
        fuelUsage = type.fuelUsage[level];
        thrust = type.thrust[level];
        cargoCapacity = type.cargo[level];
    }
    public int cargoSpace(){
        int space = type.cargo[level];
        for(ItemStack stack : cargo){
            space -= stack.amount;
        }
        return space;
    }
    public double fuelSpace(){
        return type.fuelStorage[level]-fuelStorage;
    }
    public void addCargo(Item item){
        for(ItemStack stack : cargo){
            if(stack.item.equals(item)){
                stack.amount++;
                return;
            }
        }
        cargo.add(new ItemStack(item, 1));
    }
    public int getCargoAmount(){
        int cargoAmount = 0;
        for(ItemStack stack : cargo){
            cargoAmount += stack.amount;
        }
        return cargoAmount;
    }
    public static Block load(Config config) {
        if(!config.hasProperty("type")){
            return null;
        }
        Block block = new Block(BlockType.get(config.get("type","Armor")), config.get("level",1));
        block.health = config.get("health",block.health);
        block.weight = config.get("weight",block.weight);
        block.damage = config.get("damage",block.damage);
        block.fireRate = config.get("fire rate",block.fireRate);
        block.ammoConsumption = config.get("ammo consumption",block.ammoConsumption);
        block.manaConsumption = config.get("mana consumption",block.manaConsumption);
        block.manaStorage = config.get("mana storage",block.manaStorage);
        block.manaGeneration = config.get("mana generation",block.manaGeneration);
        block.manaCollection = config.get("mana collection",block.manaCollection);
        block.fuelStorage = config.get("fuel storage",block.fuelStorage);
        block.fuelUsage = config.get("fuel usage",block.fuelUsage);
        block.thrust = config.get("thrust",block.thrust);
        block.cargoCapacity = config.get("cargo",block.cargoCapacity);
        for(int i = 0; i<config.get("cargo amount", 0); i++){
            block.cargo.add(ItemStack.load(config.get("cargo "+i)));
        }
        return block;
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("type", type.name);
        config.set("level", level);
        config.set("health", health);
        config.set("weight", weight);
        config.set("damage", damage);
        config.set("fire rate", fireRate);
        config.set("ammo consumption", ammoConsumption);
        config.set("mana consumption", manaConsumption);
        config.set("mana storage", manaStorage);
        config.set("mana generation", manaGeneration);
        config.set("mana collectioon", manaCollection);
        config.set("fuel storage", fuelStorage);
        config.set("fuel usage", fuelUsage);
        config.set("thrust", thrust);
        config.set("cargo", cargoCapacity);
        config.set("cargo amount", cargo.size());
        for(int i = 0; i<cargo.size(); i++){
            config.set("cargo "+i, cargo.get(i).save());
        }
        return config;
    }
    public void tick(MenuComponentShip component){
        if(health<=0){
            explode = true;
            return;
        }
        if(fireRate<=type.fireRate[level]){
            fireRate+=0.05;
        }
        if(fireRate>type.fireRate[level]){
            fireRate = type.fireRate[level];
        }
        Ship ship = component.getShip();
        switch(type){
            case BLASTER:
                if(ship.firing){
                    if(ship.hasMana(manaConsumption)&&fireRate==type.fireRate[level]){
                        ship.addBolt(this);
                        ship.removeMana(manaConsumption);
                        fireRate = 0;
                    }
                }
                break;
            case COLLECTOR:
                ship.addMana(manaCollection/20);
                break;
            case CORE:
                if(ship.hasMana(manaConsumption/20)){
                    double movement = thrust/ship.weight();
                    if(component.parent instanceof MenuBattle){
                        movement *= 10;
                    }
                    if(ship.movingDown){
                        component.y += movement;
                        ship.removeMana(manaConsumption/20);
                    }
                    if(ship.movingUp){
                        component.y -= movement;
                        ship.removeMana(manaConsumption/20);
                    }
                    if(ship.movingLeft){
                        component.x -= movement;
                        ship.removeMana(manaConsumption/20);
                    }
                    if(ship.movingRight){
                        component.x += movement;
                        ship.removeMana(manaConsumption/20);
                    }
                }
                break;
            case GENERATOR:
                double generate = manaGeneration;
                double percent = fuelStorage/fuelUsage;
                generate*=percent;
                generate = Math.max(generate, ship.manaSpace());
                ship.addMana(generate);
                fuelStorage-=generate/manaGeneration*fuelUsage;
                break;
            case LASER:
                if(ship.firing){
                    if(ship.hasMana(manaConsumption)&&fireRate==type.fireRate[level]){
                        ship.addLaser(this);
                        ship.removeMana(manaConsumption);
                        fireRate = 0;
                    }
                }
                break;
            case THRUSTER:
                break;
        }
    }
    public Item getItem(){
        return type.item[level];
    }
    public double damage(double damage){
        health-=damage;
        damage = 0;
        if(health<0){
            damage = -health;
            health = 0;
        }
        return damage;
    }
    public void render(double left, double top, double right, double bottom) {
        if(health<=0)return;
        Renderer2D.drawRect(left, top, right, bottom, ImageStash.instance.getTexture("/textures/ship/blocks/"+type.name.toLowerCase()+" "+(level+1)+".png"));
        GL11.glColor4d(1, 0, 0, (1-(health/(double)type.health[level])));
        Renderer2D.drawRect(left, top, right, bottom, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public static Block randomBlock(){
        BlockType type = BlockType.values()[Core.rand.nextInt(BlockType.values().length)];
        return new Block(type, Core.rand.nextInt(type.health.length));
    }
    public static Block randomBlock(Ship ship){
        if(!ship.containsBlock(BlockType.CORE)){
            return new Block(BlockType.CORE, BlockType.CORE.health.length-1);
        }
        if(!ship.containsBlock(BlockType.COLLECTOR)){
            return new Block(BlockType.COLLECTOR, Core.rand.nextInt(BlockType.COLLECTOR.health.length));
        }
        if(!ship.containsBlock(BlockType.BATTERY)){
            return new Block(BlockType.BATTERY, Core.rand.nextInt(BlockType.BATTERY.health.length));
        }
        if((!ship.containsBlock(BlockType.LASER))&&(!ship.containsBlock(BlockType.BLASTER))){
            boolean blaster = Core.rand.nextBoolean();
            if(blaster){
                return new Block(BlockType.BLASTER, Core.rand.nextInt(BlockType.BLASTER.health.length));
            }else{
                return new Block(BlockType.LASER, Core.rand.nextInt(BlockType.LASER.health.length));
            }
        }
        return randomBlock();
    }
}