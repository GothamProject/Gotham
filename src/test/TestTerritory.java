package test;

import junit.framework.TestCase;
import model.FreeIntersection;
import model.Intersection;
import model.NotAdjacentIntersectionException;
import model.Territory;
import model.TerritoryAlreadyContainsIntersectionException;

import org.junit.Test;

/**
 * 
 * Tests for the {@link Territory} class
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class TestTerritory extends TestCase {

	/**
	 * Tests simple adding of {@link Intersection} into {@link Territory}
	 */
	@Test
	public void testAdd() {

		Territory territory = new Territory();
		int count = 5;

		for (int i = 1; i <= count; i++) {
			try {
				territory.add(new FreeIntersection(count, i));
			} catch (TerritoryAlreadyContainsIntersectionException e) {
				System.err.println(e.getMessage());
			} catch (NotAdjacentIntersectionException e) {
				System.err.println(e.getMessage());
			}
		}

		assertEquals(count, territory.getIntersectionsCount());

	}

	/**
	 * Tests that adding the same {@link Intersection} twice is impossible.
	 */
	@Test
	public void testAddIdenticals() {

		Territory territory = new Territory();
		int count = 5;

		for (int i = 1; i <= count; i++) {
			try {
				territory.add(new FreeIntersection());
			} catch (TerritoryAlreadyContainsIntersectionException e) {
				// System.err.println(e.getMessage());
			} catch (NotAdjacentIntersectionException e) {
				System.err.println(e.getMessage());
			}
		}

		assertEquals(1, territory.getIntersectionsCount());

	}

	/**
	 * Tests that <code>contains</code> methods are coherent
	 */
	@Test
	public void testContainsIntersection() {

		Territory territory = new Territory();
		int count = 5;

		for (int i = 1; i <= count; i++) {
			try {
				territory.add(new FreeIntersection(count, i));
			} catch (TerritoryAlreadyContainsIntersectionException e) {
				System.err.println(e.getMessage());
			} catch (NotAdjacentIntersectionException e) {
				System.err.println(e.getMessage());
			}
		}

		Intersection intersection = new FreeIntersection(count, count - 2);
		Intersection intersection2 = new FreeIntersection(count - 2, count);

		assertTrue(territory.containsIntersection(intersection));
		assertFalse(territory.containsIntersection(intersection2));

	}

	/**
	 * Tests that <code>contains</code> methods are coherent
	 */
	@Test
	public void testContainsIntersectionByCoordinates() {

		Territory territory = new Territory();
		int count = 5;

		for (int i = 1; i <= count; i++) {
			try {
				territory.add(new FreeIntersection(count, i));
			} catch (TerritoryAlreadyContainsIntersectionException e) {
				System.err.println(e.getMessage());
			} catch (NotAdjacentIntersectionException e) {
				System.err.println(e.getMessage());
			}
		}

		int xCoordinate = count;
		int yCoordinate = count - 2;
		int xCoordinate2 = count - 2;
		int yCoordinate2 = count;

		assertTrue(territory.containsIntersectionByCoordinates(xCoordinate,
				yCoordinate));
		assertFalse(territory.containsIntersectionByCoordinates(xCoordinate2,
				yCoordinate2));

	}
}
