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
                //pelletWasEaten(yPosPacman / blockSize, xPosPacman / blockSize, (short) (ch ^ 16));
            }

            // Checks for direction buffering
            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                pacman.setPosActual(nextDirection);
                swipeDir = nextDirection;
            }

            // Checks for wall collisions
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
            pacman.setPosY(pacman.getPosY() + -blockSize/15);
        } else if (swipeDir == 1) {
            pacman.setPosX(pacman.getPosX() + blockSize/15);
        } else if (swipeDir == 2) {
            pacman.setPosY(pacman.getPosY() + blockSize/15);
        } else if (swipeDir == 3) {
            pacman.setPosX(pacman.getPosX() + -blockSize/15);
        }
    }
}
