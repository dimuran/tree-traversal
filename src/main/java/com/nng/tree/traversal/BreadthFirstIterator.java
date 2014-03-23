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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Breadth-first tree traversal iterator implementation.
 *
 * @author Richárd Ernő Kiss
 */
class BreadthFirstIterator implements Iterator<Node> {

    final private Queue<Node> queue = new LinkedList<Node>();

    public BreadthFirstIterator(Node tree) {
        queue.add(tree);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Node next() {
        Node node = queue.remove();
        if (node.getLeft() != null) {
            queue.add(node.getLeft());
        }
        if (node.getRight() != null) {
            queue.add(node.getRight());
        }
        return node;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
