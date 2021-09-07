package com.blibli.future.data.repository.impl;

import com.blibli.future.data.entity.Employee;
import com.blibli.future.data.repository.EmployeeCustomRepository;
import com.blibli.future.data.web.model.employee.EmployeeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Slf4j
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

  private final EntityManager entityManager;

  public EmployeeCustomRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Page<Employee> findByFilter(EmployeeFilter filter) {
    // Paging data
    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

    // Select query
    String contentQueryString = "SELECT e FROM Employee e JOIN Department d ON d.id = e.department";

    // Count query
    String countQueryString = "SELECT COUNT(1) FROM Employee e JOIN Department d ON d.id = e.department";

    // Criteria list for the WHERE conditions
    List<String> criteriaList = new ArrayList<>();

    // Parameters for the query
    Map<String, Object> parameters = new HashMap<>();

    if (StringUtils.hasText(filter.getName())) {
      criteriaList.add("LOWER(e.name) LIKE :nameContaining");
      parameters.put("nameContaining", String.format("%%%s%%", filter.getName().toLowerCase()));
    }

    if (StringUtils.hasText(filter.getDepartment())) {
      // For multi conditions, enclosed with braces
      criteriaList.add("(LOWER(d.id) = :department OR LOWER(d.name) LIKE :departmentContaining)");
      parameters.put("department", filter.getDepartment().toLowerCase());
      parameters.put("departmentContaining", String.format("%%%s%%", filter.getDepartment().toLowerCase()));
    }

    // Construct WHERE clause if needed
    if (!criteriaList.isEmpty()) {
      // Join all conditions using AND operator
      String whereQueries = " WHERE " + String.join(" AND ", criteriaList);
      contentQueryString += whereQueries;
      countQueryString += whereQueries;
    }

    // Create query object
    TypedQuery<Employee> contentQuery = entityManager.createQuery(contentQueryString, Employee.class)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize());
    TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString, Long.class);

    // Set parameters to both Select and Count query
    Arrays.asList(contentQuery, countQuery).forEach(query -> parameters.forEach(query::setParameter));

    long total = countQuery.getSingleResult();
    List<Employee> employees = contentQuery.getResultList();
    return new PageImpl<>(employees, pageable, total);
  }

}
