package com.googlecode.prolog_cafe.lang;

/**
 * Choice point frame stack.<br>
 * The <code>CPFStack</code> class represents a stack of choice point frames.<br>
 * Each choice point frame has the following fields:
 * <ul>
 * <li><em>arguments</em>
 * <li><em>continuation goal</em>
 * <li><em>next clause</em>
 * <li><em>trail pointer</em>
 * <li><em>cut point</em>
 * <li><em>time stamp</em>
 * </ul>
 *
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public final class ChoicePointStack {
  /** Trail of the associated Prolog instance. */
  private final Trail trail;

  /** Top of the stack. */
  ChoicePointFrame top;

  /**
   * Current level/depth of the stack.
   * <p>
   * This matches the length of the chain stored in {@link #top}.
   */
  private int level;

  ChoicePointStack(Trail trail) {
    this.trail = trail;
    this.level = -1;
  }

  /**
   * Create a new choice point frame.
   *
   * @param entry <em>entry to save</em>
   */
  void push(ChoicePointFrame entry) {
    entry.prior = top;
    top = entry;
    level++;
    trail.timeStamp = entry.timeStamp;
  }

  /** Discards all choice points after the value of <code>i</code>. */
  public void cut(int i) {
    while (level > i) {
      top = top.prior;
      level--;
    }
    updateTrailTimeStamp();
  }

  /** Discards the top of choice points. */
  void delete() {
    top = top.prior;
    level--;
    updateTrailTimeStamp();
  }

  /** Discards all choice points. */
  void init() {
    top = null;
    level = -1;
    updateTrailTimeStamp();
  }

  private void updateTrailTimeStamp() {
    trail.timeStamp = top != null ? top.timeStamp : Long.MIN_VALUE;
  }

  /** Get the current top of the stack. */
  public int top() {
    return level;
  }

  /** Get the maximum number of choice points permitted on the stack. */
  public int max() {
    // Since the stack is represented as a linked list, there is no limit.
    return Integer.MAX_VALUE;
  }
}
