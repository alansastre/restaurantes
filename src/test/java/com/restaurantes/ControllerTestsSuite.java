package com.restaurantes;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/*
./mvnw test -Dtest=ControllerTestsSuite
 */
@Suite
@SelectPackages({"com.restaurantes.controller"})
public class ControllerTestsSuite {
}
