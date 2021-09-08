package com.blibli.future.data.repository;

import com.blibli.future.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
public interface DepartmentRepository extends JpaRepository<Department, String> {

  // The query will be automatically built during the compilation.
  Department findByName(String name);

}
