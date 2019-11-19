create or replace view public.DspGruppen(GrId,Name,InstId,Instanz,ArtId,Art) as
  select g.Grid,
         g.Name,
         g.InstId,
         i.Name,
         g.Art,
         r.Wert
    from public.Instanzen as i 
    join public.Gruppen as g using(InstId)
    join public.Refs as r on(r.Nr=g.Art and r.Domaene='GruppenArten')
;

