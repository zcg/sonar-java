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

import org.sonar.check.Rule;
import org.sonar.java.model.LineUtils;
import org.sonar.plugins.java.api.tree.SyntaxToken;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@DeprecatedRuleKey(ruleKey = "LeftCurlyBraceEndLineCheck", repositoryKey = "squid")
@Rule(key = "S1105")
public class LeftCurlyBraceEndLineCheck extends LeftCurlyBraceBaseTreeVisitor {

  @Override
  protected void checkTokens(SyntaxToken lastToken, SyntaxToken openBraceToken) {
    if (LineUtils.startLine(lastToken) != LineUtils.startLine(openBraceToken)) {
      addIssue(openBraceToken, this, "Move this left curly brace to the end of previous line of code.");
    }
  }
}
