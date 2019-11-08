package ru.osipov.deploy.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.osipov.deploy.entities.Cinema;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.repositories.CinemaRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository rep;

    private static final Logger logger = getLogger(CinemaServiceImpl.class);

    @Autowired
    public CinemaServiceImpl(CinemaRepository r){
        this.rep = r;
    }

    @Nonnull
    @Override
    public List<CinemaInfo> getAllCinemas() {
        logger.info("Get all cinemas");
        return rep.findAll().stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public CinemaInfo getByName(String ciName) {
        logger.info("Get one cinema by name = '{}'",ciName);
        return rep.findByCname(ciName).map(this::buildModel).orElse(new CinemaInfo(-1l,"","","","",""));
    }

    @Nonnull
    @Override
    public List<CinemaInfo> getByCountry(String country) {
        logger.info("Get cinemas by country = '{}'",country);
        return rep.findByCountry(country).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<CinemaInfo> getByRegion(String region) {
        logger.info("Get cinemas by region = '{}'",region);
        return rep.findByRegion(region).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<CinemaInfo> getByCity(String city) {
        logger.info("Get cinemas by city = '{}'",city);
        return rep.findByCity(city).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<CinemaInfo> getByStreet(String street) {
        logger.info("Get cinemas by street = '{}'",street);
        return rep.findByStreet(street).stream().map(this::buildModel).collect(Collectors.toList());
    }

    @Nonnull
    private CinemaInfo buildModel(@Nonnull Cinema ci) {
        logger.info("Cinema: '{}'",ci);
        return new CinemaInfo(ci.getCid(),ci.getCname(), ci.getCountry(),ci.getCity(),ci.getRegion(),ci.getStreet());
    }
}
