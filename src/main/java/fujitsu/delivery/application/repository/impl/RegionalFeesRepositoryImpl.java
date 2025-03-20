package fujitsu.delivery.application.repository.impl;

import fujitsu.delivery.application.model.RegionalFees;
import fujitsu.delivery.application.repository.RegionalFeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionalFeesRepositoryImpl implements RegionalFeesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<RegionalFees> getFeesByCityAndVehicle(String city, String vehicleType) {
        final String sql = """
                SELECT city, vehicle_type, base_fee
                FROM regional_fees
                WHERE city = :city AND vehicle_type = :vehicleType
                """;
        return jdbcTemplate.query(sql, Map.of("city", city,
                                              "vehicleType", vehicleType),
                (rs, _) -> mapRegionalFees(rs)).stream().findFirst();
    }

    @Override
    public void updateFees(RegionalFees regionalFees) {
        final String sql = """
            UPDATE regional_fees
            SET base_fee = :baseFee
            WHERE city = :city AND vehicle_type = :vehicleType
            """;

        Map<String, Object> params = Map.of(
                "city", regionalFees.city(),
                "vehicleType", regionalFees.vehicleType(),
                "baseFee", regionalFees.baseFee()
        );

        jdbcTemplate.update(sql, params);
    }

    private RegionalFees mapRegionalFees(ResultSet rs) throws SQLException {
        return new RegionalFees(
                rs.getString("city"),
                rs.getString("vehicle_type"),
                rs.getDouble("base_fee")
        );
    }
}
