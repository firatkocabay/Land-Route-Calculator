package com.demo.landroutecalculator.service;

import java.util.List;

public interface CountryInformationService {

    List<String> getRelatedCountryBorders(String relatedCountry);

    List<String> getRelatedCountryBorders(String relatedCountry, List<String> disabledCountryBorders);

    List<Double> getRelatedCountryLatitudeLongitude(String relatedCountry);

}
