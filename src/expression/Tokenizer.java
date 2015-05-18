package expression;



/**
 * Tokenizer - this uses the StreamTokenizer class to make a simpler tokenizer
 * which provides a stream of tokens which are either Integer, Double, or
 * String.
 * 
 * @author Eric McCreath
 * 
 */

public abstract class Tokenizer {

	abstract boolean hasNext();

	abstract Object current();

	abstract void next();
	/*
	public void parse(Object o) throws ParseException {
		
		if (current() == null || !current().equals(o))
			throw new ParseException();
		next();
	}
	*/
}
