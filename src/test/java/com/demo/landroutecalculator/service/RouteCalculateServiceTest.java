package com.demo.landroutecalculator.service;

import com.demo.landroutecalculator.model.CountryPropertiesResponse;
import com.demo.landroutecalculator.model.LandRouteCalculateDto;
import com.demo.landroutecalculator.service.CountryInformationService;
import com.demo.landroutecalculator.service.impl.RouteCalculateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteCalculateServiceTest {

    @Autowired
    private RouteCalculateServiceImpl routeCalculateService;

    @Test
    void givenCountriesWhenCalculateRouteThenReturnPossibleRoute() {
        List<String> route = new ArrayList<>();
        route.add("CZE");
        route.add("AUT");
        route.add("ITA");
        LandRouteCalculateDto landRouteCalculateDto = routeCalculateService.calculateLandRoute("CZE", "ITA");
        assertEquals(route, landRouteCalculateDto.getRoute());
    }

    @Test
    void givenCountryIsIslandWhenCalculateRouteThenReturnEmptyList() {
        LandRouteCalculateDto landRouteCalculateDto = routeCalculateService.calculateLandRoute("ABW", "TUR");
        assertTrue(landRouteCalculateDto.getRoute().isEmpty());
    }

}