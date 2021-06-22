package app.repository;

import app.model.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

    Optional<Country> findById(long id);

    List<Country> findAllByName(String title, Pageable p);

    List<Country> findAll();
    
    int countAllBy();

}
