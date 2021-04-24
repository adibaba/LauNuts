package org.dice_research.launuts;

//import org.junit.runner.RunWith;
//import org.junit.runners.Suite;
//import org.junit.runners.Suite.SuiteClasses;
//
//@RunWith(Suite.class)
//@SuiteClasses({})
//public class AllTests {
//
//}

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
//import org.junit.platform.runner.SelectPackages;
//import org.junit.runner.RunWith;
import org.junit.runner.RunWith;

//import org.junit.jupiter.api.*;
//import org.junit.platform.*;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.platform.suite.api.SelectPackages;
//import org.junit.platform.suite.api.SuiteDisplayName;
//import org.junit.runner.RunWith;
//
//@RunWith(JUnitPlatform.class)
//@SuiteDisplayName("JUnit Platform Suite Demo")
//@SelectPackages("example")
@RunWith(JUnitPlatform.class)
@SelectPackages("org.dice_research.launuts")
public class AllTests {
}