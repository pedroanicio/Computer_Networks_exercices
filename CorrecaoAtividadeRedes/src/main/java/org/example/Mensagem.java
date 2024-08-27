package org.example;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private String remetente;
    private String destinatario;
    private String conteudo;

    public Mensagem(String remetente, String destinatario, String conteudo) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.conteudo = conteudo;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    @Override
    public String toString() {
        return "De: " + remetente + "\nPara: " + destinatario + "\nMensagem: " + conteudo;
    }
}
