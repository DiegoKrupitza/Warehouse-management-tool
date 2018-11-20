package service;


import domain.Artikelreservierung;

import java.util.List;

public class HomeService extends BaseService {

    public int getNumberOfArtikel() throws Exception {
        return artikelRepository.countTable(getConnection());
    }

    public int getNumberOfReservations() throws Exception {
        return artikelreservierungRepository.countTable(getConnection());
    }

    public int getNumberOfUsers() throws Exception {
        return userRepository.countTable(getConnection());
    }

    public List<Artikelreservierung> getAllReservations() throws Exception {

        return  artikelreservierungRepository.findAll(getConnection());

    }
}
