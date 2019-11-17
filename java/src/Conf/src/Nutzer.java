import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Nutzer {
	Integer nuId,adId;
	String nachname,vor1,vor2,vor3,login,kennwort,adName,bemerkung;
	boolean isEna;
	protected ArrayList<Gruppe> gruppen;
	Nutzer(){
		nuId=null; nachname=vor1=vor2=vor3=login=kennwort=adName=bemerkung=null;
		adId=0; isEna=false;
		gruppen=null;
	}
	Nutzer(SqCursor c) throws SQLException{
		nuId=c.getI(1);
		nachname=c.getS(2); vor1=c.getS(3); vor2=c.getS(4); vor3=c.getS(5);
		login=c.getS(6); kennwort=c.getS(7); adId=c.getI(8); adName=c.getS(9);
		isEna=c.getb(10); bemerkung=c.getS(12);
		gruppen=null;
	}
	void ladeGruppen() throws SQLException{
		Gruppe g;
		SqCursor c=new SqCursor(Config.coInt);
		gruppen=new ArrayList<>();
		for (c.open("select GrId from public.rechte where NuId="+nuId);c.next();) {
			g=Gruppe.suche(c.geti(1));
			if (g!=null) gruppen.add(g);
		}
		c.close();
	}
	void sync() throws SQLException {
		BindBef b;
		if (nuId==null) {
			b=new BindBef(Config.coInt,"insert into public.Nutzer values(?,?,?,?,?,?,?,?,?,?,0,?)");
			nuId=Config.coInt.getI("select coalesce(max(NuId),0)+1 from public.Nutzer");
			b.add(nuId); b.add(nachname); b.add(vor1); b.add(vor2); b.add(vor3); b.add(login); b.add(kennwort);
			b.add(adId); b.add(adName); b.add(isEna); b.add(bemerkung);
		} else {
			b=new BindBef(Config.coInt);
			b.addText("update public.Nutzer set NachName=?,Vor1=?,Vor2=?,Vor3=?,Login=?,Kennwort=?");
			b.addText(",AdId=?,AdName=?,isEna=?,Bemerkung=? where NuId=?");
			b.prepare();
			nuId=Config.coInt.getI("select coalesce(max(NuId),0)+1 from public.Nutzer");
			b.add(nachname); b.add(vor1); b.add(vor2); b.add(vor3); b.add(login); b.add(kennwort);
			b.add(adId); b.add(adName); b.add(isEna); b.add(bemerkung); b.add(nuId);
		}
		b.exec();
	}
	
	public boolean isAuthAdmin() {
		for (Gruppe g:gruppen) if (g.art==1) return true;
		return false;
	}

	public boolean isInstConfig(int inst) {
		for (Gruppe g:gruppen) if ((g.art==2)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isJobVerwalter(int inst) {
		for (Gruppe g:gruppen) if ((g.art==3)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isErrorVerwalter(int inst) {
		for (Gruppe g:gruppen) if ((g.art==4)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isVertrieb(int inst) {
		for (Gruppe g:gruppen) if ((g.art==5)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isExecutor(int inst) {
		for (Gruppe g:gruppen) if ((g.art==6)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isEmdocVerwalter(int inst) {
		for (Gruppe g:gruppen) if ((g.art==11)&&(g.instId==inst)) return true;
		return false;
	}
	public boolean isEmdocCollector(int inst) {
		for (Gruppe g:gruppen) if ((g.art==12)&&(g.instId==inst)) return true;
		return false;
	}
	
	public void setzeSchalter(CmdLine cmd) {
		nachname=cmd.getS("-nn", nachname);
		vor1=cmd.getS("-vo1",vor1);
		vor2=cmd.getS("-vo2",vor2);
		vor3=cmd.getS("-vo3",vor2);
		login=cmd.getS("-log",login);
		String iAd=cmd.getS("-ad",null);
		if (iAd!=null) {
			try {
				adId=Config.coInt.getI("select AdId from public.Ads where Name='"+iAd+"'");
			} catch(SQLException e) { Config.syntax("Konnte AD-Name nicht finden:"+iAd);}
		}
		adName=cmd.getS("-adname",adName);
		bemerkung=cmd.getS("-be",bemerkung);
		if (cmd.Hat("-ena")) isEna=true;
		if (cmd.Hat("-dis")) isEna=false;	
	}
	
	protected void po(String s) {
		System.out.print(s);
	}
	
	protected void pn(String s) {
		System.out.println(s);
	}
	
	public void zeigen() {
		po("Anzeige Benutzer\n-----------------------------\n");
		po("              Status: ");
		if (isEna) po("Benutzer ist aktiv"); else	pn("Benutzer ist abgeschaltet");
		pn(" (-ena / -dis)");
		pn("   Nutzer Id (-nuid): "+nuId);
		pn("      Nachname (-na): "+nachname);
		pn("    Vorname 1 (-vo1): "+vor1);
		pn("    Vorname 2 (-vo2): "+vor2);
		pn("    Vorname 3 (-vo3): "+vor3);
		pn("         Login (-lo): "+login);
		pn("Active Dir (-adName): "+adName+" auf (-ad):"+adId);
		pn("     Bemerkung (-be): "+bemerkung);
		
	}
	
	public static String crypt(String ein) {
		MessageDigest md;
		StringBuilder sb=null;
		try {
			md = MessageDigest.getInstance("MD5");
	        byte[] hashInBytes = md.digest(ein.getBytes(StandardCharsets.UTF_8));
	        sb = new StringBuilder();
	        for (byte b : hashInBytes) sb.append(String.format("%02x", b));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Konnte keine Verschlüsselung durchführen.");
			System.exit(-2);
		}
		return sb.toString();
	}
	
	public static Nutzer vonIdLaden(int id) {
		Nutzer u=null;
		try {
			SqCursor c=new SqCursor(Config.coInt);
			c.open("select * from public.Nutzer where NuId="+id);
			if (!c.next()) Config.syntax("Konnte Benutzer mit der Id "+id+" nicht finden.");
			u=new Nutzer(c);
		} catch(SQLException e) {Conf.dbError("Fehler beim Laden Benutzer nach Id", e);}
		return u;
	}
	
	public static void list() throws SQLException {
		SqCursor c=new SqCursor(Config.coInt);
		System.out.println(" Id  Nachname                       Vorname 1       Login lokal     Name in der Active Directory");
		System.out.println("---- ------------------------------ --------------- --------------- ----------------------------");
		for (c.open("select NuId,Nachname,Vor1,Login,adName from public.Nutzer order by NuId");c.next();) {
			System.out.print(String.format("%4d %-30s %-15s %-15s %s\n",c.geti(1),c.getS(2),c.getS(3),c.getS(4),c.getS(5)));
		}
	}
	
	public static void neu(CmdLine cmd) throws SQLException{
		Nutzer u=new Nutzer();
		u.setzeSchalter(cmd);
		u.sync();
	}
	
	public static void aendern(CmdLine cmd) throws SQLException{
		int nuid=cmd.geti("-nuid", -1);
		if (nuid==-1) Config.syntax("Die Angabe der Benutzerid fehlt. Verwende -nuid");
		Nutzer u=Nutzer.vonIdLaden(nuid);
		u.setzeSchalter(cmd);
		u.sync();
	}

	public static void setPass(CmdLine cmd) throws SQLException{
		int nuid=cmd.geti("-nuid", -1);
		if (nuid==-1) Config.syntax("Die Angabe der Benutzerid fehlt. Verwende -nuid");
		Nutzer u=Nutzer.vonIdLaden(nuid);
    	char[] pw1 = System.console().readPassword("Kennwort    : ");
    	char[] pw2 = System.console().readPassword("Wiederholung: ");
    	u.kennwort=String.valueOf(pw1);
    	if (!u.kennwort.equals(String.valueOf(pw2))) Config.syntax("Die beiden Kennwörter stimmen nicht überein.");
    	u.sync();
	}
	
	public static void loeschen(CmdLine cmd) throws SQLException{
		int nuid=cmd.geti("-nuid", -1);
		if (nuid==-1) Config.syntax("Die Angabe der Benutzerid fehlt. Verwende -nuid");
		Config.coInt.Bef("delete from public.Rechte where NuId="+nuid);
		Config.coInt.Bef("delete from public.Kekse where NuId="+nuid);
		Config.coInt.Bef("delete from public.Nutzer where NuId="+nuid);
	}
	
	public static void zeigen(CmdLine cmd) throws SQLException{
		int nuid=cmd.geti("-nuid", -1);
		if (nuid==-1) Config.syntax("Die Angabe der Benutzerid fehlt. Verwende -nuid");
		Nutzer u=Nutzer.vonIdLaden(nuid);
		u.zeigen();
	}
	
}
