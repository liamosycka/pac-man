package com.example.pac_man;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class DrawingView extends SurfaceView implements Runnable {
    final short leveldata1[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {19, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 22},
            {37, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 37},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 258, 24, 26, 22, 0, 21, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 3, 2, 0, 2, 6, 17, 26, 16, 26, 26, 26},
            {0, 0, 0, 21, 0, 21, 1, 0, 0, 0, 4, 21, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 9, 8, 8, 8, 12, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 18, 26, 18, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {41, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 44}, // "2" in this line is for
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0}, // pacman's spawn
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 28},
    };
    Thread thread=null;
    boolean canDraw=true;
    Bitmap map,pacmanVida;
    Canvas canvas;
    SurfaceHolder surfaceHolder;
    private MediaPlayer inicio,mover;
    private Paint paint;
    private int blockSize,screenWidth;
    private int totalFrame = 4;             // Total amount of frames fo each direction
    private int currentPacmanFrame = 0;     // Current Pacman frame to draw
    private int viewDirection = 2;
    private int score;
    private Pacman pacman;
    private Thread hiloGhost;
    private long frameTicker;
    private Movement movement;
    private float x1, x2, y1, y2;           // Initial/Final positions of swipe
    private Context context;
    private Clyde clyde;
    private Pinky pinky;
    private Ghost[] arrGhosts;

    public DrawingView(Context context,int x, int y ){
        super(context);
        arrGhosts=new Ghost[4];
        mover = MediaPlayer.create(context,R.raw.pacmanwaka);
        mover.setVolume(100,100);
        inicio = MediaPlayer.create(context,R.raw.songinicio);
        inicio.setVolume(100,100);
        inicio.start();
        paint=new Paint();
        this.context=context;
        frameTicker = 1000/totalFrame;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        blockSize = screenWidth/17;
        blockSize = (blockSize / 5) * 5;
        surfaceHolder = getHolder();
        pacman=new Pacman(blockSize,screenWidth,context);
        iniciarFantasmas();
        movement=new Movement(leveldata1,blockSize,pacman,arrGhosts);
        contarPellets(leveldata1,blockSize);



        thread=new Thread(this);
        thread.start();



    }

    @Override
    public void run(){
        while(canDraw){

            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            canvas= surfaceHolder.lockCanvas();

            if(canvas!=null){
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                /*Hay que ver si se comio una pastilla, en caso positivo, hay que actualizar
                * el mapa para que no se vuelva a dibujar esa pastilla.*/
                if(movement.needMapRefresh()){
                    movement.updateMap();
                }
                if(movement.verifPowerUp()){
                    movement.actualizarMapaPowerUp();
                }

                int pellets = Globals.getInstance().getCantidadPellets();
                drawPellets(canvas,leveldata1,paint,blockSize);
                drawPowerUp(canvas,leveldata1,paint,blockSize);
                pacman.drawPacman(canvas,context,paint,currentPacmanFrame,movement);
                dibujarVidas();
                score = Globals.getInstance().getScore();
                paint.setTextSize(60f);
                canvas.drawText("Score: " + score,8 * blockSize,19 * blockSize,paint);
                if(Globals.getInstance().getReiniciarJuego()){
                    for(int i=0;i<arrGhosts.length;i++){
                        arrGhosts[i].setReset(true);
                        iniciarFantasmas();
                    }
                    pacman.setVida(pacman.getVida()-1);
                    if(pacman.getVida()==0){
                        //game over
                    }
                    Globals.getInstance().setReiniciarJuego(false);
                }else{
                    //vemos si el pacman tiene el powerUp activo
                    if(pacman.getPowerUp()){
                        for(int i=0;i<arrGhosts.length;i++){
                            if(arrGhosts[i].getVulnerable()) {
                                arrGhosts[i].drawGhostAzul(canvas, context, paint, movement);
                            }else{
                                arrGhosts[i].drawGhost(canvas,context,paint,movement);

                            }
                            if(arrGhosts[i].getReset()){

                                arrGhosts[i]=new Ghost(blockSize,screenWidth,context,pacman,leveldata1,i);

                                hiloGhost=new Thread(arrGhosts[i]);
                                hiloGhost.start();
                            }


                        }
                    }else{
                        for(int i=0;i<arrGhosts.length;i++){
                            arrGhosts[i].drawGhost(canvas,context,paint,movement);
                        }

                    }

                }





            }
            updateFrame(System.currentTimeMillis());
            verificarVictoria();
            verificarDerrota();

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }
    private void iniciarFantasmas(){
        for(int i=0;i<arrGhosts.length;i++){
            Ghost ghost=new Ghost(blockSize,screenWidth,context,pacman,leveldata1,i);

            arrGhosts[i]=ghost;
        }
        for(int i=0;i<arrGhosts.length;i++){
            hiloGhost = new Thread(arrGhosts[i]);
            hiloGhost.start();
        }
    }

    public void onResume(){

        thread=new Thread(this);
        thread.start();
    }

    private void updateFrame(long gameTime) {

        // If enough time has passed go to next frame
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;

            // Increment the frame
            currentPacmanFrame++;
            // Loop back the frame when you have gone through all the frames
            if (currentPacmanFrame >= totalFrame) {
                currentPacmanFrame = 0;
            }
        }

    }

    public void drawMap(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.5f);
        int x;
        int y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                if ((leveldata1[i][j] & 1) != 0) // draws left
                    canvas.drawLine(x, y, x, y + blockSize - 1, paint);

                if ((leveldata1[i][j] & 2) != 0) // draws top
                    canvas.drawLine(x, y, x + blockSize - 1, y, paint);

                if ((leveldata1[i][j] & 4) != 0) // draws right
                    canvas.drawLine(
                            x + blockSize, y, x + blockSize, y + blockSize - 1, paint);
                if ((leveldata1[i][j] & 8) != 0) // draws bottom
                    canvas.drawLine(
                            x, y + blockSize, x + blockSize - 1, y + blockSize, paint);
                if ((leveldata1[i][j] & 256) != 0){//dibuja linea blanca de entrada a caja de ghosts
                    paint.setColor(Color.WHITE);
                canvas.drawLine(x, y + blockSize, x + blockSize - 1, y + blockSize, paint);
                paint.setColor(Color.BLUE);
            }
            }
        }
        paint.setColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();

                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();

                break;
            }
        }
        return true;
    }

    // Calculates which direction the user swipes
    // based on calculating the differences in
    // initial position vs final position of the swipe
    private void calculateSwipeDirection() {
        float xDiff = (x2 - x1);
        float yDiff = (y2 - y1);

        // Directions
        // 0 means going up
        // 1 means going right
        // 2 means going down
        // 3 means going left
        // 4 means stop moving, look at move function

        // Checks which axis has the greater distance
        // in order to see which direction the swipe is
        // going to be (buffering of direction)
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                pacman.setSigPos(0);
            } else if (yDiff > 0) {
                pacman.setSigPos(2);
            }
        } else {
            if (xDiff < 0) {
                pacman.setSigPos(3);

            } else if (xDiff > 0) {
                pacman.setSigPos(1);

            }
        }
    }
    public static void drawPellets(Canvas canvas, short[][] currentMap, Paint paint, int blockSize) {
        float x, y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                // Draws pellet in the middle of a block
                /*Se verifica con 16 ya que las unicas posiciones que daran 0 en esta evaluacion
                * son los bloques que tienen el valor 2 ( spawn del pacman) y 0 en la matriz.*/
                if ((currentMap[i][j] & 16) != 0) {
                    canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 10, paint);
                }
            }
        }
    }
    public static void drawPowerUp(Canvas canvas,short [][] currentMap,Paint paint,int blockSize){
        int x,y;
        paint.setColor(Color.YELLOW);
        x=0;
        y=3;
        if((currentMap[y][x]&32)!=0){
            y*=blockSize;
            canvas.drawCircle(x+blockSize/2,y+blockSize/2,blockSize/3,paint);
        }
        x=16;
        y=3;
        if((currentMap[y][x]&32)!=0){
            x=16*blockSize;
            y*=blockSize;
            canvas.drawCircle(x+blockSize/2,y+blockSize/2,blockSize/3,paint);
        }
        y=13;
        x=0;
        if((currentMap[y][x]&32)!=0){
            y*=blockSize;
            canvas.drawCircle(x+blockSize/2,y+blockSize/2,blockSize/3,paint);
        }

        y=13;


        x=16;

        if((currentMap[y][x]&32)!=0){
            y*=blockSize;
            x*=blockSize;
            canvas.drawCircle(x+blockSize/2,y+blockSize/2,blockSize/3,paint);
        }
    }

    public static void contarPellets(short[][] currentMap,int blockSize) {
        float x, y;
        int cantidadPellets=0;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;

                if ((currentMap[i][j] & 16) != 0) {
                    cantidadPellets++;
                }
            }
        }
        cantidadPellets+=4;
        Globals.getInstance().setCantidadPellets(cantidadPellets);
    }

    private void dibujarVidas(){
        int vida = pacman.getVida();
        int spriteSize = screenWidth/17;
        spriteSize = (spriteSize / 5) * 5;
        pacmanVida = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pacman_right1), spriteSize, spriteSize, false);


        for (int i=0;i<vida;i++){
            int x = 19 * blockSize;
            int y = i * blockSize;
            canvas.drawBitmap(pacmanVida,y,x,paint);
        }
    }

    private void verificarVictoria(){
        int cantidadPellets = Globals.getInstance().getCantidadPellets();
        if(cantidadPellets==0){
            Intent victoria = new Intent(context,MainActivity.class);
           context.startActivity(victoria);

        }
    }

    private void verificarDerrota(){
        if(pacman.getVida()==0){
            Intent derrota = new Intent(context,MainActivity.class);
            context.startActivity(derrota);

        }
    }
}

