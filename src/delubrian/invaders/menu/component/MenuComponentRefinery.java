package delubrian.invaders.menu.component;
import delubrian.invaders.ReverseJob;
import delubrian.invaders.menu.MenuSurface;
import delubrian.invaders.planet.Refinery;
public class MenuComponentRefinery extends MenuComponentBuilding{
    private final Refinery refinery;
    public boolean refreshNeeded = false;
    public MenuComponentRefinery(Refinery refinery){
        super(refinery, "refinery");
        this.refinery = refinery;
    }
    /**
     * @return the refinery
     */
    public Refinery getRefinery() {
        return refinery;
    }
    public int workSpeed(){
        return refinery.height*refinery.width*refinery.length;
    }
    @Override
    public void tick(){
        if(parent.parent instanceof MenuSurface&&!refinery.jobs.isEmpty()){
            if(refinery.jobs.get(0) instanceof ReverseJob){
                throw new IllegalStateException("Refinery can not do reverse jobs! How did this happen?");
            }else{
                if(refinery.hasCargo(refinery.jobs.get(0).recipe.input)){
                    refinery.jobs.get(0).progress+=workSpeed();
                    if(refinery.jobs.get(0).progress>refinery.jobs.get(0).recipe.ticks){
                        refinery.removeCargo(refinery.jobs.get(0).recipe.input);
                        refinery.addCargo(refinery.jobs.get(0).recipe.output);
                        refinery.addCargo(refinery.jobs.get(0).recipe.waste);
                        if(refinery.jobs.get(0).count==1){
                            refinery.jobs.remove(0);
                        }else{
                            refinery.jobs.get(0).count--;
                            refinery.jobs.get(0).progress = 0;
                        }
                    }
                    refreshNeeded = true;
                }
            }
        }
    }
}