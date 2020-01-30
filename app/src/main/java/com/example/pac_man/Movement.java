package com.example.pac_man;

import java.util.Timer;
import java.util.TimerTask;

public class Movement {
    private Pacman pacman;
    private int blockSize;
    private short [][] currentMap;
    private int swipeDir;
    private boolean pelletEaten,powerUp;
    private Ghost[] arrGhosts;

    public Movement(final short [][] curMap, final int blockSize,Pacman pacM,Ghost[] ghosts){
        currentMap = curMap;
        this.blockSize = blockSize;
        this.pacman = pacM;
        this.arrGhosts=ghosts;
       // ghost0 = new Ghost(blockSize);
       // ghost1 = new Ghost(blockSize);
       // ghost2 = new Ghost(blockSize);
       // ghost3 = new Ghost(blockSize);

        swipeDir = 4;
        pelletEaten = false;
        powerUp=false;
    }


    public void movePacman(){
        short ch;
        int nextDirection = pacman.getSigPos();
        int xPosPacman = pacman.getPosX();
        int yPosPacman = pacman.getPosY();

        // This was based on the non-Android Pacman legacy project for CS56
        // Check if xPos and yPos of pacman is both a multiple of block size
        if ( (xPosPacman % blockSize == 0) && (yPosPacman  % blockSize == 0) ) {

            // When pacman goes through tunnel on
            // the right reappear at left tunnel
            if (xPosPacman >= blockSize * 17) {
                xPosPacman = 0;
                pacman.setPosX(0);
            }

            // Is used to find the number in the level array in order to
            // check wall placement, pellet placement, and candy placement
            ch = currentMap[yPosPacman / blockSize][xPosPacman / blockSize];

            // If there is a pellet, eat it
            if ((ch & 16) != 0) {

                // Toggle pellet so it won't be drawn anymore
                /*aqui se verifico que la posicion actual contiene pastilla, ya que el "&" da distinto de 0,
                * por lo tanto el pacman la comera. Esta pastilla comida ya no debe ser dibujada, por este motivo,
                * se manda como paremetro la posActual elevado a la 16 para asegurar que en binario no haya un 1 en
                * la posicion del 16 asi (posM & 16) siempre dara 0.*/
                pelletWasEaten(yPosPacman / blockSize, xPosPacman / blockSize, (short) (ch ^ 16));
            }
            if((ch&32)!=0){
                powerUpComido(yPosPacman/blockSize,xPosPacman/blockSize,(short)(ch^32),8);
            }

            // Checks for direction buffering
            /*Aqui se verifica si el pacman se esta moviendo a una posicion valida, ya que ,
            * si la sigPos==3 significa que el pacman se movera a la izquierda, por lo tanto, se debe
            * verificar que en la posicion actual de la matriz (posM) NO se haya dibujado una pared a la
            * izquierda ( que se dibujan cuando (posM % 1)!=0 ) )  */
            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                pacman.setPosActual(nextDirection);
                swipeDir = nextDirection;
            }

            // Checks for wall collisions
            /*Aqui se verifica si en la direccion que el pacman se esta por mover, HAY una pared, ya que,
            * al ser por ej swipeDir==1 ( el pacman se mueve a la derecha) verifica si la posicion actual
            * de la matriz ( pos donde esta el pacman) se le ha dibujado una pared a la derecha , en caso afirmativo
            * se le asignara a swipeDir=4, que significa que no se mueva ( no hay caso en el switch para 4, en drawPacman) */
            if ((swipeDir == 3 && (ch & 1) != 0) ||
                    (swipeDir == 1 && (ch & 4) != 0) ||
                    (swipeDir == 0 && (ch & 2) != 0) ||
                    (swipeDir == 2 && (ch & 8) != 0)||
                    (swipeDir==2&&(ch&256)!=0)){
                swipeDir = 4;
            }
        }

        // When pacman goes through tunnel on
        // the left reappear at right tunnel
        if (xPosPacman < 0) {
            xPosPacman = blockSize * 17;
            pacman.setPosX(blockSize * 17);
        }
    }

    //call method after we have moved and drawn pacman
    public void updatePacman(){
        // Depending on the direction move the position of pacman
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
    public void moveGhost(Ghost ghost) {
        int ch;
        int posX=ghost.getPosX();
        int posY=ghost.getPosY();
        int sigPos=ghost.getSigPos();

        if ((posX % blockSize == 0) && (posY % blockSize == 0)) {
            if (posX >= blockSize * 17) {
                ghost.setPosX(0);

            }

            // Is used to find the number in the level array in order to
            // check wall placement, pellet placement, and candy placement
            ch = currentMap[posY / blockSize][posX / blockSize];


            if (!((sigPos == 3 && (ch & 1) != 0) ||
                    (sigPos == 1 && (ch & 4) != 0) ||
                    (sigPos == 0 && (ch & 2) != 0) ||
                    (sigPos == 2 && (ch & 8) != 0))) {
                ghost.setPosActual(sigPos);
                ghost.setDireccion(sigPos);
            }

            // Checks for wall collisions
            /*Aqui se verifica si en la direccion que el pacman se esta por mover, HAY una pared, ya que,
             * al ser por ej swipeDir==1 ( el pacman se mueve a la derecha) verifica si la posicion actual
             * de la matriz ( pos donde esta el pacman) se le ha dibujado una pared a la derecha , en caso afirmativo
             * se le asignara a swipeDir=4, que significa que no se mueva ( no hay caso en el switch para 4, en drawPacman) */
            int direccion=ghost.getDireccion();
            if ((direccion == 3 && (ch & 1) != 0) ||
                    (direccion == 1 && (ch & 4) != 0) ||
                    (direccion == 0 && (ch & 2) != 0) ||
                    (direccion == 2 && (ch & 8) != 0) ||
                    (direccion==2&&(ch&256)!=0)) {
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
        int posActual=ghost.getPosActual();

        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize)) && !pacman.getPowerUp()) {
            pacman.muerte();

            Globals.getInstance().setReiniciarJuego(true);

        }
        if (((posX / blockSize) == (pacman.getPosX() / blockSize)) &&
                ((posY / blockSize) == (pacman.getPosY() / blockSize)) && pacman.getPowerUp()) {
            //el pacman tiene el power up
            ghost.setVulnerable(false);
            ghost.setReset(true);
            Globals.getInstance().setGhostComido(ghost.getTipoGhost(),true);
            Globals.getInstance().aumentarScore(200);


        }
    }

    public short[][] updateMap(){
        pelletEaten = false;
        return currentMap;
    }
    public short[][] actualizarMapaPowerUp(){
        powerUp=false;
        return currentMap;
    }
    private void pelletWasEaten(int x, int y, short value){
        /*Como el valor que llega como parametro es un multiplo de 16, eso hara que (al igual que
        * cuando en la matriz hay valor 2 o 0 ) no se dibuje una pastilla.*/
        currentMap[x][y] = value;
        pelletEaten = true;
        Globals.getInstance().aumentarScore(10);
        Globals.getInstance().disminuirPellet(1);
    }
    private void powerUpComido(int x, int y, short value,int duracionSeg) {
        /*Como el valor que llega como parametro no contiene un 1 en la posicion de 32, eso hara que (al igual que
         * cuando en la matriz hay valor 2 o 0 ) no se dibuje una pastilla.*/
        if (!pacman.getPowerUp()) {

            Globals.getInstance().aumentarScore(50);
            Globals.getInstance().disminuirPellet(1);
            currentMap[x][y] = value;
            pacman.setPowerUp(true);
            for(int i=0;i<arrGhosts.length;i++){
                arrGhosts[i].setVulnerable(true);
                Globals.getInstance().setGhostComido(i,false);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pacman.setPowerUp(false);

                }
            }, duracionSeg*1000);
        }
    }
    public boolean needMapRefresh(){
        return pelletEaten;
    }
    public boolean verifPowerUp(){
        return powerUp;
    }
}
