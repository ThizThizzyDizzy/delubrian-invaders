package delubrian.invaders.planet;
import delubrian.invaders.Item;
public enum MineralType{
    IRON(Item.ORE_IRON),
    STONE(Item.STONE),
    CHILLTANIUM(Item.ORE_CHILLTANIUM),
    ORICHALCUM(Item.ORICHALCUM),
    AVENTURINE(Item.ORE_AVENTURINE),
    RUBY(Item.ORE_RUBY),
    GOLD(Item.ORE_GOLD),
    CALCITE(Item.CALCITE),
    ANHYDRITE(Item.ANHYDRITE),
    SIDERITE(Item.SIDERITE);
    public final String name;
    MineralType(String name){
        this.name = name;
    }
    MineralType(Item item){
        this.name = item.getName();
    }
    public Item toItem(){
        return Item.getItem(name);
    }
}