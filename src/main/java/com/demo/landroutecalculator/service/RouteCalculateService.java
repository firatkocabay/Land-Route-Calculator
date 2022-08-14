package com.demo.landroutecalculator.service;

import com.demo.landroutecalculator.model.LandRouteCalculateDto;

public interface RouteCalculateService {

    LandRouteCalculateDto calculateLandRoute(String originCountry, String destinationCountry);

}
