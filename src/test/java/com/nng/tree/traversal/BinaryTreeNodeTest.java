/*
 * Copyright (C) 2014 Richárd Ernő Kiss
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nng.tree.traversal;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Richárd Ernő Kiss
 */
public class BinaryTreeNodeTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    /**
     *        root
     *    /         \
     *   L1          R1
     *   / \           \
     * L1L2 L1R2      R1R2
     *   \             /
     *  L1L2R3       R1R2L3
     */
    

    /**
     * Test of load method, of class BinaryTreeNode.
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testLoad() throws Exception {
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(L1,   (L1L2    ,,(L1L2R3,   ,)),(L1R2,,)),(R1,    ,(R1R2,(R1R2L3,,    ),)))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
        
        assertEquals("root", instance.getName());
        //parent: root
        assertEquals("L1", instance.getLeft().getName());
        assertEquals("R1", instance.getRight().getName());
        //parent: L1
        assertEquals("L1L2", instance.getLeft().getLeft().getName());
        assertEquals("L1R2", instance.getLeft().getRight().getName());
        //parent: L1R2
        assertNull(instance.getLeft().getRight().getRight());
        assertNull(instance.getLeft().getRight().getLeft());
        //parent: L1L2
        assertNull(instance.getLeft().getLeft().getLeft());
        assertEquals("L1L2R3", instance.getLeft().getLeft().getRight().getName());
        //parent: L1L2R3
        assertNull(instance.getLeft().getLeft().getRight().getLeft());
        assertNull(instance.getLeft().getLeft().getRight().getRight());
        //parent: R1
        assertNull(instance.getRight().getLeft());
        assertEquals("R1R2", instance.getRight().getRight().getName());
        //parent: R1R2
        assertEquals("R1R2L3", instance.getRight().getRight().getLeft().getName());
        assertNull(instance.getRight().getRight().getRight());
        //parent: R1R2L3
        assertNull(instance.getRight().getRight().getLeft().getLeft());
        assertNull(instance.getRight().getRight().getLeft().getRight());
    }
    
    @org.junit.Test
    public void testLoadError1() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, expected node identifier, but found: ,");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(,,),(R1,,))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    @org.junit.Test
    public void testLoadError2() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, expected ',', but found: (");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(L1,,)(,(R1,,))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    @org.junit.Test
    public void testLoadError3() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, expected '(' or ',', but found: )");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(L1,),(R1,,))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    @org.junit.Test
    public void testLoadError4() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, expected '(' or ')', but found:");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(L1,,),".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    @org.junit.Test
    public void testLoadError5() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, expected ',', but found: ot");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(ro   ot,(1L,,),(1R,,))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    @org.junit.Test
    public void testLoadError6() throws Exception {
        exception.expect(ParseException.class);
        exception.expectMessage("Syntax error, node name can not start with a number: 1L");
        InputStreamReader input = new InputStreamReader(new ByteArrayInputStream("(root,(1L,,),(1R,,))".getBytes()));
        BinaryTreeNode instance = new BinaryTreeNode();
        instance.load(input);
    }
    
    /**
     * Test of save method, of class BinaryTreeNode.
     *
     * @throws java.lang.Exception
     */
    @org.junit.Test
    public void testSave() throws Exception {
        Writer output = new StringWriter();
        Node instance = new BinaryTreeNode("root",//
                new BinaryTreeNode("L1", //
                        new BinaryTreeNode("L1L2", null, //
                                new BinaryTreeNode("L1L2R3", null, null)),//
                        new BinaryTreeNode("L1R2", null, null)),//
                new BinaryTreeNode("R1", null, //
                        new BinaryTreeNode("R1R2", //
                                new BinaryTreeNode("R1R2L3", null, null), null)));
        String expected = "root L1 R1 L1L2 L1R2 R1R2 L1L2R3 R1R2L3";
        instance.save(output);
        assertEquals(expected, output.toString());
    }

}
