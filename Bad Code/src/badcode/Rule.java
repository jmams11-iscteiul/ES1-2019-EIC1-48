/**
 * 
 */
package badcode;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
public class Rule {

	private String metric;
	private String operator;
	private double value;
	
	/**
	 * @param metric metric typed by the user
	 * @param operator operator typed by the user
	 * @param value value typed by the user
	 */
	public Rule(String metric, String operator, double value) {
		this.metric = metric;
		this.operator = operator;
		this.value = value;
	}


	/**
	 * Checks if the rule created by the user is valid when compared to the excel method passed in the arguments
	 * 
	 * @param em excelMethod to be compared to
	 * @return true if the rule aplies, false otherwise
	 */
	@SuppressWarnings("null")
	public boolean check(ExcelMethod em) {
		double excel = 0;

		//verify metric
		switch (metric) {
		case "LAA":
			excel = em.getLaa();
			break;
		case "ATFD":
			excel = em.getAtfd();
			break;
		case "LOC":
			excel = em.getLoc();
			break;
		case "CYCLO": 
			excel = em.getCyclo();
			break;	
		}
		
		Boolean check = null;
		
		//verify operator
		switch (operator) {
		case ">":
			check = excel > value;
			break;
		case ">=":
			check = excel >= value;
			break;			
		case "<":
			check = excel < value;
			break;
		case "<=":
			check = excel <= value;
			break;
		case "=":
			check = excel == value;
			break;
		}

		return check;
	}

	/**
	 * Validates the arguments passed in the parameters. Throws an exception if any of the arguments doesn't match criteria
	 * 
	 * @param m metric typed by the user
	 * @param o operator typed by the user
	 * @param value value typed by the user
	 */
	public static void validateArguments(String m, String o, Double value) {
		if(!(m.equals("LAA") || m.equals("ATFD") || m.equals("LOC") || m.equals("CYCLO")))
			throw new NullPointerException("Metrica não existente");
		if(!(o.equals(">") || o.equals(">=") || o.equals("<") || o.equals("<=") || o.equals("=")) )
			throw new NullPointerException("Comparador não suportado");
		
	}
	
}
