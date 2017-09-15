package evochecker.prism;

public class Constraint extends Property {

	private final double constraint;
	
	private final double VIOLATION_CONSTANT=100; 
	
	public Constraint(boolean maximization, double constraint) {
		super(maximization);
		this.constraint = constraint;
	}
	
	
	public Constraint (Constraint clone) {
		this(clone.maximization, clone.constraint);
	}
	
	
	@Override
	public double evaluate(double result) {
		if ((maximization) &&
			(result < constraint))
				return ((result-constraint) * VIOLATION_CONSTANT);
		else if ( (!maximization) &&
				(result > constraint))
				return ((constraint-result) * VIOLATION_CONSTANT);
		else
			return 0;
	}

}
