package com.github.sir4ur0n.sonar.haskell.scannerside;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.config.Settings;

@ExtendWith(MockitoExtension.class)
class HaskellLintIssuesLoaderSensorTest {

  private HaskellLintIssuesLoaderSensor cut;

  @BeforeEach
  void setUp() {
    cut = new HaskellLintIssuesLoaderSensor(new Settings(), new DefaultFileSystem((File) null));
  }

  @Test
  @DisplayName("When , then ")
  void name() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();

    cut.describe(descriptor);

    assertThat(descriptor.name()).isEqualTo("HaskellLint Issues Loader Sensor");
    assertThat(descriptor.languages()).containsOnly("haskell");
  }
}
