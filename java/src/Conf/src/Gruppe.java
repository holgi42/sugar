import java.sql.SQLException;
import java.util.ArrayList;

public class Gruppe {
	Integer grId;
	int art,instId;
	String name,bemerkung;
	static ArrayList<Gruppe> gruppen;
	
	Gruppe(){
		grId=null; name=bemerkung=null;
		art=instId=0;
	}
	
	Gruppe(SqCursor c) throws SQLException{
		grId=c.getI(1); instId=c.geti(2); name=c.getS(3);
		art=c.geti(4); bemerkung=c.getS(5);
	}
	
	void Sync() throws SQLException{
		BindBef b;
		if (grId==null) {
			b=new BindBef(Config.coInt,"insert into public.gruppen values(?,?,?,?,?)");
			grId=Config.coInt.getI("select coalesce(max(GrId))+1 from public.gruppen");
			b.add(grId); b.add(instId); b.add(name); b.add(art); b.add(bemerkung);
		} else {
			b=new BindBef(Config.coInt,"update public.gruppen set InstId=?,Name=?,Art=?,Bemerkung=? where grId=?");
			b.add(instId); b.add(name); b.add(art); b.add(bemerkung); b.add(grId);
		}
		b.exec();
	}
	
	static void ladeGruppen(){
		gruppen=new ArrayList<>();
		try {
			SqCursor c=new SqCursor(Config.coInt);
			for (c.open("select * from public.Gruppen order by Name");c.next();)
				gruppen.add(new Gruppe(c));
			c.close();
		} catch(SQLException e) { Conf.dbError("Gruppenladen",e);}
	}
	
	static Gruppe suche(int id) {
		for (Gruppe k:gruppen)
			if (k.grId==id) return k;
		return null;
	}
	static Gruppe suche(String name) {
		for (Gruppe k:gruppen)
			if (k.name.equals(name)) return k;
		return null;
	}
}
