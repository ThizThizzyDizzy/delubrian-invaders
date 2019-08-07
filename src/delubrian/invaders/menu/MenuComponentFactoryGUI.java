package delubrian.invaders.menu;
import delubrian.invaders.ItemStack;
import delubrian.invaders.Job;
import delubrian.invaders.menu.component.MenuComponentFactory;
import delubrian.invaders.Recipe;
import delubrian.invaders.ReverseJob;
import delubrian.invaders.planet.Factory;
import delubrian.invaders.planet.Galaxy;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.font.FontManager;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.opengl.gui.components.MenuComponentMulticolumnList;
public final class MenuComponentFactoryGUI extends MenuComponent{
    public MenuComponentFactory factory = null;
    private final MenuComponentMulticolumnList blueprints;
    private final MenuComponentMulticolumnList qeue;
    private final MenuComponentMulticolumnList inventory;
    private final MenuComponentButton moveCargo;
    private Recipe mouseover;
    private Galaxy galaxy;
    public MenuComponentFactoryGUI(Galaxy galaxy){
        super(Display.getWidth(), 0, Display.getWidth(), Display.getHeight());
        blueprints = add(new MenuComponentMulticolumnList(Display.getWidth()/2-520, Display.getHeight()/2-250, 220, 500, 100, 100, 20, true));
        qeue = add(new MenuComponentMulticolumnList(Display.getWidth()/2-300, Display.getHeight()/2-250, 520, 500, 100, 100, 20, true));
        inventory = add(new MenuComponentMulticolumnList(Display.getWidth()/2+220, Display.getHeight()/2-250, 220, 500, 100, 100, 20, true));
        moveCargo = add(new MenuComponentButton(0, 0, 200, 50, "Move Cargo", false, true, "/textures/gui/button"));
        moveCargo.textInset+=14;
        refreshAll();
        this.galaxy = galaxy;
    }
    @Override
    public void keyboardEvent(char character, int key, boolean pressed, boolean repeat){
        if(key==Keyboard.KEY_ESCAPE&&pressed){
            factory = null;
            MenuSurface surface = (MenuSurface) parent.parent;
            surface.unlockScroll();
        }
    }
    @Override
    public void render(){
        removeRenderBound();
        x = factory==null?-Display.getWidth():0;
        if(factory==null){
            return;
        }
        GL11.glColor4d(.5, .5, .5, .5);
        drawRect(x, y, x+width, y+height, 0);
        GL11.glColor4d(1, 1, 1, 1);
        GL11.glColor4d(.5, .5, .75, 1);
        drawRect(blueprints.x, blueprints.y, blueprints.x+blueprints.width, blueprints.y+blueprints.height, 0);
        GL11.glColor4d(.5, .75, .5, 1);
        drawRect(qeue.x, qeue.y, qeue.x+qeue.width, qeue.y+qeue.height, 0);
        GL11.glColor4d(.75, .5, .5, 1);
        drawRect(inventory.x, inventory.y, inventory.x+inventory.width, inventory.y+inventory.height, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    public void renderForeground(){
        for(MenuComponent component : blueprints.components){
            MenuComponentButton button = (MenuComponentButton) component;
            if(button.isMouseOver){
                int width = 50;
                int height = 50;
                int x = Mouse.getX();
                int y = Math.min(Display.getHeight()-(height*mouseover.input.length), Math.max(0, Display.getHeight()-Mouse.getY()));
                for(int i = 0; i<mouseover.input.length; i++){
                    ItemStack stack = mouseover.input[i];
                    drawRect(x, y+i*height, x+width, y+(i+1)*height, ImageStash.instance.getTexture("/textures/items/"+stack.item.toString()+".png"));
                    drawText(x+width, y+height*i+height/3, x+width*2, y+height*i+height/3+height/3, stack.amount+"");
                }
                break;
            }
        }
    }
    @Override
    public void tick() {
        if(factory!=null/*&&factory.refreshNeeded*/){
            refreshFactory();
        }
    }
    @Override
    public void buttonClicked(MenuComponentButton button) {
        MenuSurface surface = (MenuSurface) parent.parent;
        if(button==moveCargo){
            gui.open(new MenuSelectCargo(gui, parent, galaxy, surface.planet, factory.getFactory()));
        }
    }
    public void refreshAll(){
        blueprints.components.clear();
        for(Recipe recipe : Recipe.factoryRecipes){
           blueprints.add(new MenuComponentButton(0, 0, 100, 100, "", true){
                @Override
                public void render(){
                    FontManager.setFont("small");
                    if(canMake(recipe)){
                        GL11.glColor4d(.25, .25, .5, 1);
                        drawRect(x,y,x+width, y+height, 0);
                        GL11.glColor4d(.5, .5, .75, 1);
                        drawRect(x+1,y+1,x+width-1, y+height-1, 0);
                        GL11.glColor4d(.25, .25, .5, 1);
                        drawRect(x+2, y+2, x+width-2, y+height-2, 0);
                    }else{
                        GL11.glColor4d(.5, .25, .25, 1);
                        drawRect(x,y,x+width, y+height, 0);
                        GL11.glColor4d(.75, .5, .5, 1);
                        drawRect(x+1,y+1,x+width-1, y+height-1, 0);
                        GL11.glColor4d(.5, .25, .25, 1);
                        drawRect(x+2, y+2, x+width-2, y+height-2, 0);
                    }
                    GL11.glColor4d(1, 1, 1, 1);
                    drawText(x+1, y+2, x+width-2, y+12, recipe.output.item.getName());
                    drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/items/"+recipe.output.item.toString()+".png"));
                    if(isMouseOver){
                        mouseover = recipe;
                    }
                    FontManager.setFont("font");
                }
                @Override
                public void mouseEvent(double x, double y, int button, boolean isDown){
                    if(isDown&&button==0){
                        int count = 1;
                        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                            count*=100;
                        }
                        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                            count*=10;
                        }
                        factory.getFactory().jobs.add(new Job(recipe, count));
                        refreshFactory();
                    }
                }
            });
        }
        refreshFactory();
    }
    public void refreshFactory(){
        qeue.components.clear();
        inventory.components.clear();
        if(factory!=null){
        Factory f = factory.getFactory();
            for(Job job : f.jobs){
                qeue.add(new MenuComponentButton(0, 0, 100, 100, "", false){
                    @Override
                    public void render(){
                        FontManager.setFont("small");
                        if(canWork(job)){
                            GL11.glColor4d(.25, .25, .5, 1);
                            drawRect(x,y,x+width, y+height, 0);
                            GL11.glColor4d(.5, .5, .75, 1);
                            drawRect(x+1,y+1,x+width-1, y+height-1, 0);
                            GL11.glColor4d(.25, .25, .5, 1);
                            drawRect(x+2, y+2, x+width-2, y+height-2, 0);
                        }else{
                            GL11.glColor4d(.5, .25, .25, 1);
                            drawRect(x,y,x+width, y+height, 0);
                            GL11.glColor4d(.75, .5, .5, 1);
                            drawRect(x+1,y+1,x+width-1, y+height-1, 0);
                            GL11.glColor4d(.5, .25, .25, 1);
                            drawRect(x+2, y+2, x+width-2, y+height-2, 0);
                        }
                        GL11.glColor4d(1, 1, 1, 1);
                        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/items/"+job.recipe.output.item.toString()+".png"));
                        GL11.glColor4d(0, 1, 0, 1);
                        if(!canWork(job))GL11.glColor4d(1, 1, 0, 1);
                        drawRect(x, y+height-5, x+(width*(job.progress/(job.recipe.ticks+0D))), y+height, 0);
                        GL11.glColor4d(1, 1, 1, 1);
                        drawText(x+1, y+2, x+width-2, y+12, job.recipe.output.item.getName());
                        drawCenteredText(x, y+height-21, x+width, y+height-5, job.count+"");
                        FontManager.setFont("font");
                    }
                    @Override
                    public void mouseEvent(double x, double y, int button, boolean isDown) {
                        if(isDown&&button==1){
                            int count = 1;
                            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                                count*=100;
                            }
                            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                                count*=10;
                            }
                            job.count-=count;
                            if(job.count<1){
                                f.jobs.remove(job);
                            }
                            refreshFactory();
                        }
                    }
                });
            }
            Job remove = null;
            for(int i = 0; i<f.jobs.size()-1; i++){
                if(f.jobs.get(i).recipe.equals(f.jobs.get(i+1).recipe)){
                    f.jobs.get(i).count+=f.jobs.get(i+1).count;
                    remove = f.jobs.get(i+1);
                    break;
                }
            }
            if(remove!=null){
                f.jobs.remove(remove);
            }
            for(ItemStack stack : f.getCargo()){
                inventory.add(new MenuComponentButton(0, 0, 100, 100, "", false){
                    @Override
                    public void render(){
                        FontManager.setFont("small");
                        GL11.glColor4d(.25, .25, .5, 1);
                        drawRect(x,y,x+width, y+height, 0);
                        GL11.glColor4d(.5, .5, .75, 1);
                        drawRect(x+1,y+1,x+width-1, y+height-1, 0);
                        GL11.glColor4d(.25, .25, .5, 1);
                        drawRect(x+2, y+2, x+width-2, y+height-2, 0);
                        GL11.glColor4d(1, 1, 1, 1);
                        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/items/"+stack.item.toString()+".png"));
                        drawText(x+1, y+2, x+width-2, y+12, stack.item.getName());
                        drawCenteredText(x, y+height-16, x+width, y+height, stack.amount+"");
                        FontManager.setFont("font");
                    }
                    @Override
                    public void mouseEvent(double x, double y, int button, boolean isDown){
                        for(Recipe recipe : Recipe.factoryRecipes){
                            if(recipe.output.item.equals(stack.item)&&recipe.reversible){
                                if(isDown&&button==1){
                                    int count = 1;
                                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                                        count*=100;
                                    }
                                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                                        count*=10;
                                    }
                                    factory.getFactory().jobs.add(new ReverseJob(recipe, count));
                                    refreshFactory();
                                }
                                return;
                            }
                        }
                    }
                });
            }
            factory.refreshNeeded = false;
        }
    }
    @Override
    public void renderBackground(){
        if(factory!=null){
            moveCargo.enabled = factory.getFactory().getCargoAmount()>0;
        }
    }
    public boolean canMake(Recipe recipe){
        if(factory==null)return false;
        return factory.building.hasCargo(recipe.input);
    }
    public boolean canWork(Job job){
        if(factory==null)return false;
        if(job instanceof ReverseJob){
            return factory.building.hasCargo(job.recipe.output);
        }
        return canMake(job.recipe);
    }
}