package app.controller;

import app.dto.admin.PersonListForAdmin;
import app.dto.platform.*;
import app.service.LocationService;
import app.service.MainService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform")
@Api(tags = "Работа с платформой")
public class PlatformController {

    private final LocationService locationServiceImpl;
    private final MainService mainService;

    public PlatformController(LocationService locationServiceImpl,
                              MainService mainService) {
        this.locationServiceImpl = locationServiceImpl;
        this.mainService = mainService;
    }

    @GetMapping("/languages")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Успешное получение языков",
                    response = LanguageResponse.class)})
    @ApiOperation(value = "Получение языков платформы", response = LocationService.class)
    public ResponseEntity<LanguageResponse> getLanguages(
            @RequestParam(required = false) String language,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "${default.perPage}") int itemPerPage
    ) {
        return locationServiceImpl.getLanguage(offset, itemPerPage);
    }

    @GetMapping("/cities")
    @ApiOperation(
            value = "Получение городов",
            notes = "Получение городов платформы")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Успешное получение городов",
                    response = CitiesResponse.class)})
    public ResponseEntity<CitiesResponse> cities(
            @ApiParam(value = "ID страны", defaultValue = "1")
            @RequestParam int countryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "${default.perPage}") int itemPerPage
    ) {
        return locationServiceImpl.getCities(countryId, offset, itemPerPage, city);
    }

    @GetMapping("/countries")
    @ApiOperation(
            value = "Получение стран",
            notes = "Получение стран платформы")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Успешное получение стран",
                    response = CountriesResponse.class)})
    public ResponseEntity<CountriesResponse> countries(
            @RequestParam(required = false) String country,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "${default.perPage}") int itemPerPage
    ) {
        return locationServiceImpl.getCountries(offset, itemPerPage, country);
    }

    @GetMapping("/persons")
    public ResponseEntity<PersonListForAdmin> persons() {
        return mainService.getPersonList();
    }

}