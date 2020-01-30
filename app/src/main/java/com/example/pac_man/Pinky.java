package com.example.pac_man;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.Random;

public class Pinky implements Runnable{
    private int blockSize,screenWidth,posX,posY,posActual,sigPos,direccion;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private int[] patronMov;
    private Pacman pacman;
    private Paint paint;
    private Canvas canvas;
    private boolean comenzar;
    private Bitmap bitPinkyUp,bitPinkyLeft,bitPinkyDown,bitPinkyRight;
    private short[][] map;
    public Pinky(int blockSize, int screenWidth, Context context ,Pacman pacman,short[][] map){
        this.blockSize=blockSize;
        this.screenWidth=screenWidth;
        this.context=context;
        this.map=map;
        comenzar=false;
        posX=8*blockSize;
        posY=9*blockSize;
        posActual=0;
        sigPos=0;

        crearBitmapPinky();
    }
    public void run(){

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sigPos=1;

        while(true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sigPos=sigMovimiento();

        }
    }
    private int sigMovimiento(){
        Random rnd=new Random();
        return rnd.nextInt(4);
    }


    public void drawPinky (Canvas canvas,Context context,Paint paint) {
        movePinky();
        switch (this.posActual) {
            case (0):
                canvas.drawBitmap(bitPinkyUp, posX, posY, paint);
                break;
            case (1):
                canvas.drawBitmap(bitPinkyRight, posX, posY, paint);
                break;
            case (2):
                canvas.drawBitmap(bitPinkyDown, posX, posY, paint);
                break;
            case (3):
                canvas.drawBitmap(bitPinkyLeft, posX, posY, paint);
                break;

        }
        updatePinky();
    }
    public void drawPinkyAzul (Canvas canvas,Context context,Paint paint) {
        movePinky();
        switch (this.posActual) {
            case (0):
                canvas.drawBitmap(bitPinkyUp, posX, posY, paint);
                break;
            case (1):
                canvas.drawBitmap(bitPinkyRight, posX, posY, paint);
                break;
            case (2):
                canvas.drawBitmap(bitPinkyDown, posX, posY, paint);
                break;
            case (3):
                canvas.drawBitmap(bitPinkyLeft, posX, posY, paint);
                break;

        }

        updatePinky();

    }
    private void movePinky() {
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
    private void updatePinky(){
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
    private void crearBitmapPinky(){
        int spriteSize = screenWidth/17;        // Size of Pacman & Ghost
        spriteSize = (spriteSize / 5) * 5;      // Keep it a multiple of 5
        bitPinkyUp= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pinky), spriteSize, spriteSize, false);
        bitPinkyLeft= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pinky), spriteSize, spriteSize, false);
        bitPinkyDown= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pinky), spriteSize, spriteSize, false);
        bitPinkyRight= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.pinky), spriteSize, spriteSize, false);
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