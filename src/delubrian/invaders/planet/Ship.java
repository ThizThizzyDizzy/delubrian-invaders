package delubrian.invaders.planet;
import delubrian.invaders.Item;
import delubrian.invaders.ItemStack;
import delubrian.invaders.menu.component.MenuComponentShip;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import simplelibrary.config2.Config;
public class Ship implements Serializable{
    private static final long serialVersionUID = 138144L;
    private String name;
    public Block[][][] blocks;
    public boolean firing;
    public boolean movingRight;
    public boolean movingLeft;
    public boolean movingDown;
    public boolean movingUp;
    public ArrayList<Block> firingBolts = new ArrayList<>();
    public ArrayList<Block> firingLasers = new ArrayList<>();
    public boolean redFuelStorage = false;
    public boolean redCores = false;
    public boolean redManaStorage = false;
    public boolean redManaGeneration = false;
    public boolean redStructuralIntegrity = false;
    public ArrayList<int[]> explosions = new ArrayList<>();
    public ArrayList<ItemStack> cargoToEject = new ArrayList<>();
    public boolean dead = false;
    public Ship(String name, Block[][][] blocks){
        this.name = name;
        this.blocks = blocks;
    }
    public int width(){
        return blocks.length;
    }
    public int length(){
        return blocks[0].length;
    }
    public int height(){
        return blocks[0][0].length;
    }
    public static Ship load(Config config, Hangar parent){
        Ship ship = new Ship(config.get("name","Ship"), null);
        ship.blocks = new Block[config.get("width", 3)][config.get("length", 3)][config.get("height",3)];
        for(int z = 0; z<ship.blocks[0][0].length; z++){
            for(int y = 0; y<ship.blocks[0].length; y++){
                for(int x = 0; x<ship.blocks.length; x++){
                    ship.blocks[x][y][z] = Block.load(config.get("block "+x+" "+y+" "+z, Config.newConfig()));
                }
            }
        }
        return ship;
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("name", name);
        if(blocks==null){
            return config;
        }
        for(int z = 0; z<blocks[0][0].length; z++){
            for(int y = 0; y<blocks[0].length; y++){
                for(int x = 0; x<blocks.length; x++){
                    if(blocks[x][y][z]!=null){
                        config.set("block "+x+" "+y+" "+z, blocks[x][y][z].save());
                    }
                }
            }
        }
        return config;
    }
    public boolean canLaunch(){
        redFuelStorage = false;
        redCores = false;
        redManaStorage = false;
        redManaGeneration = false;
        redStructuralIntegrity = false;
        if(blocks==null){
            return false;
        }
        boolean launch = true;
        if((containsBlocks(BlockType.GENERATOR)||containsBlocks(BlockType.THRUSTER))&&!containsBlocks(BlockType.FUEL_TANK)){
            redFuelStorage = true;
        }
        if(!containsBlocks(BlockType.CORE)){
            redCores = true;
        }
        if(!containsBlocks(BlockType.BATTERY)){
            redManaStorage = true;
        }
        if(!containsBlocks(BlockType.GENERATOR)&&!containsBlocks(BlockType.COLLECTOR)){
            redManaGeneration = true;
        }
        if((!(containsBlocks(BlockType.CORE, BlockType.BATTERY)&&
                (containsBlocks(BlockType.GENERATOR)||containsBlocks(BlockType.COLLECTOR))))||
                containsBlocks(BlockType.THRUSTER)&&!containsBlocks(BlockType.FUEL_TANK)||
                containsBlocks(BlockType.GENERATOR)&&!containsBlocks(BlockType.FUEL_TANK)){
            launch = false;
        }
        for(int z = 0; z<blocks[0][0].length; z++){
            for(int y = 0; y<blocks[0].length; y++){
                for(int x = 0; x<blocks.length; x++){
                    if(blocks[x][y][z]==null||blocks[x][y][z].type==BlockType.CORE){
                        continue;
                    }
                    if(z>0){
                        if(y>0){
                            if(x>0){
                                if(blocks[x-1][y-1][z-1]!=null&&blocks[x-1][y-1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y-1][z-1]!=null&&blocks[x][y-1][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y-1][z-1]!=null&&blocks[x+1][y-1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                        if(x>0){
                            if(blocks[x-1][y][z-1]!=null&&blocks[x-1][y][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y][z-1]!=null&&blocks[x][y][z-1].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y][z-1]!=null&&blocks[x+1][y][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(y<blocks[0].length-1){
                            if(x>0){
                                if(blocks[x-1][y+1][z-1]!=null&&blocks[x-1][y+1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y+1][z-1]!=null&&blocks[x][y+1][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y+1][z-1]!=null&&blocks[x+1][y+1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                    }
                    if(y>0){
                        if(x>0){
                            if(blocks[x-1][y-1][z]!=null&&blocks[x-1][y-1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y-1][z]!=null&&blocks[x][y-1][z].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y-1][z]!=null&&blocks[x+1][y-1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                    }
                    if(x>0){
                        if(blocks[x-1][y][z]!=null&&blocks[x-1][y][z].level>blocks[x][y][z].level){
                            continue;
                        }
                    }
                    if(x<blocks.length-1){
                        if(blocks[x+1][y][z]!=null&&blocks[x+1][y][z].level>blocks[x][y][z].level){
                            continue;
                        }
                    }
                    if(y<blocks[0].length-1){
                        if(x>0){
                            if(blocks[x-1][y+1][z]!=null&&blocks[x-1][y+1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y+1][z]!=null&&blocks[x][y+1][z].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y+1][z]!=null&&blocks[x+1][y+1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                    }
                    if(z<blocks[0][0].length-1){
                        if(y>0){
                            if(x>0){
                                if(blocks[x-1][y-1][z+1]!=null&&blocks[x-1][y-1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y-1][z+1]!=null&&blocks[x][y-1][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y-1][z+1]!=null&&blocks[x+1][y-1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                        if(x>0){
                            if(blocks[x-1][y][z+1]!=null&&blocks[x-1][y][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y][z+1]!=null&&blocks[x][y][z+1].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y][z+1]!=null&&blocks[x+1][y][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(y<blocks[0].length-1){
                            if(x>0){
                                if(blocks[x-1][y+1][z+1]!=null&&blocks[x-1][y+1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y+1][z+1]!=null&&blocks[x][y+1][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y+1][z+1]!=null&&blocks[x+1][y+1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                    }
                    redStructuralIntegrity = true;
                    launch = false;
                }
            }
        }
        return launch;
    }
    private boolean containsBlocks(BlockType... types){
        for(BlockType type : types){
            if(!containsBlock(type)){
                return false;
            }
        }
        return true;
    }
    public boolean containsBlock(BlockType type) {
        for(int z = 0; z<blocks[0][0].length; z++){
            for(int y = 0; y<blocks[0].length; y++){
                for(int x = 0; x<blocks.length; x++){
                    if(blocks[x][y][z]==null){
                        continue;
                    }
                    if(blocks[x][y][z].type==type){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public ArrayList<Block> getBlocks(){
        ArrayList<Block> blox = new ArrayList<>();
        int i = 0;
        for(Block[][] blockArray : blocks){
            for(Block[] block : blockArray){
                for(Block b : block){
                    if(b==null)continue;
                    blox.add(b);
                    i++;
                }
            }
        }
        return blox;
    }
    public ArrayList<Block> getBlocks(BlockType type){
        ArrayList<Block> blox = new ArrayList<>();
        int i = 0;
        for(Block[][] blockArray : blocks){
            for(Block[] block : blockArray){
                for(Block b : block){
                    if(type==null&&b==null){
                        blox.add(b);
                        i++;
                    }else if(b!=null&&type==b.type){
                        blox.add(b);
                        i++;
                    }
                }
            }
        }
        return blox;
    }
    public ArrayList<ItemStack> getCargo(){
        ArrayList<ItemStack> cargo = new ArrayList<>();
        for(Block block : getBlocks()){
            if(block==null) continue;
            FOR:for(ItemStack stack : block.cargo){
                for(ItemStack s : cargo){
                    if(s.item.equals(stack.item)){
                        s.amount+=stack.amount;
                        continue FOR;
                    }
                }
                cargo.add(new ItemStack(stack));
            }
            if(block.fuelStorage>=1){
                DO:do{
                    for(ItemStack s : cargo){
                        if(s.item.equals(Item.FUEL)){
                            s.amount+=block.fuelStorage;
                            break DO;
                        }
                    }
                    cargo.add(new ItemStack(Item.FUEL, (int) block.fuelStorage));
                }while(false);
            }
        }
        Collections.sort(cargo, (ItemStack o1, ItemStack o2) -> o1.item.getName().compareTo(o2.item.getName()));
        return cargo;
    }
    public int cargoSpace(){
        int space = 0;
        for(Block block : getBlocks()){
            space += block.cargoSpace();
        }
        return space;
    }
    public void addCargo(ArrayList<ItemStack> cargo){
        for(ItemStack stack : cargo){
            for(int i = 0; i<stack.amount; i++){
                addCargo(stack.item);
            }
        }
    }
    public void addCargo(Item item){
        if(item==Item.FUEL){
            for(Block block : getBlocks()){
                if(block.fuelSpace()>=1){
                    block.fuelStorage++;
                    return;
                }
            }
        }
        for(Block block : getBlocks()){
            if(block.cargoSpace()>0){
                block.addCargo(item);
                return;
            }
        }
    }
    public void removeCargo(ArrayList<ItemStack> cargo){
        for(ItemStack stack : cargo){
            removeCargo(stack);
        }
    }
    public void removeCargo(ItemStack cargo){
        for(int i = 0; i<cargo.amount; i++){
            removeCargo(cargo.item);
        }
    }
    public void removeCargo(Item item){
        if(item==Item.FUEL){
            for(Block block : getBlocks()){
                if(block.fuelStorage>=1){
                    block.fuelStorage--;
                    return;
                }
            }
        }
        for(Block block : getBlocks()){
            for (Iterator<ItemStack> it = block.cargo.iterator(); it.hasNext();) {
                ItemStack stack = it.next();
                if(stack.item.equals(item)){
                    stack.amount--;
                    if(stack.amount==0){
                        it.remove();
                    }
                    return;
                }
            }
        }
    }
    public void tick(MenuComponentShip ship){
        addMana(0.05);
        boolean dead = true;
        for(int z = 0; z<blocks[0][0].length; z++){
            for(int y = 0; y<blocks[0].length; y++){
                for(int x = 0; x<blocks.length; x++){
                    Block b = blocks[x][y][z];
                    if(b==null)continue;
                    dead = false;
                    checkIntegrity(b);
                    b.tick(ship);
                    if(b.explode&&!b.exploded){
                        blocks[x][y][z] = null;
                        cargoToEject.addAll(b.cargo);
                        b.cargo.clear();
                        explosions.add(new int[]{x,y});
                    }
                }
            }
        }
        if(dead){
            this.dead = true;
        }
    }
    boolean hasMana(double mana){
        double has = 0;
        for(Block block : getBlocks(BlockType.BATTERY)){
            has += block.manaStorage;
            if(has>=mana)return true;
        }
        return false;
    }
    double maxMana(){
        double max = 0;
        for(Block block : getBlocks(BlockType.BATTERY)){
            max += block.type.manaStorage[block.level];
        }
        return max;
    }
    double totalMana(){
        double total = 0;
        for(Block block : getBlocks(BlockType.BATTERY)){
            total += block.manaStorage;
        }
        return total;
    }
    void removeMana(double mana){
        for(Block block : getBlocks(BlockType.BATTERY)){
            if(mana>block.manaStorage){
                mana -= block.manaStorage;
                block.manaStorage = 0;
                continue;
            }
            block.manaStorage -= mana;
            return;
        }
    }
    void addMana(double mana){
        for(Block block : getBlocks(BlockType.BATTERY)){
            if(mana>block.type.manaStorage[block.level]-block.manaStorage){
                mana -= block.type.manaStorage[block.level]-block.manaStorage;
                block.manaStorage = block.type.manaStorage[block.level];
                continue;
            }
            block.manaStorage += mana;
            return;
        }
    }
    public void addBolt(Block block){
        firingBolts.add(block);
    }
    public void addLaser(Block block){
        firingLasers.add(block);
    }
    public int getCargoAmount() {
        int i = 0;
        for(ItemStack is : getCargo()){
            i += is.amount;
        }
        return i;
    }
    double weight(){
        double weight = 0;
        for(Block block : getBlocks()){
            weight += block.weight;
        }
        return weight;
    }
    public void removeAllCargo(Item item){
        for(ItemStack stack : getCargo()){
            if(stack.item.equals(item)){
                removeCargo(stack);
                return;
            }
        }
    }
    private void checkIntegrity(Block b){
        boolean badStructuralIntegrity = false;
        for(int z = 0; z<blocks[0][0].length; z++){
            for(int y = 0; y<blocks[0].length; y++){
                for(int x = 0; x<blocks.length; x++){
                    if(blocks[x][y][z]==null||blocks[x][y][z].type==BlockType.CORE||blocks[x][y][z]!=b){
                        continue;
                    }
                    if(z>0){
                        if(y>0){
                            if(x>0){
                                if(blocks[x-1][y-1][z-1]!=null&&blocks[x-1][y-1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y-1][z-1]!=null&&blocks[x][y-1][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y-1][z-1]!=null&&blocks[x+1][y-1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                        if(x>0){
                            if(blocks[x-1][y][z-1]!=null&&blocks[x-1][y][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y][z-1]!=null&&blocks[x][y][z-1].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y][z-1]!=null&&blocks[x+1][y][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(y<blocks[0].length-1){
                            if(x>0){
                                if(blocks[x-1][y+1][z-1]!=null&&blocks[x-1][y+1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y+1][z-1]!=null&&blocks[x][y+1][z-1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y+1][z-1]!=null&&blocks[x+1][y+1][z-1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                    }
                    if(y>0){
                        if(x>0){
                            if(blocks[x-1][y-1][z]!=null&&blocks[x-1][y-1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y-1][z]!=null&&blocks[x][y-1][z].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y-1][z]!=null&&blocks[x+1][y-1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                    }
                    if(x>0){
                        if(blocks[x-1][y][z]!=null&&blocks[x-1][y][z].level>blocks[x][y][z].level){
                            continue;
                        }
                    }
                    if(x<blocks.length-1){
                        if(blocks[x+1][y][z]!=null&&blocks[x+1][y][z].level>blocks[x][y][z].level){
                            continue;
                        }
                    }
                    if(y<blocks[0].length-1){
                        if(x>0){
                            if(blocks[x-1][y+1][z]!=null&&blocks[x-1][y+1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y+1][z]!=null&&blocks[x][y+1][z].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y+1][z]!=null&&blocks[x+1][y+1][z].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                    }
                    if(z<blocks[0][0].length-1){
                        if(y>0){
                            if(x>0){
                                if(blocks[x-1][y-1][z+1]!=null&&blocks[x-1][y-1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y-1][z+1]!=null&&blocks[x][y-1][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y-1][z+1]!=null&&blocks[x+1][y-1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                        if(x>0){
                            if(blocks[x-1][y][z+1]!=null&&blocks[x-1][y][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(blocks[x][y][z+1]!=null&&blocks[x][y][z+1].level>blocks[x][y][z].level){
                            continue;
                        }
                        if(x<blocks.length-1){
                            if(blocks[x+1][y][z+1]!=null&&blocks[x+1][y][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                        }
                        if(y<blocks[0].length-1){
                            if(x>0){
                                if(blocks[x-1][y+1][z+1]!=null&&blocks[x-1][y+1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                            if(blocks[x][y+1][z+1]!=null&&blocks[x][y+1][z+1].level>blocks[x][y][z].level){
                                continue;
                            }
                            if(x<blocks.length-1){
                                if(blocks[x+1][y+1][z+1]!=null&&blocks[x+1][y+1][z+1].level>blocks[x][y][z].level){
                                    continue;
                                }
                            }
                        }
                    }
                    badStructuralIntegrity = true;
                }
            }
        }
        if(badStructuralIntegrity){
            b.health = 0;
        }
    }
    public void ejectAllCargo(Item item){
        for(ItemStack stack : getCargo()){
            if(stack.item.equals(item)){
                ejectCargo(stack);
                return;
            }
        }
    }
    public void ejectCargo(ItemStack stack){
        removeCargo(stack);
        cargoToEject.add(stack);
    }
    public double manaSpace(){
        return totalMana()-maxMana();
    }
}