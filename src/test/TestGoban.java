package test;

import junit.framework.TestCase;
import model.BlackIntersection;
import model.Goban;
import model.IncorrectGobanSizeException;
import model.Intersection;
import model.Territories;
import model.TerritoryAlreadyContainsIntersectionException;
import model.WhiteIntersection;

import org.junit.Test;

import engine.IntersectionOccupiedVisitor;
import engine.IntersectionVisitor;
import engine.IsBlackIntersectionVisitor;
import engine.IsWhiteIntersectionVisitor;
import engine.SearchBlackTerritoriesVisitor;
import game.IntersectionAlreadyOccupiedException;
import game.IntersectionNotOccupiedException;
import game.OffensiveSuicideException;
import game.OutOfGobanException;
import game.SuicideException;

/**
 * 
 * Tests for the {@link Goban} class
 * 
 * @author Team AFK
 * @version 1.0
 * 
 */

public class TestGoban extends TestCase {

	/**
	 * Tests correct creation of gobans, and raising of
	 * IncorrectGobanSizeException
	 * 
	 * @see {@link IncorrectGobanSizeException}
	 */
	@Test
	public void testGoban() {
		try {
			Goban goban = new Goban(19);
			assertEquals(19, goban.getSize());

			goban = new Goban(13);
			assertEquals(13, goban.getSize());

			goban = new Goban(9);
			assertEquals(9, goban.getSize());

			goban = new Goban(10);
			fail("IncorrectGobanSizeException not thrown");

		} catch (IncorrectGobanSizeException e) {
		}
	}

	/**
	 * Tests IntersectionOccupiedVisitor
	 * 
	 * @see {@link IntersectionOccupiedVisitor}
	 */
	@Test
	public void testOccupiedVisitor() {
		try {
			Goban goban = new Goban(13);
			IntersectionOccupiedVisitor visitor = new IntersectionOccupiedVisitor();

			assertFalse(goban.getIntersectionByCoordinates(2, 2)
					.accept(visitor));

			Intersection intersection = new BlackIntersection(2, 2);
			goban.updateIntersection(intersection);

			assertTrue(goban.getIntersectionByCoordinates(2, 2).accept(visitor));

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

	/**
	 * Tests raising of OutOfGobanException
	 * 
	 * @see {@link OutOfGobanException}
	 */
	@Test
	public void testOutOfGoban() {
		try {
			Goban goban = new Goban(13);

			Intersection intersection = new BlackIntersection(20, 2);
			goban.updateIntersection(intersection);

			fail("OutOfGobanException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		}

	}

	/**
	 * Tests if an Intersection already occupied cannot be overwritten
	 * 
	 * @see {@link IntersectionAlreadyOccupiedException}
	 */
	@Test
	public void testAlreadyOccupied() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new WhiteIntersection(8, 2));

			fail("IntersectionAlreadyOccupiedException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		}

	}

	/**
	 * Tests if the suicide situation is well managed by raising a
	 * SuicideException
	 * 
	 * @see {@link SuicideException}
	 */
	@Test
	public void testSuicide() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));
			goban.updateIntersection(new BlackIntersection(6, 2));
			goban.updateIntersection(new BlackIntersection(8, 3));
			goban.updateIntersection(new BlackIntersection(6, 3));
			goban.updateIntersection(new BlackIntersection(8, 4));
			goban.updateIntersection(new BlackIntersection(7, 4));
			goban.updateIntersection(new BlackIntersection(6, 4));

			goban.updateIntersection(new WhiteIntersection(7, 3));

			fail("SuicideException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		}

	}

	/**
	 * Tests if suicide situation is well disabled when it's an offensive
	 * suicide. In this case, the rules accept the suicide and capture of the
	 * enemy.
	 * 
	 * @see {@link OffensiveSuicideException}
	 */
	@Test
	public void testSuicideOffensive() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(6, 6));
			goban.updateIntersection(new BlackIntersection(6, 8));
			goban.updateIntersection(new BlackIntersection(5, 7));
			goban.updateIntersection(new BlackIntersection(7, 7));

			goban.updateIntersection(new WhiteIntersection(4, 7));
			goban.updateIntersection(new WhiteIntersection(5, 6));
			goban.updateIntersection(new WhiteIntersection(5, 8));

			goban.updateIntersection(new WhiteIntersection(6, 7));

			System.out.println(goban.toString());

			fail("OffensiveSuicideException should have been raised.");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
			fail("SuicideException was raised, but should be allowed (Offensive suicide).");
		}
	}

	/**
	 * Tests the capture of intersections and their removal from the goban.
	 */
	@Test
	public void testCapture() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.captureIntersection(new WhiteIntersection(8, 2));

			IntersectionVisitor<Boolean> visitor = new IntersectionOccupiedVisitor();
			assertFalse(goban.getIntersectionByCoordinates(8, 2)
					.accept(visitor));

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		}

	}

	/**
	 * Tests that capture is not possible out of the goban
	 */
	@Test
	public void testCaptureOutOfGoban() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.captureIntersection(new WhiteIntersection(28, 2));

			fail("OutOfGobanException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		}

	}

	/**
	 * Tests that capture is not possible if the intersection is empty
	 */
	@Test
	public void testCaptureNotOccupied() {
		try {
			Goban goban = new Goban(13);

			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.captureIntersection(new WhiteIntersection(6, 2));

			fail("IntersectionNotOccupiedException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		}

	}

	/**
	 * Tests if a whole territory is well captured
	 */
	@Test
	public void testCaptureTerritory() {
		try {
			Goban goban = new Goban(13);

			/*
			 * Add a few territories
			 */
			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.updateIntersection(new WhiteIntersection(6, 2));

			/*
			 * Capture black territories
			 */
			IntersectionVisitor<Territories> visitor = new SearchBlackTerritoriesVisitor();
			goban.accept(visitor);
			Territories territories = visitor.getOutput();

			goban.captureIntersections(territories);

			/*
			 * Check if captured
			 */
			assertFalse(goban.getIntersectionByCoordinates(8, 2).accept(
					new IntersectionOccupiedVisitor()));
			assertFalse(goban.getIntersectionByCoordinates(7, 2).accept(
					new IntersectionOccupiedVisitor()));
			assertTrue(goban.getIntersectionByCoordinates(6, 2).accept(
					new IntersectionOccupiedVisitor()));

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		}

	}

	/**
	 * Tests that capturing an intersection out of the goban is not possible
	 */
	@Test
	public void testCaptureTerritoryOutOfGoban() {
		try {
			Goban goban = new Goban(13);

			/*
			 * Add a few territories
			 */
			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.updateIntersection(new WhiteIntersection(6, 2));

			/*
			 * Capture black territories
			 */
			IntersectionVisitor<Territories> visitor = new SearchBlackTerritoriesVisitor();
			goban.accept(visitor);
			Territories territories = visitor.getOutput();

			/*
			 * Add OutOfGobanException
			 */
			territories.add(new BlackIntersection(28, 4));

			goban.captureIntersections(territories);

			fail("OutOfGobanException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		} catch (TerritoryAlreadyContainsIntersectionException e) {
		}

	}

	/**
	 * Tests that capturing an intersection not occupied is impossible
	 */
	@Test
	public void testCaptureTerritoryOccupied() {
		try {
			Goban goban = new Goban(13);

			/*
			 * Add a few territories
			 */
			goban.updateIntersection(new BlackIntersection(8, 2));
			goban.updateIntersection(new BlackIntersection(7, 2));

			goban.updateIntersection(new WhiteIntersection(6, 2));

			/*
			 * Capture black territories
			 */
			IntersectionVisitor<Territories> visitor = new SearchBlackTerritoriesVisitor();
			goban.accept(visitor);
			Territories territories = visitor.getOutput();

			/*
			 * Add IntersectionNotOccupiedException
			 */
			territories.add(new BlackIntersection(8, 4));

			goban.captureIntersections(territories);

			fail("IntersectionNotOccupiedException not thrown");

		} catch (IncorrectGobanSizeException e) {
		} catch (OutOfGobanException e) {
		} catch (IntersectionAlreadyOccupiedException e) {
		} catch (SuicideException e) {
		} catch (IntersectionNotOccupiedException e) {
		} catch (TerritoryAlreadyContainsIntersectionException e) {
		}

	}

	/**
	 * Tests the {@link WhiteIntersectionVisitor}
	 */
	@Test
	public void testWhiteIntersectionVisitor() {

		Intersection whiteIntersection = new WhiteIntersection(2, 7);
		Intersection blackIntersection = new BlackIntersection(3, 6);
		Intersection intersection = new WhiteIntersection(1, 1);
		try {
			Goban goban = new Goban(19);

			goban.updateIntersection(intersection);
			goban.updateIntersection(whiteIntersection);
			goban.updateIntersection(blackIntersection);

			IntersectionVisitor<Boolean> whiteVisitor = new IsWhiteIntersectionVisitor();

			assertTrue(goban.getIntersectionByCoordinates(2, 7).accept(
					whiteVisitor));
			assertFalse(goban.getIntersectionByCoordinates(3, 6).accept(
					whiteVisitor));
			assertTrue(goban.getIntersectionByCoordinates(1, 1).accept(
					whiteVisitor));

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

	/**
	 * Tests the {@link BlackIntersectionVisitor}
	 */
	public void testBlackIntersectionVisitor() {
		Boolean result;

		Intersection whiteIntersection = new WhiteIntersection(2, 7);
		Intersection blackIntersection = new BlackIntersection(3, 6);
		Intersection intersection = new WhiteIntersection(1, 1);
		try {

			Goban goban = new Goban(19);

			goban.updateIntersection(intersection);
			goban.updateIntersection(whiteIntersection);
			goban.updateIntersection(blackIntersection);

			IntersectionVisitor<Boolean> blackVisitor = new IsBlackIntersectionVisitor();
			result = goban.getIntersectionByCoordinates(2, 7).accept(
					blackVisitor);
			assertFalse(result);
			result = goban.getIntersectionByCoordinates(3, 6).accept(
					blackVisitor);
			assertTrue(result);
			result = goban.getIntersectionByCoordinates(1, 1).accept(
					blackVisitor);
			assertFalse(result);

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
