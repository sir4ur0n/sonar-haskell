package com.github.sir4ur0n.sonar.haskell.allsides;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HaskellLanguageTest {

  private HaskellLanguage cut;

  @BeforeEach
  void setUp() {
    cut = new HaskellLanguage();
  }

  @Test
  void onlyHSSuffixSupported() {
    assertThat(cut.getFileSuffixes()).containsOnly("hs");
  }
}
