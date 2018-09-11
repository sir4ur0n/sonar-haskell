package com.github.sir4ur0n.sonar.haskell.allsides;

import org.sonar.api.resources.AbstractLanguage;

public class HaskellLanguage extends AbstractLanguage {

  public static final String KEY = "haskell";
  private static final String NAME = "Haskell";
  private static final String[] DEFAULT_FILE_SUFFIXES = {"hs"};

  /**
   * Constructor must be public for SonarQube to instantiate it
   */
  @SuppressWarnings("WeakerAccess")
  public HaskellLanguage() {
    super(KEY, NAME);
  }

  @Override
  public String[] getFileSuffixes() {
    return DEFAULT_FILE_SUFFIXES;
  }

}
