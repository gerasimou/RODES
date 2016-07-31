package _main;

import java.util.Arrays;

import evochecker.EvoChecker;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;

public class Experiment {

	public Experiment() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			String tolerance = Utility.getProperty("TOLERANCES");
		
			String tolerances[] = tolerance.split(",");
			
			System.out.println(Arrays.toString(tolerances));
			
			for (String t : tolerances){
					Utility.setProperty("TOLERANCE", t);
					System.out.println(Utility.getProperty("TOLERANCE"));
					EvoChecker.main(null);
			}
		} catch (EvoCheckerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}

}
