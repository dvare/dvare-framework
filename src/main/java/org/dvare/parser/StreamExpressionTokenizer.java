package org.dvare.parser;

import java.io.Reader;
import java.io.StreamTokenizer;

public class StreamExpressionTokenizer extends StreamTokenizer {
    public StreamExpressionTokenizer(Reader r) {
        super(r);
    }
}
