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
        landRouteCalculateDto.setRoute(new ArrayList<>());

        if (checkCountryBorderSizeWithIsSameCountry(originCountry, destinationCountry, landRouteCalculateDto, originCountryBorders, route))
            return landRouteCalculateDto;

        route.add(originCountry);

        checkNeighborCountries(destinationCountry, landRouteCalculateDto, originCountryBorders, route);
        if (route.contains(destinationCountry)) return landRouteCalculateDto;

        List<String> destinationCountryBorders = countryInformationService.getRelatedCountryBorders(destinationCountry);
        calculateBasicLandRoute(destinationCountry, landRouteCalculateDto, originCountryBorders, route, destinationCountryBorders);

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

    private LandRouteCalculateDto calculateBasicLandRoute(String destinationCountry, LandRouteCalculateDto landRouteCalculateDto, List<String> originCountryBorders, List<String> route, List<String> destinationCountryBorders) {
        for (String originBorder : originCountryBorders) {
            for (String destinationBorder : destinationCountryBorders) {
                if (originBorder.equals(destinationBorder) && !route.contains(destinationCountry)) {
                    route.add(originBorder);
                    route.add(destinationCountry);
                } else {
                    List<String> borderCountryBorders = countryInformationService.getRelatedCountryBorders(originBorder, route);
                    if (!borderCountryBorders.isEmpty()) {
                        if (!route.contains(destinationCountry)) {
                            route.add(originBorder);
                            calculateBasicLandRoute(destinationCountry, landRouteCalculateDto, borderCountryBorders, route, destinationCountryBorders);
                        }
                        else
                            break;
                    }
                }
            }
        }

        if (route.contains(destinationCountry)) {
            landRouteCalculateDto.setRoute(route);
        }
        return landRouteCalculateDto;
    }

}
