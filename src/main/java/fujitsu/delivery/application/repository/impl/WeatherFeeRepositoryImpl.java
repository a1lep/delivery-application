package fujitsu.delivery.application.repository.impl;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.repository.WeatherFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WeatherFeeRepositoryImpl implements WeatherFeeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveOrUpdateWeatherFee(WeatherFee weatherFee) {
        final String sql = """
                INSERT INTO weather_fees (vehicle_type, condition_type, min_value, max_value, weather_phenomenon, extra_fee)
                VALUES (:vehicleType, :conditionType, :minValue, :maxValue, :weatherPhenomenon, :extraFee)
                ON DUPLICATE KEY UPDATE
                    min_value = :minValue,
                    max_value = :maxValue,
                    weather_phenomenon = :weatherPhenomenon,
                    extra_fee = :extraFee
                """;
        jdbcTemplate.update(sql, Map.of(
                "vehicleType", weatherFee.vehicleType(),
                "conditionType", weatherFee.conditionType().name(),
                "minValue", weatherFee.minValue(),
                "maxValue", weatherFee.maxValue(),
                "weatherPhenomenon", weatherFee.weatherPhenomenon(),
                "extraFee", weatherFee.extraFee()
        ));
    }

    @Override
    public List<WeatherFee> getWeatherFeeByVehicleAndCondition(VehicleType vehicleType) {
        final String sql = """
                SELECT id, vehicle_type, condition_type, min_value, max_value, weather_phenomenon, extra_fee
                FROM weather_fees
                WHERE vehicle_type = :vehicleType 
                """;

        return jdbcTemplate.query(sql,
                Map.of("vehicleType", vehicleType.name()),
                (rs, _) -> mapWeatherFees(rs));
    }

    private WeatherFee mapWeatherFees(ResultSet rs) throws SQLException {
        return new WeatherFee(
                ConditionType.valueOf(rs.getString("condition_type")),
                rs.getDouble("min_value"),
                rs.getDouble("max_value"),
                rs.getString("weather_phenomenon"),
                VehicleType.valueOf(rs.getString("vehicle_type")),
                rs.getDouble("extra_fee")
        );
    }
}

