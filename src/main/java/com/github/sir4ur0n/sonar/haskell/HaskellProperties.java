package com.github.sir4ur0n.sonar.haskell;

import static io.vavr.API.List;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sonar.api.config.PropertyDefinition;

/**
 * Configurable properties (visible in SonarQube admin, overridable in sonar-project.properties)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HaskellProperties {

  public static final String REPORT_PATH_KEY = "sonar.hlint.reportPath";
  private static final String REPORT_PATH_DEFAULT = "hlintReport.json";

  static List<PropertyDefinition> getProperties() {
    return List(
        PropertyDefinition.builder(REPORT_PATH_KEY)
            .defaultValue(REPORT_PATH_DEFAULT)
            .category("Haskell")
            .name("hlint report path")
            .description("hlint report relative path").build());
  }
}
