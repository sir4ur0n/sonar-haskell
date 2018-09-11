package com.github.sir4ur0n.sonar.haskell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

@ExtendWith(MockitoExtension.class)
class SonarHaskellPluginTest {

  private SonarHaskellPlugin cut;

  @BeforeEach
  void setUp() {
    cut = new SonarHaskellPlugin();
  }

  @Test
  void testSonarHaskellPlugin() {
    Plugin.Context context = new Plugin.Context(
        SonarRuntimeImpl.forSonarQube(Version.create(6, 0), SonarQubeSide.SERVER));

    cut.define(context);

    assertThat(context.getExtensions()).hasSize(6);
  }

}
