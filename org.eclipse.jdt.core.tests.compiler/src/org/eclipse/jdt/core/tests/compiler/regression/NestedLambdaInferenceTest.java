/*******************************************************************************
 * Copyright (c) 2026 François Martin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     François Martin - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.compiler.regression;

import junit.framework.Test;

public class NestedLambdaInferenceTest extends AbstractRegressionTest {
	private static final int NESTING_DEPTH = 31;

	public NestedLambdaInferenceTest(String name) {
		super(name);
	}

	public static Test suite() {
		return buildMinimalComplianceTestSuite(NestedLambdaInferenceTest.class, F_1_8);
	}

	// https://github.com/eclipse-jdt/eclipse.jdt.core/issues/JDT-ISSUE-PLACEHOLDER-09
	public void testNestedLambdaGenerics() {
		runNestedLambdaTest("NestedLambdaGenerics", """
			<Z extends A0> Z m(A0 t, Callable<Z> ct) { return null; }
			<Z extends A1> Z m(A1 t, Callable<Z> ct) { return null; }
			<Z extends A2> Z m(A2 t, Callable<Z> ct) { return null; }
			<Z extends A3> Z m(A3 t, Callable<Z> ct) { return null; }
			<Z extends A4> Z m(A4 t, Callable<Z> ct) { return null; }
			<Z> Z m(Object o, Callable<Z> co) { return null; }
			""");
	}

	// https://github.com/eclipse-jdt/eclipse.jdt.core/issues/JDT-ISSUE-PLACEHOLDER-09
	public void testNestedLambdaNoGenerics() {
		runNestedLambdaTest("NestedLambdaNoGenerics", """
			String m(A0 t, Callable<A0> ct) { return ""; }
			String m(A1 t, Callable<A1> ct) { return ""; }
			String m(A2 t, Callable<A2> ct) { return ""; }
			String m(A3 t, Callable<A3> ct) { return ""; }
			String m(A4 t, Callable<A4> ct) { return ""; }
			String m(Object o, Callable<String> co) { return ""; }
			""");
	}

	private void runNestedLambdaTest(String className, String overloads) {
		this.runConformTest(new String[] {
			className + ".java",
			"""
			import java.util.concurrent.Callable;

			class %s {
			    void test() {
			        %s;
			    }
			    static class A0 { }
			    static class A1 { }
			    static class A2 { }
			    static class A3 { }
			    static class A4 { }
			%s
			}
			""".formatted(className, nestedInvocation(), overloads.indent(4).stripTrailing())
		});
	}

	private static String nestedInvocation() {
		return "m(null, () -> ".repeat(NESTING_DEPTH - 1)
				+ "m(null, (Callable<String>) null)"
				+ ")".repeat(NESTING_DEPTH - 1);
	}
}
