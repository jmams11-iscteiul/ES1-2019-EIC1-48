/**
 * 
 */
package badcode;

/**
 * @author Ricardo, Jo�o M., Jo�o R., Miguel.
 *
 */
public class Rule {

	private String metric;
	private String operator;
	private double value;
	// LAA > 20
	public Rule(String metric, String operator, double value) {
		if(metric.equals("LAA") || metric.equals("ATFD") || metric.equals("LOC") || metric.equals("CYCLO") )
			this.metric = metric;
		else
			throw new NullPointerException("Metrica n�o existente");
		if(operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=") || operator.equals("==") )
			this.operator = operator;
		else
			throw new NullPointerException("Comparador n�o suportado");
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
		default: 
			return (Boolean) null;		
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
			throw new NullPointerException("Metrica n�o existente");
		if(!(o.equals(">") || o.equals(">=") || o.equals("<") || o.equals("<=") || o.equals("==")) )
			throw new NullPointerException("Comparador n�o suportado");
		
	}
	
}