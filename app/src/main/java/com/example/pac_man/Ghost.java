package com.example.pac_man;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Ghost implements Runnable {
    private int tipoGhost;
    private int blockSize, screenWidth, posX, posY, posActual, sigPos, direccion;
    private Context context;
    private Pacman pacman;
    private Paint paint;
    private Canvas canvas;
    private boolean reset, vulnerable;
    private Bitmap bitGhost, bitAzul;

    public Ghost(int blockSize, int screenWidth, Context context, Pacman pacman, int tipo) {
        this.blockSize = blockSize;
        this.screenWidth = screenWidth;
        this.context = context;
        this.pacman = pacman;
        this.tipoGhost = tipo;
        reset = false;
        sigPos = 4;
        vulnerable = false;
        crearBitmaps();
    }

    public void run() {
        iniciarFantasma();
        //mientras que el fantasma no halla colisionado con el pacman
        while (!reset) {
            dormir(800);
            sigPos = sigMovimiento();
        }

    }

    public boolean getReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public void iniciarFantasma() {
        reset = false;
        sigPos = 4;
        switch (tipoGhost) {
            case 0:
                posX = 6 * blockSize;
                posY = 9 * blockSize;
                dormir(5000); // tiempo que espera para comenzar a moverse
                sigPos = 1;
                dormir(500);
                sigPos = 0;
                break;
            case 1:
                posX = 8 * blockSize;
                posY = 9 * blockSize;
                dormir(10000); // tiempo que espera para comenzar a moverse
                sigPos = 0;
                break;
            case 2:
                posX = 10 * blockSize;
                posY = 9 * blockSize;
                dormir(15000); // tiempo que espera para comenzar a moverse
                sigPos = 3;
                dormir(500);
                sigPos = 0;
                break;
            case 3:
                posX = 8 * blockSize;
                posY = 7 * blockSize;

                break;
        }
    }

    private int sigMovimiento() {
        Random rnd = new Random();
        return rnd.nextInt(4);
    }


    public void drawGhost(Canvas canvas, Context context, Paint paint, Movement movement) {
        movement.moveGhost(this);
        canvas.drawBitmap(bitGhost, posX, posY, paint);
        movement.chocarPacman(this);
        movement.updateGhost(this);

    }

    public void drawGhostAzul(Canvas canvas, Context context, Paint paint, Movement movement) {
        movement.moveGhost(this);
        canvas.drawBitmap(bitAzul, posX, posY, paint);
        movement.chocarPacman(this);
        movement.updateGhost(this);

    }

    public void dormir(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void crearBitmaps() {
        int spriteSize = screenWidth / 17;
        spriteSize = (spriteSize / 5) * 5;
        switch (tipoGhost) {
            case 0:
                bitGhost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.clyde), spriteSize, spriteSize, false);
                break;
            case 1:
                bitGhost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.pinky), spriteSize, spriteSize, false);
                break;
            case 2:
                bitGhost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.inky), spriteSize, spriteSize, false);
                break;
            case 3:
                bitGhost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.blinky), spriteSize, spriteSize, false);
                break;
        }
        bitAzul = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                context.getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
    }

    //Metodos de modificacion y observacion
    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public boolean getVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public int getTipoGhost() {
        return this.tipoGhost;
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

    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }
}

