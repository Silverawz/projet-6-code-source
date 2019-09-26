package com.deroussen;








import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;





@SpringBootApplication
public class EscaladeApplication implements CommandLineRunner {
	
	

	
	
	public static void main(String[] args) {
		SpringApplication.run(EscaladeApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {

		/*
		//creation de l'admin
		Utilisateur admin = new Utilisateur(null, "Admin","Admin","nico@live.fr","123456",true);
		userRepo.save(admin);
		
		
		try {
			//1.get connection to database
			Connection connection1 = DriverManager.getConnection
			("jdbc:mysql://localhost:3306/escalade_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			
			//2 create a statement
			Statement statement1 = connection1.createStatement();
			
			
			//3 execute sql query
			String sql = "insert into ROLES_USERS " + "(ROLES_ROLE_ID, USERS_USER_ID)"
				+	" values ('1','1')";
			statement1.executeUpdate(sql);
			
			String sql1 = "insert into ROLES_USERS " + "(ROLES_ROLE_ID, USERS_USER_ID)"
					+	" values ('2','1')";
				statement1.executeUpdate(sql1);
			//4. process the result set
			System.out.println("insert complete");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		*/
		
/*
		
		//Ecrire des données
		try {
			//1.get connection to database
			Connection connection1 = DriverManager.getConnection
			("jdbc:mysql://localhost:3306/escalade_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			
			//2 create a statement
			Statement statement1 = connection1.createStatement();
			
			
			//3 execute sql query
			String sql = "insert into USER " + "(USER_FIRSTNAME, USER_NAME, USER_EMAIL, USER_PASSWORD, IS_ACTIVE)"
				+	" values ('nicolas','dejoun','nicolas@live.fr','123456',true)";
			statement1.executeUpdate(sql);
			//4. process the result set
			System.out.println("insert complete");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//update des données
		try {
			//1.get connection to database
			Connection connection2 = DriverManager.getConnection
			("jdbc:mysql://localhost:3306/escalade_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			
			//2 create a statement
			Statement statement2 = connection2.createStatement();
			
			
			//3 execute sql query
			String sql = "update USER " + " set USER_EMAIL='papa.live.fr'"
				+	"where USER_ID = 11";
			statement2.executeUpdate(sql);
			//4. process the result set
			System.out.println("update complete");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//Lire les données
		try {
			//1.get connection to database
			Connection connection = DriverManager.getConnection
			("jdbc:mysql://localhost:3306/escalade_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			
			//2 create a statement
			Statement statement = connection.createStatement();
			
			
			//3 execute sql query
			ResultSet resultSet = statement.executeQuery("select * from USER");
			
			
			//4. process the result set
			while (resultSet.next()) {
				System.out.println(resultSet.getString("USER_FIRSTNAME"));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//delete des données
		try {
			//1.get connection to database
			Connection connection3 = DriverManager.getConnection
			("jdbc:mysql://localhost:3306/escalade_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			
			//2 create a statement
			Statement statement3 = connection3.createStatement();
			
			
			//3 execute sql query
			String sql = "delete from USER where USER_FIRSTNAME='prenom'";
			int rowsAffected = statement3.executeUpdate(sql);
			//4. process the result set
			System.out.println("rows affected = " + rowsAffected);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		*/
		
		
		/*
		// creation de deux utilisateurs
		Utilisateur user = new Utilisateur(null, "prenom","Nom","nico@live.fr","123456789",true);
		userRepo.save(user);
		Utilisateur user1 = new Utilisateur(null, "jack","daniel","jack@live.fr","123456789",true);
		userRepo.save(user1);
		
		
		//creation d'une longueur avec parametre : id,name,cotation
		Longueur longueur1 = new Longueur(null,"longueur 1","5b");
		longueurRepo.save(longueur1);
		
		//creation d'une voie avec parametre :id,name,cotation et ajout de longueur
		Voie voie1 = new Voie(null,"voie une","3c");
		voie1.getLongueur().add(longueur1);
		voieRepo.save(voie1);
	
		//creation d'un secteur avec parametre :id,name et ajout de voie
		Secteur secteur1 = new Secteur(null,"secteur1");
		secteur1.getVoie().add(voie1);	
		secteurRepo.save(secteur1);
		
		//creation d'un site d'escalade avec parametre : id,name,is_equipped,is_official et ajout de secteur
		SiteEscalade site = new SiteEscalade(null,"Spot d'escalade","oui","non");
		site.getSecteur().add(secteur1);
		site.getSecteur().add(secteur1);
		site.getSecteur().add(secteur1);
		
		//creation d'un commentaire
		Commentaire comment = new Commentaire(null,"ceci est un commentaire");	
		commentRepo.save(comment);
		
		//sauvegarde du sitez dans la BDD et ajout de l'utilisateur id dans le site d'escalade
		siteRepo.save(site);
		user.getSiteEscalade().add(site);
		userRepo.save(user);
		
		//ajout des id de l'utilisateur et du site dans le commentaire
		user.getCommentaire().add(comment);
		site.getCommentaire().add(comment);
		siteRepo.save(site);
		userRepo.save(user);
		
		//creation du topo et ajout du site dans celui ci, ajout egalement de l'id de l'utilisateur qui propose et de l'email de l'utilisateur qui reserve
		Topo topo = new Topo(null,"Topo un","description du topo un, blablabla","paris",new Date(),"oui");
		topo.getSiteEscalade().add(site);
		topo.getSiteEscalade().add(site);
		topoRepo.save(topo);
		user1.getTopoProposed().add(topo);
		userRepo.save(user1);
		user.getTopoReserved().add(topo);
		userRepo.save(user);
		
		
		
		topo.getSiteEscalade().add(site);
		user.getTopoProposed().add(topo);
		user.getTopoReserved().add(topo);
		topoRepo.save(topo);
		userRepo.save(user);
		*/
		
		/*
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.getTransaction().commit();	
		session.close();
		
		
		//show site
		siteRepo.findAll().forEach(p->{
			System.out.println(p.getDesignation());
		});
		
		
		//id,name,cotation
		Longueur longueur1 = new Longueur(null,"longueur 1","5b");
		Longueur longueur2 = new Longueur(null,"longueur 2","6d");
		Longueur longueur3 = new Longueur(null,"longueur 3","2c");
		longueurRepo.save(longueur1);longueurRepo.save(longueur2);longueurRepo.save(longueur3);
		
		//id,name,cotation
		Voie voie1 = new Voie(null,"voie une","3c");
		voie1.getLongueur().add(longueur1);
		Voie voie2 = new Voie(null,"voie deux","6b");
		voie2.getLongueur().add(longueur2);	
		Voie voie3 = new Voie(null,"voie trois","9a");
		voie3.getLongueur().add(longueur3);	
		voieRepo.save(voie1);voieRepo.save(voie2);voieRepo.save(voie3);
	
		//id,name
		Secteur secteur1 = new Secteur(null,"secteur1");
		secteur1.getVoie().add(voie1);	
		Secteur secteur2 = new Secteur(null,"secteur2");
		secteur2.getVoie().add(voie2);	
		Secteur secteur3 = new Secteur(null,"secteur3");
		secteur3.getVoie().add(voie3);
		secteurRepo.save(secteur1);secteurRepo.save(secteur2);secteurRepo.save(secteur3);
		
		//creation d'un site d'escalade avec parametre : id,name,is_equipped,is_official
		SiteEscalade site = new SiteEscalade(null,"Spot d'escalade","oui","non");
		site.getSecteur().add(secteur1);
		site.getSecteur().add(secteur2);
		site.getSecteur().add(secteur3);
		
		*/
	}

}
