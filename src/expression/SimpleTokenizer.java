package expression;

/**
 * This class get the idea from Eric's MySimpleTokenizer.
 * I just make some modification in the class which enables it to receive more operators.
 *  @author Zhenge Jia
 */

public class SimpleTokenizer extends Tokenizer {

	private String text;
	private int pos;
	private Object current;

	static final char whitespace[] = { ' ', '\n', '\t' };
	static final char symbol[] = { '(', ')', '-', '/', '^', '*', '+', 'e', 'p',  ',', '=', '%', ':'  }; // The symbol[] enables the token read more operators.

	public SimpleTokenizer(String text) {
		this.text = text;
		this.pos = 0;
		next();
	}

	boolean hasNext() {
		return current != null;
	}

	Object current() {
		return current;
	}

	public void next() {
		consumewhite();
		if (pos == text.length()) {
			current = null;

		} else if (isin(text.charAt(pos), symbol)) {
			current = "" + text.charAt(pos);
			pos++;

		} else if (Character.isDigit(text.charAt(pos))) {
			int start = pos;
			while (pos < text.length() && Character.isDigit(text.charAt(pos)) )
				pos++;
			if (pos+1 < text.length() && text.charAt(pos) == '.' && Character.isDigit(text.charAt(pos+1))) {
				pos++;
				while (pos < text.length() && Character.isDigit(text.charAt(pos)))
					pos++;
				current = Double.parseDouble(text.substring(start, pos));
			} else {
			    current = Double.parseDouble(text.substring(start, pos));
			}

		} else {
			int start = pos;
			while (pos < text.length() && !isin(text.charAt(pos), symbol) && !isin(text.charAt(pos), whitespace))
				pos++;
			current = text.substring(start, pos);
		}
	}

	

	private void consumewhite() {
		while (pos < text.length() && isin(text.charAt(pos), whitespace))
			pos++;
	}

	private boolean isin(char c, char charlist[]) {
		for (char w : charlist) {
			if (w == c)
				return true;
		}
		return false;
	}

}
