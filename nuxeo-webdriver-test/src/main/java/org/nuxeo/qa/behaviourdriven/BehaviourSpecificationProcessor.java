/*
 * (C) Copyright 2010 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Contributors: Sun Seng David TAN <stan@nuxeo.com>
 */
package org.nuxeo.qa.behaviourdriven;

import org.concordion.api.Resource;
import org.concordion.api.ResultSummary;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.util.Check;

/**
 *
 * @author Sun Seng David TAN <stan@nuxeo.com>
 * @author Stephane Lacoin <slacoin@nuxeo.com>
 *
 */
public class BehaviourSpecificationProcessor {

    public BehaviourSpecificationProcessor(Fixture fixture) {
        this.fixture = fixture;
    }

    private Fixture fixture;

    public void processSpecification() throws Throwable {
        ResultSummary resultSummary = new ConcordionBuilder().build().process(
                locateSpecification(fixture), fixture);
        resultSummary.print(System.out, fixture);
        resultSummary.assertIsSatisfied(fixture);
    }

    private Resource locateSpecification(Object fixture) {
        Check.notNull(fixture, "Fixture is null");

        String dottedClassName = fixture.getClass().getName();
        String slashedClassName = dottedClassName.replaceAll("\\.", "/");
        String specificationName = slashedClassName.replaceAll(
                "(Mock|Test)Case$", "");
        String resourcePath = "/" + specificationName + ".html";

        return new Resource(resourcePath);
    }
}
