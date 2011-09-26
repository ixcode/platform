package ixcode.platform.test;

/**
 * This kind of test tests the entire deployed system, i.e. it will require a running server.
 * It may furtle with the persistence layer in order to set-up / tear-down data
 * It may provide fake services to allow complete edge case testing
 */
public @interface SystemTest {
}