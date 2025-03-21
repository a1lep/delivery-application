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
                                            condition_value JSON NOT NULL,
                                            extra_fee DECIMAL(5,2) NOT NULL
);
-- {
--   "min" : -10.0,
--   "max" : 0.0
-- }
INSERT INTO weather_fees (vehicle_type, condition_type, condition_value, extra_fee)
SELECT * FROM (
               SELECT
                   'Scooter' AS vehicle_type, 'AIR_TEMPERATURE' AS condition_type, '{"min": -100, "max": -10}' AS condition_value, 1.00 AS extra_fee
               UNION ALL
               SELECT 'Scooter', 'AIR_TEMPERATURE', '{"min": -10, "max": 0}', 0.50
               UNION ALL
               SELECT 'Bike', 'AIR_TEMPERATURE', '{"min": -100, "max": -10}', 1.00
               UNION ALL
               SELECT 'Bike', 'AIR_TEMPERATURE', '{"min": -10, "max": 0}', 0.50
               UNION ALL
               SELECT 'Bike', 'WIND_SPEED', '{"min": 10, "max": 20}', 0.50
               UNION ALL
               SELECT 'Bike', 'WIND_SPEED', '{"min": 20.01, "max": 100}', -1
               UNION ALL
               SELECT 'Scooter', 'WEATHER_PHENOMENON', '{"phenomena": ["snow", "sleet"]}', 1.00
               UNION ALL
               SELECT 'Bike', 'WEATHER_PHENOMENON', '{"phenomena": ["snow", "sleet"]}', 1.00
               UNION ALL
               SELECT 'Scooter', 'WEATHER_PHENOMENON', '{"phenomena": ["rain"]}', 0.50
               UNION ALL
               SELECT 'Bike', 'WEATHER_PHENOMENON', '{"phenomena": ["rain"]}', 0.50
               UNION ALL
               SELECT 'Scooter', 'WEATHER_PHENOMENON', '{"phenomena": ["glaze", "hail", "thunder"]}', -1
               UNION ALL
               SELECT 'Bike', 'WEATHER_PHENOMENON', '{"phenomena": ["glaze", "hail", "thunder"]}', -1
              ) AS new_entries
WHERE NOT EXISTS (SELECT 1 FROM weather_fees);