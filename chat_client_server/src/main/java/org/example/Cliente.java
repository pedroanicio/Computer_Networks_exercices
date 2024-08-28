package org.example;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente implements Runnable {
    private final String host;
    private final int porta;
    private String nome;
    private Socket socket;
    private PrintWriter saida;
    private BufferedReader entrada;
    private List<String> historicoMensagens = new ArrayList<>();

    public Cliente(String host, int porta, String nome) {
        this.host = host;
        this.porta = porta;
        this.nome = nome;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, porta);
            saida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Iniciar thread para ouvir mensagens do servidor
            new Thread(this::ouvirMensagens).start();

            while (true) {
                String destinatario = JOptionPane.showInputDialog(null,
                        "Digite o destinatário (deixe vazio para todos) ou 'sair' para encerrar:", nome, JOptionPane.QUESTION_MESSAGE);


                if (destinatario == null || destinatario.equalsIgnoreCase("sair")) {
                    break;
                }

                String mensagem = JOptionPane.showInputDialog(null, "Digite a mensagem:", nome, JOptionPane.QUESTION_MESSAGE);
                if (mensagem != null && !mensagem.trim().isEmpty()) {
                    enviarMensagem(destinatario, mensagem);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fecharConexao();
        }
    }

    private void enviarMensagem(String destinatario, String mensagem) {
        String mensagemParaEnviar = destinatario.isEmpty() ?
                "ENVIAR TODOS " + nome + ": " + mensagem :
                "ENVIAR " + destinatario + " " + nome + ": " + mensagem;
        saida.println(mensagemParaEnviar);
    }

    private void ouvirMensagens() {
        String mensagemRecebida;
        try {
            while ((mensagemRecebida = entrada.readLine()) != null) {
                historicoMensagens.add(mensagemRecebida);
                System.out.println("Mensagem de " + mensagemRecebida);
            }
        } catch (IOException e) {
            System.out.println("Erro ao receber mensagens: " + e.getMessage());
        }
    }

    private void fecharConexao() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (saida != null) {
                saida.close();
            }
            if (entrada != null) {
                entrada.close();
            }
        } catch (IOException e) {
            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1"; // Endereço IP
        int porta = 5000;          // Porta que o Servidor está

        String nome = JOptionPane.showInputDialog(null, "Digite seu nome:");
        if (nome != null && !nome.trim().isEmpty()) {
            Cliente cliente = new Cliente(host, porta, nome);
            new Thread(cliente).start();
        }
    }
}
