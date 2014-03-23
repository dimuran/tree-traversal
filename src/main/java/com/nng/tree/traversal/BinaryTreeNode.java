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

import static com.nng.tree.traversal.BinaryTreeInputParser.createConfiguredStreamTokenizer;
import static com.nng.tree.traversal.BinaryTreeInputParser.parseChildNode;
import static com.nng.tree.traversal.BinaryTreeInputParser.parseNodeName;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Binary tree implementation.
 *
 * @author Richárd Ernő Kiss
 */
public class BinaryTreeNode implements Node {

    private static final String OUTPUT_DELIMETER = " ";
    private String nodeName = null;
    private Node leftNode = null;
    private Node rightNode = null;

    /**
     * Constructs an empty node.
     */
    public BinaryTreeNode() {
    }

    /**
     * Constructs a node with the specified name and childrens.
     *
     * @param name the node name
     * @param left the left children
     * @param right the right children
     */
    public BinaryTreeNode(String name, Node left, Node right) {
        this.nodeName = name;
        this.leftNode = left;
        this.rightNode = right;
    }

    /**
     * Returns the name of the node.
     *
     * @return the node name
     */
    @Override
    public String getName() {
        return nodeName;
    }

    /**
     * Sets the name of the node
     *
     * @param name the node name
     */
    @Override
    public void setName(String name) {
        nodeName = name;
    }

    /**
     * Returns the left children of the node.
     *
     * @return the left children
     */
    @Override
    public Node getLeft() {
        return leftNode;
    }

    /**
     * Sets the left children of the node.
     *
     * @param node the left children
     */
    @Override
    public void setLeft(Node node) {
        leftNode = node;
    }

    /**
     * Returns the right children of the node.
     *
     * @return the right children
     */
    @Override
    public Node getRight() {
        return rightNode;
    }

    /**
     * Sets the right children of the node.
     *
     * @param node the right children
     */
    @Override
    public void setRight(Node node) {
        rightNode = node;
    }

    /**
     * Creates the iterator to use for breadth-first tree traversal.
     *
     * @return the iterator
     */
    @Override
    public Iterator<Node> widthIterator() {
        return new BreadthFirstIterator(this);
    }

    /**
     * Parses the input from the specified reader. Wraps the reader to a
     * {@link java.io.PushbackReader} to use its buffer for peeking ahead. Uses
     * {@link java.io.StreamTokenizer} as a lexer. Uses static methods from
     * {@link com.nng.tree.traversal.BinaryTreeInputParser} for the parsing.
     *
     * @param input the underlying reader
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the input contains a syntax error
     */
    @Override
    public void load(Reader input) throws IOException, ParseException {
        PushbackReader pushbackReader = new PushbackReader(input);
        StreamTokenizer streamTokenizer = createConfiguredStreamTokenizer(pushbackReader);

        parseNodeName(this, streamTokenizer);
        //parse left child
        parseChildNode(this, streamTokenizer, pushbackReader, true);
        //examine right child
        parseChildNode(this, streamTokenizer, pushbackReader, false);
    }

    /**
     * Writes the node order of the tree traversal to the specified writer. Uses
     * {@link #widthIterator()} for the traversal.
     *
     * @param output the underlying writer
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void save(Writer output) throws IOException {
        Boolean isFirst = true;
        try {
            Iterator<Node> breadthFirstIterator = this.widthIterator();
            while (breadthFirstIterator.hasNext()) {
                if (!isFirst | (isFirst = false)) {
                    output.append(OUTPUT_DELIMETER);
                }
                output.append(breadthFirstIterator.next().getName());
            }
        } finally {
            output.flush();
        }
    }

}
