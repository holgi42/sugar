import java.sql.SQLException;

public class Conf {

	public static void dbError(String s,Exception e) {
		System.out.println("Datenbankfehler:"+s);
		e.printStackTrace();
		System.exit(-4);
	}
	public static void dbError(String s) {
		System.out.println("Datenbankfehler:"+s);
		System.exit(-4);
	}
	
	public static void main(String[] args) {
		CmdLine cmd=new CmdLine(args);
		Config.starte(cmd);
		String bef=cmd.getSl(0);
		try {
		  switch(bef) {
		  case "nutzer": regelNutzer(cmd); break;
		  default: Config.syntax("Unbekannter Befehl:"+bef);
		  }
		} catch(SQLException e) { dbError("Datenbanfehler",e);}
	}
	
	protected static void checkAdmin() {
		if (Config.nutzer.isAuthAdmin()) return;
		System.out.println("Du bist nicht Admin und hast somit keine Rechte hierzu");
		System.exit(-2);
	}
	
	public static void regelNutzer(CmdLine cmd) throws SQLException{
		checkAdmin();
		String bef=cmd.getSl(1);
		switch(bef) {
		case "list": Nutzer.list(); break;
		case "neu": Nutzer.neu(cmd); break;
		case "set": Nutzer.aendern(cmd); break;
		case "pass": Nutzer.setPass(cmd); break;
		case "del": Nutzer.loeschen(cmd); break;
		case "show": Nutzer.zeigen(cmd); break;
		default: Config.syntax("Unbekannter nutzer-Befehl:"+bef);
		}
	}

}
