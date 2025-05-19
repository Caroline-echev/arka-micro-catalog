package com.arka.micro_catalog.adapters.driven.r2dbc.util;

public class ConstantsR2DBC {
    private ConstantsR2DBC() {
        throw new IllegalStateException("Utility class");
    }
    public static final int INITIAL_SIZE = 12;
    public static final int MAX_SIZE = 15;
    public static final int MAX_IDLE_TIME = 30;
    public static final String CONNECTION_POOL_NAME = "api-postgres-connection-pool";
    public static final String VALIDATION_QUERY = "SELECT 1";
}
