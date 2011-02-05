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


import org.junit.Before;
import org.junit.Test;
/**
 * 
 * @author Sun Seng David TAN <stan@nuxeo.com>
 * @author Stephane Lacoin <slacoin@nuxeo.com>
 *
 */
public class BehavioursCase implements Fixture {
    private BehaviourSpecificationProcessor processor = new BehaviourSpecificationProcessor(
            this);
    
    @Test
    public void processSpecification() throws Throwable {
        processor.processSpecification();
    }
}
