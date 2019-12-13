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

	public Rule(String metric, String operator, double value) {
		this.metric = metric;
		this.operator = operator;
		this.value = value;
	}


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

	public static void validateArguments(String m, String o, Double value) {
		if(!(m.equals("LAA") || m.equals("ATFD") || m.equals("LOC") || m.equals("CYCLO")))
			throw new NullPointerException("Metrica não existente");
		if(!(o.equals(">") || o.equals(">=") || o.equals("<") || o.equals("<=") || o.equals("=")) )
			throw new NullPointerException("Comparador não suportado");
		
	}
	
}
