package ru.osipov.deploy.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.services.CinemaService;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/v1/cinemas")
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);
    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService s){
        this.cinemaService = s;
    }

    //GET: /v1/cinemas
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public List<CinemaInfo> getAll(){
        logger.info("/v1/cinemas");
        logger.info("Get all cinemas");
        return  cinemaService.getAllCinemas();
    }

    //GET: /v1/cinemas/country/{country}
    //if no any parameter was specified -> getAll() [GET: v1/cinemas/country/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/country/{country}","/country/"})
    public List<CinemaInfo> cinemasCountry(@PathVariable(required = false,name = "country") String country){
        List<CinemaInfo> result;
        logger.info("/v1/cinemas/country/");
        if(country == null){
            logger.info("Country is null. Return all cinemas.");
            result = cinemaService.getAllCinemas();
            return result;
        }
        else{
            logger.info("Country parameter = '{}'",country);
            result = cinemaService.getByCountry(country);
            return result;
        }
    }

    //GET: v1/cinemas/city/{city}
    //if no any parameter was specified -> getAll() [GET: v1/cinemas/city/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path= {"/city/{city}","/city/"})
    public List<CinemaInfo> cinemasCity(@PathVariable(required = false,name = "city") String city){
        List<CinemaInfo> result;
        logger.info("/v1/cinemas/city/");
        if(city == null || city.equals("")){
            logger.info("City is null");
            result = cinemaService.getAllCinemas();
            return result;
        }
        else{
            logger.info("City parameter = '{}' ",city);
            result = cinemaService.getByCity(city);
            return result;
        }
    }

    //GET: /v1/cinemas/region/{region}
    //if all parameter was specified AND region is null -> getAll() [GET: v1/cinemas/region/?all=true]
    //if region is null but not any parameter was specified -> getByRegion("") [GET: /v1/cinemas/region/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/region/{region}","/region/"})
    public List<CinemaInfo> cinemasRegion(@PathVariable(required = false,name="region") String region, @RequestParam(required = false, defaultValue = "false") Boolean all){
        List<CinemaInfo> result;
        logger.info("/v1/cinemas/region/");
        if(region == null && all){
            logger.info("Region is null but fetch all, cause p_all = '{}'",all);
            result = cinemaService.getAllCinemas();
            return result;
        }
        else if(region == null){//all was not specified.
            logger.info("Region is null. Fetch by region, cause p_all = '{}'",all);
            result = cinemaService.getByRegion(null);
            return result;
        }
        else{
            logger.info("Region is = '{}'",region);
            result = cinemaService.getByRegion(region);
            return result;
        }
    }

    //GET: /v1/cinemas/street/{street}
    //if no any parameter was specified -> getAll() [GET: v1/cinemas/street/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/street/{street}","/street/"})
    public List<CinemaInfo> cinemasStreet(@PathVariable(required = false,name="street") String street){
        List<CinemaInfo> result;
        logger.info("/v1/cinemas/street/");
        if(street == null){
            logger.info("Street is null.");
            result = cinemaService.getAllCinemas();
            return result;
        }
        else{
            logger.info("Street parameter = '{}'",street);
            result = cinemaService.getByStreet(street);
            return result;
        }
    }


    //GET: /v1/cinemas/
    //if no any parameter was specified -> getAll() [GET: v1/cinemas/]
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path={"/{name}","/"})
    public List<CinemaInfo> cinema(@PathVariable(required = false,name = "name") String name){
        List<CinemaInfo> result;
        logger.info("/v1/cinemas/");
        if(name == null){
            logger.info("Name is null.");
            result = cinemaService.getAllCinemas();
            return result;
        }
        else{
            result = new LinkedList<>();
            logger.info("Name parameter = '{}'",name);
            result.add(cinemaService.getByName(name));
            return result;
        }
    }
}
