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
	private String compare;
	private double value;
	// LAA > 20
	public Rule(String metric, String compare, double value) {
		this.metric = metric;
		this.compare = compare;
		this.value = value;
	}
	
	
	public boolean check(ExcelMethod em) {
		double excel = 0;
		switch (metric) {
			case "LAA":
				
		
		}
		return false;
	}
	
}
