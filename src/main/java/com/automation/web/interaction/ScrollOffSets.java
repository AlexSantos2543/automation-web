package com.automation.web.interaction;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.function.IntSupplier;

class ScrollOffsets {

    private final IntSupplier top;
    private final IntSupplier left;

    /**
     *
     * @param top int to be set as top offset coordinate (y)
     * @param left int to be set as left offset coordinate (x)
     */
    public ScrollOffsets(int top, int left) {
        this.top = () -> top;
        this.left = () -> left;
    }

    /**
     * @param top addition of element's y coordinate and element's height to be set as top offset
     * coordinate (y)
     * @param left addition of element's x coordinate and element's width to be set as left offset
     * coordinate (x)
     */
    public ScrollOffsets(WebElement top, WebElement left) {
        this.top = () -> {
            if (top == null) {
                return 0;
            }
            Rectangle rect = top.getRect();
            return rect.y + rect.height;
        };
        this.left = () -> {
            if (left == null) {
                return 0;
            }
            Rectangle rect = left.getRect();
            return rect.x + rect.width;
        };
    }

    /**
     * @param top to be set as top offset coordinate (y)
     * @param left to be set as left offset coordinate (x)
     */
    public ScrollOffsets(IntSupplier top, IntSupplier left) {
        this.top = top;
        this.left = left;
    }

    /**
     * gets a top offset coordinate (y)
     *
     * @return y offset coordinate
     */
    public int getTop() {
        return top.getAsInt();
    }

    /**
     * gets a left offset coordinate (x)
     */
    public int getLeft() {
        return left.getAsInt();
    }
}

