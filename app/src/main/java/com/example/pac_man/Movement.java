package com.example.pac_man;

public class Movement {
    private Pacman pacman;
    private int blockSize;
    private short [][] currentMap;
    private int swipeDir;
    private boolean pelletEaten;

    public Movement(final short [][] curMap, final int blockSize,Pacman pacM){
        currentMap = curMap;
        this.blockSize = blockSize;
        this.pacman = pacM;
       // ghost0 = new Ghost(blockSize);
       // ghost1 = new Ghost(blockSize);
       // ghost2 = new Ghost(blockSize);
       // ghost3 = new Ghost(blockSize);

        swipeDir = 4;
        pelletEaten = false;
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
                    (swipeDir == 2 && (ch & 8) != 0)) {
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
    public short[][] updateMap(){
        pelletEaten = false;
        return currentMap;
    }
    private void pelletWasEaten(int x, int y, short value){
        /*Como el valor que llega como parametro es un multiplo de 16, eso hara que (al igual que
        * cuando en la matriz hay valor 2 o 0 ) no se dibuje una pastilla.*/
        currentMap[x][y] = value;
        pelletEaten = true;
    }
    public boolean needMapRefresh(){
        return pelletEaten;
    }
}
