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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;

/**
 * Parses the input from stdin into a binary tree, then writes the node order of
 * a breadth-first tree traversal to stdout.
 * Also validates the input syntax.
 * 
 * @author Richárd Ernő Kiss
 */
public class TreeTraversal {
    
    /**
     * Application entry point.
     * 
     * @param args array of string arguemnts
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the input contains a syntax error
     */
    public static void main(String args[]) throws IOException, ParseException {
        InputStreamReader stdin = null;
        OutputStreamWriter stdout = null;
        
        try {
            System.out.println("Waiting for input:");
            stdin = new InputStreamReader(System.in);
            Node binaryTree = new BinaryTreeNode();
            binaryTree.load(stdin);
            stdout = new OutputStreamWriter(System.out);
            System.out.println("Breadth-first traversal node order: ");
            binaryTree.save(stdout);
        } finally {
            if (stdout != null) {
                stdout.close();
            }
            if (stdin != null) {
                stdin.close();
            }
        }
        System.exit(0);
    }

}
