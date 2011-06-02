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
  /** Top of the stack. */
  ChoicePointFrame top;

  /**
   * Current level/depth of the stack.
   * <p>
   * This matches the length of the chain stored in {@link #top}.
   */
  private int level = -1;

  /**
   * Create a new choice point frame.
   *
   * @param entry <em>entry to save</em>
   */
  void push(ChoicePointFrame entry) {
    entry.prior = top;
    top = entry;
    level++;
  }

  /** Discards all choice points after the value of <code>i</code>. */
  public void cut(int i) {
    while (level > i) {
      top = top.prior;
      level--;
    }
  }

  /** Discards the top of choice points. */
  void delete() {
    top = top.prior;
    level--;
  }

  /** Discards all choice points. */
  void init() {
    top = null;
    level = -1;
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

  /** Returns the <em>time stamp</em> of current choice point frame. */
  public long getTimeStamp() {
    return top.timeStamp;
  }

  /** Shows the contents of this <code>CPFStack</code>. */
  public void show() {
    if (top == null) {
      System.out.println("{choice point stack is empty!}");
      return;
    }
    for (ChoicePointFrame e = top; e != null; e = e.prior) {
      System.out.println(e);
    }
  }
}
