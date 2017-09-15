package evochecker.prism;

public class Objective extends Property {

	protected boolean isMaximization;

	
	public Objective(boolean maximization) {
		super(maximization);
		this.isMaximization = maximization;
	}
	
	
	public Objective (Objective clone) {
		this(clone.maximization);
	}


	@Override
	public double evaluate(double result) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
