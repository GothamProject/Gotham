package test;

import junit.framework.TestCase;
import model.BlackIntersection;
import model.FreeIntersection;
import model.Goban;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.IntersectionNotFoundInTerritoryException;
import model.Territories;
import model.Territory;
import model.TerritoryAlreadyContainsIntersectionException;
import model.WhiteIntersection;

import org.junit.Test;

import engine.IntersectionVisitor;
import engine.IsWhiteIntersectionVisitor;
import engine.SearchBlackTerritoriesVisitor;
import engine.SearchWhiteTerritoriesVisitor;
import game.IntersectionAlreadyOccupiedException;
import game.OutOfGobanException;
import game.SuicideException;

/**
 * 
 * Tests for the {@link Territories} class.
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class TestTerritories extends TestCase {

	/**
	 * Tests simple adding of {@link Territory} to the {@link Territories}
	 * class.
	 */
	@Test
	public void testAdd() {
		Territories territories = new Territories();
		try {
			territories.add(new FreeIntersection(1, 1));
		} catch (TerritoryAlreadyContainsIntersectionException e) {
		}
		territories.add(new Territory(new FreeIntersection(3, 3)));

		assertEquals(2, territories.getTerritoriesCount());
	}

	/**
	 * Tests getting a {@link Territory} from the {@link Territories}
	 */
	@Test
	public void testGet() {
		Territories territories = new Territories();
		Intersection intersection = new FreeIntersection(2, 2);
		Territory territory = new Territory(intersection);

		try {
			territories.add(territory);
			assertSame(territory,
					territories.getTerritoryByIntersection(intersection));
		} catch (IntersectionNotFoundInTerritoryException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Tests that {@link Territories} contains methods are coherent.
	 */
	@Test
	public void testContains() {
		Territories territories = new Territories();
		Intersection intersection = new FreeIntersection(2, 2);
		Territory territory = new Territory(intersection);

		territories.add(territory);
		assertTrue(territories.containsIntersection(intersection));
		assertTrue(territories.containsIntersectionByCoordinates(2, 2));

		assertFalse(territories
				.containsIntersection(new FreeIntersection(8, 7)));
		assertFalse(territories.containsIntersectionByCoordinates(4, 1));
	}

	/**
	 * Tests the jailing of Intersections
	 */
	@Test
	public void testJailed() {

		try {

			Goban goban = new Goban(13);

			goban.updateIntersection(new WhiteIntersection(5, 6));
			goban.updateIntersection(new WhiteIntersection(6, 6));
			goban.updateIntersection(new BlackIntersection(5, 5));
			goban.updateIntersection(new BlackIntersection(6, 5));
			goban.updateIntersection(new BlackIntersection(5, 7));
			goban.updateIntersection(new BlackIntersection(6, 7));
			goban.updateIntersection(new BlackIntersection(4, 6));

			IntersectionVisitor<Territories> visitor = new SearchWhiteTerritoriesVisitor();
			Territories territories = goban.accept(visitor);

			assertTrue(territories.containsIntersectionByCoordinates(5, 6));
			assertTrue(territories.containsIntersectionByCoordinates(6, 6));

			goban.updateIntersection(new BlackIntersection(7, 6));
			Territories territories2 = goban.accept(visitor);

			assertFalse(territories2.getTerritoryByIntersection(
					new FreeIntersection(5, 6)).accept(
					new IsWhiteIntersectionVisitor()));
			assertFalse(territories2.getTerritoryByIntersection(
					new FreeIntersection(6, 6)).accept(
					new IsWhiteIntersectionVisitor()));

		} catch (IncorrectGobanSizeException e) {
			System.err.println(e.getMessage());
		} catch (OutOfGobanException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionAlreadyOccupiedException e) {
			System.err.println(e.getMessage());
		} catch (SuicideException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionNotFoundInTerritoryException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Tests the merging of multiple {@link Territory} inside one
	 * {@link Territories}
	 */
	@Test
	public void testMerged() {

		try {

			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(5, 6));
			goban.updateIntersection(new BlackIntersection(6, 6));
			goban.updateIntersection(new BlackIntersection(5, 5));
			goban.updateIntersection(new BlackIntersection(6, 5));
			goban.updateIntersection(new BlackIntersection(5, 7));
			goban.updateIntersection(new BlackIntersection(6, 7));
			goban.updateIntersection(new BlackIntersection(4, 6));
			goban.updateIntersection(new BlackIntersection(7, 6));

			goban.updateIntersection(new BlackIntersection(3, 6));
			goban.updateIntersection(new BlackIntersection(2, 6));
			goban.updateIntersection(new BlackIntersection(2, 7));

			IntersectionVisitor<Territories> visitor = new SearchBlackTerritoriesVisitor();
			Territories territories = goban.accept(visitor);

			assertEquals(1, territories.getTerritoriesCount());

		} catch (IncorrectGobanSizeException e) {
			System.err.println(e.getMessage());
		} catch (OutOfGobanException e) {
			System.err.println(e.getMessage());
		} catch (IntersectionAlreadyOccupiedException e) {
			System.err.println(e.getMessage());
		} catch (SuicideException e) {
			System.err.println(e.getMessage());
		}
	}
}
