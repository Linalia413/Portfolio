package lasers.backtracking;

import java.util.Collection;
import java.util.List;

/**
 * The representation of a single configuration for a puzzle.
 * The Backtracker depends on these routines in order to
 * solve a puzzle.  Therefore, all puzzles must implement this
 * interface.
 *
 * @author RIT CS
 * @author Jacob Harman
 * @author Sean Harmon
 */
public interface Configuration {
    /**
     * Get the collection of successors from the current one.
     *
     * @return All successors, valid and invalid
     */
    public Collection< Configuration > getSuccessors();

    /**
     * Is the current configuration valid or not?
     *
     * @return true if valid; false otherwise
     */
    public boolean isValid();

    /**
     * Is the current configuration a goal?
     * @return true if goal; false otherwise
     */
    public boolean isGoal();

    /**
     * Get the path to current configuration
     * @return list of configurations leading to current safe
     */
    public List<Configuration> getPath();
}