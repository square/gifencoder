package com.squareup.gifencoder;

/**
 * Indicates the way in which the graphic is to be treated after being displayed.
 */
public enum DisposalMethod {
  /**
   * No disposal specified. The decoder is not required to take any action.
   */
  UNSPECIFIED,

  /**
   * Do not dispose. The graphic is to be left in place.
   */
  DO_NOT_DISPOSE,

  /**
   * Restore to background color. The area used by the graphic must be restored to the background
   * color.
   */
  RESTORE_TO_BACKGROUND,

  /**
   * Restore to previous. The decoder is required to restore the area overwritten by the graphic
   * with what was there prior to rendering the graphic.
   */
  RESTORE_TO_PREVIOUS,
}
