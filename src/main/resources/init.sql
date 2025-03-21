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
                                            vehicle_type VARCHAR(50) NOT NULL,
                                            condition_type ENUM('AIR_TEMPERATURE', 'WIND_SPEED', 'WEATHER_PHENOMENON') NOT NULL,
                                            min_value DOUBLE NULL,
                                            max_value DOUBLE NULL,
                                            weather_phenomenon VARCHAR(255) NULL,
                                            extra_fee DECIMAL(5,2) NOT NULL
);

INSERT INTO regional_fees (city, vehicle_type, base_fee)
SELECT * FROM (
                  SELECT 'Tallinn', 'Car', 4.00 UNION ALL
                  SELECT 'Tallinn', 'Scooter', 3.50 UNION ALL
                  SELECT 'Tallinn', 'Bike', 3.00 UNION ALL
                  SELECT 'Tartu', 'Car', 3.50 UNION ALL
                  SELECT 'Tartu', 'Scooter', 3.00 UNION ALL
                  SELECT 'Tartu', 'Bike', 2.50 UNION ALL
                  SELECT 'Pärnu', 'Car', 3.00 UNION ALL
                  SELECT 'Pärnu', 'Scooter', 2.50 UNION ALL
                  SELECT 'Pärnu', 'Bike', 2.00
              ) AS new_entries
WHERE NOT EXISTS (
    SELECT 1 FROM regional_fees
);

INSERT INTO weather_fees (vehicle_type, condition_type, min_value, max_value, weather_phenomenon, extra_fee)
SELECT * FROM (
                  SELECT 'Scooter', 'AIR_TEMPERATURE', -100, -10, NULL, 1.00 UNION ALL
                  SELECT 'Scooter', 'AIR_TEMPERATURE', -10, 0, NULL, 0.50 UNION ALL
                  SELECT 'Bike', 'AIR_TEMPERATURE', -100, -10, NULL, 1.00 UNION ALL
                  SELECT 'Bike', 'AIR_TEMPERATURE', -10, 0, NULL, 0.50 UNION ALL
                  SELECT 'Bike', 'WIND_SPEED', 10, 20, NULL, 0.50 UNION ALL
                  SELECT 'Bike', 'WIND_SPEED', 20.01, 100, NULL, -1 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'snow', 1.00 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'sleet', 1.00 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'snow', 1.00 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'sleet', 1.00 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'rain', 0.50 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'rain', 0.50 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'glaze', -1 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'hail', -1 UNION ALL
                  SELECT 'Scooter', 'WEATHER_PHENOMENON', NULL, NULL, 'thunder', -1 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'glaze', -1 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'hail', -1 UNION ALL
                  SELECT 'Bike', 'WEATHER_PHENOMENON', NULL, NULL, 'thunder', -1
              ) AS new_entries
WHERE NOT EXISTS (
    SELECT 1 FROM weather_fees
);
