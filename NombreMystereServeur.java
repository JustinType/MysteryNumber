// Le Nombre Mystere
// Serveur

import java.net.*;
import java.io.*;

public class NombreMystereServeur {

    int port;
    ServerSocket serveur;
    ServerSocket srv_1, srv_2;
    Socket player_1, player_2;
    String id_1, id_2;
    int score_1, score_2;
    int nombre_mistere;
    
    public NombreMystereServeur(int p){
	port = p;
	score_1 = 0;
	score_2 = 0;
    }

    public void acceptPlayer(int p) throws Exception {
	
	Socket player = serveur.accept();
	    
	BufferedReader in =
	    new BufferedReader(new InputStreamReader(player.getInputStream()));
	PrintWriter out = new PrintWriter(player.getOutputStream());

	// Envoi du message de départ
	
	out.println("Entrez votre nom");
	out.flush();

	// Récupération de l'id du joueur
	
	String id = in.readLine();

	// Calcul et envoi du port pour le joueur
	// choix très (trop) simple : le numéro de port
	// du serveur de jeux + le paramètre p devient
	// le numéro de port que doit utiliser le joueur
	
	int newport = port + p;
	out.println(newport);
	out.flush();

	// ouverture du serveur dédié à chaque client

	if (p == 1) {
	    srv_1 = new ServerSocket(newport);
	    player_1 = srv_1.accept();
	    id_1 = id;
	}
	else {
	    srv_2 = new ServerSocket(newport);
	    player_2 = srv_2.accept();
	    id_2 = id;
	}
	player.close();
    }

    public int readPlayer(Socket player, String id)  throws Exception {

	BufferedReader in =
	    new BufferedReader(new InputStreamReader(player.getInputStream()));
	PrintWriter out = new PrintWriter(player.getOutputStream());
	
	out.println("Entrez votre nombre : ");
	out.flush();
	String resp = in.readLine();

	System.out.println("Joueur "+id+" envoie : "+resp);  
	
	try {
	    int nombre = Integer.parseInt(resp);
	    return nombre;
	}catch(NumberFormatException e){
	    return -1;
	}
    }
    
    public void sendPlayer(Socket player, String id , int score, int score_other)  throws Exception {
	PrintWriter out = new PrintWriter(player.getOutputStream());
	String msg;
	
	if (score == 0 && score_other != 0) 
	    msg = "Vous avez gangne :) \nOn recommence :\n";
	else if (score == 0 && score_other == 0) 
	    msg = "Vous avez fait match null :| \nOn recommence :\n";	
	else if (score != 0 && score_other == 0) 
	    msg = "Vous avez perdu :( \nOn recommence :\n";
	else if (score > 0)
	    msg = "Vous etes trop haut";
	else
	    msg = "Vous etes trop bas";
	
	out.println(id + " : "+msg);
	out.flush();
    }

    public int unNombreAleatoire(){
	int Min = 10;
	int Max = 100;
	int rnd = Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println("Le nombre mistere = " +rnd);
	return rnd;
    }
    
    public void go()  throws Exception {

	// génération d'un nombre aléatoire dans [10, 100]
	nombre_mistere = unNombreAleatoire();
		
	// serveur d'accueil
	
	serveur = new ServerSocket(port);

	// enregistrement joueur 1
	
	acceptPlayer(1);
	
	// idem joueur 2
	
	acceptPlayer(2);

	while (true) {

	    // lecture successive des 2 joueurs
	    
	    int rep1 = -1;
	    while(rep1 == -1)
		rep1 = readPlayer(player_1, id_1);
	    int rep2 = -1;
	    while(rep2 == -1)
		rep2 = readPlayer(player_2, id_2);

	    // algorithme de jeux, très complexe ;-)
	    
	    if (rep1 > nombre_mistere )
		score_1 = 1;
	    if (rep1 < nombre_mistere )
		score_1 = -1;		    
	    
	    if (rep2 > nombre_mistere )
		score_2 = 1;
	    if (rep2 < nombre_mistere )
		score_2 = -1;
	    
	    if (rep1 == nombre_mistere ) 
		score_1 = 0;
	    if (rep2 == nombre_mistere )
		score_2 = 0;

	    sendPlayer(player_1, id_1, score_1, score_2);
	    sendPlayer(player_2, id_2, score_2, score_1);

	    // nombre trouvé on recomence 
	    if (score_1 == 0 || score_2 == 0)
		nombre_mistere = unNombreAleatoire();
	    
	}
    }

    public static void main(String[] args) throws Exception {
	int p = Integer.parseInt(args[0]);
	NombreMystereServeur nm = new NombreMystereServeur(p);
	nm.go();
    }
}
