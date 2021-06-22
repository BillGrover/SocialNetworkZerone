package app.service;

import app.dto.platform.*;
import app.exceptions.BadRequestException;
import app.model.City;
import app.model.Country;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface LocationService {

    ResponseEntity<CitiesResponse> getCities(int countryId, int offset, int perPage, String findInCity);

    ResponseEntity<CountriesResponse> getCountries(int offset, int perPage, String findInCountry);

    ResponseEntity<LanguageResponse> getLanguage(int offset, int perPage);

    Optional<Country> findCountyById(long id);

    Optional<City> findCityById(long id);

    Country getCountyById(long id) throws BadRequestException;

    City getCityById(long id) throws BadRequestException;

}
