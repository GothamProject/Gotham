package engine;

import model.ComputerUser;
import model.HumanUser;

public interface UserVisitor<T> {

	T visit(ComputerUser computer);

	T visit(HumanUser human);
}
