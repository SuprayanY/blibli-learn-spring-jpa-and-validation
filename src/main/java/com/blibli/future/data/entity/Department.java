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
import javax.persistence.Table;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments",
       indexes = {
           @Index(name = "idx_name",
                  columnList = "name")
       })
public class Department {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2",
                    strategy = "org.hibernate.id.UUIDGenerator")
  @Column
  private String id;

  @Column(length = 100)
  private String name;

}
