set search_path=public;

begin;
  delete from Refs where Domaene='GruppenArten';
  insert into Refs values('GruppenArten',1,'Authadmins');
  insert into Refs values('GruppenArten',2,'Instanzconfig');
  insert into Refs values('GruppenArten',3,'Jobverwaltung');
  insert into Refs values('GruppenArten',4,'Errorverwaltung');
  insert into Refs values('GruppenArten',5,'Vertrieb');
  insert into Refs values('GruppenArten',6,'Exec Sugar');
  insert into Refs values('GruppenArten',11,'Emdocverwaltung');
  insert into Refs values('GruppenArten',12,'Exec Emdoc Collect');
  insert into Refs values('GruppenArten',21,'Emdoc Alles Lesen');
  insert into Refs values('GruppenArten',22,'Emdoc Lesen');
  
  delete from Rechte;
  delete from Nutzer;
  delete from Gruppen;
  delete from Instanzen;
  delete from Ads;
  
  insert into Ads values(1,'spanset','172.17.5.22','172.17.5.23','172.17.5.21','SpanSet Deutschland');
  
  insert into Instanzen values(1,'ssd','SpanSet Deutschland in Übach-Palenberg');
  insert into Instanzen values(2,'axz','SpanSet Axzion');
  
  insert into Gruppen values(1,1,'Authadmins',1,null);
  insert into Gruppen values(2,1,'Instanzconfig Ssd',2,null);
  insert into Gruppen values(3,2,'Instanzconfig Axzion',2,null);
  insert into Gruppen values(4,1,'Jobverwaltung Ssd',3,null);
  insert into Gruppen values(5,2,'Jobverwaltung Axzion',3,null);
  insert into Gruppen values(6,1,'Errorverwaltung Ssd',4,null);
  insert into Gruppen values(7,2,'Errorverwaltung Axzion',4,null);
  insert into Gruppen values(8,1,'Vertrieb Ssd',5,null);
  insert into Gruppen values(9,2,'Vertrieb Axzion',5,null);
  insert into Gruppen values(10,1,'Exec Sugar Ssd',6,null);
  insert into Gruppen values(11,2,'Exec Sugar Axzion',6,null);
  insert into Gruppen values(30,1,'Emdocverwaltung Ssd',11,null);
  insert into Gruppen values(31,2,'Emdocverwaltung Axzion',11,null);
  insert into Gruppen values(32,1,'Emdoccollect Ssd',12,null);
  insert into Gruppen values(33,2,'Emdoccollect Axzion',12,null);

  insert into Nutzer values(1,'Admin',null,null,null,'admin','daskennwortistnichtgültig',null,null,1,1,'Der initiale Admin; Kennwort muß per Hand gesetzt werden');
  insert into Rechte values(1,1);
  insert into Rechte values(1,2);
  insert into Rechte values(1,3);
  insert into Rechte values(1,4);
  insert into Rechte values(1,5);
  insert into Rechte values(1,6);
  insert into Rechte values(1,7);
  insert into Rechte values(1,8);
  insert into Rechte values(1,9);
  insert into Rechte values(1,10);
  insert into Rechte values(1,11);
  insert into Rechte values(1,30);
  insert into Rechte values(1,31);
  insert into Rechte values(1,32);
  insert into Rechte values(1,33);
commit;
