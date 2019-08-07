package delubrian.invaders.menu.component;
import delubrian.invaders.ReverseJob;
import delubrian.invaders.menu.MenuSurface;
import delubrian.invaders.planet.Factory;
public class MenuComponentFactory extends MenuComponentBuilding{
    private final Factory factory;
    public boolean refreshNeeded = false;
    public MenuComponentFactory(Factory factory){
        super(factory, "factory");
        this.factory = factory;
    }
    /**
     * @return the factory
     */
    public Factory getFactory() {
        return factory;
    }
    public int workSpeed(){
        return factory.height*factory.width*factory.length;
    }
    @Override
    public void tick(){
        if(parent.parent instanceof MenuSurface&&!factory.jobs.isEmpty()){
            if(factory.jobs.get(0) instanceof ReverseJob){
                if(factory.hasCargo(factory.jobs.get(0).recipe.output)){
                    factory.jobs.get(0).progress+=workSpeed();
                    if(factory.jobs.get(0).progress>factory.jobs.get(0).recipe.ticks){
                        factory.removeCargo(factory.jobs.get(0).recipe.output);
                        factory.addCargo(factory.jobs.get(0).recipe.input);
                        if(factory.jobs.get(0).count==1){
                            factory.jobs.remove(0);
                        }else{
                            factory.jobs.get(0).count--;
                            factory.jobs.get(0).progress = 0;
                        }
                    }
                    refreshNeeded = true;
                }
            }else{
                if(factory.hasCargo(factory.jobs.get(0).recipe.input)){
                    factory.jobs.get(0).progress+=workSpeed();
                    if(factory.jobs.get(0).progress>factory.jobs.get(0).recipe.ticks){
                        factory.removeCargo(factory.jobs.get(0).recipe.input);
                        factory.addCargo(factory.jobs.get(0).recipe.output);
                        factory.addCargo(factory.jobs.get(0).recipe.waste);
                        if(factory.jobs.get(0).count==1){
                            factory.jobs.remove(0);
                        }else{
                            factory.jobs.get(0).count--;
                            factory.jobs.get(0).progress = 0;
                        }
                    }
                    refreshNeeded = true;
                }
            }
        }
    }
}