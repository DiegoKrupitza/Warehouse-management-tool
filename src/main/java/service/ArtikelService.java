package service;

import domain.Artikel;

import java.util.List;
import java.util.Optional;

public class ArtikelService extends BaseService {

    public List<Artikel> getAllArtikel() throws Exception {
        return artikelRepository.findAll(getConnection());
    }

    public boolean deleteArtikel(long idToDelete) throws Exception {
        return (artikelRepository.delete(getConnection(), idToDelete) == 1) ? true : false;
    }

    public Artikel getArtikel(long idToEdit) throws Exception {
        Optional<Artikel> artikel = artikelRepository.findById(getConnection(), idToEdit);

        if (artikel.isPresent()) {
            return artikel.get();
        }
        return null;
    }

    public Artikel updateArtikel(Artikel a) throws Exception {
        return artikelRepository.updateArtikel(getConnection(), a);
    }

    public Artikel insertNewArtikel(Artikel a) throws Exception {
        return artikelRepository.insertNewArtikel(getConnection(), a);
    }
}
