package com.demo.landroutecalculator.service.impl;

import com.demo.landroutecalculator.model.LandRouteCalculateDto;
import com.demo.landroutecalculator.service.CountryInformationService;
import com.demo.landroutecalculator.service.RouteCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        landRouteCalculateDto.setRoute(new ArrayList<>());

        if (checkCountryBorderSizeWithIsSameCountry(originCountry, destinationCountry, landRouteCalculateDto, originCountryBorders, route))
            return landRouteCalculateDto;

        route.add(originCountry);

        checkNeighborCountries(destinationCountry, landRouteCalculateDto, originCountryBorders, route);
        if (route.contains(destinationCountry)) return landRouteCalculateDto;

        List<String> destinationCountryBorders = countryInformationService.getRelatedCountryBorders(destinationCountry);
        List<Double> originCountryLatitudeLongitude = countryInformationService.getRelatedCountryLatitudeLongitude(originCountry);
        List<Double> destinationCountryLatitudeLongitude = countryInformationService.getRelatedCountryLatitudeLongitude(destinationCountry);
        calculateBasicLandRoute(destinationCountry, landRouteCalculateDto, originCountryBorders, route, destinationCountryBorders, originCountryLatitudeLongitude, destinationCountryLatitudeLongitude);

        return landRouteCalculateDto;
    }

    private boolean checkCountryBorderSizeWithIsSameCountry(String originCountry, String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route) {
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

    private LandRouteCalculateDto calculateBasicLandRoute(String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route, List<String> destinationCountryBorders, List<Double> originCountryLatitudeLongitude, List<Double> destinationCountryLatitudeLongitude) {
        for (String borderCountry : originCountryBorders) {
            for (String destinationBorder : destinationCountryBorders) {
                if (borderCountry.equals(destinationBorder) && !route.contains(destinationCountry)) {
                    route.add(borderCountry);
                    route.add(destinationCountry);
                } else {
                    List<String> borderCountryBorders = countryInformationService.getRelatedCountryBorders(borderCountry, route);
                    if (!borderCountryBorders.isEmpty()) {
                        List<Double> currentCountryLatitudeLongitude = countryInformationService.getRelatedCountryLatitudeLongitude(borderCountry);
                        if((currentCountryLatitudeLongitude.get(1) > originCountryLatitudeLongitude.get(1)
                                    && currentCountryLatitudeLongitude.get(1) < destinationCountryLatitudeLongitude.get(1))
                                || (currentCountryLatitudeLongitude.get(1) < originCountryLatitudeLongitude.get(1)
                                    && currentCountryLatitudeLongitude.get(1) > destinationCountryLatitudeLongitude.get(1))) {
                            if (!route.contains(destinationCountry)) {
                                route.add(borderCountry);
                                calculateBasicLandRoute(destinationCountry, landRouteCalculateDto, borderCountryBorders, route, destinationCountryBorders, originCountryLatitudeLongitude, destinationCountryLatitudeLongitude);
                            } else
                                break;
                        }
                    }
                }
            }
        }

        if (route.contains(destinationCountry)) {
            landRouteCalculateDto.setRoute(removeDuplicates(route));
        }
        return landRouteCalculateDto;
    }

    private List<String> removeDuplicates(List<String> list) {
        log.info("Before remove duplicates: {}", list);
        Set<String> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
        log.info("After remove duplicates: {}", list);
        return list;
    }

}
