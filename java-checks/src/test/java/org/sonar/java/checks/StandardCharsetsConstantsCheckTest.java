/*
 * SonarQube Java
 * Copyright (C) 2012-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.internal.InternalCheckVerifier;

import static org.sonar.java.checks.verifier.TestUtils.mainCodeSourcesPath;
import static org.sonar.java.checks.verifier.TestUtils.nonCompilingTestSourcesPath;

class StandardCharsetsConstantsCheckTest {

  @Test
  void test() {
    InternalCheckVerifier.newInstance()
      .onFile(mainCodeSourcesPath("checks/StandardCharsetsConstantsCheck.java"))
      .withCheck(new StandardCharsetsConstantsCheck())
      .withQuickFixes()
      .verifyIssues();
    InternalCheckVerifier.newInstance()
      .onFile(mainCodeSourcesPath("checks/StandardCharsetsConstantsCheck.java"))
      .withCheck(new StandardCharsetsConstantsCheck())
      .withJavaVersion(7)
      .withQuickFixes()
      .verifyIssues();
    InternalCheckVerifier.newInstance()
      .onFile(mainCodeSourcesPath("checks/StandardCharsetsConstantsCheck_java8.java"))
      .withCheck(new StandardCharsetsConstantsCheck())
      .withJavaVersion(8)
      .verifyIssues();
    InternalCheckVerifier.newInstance()
      .onFile(nonCompilingTestSourcesPath("checks/StandardCharsetsConstantsCheck_java10.java"))
      .withCheck(new StandardCharsetsConstantsCheck())
      .withJavaVersion(10)
      .verifyIssues();
    InternalCheckVerifier.newInstance()
      .onFile(mainCodeSourcesPath("checks/StandardCharsetsConstantsCheck.java"))
      .withCheck(new StandardCharsetsConstantsCheck())
      .withJavaVersion(6)
      .withQuickFixes()
      .verifyNoIssues();
  }
}
