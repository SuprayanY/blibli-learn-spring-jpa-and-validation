package com.blibli.future.data.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Suprayan Yapura
 * @since 0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

  private Integer status;

  private T data;

  private Map<String, List<String>> errors;

  private Pagination pagination;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Pagination {

    private Integer page;

    private Long size;

    private Long totalItems;

  }

}
