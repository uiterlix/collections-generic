/*
 *  Copyright 2003-2004 The Apache Software Foundation
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
 */
package org.apache.commons.collections15.map;

import junit.framework.Test;
import org.apache.commons.collections15.BulkTest;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.commons.collections15.collection.TestTransformedCollection;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Extension of {@link AbstractTestSortedMap} for exercising the {@link TransformedSortedMap}
 * implementation.
 *
 * @author Matt Hall, John Watkinson, Stephen Colebourne
 * @version $Revision: 1.1 $ $Date: 2005/10/11 17:06:04 $
 * @since Commons Collections 3.0
 */
public class TestTransformedSortedMap extends AbstractTestSortedMap {

    public TestTransformedSortedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestTransformedSortedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = {TestTransformedSortedMap.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    public Map makeEmptyMap() {
        return TransformedSortedMap.decorate(new TreeMap(), TransformerUtils.nopTransformer(), TransformerUtils.nopTransformer());
    }

    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    //-----------------------------------------------------------------------    
    public void testTransformedMap() {
        Object[] els = new Object[]{"1", "3", "5", "7", "2", "4", "6"};

        Map map = TransformedSortedMap.decorate(new TreeMap(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER, null);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put(els[i], els[i]);
            assertEquals(i + 1, map.size());
            assertEquals(true, map.containsKey(new Integer((String) els[i])));
            try {
                map.containsKey(els[i]);
                fail();
            } catch (ClassCastException ex) {
            }
            assertEquals(true, map.containsValue(els[i]));
            assertEquals(els[i], map.get(new Integer((String) els[i])));
        }

        try {
            map.remove(els[0]);
            fail();
        } catch (ClassCastException ex) {
        }
        assertEquals(els[0], map.remove(new Integer((String) els[0])));

        map = TransformedSortedMap.decorate(new TreeMap(), null, TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, map.size());
        for (int i = 0; i < els.length; i++) {
            map.put(els[i], els[i]);
            assertEquals(i + 1, map.size());
            assertEquals(true, map.containsValue(new Integer((String) els[i])));
            assertEquals(false, map.containsValue(els[i]));
            assertEquals(true, map.containsKey(els[i]));
            assertEquals(new Integer((String) els[i]), map.get(els[i]));
        }

        assertEquals(new Integer((String) els[0]), map.remove(els[0]));

        Set entrySet = map.entrySet();
        Map.Entry[] array = (Map.Entry[]) entrySet.toArray(new Map.Entry[0]);
        array[0].setValue("66");
        assertEquals(new Integer(66), array[0].getValue());
        assertEquals(new Integer(66), map.get(array[0].getKey()));

        Map.Entry entry = (Map.Entry) entrySet.iterator().next();
        entry.setValue("88");
        assertEquals(new Integer(88), entry.getValue());
        assertEquals(new Integer(88), map.get(entry.getKey()));
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

    //    public void testCreate() throws Exception {
    //        resetEmpty();
    //        writeExternalFormToDisk(
    //            (java.io.Serializable) map,
    //            "D:/dev/collections15/data/test/TransformedSortedMap.emptyCollection.version3.1.obj");
    //        resetFull();
    //        writeExternalFormToDisk(
    //            (java.io.Serializable) map,
    //            "D:/dev/collections15/data/test/TransformedSortedMap.fullCollection.version3.1.obj");
    //    }
}
