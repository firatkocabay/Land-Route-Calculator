package com.demo.landroutecalculator.service.impl;

import com.demo.landroutecalculator.exception.CountryPropertiesNotFound;
import com.demo.landroutecalculator.model.CountryPropertiesResponse;
import com.demo.landroutecalculator.service.CountryInformationService;
import com.demo.landroutecalculator.util.RestCallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class CountryInformationServiceImpl implements CountryInformationService {

    private static final String DATA_LINK = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    private static final CountryPropertiesResponse[] countryPropertiesResponse;

    static {
        try {
            final RestTemplate restTemplate = RestCallUtil.getRestTemplateForHttps();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<CountryPropertiesResponse[]> entity = new HttpEntity<>(headers);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
            countryPropertiesResponse = restTemplate.exchange(DATA_LINK,HttpMethod.GET,entity, CountryPropertiesResponse[].class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured when call data. Message: " + e.getMessage());
        }
    }

    @Cacheable(value = "relatedCountryBorders", key = "#relatedCountry")
    @Override
    public List<String> getRelatedCountryBorders(String relatedCountry) {
        log.info("Calling service for country of: {}", relatedCountry);
        if (countryPropertiesResponse == null) {
            throw new CountryPropertiesNotFound("Country properties not found of given country: " + relatedCountry);
        }
        List<String> countryBorders = new ArrayList<>();
        for (CountryPropertiesResponse res : countryPropertiesResponse) {
            if (res.getCountryName().equals(relatedCountry)) {
                countryBorders.addAll(res.getCountryBorders());
            }
        }
        return countryBorders;
    }

    @Override
    public List<String> getRelatedCountryBorders(String relatedCountry, List<String> disabledCountryBorders) {
        log.info("disabledCountryBorders: {}", disabledCountryBorders);
        List<String> relatedCountryAllBorders = this.getRelatedCountryBorders(relatedCountry);
        log.info("relatedCountryAllBorders 1: {}", relatedCountryAllBorders);
        relatedCountryAllBorders.removeAll(new HashSet<>(disabledCountryBorders));
        log.info("relatedCountryAllBorders 2: {}", relatedCountryAllBorders);
        return relatedCountryAllBorders;
    }

}
