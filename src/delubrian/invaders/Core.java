package delubrian.invaders;
import delubrian.invaders.planet.Galaxy;
import delubrian.invaders.menu.MenuMain;
import delubrian.invaders.menu.MenuMainBattle;
import delubrian.invaders.planet.Planet;
import delubrian.invaders.planet.StarSystem;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import simplelibrary.Sys;
import simplelibrary.error.ErrorAdapter;
import simplelibrary.error.ErrorCategory;
import simplelibrary.font.FontManager;
import simplelibrary.game.GameHelper;
import simplelibrary.opengl.ImageStash;
import simplelibrary.opengl.Renderer2D;
import static simplelibrary.opengl.Renderer2D.drawRect;
import simplelibrary.opengl.gui.GUI;
import simplelibrary.opengl.gui.components.MenuComponent;
import simplelibrary.opengl.gui.components.MenuComponentButton;
import simplelibrary.texture.TexturePack;
import simplelibrary.texture.TexturePackManager;
public class Core extends Renderer2D{
    public static GUI gui;
    public static GameHelper helper;
    public static ArrayList<Long> FPStracker = new ArrayList<>();
    public static boolean debugMode = false;
    public static final boolean is3D = false;
    public static boolean enableCullFace = true;
    public static final boolean fullscreen = true;
    public static final boolean supportTyping = false;
    public static Random rand = new Random();
    public static void main(String[] args) throws NoSuchMethodException{
        helper = new GameHelper();
        helper.setBackground(Color.gray);
        helper.setDisplaySize(800, 600);
        helper.setRenderInitMethod(Core.class.getDeclaredMethod("renderInit", new Class<?>[0]));
        helper.setTickInitMethod(Core.class.getDeclaredMethod("tickInit", new Class<?>[0]));
        helper.setFinalInitMethod(Core.class.getDeclaredMethod("finalInit", new Class<?>[0]));
        helper.setMaximumFramerate(60);
        helper.setRenderMethod(Core.class.getDeclaredMethod("render", int.class));
        helper.setTickMethod(Core.class.getDeclaredMethod("tick", boolean.class));
        helper.setUsesControllers(true);
        helper.setWindowTitle(Main.applicationName);
        helper.setMode(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D);
        if(fullscreen){
            helper.setFullscreen(true);
            helper.setAutoExitFullscreen(false);
        }
        helper.setRenderRange(0, 1000);
        helper.setFrameOfView(90);
        Sys.initLWJGLGame(new File("/errors/"), new ErrorAdapter(){
            @Override
            public void warningError(String message, Throwable error, ErrorCategory catagory){
                if(message==null){
                    return;
                }
                if(message.contains(".png!")){
                    System.err.println(message);
                }
            }
        }, null, helper);
    }
    public static void renderInit() throws LWJGLException{
        helper.frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                helper.running = false;
            }
        });
        FontManager.addFont("/simplelibrary/font");
        FontManager.addFont("/font");
        FontManager.setFont("font");
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        if(is3D){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if(enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        }
        if(supportTyping){
            Keyboard.enableRepeatEvents(true);
        }
        new TexturePackManager(new File(Main.getAppdataRoot()+"\\Texture packs"), new TexturePack(){
            @Override
            public InputStream getResourceAsStream(String name){
                if(name.startsWith("/")){
                    return super.getResourceAsStream(name);
                }
                try{
                    return new FileInputStream(new File(name));
                }catch(FileNotFoundException ex){}
                return super.getResourceAsStream(name);
            }
        });
        gui = new GUI(is3D?GameHelper.MODE_HYBRID:GameHelper.MODE_2D, helper);
        gui.open(new MenuMain(gui, null));
    }
    public static void tickInit() throws LWJGLException{}
    public static void finalInit() throws LWJGLException{
        if(Main.jLayer){
            Sounds.create();
        }
    }
    public static void tick(boolean isLastTick){
        if(Main.jLayer){
            Sounds.tick(isLastTick);
        }
        if(!isLastTick){
            synchronized(gui){
                gui.tick();
            }
        }
    }
    public static void render(int millisSinceLastTick){
        if(is3D&&enableCullFace) GL11.glDisable(GL11.GL_CULL_FACE);
        synchronized(gui){
            gui.render(millisSinceLastTick);
        }
        if(is3D&&enableCullFace) GL11.glEnable(GL11.GL_CULL_FACE);
        FPStracker.add(System.currentTimeMillis());
        while(FPStracker.get(0)<System.currentTimeMillis()-5_000){
            FPStracker.remove(0);
        }
    }
    public static long getFPS(){
        return FPStracker.size()/5;
    }
    public static double distance(MenuComponent o1, MenuComponent o2){
        return Math.sqrt(Math.pow((o1.x+o1.width/2)-(o2.x+o2.width/2), 2)+Math.pow((o1.y+o1.height/2)-(o2.y+o2.height/2), 2));
    }
    public static double distance(MenuComponent component, double x, double y) {
        return distance(component, new MenuComponentButton(x, y, 0, 0, "", false));
    }
    public static double distance(double x1, double y1, double x2, double y2) {
        return distance(new MenuComponentButton(x1, y1, 0, 0, "", false), new MenuComponentButton(x2, y2, 0, 0, "", false));
    }
    public static boolean isMouseWithinComponent(MenuComponent component){
        return isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), component.x, component.y, component.x+component.width, component.y+component.height);
    }
    public static boolean isMouseWithinComponent(MenuComponent component, MenuComponent... parents){
        double x = component.x;
        double y = component.y;
        for(MenuComponent c : parents){
            x+=c.x;
            y+=c.y;
        }
        return isClickWithinBounds(Mouse.getX(), Display.getHeight()-Mouse.getY(), x, y, x+component.width, y+component.height);
    }
    public static boolean isPointWithinComponent(double x, double y, MenuComponent component){
        return isClickWithinBounds(x, y, component.x, component.y, component.x+component.width, component.y+component.height);
    }
    public static double getValueBetweenTwoValues(double pos1, double val1, double pos2, double val2, double pos){
        if(pos1>pos2){
            return getValueBetweenTwoValues(pos2, val2, pos1, val1, pos);
        }
        double posDiff = pos2-pos1;
        double percent = pos/posDiff;
        double valDiff = val2-val1;
        return percent*valDiff+val1;
    }
    public static void drawLine(double x1, double y1, double x2, double y2, int width){
        Renderer2D.drawLine(x1, y1, x2, y2);
        for(int i = 0; i<width/2; i++){
            Renderer2D.drawLine(x1+i, y1, x2+i, y2);
            Renderer2D.drawLine(x1-i, y1, x2-i, y2);
            Renderer2D.drawLine(x1, y1+i, x2, y2+i);
            Renderer2D.drawLine(x1, y1-i, x2, y2-i);
        }
    }
    public static char toLetter(int i){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if(i>=1&&i<=26){
            return alphabet.toCharArray()[i-1];
        }
        return '-';
    }
    public static Galaxy load(String name){
        ObjectInputStream stream = null;
        try {
            File file = new File(Main.getAppdataRoot()+"\\saves\\"+name);
            if(!file.exists()){
                resetSave(name);
            }
            stream = new ObjectInputStream(new FileInputStream(file));
            Galaxy galaxy = new Galaxy(name);
            galaxy.starSystems.clear();
            int systems = stream.readInt();
            for(int i = 0; i<systems; i++){
                galaxy.starSystems.add((StarSystem) stream.readObject());
            }
            galaxy.system = galaxy.starSystems.get(0);
            galaxy.planet = galaxy.system.getPlanet("Shielgic");
            return galaxy;
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if(stream!=null)stream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void save(Galaxy galaxy){
        ObjectOutputStream stream = null;
        try {
            File file = new File(Main.getAppdataRoot()+"\\saves\\"+galaxy.name);
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeInt(galaxy.starSystems.size());
            for(StarSystem s : galaxy.starSystems){
                stream.writeObject(s);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void resetSave(String name){
        try{
            File from = new File(Main.getAppdataRoot()+"\\saves\\"+name);
            if(!from.exists()){
                save(new Galaxy(name));
                return;
            }
            Calendar.getInstance().getTime().toString();
            GregorianCalendar cal = new GregorianCalendar();
            SimpleDateFormat.getDateTimeInstance().format(cal.getTime());
            File to = new File((Main.getAppdataRoot()+"\\deleted save "+SimpleDateFormat.getDateTimeInstance().format(cal.getTime())+".dat").replaceAll(":", "-").replaceFirst("-", ":"));
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex){
            throw  new RuntimeException(ex);
        }
        save(new Galaxy(name));
    }
    public static void deleteSave(String name){
        File from = new File(Main.getAppdataRoot()+"\\saves\\"+name);
        if(from.exists()){
            from.delete();
        }
    }
    private static Random starRand = new Random();
    private static double starsPerPixel = 1/104.9088;
    private static int starCount;
    private static MenuMainBattle battle = new MenuMainBattle();
    private static int speedMultiplier = 10;
    private static int screenSize = 0;
    private static ArrayList<double[]> stars = new ArrayList<>();
    public static void tickMenuBackground(boolean tickBattle){
        if(screenSize!=Display.getWidth()*Display.getHeight()){
            screenSize = Display.getWidth()*Display.getHeight();
            starCount = (int) (starsPerPixel*screenSize);
            stars.clear();
            int minX = 0;
            int minY = 0;
            int maxX = Display.getWidth()+50;
            int maxY = Display.getHeight();
            for(int i = 0; i<starCount; i++){
                stars.add(new double[]{starRand.nextInt(maxX-minX)+minX, 
                                    starRand.nextInt((maxY-minY)*2)+minY,
                                    (starRand.nextInt(100)),
                                    (starRand.nextInt(20)+80),
                                    (starRand.nextInt(20)+80),
                                    (starRand.nextInt(20)+80)});
            }
        }
        if(tickBattle){
            for(int i = 0; i<speedMultiplier; i++){
                battle.tick();
            }
        }
        for(double[] val : stars){
            val[1]+=.1*speedMultiplier;
        }
        for(Iterator<double[]> it = stars.iterator(); it.hasNext();){
           double[] val = it.next();
           if(val[1]>Display.getHeight()) it.remove();
        }
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth();
        int maxY = Display.getHeight();
        while(stars.size()<starCount){
            stars.add(new double[]{starRand.nextInt(maxX-minX)+minX, 
                                starRand.nextInt(maxY-minY)-maxY,
                                (starRand.nextInt(100)),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80),
                                (starRand.nextInt(20)+80)});
        }
    }
    public static void renderMenuBackground(boolean renderBattle){
        int minX = 0;
        int minY = 0;
        int maxX = Display.getWidth();
        int maxY = Display.getHeight();
        GL11.glColor4f(0, 0, 0, 1);
        drawRect(minX, minY, maxX, maxY, 0);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glBegin(GL11.GL_POINTS);
        for(double[] star : stars){
            GL11.glColor4d(star[3]/100d, star[4]/100d, star[5]/100d, star[2]/100D);
            GL11.glVertex2d(star[0], star[1]);//Draw the stars!
            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glEnd();
        if(renderBattle){
            battle.render(0);
        }
        double I = 120;
        for(int i = 0; i<I; i++){
            GL11.glColor4d((I-i)/I, (I-i)/I, (I-i)/I, (I-i)/I);
            GL11.glPushMatrix();
            GL11.glTranslated(Display.getWidth()/2, 0, 0);
            GL11.glScaled(1+i/800d, 1+i/800d, 1);
            drawRect(-Display.getWidth()/2+100, 100, Display.getWidth()/2-100, 250, ImageStash.instance.getTexture("/textures/logo.png"));
            GL11.glPopMatrix();
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
}