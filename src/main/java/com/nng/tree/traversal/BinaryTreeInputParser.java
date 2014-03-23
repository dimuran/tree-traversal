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
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of static helper methods to parse tokens and to validate the
 * syntax of the tokens.
 *
 * @author Richárd Ernő Kiss
 */
public class BinaryTreeInputParser {

    //( -> 40
    private static final int LEFT_PARENTHESIS = 40;
    //) -> 41
    private static final int RIGHT_PARENTHESIS = 41;
    //, -> 44
    private static final int COMMA = 44;

    private static final Pattern nodeNamePattern = Pattern.compile("^[a-zA-z]{1}[0-9a-zA-z]*");

    /**
     * Configures a stream tokenizer for the grammar.
     * <br>&lt;node&gt; ::= "(" string "," &lt;node&gt; "," &lt;node&gt; ")"
     * <br>
     *
     * The default config is almost good, regarding whitespace and token
     * splitting. Removed numbers from TT_NUMBER and add to TT_WORD, also added
     * EOL to the split tokens.
     *
     * @param input the underlying reader
     * @return the configured stream tokenizer
     */
    protected static StreamTokenizer createConfiguredStreamTokenizer(Reader input) {
        StreamTokenizer st = new StreamTokenizer(input);

        st.ordinaryChar('0');
        st.ordinaryChar('1');
        st.ordinaryChar('2');
        st.ordinaryChar('3');
        st.ordinaryChar('4');
        st.ordinaryChar('5');
        st.ordinaryChar('6');
        st.ordinaryChar('7');
        st.ordinaryChar('8');
        st.ordinaryChar('9');
        st.ordinaryChar('.');
        st.ordinaryChar('-');
        // 0 1 2 3 4 5 6 7 8 9
        st.wordChars('\u0030', '\u0039');
        st.eolIsSignificant(true);
        return st;
    }

    /**
     * Parses and validates the indentifier of the specified node from the
     * specified stream tokenizer. Uses {@link #checkNextToken(java.io.StreamTokenizer, int)
     * } for token validation.
     *
     * @param currentNode the node to set the name to
     * @param streamTokenizer the stream tokenizer that contains the tokens
     * @throws ParseException if the input contains a syntax error
     * @throws IOException if an I/O error occurs
     */
    protected static void parseNodeName(Node currentNode, StreamTokenizer streamTokenizer) throws ParseException, IOException {
        //first token must be '('
        checkNextToken(streamTokenizer, LEFT_PARENTHESIS);
        streamTokenizer.nextToken();
        if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) { //token must be word -> node name
            //name must start with a character
            Matcher matcher = nodeNamePattern.matcher(streamTokenizer.sval);
            if (!matcher.matches()) {
                if (Character.isDigit(streamTokenizer.sval.charAt(0))) {
                    throw new ParseException("Syntax error, node name can not start with a number: " + currentTokenToString(streamTokenizer), 0);
                } else{
                    throw new ParseException("Syntax error, node name must consist of english letters and numbers: " + currentTokenToString(streamTokenizer), 0);
                }
            }
            currentNode.setName(streamTokenizer.sval);
            //next token must be a comma
            checkNextToken(streamTokenizer, COMMA);
        } else {
            throw new ParseException("Syntax error, expected node identifier, but found: " + currentTokenToString(streamTokenizer), 0);
        }
    }

    /**
     * Parses child nodes from the specified stream tokenizer and sets it to the
     * specified parent node depeneding on the specified boolean.
     *
     * @param parentNode the node to set the children to
     * @param streamTokenizer he stream tokenizer that contains the tokens
     * @param pushbackReader the reader with pushback buffer
     * @param isFirstChild indicates if it is a left or right child node
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the input contains a syntax error
     */
    protected static void parseChildNode(Node parentNode, StreamTokenizer streamTokenizer, PushbackReader pushbackReader, Boolean isFirstChild) throws IOException, ParseException {
        int expectedTokenAfterChild = isFirstChild ? COMMA : RIGHT_PARENTHESIS;

        streamTokenizer.nextToken();
        if (streamTokenizer.ttype == LEFT_PARENTHESIS) {
            pushbackReader.unread((int) '(');       //the Reader buffer will start with '(' when we pass it forward
            //create new child
            Node childNode = new BinaryTreeNode();
            childNode.load(pushbackReader);
            if (isFirstChild) {
                parentNode.setLeft(childNode);
            } else {
                parentNode.setRight(childNode);
            }
            //next token must be a comma if first child or a right parenthesis if last child
            checkNextToken(streamTokenizer, expectedTokenAfterChild);
        } else if (streamTokenizer.ttype == expectedTokenAfterChild) {
            //empty children, nothing to do
        } else {
            throw new ParseException("Syntax error, expected '(' or '" + (char) expectedTokenAfterChild + "', but found: " + currentTokenToString(streamTokenizer), 0);
        }
    }

    /**
     * Checks if the next token in the specified stream matches the specified
     * token.
     *
     * @param stremTokenizer the stream tokenizer that contains the tokens
     * @param expectedToken the next token that we expect in the stream
     * tokenizer
     * @throws ParseException if the provided token does not match the token in
     * the stream tokenizer
     * @throws IOException if an I/O error occurs
     */
    protected static void checkNextToken(StreamTokenizer stremTokenizer, int expectedToken) throws ParseException, IOException {
        stremTokenizer.nextToken();
        if (stremTokenizer.ttype != expectedToken) {
            throw new ParseException("Syntax error, expected '" + (char) expectedToken + "', but found: " + currentTokenToString(stremTokenizer), 0);
        }

    }

    /**
     * Converts the current token in the specified stream tokenizer to string.
     *
     * @param streamTokenizer the stream tokenizer
     * @return the string representation of the token
     */
    protected static String currentTokenToString(StreamTokenizer streamTokenizer) {
        String ret;
        switch (streamTokenizer.ttype) {
            case StreamTokenizer.TT_WORD:
                ret = streamTokenizer.sval;
                break;
            case StreamTokenizer.TT_NUMBER:
                ret = Double.toString(streamTokenizer.nval);
                break;
            default:
                if (streamTokenizer.ttype == StreamTokenizer.TT_EOL) {
                    ret = "EndOfLine";
                } else {
                    ret = Character.toString((char) streamTokenizer.ttype);
                }
        }
        return ret;
    }

}
