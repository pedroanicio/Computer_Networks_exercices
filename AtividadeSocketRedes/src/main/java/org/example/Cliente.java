package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente implements Runnable {
    private final String host;
    private final int porta;
    private String mensagem;
    private String opcao;

    public Cliente(String host, int porta, String mensagem, String opcao) {
        this.host = host;
        this.porta = porta;
        this.mensagem = mensagem;
        this.opcao = opcao;
    }

    @Override
    public void run() {
        try (Socket soquete = new Socket(host, porta);
             PrintWriter saida = new PrintWriter(soquete.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(soquete.getInputStream()))) {

            if (opcao.equals("1")) {
                saida.println("ENVIAR " + mensagem);
            } else if (opcao.equals("2")) {
                saida.println("LISTAR" + mensagem);
                String resposta;
                while ((resposta = entrada.readLine()) != null) {
                    System.out.println(resposta);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1"; // Endereço IP do Servidor
        int porta = 5000;          // Porta que o Servidor está
        String nome, opcao;

        Scanner s = new Scanner(System.in);

        System.out.println("Digite seu nome:");
        nome = s.nextLine();

        do {
            System.out.println("Informe a opção desejada:");
            System.out.println("1 - Enviar mensagem");
            System.out.println("2 - Listar mensagens");
            System.out.println("3 - Sair");
            opcao = s.nextLine();

            if (opcao.equals("1")) {
                System.out.println("Digite o destinatário:");
                String destinatario = s.nextLine();
                System.out.println("Digite a mensagem:");
                String mensagem = nome + " para " + destinatario + ": " + s.nextLine();
                new Thread(new Cliente(host, porta, mensagem, opcao)).start();
            } else if (opcao.equals("2")) {
                new Thread(new Cliente(host, porta, nome, opcao)).start();
            }
        } while (!opcao.equals("3"));
    }
}