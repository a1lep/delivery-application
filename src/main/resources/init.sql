CREATE TABLE IF NOT EXISTS weather (
                                       station_name VARCHAR(255) NOT NULL,
                                       wmo_code INT,
                                       air_temperature DOUBLE,
                                       wind_speed DOUBLE,
                                       phenomenon VARCHAR(255),
                                       create_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       PRIMARY KEY (station_name, create_timestamp)
);

CREATE TABLE IF NOT EXISTS regional_fees (
                                             city VARCHAR(255) NOT NULL,
                                             vehicle_type VARCHAR(255) NOT NULL,
                                             base_fee DECIMAL(5,2) NOT NULL,
                                             PRIMARY KEY (city, vehicle_type)
);

CREATE TABLE IF NOT EXISTS weather_fees (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            vehicle_type VARCHAR(255) NOT NULL,
                                            condition_type VARCHAR(255) NOT NULL,
                                            condition_value DECIMAL(5,2) NOT NULL,
                                            extra_fee DECIMAL(5,2) NOT NULL
);


-- INSERT INTO weather (station_name, wmo_code, air_temperature, wind_speed, phenomenon, timestamp) VALUES
--                                                                                                      ('Tallinn-Harku', 26038, 5.2, 3.5, 'Cloudy', 1710720000),
--                                                                                                      ('Tartu-Tõravere', 26242, 2.8, 5.0, 'Snow', 1710723600);