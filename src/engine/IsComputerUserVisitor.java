package engine;

import model.ComputerUser;
import model.HumanUser;

public class IsComputerUserVisitor implements UserVisitor<Boolean> {

	@Override
	public Boolean visit(ComputerUser computer) {
		return true;
	}

	@Override
	public Boolean visit(HumanUser human) {
		return false;
	}

}
