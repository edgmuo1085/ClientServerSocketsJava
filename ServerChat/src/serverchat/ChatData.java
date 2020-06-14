/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author otro
 */
public class ChatData extends Thread {

    DataOutputStream os;
    DataInputStream is;
    Socket client;
    LinkedList<Socket> clients;
    LinkedList<String> usuarios;
    String nombre;

    public ChatData(DataOutputStream os, DataInputStream is, Socket client, LinkedList<Socket> clients, LinkedList<String> usuarios, String nombre) {
        this.os = os;
        this.is = is;
        this.client = client;
        this.clients = clients;
        this.usuarios = usuarios;
        this.nombre = nombre;
    }

    public ChatData(Socket client, String nombre) {
        this.client = client;
        this.nombre = nombre;

        try {
            this.os = new DataOutputStream(client.getOutputStream());
            this.is = new DataInputStream(client.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(ChatData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msj = is.readUTF();
                System.out.println(msj);
                for (Socket client1 : clients) {
                    DataOutputStream os = new DataOutputStream(client1.getOutputStream());
                    os.writeUTF(usuarios + ":" + msj);
                }
            } catch (IOException ex) {
                try {
                    this.client.close();
                    this.os.close();
                    this.is.close();
                    clients.remove(this.client);
                    usuarios.remove(this.nombre);
                    System.out.println(usuarios + ":Todos:" + this.nombre + " se desconecto");

                    for (Socket client1 : clients) {
                        DataOutputStream os;
                        os = new DataOutputStream(client1.getOutputStream());
                        os.writeUTF(usuarios + ":Todos:" + this.nombre + " se desconecto");
                    }
                } catch (IOException ex1) {
                    Logger.getLogger(ChatData.class.getName()).log(Level.SEVERE, null, ex1);
                }
                break;
            }
        }

    }

}
