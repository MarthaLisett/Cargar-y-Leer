/**
 * Juego
 *
 * Juego en el que Nena se mueve con WASD y si choca con fantasma se le suma
 * 1 punto al Score, pero si choca con 5 Juanitos se le quita una vida
 *
 * @author Martha Lisett Benavides Martínez A01280115
 * @version 1.0 
 * @date 2015/02/11
 */
 
import java.applet.Applet;
import java.applet.AudioClip; 
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class Juego extends Applet implements Runnable, KeyListener  {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maximo numero de personajes por alto
    private int iNumRandomVidas; // Número random vidas
    private Base basNena; // Objeto de la clase Base nena
    private Base basFantasma; // Objeto de la clase Base Fantasma
    private Base basJuanito; // Objeto de la clase Base Juanito
    private int iDireccionNena; //Direccion de nena
    private LinkedList <Base> linColeccionFantasmas;  // Lista de fantasmas
    private LinkedList <Base> linColeccionJuanitos;  // Lista de Juanitos
    private int iNumRandomFantasmas; // Número random de fantasmas 8-10
    private int iNumRandomJuanitos; // Número random de Juanitos 10-15
    private int iNumRandomVelocidadFantasmas; // Número random velocidad fantasmas
    private int iJuanitosColisionados; // Juanitos que han colisionado con nena
    private int iVelocidadJuanitos; // Velocidad de juanitos
    private Image imaGameOver;   // Imagen a proyectar en Applet
    private int iScore; // Puntaje
    private AudioClip aucSonidoNenaJuanito; // Objeto AudioClip sonido Base
    private AudioClip aucSonidoNenaFantasma; // Objeto AudioClip sonido Base
    
    //Guarda Posiciones en X y Y
    private int iPosX;
    private int iPosY;
    private int iPosX2;
    private int iPosY2;
    private int iPosX3;
    private int iPosY3;
    // Bolean para checar si pausó el Juego o no
    private boolean bPausa;
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // Hago el applet de un tamaño 800,600
        setSize(800, 600);
        bPausa = false;
        // Score inicial
        iScore = 0;
        // Numero de Juanitos colisionados con nena
        iJuanitosColisionados = 0;
        // Velocidad Juanitos
        iVelocidadJuanitos = 1;
        
        // Se crea el sonido de colision entre Nena y Juanito
	URL urlSonidoJuanito = this.getClass().getResource("monkey1.wav");
        aucSonidoNenaJuanito = getAudioClip (urlSonidoJuanito);
        
        // Se crea el sonido de colision entre Nena y el Fantasma
	URL urlSonidoFantasma = this.getClass().getResource("monkey2.wav");
        aucSonidoNenaFantasma = getAudioClip (urlSonidoFantasma);
   
 	// Se crea imagen de Nena
        URL urlImagenNena = this.getClass().getResource("chimpy.gif");
        
        // Se crea a Nena 
	basNena = new Base(getWidth() / 2, getHeight() / 2, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO, Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        // posicionar a nena al centro del applet
        basNena.setX(getWidth() / 2);
        basNena.setY(getHeight() / 2);

        // Número random para crear vidas
        iNumRandomVidas = (int) (Math.random() * 3 + 3);
        // Número random para crear a colección de Fantasmas
        iNumRandomFantasmas = (int) (Math.random() * 3 + 8);
        // Número random para crear a colección de Juanitos
        iNumRandomJuanitos = (int) (Math.random() * 6 + 10); 
        // Crear objeto de coleccion de Fantasmas
        linColeccionFantasmas = new LinkedList();
        
        // Se crean random de Fantasmas
        for(int iX = 0; iX < iNumRandomFantasmas; iX++){   
            // Número random para la velocidad de los fantasmas
            iNumRandomVelocidadFantasmas = (int) (Math.random() * 5 + 3);
            
            // Se posicionan a los fantasmas en la orilla izquierda del applet
            URL urlImagenFantasmas = this.getClass().getResource("fantasmita.gif");
            iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
            iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	    basFantasma = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO, Toolkit.getDefaultToolkit().getImage(urlImagenFantasmas));
            
            // Set Y de fantasma
            basFantasma.setY(basFantasma.getY() + basFantasma.getAlto());
            // Se le añade a la lista los fantasmas
            linColeccionFantasmas.add(basFantasma);
            // Velocidad de fantasmas
            basFantasma.setVelocidad(iNumRandomVelocidadFantasmas);
        }
        
         // Crear objeto de coleccion de Juanitos
        linColeccionJuanitos = new LinkedList();
        // Se crean random de Juanitos
        for(int iX = 0; iX < iNumRandomJuanitos; iX++){         
            // Se posicionan a Juanitos en la parte superior fuera del applet
            iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
            iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;  
            // Asignar imagen a Juanitos 
            URL urlImagenJuanito = this.getClass().getResource("juanito.gif");
            basJuanito = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO, Toolkit.getDefaultToolkit().getImage(urlImagenJuanito)); 
            // Se le anade a la lista de Juanitos
            linColeccionJuanitos.add(basJuanito);
        } 
        
        // Se carga la imagen de GameOver
        URL urlImagenGameOver = this.getClass().getResource("GameOver.jpg");
        imaGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
        
        /* se le añade la opcion al applet de ser escuchado por los eventos
           del teclado  */ 
	addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (true) {
            if(!bPausa){ //Para que funcione el Pausa
            actualiza();
            checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */ 
    public void actualiza(){
         //Dependiendo de la iDireccion de nena es hacia donde se mueve.
        switch(iDireccionNena) {
            case 1: { //se mueve hacia arriba
                basNena.setY(basNena.getY() - 1);
                break;    
            }
            case 2: { //se mueve hacia abajo
                basNena.setY(basNena.getY() + 1);
                break;    
            }
            case 3: { //se mueve hacia izquierda
                basNena.setX(basNena.getX() - 1);
                break;    
            }
            case 4: { //se mueve hacia derecha
                basNena.setX(basNena.getX() + 1);
                break;    	
            }
        }
        
        // La coleccion de Fantasmas se actualiza
        for(Base fantasmas : linColeccionFantasmas){
            fantasmas.setX(fantasmas.getX() + fantasmas.getVelocidad());
        }
        
        // La coleccion de Juanitos se actualiza
        for(Base juanitos : linColeccionJuanitos){
            juanitos.setY(juanitos.getY() + 1 * iVelocidadJuanitos);
        }
        
        // Actualiza vidas dependiendo de Juanitos colisionados
        if(iJuanitosColisionados == 5)
        {
            iNumRandomVidas--; // Disminuye vida
            iVelocidadJuanitos += 2; // Velocidad de Juanitos aumenta en 2
            iJuanitosColisionados = 0; // Se reinicializa a Juanitos Chocados en 0
        }

    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        switch(iDireccionNena){
            case 1: { 
                // si se mueve hacia arriba y esta pasando el limite 
                if(basNena.getY() < 0) { 
                    iDireccionNena = 0; // que no se mueva
                }
                break;    	
            }     
            case 2: { 
                // si se mueve hacia abajo y se esta saliendo del applet
                if(basNena.getY() + basNena.getAlto() > getHeight()) {
                    iDireccionNena = 0; // que no se mueva
                }
                break;    	
            } 
            case 3: { 
                // si se mueve hacia izquierda y se sale del applet
                if(basNena.getX() < 0) { 
                    iDireccionNena = 0; // que no se mueva
                }
                break;    	
            }    
            case 4: { 
                // si se mueve hacia derecha y se esta saliendo del applet
                if(basNena.getX() + basNena.getAncho() > getWidth()) { 
                    iDireccionNena = 0; // que no se mueva
                }
                break;    	
            }			
        }
        
        // Fantasmas colisionando con applet
        for(Object fantasmas : linColeccionFantasmas){
            Base basFantasma = (Base)fantasmas;
            // Se está pasando el limite
            if((basFantasma.getX() + basFantasma.getAncho() > getWidth())) { 
                // Set x del Fantasma
                basFantasma.setX((int) (Math.random() * (getWidth() / 2)) * (-1));    
                // Set y del Fantasma
                basFantasma.setY((int) (Math.random() *(getHeight() / 2)) + 
                                        basFantasma.getAlto()); 
            }
            // Si colisiona el fantasma con Nena
            if(basFantasma.colisiona(basNena))
            {
                iScore++; // Se aumenta el puntaje en 1
                // Se regresa para que vuelva a salir de la izquierda
                basFantasma.setX((int) (Math.random() * (getWidth() / 2)) * (-1));    
                // Se se asigna el nuevo valor de y
                basFantasma.setY((int) (Math.random() *(getHeight() / 2)) + 
                                                    basFantasma.getAlto()); 
                aucSonidoNenaFantasma.play(); // Se escucha el sonido
            }
        }
        
         // Si colisionan los Juanitos con el applet
        for(Base juanitos : linColeccionJuanitos){
            Base basJuanito = (Base)juanitos;
            if((basJuanito.getY()+ basJuanito.getAlto()> getHeight())){
                // Se le asigna un nuevo valor de X
                iPosX2 = (int) (Math.random() * (getWidth() - 70)) ; 
                // Se regresa afuera superior
                iPosY2 = (int) (Math.random() * (getHeight() / 2) + 0) * (-1);
                // Asignar las posiciones x y y
                juanitos.setX(iPosX2);   
                juanitos.setY(iPosY2);   
            }
            // Si colisiona un Juanito con nena
            if(basJuanito.colisiona(basNena))
            {
                iJuanitosColisionados++; // Juanitos colisionados aumenta
                aucSonidoNenaJuanito. play(); // Se escucha el sonido
                //Mandar a los Juanitos a su lugar original
                // Posicion X
                iPosX3 = (int) (Math.random() * (getWidth())) ;    
                // Posicion Y
                iPosY3 = (int) (Math.random() * (getHeight() / 2) + 0) * (-1);   
                juanitos.setX(iPosX3);  
                juanitos.setY(iPosY3);   
            }
        }
}
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update(Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param g es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics g) {
        // Si todavía hay vidas
        if(iNumRandomVidas > 0){
            // Si la imagen ya se cargo
            if (linColeccionFantasmas != null & basNena != null & linColeccionJuanitos != null) {
                    /* Dibuja la imagen de cada uno de los fasntasmas 
                      en la posicion actualizada */
                    for(Base fantasmas : linColeccionFantasmas){
                        Base basFantasma = (Base)fantasmas;
                        g.drawImage(basFantasma.getImagen(), 
                                basFantasma.getX(), basFantasma.getY(), this);
                    }
                    for(Base juanitos : linColeccionJuanitos){
                            Base basJuanito = (Base)juanitos;
                            g.drawImage(basJuanito.getImagen(), basJuanito.getX(),
                                basJuanito.getY(), this);
                        }
                    //Dibuja la imagen de Nena en la posicion actualizada
                    g.drawImage(basNena.getImagen(), basNena.getX(), basNena.getY(), this);
                    // Muestra los resultados del juego
                    g.setColor(Color.red);
                    g.drawString("Vidas : " + iNumRandomVidas, 40, 525);
                    g.drawString("Score : " + iScore, 40, 550);

            } // sino se ha cargado se dibuja un mensaje 
            else {
                    //Da un mensaje mientras se carga el dibujo	
                    g.drawString("No se cargo la imagen..", 20, 20);
            }
        }
        else
        {
            g.drawImage(imaGameOver, (getWidth() / 2) - (112), (getHeight() / 2) 
                                                                 - (112), this);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //Si presiono flecha para arriba
        if(ke.getKeyCode() == KeyEvent.VK_W) {  
            iDireccionNena = 1;
        }
        // si presiono flecha para abajo
        else if(ke.getKeyCode() == KeyEvent.VK_S) {    
            iDireccionNena = 2;
        }
        // si presiono flecha para la izquierda
        else if(ke.getKeyCode() == KeyEvent.VK_A) {    
            iDireccionNena = 3;
        }
        // si presiono flecha para derecha
        else if(ke.getKeyCode() == KeyEvent.VK_D) {    
            iDireccionNena = 4;
        }
        else if (ke.getKeyCode() == KeyEvent.VK_P){ 
            bPausa=!bPausa; // Si se le da el clic activar el boolean
        }
        else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE){ 
            iNumRandomVidas=0; // Que se acabe el juego
        }
    }
}