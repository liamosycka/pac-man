package com.example.pac_man;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Pacman {
    private int blockSize,posX,posY,posActual,sigPos,totalFrame,screenWidth,vida;
    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Context contextPlayAct;
    private boolean powerUp,inicio;

    public Pacman(int blockS,int screenWidth,Context context){
        this.blockSize=blockS;
        this.powerUp=false;
        this.inicio=true;
        this.vida = 3;
        this.screenWidth=screenWidth;
        this.contextPlayAct=context;
        totalFrame=4;
        posX = 8 * blockSize; /*Como la pantalla se dividió en 17 bloques del mismo tamaño, para situarse
                                en ella hay que simplemente multiplicar al X por ( tamBloque * cantColumnas)
                                   y al Y por ( tamBloque*cantFilas) */
        posY= 13 * blockSize;
                            /*De esta manera el pacman comienza en la fila 13, columna 8 */
        posActual = 3;        //esto es para que el pacman comienze mirando a la izquierda
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
        /*Como en el metodo movement.movePacman() para realizar alguna accion se requiere que posX y posY sean
        * multiplos de 60, es decir, sea un bloque, una posicion de la matriz, durante la transicion de un bloque
        * a otro no deben realizarse cambios en la direccion que mira el pacman, para evitar eso, al haber 4 frames
        * de movimiento se va incrementando/disminuyendo 1/4 de 60 a posX y posY, de esta manera se evitara que estas 2
        * variables sean multiplos de 60, lo que permitira la transicion de los 4 frames sin cambiar de direccion la vista
        * del pacman.*/
        movement.updatePacman();
    }

    public void pacmanInicio(Canvas canvas, Context context, Paint paint){
        canvas.drawBitmap(pacmanLeft[2],posX,posY,paint);
    }

    private void crearBitmapImgPacman() {
        int spriteSize = screenWidth/17;        // size del pacman
        spriteSize = (spriteSize / 5) * 5;

        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right1), spriteSize, spriteSize, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right2), spriteSize, spriteSize, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right3), spriteSize, spriteSize, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down1), spriteSize, spriteSize, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down2), spriteSize, spriteSize, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down3), spriteSize, spriteSize, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left1), spriteSize, spriteSize, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left2), spriteSize, spriteSize, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left3), spriteSize, spriteSize, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                contextPlayAct.getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
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

    public void muerte(){
        //Se situa al pacman en su posicion inicial
        posX = 8 * blockSize;
        posY = 13 * blockSize;

    }

    //Metodos de modificacion y observacion
    public boolean getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(boolean powerUp) {
        this.powerUp = powerUp;
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

    public boolean getPacmanInicio(){
        return this.inicio;
    }

    public void setPacmanInicio(boolean ini){
        this.inicio=ini;
    }

    public int getVida(){
        return this.vida;
    }

    public void setVida(int nuevaVida){
        this.vida=nuevaVida;
    }

}


