/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net;

import java.io.PrintWriter;

/**
 *
 * @author Sistinformatici PC 4
 */
public class Sender {
    PrintWriter out;

    public Sender(PrintWriter writer) {
        this.out = writer;
    }
    
    public void send(String message){
        out.println(message);
        out.flush();
    }
    
    public void close(){
        out.close();
    }
}
