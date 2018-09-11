package com.github.sir4ur0n.sonar.haskell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Wither
public
class HLintError {

  private String hint;
  private String filePath;
  private int line;
  private String description;

}
