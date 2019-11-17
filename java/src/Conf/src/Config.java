import java.sql.SQLException;

public class Config {
	public static SqConn coInt;
	public static Nutzer nutzer;
	
	public static void syntax(String s) {
		System.out.println("Syntax oder Parameterfehler: "+s);
		System.exit(-1);
	}
	
	public static String getEnv(String was,String ersatz) {
		String erg=System.getenv(was);
		if (erg!=null) return erg;
		return ersatz;
	}
	
	public static void starte(CmdLine cmd) {
		String dbConn,sugarNutzer,sugarPass;
		dbConn=cmd.getS("-db",getEnv("SugarDb",null));
		if (dbConn==null) syntax("Keine Datenbankverbindung angegeben.");
	    try {
	    	Class.forName("org.postgresql.Driver");
	    	coInt=new SqConn(dbConn);
	    } catch (Exception e) {  e.printStackTrace(); System.err.println(e.getClass().getName()+": "+e.getMessage()); System.exit(9); }
	    //"jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
	    if (!coInt.open(dbConn)) Conf.dbError("Datenbank konnte nicht geöffnet werden");
	    Gruppe.ladeGruppen();
	    
	    sugarNutzer=cmd.getS("-user",getEnv("SugarUser",null));
	    sugarPass=cmd.getS("-pass",getEnv("SugarPass",null));
	    
	    if (sugarNutzer==null) Config.syntax("Kein Sugarnutzer angegeben.");
	    if (sugarPass==null){
	    	char[] passwordArray = System.console().readPassword("Kennwort: ");
	    	sugarPass=String.valueOf(passwordArray);
	    }
	    SqCursor c=new SqCursor(coInt);
	    try {
	      c.open("select * from public.Nutzer where login='"+sugarNutzer+"'");
	      if (c.next()) {
	    	  String iPass=Nutzer.crypt(sugarPass);
	    	  if (!iPass.equals(c.getS("kennwort"))) {
	    		  System.out.println("Benutzer oder Kennwort falsch");
	    		  System.exit(-2);
	    	  }
	      } else {
    		  System.out.println("Benutzer oder Kennwort falsch");
    		  System.exit(-2);
	      }
	      nutzer=new Nutzer(c);
	      nutzer.ladeGruppen();
	    } catch(SQLException e) { Conf.dbError("Konnte Nutzer nicht laden",e);}
	}
}
