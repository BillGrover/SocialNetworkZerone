package app.service.implementations;

import app.dto.location.CityDto;
import app.dto.location.CountryDto;
import app.dto.platform.*;
import app.exceptions.BadRequestException;
import app.model.City;
import app.model.Country;
import app.repository.CityRepository;
import app.repository.CountryRepository;
import app.service.LocationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LocationServiceImpl implements LocationService {

    private final CityRepository cityRepo;
    private final CountryRepository countryRepo;

    public LocationServiceImpl(CityRepository cityRepo, CountryRepository countryRepo) {
        this.cityRepo = cityRepo;
        this.countryRepo = countryRepo;
    }
    @Override
    public ResponseEntity<CitiesResponse> getCities(int countryId, int offset, int perPage, String findInCity) {

        Pageable pagination = PageRequest.of(offset, perPage);

        List<City> cityList;
        if (findInCity != null) {
            cityList = cityRepo.findAllByCountryIdIsAndNameLike((long) countryId, findInCity, pagination);
        } else {
            cityList = cityRepo.findAllByCountryId(countryId);
        }
        List<CityDto> list = new ArrayList<>();
        cityList.forEach(item -> {
            CityDto city = new CityDto();
            city.setId(item.getId());
            city.setName(item.getName());
            list.add(city);
        });

        CitiesResponse response = new CitiesResponse();
        response.setTotal(cityRepo.countAllByCountryIdIs(countryId));
        response.setOffset(offset);
        response.setPerPage(perPage);
        response.setCities(list);


        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CountriesResponse> getCountries(int offset, int perPage, String findInCountry) {

        Pageable pagination = PageRequest.of(offset, perPage);

        List<Country> countryList;
        if (findInCountry != null) {
            countryList = countryRepo.findAllByName(findInCountry, pagination);
        } else
            countryList = countryRepo.findAll();

        List<CountryDto> list = new ArrayList<>();
        countryList.forEach(item -> {
            CountryDto country = new CountryDto();
            country.setId(item.getId());
            country.setName(item.getName());
            list.add(country);
        });

        CountriesResponse response = new CountriesResponse();
        response.setTotal(countryRepo.countAllBy());
        response.setOffset(offset);
        response.setPerPage(perPage);
        response.setCountries(list);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LanguageResponse> getLanguage(int offset, int perPage) {
        LanguageDto ru = new LanguageDto(1, "Русский");
        LanguageDto en = new LanguageDto(2, "English");
        LanguageResponse response = new LanguageResponse(offset, perPage);
        response.addLanguage(ru);
        response.addLanguage(en);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<Country> findCountyById(long id) {
        return countryRepo.findById(id);
    }

    @Override
    public Optional<City> findCityById(long id) {
        return cityRepo.findById(id);
    }

    @Override
    public Country getCountyById(long id) throws BadRequestException {
        return countryRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Страна id:%d не найдена", id)));
    }

    @Override
    public City getCityById(long id) throws BadRequestException {
        return cityRepo.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Город id:%d не найден", id)));
    }

}
