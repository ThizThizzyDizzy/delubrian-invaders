package delubrian.invaders;
import java.io.Serializable;
import java.util.Iterator;
import simplelibrary.config2.Config;
public class ItemStack implements Serializable{
    private static final long serialVersionUID = 11238347109237401L;
    public final Item item;
    public int amount;
    public ItemStack(Item item, int amount){
        this.item = item;
        this.amount = amount;
    }
    public ItemStack(ItemStack stack, int amount){
        this(stack.item, amount);
    }
    public ItemStack(Item item){
        this(item, 1);
    }
    public static ItemStack load(Config config){
        int amount = config.get("amount", 1);
        Item item = null;
        for(Item i : Item.items){
            if(config.get("item", "").equals(i.getName())){
                item = i;
            }
        }
        return new ItemStack(item, amount);
    }
    public ItemStack(ItemStack stack) {
        this(stack.item, stack.amount);
    }
    public Config save(){
        Config config = Config.newConfig();
        config.set("amount", amount);
        config.set("item", item.getName());
        return config;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemStack){
            ItemStack i = (ItemStack) obj;
            return item.equals(i.item)&&i.amount==amount;
        }
        return false;
    }
}