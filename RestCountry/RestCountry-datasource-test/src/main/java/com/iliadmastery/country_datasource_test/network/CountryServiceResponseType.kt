package com.iliadmastery.country_datasource_test.network


sealed class CountryServiceResponseType{

    object EmptyList: CountryServiceResponseType()

    object ErrorOnJsonData: CountryServiceResponseType()

    object CorrectData: CountryServiceResponseType()

    object Http404: CountryServiceResponseType()
}
