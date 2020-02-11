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

public class DrawingView extends SurfaceView implements Runnable {

    final short map[][] = new short[][]{
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
            {41, 22, 0, 21, 0, 0, 0, 17, 1026, 20, 0, 0, 0, 21, 0, 19, 44}, //1026 spawn pacman
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0},
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 28},
    };
    boolean canDraw = true;
    Canvas canvas;
    final int cantGhosts = 4;
    SurfaceHolder surfaceHolder;
    private MediaPlayer inicio, mover;
    private Paint paint;
    private int blockSize, screenWidth, score, timerFruta = 0;
    private int totalFrame = 4;             // cantidad de frames para el pacman por movimiento
    private int currentPacmanFrame = 0;     // frame inicial para el pacman
    private Pacman pacman;
    private Thread hiloGhost, thread;
    private long frameTicker;
    private Movement movement;
    private float x1, x2, y1, y2;           //variables para calcular la direccion del swipe
    private Context context;
    private Ghost[] arrGhosts;
    private Bitmap fruta, pacmanVida;


    public DrawingView(Context context) {
        super(context);
        arrGhosts = new Ghost[cantGhosts];
        mover= MediaPlayer.create(context,R.raw.pacmanwaka);
        mover.setVolume(100,100);
        inicio = MediaPlayer.create(context, R.raw.songinicio);
        inicio.setVolume(100, 100);
        inicio.start();
        paint = new Paint();
        this.context = context;
        frameTicker = 1000 / totalFrame; //para saber cuando se actualizo por ultima vez el frame
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        blockSize = screenWidth / 17;
        blockSize = (blockSize / 5) * 5;
        surfaceHolder = getHolder();
        pacman = new Pacman(blockSize, screenWidth, context);

        iniciarFantasmas();
        movement = new Movement(map, blockSize, pacman, arrGhosts,context);
        contarPellets(map, blockSize);
        crearBitmaps();
        thread = new Thread(this);
        thread.start();
    }

    public void crearBitmaps() {
        int spriteSize = screenWidth / 17;
        spriteSize = (spriteSize / 5) * 5;
        fruta = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.cereza), spriteSize, spriteSize, false);
        pacmanVida = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pacman_right1), spriteSize, spriteSize, false);

    }
    @Override
    public void run() {
        while (canDraw) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            canvas = surfaceHolder.lockCanvas();

            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                drawPellets(canvas, map, paint, blockSize);
                drawPowerUp(canvas, map, paint, blockSize);
                if(pacman.getPacmanInicio()) {
                   pacman.pacmanInicio(canvas,context,paint);

                }else{
                    pacman.drawPacman(canvas, context, paint, currentPacmanFrame, movement);
                }
                dibujarVidas();
                score = Globals.getInstance().getScore();
                paint.setTextSize(60f);
                canvas.drawText("Score: " + score, 7 * blockSize, 22 * blockSize, paint);
                //verifica si el pacman choco un fantasma
                if (Globals.getInstance().getReiniciarJuego()) {
                    mover.pause();
                    for (int i = 0; i < arrGhosts.length; i++) {
                        arrGhosts[i].setReset(true);
                        iniciarFantasmas();
                        pacman.setPacmanInicio(true);
                    }
                    pacman.setVida(pacman.getVida() - 1);
                    if (pacman.getVida() == 0) {
                        //game over ver que hacer
                    }
                    Globals.getInstance().setReiniciarJuego(false);
                } else {
                    //vemos si el pacman tiene el powerUp activo
                    if (pacman.getPowerUp()) {
                        for (int i = 0; i < arrGhosts.length; i++) {
                            if (arrGhosts[i].getVulnerable()) {
                                arrGhosts[i].drawGhostAzul(canvas, context, paint, movement);
                            } else {
                                arrGhosts[i].drawGhost(canvas, context, paint, movement);
                            }
                            //verifica si el fantasma fue comido y hay que resetearlo
                            if (arrGhosts[i].getReset()) {
                                arrGhosts[i] = new Ghost(blockSize, screenWidth, context, pacman, i);
                                hiloGhost = new Thread(arrGhosts[i]);
                                hiloGhost.start();
                            }
                        }
                    } else {
                        for (int i = 0; i < arrGhosts.length; i++) {
                            arrGhosts[i].drawGhost(canvas, context, paint, movement);
                        }
                    }
                }
            }
            if (Globals.getInstance().getFrutaActiva()) {
                dibujarFruta();
            } else {
                timerFruta++;
            }
            if (timerFruta == 400) {
                timerFruta = 0;
                map[13][8] = 514;     //se le asigna el valor correspondiente a que la fruta esta activa
                Globals.getInstance().setFrutaActiva(true);
            }
            updateFrame(System.currentTimeMillis());
            //se resolvio hacer 1 solo metodo verificarSituacion que haga ambas

            verificarEstado();

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    private void dibujarFruta() {
        int x = 8 * blockSize;
        int y = 13 * blockSize;
        canvas.drawBitmap(fruta, x, y, paint);
    }

    private void iniciarFantasmas() {
        for (int i = 0; i < arrGhosts.length; i++) {
            Ghost ghost = new Ghost(blockSize, screenWidth, context, pacman, i);
            arrGhosts[i] = ghost;
        }
        for (int i = 0; i < arrGhosts.length; i++) {
            hiloGhost = new Thread(arrGhosts[i]);
            hiloGhost.start();
        }
    }



    private void updateFrame(long gameTime) {
        // verifica si ha pasado el tiempo suficiente para ir al siguiente frame
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;
            currentPacmanFrame++;
            // si ya se visitaron todos los frames posibles, se vuelve al inicio
            if (currentPacmanFrame >= totalFrame) {
                currentPacmanFrame = 0;
            }
        }
    }

    public void drawMap(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.5f);
        int x, y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                if ((map[i][j] & 1) != 0) // dibuja una pared a la izquierda
                    canvas.drawLine(x, y, x, y + blockSize - 1, paint);

                if ((map[i][j] & 2) != 0) // dibuja una pared arriba
                    canvas.drawLine(x, y, x + blockSize - 1, y, paint);

                if ((map[i][j] & 4) != 0) // dibuja una pared a la derecha
                    canvas.drawLine(
                            x + blockSize, y, x + blockSize, y + blockSize - 1, paint);
                if ((map[i][j] & 8) != 0) // dibuja una pared abajo
                    canvas.drawLine(
                            x, y + blockSize, x + blockSize - 1, y + blockSize, paint);
                if ((map[i][j] & 256) != 0) {//dibuja linea blanca de entrada a caja de ghosts
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
        pacman.setPacmanInicio(false);
        if(!inicio.isPlaying()){
            mover.start();
        }
        switch (event.getAction()) {
            /*indica que ha comenzado una accion en el tactil,
             * indicando la posicion inicial donde se origino*/
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            /*indica que ha finalizado la accion en el tactil,
             * indicando la posicion final donde finalizo*/
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();
                break;
            }
        }
        return true;
    }

    /*utilizando los valores de posicion inicial y final de la accion,
     * se determina la direccion del swipe */
    private void calculateSwipeDirection() {
        float xDiff = (x2 - x1);
        float yDiff = (y2 - y1);

        /* Direcciones:
         0 : Arriba
         1: Derecha
         2: Abajo
         3: Izquierda
         4: Detenerse, direccion invalida/*
         */
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            //significa que el swipe fue vertical
            if (yDiff < 0) {
                //el swipe fue para arriba
                pacman.setSigPos(0);
            } else if (yDiff > 0) {
                //el swipe fue para abajo
                pacman.setSigPos(2);
            }
        } else {
            //significa que el swipe fue horizontal
            if (xDiff < 0) {
                //el swipe fue para la izquierda
                pacman.setSigPos(3);

            } else if (xDiff > 0) {
                //el swipe fue para la derecha
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
                /*Se dibujan en las posiciones de la matriz que tengan un bit asertado en 16*/
                if ((currentMap[i][j] & 16) != 0) {
                    canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 10, paint);
                }
            }
        }
    }

    public static void drawPowerUp(Canvas canvas, short[][] currentMap, Paint paint, int blockSize) {
        int x = 0, y = 3;
        paint.setColor(Color.YELLOW);
        /*Las posiciones de los power up estan fijas, por lo tanto en este metodo
         * se ve si dichas posiciones tienen asertado un bit en 32, lo que indica
         * un power up disponible*/
        if ((currentMap[y][x] & 32) != 0) {
            y *= blockSize;
            canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 3, paint);
        }
        x = 16;
        y = 3;
        if ((currentMap[y][x] & 32) != 0) {
            x = 16 * blockSize;
            y *= blockSize;
            canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 3, paint);
        }
        y = 13;
        x = 0;
        if ((currentMap[y][x] & 32) != 0) {
            y *= blockSize;
            canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 3, paint);
        }
        y = 13;
        x = 16;

        if ((currentMap[y][x] & 32) != 0) {
            y *= blockSize;
            x *= blockSize;
            canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 3, paint);
        }
    }

    public static void contarPellets(short[][] currentMap, int blockSize) {
        float x, y;
        int cantidadPellets = 0;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;

                if ((currentMap[i][j] & 16) != 0) {
                    cantidadPellets++;
                }
            }
        }
        cantidadPellets += 4;
        Globals.getInstance().setCantidadPellets(cantidadPellets);
    }

    private void dibujarVidas() {
        int vida = pacman.getVida();
        for (int i = 0; i < vida; i++) {
            int x = 19 * blockSize;
            int y = i * blockSize;
            canvas.drawBitmap(pacmanVida, y, x, paint);
        }
    }

    private void verificarEstado(){
        //verifica si se perdio la partida
        if (pacman.getVida() == 0) {
            canDraw = false;
            mover.stop();
            int score = Globals.getInstance().getScore();
            Intent derrota = new Intent(context, Derrota.class);
            derrota.putExtra("score",score);
            Globals.getInstance().setFrutaActiva(false);
            context.startActivity(derrota);
            ((PlayActivity)context).finish();
        }
        //verifica si se gano la partida
        int cantidadPellets = Globals.getInstance().getCantidadPellets();
        int score = Globals.getInstance().getScore();
        if (cantidadPellets == 0) {
            canDraw = false;
            mover.stop();
            Globals.getInstance().setFrutaActiva(false);
            Intent victoria = new Intent(context, Victoria.class);
            victoria.putExtra("score",score);
            context.startActivity(victoria);
            ((PlayActivity)context).finish();
        }
    }

        /*public void pause(){
        canDraw = false;
        thread = null;
    }

    public void resume(){
        if(thread==null) {
            thread = new Thread(this);
        }
        thread.start();
        canDraw=true;
    }*/

}