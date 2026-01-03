package www.spring.com.empleados_2monolito.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import www.spring.com.empleados_2monolito.entity.EmployeeDTO;
import www.spring.com.empleados_2monolito.repository.EmpleadoRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final JdbcTemplate jdbcTemplate;
    private final EmpleadoRepository repository;

    public EmpleadoServiceImpl(JdbcTemplate jdbcTemplate, EmpleadoRepository repository) {
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
    }

    @Override
    public Page<EmployeeDTO> buscarConFiltros(
            Integer empNo,
            LocalDate birthDate,
            String firstName,
            String lastName,
            EmployeeDTO.Gender gender,
            LocalDate hireDate,
            String deptNo,
            LocalDate fromDate,
            LocalDate toDate,
            int page,
            int size
    ) {

        StringBuilder baseSql = new StringBuilder("""
                FROM employees e
                LEFT JOIN dept_emp de
                  ON e.emp_no = de.emp_no
                WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (empNo != null) { baseSql.append(" AND e.emp_no = ? "); params.add(empNo); }
        if (birthDate != null) { baseSql.append(" AND e.birth_date = ? "); params.add(java.sql.Date.valueOf(birthDate)); }
        if (firstName != null && !firstName.isBlank()) { baseSql.append(" AND e.first_name LIKE ? "); params.add("%" + firstName.trim() + "%"); }
        if (lastName != null && !lastName.isBlank()) { baseSql.append(" AND e.last_name LIKE ? "); params.add("%" + lastName.trim() + "%"); }
        if (gender != null) { baseSql.append(" AND e.gender = ? "); params.add(gender.name()); }
        if (hireDate != null) { baseSql.append(" AND e.hire_date = ? "); params.add(java.sql.Date.valueOf(hireDate)); }
        if (deptNo != null && !deptNo.isBlank()) { baseSql.append(" AND de.dept_no = ? "); params.add(deptNo.trim()); }
        if (fromDate != null) { baseSql.append(" AND de.from_date >= ? "); params.add(java.sql.Date.valueOf(fromDate)); }
        if (toDate != null) { baseSql.append(" AND de.to_date <= ? "); params.add(java.sql.Date.valueOf(toDate)); }

        // COUNT (sin ORDER BY / LIMIT)
        String countSql = "SELECT COUNT(*) " + baseSql;
        Integer total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);
        if (total == null) total = 0;

        Pageable pageable = PageRequest.of(page, size);

        // DATA
        String dataSql = """
                SELECT
                  e.emp_no,
                  e.birth_date,
                  e.first_name,
                  e.last_name,
                  e.gender,
                  e.hire_date,
                  de.dept_no,
                  de.from_date,
                  de.to_date
                """ + baseSql + """
                ORDER BY e.emp_no
                LIMIT ? OFFSET ?
                """;

        List<Object> pageParams = new ArrayList<>(params);
        pageParams.add(pageable.getPageSize());
        pageParams.add((int) pageable.getOffset());

        List<EmployeeDTO> content = jdbcTemplate.query(dataSql, pageParams.toArray(), new EmployeeRowMapper());

        return new PageImpl<>(content, pageable, total);
    }

    static class EmployeeRowMapper implements RowMapper<EmployeeDTO> {
        @Override
        public EmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeDTO dto = new EmployeeDTO();

            dto.setEmpNo(rs.getInt("emp_no"));
            dto.setBirthDate(getLocalDateSafe(rs, "birth_date"));
            dto.setFirstName(rs.getString("first_name"));
            dto.setLastName(rs.getString("last_name"));

            String gender = rs.getString("gender");
            if (gender != null) dto.setGender(EmployeeDTO.Gender.valueOf(gender));

            dto.setHireDate(getLocalDateSafe(rs, "hire_date"));

            dto.setDeptNo(rs.getString("dept_no"));
            dto.setFromDate(getLocalDateSafe(rs, "from_date"));
            dto.setToDate(getLocalDateSafe(rs, "to_date"));

            return dto;
        }

        private static LocalDate getLocalDateSafe(ResultSet rs, String column) throws SQLException {
            java.sql.Date date = rs.getDate(column);
            return date != null ? date.toLocalDate() : null;
        }
    }
}
