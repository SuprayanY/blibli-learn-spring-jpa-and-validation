package com.blibli.future.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigInteger;
import java.time.LocalDate;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees",
       indexes = {
           @Index(name = "idx_name_departmentId",
                  columnList = "employee_name, department_id",
                  unique = false),
//           @Index(name = "uniqueIdx_employeeId", // index for primary key is auto created, so this one is not needed
//                  columnList = "employee_id",
//                  unique = true)
       })
public class Employee {

  /**
   * Available identifier generators: {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory}
   */
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2",
                    strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "employee_id")
  private String id;

  @Column(name = "employee_name",
          length = 100)
  private String name;

  @Column
  private LocalDate birthdate;

  @OneToOne
  @JoinColumn(name = "department_id",
              nullable = false)
  private Department department;

  @Column
  private BigInteger salary;

  @Version
  @Column
  private Long version;

}
