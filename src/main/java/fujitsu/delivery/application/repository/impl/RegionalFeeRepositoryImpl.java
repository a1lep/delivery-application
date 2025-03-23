package fujitsu.delivery.application.repository.impl;

import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.repository.RegionalFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionalFeeRepositoryImpl implements RegionalFeeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<RegionalFee> getFeesByCityAndVehicle(String city, VehicleType vehicleType) {
        final String sql = """
                SELECT city, vehicle_type, base_fee
                FROM regional_fees
                WHERE city LIKE CONCAT('%', :city, '%') AND vehicle_type = :vehicleType
                """;
        return jdbcTemplate.query(sql, Map.of("city", city,
                                              "vehicleType", vehicleType.name()),
                (rs, _) -> mapRegionalFees(rs)).stream().findFirst();
    }

    @Override
    public void updateFees(RegionalFee regionalFee) {
        final String sql = """
            UPDATE regional_fees
            SET base_fee = :baseFee
            WHERE city = :city AND vehicle_type = :vehicleType
            """;

        Map<String, Object> params = Map.of(
                "city", regionalFee.city(),
                "vehicleType", regionalFee.vehicleType(),
                "baseFee", regionalFee.baseFee()
        );

        jdbcTemplate.update(sql, params);
    }

    private RegionalFee mapRegionalFees(ResultSet rs) throws SQLException {
        return new RegionalFee(
                VehicleType.valueOf(rs.getString("vehicle_type")),
                rs.getString("city"),
                rs.getDouble("base_fee")
        );
    }
}
