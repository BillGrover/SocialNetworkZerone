package app.repository;

import app.model.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    Optional<City> findById(long id);

    List<City> findAllByCountryIdIsAndNameLike(Long countryId, String title, Pageable p);

    List<City> findAllByCountryId(long countryId);

    int countAllByCountryIdIs(long countryId);

}
