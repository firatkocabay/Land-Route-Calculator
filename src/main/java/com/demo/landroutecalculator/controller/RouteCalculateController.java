package com.demo.landroutecalculator.controller;

import com.demo.landroutecalculator.model.LandRouteCalculateDto;
import com.demo.landroutecalculator.service.RouteCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RouteCalculateController {

    private final RouteCalculateService routeCalculateService;

    public RouteCalculateController(RouteCalculateService routeCalculateService) {
        this.routeCalculateService = routeCalculateService;
    }

    @GetMapping("/routing/{originCountry}/{destinationCountry}")
    public ResponseEntity<LandRouteCalculateDto> getLandRoutes(@PathVariable String originCountry, @PathVariable String destinationCountry) {
        LandRouteCalculateDto landRouteCalculateDto = routeCalculateService.calculateLandRoute(originCountry, destinationCountry);
        return new ResponseEntity<>(landRouteCalculateDto, getHttpStatus(landRouteCalculateDto));
    }

    private HttpStatus getHttpStatus(LandRouteCalculateDto landRouteCalculateDto) {
        return landRouteCalculateDto.getRoute() == null || landRouteCalculateDto.getRoute().isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
    }

}
