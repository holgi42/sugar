import java.util.ArrayList;

class KopfElement{
	int art; // 1-String 2-Int
	String ueber;
	KopfElement(int art){
		this.art=art;
		ueber=null;
	}
	KopfElement(int art,String ueber){
		this.art=art;
		this.ueber=ueber;
	}
	int ueberLang() {
		if (ueber==null) return 0;
		return ueber.length();
	}
	String getUeber() {
		if (ueber==null) return "";
		return ueber;
	}
}

public class SpaltenTab {
	protected ArrayList<KopfElement> oben;
	protected ArrayList<String[]> feld;
	protected String[] akZeile;
	protected int zustand; // 1-Kopf erweitern 2-Daten sammeln
	protected int anzahl,akp;
	public boolean mitKopf;
	SpaltenTab(){
		oben=new ArrayList<>();
		zustand=1;
		akZeile=null;
		mitKopf=false;
		feld=new ArrayList<>();
	}
	SpaltenTab(String ...iueber){
		oben=new ArrayList<>();
		zustand=1;
		akZeile=null;
		mitKopf=true;
		feld=new ArrayList<>();
		for (String k:iueber) {
			if (k.charAt(0)=='#') iaddKopf(2,k.substring(1)); else iaddKopf(1,k);
		}
	}
	protected void iaddKopf(int art,String ueber) {
		oben.add(new KopfElement(art,ueber));
		anzahl=oben.size();
		if (ueber!=null) mitKopf=true;
		akp=0;
	}
	void addKopf(String ueber) { iaddKopf(1,ueber);	}
	void addKopf() { iaddKopf(1,null);	}
	void addKopfi(String ueber) { iaddKopf(2,ueber); }
	void addI() { iaddKopf(2,null);	}
	protected void iadd(String w) {
		if (akZeile==null) {
			akp=0;
			akZeile=new String[anzahl];
		}
		if (w==null) akZeile[akp++]=""; else akZeile[akp++]=w;
		if (akp>=anzahl) {
			feld.add(akZeile);
			akZeile=null;
		}
	}
	void add(String w) { iadd(w);}
	void add(int i) { iadd(Integer.toString(i)); }
	void add(boolean b) { if (b) iadd("X"); else iadd(".");	}
	void ausgabe() {
		// Ermittle pro Spalte die größte Länge
		int[] lang=new int[anzahl];
		for (int i=0;i<anzahl;i++)
			lang[i]=mitKopf?oben.get(i).ueberLang():0;
		for (int j=0;j<feld.size();j++) {
			String[] k=feld.get(j);
			for (int i=0;i<anzahl;i++) {
				if (k[i].length()>lang[i]) lang[i]=k[i].length();
			}
		}
		
		// Kopf ausgeben ?
		if (mitKopf) {
			for (int i=0;i<anzahl;i++) System.out.print(String.format(" %-"+lang[i]+"s  ", oben.get(i).getUeber()));
			System.out.print("\n ");
			for (int i=0;i<anzahl;i++) {
				for (int j=0;j<lang[i];j++) System.out.print("-");
				System.out.print("   ");
			}
			System.out.println();
		}
		
		// Nun der Rest
		for (String[] k:feld) {
			for (int i=0;i<anzahl;i++) {
				if (oben.get(i).art==1)
					System.out.print(String.format(" %-"+lang[i]+"s  ", k[i]));
				else
					System.out.print(String.format(" %"+lang[i]+"s  ", k[i]));
			}
			System.out.println();
		}
	}
}
