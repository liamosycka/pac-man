package com.example.pac_man;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class Movement {
    private Pacman pacman;
    private int blockSize,swipeDir;
    private short [][] currentMap;
    private MediaPlayer waka;
    private Ghost[] arrGhosts;

    public Movement(final short [][] curMap, final int blockSize,Pacman pacM,Ghost[] ghosts, Context context){
        currentMap = curMap;
        this.waka = MediaPlayer.create(context,R.raw.pacmanwaka);
        this.waka.setVolume(100,100);
        this.blockSize = blockSize;
        this.pacman = pacM;
        this.arrGhosts=ghosts;
        swipeDir = 4;
    }

    public void movePacman(){
        short posMatriz;
        int nextDirection = pacman.getSigPos();
        int xPosPacman = pacman.getPosX();
        int yPosPacman = pacman.getPosY();
        //verifica si el pacman esta en un bloque valido
        if ( (xPosPacman % blockSize == 0) && (yPosPacman  % blockSize == 0) ) {
            //vemos si el pacman se fue por el tunel de la derecha
            if (xPosPacman >= blockSize * 17) {
                xPosPacman = 0;
                pacman.setPosX(0);
            }
            posMatriz = currentMap[yPosPacman / blockSize][xPosPacman / blockSize];
            // Si en la posicion de la matriz hay un bit asertado en 16, significa que hay una pellet y la come
            if ((posMatriz & 16) != 0) {
                /*Esta pellet comida ya no debe ser dibujada, por este motivo,
                * se manda como paremetro el valor de la posMatriz elevado a la 16 para asegurar que deje de
                * existir un bit asertado en 16.*/

                pelletComida(yPosPacman / blockSize, xPosPacman / blockSize, (short) (posMatriz ^ 16));
            }


            //Si en la posicion de la matriz hay un bit asertado en 32, significa que hay un powerUp disponible, y lo come
            if((posMatriz&32)!=0){
                //en la posMatriz se guardara el valor que habia elevado a la 32 para que deje de asertar el bit en 32
                powerUpComido(yPosPacman/blockSize,xPosPacman/blockSize,(short)(posMatriz^32),8);
            }
            //Si en la posicion de la matriz hay un bit asertado en 512, significa que hay una fruta activa, y la come
            if((posMatriz&512)!=0){
                /*en la posicion de la matriz donde esta la fruta, se le asigna 1026, que tiene un bit asertado en 1024,
                * que indica que la fruta no esta activa*/

                Globals.getInstance().setFrutaActiva(false);
                Globals.getInstance().aumentarScore(100);
                currentMap[yPosPacman/blockSize][xPosPacman/blockSize]=1026;
            }
            /*Aqui se verifica si el pacman se esta moviendo a una posicion valida, ya que ,
            * si la sigPos==3 significa que el pacman se movera a la izquierda, por lo tanto, se debe
            * verificar que en la posicion actual de la matriz (posMatriz) NO se haya dibujado una pared a la
            * izquierda ( que no tiene un bit asertado en 1 )  */
            if (!((nextDirection == 3 && (posMatriz & 1) != 0) ||
                    (nextDirection == 1 && (posMatriz & 4) != 0) ||
                    (nextDirection == 0 && (posMatriz & 2) != 0) ||
                    (nextDirection == 2 && (posMatriz & 8) != 0))) {
                pacman.setPosActual(nextDirection);
                swipeDir = nextDirection;
            }

            //Verifica colisiones con paredes
            /*Aqui se verifica si en la direccion que el pacman se esta por mover, HAY una pared, ya que,
            * al ser por ej swipeDir==1 ( el pacman se mueve a la derecha) verifica si la posicion actual
            * de la matriz ( posicion donde esta el pacman) se le ha dibujado una pared a la derecha , en caso afirmativo
            * se le asignara a swipeDir=4, que significa que no se mueva ( no hay caso en el switch para 4, en drawPacman) */
            if ((swipeDir == 3 && (posMatriz & 1) != 0) ||
                    (swipeDir == 1 && (posMatriz & 4) != 0) ||
                    (swipeDir == 0 && (posMatriz & 2) != 0) ||
                    (swipeDir == 2 && (posMatriz & 8) != 0)||
                    (swipeDir==2&&(posMatriz&256)!=0)){
                swipeDir = 4;
            }
        }

        //verifica si el pacman se fue por el tunel de la izquierda
        if (xPosPacman < 0) {
            pacman.setPosX(blockSize * 17);
        }
    }

    public void updatePacman(){

        if (swipeDir == 0) {
            pacman.setPosY(pacman.getPosY() + -blockSize/15); /*se disminuye porque se debe ir a posiciones de la matriz mas bajas, ya que
                                                                se esta subiendo*/
        } else if (swipeDir == 1) {
            pacman.setPosX(pacman.getPosX() + blockSize/15);
        } else if (swipeDir == 2) {
            pacman.setPosY(pacman.getPosY() + blockSize/15);
        } else if (swipeDir == 3) {
            pacman.setPosX(pacman.getPosX() + -blockSize/15);
        }
    }
    //misma logica que en movePacman
    public void moveGhost(Ghost ghost) {
        int posMatriz;
        int posX=ghost.getPosX();
        int posY=ghost.getPosY();
        int sigPos=ghost.getSigPos();

        if ((posX % blockSize == 0) && (posY % blockSize == 0)) {
            if (posX >= blockSize * 17) {
                posX=0;
                ghost.setPosX(0);

            }
            posMatriz = currentMap[posY / blockSize][posX / blockSize];
            if (!((sigPos == 3 && (posMatriz & 1) != 0) ||
                    (sigPos == 1 && (posMatriz & 4) != 0) ||
                    (sigPos == 0 && (posMatriz & 2) != 0) ||
                    (sigPos == 2 && (posMatriz & 8) != 0))) {
                ghost.setPosActual(sigPos);
                ghost.setDireccion(sigPos);
            }
            int direccion=ghost.getDireccion();
            if ((direccion == 3 && (posMatriz & 1) != 0) ||
                    (direccion == 1 && (posMatriz & 4) != 0) ||
                    (direccion == 0 && (posMatriz & 2) != 0) ||
                    (direccion == 2 && (posMatriz & 8) != 0) ||
                    (direccion==2&&(posMatriz&256)!=0)) {
                ghost.setDireccion(4);
            }
        }
        if (posX < 0) {
            ghost.setPosX(blockSize * 17);
        }
    }
    public void updateGhost(Ghost ghost){
        int direccion=ghost.getDireccion();
        int posX=ghost.getPosX();
        int posY=ghost.getPosY();
        if (direccion == 0) {
            ghost.setPosY(posY + -blockSize / 20);

        } else if (direccion == 1) {
            ghost.setPosX(posX + blockSize / 20);
        } else if (direccion == 2) {
            ghost.setPosY(posY + blockSize / 20);
        } else if (direccion == 3) {
            ghost.setPosX(posX + -blockSize / 20);
        }
    }
    public void chocarPacman(Ghost ghost) {
        int posX=ghost.getPosX();
        int posY=ghost.getPosY();

        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize)) && !pacman.getPowerUp()) {
            pacman.muerte();
            Globals.getInstance().setReiniciarJuego(true);
        }
        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize))) {
            if(pacman.getPowerUp()&&ghost.getVulnerable()){
                //el pacman tiene el power up
                ghost.setVulnerable(false);
                ghost.setReset(true);
                Globals.getInstance().aumentarScore(200);
            }else{
                pacman.muerte();
                Globals.getInstance().setReiniciarJuego(true);
            }
        }
    }


    private void pelletComida(int x, int y, short value){
        currentMap[x][y] = value;
        Globals.getInstance().aumentarScore(10);
        Globals.getInstance().disminuirPellet();
    }
    private void powerUpComido(int x, int y, short value,int duracionSeg) {
        if (!pacman.getPowerUp()) {

            Globals.getInstance().aumentarScore(50);
            Globals.getInstance().disminuirPellet();
            currentMap[x][y] = value;
            pacman.setPowerUp(true);
            for(int i=0;i<arrGhosts.length;i++){
                arrGhosts[i].setVulnerable(true);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pacman.setPowerUp(false);

                }
            }, duracionSeg*1000);
        }
    }

}
