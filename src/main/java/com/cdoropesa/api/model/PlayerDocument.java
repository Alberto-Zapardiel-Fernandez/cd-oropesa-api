package com.cdoropesa.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDocument {

  private String id;
  private String fileName;
  private String kind;
  private String contentType;
  private String storagePath;
}
