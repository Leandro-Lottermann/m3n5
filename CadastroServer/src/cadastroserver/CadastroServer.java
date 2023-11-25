/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import cadastroserver.model.Usuarios;
import controller.UsuariosJpaController;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author leand
 */
public class CadastroServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");	

	UsuariosJpaController ctrlUsu = new UsuariosJpaController(emf);	
	
	try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor aguardando conexoes na porta 1234...");
			Socket socket = serverSocket.accept();
			
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                String login = (String) in.readObject();
                String senha = (String) in.readObject();
                String mensagem = (String) in.readObject();
                
                System.out.println("login=" + login + "   senha=" + senha);
                System.out.println("mensagem=" + mensagem);
                out.writeObject("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
                out.flush();	
                
                
                List<Usuarios> usuariosList = ctrlUsu.findUsuariosEntities();
                int tamanhoLista= usuariosList.size();
                System.out.println("tamnho="+tamanhoLista);            
                for (Usuarios usuario : usuariosList) {
                        System.out.println("login="+usuario.getLogin());            
                }

                System.out.println("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
                Usuarios userTeste = new Usuarios((tamanhoLista+1));
                userTeste.setLogin(login+(tamanhoLista+1));
                userTeste.setSenha(senha+(tamanhoLista+1));

                ctrlUsu.create(userTeste);
//          
            }			
				
			
	} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
