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

import org.concordion.api.ExpectedToFail;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.Runner;
import org.concordion.api.RunnerResult;
import org.concordion.api.Unimplemented;
import org.junit.runner.JUnitCore;

/**
 *
 * @author Sun Seng David TAN <stan@nuxeo.com>
 * @author Stephane Lacoin <slacoin@nuxeo.com>
 *
 */
public class NuxeoConcordionRunner implements Runner {

    @Override
    public RunnerResult execute(Resource resource, String href)
            throws Exception {
        String name = resource.getName();
        Resource hrefResource = resource.getParent().getRelativeResource(href);
        name = hrefResource.getPath().replaceFirst("/", "").replace("/", ".").replaceAll(
                "\\.html$", "");
        Class<?> concordionClass;
        try {
            concordionClass = Class.forName(name);
        } catch (ClassNotFoundException e) {
            try {
                concordionClass = Class.forName(name + "TestCase");
            } catch (ClassNotFoundException e2) {
                concordionClass = Class.forName(name + "MockCase");
            }
        }
        org.junit.runner.Result jUnitResult = JUnitCore.runClasses(concordionClass);

        Result result = Result.FAILURE;
        if (jUnitResult.wasSuccessful()) {
            result = Result.SUCCESS;
            if (isOnlySuccessfulBecauseItWasExpectedToFail(concordionClass)
                    || isOnlySuccessfulBecauseItIsUnimplemented(concordionClass)) {
                result = Result.IGNORED;
            }
        }
        return new RunnerResult(result);
    }

    private boolean isOnlySuccessfulBecauseItWasExpectedToFail(
            Class<?> concordionClass) {
        return concordionClass.getAnnotation(ExpectedToFail.class) != null;
    }

    private boolean isOnlySuccessfulBecauseItIsUnimplemented(
            Class<?> concordionClass) {
        return concordionClass.getAnnotation(Unimplemented.class) != null;
    }
}
