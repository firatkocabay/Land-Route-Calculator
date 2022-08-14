package com.demo.landroutecalculator.service.impl;

import com.demo.landroutecalculator.model.LandRouteCalculateDto;
import com.demo.landroutecalculator.service.CountryInformationService;
import com.demo.landroutecalculator.service.RouteCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
public class RouteCalculateServiceImpl implements RouteCalculateService {

    private final CountryInformationService countryInformationService;

    public RouteCalculateServiceImpl(CountryInformationService countryInformationService) {
        this.countryInformationService = countryInformationService;
    }

    @Override
    public LandRouteCalculateDto calculateLandRoute(String originCountry, String destinationCountry) {
        LandRouteCalculateDto landRouteCalculateDto = new LandRouteCalculateDto();
        List<String> originCountryBorders = countryInformationService.getRelatedCountryBorders(originCountry);
        List<String> route = new ArrayList<>(new LinkedHashSet<>());

        if (checkCountryBorderAndIsSameCountry(originCountry, destinationCountry, landRouteCalculateDto, originCountryBorders, route))
            return landRouteCalculateDto;

        route.add(originCountry);

        checkNeighborCountries(destinationCountry, landRouteCalculateDto, originCountryBorders, route);
        if (route.contains(destinationCountry)) return landRouteCalculateDto;

        List<String> destinationCountryBorders = countryInformationService.getRelatedCountryBorders(destinationCountry);
        calculateBasicLandRoute(destinationCountry, landRouteCalculateDto, originCountryBorders, route, destinationCountryBorders);
        if (route.contains(destinationCountry)) return landRouteCalculateDto;

        calculateRouteUsingLandBorders(route, destinationCountry, originCountryBorders, destinationCountryBorders, landRouteCalculateDto);

        return landRouteCalculateDto;
    }

    private boolean checkCountryBorderAndIsSameCountry(String originCountry, String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route) {
        if (originCountryBorders.isEmpty() || originCountry.equals(destinationCountry)) {
            landRouteCalculateDto.setRoute(route);
            return true;
        }
        return false;
    }

    private LandRouteCalculateDto checkNeighborCountries(String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route) {
        if (originCountryBorders.contains(destinationCountry)) {
            route.add(destinationCountry);
            landRouteCalculateDto.setRoute(route);
            return landRouteCalculateDto;
        }
        return landRouteCalculateDto;
    }

    private LandRouteCalculateDto calculateBasicLandRoute(String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route, List<String> destinationCountryBorders) {
        originCountryBorders.forEach(originBorder -> destinationCountryBorders.forEach(destinationBorder -> {
            if (originBorder.equals(destinationBorder) && !route.contains(destinationCountry)) {
                log.info("Contains neighbor border land country of {}", originBorder);
                route.add(originBorder);
                route.add(destinationCountry);
                landRouteCalculateDto.setRoute(route);
            }
        }));

        if (route.contains(destinationCountry)) {
            return landRouteCalculateDto;
        }
        return landRouteCalculateDto;
    }

    private LandRouteCalculateDto calculateRouteUsingLandBorders(List<String> route,
                                                                 String destinationCountry,
                                                                 List<String> originCountryBorders,
                                                                 List<String> destinationCountryBorders,
                                                                 LandRouteCalculateDto landRouteCalculateDto) {
        log.info("Route List 1: {}", route);

        for (int i = 0; i < originCountryBorders.size(); i++) {
            if (route.contains(destinationCountry)) {
                log.info("route contain destinationCountry!");
                landRouteCalculateDto.setRoute(route);
                break;
            }
            String border = originCountryBorders.get(i);
            route.add(border);
            log.info("Route List 2: {}", route);
            List<String> neighborCountryLandBorders = countryInformationService.getRelatedCountryBorders(border, route);
            if (neighborCountryLandBorders.isEmpty() && !route.contains(destinationCountry)) {
                route.remove(border);
                continue;
            }
            this.calculateRouteUsingLandBorders(route, destinationCountry, neighborCountryLandBorders, destinationCountryBorders, landRouteCalculateDto);
        }

        return landRouteCalculateDto;
    }

}
