create table if not exists public.Refs(
  Domaene varchar(25) not null,
  Nr int not null,
  Wert varchar(500)
);

create table if not exists public.Instanzen(
  InstId int not null,
  Name varchar(25) not null,
  Bemerkung varchar(500),
  primary key(InstId),
  unique(Name)
);

create table if not exists public.Ads(
  AdId int not null,
  Name varchar(25) not null,
  Ip1 varchar(50) not null,
  Ip2 varchar(50),
  Ip3 varchar(50),
  Bemerkung varchar(500),
  primary key(AdId),
  unique(Name)
);

create table if not exists public.Nutzer(
  NuId int not null,
  Nachname varchar(50),
  Vor1 varchar(25),
  Vor2 varchar(25),
  Vor3 varchar(25),
  Login varchar(25),
  Kennwort varchar(100),
  AdId int,
  AdName varchar(100),
  isEna int not null,
  isAdmin int not null,
  Bemerkung varchar(500),
  primary key(NuId),
  foreign key(AdId) references public.Ads
);

create table if not exists public.Gruppen(
  GrId int not null,
  InstId int not null,
  Name varchar(25) not null,
  Art int not null, -- Domäne GruppenArten
  Bemerkung varchar(500),
  primary key(GrId),
  foreign key(InstId) references public.Instanzen
);

create table if not exists public.Rechte(
  NuId int not null,
  GrId int not null,
  foreign key(NuId) references public.Nutzer,
  foreign key(GrId) references public.Gruppen
);

create table if not exists public.Kekse(
  sId int not null,
  Art int not null, -- Domäne Keksarten
  NuId int not null,
  Wert varchar(300),
  Ausgestellt timestamp not null,
  Ablauf timestamp not null,
  Zugriff timestamp not null,
  primary key(sId),
  foreign key(NuId) references public.Nutzer
);
