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

        if (originCountryBorders.isEmpty() || originCountry.equals(destinationCountry)) {
            landRouteCalculateDto.setRoute(route);
            return landRouteCalculateDto;
        }

        route.add(originCountry);

        if (originCountryBorders.contains(destinationCountry)) {
            route.add(destinationCountry);
            landRouteCalculateDto.setRoute(route);
            return landRouteCalculateDto;
        }


        List<String> destinationCountryBorders = countryInformationService.getRelatedCountryBorders(destinationCountry);

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

        calculateRouteUsingLandBorders(route, originCountry, destinationCountry, originCountryBorders, landRouteCalculateDto);

        return landRouteCalculateDto;
    }

    private LandRouteCalculateDto calculateRouteUsingLandBorders(List<String> route,
                                                                 String originCountry,
                                                                 String destinationCountry,
                                                                 List<String> originCountryBorders,
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
            this.calculateRouteUsingLandBorders(route, originCountry, destinationCountry, neighborCountryLandBorders, landRouteCalculateDto);
        }

        return landRouteCalculateDto;
    }

}
