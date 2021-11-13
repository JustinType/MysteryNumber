import java.net.*;
import java.io.*;

public class NombreMystereClient{
    public static void main(String[]args){
      int port_serveur  = Integer.parseInt(args[1]);
    	String ip_serveur = args[0];

    	Socket socket=null;
    	BufferedReader input=null, bR=null;
    	PrintWriter output=null;

    	try {
    	    socket = new Socket(ip_serveur, port_serveur);
    	    System.out.println("Connexion etablie avec "+ip_serveur+" sur le port "+ port_serveur);
    	}catch(UnknownHostException e ){
    	    System.out.println("Le nom de l'hote n'est pas correcte");
    	    System.exit(1);
    	}catch(IOException e ){
    	    System.out.println("Une erreur de lecture/ecriture lors de l'etablissment de la connexion");
    	    System.exit(1);
    	}

      try {
          input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          output = new PrintWriter(socket.getOutputStream());

          bR = new BufferedReader(new InputStreamReader(System.in));
      }catch(IOException e ){
          System.out.println("Une erreur de lecture/ecriture lors de la creation des flux");
          System.exit(1);
      }
      try {
        System.out.println("debut init");
        String s2 = input.readLine();
        System.out.println(s2);
        String s = bR.readLine();
        System.out.println("votre nom sera : "+s);

        output.println(s);
        output.flush();

        s2 = input.readLine();
        int np = Integer.parseInt(s2);

        try {
            input.close();
            output.close();
            socket.close();
        }catch(IOException e ){
            System.out.println("Une erreur lors de la ferneture des flux");
            System.exit(1);
        }

        try {
            socket = new Socket(ip_serveur,np);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());

        }catch(IOException e ){
            System.out.println("Une erreur de lecture/ecriture lors de la creation des flux");
            System.exit(1);
        }


      }catch(IOException e ){
          System.out.println("Une erreur lors de l'initialisation");
          System.exit(1);
      }catch(Exception e ){
        System.out.println("Une erreur inconue est survenu");
        System.exit(1);
      }

      try {
      while (true) {

            System.out.println(input.readLine());

            output.println(bR.readLine());
            output.flush();
            
            System.out.println(input.readLine());
    	}
    }catch(Exception e){
      System.out.println("erreur lors du tour!");
    }

      try {
          input.close();
          output.close();
          bR.close();
          socket.close();
      }catch(IOException e ){
          System.out.println("Une erreur lors de la ferneture des flux");
          System.exit(1);
      }
    }
}
