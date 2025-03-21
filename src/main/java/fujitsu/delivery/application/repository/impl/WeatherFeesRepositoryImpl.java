package fujitsu.delivery.application.repository.impl;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFees;
import fujitsu.delivery.application.repository.WeatherFeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WeatherFeesRepositoryImpl implements WeatherFeesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveOrUpdateWeatherFee(WeatherFees weatherFees) {
        final String sql = """
                INSERT INTO weather_fees (vehicle_type, condition_type, condition_value, extra_fee)
                VALUES (:vehicleType, :conditionType, :conditionValue, :extraFee)
                ON DUPLICATE KEY UPDATE
                    condition_value = :conditionValue,
                    extra_fee = :extraFee
                """;
        jdbcTemplate.update(sql, Map.of(
                "vehicleType", weatherFees.vehicleType(),
                "conditionType", weatherFees.conditionType().name(),
                "conditionValue", weatherFees.conditionValue(),
                "extraFee", weatherFees.extraFee()
        ));
    }

    @Override
    public Optional<WeatherFees> getWeatherFeeByVehicleAndCondition(String vehicleType, ConditionType conditionType) {
        final String sql = """
                SELECT id, vehicle_type, condition_type, condition_value, extra_fee
                FROM weather_fees
                WHERE vehicle_type = :vehicleType AND condition_type = :conditionType
                LIMIT 1
                """;

        return jdbcTemplate.query(sql,
                        Map.of("vehicleType", vehicleType, "conditionType", conditionType.name()),
                        (rs, _) -> mapWeatherFees(rs))
                .stream()
                .findFirst();
    }

    private WeatherFees mapWeatherFees(ResultSet rs) throws SQLException {
        return new WeatherFees(
                ConditionType.valueOf(rs.getString("condition_type")),
                rs.getString("condition_value"),
                rs.getString("vehicle_type"),
                rs.getDouble("extra_fee")
        );
    }
}
