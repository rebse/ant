/*
 * Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.types.resources;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.tools.ant.types.ResourceCollection;

/**
 * ResourceCollection representing the union of multiple nested ResourceCollections.
 * @since Ant 1.7
 */
public class Union extends BaseResourceCollectionContainer {

    /**
     * Returns all Resources in String format. Moved up from
     * Path for convenience.
     * @return String array of Resources.
     */
    public String[] list() {
        if (isReference()) {
            return ((Union) getCheckedRef()).list();
        }
        Collection result = getCollection(true);
        return (String[]) (result.toArray(new String[result.size()]));
    }

    /**
     * Unify the contained Resources.
     * @return a Collection of Resources.
     */
    protected Collection getCollection() {
        return getCollection(false);
    }

    /**
     * Unify the contained Resources.
     * @param asString indicates whether the resulting Collection
     *        should contain Strings instead of Resources.
     * @return a Collection of Resources.
     */
    protected Collection getCollection(boolean asString) {
        List rc = getResourceCollections();
        if (rc.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        //preserve order-encountered using a list; enforce set logic manually:
        ArrayList union = new ArrayList(rc.size() * 2);
        for (Iterator rcIter = rc.iterator(); rcIter.hasNext();) {
            for (Iterator r = nextRC(rcIter).iterator(); r.hasNext();) {
                Object o = r.next();
                if (asString) {
                    o = o.toString();
                }
                if (!(union.contains(o))) {
                    union.add(o);
                }
            }
        }
        return union;
    }

    private static ResourceCollection nextRC(Iterator i) {
        return (ResourceCollection) i.next();
    }
}
