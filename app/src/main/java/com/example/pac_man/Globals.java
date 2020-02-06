package com.example.pac_man;

public class Globals {
    private boolean reiniciarJuego, frutaActiva;
    private static Globals instance;
    private int score, cantidadPellets;

    public Globals() {
        this.score = 0;
        this.frutaActiva = false;
    }

    public void setFrutaActiva(boolean frutaActiva) {
        this.frutaActiva = frutaActiva;
    }

    public boolean getFrutaActiva() {
        return this.frutaActiva;
    }

    public boolean getReiniciarJuego() {
        return this.reiniciarJuego;
    }

    public void setReiniciarJuego(boolean reinicio) {
        this.reiniciarJuego = reinicio;
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    public void aumentarScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCantidadPellets(int pellets) {
        this.cantidadPellets = pellets;
    }

    public int getCantidadPellets() {
        return this.cantidadPellets;
    }

    public void disminuirPellet() {
        this.cantidadPellets--;
    }


}
