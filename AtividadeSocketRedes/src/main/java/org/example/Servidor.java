package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {

    public static void main(String[] args) {
        int porta = 5000; // Porta que o Servidor está

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor ouvindo na porta " + porta);

            while (true) {
                Socket conexao = servidor.accept();
                System.out.println("Nova conexão recebida de " + conexao.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(conexao)).start();
            }
        } catch (IOException e) {
            System.out.println("Erro ao tentar se conectar: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket conexao;
    public static ArrayList<String> mensagens = new ArrayList<>();

    public ClientHandler(Socket conexao) {
        this.conexao = conexao;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
             PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true)) {
            System.out.println("Tratando conexão de " + conexao.getInetAddress().getHostAddress());
            String linha = entrada.readLine();
            if (linha.startsWith("ENVIAR")) {
                String mensagem = linha.substring(7); // Remove "ENVIAR "
                salvarMensagem(mensagem);
                System.out.println("Mensagem recebida e salva: " + mensagem);
            } else if (linha.startsWith("LISTAR")) {
                String nome = linha.substring(6).trim(); // Remove "LISTAR "
                listarMensagens(saida, nome);
            }
        } catch (IOException e) {
            System.out.println("Erro na comunicação com o cliente: " + e.getMessage());
        } finally {
            try {
                conexao.close();
                System.out.println("Conexão fechada com " + conexao.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.out.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    private void salvarMensagem(String mensagem) {
        mensagens.add(mensagem);
        System.out.println(mensagens);
    }

    private void listarMensagens(PrintWriter saida, String nome) {
        for (String mensagem : mensagens) {
            System.out.println(nome);
            if (mensagem.contains(" para " + nome + ": ")) {
                saida.println(mensagem);
                mensagens.remove(mensagem);
            }
        }
    }
}
