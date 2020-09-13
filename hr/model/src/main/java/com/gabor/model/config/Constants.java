package com.gabor.model.config;

import java.time.format.DateTimeFormatter;


public class Constants {

    //30 days
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS_REMEMBER_ME = 30 * 24 * 60 * 60;

    //5 hours
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;

    public static final String ROLE_KEY = "role";

    public static final String ALLOWED_ORIGIN = "*";

    public static final String APP_URL = "http://localhost:8080/login";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final int CITY_NAME_LENGTH_MAX = 30;

    public static final int COUNTRY_NAME_LENGTH_MAX = 30;

    public static final int REQUEST_DETAILS_LENGTH_MAX = 100;

    public static final int ROLE_NAME_LENGTH_MAX = 20;

    public static final int TRANSPORTATION_MEAN_LENGTH_MAX = 20;

    public static final int USER_FIRST_NAME_LENGTH_MAX = 20;

    public static final int USER_LAST_NAME_LENGTH_MAX = 20;

    public static final int USER_EMAIL_LENGTH_MAX = 50;

    public static final int PROJECT_TITLE_NAME_LENGTH_MAX = 20;

    public static final int PROJECT_DESCRIPTION_NAME_LENGTH_MAX = 100;

    public static final int TASK_IDENTIFIER_NAME_LENGTH_MAX = 20;

    public static final int TASK_DESCRIPTION_NAME_LENGTH_MAX = 100;

}
