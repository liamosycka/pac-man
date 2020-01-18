package com.example.pac_man;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Pacman {
    private int blockSize,posX,posY,posActual,sigPos,totalFrame,screenWidth;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Context contextPlayAct;

    public Pacman(int blockS,int screenWidth,Context context){
        this.blockSize=blockS;
        this.screenWidth=screenWidth;
        this.contextPlayAct=context;
        totalFrame=4;
        posX = 8 * blockSize;
        posY= 13 * blockSize;
        posActual = 3;
        sigPos = 4;
        crearBitmapImgPacman();
    }



    public void drawPacman(Canvas canvas, Context context, Paint paint, int currentPacmanFrame,Movement movement) {
        movement.movePacman();
        switch (this.posActual) {
            case (0):
                canvas.drawBitmap(pacmanUp[currentPacmanFrame], posX, posY, paint);
                break;
            case (1):
                canvas.drawBitmap(pacmanRight[currentPacmanFrame], posX, posY, paint);
                break;
            case(2):
                canvas.drawBitmap(pacmanDown[currentPacmanFrame], posX, posY, paint);
                break;
            case (3):
                canvas.drawBitmap(pacmanLeft[currentPacmanFrame], posX, posY, paint);
                break;

        }
        movement.updatePacman();
    }
    private void crearBitmapImgPacman() {
        int spriteSize = screenWidth/17;        // Size of Pacman & Ghost
        spriteSize = (spriteSize / 5) * 5;      // Keep it a multiple of 5

        // Add bitmap images of pacman facing right
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right1), spriteSize, spriteSize, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right2), spriteSize, spriteSize, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right3), spriteSize, spriteSize, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing down
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down1), spriteSize, spriteSize, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down2), spriteSize, spriteSize, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down3), spriteSize, spriteSize, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing left
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left1), spriteSize, spriteSize, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left2), spriteSize, spriteSize, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left3), spriteSize, spriteSize, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
        // Add bitmap images of pacman facing up
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_up1), spriteSize, spriteSize, false);
        pacmanUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_up2), spriteSize, spriteSize, false);
        pacmanUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_up3), spriteSize, spriteSize, false);
        pacmanUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_up), spriteSize, spriteSize, false);
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

    public int getTotalFrame() {
        return totalFrame;
    }

    public void setTotalFrame(int totalFrame) {
        this.totalFrame = totalFrame;
    }
}


