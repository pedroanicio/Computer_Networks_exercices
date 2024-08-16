package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int PORTA = 5000;
    private static final Map<String, ClientHandler> clientes = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor ouvindo na porta " + PORTA);

            while (true) {
                Socket socket = servidor.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("Erro ao tentar se conectar: " + e.getMessage());
        }
    }

    // Classe para tratar clientes conectados
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter saida;
        private BufferedReader entrada;
        private String nomeCliente;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                saida = new PrintWriter(socket.getOutputStream(), true);

                // Receber o nome do cliente na primeira mensagem
                String msg = entrada.readLine();
                String[] partes = msg.split(" ", 4);
                String nome = partes[2];
                nome = nome.substring(0, nome.length() - 1);
                nomeCliente = nome;
                clientes.put(nomeCliente, this);
                System.out.println(nomeCliente + " conectou-se.");

                String linha;
                while ((linha = entrada.readLine()) != null) {
                    processarMensagem(linha);
                }
            } catch (IOException e) {
                System.out.println("Erro na comunicação com o cliente: " + e.getMessage());
            } finally {
                fecharConexao();
            }
        }

        private void processarMensagem(String mensagem) {
            if (mensagem.startsWith("ENVIAR TODOS")) {
                // Enviar para todos os clientes conectados
                String mensagemParaTodos = mensagem.substring(12); // Remove "ENVIAR TODOS "
                enviarParaTodos(mensagemParaTodos);
            } else if (mensagem.startsWith("ENVIAR ")) {
                // Enviar para um destinatário específico
                String[] partes = mensagem.split(" ", 3);
                String destinatario = partes[1];
                String mensagemParaDestinatario = partes[2];

                enviarParaCliente(destinatario, mensagemParaDestinatario);
            }
        }

        private void enviarParaTodos(String mensagem) {
            synchronized (clientes) {
                for (ClientHandler cliente : clientes.values()) {
                    cliente.saida.println(mensagem);
                }
            }
        }

        private void enviarParaCliente(String destinatario, String mensagem) {
            ClientHandler cliente = clientes.get(destinatario);
            if (cliente != null) {
                cliente.saida.println(mensagem);
            } else {
                saida.println("Cliente " + destinatario + " não encontrado.");
            }
        }

        private void fecharConexao() {
            try {
                if (nomeCliente != null) {
                    clientes.remove(nomeCliente);
                    System.out.println(nomeCliente + " desconectou-se.");
                }
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
    }
}
