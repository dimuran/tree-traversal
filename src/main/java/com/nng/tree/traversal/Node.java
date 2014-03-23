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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Iterator;

/**
 * This interface describes the node of a tree. Provided by NNG.
 * 
 * @author Richárd Ernő Kiss
 */
public interface Node {

    /**
     * Returns the name of the node.
     * 
     * @return the node name
     */
    String getName();

    /**
     * Sets the name of the node
     * 
     * @param name the node name
     */
    void setName(String name);

    /**
     * Returns the left children of the node.
     * 
     * @return the left children
     */
    Node getLeft();

    /**
     * Sets the left children of the node.
     * 
     * @param node the left children
     */
    void setLeft(Node node);

    /**
     * Returns the right children of the node.
     * 
     * @return the right children
     */
    Node getRight();

    /**
     * Sets the right children of the node.
     * 
     * @param node the right children
     */
    void setRight(Node node);

    /**
     * Creates the iterator to use for tree traversal.
     *
     * @return the iterator
     */
    Iterator<Node> widthIterator();

    /**
     * Parses the input from the specified reader.
     * 
     * @param input the underlying reader
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the input contains a syntax error
     */
    void load(Reader input) throws IOException, ParseException;

    /**
     * Writes the node order of the tree traversal to the specified writer.
     * Uses {@link #widthIterator()} for the traversal.
     * 
     * @param output the underlying writer
     * @throws IOException if an I/O error occurs
     */
    void save(Writer output) throws IOException;

}
