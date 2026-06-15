package com.restaurantes;

import org.junit.platform.suite.api.SelectFile;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/*
./mvnw test -Dtest=-SeleniumTestsSuite
 */
@Suite
@SelectPackages({"com.restaurantes"})
public class AllTestsSuite {
}
