package com.example.pac_man;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.Random;

public class Clyde implements Runnable{
    private int blockSize,screenWidth,posX,posY,posActual,sigPos,direccion;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private int[] patronMov;
    private Pacman pacman;
    private Paint paint;
    private Canvas canvas;
    private boolean comenzar,reiniciar;
    private Bitmap bitClyde,bitClydeAzul;
    private short[][] map;
    public Clyde(int blockSize, int screenWidth, Context context ,Pacman pacman,short[][] map){
        this.blockSize=blockSize;
        this.screenWidth=screenWidth;
        this.context=context;
        this.map=map;
        this.pacman=pacman;
        comenzar=false;
        reiniciar=false;
        posX=6*blockSize;
        posY=9*blockSize;
        posActual=0;
        sigPos=0;

        crearBitmapsClyde();
    }
    public void run(){

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sigPos = 1;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sigPos = 0;
            while (true) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sigPos = sigMovimiento();
        }
    }
    private int sigMovimiento(){
        Random rnd=new Random();
        return rnd.nextInt(4);
    }


    public void drawClyde (Canvas canvas,Context context,Paint paint) {
        moveClyde();
        canvas.drawBitmap(bitClyde, posX, posY, paint);
        chocarPacman();
        updateClyde();

    }
    public void drawClydeAzul (Canvas canvas,Context context,Paint paint) {
        moveClyde();
        canvas.drawBitmap(bitClydeAzul, posX, posY, paint);
        chocarPacman();
        updateClyde();

    }

    public void chocarPacman() {
        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize)) && !pacman.getPowerUp()) {
            pacman.muerte();
            this.posX = 6 * blockSize;
            this.posY = 9 * blockSize;
            Globals.getInstance().setReiniciarJuego(true);
            posActual = 0;

        }
        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize)) && pacman.getPowerUp()) {
            //el pacman tiene el power up
            this.posX = 6 * blockSize;
            this.posY = 9 * blockSize;
            posActual = 0;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private void moveClyde() {
        int ch;

        if ((posX % blockSize == 0) && (posY % blockSize == 0)) {
            if (posX >= blockSize * 17) {
                posX = 0;

            }

            // Is used to find the number in the level array in order to
            // check wall placement, pellet placement, and candy placement
            ch = map[posY / blockSize][posX / blockSize];


            if (!((sigPos == 3 && (ch & 1) != 0) ||
                    (sigPos == 1 && (ch & 4) != 0) ||
                    (sigPos == 0 && (ch & 2) != 0) ||
                    (sigPos == 2 && (ch & 8) != 0))) {
                posActual=sigPos;
                direccion=sigPos;
            }

            // Checks for wall collisions
            /*Aqui se verifica si en la direccion que el pacman se esta por mover, HAY una pared, ya que,
             * al ser por ej swipeDir==1 ( el pacman se mueve a la derecha) verifica si la posicion actual
             * de la matriz ( pos donde esta el pacman) se le ha dibujado una pared a la derecha , en caso afirmativo
             * se le asignara a swipeDir=4, que significa que no se mueva ( no hay caso en el switch para 4, en drawPacman) */
            if ((direccion == 3 && (ch & 1) != 0) ||
                    (direccion == 1 && (ch & 4) != 0) ||
                    (direccion == 0 && (ch & 2) != 0) ||
                    (direccion == 2 && (ch & 8) != 0) ||
                    (direccion==2&&(ch&256)!=0)) {
                direccion=4;
            }
        }
        if (posX < 0) {
            posX= blockSize * 17;
        }
    }
    private void updateClyde(){
        if (direccion == 0) {
            this.setPosY(posY + -blockSize / 20);

        } else if (direccion == 1) {
            this.setPosX(posX + blockSize / 20);
        } else if (direccion == 2) {
            this.setPosY(posY + blockSize / 20);
        } else if (direccion == 3) {
            this.setPosX(posX + -blockSize / 20);
        }
    }
    private void crearBitmapsClyde(){
        int spriteSize = screenWidth/17;        // Size of Pacman & Ghost
        spriteSize = (spriteSize / 5) * 5;      // Keep it a multiple of 5
        bitClyde= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.clyde), spriteSize, spriteSize, false);

        bitClydeAzul= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
    }
    public void setComenzar(boolean comenzar){
        this.comenzar=comenzar;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean getReiniciar(){
        return this.reiniciar;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosActual() {
        return posActual;
    }

    public void setPosActual(int posActual) {
        this.posActual = posActual;
    }

    public int getSigPos() {
        return sigPos;
    }

    public void setSigPos(int sigPos) {
        this.sigPos = sigPos;
    }

    public int[] getPatronMov() {
        return patronMov;
    }

    public void setPatronMov(int[] patronMov) {
        this.patronMov = patronMov;
    }

    public Pacman getPacman() {
        return pacman;
    }

    public void setPacman(Pacman pacman) {
        this.pacman = pacman;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
