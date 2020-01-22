package com.example.pac_man;

public class Globals {
    private boolean reiniciarJuego;
    private static Globals instance;

    public Globals(){

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
}
