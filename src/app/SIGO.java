/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import controller.ControllerTrabalho;
import model.Ficheiros;
import view.MenuPrincipal;
import view.TelaOcorrencia;

/**
 *
 * @author Leo
 */
public class SIGO {
    public static void main(String[] args) {
         Ficheiros ficheiro = new Ficheiros ();
        MenuPrincipal menu = new MenuPrincipal ();
   
        ControllerTrabalho controller = new ControllerTrabalho (menu, ficheiro);
        
    }
}
