package com.example.pac_man;

public class Globals {
    private boolean reiniciarJuego;
    private static Globals instance;
    private int score,cantidadPellets;
    private static boolean clydeComido,pinkyComido,inkyComido,blinkyComido;

    public Globals(){
        this.clydeComido=false;
        this.pinkyComido=false;
        this.inkyComido=false;
        this.blinkyComido=false;
        this.score=0;

    }
    public boolean getReiniciarJuego(){
        return this.reiniciarJuego;
    }

    public void setReiniciarJuego(boolean reinicio){
        this.reiniciarJuego=reinicio;
    }
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    public void aumentarScore(int score){
        this.score+=score;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score=score;
    }

    public void setCantidadPellets(int pellets){
        this.cantidadPellets = pellets;
    }

    public int getCantidadPellets(){
        return this.cantidadPellets;
    }

    public void disminuirPellet(int cantidad){
        this.cantidadPellets--;
    }

    public void setGhostComido(int tipo,boolean comido){
        switch(tipo){
            case 0:
                clydeComido=comido;
                break;
            case 1:
                pinkyComido=comido;
                break;
            case 2:
                inkyComido=comido;
                break;
            case 3:
                blinkyComido=comido;

        }
    }

    public static boolean isClydeComido() {
        return clydeComido;
    }

    public static void setClydeComido(boolean clydeComido) {
        Globals.clydeComido = clydeComido;
    }

    public static boolean isPinkyComido() {
        return pinkyComido;
    }

    public static void setPinkyComido(boolean pinkyComido) {
        Globals.pinkyComido = pinkyComido;
    }

    public static boolean isInkyComido() {
        return inkyComido;
    }

    public static void setInkyComido(boolean inkyComido) {
        Globals.inkyComido = inkyComido;
    }

    public static boolean isBlinkyComido() {
        return blinkyComido;
    }

    public static void setBlinkyComido(boolean blinkyComido) {
        Globals.blinkyComido = blinkyComido;
    }
}
