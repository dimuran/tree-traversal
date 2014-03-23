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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Richárd Ernő Kiss
 */
public class BinaryTreeInputParserTest {

    public BinaryTreeInputParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createConfiguredStreamTokenizer method, of class BinaryTreeInputParser.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testCreateConfiguredStreamTokenizer() throws IOException {
        Reader input = new InputStreamReader(new ByteArrayInputStream("(R1R2L3   ,,  )".getBytes()));
        StreamTokenizer st = BinaryTreeInputParser.createConfiguredStreamTokenizer(input);
        List<String> result = new ArrayList();
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            result.add(BinaryTreeInputParser.currentTokenToString(st));
        }
        assertEquals(Arrays.asList("(", "R1R2L3", ",", ",", ")"), result);
    }

    /**
     * Test of parseNodeName method, of class BinaryTreeInputParser.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testParseNodeName() throws Exception {
        Node currentNode = new BinaryTreeNode();
        StreamTokenizer st = new StreamTokenizer(new InputStreamReader(new ByteArrayInputStream("(root,".getBytes())));
        BinaryTreeInputParser.parseNodeName(currentNode, st);
        assertEquals("Node name must be \"root\"", "root", currentNode.getName());
    }

    /**
     * Test of parseChildNode method, of class BinaryTreeInputParser.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testParseChildNode() throws Exception {
        testParseChildNodeLeft();
        testParseChildNodeLeftEmpty();
        testParseChildNodeRight();
        testParseChildNodeRightEmpty();
    }

    public void testParseChildNodeLeft() throws Exception {
        Node parentNode = new BinaryTreeNode();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream("(L1,,),".getBytes()));
        PushbackReader pr = new PushbackReader(isr);
        StreamTokenizer st = new StreamTokenizer(pr);
        Boolean isFirstChild = true;
        BinaryTreeInputParser.parseChildNode(parentNode, st, pr, isFirstChild);
        assertEquals("Left child name must be \"L1\"", "L1", parentNode.getLeft().getName());
    }

    public void testParseChildNodeLeftEmpty() throws Exception {
        Node parentNode = new BinaryTreeNode();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(",".getBytes()));
        PushbackReader pr = new PushbackReader(isr);
        StreamTokenizer st = new StreamTokenizer(pr);
        Boolean isFirstChild = true;
        BinaryTreeInputParser.parseChildNode(parentNode, st, pr, isFirstChild);
        assertNull("Left child must be 'null'", parentNode.getLeft());
    }

    public void testParseChildNodeRight() throws Exception {
        Node parentNode = new BinaryTreeNode();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream("(R1,,))".getBytes()));
        PushbackReader pr = new PushbackReader(isr);
        StreamTokenizer st = new StreamTokenizer(pr);
        Boolean isFirstChild = false;
        BinaryTreeInputParser.parseChildNode(parentNode, st, pr, isFirstChild);
        assertEquals("Right child name must be be \"R1\"", "R1", parentNode.getRight().getName());
    }

    public void testParseChildNodeRightEmpty() throws Exception {
        Node parentNode = new BinaryTreeNode();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(")".getBytes()));
        PushbackReader pr = new PushbackReader(isr);
        StreamTokenizer st = new StreamTokenizer(pr);
        Boolean isFirstChild = false;
        BinaryTreeInputParser.parseChildNode(parentNode, st, pr, isFirstChild);
        assertNull("Right child must be 'null'", parentNode.getRight());
    }

    /**
     * Test of checkNextToken method, of class BinaryTreeInputParser.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCheckNextToken() throws Exception {
         //( -> 40
         //) -> 41
         //, -> 44
        StreamTokenizer st = new StreamTokenizer(new InputStreamReader(new ByteArrayInputStream(",".getBytes())));
        int expectedToken = 44;
        BinaryTreeInputParser.checkNextToken(st, expectedToken);
    }

    /**
     * Test of currentTokenToString method, of class BinaryTreeInputParser.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testCurrentTokenToString() throws IOException {
        testCurrentWordTokenToString();
        testCurrentCharTokenToString();
    }

    public void testCurrentWordTokenToString() throws IOException {
        StreamTokenizer st = new StreamTokenizer(new InputStreamReader(new ByteArrayInputStream("test".getBytes())));
        String expResult = "test";
        st.nextToken();
        String result = BinaryTreeInputParser.currentTokenToString(st);
        assertEquals(expResult, result);
    }

    public void testCurrentCharTokenToString() throws IOException {
        StreamTokenizer st = new StreamTokenizer(new InputStreamReader(new ByteArrayInputStream(",".getBytes())));
        String expResult = ",";
        st.nextToken();
        String result = BinaryTreeInputParser.currentTokenToString(st);
        assertEquals(expResult, result);
    }

}
